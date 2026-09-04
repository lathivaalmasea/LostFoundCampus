package Model.Claim;

import Model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOClaimRequest implements InterfaceDAOClaimRequest {

    private static final String SELECT_COLS =
        "SELECT cr.id, cr.barang_id, cr.requester_user_id, cr.status, "
        + "cr.alasan_klaim, cr.alasan_review, "
        + "cr.requested_at, cr.reviewed_at, cr.reviewed_by_user_id, "
        + "cr.user_notified_at, "
        + "b.nama_barang, b.kategori, b.status AS barang_status, "
        + "u.nama AS requester_nama, u.username AS requester_username, "
        + "reviewer.nama AS reviewer_nama, "
        + "pelapor.nama AS pelapor_nama, pelapor.no_telp AS pelapor_no_telp, pelapor.role AS pelapor_role "
        + "FROM claim_requests cr "
        + "JOIN barang b ON b.id = cr.barang_id "
        + "JOIN users u ON u.id = cr.requester_user_id "
        + "LEFT JOIN users reviewer ON reviewer.id = cr.reviewed_by_user_id "
        + "LEFT JOIN users pelapor ON pelapor.id = b.user_id ";

    private final Connection connection;

    public DAOClaimRequest() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void insert(ModelClaimRequest claimRequest) {
        String query = "INSERT INTO claim_requests (barang_id, requester_user_id, status, alasan_klaim) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, claimRequest.getBarangId());
            ps.setInt(2, claimRequest.getRequesterUserId());
            ps.setString(3, "Pending");
            ps.setString(4, claimRequest.getAlasanKlaim());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean existsPendingRequest(int barangId, int requesterUserId) {
        String query = "SELECT 1 FROM claim_requests WHERE barang_id = ? "
                + "AND requester_user_id = ? AND status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, barangId);
            ps.setInt(2, requesterUserId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /** Jumlah claim berstatus Pending - dipakai admin untuk badge notifikasi. */
    public int countPendingRequests() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM claim_requests WHERE status = 'Pending'")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Jumlah claim Pending yang BELUM pernah dilihat admin.
     * Badge di DashboardAdmin hanya menampilkan angka ini.
     */
    public int countNewPendingRequests() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM claim_requests " +
                "WHERE status = 'Pending' AND admin_viewed_at IS NULL")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Tandai semua claim Pending yang belum dilihat sebagai sudah dilihat admin.
     * Dipanggil saat admin membuka halaman Manajemen Claim.
     */
    public void markAllPendingAdminViewed() {
        String query = "UPDATE claim_requests SET admin_viewed_at = CURRENT_TIMESTAMP " +
                       "WHERE status = 'Pending' AND admin_viewed_at IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ModelClaimRequest> getPendingRequests() {
        return getRequestsByQuery(SELECT_COLS
                + "WHERE cr.status = 'Pending' ORDER BY cr.requested_at ASC");
    }

    /**
     * Mengambil SEMUA claim request (Pending, Approved, Rejected).
     * Digunakan oleh ViewClaimAdmin untuk menampilkan riwayat.
     */
    public List<ModelClaimRequest> getAllRequests() {
        return getRequestsByQuery(SELECT_COLS + "ORDER BY cr.requested_at DESC");
    }

    /**
     * Mengambil semua claim request milik user tertentu (sebagai pemohon).
     * Digunakan untuk menampilkan notifikasi status klaim kepada user.
     */
    public List<ModelClaimRequest> getClaimsByUserId(int userId) {
        String query = SELECT_COLS
                + "WHERE cr.requester_user_id = ? ORDER BY cr.requested_at DESC";
        List<ModelClaimRequest> requests = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return requests;
    }

    @Override
    public List<ModelClaimRequest> getPendingRequestsByBarang(int barangId) {
        String query = SELECT_COLS
                + "WHERE cr.status = 'Pending' AND cr.barang_id = ? "
                + "ORDER BY cr.requested_at ASC";
        List<ModelClaimRequest> requests = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, barangId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return requests;
    }

    @Override
    public void approveRequest(int requestId, int reviewedByUserId) {
        approveRequest(requestId, reviewedByUserId, null);
    }

    @Override
    public void approveRequest(int requestId, int reviewedByUserId, String alasanReview) {
        try {
            connection.setAutoCommit(false);

            int barangId = 0, requesterUserId = 0;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT barang_id, requester_user_id FROM claim_requests "
                    + "WHERE id = ? AND status = 'Pending'")) {
                ps.setInt(1, requestId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    barangId = rs.getInt("barang_id");
                    requesterUserId = rs.getInt("requester_user_id");
                }
            }

            if (barangId == 0 || requesterUserId == 0) {
                connection.rollback();
                return;
            }

            // Update status barang -> Sudah Diklaim
            approveBarangClaim(barangId, requesterUserId);

            // Approve request yang dipilih
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE claim_requests SET status = 'Approved', "
                    + "reviewed_by_user_id = ?, alasan_review = ?, "
                    + "reviewed_at = CURRENT_TIMESTAMP WHERE id = ?")) {
                ps.setInt(1, reviewedByUserId);
                ps.setString(2, alasanReview);
                ps.setInt(3, requestId);
                ps.executeUpdate();
            }

            // Tolak semua request lain untuk barang yang sama
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE claim_requests SET status = 'Rejected', "
                    + "reviewed_by_user_id = ?, alasan_review = 'Klaim lain telah disetujui', "
                    + "reviewed_at = CURRENT_TIMESTAMP "
                    + "WHERE barang_id = ? AND status = 'Pending' AND id <> ?")) {
                ps.setInt(1, reviewedByUserId);
                ps.setInt(2, barangId);
                ps.setInt(3, requestId);
                ps.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { System.out.println(ex.getMessage()); }
            System.out.println(e.getMessage());
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { System.out.println(e.getMessage()); }
        }
    }

    public void rejectRequest(int requestId, int reviewedByUserId) {
        rejectRequest(requestId, reviewedByUserId, null);
    }

    public void rejectRequest(int requestId, int reviewedByUserId, String alasanReview) {
        String query = "UPDATE claim_requests SET status = 'Rejected', "
                + "reviewed_by_user_id = ?, alasan_review = ?, "
                + "reviewed_at = CURRENT_TIMESTAMP "
                + "WHERE id = ? AND status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, reviewedByUserId);
            ps.setString(2, alasanReview);
            ps.setInt(3, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void manualClaim(int barangId, int requesterUserId, int reviewedByUserId) {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO claim_requests (barang_id, requester_user_id, "
                    + "status, alasan_klaim, reviewed_by_user_id, reviewed_at) "
                    + "VALUES (?, ?, 'Approved', 'Klaim manual oleh admin', ?, CURRENT_TIMESTAMP)")) {
                ps.setInt(1, barangId);
                ps.setInt(2, requesterUserId);
                ps.setInt(3, reviewedByUserId);
                ps.executeUpdate();
            }

            approveBarangClaim(barangId, requesterUserId);

            // Tolak request pending lain untuk barang ini
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE claim_requests SET status = 'Rejected', "
                    + "reviewed_by_user_id = ?, alasan_review = 'Klaim manual oleh admin', "
                    + "reviewed_at = CURRENT_TIMESTAMP "
                    + "WHERE barang_id = ? AND status = 'Pending'")) {
                ps.setInt(1, reviewedByUserId);
                ps.setInt(2, barangId);
                ps.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { System.out.println(ex.getMessage()); }
            System.out.println(e.getMessage());
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { System.out.println(e.getMessage()); }
        }
    }

    private void approveBarangClaim(int barangId, int requesterUserId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE barang SET status_claim = 'Sudah Diklaim', "
                + "claimed_by_user_id = ? WHERE id = ?")) {
            ps.setInt(1, requesterUserId);
            ps.setInt(2, barangId);
            ps.executeUpdate();
        }
    }

    private List<ModelClaimRequest> getRequestsByQuery(String query) {
        List<ModelClaimRequest> requests = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return requests;
    }

    private ModelClaimRequest mapRequest(ResultSet rs) throws SQLException {
        ModelClaimRequest r = new ModelClaimRequest();
        r.setId(rs.getInt("id"));
        r.setBarangId(rs.getInt("barang_id"));
        r.setRequesterUserId(rs.getInt("requester_user_id"));
        r.setStatus(rs.getString("status"));
        r.setAlasanKlaim(rs.getString("alasan_klaim"));
        r.setAlasanReview(rs.getString("alasan_review"));
        r.setRequestedAt(rs.getString("requested_at"));
        r.setReviewedAt(rs.getString("reviewed_at"));
        r.setReviewedByUserId(rs.getInt("reviewed_by_user_id"));
        r.setBarangName(rs.getString("nama_barang"));
        r.setBarangCategory(rs.getString("kategori"));
        r.setBarangStatus(rs.getString("barang_status"));
        r.setRequesterName(rs.getString("requester_nama"));
        r.setRequesterUsername(rs.getString("requester_username"));
        r.setReviewerName(rs.getString("reviewer_nama"));
        r.setUserNotifiedAt(rs.getString("user_notified_at"));
        r.setPelaporNama(rs.getString("pelapor_nama"));
        r.setPelaporNoTelp(rs.getString("pelapor_no_telp"));
        r.setPelaporRole(rs.getString("pelapor_role"));
        return r;
    }

    /**
     * Tandai kumpulan claim request sudah dilihat notifikasinya oleh user.
     * Dipanggil setelah dialog notifikasi ditutup.
     */
    public void markNotified(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        String placeholders = String.join(",", ids.stream().map(i -> "?").toArray(String[]::new));
        String query = "UPDATE claim_requests SET user_notified_at = CURRENT_TIMESTAMP "
                + "WHERE id IN (" + placeholders + ") AND user_notified_at IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

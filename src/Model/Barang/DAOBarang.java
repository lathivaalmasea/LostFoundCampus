/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Barang;

import Model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Ivaa
 */
public class DAOBarang implements InterfaceDAOBarang {
    private static final String SELECT_BARANG_COLUMNS = "SELECT b.id, "
            + "b.nama_barang, b.kategori, b.deskripsi, b.lokasi, b.status, "
            + "b.status_claim, b.user_id, b.claimed_by_user_id, b.created_at, "
            + "owner.nama AS owner_nama, claimer.nama AS claimed_by_nama, "
            + "COALESCE(SUM(CASE WHEN cr.status = 'Pending' THEN 1 ELSE 0 END),0) "
            + "AS pending_claim_count "
            + "FROM barang b "
            + "JOIN users owner ON owner.id = b.user_id "
            + "LEFT JOIN users claimer ON claimer.id = b.claimed_by_user_id "
            + "LEFT JOIN claim_requests cr ON cr.barang_id = b.id ";
 
    private static final String SELECT_BARANG_GROUP_BY =
            " GROUP BY b.id, b.nama_barang, b.kategori, b.deskripsi, b.lokasi, "
            + "b.status, b.status_claim, b.user_id, b.claimed_by_user_id, "
            + "b.created_at, owner.nama, claimer.nama ";
 
    Connection connection;
 
    public DAOBarang(){
        connection = DatabaseConnection.getConnection();
    }
 
    @Override
    public void insert(ModelBarang barang) {
        try {
            String query = "INSERT INTO barang (nama_barang, kategori, "
                    + "deskripsi, lokasi, status, status_claim, user_id) "
                    + "VALUES (?,?,?,?,?,?,?)";
 
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, barang.getNamaBarang());
            ps.setString(2, barang.getKategori());
            ps.setString(3, barang.getDeskripsi());
            ps.setString(4, barang.getLokasi());
            ps.setString(5, barang.getStatus());
            ps.setString(6, barang.getStatusClaim());
            ps.setInt(7, barang.getUserId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    @Override
    public void update(ModelBarang barang) {
        try {
            String query = "UPDATE barang SET nama_barang=?, kategori=?,"
                    + "deskripsi=?, lokasi=?, status=?, status_claim=? "
                    + "WHERE id=?";
 
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, barang.getNamaBarang());
            ps.setString(2, barang.getKategori());
            ps.setString(3, barang.getDeskripsi());
            ps.setString(4, barang.getLokasi());
            ps.setString(5, barang.getStatus());
            ps.setString(6, barang.getStatusClaim());
            ps.setInt(7, barang.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    @Override
    public void delete(int id) {
        try {
            String query = "DELETE FROM barang WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    @Override
    public List<ModelBarang> getAll() {
        String query = SELECT_BARANG_COLUMNS + SELECT_BARANG_GROUP_BY
                + " ORDER BY b.id DESC";
        return getBarangByQuery(query);
    }
 
    @Override
    public List<ModelBarang> getAllByUserId(int userId) {
        String query = SELECT_BARANG_COLUMNS + " WHERE b.user_id = ?"
                + SELECT_BARANG_GROUP_BY + " ORDER BY b.id DESC";
        return getBarangByQuery(query, userId);
    }
 
    @Override
    public List<ModelBarang> search(String keyword) {
        String cari = "%" + keyword + "%";
        String query = SELECT_BARANG_COLUMNS
                + " WHERE b.nama_barang LIKE ? OR b.kategori LIKE ? "
                + "OR b.lokasi LIKE ?" + SELECT_BARANG_GROUP_BY
                + " ORDER BY b.id DESC";
        return getBarangByQuery(query, cari, cari, cari);
    }
 
    @Override
    public List<ModelBarang> searchByUserId(int userId, String keyword) {
        String cari = "%" + keyword + "%";
        String query = SELECT_BARANG_COLUMNS
                + " WHERE b.user_id = ? AND (b.nama_barang LIKE ? "
                + "OR b.kategori LIKE ? OR b.lokasi LIKE ?)"
                + SELECT_BARANG_GROUP_BY + " ORDER BY b.id DESC";
        return getBarangByQuery(query, userId, cari, cari, cari);
    }
 
    @Override
    public ModelBarang getById(int id) {
        ModelBarang barang = null;
        try {
            String query = SELECT_BARANG_COLUMNS + " WHERE b.id=?"
                    + SELECT_BARANG_GROUP_BY;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                barang = mapBarang(rs);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return barang;
    }
 
    @Override
    public int getTotalBarang() {
        return getCountByQuery("SELECT COUNT(*) FROM barang");
    }
 
    @Override
    public int getTotalByStatus(String status) {
        return getCountByQuery("SELECT COUNT(*) FROM barang WHERE status = ?", status);
    }
 
    // total berdasarkan status_claim
    public int getTotalByStatusClaim(String statusClaim) {
        return getCountByQuery(
            "SELECT COUNT(*) FROM barang WHERE status_claim = ?", statusClaim);
    }
 
    // total barang milik user tertentu 
    public int getTotalByUserId(int userId) {
        return getCountByQuery(
            "SELECT COUNT(*) FROM barang WHERE user_id = ?", userId);
    }
 
    // total barang milik user berdasarkan status
    public int getTotalByUserIdAndStatus(int userId, String status) {
        return getCountByQuery(
            "SELECT COUNT(*) FROM barang WHERE user_id = ? AND status = ?",
            userId, status);
    }
 
    // total claim yang disetujui untuk user tertentu (sebagai claimer)
    public int getTotalApprovedClaimByUserId(int userId) {
        return getCountByQuery(
            "SELECT COUNT(*) FROM barang WHERE claimed_by_user_id = ? "
            + "AND status_claim = 'Sudah Diklaim'", userId);
    }
 
    @Override
    public List<ModelBarang> getReturnedBarang() {
        String query = SELECT_BARANG_COLUMNS + " WHERE b.status_claim = ?"
                + SELECT_BARANG_GROUP_BY + " ORDER BY b.id DESC";
        return getBarangByQuery(query, "Sudah Ditemukan");
    }
 
    private List<ModelBarang> getBarangByQuery(String query, Object... parameters) {
        List<ModelBarang> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapBarang(rs));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
 
    private int getCountByQuery(String query, Object... parameters) {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
 
    private ModelBarang mapBarang(ResultSet rs) throws SQLException {
        ModelBarang barang = new ModelBarang();
        barang.setId(rs.getInt("id"));
        barang.setNamaBarang(rs.getString("nama_barang"));
        barang.setKategori(rs.getString("kategori"));
        barang.setDeskripsi(rs.getString("deskripsi"));
        barang.setLokasi(rs.getString("lokasi"));
        barang.setStatus(rs.getString("status"));
        barang.setStatusClaim(rs.getString("status_claim"));
        barang.setUserId(rs.getInt("user_id"));
        barang.setClaimedByUserId(rs.getInt("claimed_by_user_id"));
        barang.setOwnerName(rs.getString("owner_nama"));
        barang.setClaimedByName(rs.getString("claimed_by_nama"));
        barang.setPendingClaimCount(rs.getInt("pending_claim_count"));
        barang.setCreatedAt(rs.getString("created_at"));
        return barang;
    }

    /**
     * Pelapor menarik laporannya sendiri.
     * Hanya diizinkan jika status_claim masih 'Belum Diklaim' dan tidak ada pending request.
     * Set status_claim = 'Ditarik' sehingga tidak muncul sebagai aktif tapi data tetap ada.
     */
    public boolean tarikLaporan(int barangId, int userId) {
        try {
            String query = "UPDATE barang SET status_claim = 'Ditarik' "
                    + "WHERE id = ? AND user_id = ? AND status_claim = 'Belum Diklaim'";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, barangId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
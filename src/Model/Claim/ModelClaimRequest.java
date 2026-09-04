package Model.Claim;

public class ModelClaimRequest {

    private int id;
    private int barangId;
    private int requesterUserId;
    private String status;
    private String alasanKlaim;   // alasan dari user saat mengajukan
    private String alasanReview;  // alasan dari admin saat approve/reject
    private String requestedAt;
    private String reviewedAt;
    private int reviewedByUserId;
    private String barangName;
    private String barangCategory;
    private String barangStatus;  // 'Hilang' atau 'Ditemukan' - untuk menentukan konteks klaim
    private String requesterName;
    private String requesterUsername;
    private String reviewerName;
    private String userNotifiedAt;  // NULL = belum dilihat user, terisi = sudah dilihat

    // Info kontak pelapor barang (user yang melaporkan hilang/ditemukan)
    // Digunakan untuk info serah terima setelah klaim disetujui
    private String pelaporNama;
    private String pelaporNoTelp;   // null jika pelapor adalah admin
    private String pelaporRole;     // 'admin' atau 'user'

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBarangId() { return barangId; }
    public void setBarangId(int barangId) { this.barangId = barangId; }

    public int getRequesterUserId() { return requesterUserId; }
    public void setRequesterUserId(int requesterUserId) { this.requesterUserId = requesterUserId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlasanKlaim() { return alasanKlaim; }
    public void setAlasanKlaim(String alasanKlaim) { this.alasanKlaim = alasanKlaim; }

    public String getAlasanReview() { return alasanReview; }
    public void setAlasanReview(String alasanReview) { this.alasanReview = alasanReview; }

    public String getRequestedAt() { return requestedAt; }
    public void setRequestedAt(String requestedAt) { this.requestedAt = requestedAt; }

    public String getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(String reviewedAt) { this.reviewedAt = reviewedAt; }

    public int getReviewedByUserId() { return reviewedByUserId; }
    public void setReviewedByUserId(int reviewedByUserId) { this.reviewedByUserId = reviewedByUserId; }

    public String getBarangName() { return barangName; }
    public void setBarangName(String barangName) { this.barangName = barangName; }

    public String getBarangCategory() { return barangCategory; }
    public void setBarangCategory(String barangCategory) { this.barangCategory = barangCategory; }

    public String getBarangStatus() { return barangStatus; }
    public void setBarangStatus(String barangStatus) { this.barangStatus = barangStatus; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public String getRequesterUsername() { return requesterUsername; }
    public void setRequesterUsername(String requesterUsername) { this.requesterUsername = requesterUsername; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getUserNotifiedAt() { return userNotifiedAt; }
    public void setUserNotifiedAt(String userNotifiedAt) { this.userNotifiedAt = userNotifiedAt; }

    public boolean isUserNotified() { return userNotifiedAt != null; }

    public String getPelaporNama() { return pelaporNama; }
    public void setPelaporNama(String pelaporNama) { this.pelaporNama = pelaporNama; }

    public String getPelaporNoTelp() { return pelaporNoTelp; }
    public void setPelaporNoTelp(String pelaporNoTelp) { this.pelaporNoTelp = pelaporNoTelp; }

    public String getPelaporRole() { return pelaporRole; }
    public void setPelaporRole(String pelaporRole) { this.pelaporRole = pelaporRole; }

    /**
     * Mengembalikan label peran pemohon berdasarkan status barang.
     * Jika barang 'Ditemukan' -> pemohon adalah PEMILIK yang ingin mengambil barangnya.
     * Jika barang 'Hilang'    -> pemohon adalah PENEMU yang ingin menyerahkan barang.
     */
    public String getRolePemohon() {
        if ("Hilang".equalsIgnoreCase(barangStatus)) {
            return "Penemu";
        }
        return "Pemilik";
    }
}

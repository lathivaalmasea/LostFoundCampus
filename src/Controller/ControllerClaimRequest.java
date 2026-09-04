package Controller;

import Model.Claim.DAOClaimRequest;
import Model.Claim.ModelClaimRequest;
import java.util.List;

public class ControllerClaimRequest {

    private final DAOClaimRequest daoClaimRequest;

    public ControllerClaimRequest() {
        this.daoClaimRequest = new DAOClaimRequest();
    }

    public void insert(ModelClaimRequest claimRequest) {
        daoClaimRequest.insert(claimRequest);
    }

    public boolean existsPendingRequest(int barangId, int requesterUserId) {
        return daoClaimRequest.existsPendingRequest(barangId, requesterUserId);
    }

    /** Jumlah klaim pending — untuk badge notifikasi di dashboard admin. */
    public int countPendingRequests() {
        return daoClaimRequest.countPendingRequests();
    }

    /** Jumlah klaim Pending yang BELUM dilihat admin — untuk badge notifikasi. */
    public int countNewPendingRequests() {
        return daoClaimRequest.countNewPendingRequests();
    }

    /** Tandai semua Pending belum-dilihat sebagai sudah dilihat admin. */
    public void markAllPendingAdminViewed() {
        daoClaimRequest.markAllPendingAdminViewed();
    }

    public List<ModelClaimRequest> getPendingRequests() {
        return daoClaimRequest.getPendingRequests();
    }

    public List<ModelClaimRequest> getPendingRequestsByBarang(int barangId) {
        return daoClaimRequest.getPendingRequestsByBarang(barangId);
    }

    public void approveRequest(int requestId, int reviewedByUserId, String alasanReview) {
        daoClaimRequest.approveRequest(requestId, reviewedByUserId, alasanReview);
    }

    public void approveRequest(int requestId, int reviewedByUserId) {
        daoClaimRequest.approveRequest(requestId, reviewedByUserId);
    }

    public void rejectRequest(int requestId, int reviewedByUserId, String alasanReview) {
        daoClaimRequest.rejectRequest(requestId, reviewedByUserId, alasanReview);
    }

    public void rejectRequest(int requestId, int reviewedByUserId) {
        daoClaimRequest.rejectRequest(requestId, reviewedByUserId);
    }

    public void manualClaim(int barangId, int requesterUserId, int reviewedByUserId) {
        daoClaimRequest.manualClaim(barangId, requesterUserId, reviewedByUserId);
    }

    public List<ModelClaimRequest> getAllRequests() {
        return daoClaimRequest.getAllRequests();
    }

    public List<ModelClaimRequest> getClaimsByUserId(int userId) {
        return daoClaimRequest.getClaimsByUserId(userId);
    }

    /** Tandai kumpulan claim sudah dilihat notifikasinya oleh user (simpan ke DB). */
    public void markNotified(List<Integer> ids) {
        daoClaimRequest.markNotified(ids);
    }
}

package Model.Claim;

import java.util.List;

public interface InterfaceDAOClaimRequest {
    void insert(ModelClaimRequest claimRequest);
    boolean existsPendingRequest(int barangId, int requesterUserId);
    List<ModelClaimRequest> getPendingRequests();
    List<ModelClaimRequest> getPendingRequestsByBarang(int barangId);
    void approveRequest(int requestId, int reviewedByUserId);
    void approveRequest(int requestId, int reviewedByUserId, String alasanReview);
    void manualClaim(int barangId, int requesterUserId, int reviewedByUserId);
}

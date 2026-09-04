package Model.User;

import java.util.HashSet;
import java.util.Set;

public final class UserSession {

    private static ModelUser currentUser;
 
    /**
     * Menyimpan ID claim_request yang sudah pernah ditampilkan sebagai
     * notifikasi kepada user dalam sesi ini. Direset setiap login baru.
     */
    private static final Set<Integer> seenClaimIds = new HashSet<>();
 
    private UserSession() {}
 
    public static void setCurrentUser(ModelUser user) {
        currentUser = user;
    }
 
    public static ModelUser getCurrentUser() {
        return currentUser;
    }
 
    public static int getCurrentUserId() {
        return currentUser == null ? 0 : currentUser.getId();
    }
 
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
 
    public static boolean isAdmin() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }
 
    /**
     * Tandai sebuah claim request sudah dilihat notifikasinya.
     */
    public static void markClaimSeen(int claimRequestId) {
        seenClaimIds.add(claimRequestId);
    }
 
    /**
     * Tandai sekumpulan claim request sudah dilihat sekaligus.
     */
    public static void markAllClaimsSeen(Iterable<Integer> ids) {
        for (int id : ids) {
            seenClaimIds.add(id);
        }
    }
 
    /**
     * Cek apakah notifikasi untuk claim tertentu sudah pernah ditampilkan.
     */
    public static boolean isClaimSeen(int claimRequestId) {
        return seenClaimIds.contains(claimRequestId);
    }
 
    public static void clear() {
        currentUser = null;
        seenClaimIds.clear();
    }
}
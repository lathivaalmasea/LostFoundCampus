package Controller;

public interface LoginViewContract {
    void showInfoMessage(String message);
    void showErrorMessage(String message);
    void openAdminDashboard();
    void openUserDashboard();
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.User.DAOUser;
import Model.User.ModelUser;
import Model.User.UserSession;
import java.util.Arrays;
/**
 *
 * @author karina
 */
public class ControllerLogin {

    private final LoginViewContract view;
    private final DAOUser daoUser;

    // Usage: used for testing, without view contract and with a mock DAOUser
    public ControllerLogin() {
        this(null, new DAOUser());
    }

    // Usage: used by the login view, passing itself as the view contract
    public ControllerLogin(LoginViewContract view) {
        this(view, new DAOUser());
    }

    // Usage: used for testing, passing both view contract and mock DAOUser
    public ControllerLogin(LoginViewContract view, DAOUser daoUser) {
        this.view = view;
        this.daoUser = daoUser;
    }

    private ModelUser login(String username, String password) {
        String safeUsername = username == null ? "" : username.trim();
        if (safeUsername.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        ModelUser user = daoUser.getByUsername(safeUsername);
        if (user == null) {
            return null;
        }

        if (!password.equals(user.getPassword())) {
            return null;
        }

        return user;
    }

    public void handleLogin(String username, char[] passwordChars) {
        if (view == null) {
            return;
        }

        String password = new String(passwordChars);

        try {
            if ((username == null ? "" : username.trim()).isEmpty() || password.isEmpty()) {
                view.showErrorMessage("Username dan password wajib diisi");
                return;
            }

            ModelUser user = login(username, password);

            if (user == null) {
                view.showErrorMessage("Username / Password salah");
                return;
            }

            UserSession.setCurrentUser(user);

            view.showInfoMessage("Login berhasil");

            if ("admin".equalsIgnoreCase(user.getRole())) {
                view.openAdminDashboard();
            } else {
                view.openUserDashboard();
            }

        } finally {
            Arrays.fill(passwordChars, '\0');
        }
    }
}

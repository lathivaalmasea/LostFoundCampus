package Controller;

import java.awt.event.ActionEvent;

public class ControllerHome {

    private final HomeViewContract view;

    public ControllerHome(HomeViewContract view) {
        this.view = view;
    }

    public void handleOpenLogin() {
        if (view == null) {
            return;
        }

        view.openLogin();
    }

    public void handleOpenLogin(ActionEvent event) {
        handleOpenLogin();
    }

    public void handleOpenRegister() {
        if (view == null) {
            return;
        }

        view.openRegister();
    }

    public void handleOpenRegister(ActionEvent event) {
        handleOpenRegister();
    }

    public void handleExit() {
        if (view == null) {
            return;
        }

        if (view.confirmExit()) {
            view.exitApplication();
        }
    }

    public void handleExit(ActionEvent event) {
        handleExit();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.User;

import Controller.ControllerLogin;
import Controller.LoginViewContract;
import View.HomeView;
import View.Admin.DashboardAdmin;
import View.Component.AppButtonFactory;
import View.Component.AppContentPanel;
import View.Component.AppFrame;
import View.Component.AppTheme;
import View.Component.LabeledInput;
import javax.swing.*;
import java.awt.*;

public class Login extends AppFrame implements LoginViewContract {

    private final LabeledInput usernameInput;
    private final LabeledInput passwordInput;
 
    public Login() {
        this(null);
    }
 
    public Login(JFrame parentFrame) {
        super("Login", AppTheme.WINDOW_AUTH, parentFrame);
 
        ControllerLogin controller = new ControllerLogin(this);
        JPanel panel = createScreenPanel();
        JPanel form = new AppContentPanel(new GridBagLayout());
        form.setOpaque(false);
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 14, 8, 14);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
 
        JLabel title = new JLabel("LOGIN");
        title.setFont(AppTheme.TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(AppTheme.PRIMARY);
 
        usernameInput = LabeledInput.text("Username", 16);
        passwordInput = LabeledInput.password("Password", 16);
 
        JButton btnHome = AppButtonFactory.danger("HOME");
        JButton btnLogin = AppButtonFactory.primary("LOGIN");
        JButton btnRegister = AppButtonFactory.accent("REGISTER");
 
        btnHome.setPreferredSize(new Dimension(110, 38));
        btnLogin.setPreferredSize(new Dimension(110, 38));
        btnRegister.setPreferredSize(new Dimension(110, 38));
 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnHome);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
 
        gbc.gridy = 0;
        form.add(title, gbc);
 
        gbc.gridy = 1;
        form.add(usernameInput, gbc);
 
        gbc.gridy = 2;
        form.add(passwordInput, gbc);
 
        gbc.gridy = 3;
        gbc.insets = new Insets(14, 14, 8, 14);
        form.add(buttonPanel, gbc);
 
        panel.add(form, BorderLayout.CENTER);
        setScreenContent(panel);
 
        btnLogin.addActionListener(_ ->
            controller.handleLogin(
                usernameInput.getText(),
                passwordInput.getPassword()
            )
        );
 
        btnHome.addActionListener(_ -> openHome());
 
        btnRegister.addActionListener(_ -> showChildFrame(new Register(this)));
 
    }
 
    @Override
    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
 
    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
 
    @Override
    public void openAdminDashboard() {
        if (hasParentFrame()) {
            getParentFrame().dispose();
        }
        dispose();
        new DashboardAdmin().setVisible(true);
    }
 
    @Override
    public void openUserDashboard() {
        if (hasParentFrame()) {
            getParentFrame().dispose();
        }
        dispose();
        new DashboardUser().setVisible(true);
    }
 
    private void openHome() {
        if (hasParentFrame()) {
            backToParent();
            return;
        }
 
        dispose();
        new HomeView().setVisible(true);
    }
}
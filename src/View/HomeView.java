package View;

import Controller.ControllerHome;
import Controller.HomeViewContract;
import View.Component.AppButtonFactory;
import View.Component.AppCard;
import View.Component.AppFrame;
import View.Component.AppHeader;
import View.Component.AppTheme;
import View.User.Login;
import View.User.Register;
import javax.swing.*;
import java.awt.*;

public class HomeView extends AppFrame implements HomeViewContract {

    public HomeView() {
        super("Lost & Found Kampus", AppTheme.WINDOW_HOME);
        ControllerHome controller = new ControllerHome(this);

        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(AppTheme.BACKGROUND);

        AppCard card = new AppCard();

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        AppHeader header = new AppHeader(
            "LOST & FOUND KAMPUS",
            "Aplikasi Barang Hilang & Ditemukan",
            AppTheme.TEXT_PRIMARY,
            AppTheme.TEXT_SECONDARY
        );

        JButton btnLogin = AppButtonFactory.primary("Login");
        JButton btnRegister = AppButtonFactory.accent("Daftar Akun");
        JButton btnExit = AppButtonFactory.danger("Keluar");

        Dimension buttonSize = new Dimension(240, 40);
        for (JButton btn : new JButton[]{btnLogin, btnRegister, btnExit}) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(btnLogin);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(btnRegister);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(btnExit);

        card.setContent(contentPanel);
        rootPanel.add(card);
        add(rootPanel);

        btnLogin.addActionListener(controller::handleOpenLogin);
        btnRegister.addActionListener(controller::handleOpenRegister);
        btnExit.addActionListener(controller::handleExit);
    }

    @Override 
    public void openLogin() { 
        showChildFrame(new Login(this)); 
    }
    
    @Override 
    public void openRegister() { 
        showChildFrame(new Register(this)); 
    }

    @Override 
    public boolean confirmExit() {
        return JOptionPane.showConfirmDialog(
                this, 
                "Keluar aplikasi?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override 
    public void exitApplication() { System.exit(0); }
}

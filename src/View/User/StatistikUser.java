/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.User;

import Controller.ControllerBarang;
import Controller.ControllerClaimRequest;
import Model.Claim.ModelClaimRequest;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
/**
 *
 * @author Ivaa
 */
public class StatistikUser extends AppFrame {

    // Label angka biar bisa di-update saat refresh tanpa buat window baru
    private JLabel lblTotalValue;
    private JLabel lblHilangValue;
    private JLabel lblDitemukanValue;
    private JLabel lblDiklaimValue;
    private JLabel lblPendingValue;
    private JLabel infoLabel;
    private JLabel greeting;

    // Ukuran card tetap sama seperti sebelumnya
    private static final int CARD_W = 175;
    private static final int CARD_H = 120;

    public StatistikUser() {
        this(null);
    }

    public StatistikUser(JFrame parentFrame) {
        super("Statistik Saya", new Dimension(650, 520), parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Statistik Barang Saya"), BorderLayout.WEST);

        if (hasParentFrame()) {
            JButton btnBackTop = AppButtonFactory.backButton();
            btnBackTop.addActionListener(e -> backToParent());
            JPanel backWrapper = new JPanel();
            backWrapper.setLayout(new BoxLayout(backWrapper, BoxLayout.Y_AXIS));
            backWrapper.setOpaque(false);
            backWrapper.add(Box.createVerticalGlue());
            btnBackTop.setAlignmentX(Component.CENTER_ALIGNMENT);
            backWrapper.add(btnBackTop);
            backWrapper.add(Box.createVerticalGlue());
            topBar.add(backWrapper, BorderLayout.EAST);
        }
        add(topBar, BorderLayout.NORTH);

        // ---- Content (BoxLayout vertikal, padding kiri-kanan) ----
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        String namaUser = UserSession.getCurrentUser() != null
                ? UserSession.getCurrentUser().getNama() : "User";

        // Greeting
        greeting = AppLabelFactory.body("Halo, " + namaUser + "! Berikut ringkasan barang Anda.");
        greeting.setForeground(AppTheme.TEXT_SECONDARY);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(greeting);
        content.add(Box.createVerticalStrut(12));

        // ---- Baris kartu ----
        lblTotalValue     = new JLabel("0", SwingConstants.CENTER);
        lblHilangValue    = new JLabel("0", SwingConstants.CENTER);
        lblDitemukanValue = new JLabel("0", SwingConstants.CENTER);
        lblDiklaimValue   = new JLabel("0", SwingConstants.CENTER);
        lblPendingValue   = new JLabel("0", SwingConstants.CENTER);

        // Baris 1: 3 kartu sejajar
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.add(makeStatCard("Total Barang Saya", lblTotalValue,    AppTheme.PRIMARY));
        row1.add(makeStatCard("Barang Hilang",     lblHilangValue,   AppTheme.DANGER));
        row1.add(makeStatCard("Barang Ditemukan",  lblDitemukanValue, AppTheme.SUCCESS));
        content.add(row1);
        content.add(Box.createVerticalStrut(10));

        // Baris 2: 2 kartu sejajar
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.add(makeStatCard("Klaim Approved", lblDiklaimValue, AppTheme.ACCENT));
        row2.add(makeStatCard("Klaim Pending",  lblPendingValue, AppTheme.WARNING));
        content.add(row2);
        content.add(Box.createVerticalStrut(14));

        // ---- Info detail panel ----
        infoLabel = new JLabel();
        infoLabel.setFont(AppTheme.BODY_FONT);
        infoLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel infoPanel = makeInfoPanelShell(infoLabel);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Lebar mengikuti lebar row1 (3 card + gap): 3*175 + 2*10 = 545 + padding
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        infoPanel.setPreferredSize(new Dimension(600, 170));
        content.add(infoPanel);
        content.add(Box.createVerticalStrut(12));

        // ---- Refresh button ----
        JButton btnRefresh = AppButtonFactory.primary("Refresh Data");
        btnRefresh.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRefresh.setMaximumSize(new Dimension(160, 38));
        content.add(btnRefresh);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BACKGROUND);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> muatData());

        // Load data awal
        muatData();
    }

    /**
     * Memuat ulang semua angka statistik langsung dari database.
     * Bisa dipanggil kapan saja tanpa membuat window baru.
     */
    private void muatData() {
        int uid = UserSession.getCurrentUserId();
        ControllerBarang cb = new ControllerBarang();
        ControllerClaimRequest ccr = new ControllerClaimRequest();

        int total      = cb.getTotalByUserId(uid);
        int hilang     = cb.getTotalByUserIdAndStatus(uid, "Hilang");
        int ditemukan  = cb.getTotalByUserIdAndStatus(uid, "Ditemukan");

        List<ModelClaimRequest> semuaKlaim = ccr.getClaimsByUserId(uid);
        long approved  = semuaKlaim.stream()
                .filter(r -> "Approved".equalsIgnoreCase(r.getStatus())).count();
        long pending   = semuaKlaim.stream()
                .filter(r -> "Pending".equalsIgnoreCase(r.getStatus())).count();

        lblTotalValue.setText(String.valueOf(total));
        lblHilangValue.setText(String.valueOf(hilang));
        lblDitemukanValue.setText(String.valueOf(ditemukan));
        lblDiklaimValue.setText(String.valueOf(approved));
        lblPendingValue.setText(String.valueOf(pending));

        String html = "<html><table cellspacing='0' cellpadding='2'>"
            + "<tr><td>&#8226; Total barang yang pernah Anda daftarkan</td><td>&nbsp;:&nbsp;</td><td><b>" + total     + "</b> barang</td></tr>"
            + "<tr><td>&#8226; Barang berstatus Hilang</td>               <td>&nbsp;:&nbsp;</td><td><b>" + hilang    + "</b> barang</td></tr>"
            + "<tr><td>&#8226; Barang berstatus Ditemukan</td>            <td>&nbsp;:&nbsp;</td><td><b>" + ditemukan + "</b> barang</td></tr>"
            + "<tr><td>&#8226; Pengajuan klaim Anda yang disetujui</td>   <td>&nbsp;:&nbsp;</td><td><b>" + approved  + "</b> klaim</td></tr>"
            + "<tr><td>&#8226; Pengajuan klaim Anda yang masih menunggu</td><td>&nbsp;:&nbsp;</td><td><b>" + pending  + "</b> klaim</td></tr>"
            + "</table></html>";
        infoLabel.setText(html);
    }

    // Card — ukuran tetap sama (175x120), layout internal pakai setBounds
    private JPanel makeStatCard(String label, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 14));
                g2.fill(new RoundRectangle2D.Float(4, 6, CARD_W - 8, CARD_H - 6, 14, 14));
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, CARD_W - 4, CARD_H - 4, 14, 14));
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, 4, CARD_H - 4, 4, 4));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setPreferredSize(new Dimension(CARD_W, CARD_H));
        card.setMinimumSize(new Dimension(CARD_W, CARD_H));
        card.setMaximumSize(new Dimension(CARD_W, CARD_H));
        card.setOpaque(false);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));
        valueLabel.setForeground(color);
        valueLabel.setBounds(0, 18, CARD_W - 4, 50);
        card.add(valueLabel);

        JLabel lblTitle = new JLabel(label, SwingConstants.CENTER);
        lblTitle.setFont(AppTheme.CARD_TITLE_FONT);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitle.setBounds(0, 70, CARD_W - 4, 24);
        card.add(lblTitle);

        return card;
    }

    private JPanel makeInfoPanelShell(JLabel infoLabel) {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.setColor(AppTheme.BORDER);
                g2.setStroke(new java.awt.BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 2, getHeight() - 2, 14, 14));
                g2.setColor(AppTheme.PRIMARY_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, 36, 14, 14));
                g2.fillRect(0, 22, getWidth() - 1, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);

        JLabel titleLbl = new JLabel("Detail Statistik Anda");
        titleLbl.setFont(AppTheme.LABEL_FONT);
        titleLbl.setForeground(AppTheme.PRIMARY);
        titleLbl.setBounds(16, 8, 400, 22);
        p.add(titleLbl);

        infoLabel.setBounds(16, 46, 560, 115);
        p.add(infoLabel);

        return p;
    }
}

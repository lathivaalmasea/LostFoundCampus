package View.User;

import Controller.ControllerBarang;
import Controller.ControllerClaimRequest;
import Model.Claim.ModelClaimRequest;
import java.util.List;
import java.util.stream.Collectors;
import Model.User.UserSession;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTheme;
import View.Component.AppButtonFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DashboardUser extends AppFrame {
 
    private JLabel lblTotalValue;
    private JLabel lblHilangValue;
    private JLabel lblDitemukanValue;
    private JLabel lblApprovedValue;
    private JLabel lblPendingValue;
 
    public DashboardUser() { this(null); }
 
    public DashboardUser(JFrame parentFrame) {
        super("Dashboard User", AppTheme.WINDOW_DASHBOARD, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
 
        // ======== SIDEBAR ========
        JPanel sidebar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.SIDEBAR, 0, getHeight(), new Color(14, 22, 50));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(67, 97, 238, 60));
                g2.fillRect(getWidth() - 2, 0, 2, getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(AppTheme.SIDEBAR);
 
        // Logo area
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBounds(0, 0, 230, 85);
        logoPanel.setOpaque(false);
 
        JLabel logoIcon = new JLabel("L&F") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, 40, 40, 10, 10));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String t = "L&F";
                g2.drawString(t, (40 - fm.stringWidth(t)) / 2, (40 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        logoIcon.setPreferredSize(new Dimension(40, 40));
 
        JLabel logoText = new JLabel("L&F Kampus");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoText.setForeground(Color.WHITE);
        JLabel logoSub = new JLabel("User Panel");
        logoSub.setFont(AppTheme.SMALL_FONT);
        logoSub.setForeground(new Color(148, 163, 184));
 
        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);
        textBox.add(logoText);
        textBox.add(Box.createVerticalStrut(2));
        textBox.add(logoSub);
 
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 22));
        logoRow.setOpaque(false);
        logoRow.add(logoIcon);
        logoRow.add(textBox);
        logoPanel.add(logoRow, BorderLayout.CENTER);
 
        JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 18));
                g.fillRect(14, 0, getWidth() - 28, 1);
            }
        };
        sep.setOpaque(false);
        sep.setBounds(0, 83, 230, 2);
 
        JLabel lblMenu = new JLabel("  NAVIGASI");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMenu.setForeground(new Color(99, 179, 237, 160));
        lblMenu.setBounds(20, 100, 190, 18);
 
        JButton btnLihat       = makeSidebarBtn("Lihat Barang", 120);
        JButton btnLaporHilang = makeSidebarBtn("Lapor Hilang", 165);
        JButton btnLaporTemuan = makeSidebarBtn("Lapor Ditemukan", 210);
        JButton btnRiwayat     = makeSidebarBtn("Riwayat & Klaim", 255);
        JButton btnStatistik   = makeSidebarBtn("Statistik Saya", 300);
 
        JPanel sep2 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 12));
                g.fillRect(14, 0, getWidth() - 28, 1);
            }
        };
        sep2.setOpaque(false);
        sep2.setBounds(0, 352, 230, 2);
 
        JButton btnLogout = makeSidebarBtn("Logout", 370);
        btnLogout.setForeground(new Color(252, 165, 165));
 
        JButton btnBack = null;
        if (hasParentFrame()) {
            btnBack = makeSidebarBtn("Kembali", 415);
            btnBack.setForeground(new Color(165, 243, 252));
        }
 
        sidebar.add(logoPanel);
        sidebar.add(sep);
        sidebar.add(lblMenu);
        sidebar.add(btnLihat);
        sidebar.add(btnLaporHilang);
        sidebar.add(btnLaporTemuan);
        sidebar.add(btnRiwayat);
        sidebar.add(btnStatistik);
        sidebar.add(sep2);
        sidebar.add(btnLogout);
        if (btnBack != null) sidebar.add(btnBack);
 
        // ======== CONTENT AREA ========
        JPanel content = new JPanel(null);
        content.setBackground(AppTheme.BACKGROUND);
 
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBounds(0, 0, 770, 66);
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 24, 0, 24)));
 
        JLabel pageTitle = new JLabel("Dashboard User");
        pageTitle.setFont(AppTheme.SECTION_TITLE_FONT);
        pageTitle.setForeground(AppTheme.TEXT_PRIMARY);
 
        String namaUser = (UserSession.getCurrentUser() != null && UserSession.getCurrentUser().getNama() != null)
                ? UserSession.getCurrentUser().getNama() : "User";
        JLabel userInfo = new JLabel(namaUser);
        userInfo.setFont(AppTheme.LABEL_FONT);
        userInfo.setForeground(AppTheme.PRIMARY);
 
        JPanel userBadge = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int pad = 10;
                int h = getHeight() - pad * 2;
                g2.setColor(AppTheme.PRIMARY_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, pad, getWidth(), h, h, h));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        userBadge.setOpaque(false);
        userBadge.setPreferredSize(new Dimension(160, 66));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4, 0, 4);
        userBadge.add(userInfo, gbc);
 
        topBar.add(pageTitle, BorderLayout.WEST);
        topBar.add(userBadge, BorderLayout.EAST);
        content.add(topBar);
 
        JLabel welcome = AppLabelFactory.body(
                "Selamat datang di Sistem Lost & Found Kampus"
        );
        welcome.setForeground(AppTheme.TEXT_SECONDARY);
        welcome.setBounds(28, 86, 600, 22);
        content.add(welcome);
 
        // STAT CARDS (JLabel disimpan agar bisa di-refresh)
        lblTotalValue     = new JLabel("0", SwingConstants.CENTER);
        lblHilangValue    = new JLabel("0", SwingConstants.CENTER);
        lblDitemukanValue = new JLabel("0", SwingConstants.CENTER);
        lblApprovedValue  = new JLabel("0", SwingConstants.CENTER);
        lblPendingValue   = new JLabel("0", SwingConstants.CENTER);
 
        // Baris 1: 3 card statistik barang
        content.add(makeStatCard("Total Barang", lblTotalValue, AppTheme.PRIMARY, 28,  126, 200, 110));
        content.add(makeStatCard("Barang Hilang", lblHilangValue, AppTheme.ACCENT, 248,  126, 200, 110));
        content.add(makeStatCard("Barang Ditemukan", lblDitemukanValue, AppTheme.SUCCESS, 468,  126, 200, 110));
        // Baris 2: Klaim Approved + Klaim Pending
        content.add(makeStatCard("Klaim Approved", lblApprovedValue,  new Color(139, 92, 246), 28,  256, 200, 110));
        content.add(makeStatCard("Klaim Pending", lblPendingValue,   AppTheme.WARNING, 248,  256, 200, 110));
 
        // Load data awal
        muatDataDashboard();
 
        // Info panel
        JPanel infoPanel = makeInfoPanel();
        infoPanel.setBounds(28, 390, 640, 160);
        content.add(infoPanel);
 
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
 
        // ======== ACTIONS ========
        btnLihat.addActionListener(e -> showChildFrame(new LihatBarang(this)));
        btnLaporHilang.addActionListener(e -> showChildFrame(new LaporHilang(this)));
        btnLaporTemuan.addActionListener(e -> showChildFrame(new LaporDitemukan(this)));
        btnRiwayat.addActionListener(e -> showChildFrame(new RiwayatBarang(this)));
        btnStatistik.addActionListener(e -> showChildFrame(new StatistikUser(this)));
        btnLogout.addActionListener(e -> {
            UserSession.clear();
            dispose();
            new Login().setVisible(true);
        });
        if (btnBack != null) {
            final JButton fb = btnBack;
            fb.addActionListener(e -> backToParent());
        }
 
        // NOTIFIKASI OTOMATIS - hanya tampil jika ada klaim yang SUDAH direspon admin
        // tapi BELUM pernah dilihat notifikasinya (user_notified_at IS NULL di DB)
        SwingUtilities.invokeLater(() -> {
            ControllerClaimRequest ccrNotif = new ControllerClaimRequest();
            List<ModelClaimRequest> allKlaim = ccrNotif.getClaimsByUserId(UserSession.getCurrentUserId());
            List<ModelClaimRequest> belumDilihat = allKlaim.stream()
                    .filter(r -> !r.getStatus().equalsIgnoreCase("Pending"))
                    .filter(r -> !r.isUserNotified())
                    .collect(Collectors.toList());

            if (!belumDilihat.isEmpty()) {
                tampilkanDialogNotifikasi(belumDilihat);
                // Tandai sudah dilihat di DB setelah dialog modal ditutup
                List<Integer> ids = belumDilihat.stream()
                        .map(ModelClaimRequest::getId)
                        .collect(Collectors.toList());
                ccrNotif.markNotified(ids);
            }
        });
    }
 
    // Dialog notifikasi klaim yang baru direspon admin
    private void tampilkanDialogNotifikasi(List<ModelClaimRequest> klaims) {
        JDialog dialog = new JDialog(this, "Notifikasi Status Klaim", true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppTheme.BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("Ada update status klaim kamu!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel(klaims.size() + " klaim baru direspon admin");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(200, 220, 255));
        header.add(sub, BorderLayout.EAST);
        dialog.add(header, BorderLayout.NORTH);

        // Panel scroll berisi kartu per klaim
        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.setBackground(AppTheme.BACKGROUND);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        for (ModelClaimRequest r : klaims) {
            cards.add(buildKlaimCard(r));
            cards.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BACKGROUND);
        dialog.add(scroll, BorderLayout.CENTER);

        // Footer
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(AppTheme.BACKGROUND);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 14, 14, 14));

        JLabel note = new JLabel("Buka 'Riwayat & Klaim', lalu tab 'Status Klaim Saya' untuk detail lengkap.");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(AppTheme.TEXT_SECONDARY);
        bottom.add(note, BorderLayout.WEST);

        JButton btnTutup = AppButtonFactory.primary("Mengerti");
        btnTutup.addActionListener(e -> dialog.dispose());
        bottom.add(btnTutup, BorderLayout.EAST);
        dialog.add(bottom, BorderLayout.SOUTH);

        int h = Math.min(200 + klaims.size() * 160, 540);
        dialog.setSize(640, h);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /** Kartu satu klaim - menampilkan status dan info kontak jika Approved */
    private JPanel buildKlaimCard(ModelClaimRequest r) {
        boolean approved = "Approved".equalsIgnoreCase(r.getStatus());
        boolean rejected = "Rejected".equalsIgnoreCase(r.getStatus());

        Color borderColor = approved ? AppTheme.SUCCESS : rejected ? AppTheme.DANGER : AppTheme.WARNING;

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(AppTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, borderColor),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, approved ? 180 : 100));

        // Baris atas: nama barang + badge status
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lblBarang = new JLabel(r.getBarangName() + "  -  " + r.getBarangCategory());
        lblBarang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBarang.setForeground(AppTheme.TEXT_PRIMARY);
        top.add(lblBarang, BorderLayout.WEST);

        JLabel lblStatus = new JLabel(r.getStatus());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(borderColor);
        top.add(lblStatus, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        // Konten bawah
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        if (approved) {
            // Tampilkan info kontak pelapor
            boolean pelaporAdmin = "admin".equalsIgnoreCase(r.getPelaporRole());
            String noTelp = r.getPelaporNoTelp();
            boolean adaNoTelp = noTelp != null && !noTelp.trim().isEmpty();

            body.add(makeInfoLine("Klaim kamu disetujui! Silakan hubungi pihak berikut untuk serah terima:"));
            body.add(Box.createVerticalStrut(6));
            body.add(makeInfoLine("Nama     :  " + (r.getPelaporNama() != null ? r.getPelaporNama() : "-")));

            if (pelaporAdmin) {
                body.add(makeInfoLine("Kontak   :  Hubungi Admin / Petugas Kampus secara langsung"));
            } else if (adaNoTelp) {
                body.add(makeInfoLine("No. Telp :  " + noTelp));
            } else {
                body.add(makeInfoLine("No. Telp :  Tidak tersedia - hubungi admin untuk mediasi"));
            }

        } else if (rejected) {
            String alasan = r.getAlasanReview() != null && !r.getAlasanReview().isEmpty()
                    ? r.getAlasanReview() : "Tidak ada keterangan tambahan.";
            body.add(makeInfoLine("Klaim ditolak admin."));
            body.add(makeInfoLine("Alasan: " + alasan));
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JLabel makeInfoLine(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        return lbl;
    }    // ======== SIDEBAR BUTTON ========
    private JButton makeSidebarBtn(String text, int y) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(AppTheme.SIDEBAR_HOVER);
                    g2.fill(new RoundRectangle2D.Float(4, 2, getWidth() - 8, getHeight() - 4, 8, 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppTheme.SIDEBAR_FONT);
        btn.setForeground(new Color(203, 213, 225));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBounds(10, y, 210, 38);
        return btn;
    }
 
    // ======== STAT CARD ========
    private JPanel makeStatCard(String label, JLabel valueLabel, Color color, 
            int x, int y, int w, int h) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 14));
                g2.fill(new RoundRectangle2D.Float(4, 6, w - 8, h - 6, 14, 14));
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, w - 4, h - 4, 14, 14));
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, 4, h - 4, 4, 4));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setBounds(x, y, w, h);
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 14));
 
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
 
        JLabel lblTitle = new JLabel(label, SwingConstants.CENTER);
        lblTitle.setFont(AppTheme.CARD_TITLE_FONT);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
 
        JPanel inner = new JPanel(new BorderLayout(0, 4));
        inner.setOpaque(false);
        inner.add(valueLabel, BorderLayout.CENTER);
        inner.add(lblTitle, BorderLayout.SOUTH);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }
 
    // ======== REFRESH DATA ========
    private void muatDataDashboard() {
        int uid = UserSession.getCurrentUserId();
        ControllerBarang cb = new ControllerBarang();
        ControllerClaimRequest ccr = new ControllerClaimRequest();
 
        int totalSaya      = cb.getTotalByUserId(uid);
        int totalHilang    = cb.getTotalByUserIdAndStatus(uid, "Hilang");
        int totalDitemukan = cb.getTotalByUserIdAndStatus(uid, "Ditemukan");
 
        List<ModelClaimRequest> semuaKlaim = ccr.getClaimsByUserId(uid);
        long sudahApproved = semuaKlaim.stream()
                .filter(r -> "Approved".equalsIgnoreCase(r.getStatus())).count();
        long klaimPending  = semuaKlaim.stream()
                .filter(r -> "Pending".equalsIgnoreCase(r.getStatus())).count();
 
        lblTotalValue.setText(String.valueOf(totalSaya));
        lblHilangValue.setText(String.valueOf(totalHilang));
        lblDitemukanValue.setText(String.valueOf(totalDitemukan));
        lblApprovedValue.setText(String.valueOf(sudahApproved));
        lblPendingValue.setText(String.valueOf(klaimPending));
    }
 
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            // Refresh data setiap kali dashboard ditampilkan kembali
            if (lblTotalValue != null) {
                muatDataDashboard();
            }
        }
        super.setVisible(visible);
    }
 
    // ======== INFO PANEL ========
    private JPanel makeInfoPanel() {
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
 
        JLabel titleLbl = new JLabel("Panduan Penggunaan");
        titleLbl.setFont(AppTheme.LABEL_FONT);
        titleLbl.setForeground(AppTheme.PRIMARY);
        titleLbl.setBounds(16, 8, 400, 22);
        p.add(titleLbl);
 
        String[] tips = {
            "\u2022 Gunakan menu 'Lihat Barang' untuk melihat dan mengajukan klaim barang",
            "\u2022 Gunakan menu 'Input Barang' untuk mendaftarkan barang hilang/temuan",
            "\u2022 Cek menu 'Riwayat & Klaim', lalu tab 'Status Klaim Saya' untuk melihat semua status klaimmu",
            "\u2022 Notifikasi muncul otomatis saat ada klaim yang baru direspon admin",
        };
        int ty = 50;
        for (String tip : tips) {
            JLabel l = AppLabelFactory.body(tip);
            l.setBounds(16, ty, 620, 22);
            p.add(l);
            ty += 28;
        }
        return p;
    }
}
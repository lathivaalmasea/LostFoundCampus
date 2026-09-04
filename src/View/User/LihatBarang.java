package View.User;

import Controller.ControllerBarang;
import Controller.ControllerClaimRequest;
import Model.Barang.ModelBarang;
import Model.Barang.ModelTableBarang;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTableFactory;
import View.Component.AppTheme;
import javax.swing.*;
import java.awt.*;

public class LihatBarang extends AppFrame {
    private final JTable tableBarang;
    private final JTextField txtSearch;

    public LihatBarang() { this(null); }

    public LihatBarang(JFrame parentFrame) {
        super("Lihat Barang", AppTheme.WINDOW_TABLE, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Daftar Barang"), BorderLayout.WEST);

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

        // Keterangan singkat logika status
        JLabel lblKet = new JLabel(
            "<html><i>Status <b>Hilang</b>: pemilik mencari barangnya — klaim jika Anda PENEMU. &nbsp;|&nbsp; "
            + "Status <b>Ditemukan</b>: ada yang menemukan barang — klaim jika Anda PEMILIK.</i></html>");
        lblKet.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblKet.setForeground(AppTheme.TEXT_SECONDARY);
        lblKet.setBorder(BorderFactory.createEmptyBorder(6, 20, 0, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(AppTheme.BACKGROUND);
        toolbar.setBorder(BorderFactory.createEmptyBorder(4, 16, 0, 16));

        txtSearch = new JTextField(22);
        AppTableFactory.styleSearchField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(220, 36));

        JButton btnSearch  = AppButtonFactory.primary("Cari");
        JButton btnRefresh = AppButtonFactory.success("Refresh");
        JButton btnClaim   = AppButtonFactory.accent("Ajukan Claim");

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnRefresh);
        toolbar.add(btnClaim);

        // Table
        tableBarang = new JTable();
        AppTableFactory.style(tableBarang);
        JScrollPane scroll = new JScrollPane(tableBarang);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.SURFACE);

        JPanel topContent = new JPanel(new BorderLayout());
        topContent.setBackground(AppTheme.BACKGROUND);
        topContent.add(lblKet, BorderLayout.NORTH);
        topContent.add(toolbar, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        center.add(topContent, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadTable();

        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadTable(); });
        btnClaim.addActionListener(e -> claimSelectedBarang());
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { searchData(); }
        });
    }

    private void loadTable() {
        tableBarang.setModel(new ModelTableBarang(new ControllerBarang().getAll()));
    }

    private void searchData() {
        tableBarang.setModel(new ModelTableBarang(new ControllerBarang()
                .search(txtSearch.getText())));
    }

    private void claimSelectedBarang() {
        int row = tableBarang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang ingin diklaim terlebih dahulu");
            return;
        }

        int uid = UserSession.getCurrentUserId();
        if (uid == 0) {
            JOptionPane.showMessageDialog(this, "Silakan login kembali untuk mengajukan claim");
            return;
        }

        int barangId = Integer.parseInt(tableBarang.getValueAt(row, 0).toString());
        ControllerBarang cb = new ControllerBarang();
        ModelBarang barang = cb.getById(barangId);
        if (barang == null) {
            JOptionPane.showMessageDialog(this, "Data barang tidak ditemukan");
            return;
        }

        // Tidak bisa klaim barang sendiri
        if (barang.getUserId() == uid) {
            JOptionPane.showMessageDialog(this,
                    "Ini adalah laporan Anda sendiri. Gunakan menu 'Laporan Saya' untuk menariknya.");
            return;
        }

        // Sudah diklaim orang lain
        if ("Sudah Diklaim".equalsIgnoreCase(barang.getStatusClaim())) {
            JOptionPane.showMessageDialog(this, "Barang ini sudah diklaim oleh orang lain.");
            return;
        }

        // Ditarik oleh pelapor
        if ("Ditarik".equalsIgnoreCase(barang.getStatusClaim())) {
            JOptionPane.showMessageDialog(this, "Laporan barang ini sudah ditarik oleh pelapor.");
            return;
        }

        ControllerClaimRequest ccr = new ControllerClaimRequest();
        if (ccr.existsPendingRequest(barangId, uid)) {
            JOptionPane.showMessageDialog(this, "Anda sudah mengajukan claim untuk barang ini.");
            return;
        }

        // Buka modal — kirimkan status barang agar konteks tampil dengan benar
        new ModalClaimBarang(this, barangId, uid, barang.getStatus());
        loadTable();
    }
}

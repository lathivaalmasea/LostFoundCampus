package View.User;

import Controller.ControllerBarang;
import Controller.ControllerClaimRequest;
import Model.Barang.ModelBarang;
import Model.Barang.ModelTableBarang;
import Model.Claim.ModelClaimRequest;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTableFactory;
import View.Component.AppTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RiwayatBarang extends AppFrame {
    private JTable tableBarang;
    private final JTextField txtSearch;
    private JTable tableKlaim;

    public RiwayatBarang() { this(null); }

    public RiwayatBarang(JFrame parentFrame) {
        super("Riwayat Barang Saya", AppTheme.WINDOW_TABLE, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Riwayat & Klaim Saya"), BorderLayout.WEST);

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

        // ======== TAB PANE ========
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(AppTheme.LABEL_FONT);
        tabbedPane.setBackground(AppTheme.BACKGROUND);

        // ---- TAB 1: Barang yang saya laporkan ----
        JPanel panelBarang = new JPanel(new BorderLayout());
        panelBarang.setBackground(AppTheme.BACKGROUND);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(AppTheme.BACKGROUND);
        toolbar.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));

        txtSearch = new JTextField(22);
        AppTableFactory.styleSearchField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(220, 36));

        JButton btnSearch     = AppButtonFactory.primary("Cari");
        JButton btnRefresh    = AppButtonFactory.success("Refresh");
        JButton btnTarikLapor = AppButtonFactory.danger("Tarik Laporan");

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnRefresh);
        toolbar.add(btnTarikLapor);

        // Keterangan
        JLabel lblKetBarang = new JLabel(
            "<html><i>Daftar laporan yang Anda buat. Klik <b>Tarik Laporan</b> untuk membatalkan "
            + "laporan selama belum ada yang klaim.</i></html>");
        lblKetBarang.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblKetBarang.setForeground(AppTheme.TEXT_SECONDARY);
        lblKetBarang.setBorder(BorderFactory.createEmptyBorder(4, 12, 0, 8));

        JPanel topBarang = new JPanel(new BorderLayout());
        topBarang.setBackground(AppTheme.BACKGROUND);
        topBarang.add(toolbar, BorderLayout.NORTH);
        topBarang.add(lblKetBarang, BorderLayout.CENTER);

        tableBarang = new JTable();
        AppTableFactory.style(tableBarang);
        JScrollPane scrollBarang = new JScrollPane(tableBarang);
        scrollBarang.setBorder(null);
        scrollBarang.getViewport().setBackground(AppTheme.SURFACE);

        panelBarang.add(topBarang, BorderLayout.NORTH);
        panelBarang.add(scrollBarang, BorderLayout.CENTER);

        // ---- TAB 2: Klaim yang saya ajukan ----
        JPanel panelKlaim = new JPanel(new BorderLayout());
        panelKlaim.setBackground(AppTheme.BACKGROUND);

        JPanel toolbarKlaim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbarKlaim.setBackground(AppTheme.BACKGROUND);
        toolbarKlaim.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));
        JButton btnRefreshKlaim = AppButtonFactory.success("Refresh");
        JLabel infoKlaim = new JLabel("Daftar semua klaim yang pernah kamu ajukan beserta statusnya.");
        infoKlaim.setFont(AppTheme.BODY_FONT);
        infoKlaim.setForeground(AppTheme.TEXT_SECONDARY);
        toolbarKlaim.add(btnRefreshKlaim);
        toolbarKlaim.add(Box.createHorizontalStrut(10));
        toolbarKlaim.add(infoKlaim);

        String[] colsKlaim = {
            "Nama Barang", "Kategori", "Peran Saya", "Status Klaim",
            "Alasan Klaim", "Alasan Review", "Direview Oleh", "Tgl Pengajuan", "Tgl Review"
        };
        DefaultTableModel modelKlaim = new DefaultTableModel(colsKlaim, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableKlaim = new JTable(modelKlaim);
        tableKlaim.setRowHeight(34);
        tableKlaim.setFont(AppTheme.BODY_FONT);
        tableKlaim.setShowVerticalLines(false);
        tableKlaim.setGridColor(AppTheme.BORDER);
        tableKlaim.setSelectionBackground(AppTheme.PRIMARY_LIGHT);
        tableKlaim.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        tableKlaim.setIntercellSpacing(new Dimension(0, 0));
        tableKlaim.getTableHeader().setFont(AppTheme.LABEL_FONT);
        tableKlaim.getTableHeader().setBackground(AppTheme.TABLE_HEADER);
        tableKlaim.getTableHeader().setForeground(AppTheme.TEXT_SECONDARY);
        tableKlaim.getTableHeader().setPreferredSize(new Dimension(0, 38));

        // Warnai kolom Status Klaim (index 3)
        tableKlaim.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t,
                    Object value, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) setBackground(row % 2 == 0 ? AppTheme.SURFACE : AppTheme.TABLE_STRIPE);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                String val = value != null ? value.toString() : "";
                if (col == 3) {
                    if ("Approved".equalsIgnoreCase(val)) { setForeground(AppTheme.SUCCESS); setFont(AppTheme.LABEL_FONT); }
                    else if ("Rejected".equalsIgnoreCase(val)) { setForeground(AppTheme.DANGER); setFont(AppTheme.LABEL_FONT); }
                    else if ("Pending".equalsIgnoreCase(val)) { setForeground(AppTheme.WARNING); setFont(AppTheme.LABEL_FONT); }
                    else { setForeground(AppTheme.TEXT_PRIMARY); setFont(AppTheme.BODY_FONT); }
                } else {
                    setForeground(AppTheme.TEXT_PRIMARY);
                    setFont(AppTheme.BODY_FONT);
                }
                return this;
            }
        });

        JScrollPane scrollKlaim = new JScrollPane(tableKlaim);
        scrollKlaim.setBorder(null);
        scrollKlaim.getViewport().setBackground(AppTheme.SURFACE);

        panelKlaim.add(toolbarKlaim, BorderLayout.NORTH);
        panelKlaim.add(scrollKlaim, BorderLayout.CENTER);

        tabbedPane.addTab("  Laporan Saya  ", panelBarang);
        tabbedPane.addTab("  Status Klaim Saya  ", panelKlaim);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        center.add(tabbedPane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // Load data awal
        loadTableBarang();
        loadTableKlaim(modelKlaim);

        // Actions tab 1
        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadTableBarang(); });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { searchData(); }
        });
        btnTarikLapor.addActionListener(e -> tarikLaporan());

        // Actions tab 2
        btnRefreshKlaim.addActionListener(e -> loadTableKlaim(modelKlaim));

        // Saat tab "Status Klaim Saya" dibuka, tandai semua klaim sebagai sudah dibaca
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) markAllClaimsSeen();
        });
    }

    private void loadTableBarang() {
        int uid = UserSession.getCurrentUserId();
        tableBarang.setModel(new ModelTableBarang(
                new ControllerBarang().getAllByUserId(uid)));
    }

    private void searchData() {
        int uid = UserSession.getCurrentUserId();
        tableBarang.setModel(new ModelTableBarang(
                new ControllerBarang().searchByUserId(uid, txtSearch.getText())));
    }

    private void loadTableKlaim(DefaultTableModel model) {
        model.setRowCount(0);
        int uid = UserSession.getCurrentUserId();
        List<ModelClaimRequest> klaims = new ControllerClaimRequest().getClaimsByUserId(uid);
        for (ModelClaimRequest r : klaims) {
            model.addRow(new Object[]{
                r.getBarangName(),
                r.getBarangCategory(),
                r.getRolePemohon(),
                r.getStatus(),
                r.getAlasanKlaim()  != null ? r.getAlasanKlaim()  : "-",
                r.getAlasanReview() != null ? r.getAlasanReview() : "-",
                r.getReviewerName() != null ? r.getReviewerName() : "-",
                r.getRequestedAt()  != null ? r.getRequestedAt()  : "-",
                r.getReviewedAt()   != null ? r.getReviewedAt()   : "-"
            });
        }
    }

    /**
     * User menarik laporannya sendiri — hanya bisa jika status_claim masih 'Belum Diklaim'.
     * Jika sudah ada yang claim (pending), laporan tidak bisa ditarik agar pemohon tidak dirugikan.
     */
    private void tarikLaporan() {
        int row = tableBarang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih laporan yang ingin ditarik.");
            return;
        }

        int barangId = Integer.parseInt(tableBarang.getValueAt(row, 0).toString());
        String namaBarang = tableBarang.getValueAt(row, 1).toString();

        // Cek apakah ada pending claim
        ControllerClaimRequest ccr = new ControllerClaimRequest();
        List<ModelClaimRequest> pending = ccr.getPendingRequestsByBarang(barangId);
        if (!pending.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "<html>Laporan <b>" + namaBarang + "</b> tidak dapat ditarik karena sudah ada<br>"
                + "pengajuan klaim yang sedang menunggu persetujuan admin.<br>"
                + "Hubungi admin jika tetap ingin membatalkan laporan.</html>",
                "Tidak Dapat Ditarik", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int uid = UserSession.getCurrentUserId();
        int confirm = JOptionPane.showConfirmDialog(this,
            "<html>Tarik laporan <b>" + namaBarang + "</b>?<br>"
            + "Data akan tetap tersimpan namun laporan tidak lagi aktif.</html>",
            "Konfirmasi Tarik Laporan", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = new ControllerBarang().tarikLaporan(barangId, uid);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Laporan berhasil ditarik.");
            loadTableBarang();
        } else {
            JOptionPane.showMessageDialog(this,
                "Laporan tidak dapat ditarik. Mungkin sudah diklaim atau ditarik sebelumnya.",
                "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAllClaimsSeen() {
        int uid = UserSession.getCurrentUserId();
        List<ModelClaimRequest> klaims = new ControllerClaimRequest().getClaimsByUserId(uid);
        for (ModelClaimRequest r : klaims) {
            if (!"Pending".equalsIgnoreCase(r.getStatus())) {
                UserSession.markClaimSeen(r.getId());
            }
        }
    }
}

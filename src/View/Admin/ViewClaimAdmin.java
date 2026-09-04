package View.Admin;

import Controller.ControllerClaimRequest;
import Model.Claim.ModelClaimRequest;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTableFactory;
import View.Component.AppTheme;
import View.Component.DetailDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class ViewClaimAdmin extends AppFrame {

    private JTable tablePending;
    private JTable tableRiwayat;
    private ControllerClaimRequest controller;

    public ViewClaimAdmin() { this(null); }

    public ViewClaimAdmin(JFrame parentFrame) {
        super("Manajemen Claim", AppTheme.WINDOW_TABLE, parentFrame);
        controller = new ControllerClaimRequest();
        // Tandai semua pending sebagai sudah dilihat admin -
        // badge notifikasi di dashboard akan hilang setelah halaman ini dibuka.
        controller.markAllPendingAdminViewed();
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel pageTitle = AppLabelFactory.sectionTitle("Manajemen Claim");
        titlePanel.add(pageTitle);
        topBar.add(titlePanel, BorderLayout.WEST);

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

        // ---- Main panel ----
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));

        // ---- Pending Claims table ----
        JPanel pendingPanel = makeCardPanel("Claim Menunggu Persetujuan  \u2014  klik 2x baris untuk detail lengkap");
        String[] colsPending = {
            "ID Request", "ID Barang", "Nama Barang", "Kategori",
            "Pemohon", "Username", "Peran Pemohon", "Alasan Klaim", "Status", "Tanggal Pengajuan"
        };
        DefaultTableModel modelPending = new DefaultTableModel(colsPending, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePending = buildStyledTable(modelPending);
        setColumnWidths(tablePending, colsPending);
        JScrollPane scrollPending = new JScrollPane(tablePending);
        styleScrollPane(scrollPending);
        pendingPanel.add(scrollPending, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);
        JButton btnApprove = AppButtonFactory.success("Approve");
        JButton btnReject  = AppButtonFactory.danger("Reject");
        JButton btnRefresh = AppButtonFactory.primary("Refresh");
        JButton btnDetail  = AppButtonFactory.accent("Lihat Detail");
        actionPanel.add(btnRefresh);
        actionPanel.add(btnDetail);
        actionPanel.add(btnReject);
        actionPanel.add(btnApprove);
        pendingPanel.add(actionPanel, BorderLayout.SOUTH);

        // ---- Riwayat table ----
        JPanel riwayatPanel = makeCardPanel("Riwayat Klaim (Approved / Rejected)  \u2014  klik 2x baris untuk detail lengkap");
        String[] colsRiwayat = {
            "ID Request", "ID Barang", "Nama Barang", "Kategori",
            "Pemohon", "Username", "Peran Pemohon", "Alasan Klaim", "Status",
            "Alasan Review", "Tanggal Pengajuan", "Tanggal Review", "Direview Oleh"
        };
        DefaultTableModel modelRiwayat = new DefaultTableModel(colsRiwayat, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableRiwayat = buildStyledTable(modelRiwayat);
        setColumnWidths(tableRiwayat, colsRiwayat);
        JScrollPane scrollRiwayat = new JScrollPane(tableRiwayat);
        styleScrollPane(scrollRiwayat);
        riwayatPanel.add(scrollRiwayat, BorderLayout.CENTER);

        // Split vertically
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pendingPanel, riwayatPanel);
        splitPane.setDividerLocation(290);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setBackground(AppTheme.BACKGROUND);
        splitPane.setOpaque(false);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Load data
        loadPending(modelPending);
        loadRiwayat(modelRiwayat);

        // Actions
        btnApprove.addActionListener(e -> {
            approveClaim();
            loadPending(modelPending);
            loadRiwayat(modelRiwayat);
        });
        btnReject.addActionListener(e -> {
            rejectClaim();
            loadPending(modelPending);
            loadRiwayat(modelRiwayat);
        });
        btnRefresh.addActionListener(e -> {
            loadPending(modelPending);
            loadRiwayat(modelRiwayat);
        });
        btnDetail.addActionListener(e -> {
            // Cek tabel mana yang ada seleksinya
            if (tablePending.getSelectedRow() != -1) {
                showDetail(tablePending);
            } else if (tableRiwayat.getSelectedRow() != -1) {
                showDetail(tableRiwayat);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih salah satu baris terlebih dahulu.");
            }
        });
    }

    // -------------------------------------------------------
    // Atur lebar kolom berdasarkan nama kolom
    // -------------------------------------------------------
    private void setColumnWidths(JTable table, String[] cols) {
        for (int i = 0; i < cols.length; i++) {
            int w;
            switch (cols[i]) {
                case "ID Request":
                case "ID Barang":     w = 70;  break;
                case "Status":        w = 90;  break;
                case "Peran Pemohon": w = 110; break;
                case "Alasan Klaim":
                case "Alasan Review": w = 200; break;
                case "Tanggal Pengajuan":
                case "Tanggal Review": w = 160; break;
                case "Direview Oleh": w = 130; break;
                default:              w = 120; break;
            }
            table.getColumnModel().getColumn(i).setPreferredWidth(w);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    // -------------------------------------------------------
    // Popup detail satu baris - pakai DetailDialog
    // -------------------------------------------------------
    private void showDetail(JTable table) {
        if (table.getSelectedRow() == -1) return;
        DetailDialog.show(this, table, "Detail Klaim");
    }

    // -------------------------------------------------------
    // Load data
    // -------------------------------------------------------
    private void loadPending(DefaultTableModel model) {
        model.setRowCount(0);
        List<ModelClaimRequest> list = controller.getPendingRequests();
        for (ModelClaimRequest r : list) {
            model.addRow(new Object[]{
                r.getId(),
                r.getBarangId(),
                r.getBarangName(),
                r.getBarangCategory(),
                r.getRequesterName(),
                r.getRequesterUsername(),
                r.getRolePemohon(),
                r.getAlasanKlaim() != null ? r.getAlasanKlaim() : "-",
                r.getStatus(),
                r.getRequestedAt()
            });
        }
    }

    private void loadRiwayat(DefaultTableModel model) {
        model.setRowCount(0);
        List<ModelClaimRequest> all = controller.getAllRequests();
        for (ModelClaimRequest r : all) {
            if (!"Pending".equalsIgnoreCase(r.getStatus())) {
                model.addRow(new Object[]{
                    r.getId(),
                    r.getBarangId(),
                    r.getBarangName(),
                    r.getBarangCategory(),
                    r.getRequesterName(),
                    r.getRequesterUsername(),
                    r.getRolePemohon(),
                    r.getAlasanKlaim()  != null ? r.getAlasanKlaim()  : "-",
                    r.getStatus(),
                    r.getAlasanReview() != null ? r.getAlasanReview() : "-",
                    r.getRequestedAt(),
                    r.getReviewedAt()   != null ? r.getReviewedAt()   : "-",
                    r.getReviewerName() != null ? r.getReviewerName() : "-"
                });
            }
        }
    }

    // -------------------------------------------------------
    // Approve / Reject
    // -------------------------------------------------------
    private void approveClaim() {
        int row = tablePending.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih claim terlebih dahulu!");
            return;
        }
        String namaBarang = tablePending.getValueAt(row, 2).toString();
        String pemohon    = tablePending.getValueAt(row, 4).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Approve klaim \"" + namaBarang + "\" oleh " + pemohon + "?\n"
                + "Semua klaim lain untuk barang ini akan otomatis di-reject.",
                "Konfirmasi Approve", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int requestId = Integer.parseInt(tablePending.getValueAt(row, 0).toString());
        int adminId   = UserSession.getCurrentUserId();
        controller.approveRequest(requestId, adminId);
        JOptionPane.showMessageDialog(this, "Klaim berhasil di-approve!");
    }

    private void rejectClaim() {
        int row = tablePending.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih claim terlebih dahulu!");
            return;
        }
        String namaBarang = tablePending.getValueAt(row, 2).toString();
        String pemohon    = tablePending.getValueAt(row, 4).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reject klaim \"" + namaBarang + "\" oleh " + pemohon + "?",
                "Konfirmasi Reject", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int requestId = Integer.parseInt(tablePending.getValueAt(row, 0).toString());
        controller.rejectRequest(requestId, UserSession.getCurrentUserId());
        JOptionPane.showMessageDialog(this, "Klaim berhasil di-reject.");
    }

    // -------------------------------------------------------
    // UI builders
    // -------------------------------------------------------
    private JPanel makeCardPanel(String title) {
        JPanel outer = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fill(new RoundRectangle2D.Float(3, 5, getWidth()-5, getHeight()-5, 14, 14));
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-3, getHeight()-3, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        outer.setOpaque(false);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        header.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setFont(AppTheme.LABEL_FONT);
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(lbl);
        outer.add(header, BorderLayout.NORTH);
        outer.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 4));
        return outer;
    }

    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(AppTheme.BODY_FONT);
        table.setShowVerticalLines(false);
        table.setGridColor(AppTheme.BORDER);
        table.setSelectionBackground(AppTheme.PRIMARY_LIGHT);
        table.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader th = table.getTableHeader();
        th.setFont(AppTheme.LABEL_FONT);
        th.setBackground(AppTheme.TABLE_HEADER);
        th.setForeground(AppTheme.TEXT_SECONDARY);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
        th.setPreferredSize(new Dimension(0, 38));
        th.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t,
                    Object value, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? AppTheme.SURFACE : AppTheme.TABLE_STRIPE);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                String val = value != null ? value.toString() : "";
                if (val.equalsIgnoreCase("Approved") || val.equalsIgnoreCase("Sudah Diklaim")) {
                    setForeground(AppTheme.SUCCESS);
                    setFont(AppTheme.LABEL_FONT);
                } else if (val.equalsIgnoreCase("Pending")) {
                    setForeground(AppTheme.WARNING);
                    setFont(AppTheme.LABEL_FONT);
                } else if (val.equalsIgnoreCase("Rejected")) {
                    setForeground(AppTheme.DANGER);
                    setFont(AppTheme.LABEL_FONT);
                } else {
                    setForeground(AppTheme.TEXT_PRIMARY);
                    setFont(AppTheme.BODY_FONT);
                }
                // Tooltip teks lengkap saat hover
                setToolTipText(val.isEmpty() ? null : val);
                return this;
            }
        });

        // Double-click untuk popup detail lengkap
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showDetail(table);
                }
            }
        });

        return table;
    }

    private void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));
        sp.getViewport().setBackground(AppTheme.SURFACE);
    }
}
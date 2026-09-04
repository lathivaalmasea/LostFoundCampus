package View.Admin;

import Controller.ControllerBarang;
import Controller.ControllerClaimRequest;
import Model.Claim.ModelClaimRequest;
import Model.Barang.ModelBarang;
import Model.Barang.ModelTableBarang;
import Model.User.DAOUser;
import Model.User.ModelUser;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTableFactory;
import View.Component.AppTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ViewBarang extends AppFrame {
    private final JTable tableBarang;
    private final JTextField txtSearch;

    public ViewBarang() { this(null); }

    public ViewBarang(JFrame parentFrame) {
        super("View Barang", AppTheme.WINDOW_TABLE, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Data Barang"), BorderLayout.WEST);

        // Back button in top bar
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

        // ---- Toolbar ----
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(AppTheme.BACKGROUND);
        toolbar.setBorder(BorderFactory.createEmptyBorder(4, 16, 0, 16));

        txtSearch = new JTextField(22);
        AppTableFactory.styleSearchField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(220, 36));

        JButton btnSearch      = AppButtonFactory.primary("Cari");
        JButton btnRefresh     = AppButtonFactory.success("Refresh");
        JButton btnEdit        = AppButtonFactory.accent("Edit");        // TAMBAHAN
        JButton btnDelete      = AppButtonFactory.danger("Hapus");
        JButton btnReviewClaim = AppButtonFactory.accent("Review Claim");
        JButton btnRiwayat     = AppButtonFactory.primary("Riwayat & Statistik"); // TAMBAHAN

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnRefresh);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnReviewClaim);
        toolbar.add(btnRiwayat);

        // ---- Table ----
        tableBarang = new JTable();
        AppTableFactory.style(tableBarang);
        JScrollPane scroll = new JScrollPane(tableBarang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.SURFACE);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        center.add(toolbar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadTable();
        applyTableRenderers();

        // ---- Action Listeners (fungsi lama tidak diubah) ----
        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadTable(); });
        btnDelete.addActionListener(e -> deleteData());
        btnReviewClaim.addActionListener(e -> reviewClaim());
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { searchData(); }
        });

        // TAMBAHAN: aksi tombol Edit
        btnEdit.addActionListener(e -> editData());

        // TAMBAHAN: aksi tombol Riwayat & Statistik
        btnRiwayat.addActionListener(e -> showChildFrame(new StatistikAdmin(this)));
    }

    private void loadTable() {
        tableBarang.setModel(new ModelTableBarang(new ControllerBarang().getAll()));
        applyTableRenderers();
    }

    /** Renderer warna untuk kolom Status Klaim (kol 5) dan Klaim Pending (kol 6) */
    private void applyTableRenderers() {
        // Kolom 5 — Status Klaim
        tableBarang.getColumnModel().getColumn(5).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(CENTER);
                    if (!isSelected) {
                        String v = value == null ? "" : value.toString();
                        switch (v) {
                            case "Sudah Diklaim":
                                setForeground(new Color(22, 163, 74));   break;
                            case "Ditarik":
                                setForeground(new Color(220, 38, 38));   break;
                            default: // Belum Diklaim
                                setForeground(new Color(100, 116, 139)); break;
                        }
                    } else {
                        setForeground(Color.WHITE);
                    }
                    return this;
                }
            }
        );
        // Kolom 6 — Klaim Pending
        tableBarang.getColumnModel().getColumn(6).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(CENTER);
                    if (!isSelected) {
                        String v = value == null ? "" : value.toString();
                        if (!"—".equals(v)) {
                            setForeground(new Color(217, 119, 6)); // amber — ada pending
                        } else {
                            setForeground(new Color(148, 163, 184));
                        }
                    } else {
                        setForeground(Color.WHITE);
                    }
                    return this;
                }
            }
        );
    }

    private void searchData() {
        tableBarang.setModel(new ModelTableBarang(
            new ControllerBarang().search(txtSearch.getText())));
        applyTableRenderers();
    }

    private void deleteData() {
        int row = tableBarang.getSelectedRow();
        if (row == -1) { 
            JOptionPane.showMessageDialog(this, "Pilih data dulu!"); 
            return; 
        }
        
        int id = Integer.parseInt(tableBarang.getValueAt(row, 0).toString());
        new ControllerBarang().delete(id);
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        loadTable();
    }

    private void reviewClaim() {
        int row = tableBarang.getSelectedRow();
        if (row == -1) { 
            JOptionPane.showMessageDialog(this, "Pilih data barang terlebih dahulu"); 
            return; 
        }
        
        int barangId = Integer.parseInt(tableBarang.getValueAt(row, 0).toString());
        ControllerBarang cb = new ControllerBarang();
        ModelBarang barang = cb.getById(barangId);
        if (barang == null) { 
            JOptionPane.showMessageDialog(this, "Data barang tidak ditemukan"); 
            return; 
        }
        
        ControllerClaimRequest ccr = new ControllerClaimRequest();
        List<ModelClaimRequest> pending = ccr.getPendingRequestsByBarang(barangId);
        if (!pending.isEmpty()) {
            ModelClaimRequest req = choosePendingRequest(pending);
            if (req == null) return;
            int c = JOptionPane.showConfirmDialog(this,
                "Setujui claim dari " + req.getRequesterName() + " untuk barang " 
                        + req.getBarangName() + "?",
                "Persetujuan Claim", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            int admin = UserSession.getCurrentUserId();
            ccr.approveRequest(req.getId(), admin == 0 ? 1 : admin);
            JOptionPane.showMessageDialog(this, "Claim berhasil disetujui");
            loadTable(); return;
        }
        ModelUser user = chooseUserForManualClaim(barang);
        if (user == null) return;
        int c = JOptionPane.showConfirmDialog(this,
            "Admin akan langsung menandai barang ini diklaim oleh " 
                    + user.getNama() + ". Lanjutkan?",
            "Manual Claim", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        int admin = UserSession.getCurrentUserId();
        ccr.manualClaim(barangId, user.getId(), admin == 0 ? 1 : admin);
        JOptionPane.showMessageDialog(this, "Barang berhasil diklaim secara manual");
        loadTable();
    }

    private ModelClaimRequest choosePendingRequest(List<ModelClaimRequest> list) {
        String[] opts = list.stream().map(r -> r.getRequesterName() +
                " (@" + r.getRequesterUsername() + ") - "
                + r.getRequestedAt()).toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(this, 
                "Pilih request claim yang akan ditinjau", "Daftar Claim Pending",
                JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
        if (sel == null) return null;
        for (int i = 0; i < opts.length; i++) if (opts[i].equals(sel)) 
            return list.get(i);
        return null;
    }

    private ModelUser chooseUserForManualClaim(ModelBarang barang) {
        List<ModelUser> users = new DAOUser().getAll().stream()
            .filter(u -> "user".equalsIgnoreCase(u.getRole()))
            .filter(u -> u.getId() != barang.getUserId()).toList();
        
        if (users.isEmpty()) { JOptionPane.showMessageDialog(this,
                "Tidak ada user yang dapat dipilih untuk claim manual");
        return null;
        }
        
        String[] opts = users.stream().map(u -> u.getNama() +
                " (@" + u.getUsername() + ")").toArray(String[]::new);
        
        String sel = (String) JOptionPane.showInputDialog(this, 
                "Tidak ada request pending. Pilih user untuk claim manual", 
                "Manual Claim", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
        
        if (sel == null) return null;
        for (int i = 0; i < opts.length; i++) if (opts[i].equals(sel))
            return users.get(i);
        return null;
    }

    // ============================================================
    //  FUNGSI BARU — edit data barang yang dipilih
    // ============================================================

    private void editData() {
        int row = tableBarang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data barang terlebih dahulu!");
            return;
        }

        int barangId = Integer.parseInt(tableBarang.getValueAt(row, 0).toString());
        ModelBarang barang = new ControllerBarang().getById(barangId);
        if (barang == null) {
            JOptionPane.showMessageDialog(this, "Data barang tidak ditemukan");
            return;
        }

        // Buka EditBarang dengan data yang sudah diisi
        showChildFrame(new EditBarang(this, barang));
    }
}

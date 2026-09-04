package View.Admin;

import Controller.ControllerBarang;
import Model.Barang.ModelBarang;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Form edit data barang oleh admin.
 * Admin hanya boleh mengubah: nama, kategori, lokasi, deskripsi, dan status (Hilang/Ditemukan).
 * Status Claim TIDAK boleh diubah langsung di sini — perubahan status claim
 * hanya boleh terjadi melalui flow approve/reject di Manajemen Claim atau Review Claim
 * di ViewBarang, agar tabel claim_requests dan barang tetap sinkron.
 */
public class EditBarang extends AppFrame {

    public EditBarang() {
        this(null);
    }

    public EditBarang(JFrame parentFrame) {
        this(parentFrame, null);
    }

    public EditBarang(JFrame parentFrame, ModelBarang barang) {
        super("Edit Barang", new Dimension(540, 580), parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Edit Barang"), BorderLayout.WEST);
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

        // ---- Form Panel ----
        JPanel formPanel = new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(500, 560);
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(16, 12, getWidth() - 32, getHeight() - 24, 14, 14));
                g2.setColor(AppTheme.BORDER);
                g2.setStroke(new java.awt.BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(16.5f, 12.5f, getWidth() - 33, getHeight() - 25, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formPanel.setOpaque(false);

        // Field: Nama Barang
        JLabel lblNama = makeLabel("Nama Barang *");
        lblNama.setBounds(40, 30, 200, 22);
        JTextField txtNama = makeTextField();
        txtNama.setBounds(40, 54, 420, 36);
        formPanel.add(lblNama);
        formPanel.add(txtNama);

        // Field: Kategori
        JLabel lblKategori = makeLabel("Kategori");
        lblKategori.setBounds(40, 102, 200, 22);
        String[] kategoriOptions = {"Elektronik", "Pakaian", "Aksesoris", "Dokumen",
                                    "Kendaraan", "Peralatan Kuliah", "Lainnya"};
        JComboBox<String> cmbKategori = makeComboBox(kategoriOptions);
        cmbKategori.setBounds(40, 126, 420, 36);
        formPanel.add(lblKategori);
        formPanel.add(cmbKategori);

        // Field: Lokasi
        JLabel lblLokasi = makeLabel("Lokasi *");
        lblLokasi.setBounds(40, 174, 200, 22);
        JTextField txtLokasi = makeTextField();
        txtLokasi.setBounds(40, 198, 420, 36);
        formPanel.add(lblLokasi);
        formPanel.add(txtLokasi);

        // Field: Status Barang (full width — Status Claim tidak lagi di samping ini)
        JLabel lblStatus = makeLabel("Status Barang");
        lblStatus.setBounds(40, 246, 200, 22);
        String[] statusOptions = {"Hilang", "Ditemukan"};
        JComboBox<String> cmbStatus = makeComboBox(statusOptions);
        cmbStatus.setBounds(40, 270, 195, 36);
        formPanel.add(lblStatus);
        formPanel.add(cmbStatus);

        // Status Claim — READ ONLY, tampilkan sebagai label informatif
        // Admin TIDAK boleh mengubah status_claim dari sini.
        // Perubahan status_claim hanya boleh lewat Review Claim / Manajemen Claim.
        JLabel lblStatusClaimTitle = makeLabel("Status Klaim");
        lblStatusClaimTitle.setBounds(265, 246, 200, 22);

        String currentStatusClaim = (barang != null && barang.getStatusClaim() != null)
                ? barang.getStatusClaim() : "Belum Diklaim";
        JLabel lblStatusClaimValue = new JLabel(currentStatusClaim);
        lblStatusClaimValue.setFont(AppTheme.BODY_FONT);
        lblStatusClaimValue.setForeground(resolveClaimColor(currentStatusClaim));
        lblStatusClaimValue.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        lblStatusClaimValue.setBackground(new Color(245, 245, 250));
        lblStatusClaimValue.setOpaque(true);
        lblStatusClaimValue.setBounds(265, 270, 195, 36);
        lblStatusClaimValue.setToolTipText(
            "Status klaim hanya bisa diubah melalui menu Manajemen Claim atau Review Claim.");

        JLabel lblClaimNote = new JLabel("<html><i>Ubah via Manajemen Claim</i></html>");
        lblClaimNote.setFont(AppTheme.SMALL_FONT);
        lblClaimNote.setForeground(AppTheme.TEXT_SECONDARY);
        lblClaimNote.setBounds(265, 308, 195, 18);

        formPanel.add(lblStatusClaimTitle);
        formPanel.add(lblStatusClaimValue);
        formPanel.add(lblClaimNote);

        // Field: Deskripsi
        JLabel lblDeskripsi = makeLabel("Deskripsi");
        lblDeskripsi.setBounds(40, 334, 200, 22);
        JTextArea txtDeskripsi = new JTextArea();
        txtDeskripsi.setFont(AppTheme.BODY_FONT);
        txtDeskripsi.setForeground(AppTheme.TEXT_PRIMARY);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        JScrollPane scrollDeskripsi = new JScrollPane(txtDeskripsi);
        scrollDeskripsi.setBounds(40, 358, 420, 90);
        scrollDeskripsi.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
        formPanel.add(lblDeskripsi);
        formPanel.add(scrollDeskripsi);

        // ---- Tombol ----
        JButton btnSimpan = AppButtonFactory.primary("Simpan Perubahan");
        btnSimpan.setBounds(40, 468, 200, 42);
        formPanel.add(btnSimpan);

        JButton btnBatal = AppButtonFactory.danger("Batal");
        btnBatal.setBounds(265, 468, 195, 42);
        formPanel.add(btnBatal);

        // ---- Isi form jika data tersedia ----
        if (barang != null) {
            txtNama.setText(barang.getNamaBarang());
            txtLokasi.setText(barang.getLokasi());
            txtDeskripsi.setText(barang.getDeskripsi());
            cmbKategori.setSelectedItem(barang.getKategori());
            cmbStatus.setSelectedItem(barang.getStatus());
            // status_claim sudah diisi via lblStatusClaimValue di atas — tidak perlu lagi
        }

        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        scrollForm.getViewport().setBackground(AppTheme.BACKGROUND);
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        wrapper.add(scrollForm, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // ---- Action Listeners ----
        btnBatal.addActionListener(e -> backToParent());

        btnSimpan.addActionListener(e -> {
            String nama      = txtNama.getText().trim();
            String lokasi    = txtLokasi.getText().trim();
            String deskripsi = txtDeskripsi.getText().trim();
            String kategori  = (String) cmbKategori.getSelectedItem();
            String status    = (String) cmbStatus.getSelectedItem();

            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nama Barang wajib diisi!",
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                txtNama.requestFocus();
                return;
            }
            if (lokasi.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Lokasi wajib diisi!",
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                txtLokasi.requestFocus();
                return;
            }

            ModelBarang updated = barang != null ? barang : new ModelBarang();
            updated.setNamaBarang(nama);
            updated.setKategori(kategori);
            updated.setLokasi(lokasi);
            updated.setDeskripsi(deskripsi);
            updated.setStatus(status);
            // status_claim TIDAK diubah di sini — tetap seperti yang ada di DB

            new ControllerBarang().update(updated);
            JOptionPane.showMessageDialog(this,
                "Data barang berhasil diperbarui!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            backToParent();
        });
    }

    /** Warna label status klaim berdasarkan nilainya */
    private Color resolveClaimColor(String statusClaim) {
        if (statusClaim == null) return AppTheme.TEXT_SECONDARY;
        switch (statusClaim) {
            case "Sudah Diklaim": return new Color(22, 163, 74);   // hijau
            case "Ditarik":       return new Color(220, 38, 38);   // merah
            default:              return AppTheme.TEXT_SECONDARY;   // abu — Belum Diklaim
        }
    }

    // ---- Helper UI ----
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppTheme.LABEL_FONT);
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        tf.setFont(AppTheme.BODY_FONT);
        tf.setForeground(AppTheme.TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return tf;
    }

    private JComboBox<String> makeComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(AppTheme.BODY_FONT);
        cb.setForeground(AppTheme.TEXT_PRIMARY);
        cb.setBackground(AppTheme.SURFACE);
        return cb;
    }
}

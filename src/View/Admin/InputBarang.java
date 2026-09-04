package View.Admin;

import Controller.ControllerBarang;
import Model.Barang.ModelBarang;
import Model.User.UserSession;
import View.Component.AppButtonFactory;
import View.Component.AppFrame;
import View.Component.AppLabelFactory;
import View.Component.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Form input data barang DARURAT oleh admin.
 * status_claim selalu otomatis "Belum Diklaim" — tidak ada pilihan dropdown.
 * Status (Hilang/Ditemukan) wajib dipilih oleh admin.
 */
public class InputBarang extends AppFrame {

    private final JTextField txtNamaBarang;
    private final JComboBox<String> cbKategori;
    private final JTextArea txtDeskripsi;
    private final JTextField txtLokasi;
    private final JComboBox<String> cbStatus;
    // cbStatusClaim DIHAPUS — selalu "Belum Diklaim" secara otomatis

    public InputBarang() { this(null); }

    public InputBarang(JFrame parentFrame) {
        super("Input Barang (Admin)", AppTheme.WINDOW_FORM, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrap.setOpaque(false);
        titleWrap.add(AppLabelFactory.sectionTitle("Input Barang Darurat"));
        // Keterangan kecil di bawah judul
        topBar.add(titleWrap, BorderLayout.WEST);

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

        // ---- Info banner ----
        // (menjelaskan bahwa ini fitur darurat admin, bukan laporan user biasa)

        // ---- Form card ----
        // Tinggi dikurangi karena baris Status Claim sudah dihapus
        JPanel formCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,12));
                g2.fill(new RoundRectangle2D.Float(4, 6, getWidth()-6, getHeight()-6, 14, 14));
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-3, getHeight()-3, 14, 14));
                g2.setColor(AppTheme.BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-4, getHeight()-4, 14, 14));
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        // Tinggi dikurangi 52px karena satu baris (Status Claim) dihapus
        formCard.setPreferredSize(new Dimension(480, 468));

        int lx = 28, fx = 200, fw = 234, rowH = 36, gap = 52, y = 28;

        // --- Banner keterangan ---
        JLabel lblInfo = new JLabel(
                "<html><i style='color:#6366f1'>Input ini diperuntukkan admin untuk entri data darurat. "
                + "Status Claim otomatis diset <b>Belum Diklaim</b>.</i></html>");
        lblInfo.setFont(AppTheme.SMALL_FONT);
        lblInfo.setForeground(AppTheme.TEXT_SECONDARY);
        lblInfo.setBounds(lx, y, 420, 32);
        formCard.add(lblInfo);
        y += 40;

        addFormRow(formCard, "Nama Barang *", lx, y);
        txtNamaBarang = styledField();
        txtNamaBarang.setBounds(fx, y, fw, rowH);
        formCard.add(txtNamaBarang); y += gap;

        addFormRow(formCard, "Kategori", lx, y);
        cbKategori = styledCombo(new String[]{
            "Elektronik","Dokumen","Aksesoris","Pakaian",
            "Kendaraan","Peralatan Kuliah"
        });
        cbKategori.setBounds(fx, y, fw, rowH);
        formCard.add(cbKategori); y += gap;

        addFormRow(formCard, "Deskripsi", lx, y);
        txtDeskripsi = new JTextArea();
        txtDeskripsi.setFont(AppTheme.BODY_FONT);
        txtDeskripsi.setLineWrap(true); txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JScrollPane spDesc = new JScrollPane(txtDeskripsi);
        spDesc.setBounds(fx, y, fw, 70);
        spDesc.setBorder(null);
        formCard.add(spDesc); y += 86;

        addFormRow(formCard, "Lokasi *", lx, y);
        txtLokasi = styledField();
        txtLokasi.setBounds(fx, y, fw, rowH);
        formCard.add(txtLokasi); y += gap;

        addFormRow(formCard, "Status", lx, y);
        cbStatus = styledCombo(new String[]{"Hilang","Ditemukan"});
        cbStatus.setBounds(fx, y, fw, rowH);
        formCard.add(cbStatus); y += gap + 6;

        // Status Claim TIDAK ditampilkan — selalu otomatis "Belum Diklaim"

        JButton btnSimpan = AppButtonFactory.success("Simpan");
        JButton btnReset  = AppButtonFactory.warning("Reset");
        btnSimpan.setBounds(fx, y, 118, 38);
        btnReset.setBounds(fx + 126, y, 100, 38);
        formCard.add(btnSimpan); formCard.add(btnReset);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        wrapper.setBackground(AppTheme.BACKGROUND);
        wrapper.add(formCard);
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanData());
        btnReset.addActionListener(e -> resetForm());
    }

    private void addFormRow(JPanel p, String text, int x, int y) {
        JLabel lbl = AppLabelFactory.create(text, AppTheme.LABEL_FONT, AppTheme.TEXT_PRIMARY, JLabel.RIGHT);
        lbl.setBounds(x, y, 160, 36);
        p.add(lbl);
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(AppTheme.BODY_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(AppTheme.BODY_FONT);
        cb.setBackground(AppTheme.SURFACE);
        return cb;
    }

    private void simpanData() {
        // --- Validasi wajib isi ---
        String nama   = txtNamaBarang.getText().trim();
        String lokasi = txtLokasi.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Barang wajib diisi!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            txtNamaBarang.requestFocus();
            return;
        }
        if (lokasi.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Lokasi wajib diisi!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            txtLokasi.requestFocus();
            return;
        }

        ModelBarang barang = new ModelBarang();
        barang.setNamaBarang(nama);
        barang.setKategori(String.valueOf(cbKategori.getSelectedItem()));
        barang.setDeskripsi(txtDeskripsi.getText().trim());
        barang.setLokasi(lokasi);
        barang.setStatus(String.valueOf(cbStatus.getSelectedItem()));
        barang.setStatusClaim("Belum Diklaim"); // selalu otomatis
        int uid = UserSession.getCurrentUserId();
        barang.setUserId(uid == 0 ? 1 : uid);

        new ControllerBarang().insert(barang);
        JOptionPane.showMessageDialog(this,
            "Data barang berhasil disimpan.\nStatus Claim: Belum Diklaim (otomatis).");
        resetForm();
    }

    private void resetForm() {
        txtNamaBarang.setText("");
        txtDeskripsi.setText("");
        txtLokasi.setText("");
        cbKategori.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.User;

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
 *
 * @author Ivaa
 */
public class LaporHilang extends AppFrame {
    private final JTextField txtNamaBarang;
    private final JComboBox<String> cbKategori;
    private final JTextArea txtDeskripsi;
    private final JTextField txtLokasi;
 
    public LaporHilang() { this(null); }
 
    public LaporHilang(JFrame parentFrame) {
        super("Lapor Barang Hilang", AppTheme.WINDOW_FORM, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
 
        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle("Lapor Barang Hilang"), BorderLayout.WEST);
 
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
 
        // ---- Form card ----
        JPanel formCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 12));
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
        formCard.setPreferredSize(new Dimension(480, 440));
 
        int lx = 28, fx = 200, fw = 234, rowH = 36, gap = 52, y = 28;
 
        // Info banner di dalam card
        JLabel lblBanner = new JLabel(
            "<html><b>Barang Hilang</b> — Isi data barang milik Anda yang hilang. "
            + "Jika seseorang menemukannya, mereka akan mengajukan klaim sebagai penemu.</html>");
        lblBanner.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblBanner.setForeground(new Color(146, 64, 14));
        lblBanner.setBounds(lx, y, 420, 40);
        formCard.add(lblBanner); y += 52;
 
        addFormRow(formCard, "Nama Barang *", lx, y);
        txtNamaBarang = styledField();
        txtNamaBarang.setBounds(fx, y, fw, rowH);
        formCard.add(txtNamaBarang); y += gap;
 
        addFormRow(formCard, "Kategori", lx, y);
        cbKategori = styledCombo(new String[]{
            "Elektronik", "Dokumen", "Aksesoris", "Pakaian",
            "Kendaraan", "Peralatan Kuliah"
        });
        cbKategori.setBounds(fx, y, fw, rowH);
        formCard.add(cbKategori); y += gap;
 
        addFormRow(formCard, "Deskripsi", lx, y);
        txtDeskripsi = new JTextArea();
        txtDeskripsi.setFont(AppTheme.BODY_FONT);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JScrollPane spDesc = new JScrollPane(txtDeskripsi);
        spDesc.setBounds(fx, y, fw, 70);
        spDesc.setBorder(null);
        formCard.add(spDesc); y += 86;
 
        addFormRow(formCard, "Terakhir Dilihat Di *", lx, y);
        txtLokasi = styledField();
        txtLokasi.setToolTipText("Lokasi terakhir barang diketahui, mis: Perpustakaan Lt. 2");
        txtLokasi.setBounds(fx, y, fw, rowH);
        formCard.add(txtLokasi); y += gap;
 
        // Status badge (read-only)
        addFormRow(formCard, "Status Laporan", lx, y);
        JLabel lblStatus = new JLabel("  HILANG  ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239, 68, 68));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setOpaque(false);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblStatus.setBounds(fx, y + 5, 90, 26);
        formCard.add(lblStatus); y += gap + 6;
 
        JButton btnSimpan = AppButtonFactory.success("Kirim Laporan");
        JButton btnReset  = AppButtonFactory.warning("Reset");
        btnSimpan.setBounds(fx, y, 140, 38);
        btnReset.setBounds(fx + 148, y, 90, 38);
        formCard.add(btnSimpan);
        formCard.add(btnReset);
 
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
        String nama   = txtNamaBarang.getText().trim();
        String lokasi = txtLokasi.getText().trim();
 
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNamaBarang.requestFocus();
            return;
        }
        if (lokasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lokasi terakhir dilihat wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtLokasi.requestFocus();
            return;
        }
 
        ModelBarang barang = new ModelBarang();
        barang.setNamaBarang(nama);
        barang.setKategori(String.valueOf(cbKategori.getSelectedItem()));
        barang.setDeskripsi(txtDeskripsi.getText().trim());
        barang.setLokasi(lokasi);
        barang.setStatus("Hilang");            // hardcoded
        barang.setStatusClaim("Belum Diklaim");
        int uid = UserSession.getCurrentUserId();
        barang.setUserId(uid == 0 ? 1 : uid);
 
        new ControllerBarang().insert(barang);
        JOptionPane.showMessageDialog(this,
            "Laporan barang hilang berhasil dikirim!\n"
            + "Jika seseorang menemukannya, mereka dapat mengajukan klaim sebagai penemu.");
        resetForm();
    }
 
    private void resetForm() {
        txtNamaBarang.setText("");
        txtDeskripsi.setText("");
        txtLokasi.setText("");
        cbKategori.setSelectedIndex(0);
    }
}

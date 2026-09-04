/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Component;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 *
 * @author Ivaa
 */
public class DetailDialog extends JDialog {
    
    // Warna khusus status badge
    private static final Color BADGE_APPROVED = new Color(220, 252, 231); // hijau muda
    private static final Color BADGE_APPROVED_FG = new Color(22, 101, 52);
    private static final Color BADGE_PENDING  = new Color(254, 243, 199); // kuning muda
    private static final Color BADGE_PENDING_FG  = new Color(146, 64, 14);
    private static final Color BADGE_REJECTED = new Color(254, 226, 226); // merah muda
    private static final Color BADGE_REJECTED_FG = new Color(153, 27, 27);
    private static final Color BADGE_DEFAULT  = new Color(241, 245, 249);
    private static final Color BADGE_DEFAULT_FG  = AppTheme.TEXT_SECONDARY;
 
    private DetailDialog(Window owner, String title,
                         String[] colNames, Object[] values) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
 
        // Root panel dengan rounded corners
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // shadow
                g2.setColor(new Color(0, 0, 0, 28));
                g2.fill(new RoundRectangle2D.Float(4, 6, getWidth()-6, getHeight()-6, 18, 18));
                // card
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-4, getHeight()-4, 18, 18));
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
        setContentPane(root);
 
        // Header bar
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.PRIMARY);
                // rounded only on top corners
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-4, getHeight() + 18, 18, 18));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 52));
        header.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 16));
 
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppTheme.CARD_TITLE_FONT);
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);
 
        // Tombol tutup
        JButton btnClose = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnClose.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnClose.setForeground(new Color(255, 255, 255, 200));
        btnClose.setOpaque(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(32, 32));
        btnClose.addActionListener(e -> dispose());
        header.add(btnClose, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
 
        // Scroll pane berisi baris-baris data
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.SURFACE);
        content.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
 
        for (int i = 0; i < colNames.length; i++) {
            content.add(buildRow(colNames[i],
                    values[i] != null ? values[i].toString() : "-",
                    i % 2 == 0));
        }
 
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.SURFACE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        root.add(scroll, BorderLayout.CENTER);
 
        // Footer dengan tombol Tutup
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setBackground(AppTheme.SURFACE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 0, 14, 0)));
 
        JButton btnOk = AppButtonFactory.primary("Tutup");
        btnOk.setPreferredSize(new Dimension(120, 38));
        btnOk.addActionListener(e -> dispose());
        footer.add(btnOk);
        root.add(footer, BorderLayout.SOUTH);
 
        // ESC untuk tutup
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        root.getActionMap().put("close", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { dispose(); }
        });
 
        // Ukuran & posisi - pack() agar tinggi mengikuti konten
        pack();
        int maxH = 580;
        if (getHeight() > maxH) setSize(getWidth(), maxH);
        setMinimumSize(new Dimension(560, 200));
        setLocationRelativeTo(owner instanceof Component ? (Component) owner : null);
    }
 
    // Satu baris: label | nilai
    private JPanel buildRow(String label, String value, boolean even) {
        boolean isStatus = label.equalsIgnoreCase("Status");
 
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(even ? AppTheme.SURFACE : AppTheme.TABLE_STRIPE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(10, 4, 10, 8)));
 
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy  = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill   = GridBagConstraints.BOTH;
        gc.weighty = 1;
 
        // Kolom kiri - label
        gc.gridx   = 0;
        gc.weightx = 0;
        gc.insets  = new Insets(0, 0, 0, 16);
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(AppTheme.LABEL_FONT);
        lblKey.setForeground(AppTheme.TEXT_SECONDARY);
        lblKey.setPreferredSize(new Dimension(155, 24));
        lblKey.setMinimumSize(new Dimension(155, 24));
        row.add(lblKey, gc);
 
        // Kolom kanan - nilai
        gc.gridx   = 1;
        gc.weightx = 1;
        gc.insets  = new Insets(0, 0, 0, 0);
 
        if (isStatus) {
            JPanel badgeHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            badgeHolder.setOpaque(false);
            badgeHolder.add(buildBadge(value));
            row.add(badgeHolder, gc);
        } else {
            // Selalu HTML agar JLabel bisa wrap otomatis; lebar diset via CSS
            String display = "<html><body style='width:290px;'>"
                    + escHtml(value) + "</body></html>";
            JLabel lblVal = new JLabel(display);
            lblVal.setFont(AppTheme.BODY_FONT);
            lblVal.setForeground(AppTheme.TEXT_PRIMARY);
            row.add(lblVal, gc);
        }
 
        return row;
    }
 
    private static String escHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
 
    /** Badge pill berwarna sesuai status */
    private JPanel buildBadge(String status) {
        Color bg, fg;
        String lower = status.toLowerCase();
        if (lower.contains("approved") || lower.contains("sudah diklaim")) {
            bg = BADGE_APPROVED; fg = BADGE_APPROVED_FG;
        } else if (lower.contains("pending") || lower.contains("belum")) {
            bg = BADGE_PENDING; fg = BADGE_PENDING_FG;
        } else if (lower.contains("rejected") || lower.contains("ditarik")) {
            bg = BADGE_REJECTED; fg = BADGE_REJECTED_FG;
        } else {
            bg = BADGE_DEFAULT; fg = BADGE_DEFAULT_FG;
        }
 
        JPanel pill = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setOpaque(false);
        pill.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));
 
        JLabel lbl = new JLabel(status);
        lbl.setFont(AppTheme.LABEL_FONT);
        lbl.setForeground(fg);
        pill.add(lbl);
        pill.setPreferredSize(new Dimension(
                lbl.getPreferredSize().width + 24,
                28));
        return pill;
    }
 
    // Static helper: dari JTable
    public static void show(Component parent, JTable table, String title) {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int colCount = table.getColumnCount();
        String[] names = new String[colCount];
        Object[] vals  = new Object[colCount];
        for (int c = 0; c < colCount; c++) {
            names[c] = table.getColumnName(c);
            vals[c]  = table.getValueAt(row, c);
        }
        show(parent, names, vals, title);
    }
 
    // Static helper: dari TableModel + baris tertentu
    public static void show(Component parent, TableModel model, int row, String title) {
        int colCount = model.getColumnCount();
        String[] names = new String[colCount];
        Object[] vals  = new Object[colCount];
        for (int c = 0; c < colCount; c++) {
            names[c] = model.getColumnName(c);
            vals[c]  = model.getValueAt(row, c);
        }
        show(parent, names, vals, title);
    }
 
    // Titik masuk utama
    public static void show(Component parent, String[] colNames,
                             Object[] values, String title) {
        Window owner = parent == null ? null
                : (parent instanceof Window ? (Window) parent
                : SwingUtilities.getWindowAncestor(parent));
        DetailDialog dlg = new DetailDialog(owner, title, colNames, values);
        dlg.setVisible(true);
    }
}
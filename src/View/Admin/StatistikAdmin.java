/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Admin;

import Controller.ControllerBarang;
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
public class StatistikAdmin extends AppFrame {
    public StatistikAdmin() { 
        this(null); 
    }
 
    public StatistikAdmin(JFrame parentFrame) {
        super("Statistik Admin", AppTheme.WINDOW_TABLE, parentFrame);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
 
        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(0, 22, 0, 22)));
        topBar.setPreferredSize(new Dimension(0, 62));
        topBar.add(AppLabelFactory.sectionTitle(
                "Statistik Barang"), BorderLayout.WEST
        );
 
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
 
        // ---- Content ----
        JPanel content = new JPanel(null);
        content.setBackground(AppTheme.BACKGROUND);
 
        ControllerBarang cb = new ControllerBarang();
        int totalBarang    = cb.getTotalBarang();
        int totalHilang    = cb.getTotalByStatus("Hilang");
        int totalDitemukan = cb.getTotalByStatus("Ditemukan");
        int totalDiklaim   = cb.getTotalByStatusClaim("Sudah Diklaim");
 
        // Sub-title
        JLabel subtitle = AppLabelFactory.body("Ringkasan data barang "
                + "dalam sistem");
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setBounds(28, 20, 600, 22);
        content.add(subtitle);
 
        content.add(makeStatCard("Total Barang", String.valueOf(totalBarang), AppTheme.PRIMARY, 28,  60, 190, 120));
        content.add(makeStatCard("Barang Hilang", String.valueOf(totalHilang), AppTheme.DANGER, 238,  60, 190, 120));         
        content.add(makeStatCard("Barang Ditemukan", String.valueOf(totalDitemukan), AppTheme.SUCCESS, 448,  60, 190, 120));     
        content.add(makeStatCard("Sudah Diklaim", String.valueOf(totalDiklaim), AppTheme.ACCENT, 658,  60, 190, 120));  
        
        // Info panel
        JPanel infoPanel = makeInfoPanel(
                totalBarang, totalHilang, totalDitemukan, totalDiklaim
        );
        infoPanel.setBounds(28, 210, 820, 160);
        content.add(infoPanel);
 
        // Refresh button
        JButton btnRefresh = AppButtonFactory.primary("Refresh Data");
        btnRefresh.setBounds(28, 390, 160, 38);
        content.add(btnRefresh);
        btnRefresh.addActionListener(e -> {
            dispose();
            new StatistikAdmin(parentFrame).setVisible(true);
        });
 
        add(content, BorderLayout.CENTER);
    }
 
    private JPanel makeStatCard(String label, String value, Color color, int x, int y, int w, int h) {
        JPanel card = new JPanel(null) {
            
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
 
        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblValue.setForeground(color);
        lblValue.setBounds(0, 18, w - 4, 50);
        card.add(lblValue);
 
        JLabel lblTitle = new JLabel(label, SwingConstants.CENTER);
        lblTitle.setFont(AppTheme.CARD_TITLE_FONT);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitle.setBounds(0, 70, w - 4, 24);
        card.add(lblTitle);
 
        return card;
    }
 
    private JPanel makeInfoPanel(int total, int hilang, int ditemukan, int diklaim) {
        JPanel p = new JPanel(null) {
            
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 14, 14));
                g2.setColor(AppTheme.BORDER);
                g2.setStroke(new java.awt.BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-2, getHeight()-2, 14, 14));
                g2.setColor(AppTheme.PRIMARY_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, 36, 14, 14));
                g2.fillRect(0, 22, getWidth()-1, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
 
        JLabel titleLbl = new JLabel("Ringkasan Statistik");
        titleLbl.setFont(AppTheme.LABEL_FONT);
        titleLbl.setForeground(AppTheme.PRIMARY);
        titleLbl.setBounds(16, 8, 400, 22);
        p.add(titleLbl);
 
        int pctHilang    = total > 0 ? (hilang    * 100 / total) : 0;
        int pctDitemukan = total > 0 ? (ditemukan * 100 / total) : 0;
        int pctDiklaim   = total > 0 ? (diklaim   * 100 / total) : 0;
 
        String html = "<html><table cellspacing='0' cellpadding='2'>"
            + "<tr><td>* Total keseluruhan barang terdaftar</td><td>&nbsp;:&nbsp;</td><td>" + total     + " barang</td></tr>"
            + "<tr><td>* Barang berstatus Hilang</td>           <td>&nbsp;:&nbsp;</td><td>" + hilang    + " barang (" + pctHilang    + "%)</td></tr>"
            + "<tr><td>* Barang berstatus Ditemukan</td>        <td>&nbsp;:&nbsp;</td><td>" + ditemukan + " barang (" + pctDitemukan + "%)</td></tr>"
            + "<tr><td>* Barang berstatus Sudah Diklaim</td>    <td>&nbsp;:&nbsp;</td><td>" + diklaim   + " barang (" + pctDiklaim   + "%)</td></tr>"
            + "</table></html>";
        JLabel infoLabel = new JLabel(html);
        infoLabel.setFont(AppTheme.BODY_FONT);
        infoLabel.setForeground(AppTheme.TEXT_PRIMARY);
        infoLabel.setBounds(16, 46, 780, 110);
        p.add(infoLabel);
 
        return p;
    }
}

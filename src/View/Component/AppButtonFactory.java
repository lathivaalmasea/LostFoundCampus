package View.Component;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public final class AppButtonFactory {

    private AppButtonFactory() {}

    public static JButton primary(String text)  { return create(text, 
            AppTheme.PRIMARY,  
            AppTheme.TEXT_ON_PRIMARY); }
    public static JButton success(String text)  { return create(text, 
            AppTheme.SUCCESS,  
            AppTheme.TEXT_ON_PRIMARY); }
    public static JButton warning(String text)  { return create(text, 
            AppTheme.WARNING,  
            AppTheme.TEXT_ON_PRIMARY); }
    public static JButton danger(String text)   { return create(text, 
            AppTheme.DANGER,   
            AppTheme.TEXT_ON_PRIMARY); }
    public static JButton accent(String text)   { return create(text, 
            AppTheme.ACCENT,   
            AppTheme.TEXT_ON_PRIMARY); }

    private static JButton create(String text, Color bg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color paintBg = getModel().isPressed()
                        ? bg.darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(paintBg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                        getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(fg);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 28, 38));
        return button;
    }

    public static JButton backButton() {
        JButton btn = new JButton("Kembali") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()
                        ? new Color(229, 231, 235)
                        : getModel().isRollover() ? new Color(243, 244, 246) : Color.WHITE;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(AppTheme.BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth()-2, 
                        getHeight()-2, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(AppTheme.TEXT_SECONDARY);
        btn.setFont(AppTheme.BUTTON_FONT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setMaximumSize(new Dimension(120, 38));
        btn.setMinimumSize(new Dimension(120, 38));
        return btn;
    }
}

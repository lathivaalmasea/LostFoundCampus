package View.Component;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class AppCard extends JPanel {

    public AppCard() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Drop shadow
        for (int i = 8; i > 0; i--) {
            int alpha = (int)(35 * ((8 - i + 1) / 8.0));
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fill(new RoundRectangle2D.Float(i, i, getWidth() - i * 0.5f, 
                getHeight() - i * 0.5f, 16, 16));
        }

        // Card surface
        g2.setColor(AppTheme.SURFACE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 6, 
                getHeight() - 6, 16, 16));

        // Subtle border
        g2.setColor(AppTheme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 7, 
                getHeight() - 7, 16, 16));
        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        return new Insets(32, 32, 32, 38); // extra right for shadow
    }

    public void setContent(JPanel panel) {
        removeAll();
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }
}
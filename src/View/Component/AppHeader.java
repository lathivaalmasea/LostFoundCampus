package View.Component;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class AppHeader extends JPanel {

    public AppHeader(String titleText, String subtitleText) {
        this(titleText, subtitleText, AppTheme.TEXT_PRIMARY,
            AppTheme.TEXT_SECONDARY);
    }

    public AppHeader(String titleText, String subtitleText,
            Color titleColor, Color subtitleColor) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final int BADGE_SIZE = 36;

        JPanel badgeWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int x = (getWidth() - BADGE_SIZE) / 2;
                int y = 0;
                g2.setColor(AppTheme.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(x, y, BADGE_SIZE, BADGE_SIZE, 10, 10));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                String t = "L&F";
                g2.drawString(t,
                        x + (BADGE_SIZE - fm.stringWidth(t)) / 2,
                        y + (BADGE_SIZE + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }

            @Override public Dimension getPreferredSize() { return new Dimension(200, BADGE_SIZE); }
            @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, BADGE_SIZE); }
        };
        badgeWrapper.setOpaque(false);
        badgeWrapper.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitleText, SwingConstants.CENTER);
        subtitleLabel.setFont(AppTheme.SUBTITLE_FONT);
        subtitleLabel.setForeground(subtitleColor);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        add(badgeWrapper);
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(4));
        add(subtitleLabel);
    }
}

package View.Component;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.text.JTextComponent;

public class LabeledInput extends JPanel {

    private final JComponent input;

    private LabeledInput(String labelText, JComponent inputField) {
        this.input = inputField;
        setLayout(new BorderLayout(0, 6));
        setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.LABEL_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);

        if (inputField instanceof JTextComponent tc) {
            tc.setFont(AppTheme.BODY_FONT);
            tc.setBackground(AppTheme.SURFACE);
            tc.setForeground(AppTheme.TEXT_PRIMARY);
            tc.setCaretColor(AppTheme.PRIMARY);
            applyInputBorder(tc, false);

            tc.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { applyInputBorder(tc, true); }
                @Override public void focusLost(FocusEvent e)   { applyInputBorder(tc, false); }
            });
        }

        add(label, BorderLayout.NORTH);
        add(inputField, BorderLayout.CENTER);
    }

    private void applyInputBorder(JTextComponent tc, boolean focused) {
        Color borderColor = focused ? AppTheme.PRIMARY : AppTheme.BORDER;
        tc.setBorder(new RoundedBorder(borderColor, focused ? 2 : 1, 8, new Insets(8, 10, 8, 10)));
        tc.repaint();
    }

    public static LabeledInput text(String labelText, int columns) {
        JTextField field = new JTextField(columns);
        return new LabeledInput(labelText, field);
    }

    public static LabeledInput password(String labelText, int columns) {
        JPasswordField field = new JPasswordField(columns);
        return new LabeledInput(labelText, field);
    }

    public String getText() {
        if (input instanceof JTextField tf) return tf.getText();
        return "";
    }

    public char[] getPassword() {
        if (input instanceof JPasswordField pf) return pf.getPassword();
        return new char[0];
    }

    public void setText(String value) {
        if (input instanceof JTextComponent tc) tc.setText(value);
    }

    public void clear() { setText(""); }

    // Custom rounded border with padding
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, arc;
        private final Insets padding;
        RoundedBorder(Color color, int thickness, int arc, Insets padding) {
            this.color = color; this.thickness = thickness;
            this.arc = arc; this.padding = padding;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float
                    (x + 0.5f, y + 0.5f, w - 1, h - 1, arc, arc));
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(padding.top + thickness, padding.left + thickness,
                              padding.bottom + thickness, padding.right + thickness);
        }
    }
}

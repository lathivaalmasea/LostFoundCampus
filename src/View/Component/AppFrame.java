package View.Component;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AppFrame extends JFrame {

    private final JFrame parentFrame;

    protected AppFrame(String title, Dimension size) {
        this(title, size, null);
    }

    protected AppFrame(String title, Dimension size, JFrame parentFrame) {
        setTitle(title);
        setSize(size);
        this.parentFrame = parentFrame;
        setDefaultCloseOperation(parentFrame == null ? EXIT_ON_CLOSE : DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    protected JPanel createScreenPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        return panel;
    }

    protected void setScreenContent(Container content) {
        setContentPane(content);
    }

    protected boolean hasParentFrame() {
        return parentFrame != null;
    }

    protected JFrame getParentFrame() {
        return parentFrame;
    }

    protected void showChildFrame(JFrame childFrame) {
        setVisible(false);
        childFrame.setVisible(true);
    }

    protected void backToParent() {
        dispose();
        if (parentFrame != null) {
            parentFrame.setLocationRelativeTo(null);
            parentFrame.setVisible(true);
        }
    }
}
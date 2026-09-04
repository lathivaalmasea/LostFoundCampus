package View.Component;

import java.awt.*;
import javax.swing.*;

public final class AppLabelFactory {

    private AppLabelFactory() {}
    
    public static JLabel title(String text) {
        return create(text, AppTheme.TITLE_FONT, 
                AppTheme.PRIMARY, JLabel.CENTER);
    }

    public static JLabel sectionTitle(String text) {
        return create(text, AppTheme.SECTION_TITLE_FONT, 
                AppTheme.TEXT_PRIMARY, JLabel.LEFT);
    }

    public static JLabel body(String text) {
        return create(text, AppTheme.BODY_FONT, 
                AppTheme.TEXT_SECONDARY, JLabel.LEFT);
    }

    public static JLabel create(String text, Font font, Color color, int align) {
        JLabel label = new JLabel(text, align);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
}
package View.Component;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public final class AppTableFactory {

    private AppTableFactory() {}

    public static void style(JTable table) {
        table.setFont(AppTheme.BODY_FONT);
        table.setRowHeight(36);
        table.setGridColor(AppTheme.BORDER);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(AppTheme.PRIMARY_LIGHT);
        table.setSelectionForeground(AppTheme.PRIMARY);
        table.setBackground(AppTheme.SURFACE);
        table.setForeground(AppTheme.TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ?
                            AppTheme.SURFACE : AppTheme.TABLE_STRIPE);
                    String val = value != null ? value.toString() : "";
                    if (val.equalsIgnoreCase("Sudah Diklaim")
                            || val.equalsIgnoreCase("Approved")) {
                        c.setForeground(AppTheme.SUCCESS);
                        ((JLabel)c).setFont(AppTheme.LABEL_FONT);
                    } else if (val.equalsIgnoreCase("Pending")
                            || val.equalsIgnoreCase("Belum Diklaim")) {
                        c.setForeground(AppTheme.WARNING);
                        ((JLabel)c).setFont(AppTheme.LABEL_FONT);
                    } else if (val.equalsIgnoreCase("Rejected")) {
                        c.setForeground(AppTheme.DANGER);
                        ((JLabel)c).setFont(AppTheme.LABEL_FONT);
                    } else {
                        c.setForeground(AppTheme.TEXT_PRIMARY);
                        ((JLabel)c).setFont(AppTheme.BODY_FONT);
                    }
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                // Tooltip: tampilkan teks lengkap saat hover
                String fullText = value != null ? value.toString() : "";
                ((JLabel) c).setToolTipText(fullText.isEmpty() ? null : fullText);
                return c;
            }
        });

        // Double-click untuk lihat detail baris
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DetailDialog.show(table, table, "Detail Data");
                }
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent
                    (t, value, isSelected, hasFocus, row, col);
                lbl.setBackground(AppTheme.TABLE_HEADER);
                lbl.setForeground(AppTheme.PRIMARY);
                lbl.setFont(AppTheme.BUTTON_FONT);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.PRIMARY),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                lbl.setOpaque(true);
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        header.setBackground(AppTheme.TABLE_HEADER);
    }

    public static void styleSearchField(JTextField field) {
        field.setFont(AppTheme.BODY_FONT);
        field.setBackground(AppTheme.SURFACE);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setCaretColor(AppTheme.PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }

}

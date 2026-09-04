package View.Component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class AppTheme {

    // COLOR PALETTE (Indigo/Teal harmony)
    public static final Color BACKGROUND       = new Color(240, 244, 248);
    public static final Color SURFACE          = Color.WHITE;
    public static final Color SIDEBAR          = new Color(22, 33, 62);       // deep indigo
    public static final Color SIDEBAR_HOVER    = new Color(41, 55, 99);
 
    public static final Color PRIMARY          = new Color(67, 97, 238);      // vivid indigo
    public static final Color PRIMARY_LIGHT    = new Color(224, 229, 253);
 
    public static final Color ACCENT           = new Color(72, 199, 186);     // teal accent
 
    public static final Color DANGER           = new Color(239, 68, 68);
    public static final Color SUCCESS          = new Color(16, 185, 129);
    public static final Color WARNING          = new Color(245, 158, 11);
 
    public static final Color TEXT_PRIMARY     = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY   = new Color(75, 85, 99);
    public static final Color TEXT_MUTED       = new Color(156, 163, 175);
    public static final Color TEXT_ON_PRIMARY  = Color.WHITE;
 
    public static final Color BORDER           = new Color(229, 231, 235);
 
    public static final Color TABLE_STRIPE     = new Color(249, 250, 251);
    public static final Color TABLE_HEADER     = new Color(239, 242, 255);
 
    // TYPOGRAPHY
    public static final Font TITLE_FONT        = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SECTION_TITLE_FONT= new Font("Segoe UI", Font.BOLD, 18);
    public static final Font CARD_TITLE_FONT   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font SUBTITLE_FONT     = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LABEL_FONT        = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font BODY_FONT         = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT       = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font SIDEBAR_FONT      = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font SMALL_FONT        = new Font("Segoe UI", Font.PLAIN, 11);
 
    // WINDOW SIZES
    public static final Dimension WINDOW_AUTH           = new Dimension(460, 360);
    public static final Dimension WINDOW_AUTH_REGISTER  = new Dimension(440, 440);
    public static final Dimension WINDOW_HOME           = new Dimension(520, 460);
    public static final Dimension WINDOW_FORM           = new Dimension(560, 620);
    public static final Dimension WINDOW_TABLE          = new Dimension(900, 580);
    public static final Dimension WINDOW_DASHBOARD      = new Dimension(1000, 660);
 
    // DIALOG SIZES
    public static final Dimension DIALOG_CLAIM          = new Dimension(480, 360);
 
    private AppTheme() {}
}

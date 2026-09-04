/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import View.HomeView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 *
 * @author Ivaa
 */
public class Main {

    public static void main(String[] args) {
        // Pakai tampilan sistem operasi (lebih rapi di Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Kalau gagal, pakai tampilan default Java saja
            System.out.println("Menggunakan look-and-feel default.");
        }

        // Jalankan GUI di Event Dispatch Thread (EDT) — aturan baku Swing
        SwingUtilities.invokeLater(() -> {
            new HomeView().setVisible(true); // Buka halaman awal aplikasi
        });
    }
}

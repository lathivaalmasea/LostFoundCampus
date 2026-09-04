/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.User.DAOUser;
import Model.User.ModelUser;
/**
 *
 * @author karina
 */
public class ControllerUser {

    DAOUser daoUser;

    public ControllerUser(){
        daoUser = new DAOUser();
    }

    public void insert(ModelUser user){
        daoUser.insert(user);
    }
    
    /**
     * Mendaftarkan user baru.
     * Melempar IllegalArgumentException jika ada validasi yang gagal
     * (field kosong, password tidak cocok, atau username sudah dipakai).
     */
    public void register(String nama, String username, String password, String confirm, String noTelp) {

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            throw new IllegalArgumentException("Semua field harus diisi!");
        }

        if (!password.equals(confirm)) {
            throw new IllegalArgumentException("Konfirmasi password tidak cocok!");
        }

        if (daoUser.existsByUsername(username)) {
            throw new IllegalArgumentException(
                "Username \"" + username + "\" sudah digunakan. Silakan pilih username lain.");
        }

        ModelUser user = new ModelUser();
        user.setNama(nama);
        user.setUsername(username);
        user.setPassword(password);
        user.setNoTelp(noTelp.isEmpty() ? null : noTelp);
        daoUser.insert(user);
    }

    /** Overload lama — tanpa noTelp (backward compat). */
    public void register(String nama, String username, String password, String confirm) {
        register(nama, username, password, confirm, "");
    }
}

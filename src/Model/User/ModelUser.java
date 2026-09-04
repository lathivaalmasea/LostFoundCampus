/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.User;

/**
 *
 * @author Ivaa
 */
public class ModelUser {

    private int id;
    private String username;
    private String password;
    private String nama;
    private String role;
    private String noTelp;  // opsional — untuk kontak serah terima barang
 
    public int getId() { return id; }
    public void setId(int id) { 
        this.id = id; 
    }
 
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
 
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
 
    public String getNama() { 
        return nama; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }
 
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
 
    public String getNoTelp() { 
        return noTelp; 
    }
    
    public void setNoTelp(String noTelp) { 
        this.noTelp = noTelp; 
    }
}

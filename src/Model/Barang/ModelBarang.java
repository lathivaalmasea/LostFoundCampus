/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Barang;

/**
 *
 * @author Ivaa
 */
public class ModelBarang {

    private int id;
    private String namaBarang;
    private String kategori;
    private String deskripsi;
    private String lokasi;
    private String status;
    private String statusClaim;
    private int userId;
    private int claimedByUserId;
    private int pendingClaimCount;
    private String ownerName;
    private String claimedByName;
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusClaim() {
        return statusClaim;
    }

    public void setStatusClaim(String statusClaim) {
        this.statusClaim = statusClaim;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClaimedByUserId() {
        return claimedByUserId;
    }

    public void setClaimedByUserId(int claimedByUserId) {
        this.claimedByUserId = claimedByUserId;
    }

    public int getPendingClaimCount() {
        return pendingClaimCount;
    }

    public void setPendingClaimCount(int pendingClaimCount) {
        this.pendingClaimCount = pendingClaimCount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getClaimedByName() {
        return claimedByName;
    }

    public void setClaimedByName(String claimedByName) {
        this.claimedByName = claimedByName;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
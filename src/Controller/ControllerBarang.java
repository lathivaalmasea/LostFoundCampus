package Controller;

import Model.Barang.*;
import java.util.List;

public class ControllerBarang {
    DAOBarang daoBarang;

    public ControllerBarang(){
        daoBarang = new DAOBarang();
    }

    public void insert(ModelBarang barang){ 
        daoBarang.insert(barang); 
    }
    
    public void update(ModelBarang barang){ 
        daoBarang.update(barang); }
    public void delete(int id){ daoBarang.delete(id); 
    }
    
    public List<ModelBarang> getAll(){ 
        return daoBarang.getAll(); 
    }
    
    public List<ModelBarang> getAllByUserId(int userId) { 
        return daoBarang.getAllByUserId(userId); 
    }
    
    public List<ModelBarang> search(String keyword){ 
        return daoBarang.search(keyword); 
    }
    
    public List<ModelBarang> searchByUserId(int userId, String keyword) { 
        return daoBarang.searchByUserId(userId, keyword); 
    }
    
    public ModelBarang getById(int id){ 
        return daoBarang.getById(id); 
    }
    
    public int getTotalBarang(){ 
        return daoBarang.getTotalBarang(); 
    }
    
    public int getTotalByStatus(String status){ 
        return daoBarang.getTotalByStatus(status); 
    }
    
    public List<ModelBarang> getReturnedBarang(){ 
        return daoBarang.getReturnedBarang(); 
    }
    
    public int getTotalByUserId(int userId) { 
        return daoBarang.getTotalByUserId(userId); 
    }
    
    public int getTotalByUserIdAndStatus(int userId, String status) { 
        return daoBarang.getTotalByUserIdAndStatus(userId, status); 
    }
    
    public int getTotalApprovedClaimByUserId(int userId) { 
        return daoBarang.getTotalApprovedClaimByUserId(userId); 
    }
    
    public int getTotalByStatusClaim(String statusClaim) { 
        return daoBarang.getTotalByStatusClaim(statusClaim); 
    }

    /**
     * Pelapor menarik laporannya sendiri selama belum ada yang klaim.
     * @return true jika berhasil ditarik
     */
    public boolean tarikLaporan(int barangId, int userId) {
        return daoBarang.tarikLaporan(barangId, userId);
    }
}

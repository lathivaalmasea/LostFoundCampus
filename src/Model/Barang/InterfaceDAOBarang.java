/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.Barang;

import java.util.List;
/**
 *
 * @author Ivaa
 */
public interface InterfaceDAOBarang {
    
    public void insert(ModelBarang barang);

    public void update(ModelBarang barang);

    public void delete(int id);

    public List<ModelBarang> getAll();

    public List<ModelBarang> getAllByUserId(int userId);

    public List<ModelBarang> search(String keyword);

    public List<ModelBarang> searchByUserId(int userId, String keyword);
    
    public ModelBarang getById(int id);

    public int getTotalBarang();

    public int getTotalByStatus(String status);

    public List<ModelBarang> getReturnedBarang();
}
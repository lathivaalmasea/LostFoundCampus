/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.User;

import java.util.List;
/**
 *
 * @author Ivaa
 */
public interface InterfaceDAOUser {

    public void insert(ModelUser user);

    public ModelUser getById(int id);

    public ModelUser getByUsername(String username);

    public List<ModelUser> getAll();

    public void update(ModelUser user);

    public void delete(int id);
}
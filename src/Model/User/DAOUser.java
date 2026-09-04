/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.User;

import Model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ivaa
 */
public class DAOUser implements InterfaceDAOUser {

    private static final Logger LOGGER = Logger.getLogger(DAOUser.class.getName());
 
    private static final String COL_ID       = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_NAMA     = "nama";
    private static final String COL_ROLE     = "role";
    private static final String COL_NO_TELP  = "no_telp";
 
    private static final String SELECT_COLUMNS =
            "id, username, password, nama, role, no_telp";
 
    private static final String SQL_INSERT =
            "INSERT INTO users (username, password, nama, role, no_telp) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_GET_BY_ID =
            "SELECT " + SELECT_COLUMNS + " FROM users WHERE id = ?";
    private static final String SQL_GET_BY_USERNAME =
            "SELECT " + SELECT_COLUMNS + " FROM users WHERE username = ?";
    private static final String SQL_GET_ALL =
            "SELECT " + SELECT_COLUMNS + " FROM users";
    private static final String SQL_UPDATE =
            "UPDATE users SET username = ?, password = ?, nama = ?, role = ?, no_telp = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM users WHERE id = ?";
    private static final String SQL_EXISTS_USERNAME =
            "SELECT 1 FROM users WHERE username = ?";
 
    private final Connection connection;
 
    public DAOUser() {
        this.connection = DatabaseConnection.getConnection();
    }
 
    @Override
    public void insert(ModelUser user) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNama());
            ps.setString(4, "user");
            ps.setString(5, user.getNoTelp());
            ps.executeUpdate();
        } catch (SQLException e) {
            logSqlError("insert user", e);
        }
    }
 
    @Override
    public ModelUser getById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs, true);
        } catch (SQLException e) {
            logSqlError("get user by id", e);
        }
        return null;
    }
 
    @Override
    public ModelUser getByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs, true);
        } catch (SQLException e) {
            logSqlError("get user by username", e);
        }
        return null;
    }
 
    @Override
    public List<ModelUser> getAll() {
        List<ModelUser> users = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SQL_GET_ALL)) {
            while (rs.next()) users.add(mapUser(rs, false));
        } catch (SQLException e) {
            logSqlError("get all users", e);
        }
        return users;
    }
 
    @Override
    public void update(ModelUser user) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNama());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getNoTelp());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logSqlError("update user", e);
        }
    }
 
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logSqlError("delete user", e);
        }
    }
 
    public boolean existsByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_EXISTS_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logSqlError("check username exists", e);
        }
        return false;
    }
 
    private static ModelUser mapUser(ResultSet rs, boolean includePassword) throws SQLException {
        ModelUser user = new ModelUser();
        user.setId(rs.getInt(COL_ID));
        user.setUsername(rs.getString(COL_USERNAME));
        if (includePassword) user.setPassword(rs.getString(COL_PASSWORD));
        user.setNama(rs.getString(COL_NAMA));
        user.setRole(rs.getString(COL_ROLE));
        user.setNoTelp(rs.getString(COL_NO_TELP));
        return user;
    }
 
    private static void logSqlError(String action, SQLException e) {
        LOGGER.log(Level.SEVERE, () -> "Failed to " + action + ": " + e.getMessage());
        LOGGER.log(Level.SEVERE, "SQL exception stacktrace", e);
    }
}

package com.ecotracker.dao;

import com.ecotracker.util.DBConnection;
import java.sql.*;

public class UserDAO {

    
    public boolean emailExists(String email) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT id FROM users WHERE email=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    
    public int registerUser(String name, String email, String password) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO users (name, email, password, role, bio, location, profile_photo, created_at) " +
                         "VALUES (?, ?, ?, 'user', '', '', NULL, NOW())";

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // return userId
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

   
    public ResultSet loginUser(String email, String password) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT id, name, email, role, bio, location, profile_photo " +
                         "FROM users WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
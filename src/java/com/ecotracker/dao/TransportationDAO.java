package com.ecotracker.dao;

import com.ecotracker.util.DBConnection;
import java.sql.*;

public class TransportationDAO {

    public void saveBatch(int userId, String userName, String routine,
                          String[] modes, String[] distances) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO transportation_logs " +
                    "(user_id, user_name, routine_type, transport_mode, distance_km, emission_kg) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < modes.length; i++) {

                if (distances[i] == null || distances[i].trim().isEmpty()) continue;

                double distance = Double.parseDouble(distances[i]);
                double emission = distance * getEmissionFactor(modes[i]);

                ps.setInt(1, userId);
                ps.setString(2, userName);
                ps.setString(3, routine);
                ps.setString(4, modes[i]);
                ps.setDouble(5, distance);
                ps.setDouble(6, emission);

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getEmissionFactor(String mode) {
        switch (mode) {
            case "Bike / Scooter (Petrol)": return 0.12;
            case "Electric Two-Wheeler": return 0.02;
            case "Car (Petrol)": return 0.21;
            case "Car (Diesel)": return 0.24;
            case "Car (CNG)": return 0.16;
            case "Electric Car": return 0.05;
            case "Auto Rickshaw": return 0.13;
            case "Bus": return 0.08;
            case "Metro / Local Train": return 0.04;
            case "Indian Railways": return 0.03;
            case "Flight (Domestic)": return 0.15;
            default: return 0.1;
        }
    }
}
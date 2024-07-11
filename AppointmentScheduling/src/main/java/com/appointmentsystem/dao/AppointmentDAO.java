package com.appointmentsystem.dao;

import com.appointmentsystem.database.Database;
import com.appointmentsystem.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public void addAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (client_id, date_time, description) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getClientId());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getDate()));
            stmt.setString(3, appointment.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing the exception for higher layers to handle
        }
    }

    public List<Appointment> getAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setClientId(rs.getInt("client_id"));
                appointment.setDate(rs.getTimestamp("date_time").toLocalDateTime());
                appointment.setDescription(rs.getString("description"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing the exception for higher layers to handle
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String query = "UPDATE appointments SET client_id = ?, date_time = ?, description = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getClientId());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getDate()));
            stmt.setString(3, appointment.getDescription());
            stmt.setInt(4, appointment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing the exception for higher layers to handle
        }
    }

    public void deleteAppointment(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing the exception for higher layers to handle
        }
    }
}

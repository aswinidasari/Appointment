package com.appointmentsystem.ui;

import java.io.Serializable;
import java.sql.SQLException; // Import SQLException from java.sql package
import java.util.List;

import com.appointmentsystem.dao.AppointmentDAO;
import com.appointmentsystem.model.Appointment;

public class AppointmentManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;

    public AppointmentManager() {
        appointmentDAO = new AppointmentDAO();
    }

    public void bookAppointment(Appointment appointment) throws SQLException {
        appointmentDAO.addAppointment(appointment);
    }

    public void cancelAppointment(int appointmentId) throws SQLException {
        appointmentDAO.deleteAppointment(appointmentId);
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        return appointmentDAO.getAppointments();
    }
}

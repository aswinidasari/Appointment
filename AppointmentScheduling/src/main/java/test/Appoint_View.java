package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/Appoint_View")
public class Appoint_View extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();

        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM appointments ORDER BY appointment_date ASC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    Timestamp appointmentDate = rs.getTimestamp("appointment_date");
                    String reminderMessage = rs.getString("reminder_message");
                    int reminderDelay = rs.getInt("reminder_delay");

                    Appointment appointment = new Appointment(customerId, appointmentDate, reminderMessage, reminderDelay);
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error fetching appointments from database!");
            return;
        }

        // Convert appointments list to JSON and send response
        writer.println("[");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            writer.println("{");
            writer.println("\"customer_id\": " + appointment.getCustomerId() + ",");
            writer.println("\"appointment_date\": \"" + appointment.getAppointmentDate() + "\",");
            writer.println("\"reminder_message\": \"" + appointment.getReminderMessage() + "\",");
            writer.println("\"reminder_delay\": " + appointment.getReminderDelay());
            writer.println("}");
            if (i < appointments.size() - 1) {
                writer.println(",");
            }
        }
        writer.println("]");
    }
}

// Helper class for Appointment entity
class Appointment {
    private int customerId;
    private Timestamp appointmentDate;
    private String reminderMessage;
    private int reminderDelay;

    public Appointment(int customerId, Timestamp appointmentDate, String reminderMessage, int reminderDelay) {
        this.customerId = customerId;
        this.appointmentDate = appointmentDate;
        this.reminderMessage = reminderMessage;
        this.reminderDelay = reminderDelay;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public int getReminderDelay() {
        return reminderDelay;
    }
}
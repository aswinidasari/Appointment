package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/Appoint_Reschedule")
public class Appoint_Reschedule extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        // Get parameters from the request
        String appointmentId = req.getParameter("appointment_id");
        String newDateTime = req.getParameter("new_datetime");
        String newReminderMessage = req.getParameter("new_reminder_message");
        String newReminderDelay = req.getParameter("new_reminder_delay");

        // Validate input fields
        if (appointmentId == null || appointmentId.isEmpty() || newDateTime == null || newDateTime.isEmpty()) {
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('Please fill in all required fields.');");
            writer.println("window.location.href = 'Reschedule_Appoint.html';"); 
            writer.println("</script>");
            writer.println("</body></html>");
            return;
        }

        // Parse new date and time
        Timestamp newAppointmentDateTime = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setLenient(false);
            newAppointmentDateTime = new Timestamp(dateFormat.parse(newDateTime).getTime());
        } catch (ParseException e) {
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('Invalid date format! Please use yyyy-MM-dd HH:mm:ss.');");
            writer.println("window.location.href = 'Reschedule_Appoint.html';"); 
            writer.println("</script>");
            writer.println("</body></html>");
            return;
        }

        // Check if the appointment ID exists
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM appointments WHERE appointment_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(appointmentId));
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt(1) == 0) {
                        writer.println("<html><body>");
                        writer.println("<script type='text/javascript'>");
                        writer.println("alert('Appointment ID does not exist.');");
                        writer.println("window.location.href = 'Reschedule_Appoint.html';");
                        writer.println("</script>");
                        writer.println("</body></html>");
                        return;
                    }
                }
            }

            // Update the appointment
            String updateSql = "UPDATE appointments SET appointment_date = ?, reminder_message = ?, reminder_delay = ? WHERE appointment_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setTimestamp(1, newAppointmentDateTime);
                pstmt.setString(2, newReminderMessage);

                if (newReminderDelay != null && !newReminderDelay.isEmpty()) {
                    pstmt.setInt(3, Integer.parseInt(newReminderDelay));
                } else {
                    pstmt.setNull(3, java.sql.Types.INTEGER);
                }

                pstmt.setInt(4, Integer.parseInt(appointmentId));

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    writer.println("<html><body>");
                    writer.println("<script type='text/javascript'>");
                    writer.println("alert('Appointment Rescheduled:\\nAppointment ID: " + appointmentId + "\\nNew Date & Time: " + newDateTime + "\\nNew Reminder Message: " + newReminderMessage + "\\nNew Reminder Delay: " + newReminderDelay + "');");
                    writer.println("window.location.href = 'Reschedule_Appoint.html';");
                    writer.println("</script>");
                    writer.println("</body></html>");
                } else {
                    writer.println("<html><body>");
                    writer.println("<script type='text/javascript'>");
                    writer.println("alert('Failed to reschedule appointment.');");
                    writer.println("window.location.href = 'Reschedule_Appoint.html';");
                    writer.println("</script>");
                    writer.println("</body></html>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('Error updating appointment in the database!');");
            writer.println("window.location.href = 'Reschedule_Appoint.html';");
            writer.println("</script>");
            writer.println("</body></html>");
        }
    }
}

package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/Appoint_Cancel")
public class Appoint_Cancel extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        // Get parameters from the request
        String appointmentId = req.getParameter("appointment_id");

        // Prepare the result message
        String message;

        // Delete appointment from the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM appointments WHERE appointment_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(appointmentId));

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    message = "Appointment successfully canceled!";
                } else {
                    message = "Failed to cancel appointment. Please check the appointment ID.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error canceling appointment in the database!";
        }

        // Print the result in an alert box and redirect to the signup form page
        writer.println("<html><body>");
        writer.println("<script type='text/javascript'>");
        writer.println("alert('" + message + "');");
        writer.println("window.location.href = 'Cancel_Appoint.html';"); // Adjust this to the correct path of your signup form
        writer.println("</script>");
        writer.println("</body></html>");
    }
}
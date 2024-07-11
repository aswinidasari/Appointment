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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/Appoint_insert")
public class Appoint_Insert extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        // Get parameters from the request
        String customerId = req.getParameter("customer_id");
        String datetime = req.getParameter("appointment_datetime");
        String reminderMessage = req.getParameter("reminder_message");
        String reminderDelay = req.getParameter("reminder_delay");

        // Validate and parse date and time
        Timestamp appointmentDate = null;
        try {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datetimeFormat.setLenient(false);
            appointmentDate = new Timestamp(datetimeFormat.parse(datetime).getTime());
        } catch (ParseException e) {
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('Invalid date or time format! Please use the provided date and time picker.');");
            writer.println("window.location.href = 'Appoint_Insert.html';"); // Adjust this to the correct path of your form
            writer.println("</script>");
            writer.println("</body></html>");
            return;
        }

        // Check for date conflicts in the database
        boolean dateConflict = false;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setTimestamp(1, appointmentDate);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        dateConflict = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('Error checking date conflicts in the database!');");
            writer.println("window.location.href = 'Appoint_Insert.html';"); // Adjust this to the correct path of your form
            writer.println("</script>");
            writer.println("</body></html>");
            return;
        }

        if (dateConflict) {
            writer.println("<html><body>");
            writer.println("<script type='text/javascript'>");
            writer.println("alert('The appointment date already exists. Please choose a different date.');");
            writer.println("window.location.href = 'Appoint_Insert.html';"); // Adjust this to the correct path of your form
            writer.println("</script>");
            writer.println("</body></html>");
            return;
        }

        // Insert data into the database and retrieve appointment ID
        int appointmentId = -1;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO appointments (customer_id, appointment_date, reminder_message, reminder_delay) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, Integer.parseInt(customerId));
                pstmt.setTimestamp(2, appointmentDate);
                pstmt.setString(3, reminderMessage);
                if (reminderDelay != null && !reminderDelay.isEmpty()) {
                    pstmt.setInt(4, Integer.parseInt(reminderDelay));
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Retrieve the generated appointment ID
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        appointmentId = generatedKeys.getInt(1);
                    } else {
                        writer.println("<html><body>");
                        writer.println("<p>Failed to retrieve appointment ID.</p>");
                        writer.println("</body></html>");
                        return;
                    }
                } else {
                    writer.println("<html><body>");
                    writer.println("<p>Failed to add appointment details to the database.</p>");
                    writer.println("</body></html>");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<html><body>");
            writer.println("<p>Error inserting data into database!</p>");
            writer.println("</body></html>");
            return;
        }

        // Print parameters to the response in an alert box and redirect to form
        writer.println("<html><body>");
        writer.println("<script type='text/javascript'>");
        writer.println("alert('Appointment Details:\\nCustomer ID: " + customerId + "\\nDate & Time: " + datetime + "\\nReminder Message: " + reminderMessage + "\\nReminder Delay: " + reminderDelay + "\\nAppointment ID: " + appointmentId + "');");
        writer.println("window.location.href = 'Appoint_Insert.html';"); // Adjust this to the correct path of your form
        writer.println("</script>");
        writer.println("</body></html>");
    }
}

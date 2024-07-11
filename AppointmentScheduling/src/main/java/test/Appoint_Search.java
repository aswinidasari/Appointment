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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/Appoint_Search")
public class Appoint_Search extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        List<Appointment> appointments = new ArrayList<>();

        // Get parameters from the request
        String customerId = req.getParameter("customer_id");
        String startDateStr = req.getParameter("start_date");

        // Prepare SQL query based on provided parameters
        StringBuilder sql = new StringBuilder("SELECT * FROM appointments WHERE 1=1");

        if (customerId != null && !customerId.isEmpty()) {
            sql.append(" AND customer_id = ?");
        }
        if (startDateStr != null && !startDateStr.isEmpty()) {
            sql.append(" AND appointment_date >= ?");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                int parameterIndex = 1;

                if (customerId != null && !customerId.isEmpty()) {
                    pstmt.setInt(parameterIndex++, Integer.parseInt(customerId));
                }
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    Timestamp startDate = parseTimestamp(startDateStr);
                    pstmt.setTimestamp(parameterIndex++, startDate);
                }

                // Execute query
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int custId = rs.getInt("customer_id");
                    Timestamp appDate = rs.getTimestamp("appointment_date");
                    String reminderMsg = rs.getString("reminder_message");
                    int reminderDelay = rs.getInt("reminder_delay");

                    Appointment appointment = new Appointment(custId, appDate, reminderMsg, reminderDelay);
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<p>Error fetching appointments from database!</p>");
            return;
        }

        // Display appointments on the webpage
        writer.println("<!DOCTYPE html>");
        writer.println("<html lang=\"en\">");
        writer.println("<head>");
        writer.println("<meta charset=\"UTF-8\">");
        writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        writer.println("<title>Search Results</title>");
        writer.println("<style>");
        writer.println("body { font-family: Arial, sans-serif; background-color: #f0f0f0; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
        writer.println(".results-container { background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        writer.println(".results-container h1 { margin-bottom: 20px; }");
        writer.println(".appointment { margin-bottom: 10px; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }");
        writer.println("</style>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<div class=\"results-container\">");
        writer.println("<h1>Search Results</h1>");

        if (appointments.isEmpty()) {
            writer.println("<p>No appointments found matching the criteria.</p>");
        } else {
            for (Appointment appointment : appointments) {
                writer.println("<div class=\"appointment\">");
                writer.println("<p><strong>Customer ID:</strong> " + appointment.getCustomerId() + "</p>");
                writer.println("<p><strong>Appointment Date:</strong> " + appointment.getAppointmentDate() + "</p>");
                writer.println("<p><strong>Reminder Message:</strong> " + appointment.getReminderMessage() + "</p>");
                writer.println("<p><strong>Reminder Delay:</strong> " + appointment.getReminderDelay() + "</p>");
                writer.println("</div>");
            }
        }

        writer.println("</div>");
        writer.println("</body>");
        writer.println("</html>");
    }

    // Helper method to parse Timestamp from string
    private Timestamp parseTimestamp(String timestampStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setLenient(false);
            return new Timestamp(dateFormat.parse(timestampStr).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format for timestamp: " + timestampStr);
        }
    }
}

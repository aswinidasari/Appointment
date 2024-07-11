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
@WebServlet("/CustomerDelete")
public class CustomerDeleteServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        // Get parameters from the request
        String customerId = req.getParameter("customer_id");

        // Print parameters to the response
        writer.println("<html><body>");
        writer.println("<h1>Delete Customer</h1>");
        writer.println("<p>Customer ID: " + customerId + "</p>");

        // Delete data in the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Begin transaction
            conn.setAutoCommit(false);

            try {
                // First, delete associated records from appointments
                String deleteAppointmentsSql = "DELETE FROM appointments WHERE customer_id = ?";
                try (PreparedStatement pstmtAppointments = conn.prepareStatement(deleteAppointmentsSql)) {
                    pstmtAppointments.setInt(1, Integer.parseInt(customerId));
                    pstmtAppointments.executeUpdate();
                }

                // Then, delete the customer
                String deleteCustomerSql = "DELETE FROM customers WHERE customer_id = ?";
                try (PreparedStatement pstmtCustomer = conn.prepareStatement(deleteCustomerSql)) {
                    pstmtCustomer.setInt(1, Integer.parseInt(customerId));
                    int rowsDeleted = pstmtCustomer.executeUpdate();

                    if (rowsDeleted > 0) {
                        writer.println("<p>Customer successfully deleted from the database!</p>");
                    } else {
                        writer.println("<p>Failed to delete customer from the database. Please check the Customer ID.</p>");
                    }
                }

                // Commit transaction
                conn.commit();
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                e.printStackTrace();
                writer.println("<p>Error deleting data from the database!</p>");
            } finally {
                // Reset auto commit
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<p>Error connecting to the database!</p>");
        }

        writer.println("</body></html>");
    }
}
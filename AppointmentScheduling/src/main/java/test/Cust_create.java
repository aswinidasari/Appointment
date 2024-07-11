package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/customer")
public class Cust_create extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        // Get parameters from the request
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        // Print parameters to the response
        writer.println("<html><body>");
        writer.println("<h1>Customer Details</h1>");
        writer.println("<p>Name: " + name + "</p>");
        writer.println("<p>Email: " + email + "</p>");

        // Insert data into the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO customers (name, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    writer.println("<p>Customer successfully added to the database!</p>");
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1);
                        writer.println("<p>Generated Customer ID: " + customerId + "</p>");
                    }
                } else {
                    writer.println("<p>Failed to add customer to the database.</p>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<p>Error inserting data into database!</p>");
        }

        writer.println("</body></html>");
    }
}

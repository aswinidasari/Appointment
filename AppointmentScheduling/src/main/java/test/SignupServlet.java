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
@WebServlet("/signup")
public class SignupServlet extends GenericServlet {
    
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();

        // Get parameters from the request
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm-password");

        // Validate the input
        if (email == null || password == null || confirmPassword == null ||
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            writer.println("<html><body>");
            writer.println("<h1>Signup Failed</h1>");
            writer.println("<p>All fields are required.</p>");
            writer.println("</body></html>");
            return;
        }

        if (!password.equals(confirmPassword)) {
            writer.println("<html><body>");
            writer.println("<h1>Signup Failed</h1>");
            writer.println("<p>Passwords do not match.</p>");
            writer.println("</body></html>");
            return;
        }

        // Insert the new user into the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);  // Note: In a real application, you should hash the password before storing it
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    writer.println("<html><body>");
                    writer.println("<h1>Signup Successful</h1>");
                    writer.println("<p>Welcome, " + email + "!</p>");
                    writer.println("</body></html>");
                } else {
                    writer.println("<html><body>");
                    writer.println("<h1>Signup Failed</h1>");
                    writer.println("<p>Unable to create your account. Please try again later.</p>");
                    writer.println("</body></html>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<html><body>");
            writer.println("<h1>Error</h1>");
            writer.println("<p>Database error!</p>");
            writer.println("</body></html>");
        }
    }
}
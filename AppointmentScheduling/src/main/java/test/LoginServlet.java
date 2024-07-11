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
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        // Get parameters from the request
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Validate the user credentials
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Redirect to app.html after successful login
                    httpResponse.sendRedirect("main.html");
                } else {
                    writer.println("<html><body>");
                    writer.println("<h1>Login Failed</h1>");
                    writer.println("<p>Invalid email or password.</p>");
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
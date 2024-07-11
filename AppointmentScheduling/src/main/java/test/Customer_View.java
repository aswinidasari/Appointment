package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused"})
@WebServlet("/Customer_View")
public class Customer_View extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();

        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM customers";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("customer_id"); // Updated to match the column name in the table
                    String name = rs.getString("name");
                    String email = rs.getString("email");

                    Customer customer = new Customer(id, name, email);
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error fetching customers from database!");
            return;
        }

        // Convert customers list to JSON and send response
        writer.println("[");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            writer.println("{");
            writer.println("\"id\": " + customer.getId() + ",");
            writer.println("\"name\": \"" + customer.getName() + "\",");
            writer.println("\"email\": \"" + customer.getEmail() + "\"");
            writer.println("}");
            if (i < customers.size() - 1) {
                writer.println(",");
            }
        }
        writer.println("]");
    }
}

// Helper class for Customer entity
class Customer {
    private int id;
    private String name;
    private String email;

    public Customer(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}



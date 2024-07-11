package com.appointmentsystem.servlet;

import com.appointmentsystem.dao.ClientDAO;
import com.appointmentsystem.model.Client;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;

    @Override
    public void init() {
        clientDAO = new ClientDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("book".equals(action)) {
            // Handle booking logic
            try {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                Client client = new Client();
                client.setName(name);
                client.setEmail(email);
                client.setPhone(phone);
                clientDAO.addClient(client);
                response.sendRedirect("appointments");
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else if ("cancel".equals(action)) {
            // Handle canceling logic
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle retrieving appointment list
        try {
            request.setAttribute("clients", clientDAO.getClients());
            request.getRequestDispatcher("/jsp/appointment.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

package com.appointmentsystem.dao;

import com.appointmentsystem.database.Database;
import com.appointmentsystem.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private static final String INSERT_CLIENT_SQL = "INSERT INTO clients (name, email, phone) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM clients";

    public void addClient(Client client) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT_SQL)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getPhone());
            preparedStatement.executeUpdate();
        }
    }

    public List<Client> getClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                clients.add(client);
            }
        }
        return clients;
    }
}

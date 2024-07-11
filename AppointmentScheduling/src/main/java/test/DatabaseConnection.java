package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Appointment_Database";
    private static final String USER = "postgres";  
    private static final String PASSWORD = "Ashu1212";  

    public static Connection getConnection() {
    	Connection connection = null;
    	try {
    		Class.forName("org.postgresql.Driver");
    		System.out.println("Connecting to Database.....");
    		connection = DriverManager.getConnection(URL,USER,PASSWORD);
    		System.out.println("Connected to Database");
    	} catch (ClassNotFoundException | SQLException e){
    		e.printStackTrace();
    	}
		return connection;
    }
}

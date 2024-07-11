package com.appointmentsystem.model;


	import java.time.LocalDateTime;

	public class Appointment {
	    private int id;
	    private int clientId;
	    private LocalDateTime date;
	    private String description;

	    // Getters and setters
	    public int getId() { return id; }
	    public void setId(int id) { this.id = id; }
	    public int getClientId() { return clientId; }
	    public void setClientId(int clientId) { this.clientId = clientId; }
	    public LocalDateTime getDate() { return date; }
	    public void setDate(LocalDateTime date) { this.date = date; }
	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; 
	    
	 }
}

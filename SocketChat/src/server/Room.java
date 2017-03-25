package server;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
	private int ID;
	private String name;
	private transient ArrayList<ClientHandler> members;
	
	public Room(int ID, ClientHandler creator, String name) {
		this.ID = ID;
		this.members = new ArrayList<ClientHandler>();
		this.members.add(creator);
		this.name = name;
	}
	
	public void addMember(ClientHandler member) {
		
	}
	
	public String getName() {
		return name;
	}
}

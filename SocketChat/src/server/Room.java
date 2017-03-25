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
		if(!hasMember(member))
			members.add(member);
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return ID;
	}
	
	public boolean hasMember(ClientHandler member) {
		return members.contains(member);
	}
	
	public void removeMember(ClientHandler member) {
		members.remove(member);
	}
	
	public void sendMessage(String messageText) {
		for(ClientHandler member : members)
			member.sendMessage(messageText, false);
	}
}

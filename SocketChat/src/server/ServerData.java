package server;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ServerData {
	private static int ID = 0;
	
	private ArrayList<Room> rooms;
	
	public ServerData() {
		this.rooms = new ArrayList<Room>();
	}
	
	public Room addRoom(ClientHandler creator, String name) {
		Room room = new Room(ID++, creator, name);
		rooms.add(room);
		return room;
	}
	
	public Room[] getRooms() {
		return rooms.toArray(new Room[rooms.size()]);
	}
	
	public Room getRoom(String stringID) throws NoSuchElementException {
		int ID = Integer.parseInt(stringID);
		
		for(Room room : rooms)
			if(room.getID() == ID)
				return room;
		
		throw new NoSuchElementException();
	}
	
	public void removeUser(ClientHandler user) {
		for(Room room : rooms)
			if(room.hasMember(user))
				room.removeMember(user);
	}
}

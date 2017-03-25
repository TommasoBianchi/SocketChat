package server;

import java.util.ArrayList;

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
}

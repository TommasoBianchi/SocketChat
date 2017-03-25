package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import server.Message;
import server.MessageType;
import server.Room;

public class ServerInterface {
	
	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Room currentRoom;
	private String username;
	
	public ServerInterface(String ip, int portNumber, String username) {
		this.username = username;
		
		try {
			socket = new Socket(ip, portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Connected to server " + ip + ":" + portNumber);
		
		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());
			inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Message getTextMessage() {
		try {
			return (Message)inStream.readObject();
		} catch (SocketException e) { 
			// I've probably closed the client and thus also the socket
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public synchronized Room[] getRooms() {
		try {
			outStream.writeObject(Message.GET_ROOMS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Room[] rooms = new Room[0];
		try {
			rooms = (Room[])inStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rooms;
	}
	
	public synchronized Room createRoom(String title) {
		try {
			outStream.writeObject(new Message(title, MessageType.CREATE_ROOM, username));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Room room = null;
		try {
			room = (Room)inStream.readObject();
			currentRoom = room;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return room;
	}
	
	public synchronized Room joinRoom(String roomID) {
		try {
			outStream.writeObject(new Message(roomID, MessageType.JOIN_ROOM, username));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Room room = null;
		try {
			room = (Room)inStream.readObject();
			currentRoom = room;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return room;
	}
	
	public synchronized void sendMessage(String text) {
		if(currentRoom == null)
			System.err.println("Trying to send a message while not in a room");
		else {
			try {
				outStream.writeObject(new Message(text, MessageType.TEXT_MESSAGE, username));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void stop() {
		try {
			outStream.writeObject(Message.CLOSE_CONNECTION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			inStream.close();
			outStream.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

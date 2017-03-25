package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Message;
import server.Room;

public class ServerInterface {
	
	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	
	public ServerInterface(String ip, int portNumber) {
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
	
	public Room[] getRooms() {
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
}

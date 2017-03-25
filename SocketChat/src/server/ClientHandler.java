package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	
	private Socket clientSocket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private ServerData serverData;
	private Room currentRoom;
	
	public ClientHandler(Socket clientSocket, ServerData serverData) {
		this.clientSocket = clientSocket;
		this.serverData = serverData;
	}

	@Override
	public void run() {
		try {
			outStream = new ObjectOutputStream(clientSocket.getOutputStream());
			inStream = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		/*for(int i = 0; i < 100; i++){
		serverData.addRoom(this, "Room" + i);
		}*/
		
		boolean run = true;
		while(run) {			
			Message message = receiveMessage();
			
			switch(message.getMessageType()){
				case GET_ROOMS:
					getRooms();
					break;
				case JOIN_ROOM:
					joinRoom(message.getText());
					break;
				case CREATE_ROOM:
					createRoom(message.getText());
					break;
				case CLOSE_CONNECTION:
					run = false;
					break;
				case TEXT_MESSAGE:
					sendMessage(message.getText(), message.getSenderName(), true);
					break;
			}
		}
		
		serverData.removeUser(this);
		
		try {
			inStream.close();
			outStream.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Connection closed");
		}
	}
	
	private Message receiveMessage() {
		Message message = Message.CLOSE_CONNECTION;
		try {
			message = (Message)inStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}

	private void getRooms() {
		try {
			outStream.writeObject(serverData.getRooms());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createRoom(String messageText) {
		Room room = serverData.addRoom(this, messageText);
		try {
			outStream.writeObject(room);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentRoom = room;
	}

	private void joinRoom(String messageText) {
		Room room = serverData.getRoom(messageText);
		room.addMember(this);
		try {
			outStream.writeObject(room);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentRoom = room;
	}
	
	public void sendMessage(String messageText, String senderName, boolean fromClient) {
		if(currentRoom == null)
			System.err.println("Trying to send a message while not in a room");
		else {
			if(fromClient) {
				currentRoom.sendMessage(messageText, senderName);
				System.out.println(senderName + " wrote " + messageText);
			}
			else {
				try {
					outStream.writeObject(new Message(messageText, MessageType.TEXT_MESSAGE, senderName));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

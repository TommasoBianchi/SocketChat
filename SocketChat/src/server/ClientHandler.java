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
		for(int i = 0; i < 100; i++){
		serverData.addRoom(this, "Room" + i);
		}
		
		boolean run = true;
		while(run) {			
			Message message = Message.CLOSE_CONNECTION;
			try {
				message = (Message)inStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			switch(message.getMessageType()){
				case GET_ROOMS:
					try {
						outStream.writeObject(serverData.getRooms());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case CLOSE_CONNECTION:
					run = false;
					break;
				case TEXT_MESSAGE:
					// TODO: send message in chatroom
					break;
			}
		}
		
		try {
			inStream.close();
			outStream.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

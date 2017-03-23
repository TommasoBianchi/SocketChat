package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	
	private Socket clientSocket;
	
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println(inStream.readChar());
			System.out.println(inStream.readChar());
			System.out.println(inStream.readChar());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}

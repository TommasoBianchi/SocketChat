package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerInterface {
	
	private Socket socket;
	
	public ServerInterface(String ip, int portNumber) {
		try {
			socket = new Socket(ip, portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Connected to server " + ip + ":" + portNumber);
		
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			outStream.writeChars("Ciao");
			outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

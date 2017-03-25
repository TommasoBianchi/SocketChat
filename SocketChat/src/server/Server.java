package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
	
	private static ServerData serverData;

	public static void main(String[] args) {	
		ServerSocket serverSocket;
		
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Server ready on port " + serverSocket.getLocalPort());
		
		serverData = new ServerData();
		
		Executor executor = Executors.newCachedThreadPool();
		
		while(true) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			System.out.println("Client " + clientSocket.getInetAddress().getHostAddress() + " connected");
			
			executor.execute(new ClientHandler(clientSocket, serverData));
		}
	}
}

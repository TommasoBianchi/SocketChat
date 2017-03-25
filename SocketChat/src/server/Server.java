package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Server {
	
	private static ServerData serverData;

	public static void main(String[] args) {	
		ServerSocket serverSocket;
		
		int port = 0;
		if(args.length > 0){
			try {
				port = Integer.parseInt(args[0]);
			} 
			catch(NumberFormatException e) {
				System.err.println("Argument two (port number) is not correct");
				return;
			}
		}
		
		try {
			serverSocket = new ServerSocket(port);
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
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

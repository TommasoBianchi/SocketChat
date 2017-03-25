package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Message;

public class MessageReceiverThread implements Runnable {
	
	private ServerInterface serverInterface;
	private ClientView clientView;
	private boolean run;
	
	public MessageReceiverThread(ClientView clientView, ServerInterface serverInterface) {
		this.serverInterface = serverInterface;
		this.clientView = clientView;
		this.run = true;
	}

	@Override
	public void run() {
		while(run) {
			Message message = serverInterface.getTextMessage();
			if(run)
				clientView.displayMessage(message.getText(), message.getSenderName());
		}
	}
	
	public void stop() {
		run = false;
	}
}

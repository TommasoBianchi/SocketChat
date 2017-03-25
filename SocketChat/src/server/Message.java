package server;

import java.io.Serializable;

public class Message implements Serializable {
	public static final Message GET_ROOMS = new Message(MessageType.GET_ROOMS);
	public static final Message CLOSE_CONNECTION = new Message(MessageType.CLOSE_CONNECTION);
	
	private MessageType messageType;	
	private String message;
	
	public Message(MessageType messageType){
		this.messageType = messageType;
		this.message = "";
	}
	
	public Message(String message){
		this.messageType = MessageType.TEXT_MESSAGE;
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}
}

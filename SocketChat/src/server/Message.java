package server;

import java.io.Serializable;

public class Message implements Serializable {
	public static final Message GET_ROOMS = new Message("Get rooms", MessageType.GET_ROOMS);
	public static final Message CLOSE_CONNECTION = new Message("Close connection", MessageType.CLOSE_CONNECTION);
	
	private MessageType messageType;	
	private String text;
	
	public Message(String message, MessageType messageType){
		this.messageType = messageType;
		this.text = message;
	}
	
	public Message(String message){
		this(message, MessageType.TEXT_MESSAGE);
	}

	public MessageType getMessageType() {
		return messageType;
	}
	
	public String getText() {
		return text;
	}
}

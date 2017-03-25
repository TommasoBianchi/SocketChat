package server;

import java.io.Serializable;

public class Message implements Serializable {
	public static final Message GET_ROOMS = new Message("Get rooms", MessageType.GET_ROOMS, "");
	public static final Message CLOSE_CONNECTION = new Message("Close connection", MessageType.CLOSE_CONNECTION, "");
	
	private MessageType messageType;	
	private String text;
	private String senderName;
	
	public Message(String message, MessageType messageType, String senderName){
		this.messageType = messageType;
		this.text = message;
		this.senderName = senderName;
	}
	
	public Message(String message, String senderName){
		this(message, MessageType.TEXT_MESSAGE, senderName);
	}

	public MessageType getMessageType() {
		return messageType;
	}
	
	public String getText() {
		return text;
	}
	
	public String getSenderName() {
		return senderName;
	}
}

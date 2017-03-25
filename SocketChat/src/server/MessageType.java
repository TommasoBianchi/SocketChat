package server;

import java.io.Serializable;

public enum MessageType implements Serializable {
	GET_ROOMS, JOIN_ROOM, CREATE_ROOM, CLOSE_CONNECTION, TEXT_MESSAGE;
}

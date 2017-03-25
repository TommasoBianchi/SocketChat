package server;

import java.io.Serializable;

public enum MessageType implements Serializable {
	GET_ROOMS, CLOSE_CONNECTION, TEXT_MESSAGE;
}

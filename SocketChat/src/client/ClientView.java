package client;

import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import server.Room;

public class ClientView {
	private Stage stage;
	private Scene homeScene, lobbyScene, roomScene;
	private GridPane lobbyGrid;
	private Pane messagesPane, sendMessagePane;
	private ServerInterface serverInterface;
	private MessageReceiverThread messageReceiverThread;
	
	private final int lobbyRoomSquareSide = 100;
	private static final String IP_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	public ClientView(Stage stage){
		this.stage = stage;		
		
		// Home scene
	    homeScene = createHomeScene();
	    
	    // Lobby scene
	    lobbyScene = createLobbyScene();	   
	    
		stage.setScene(homeScene);
		stage.show();	
	}
	
	private Scene createRoomScene(Room room) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		
		Pane roomPane = new Pane();
		
		messagesPane = new Pane();
		
		Label titleLabel = new Label(room.getName());
		titleLabel.setPrefSize(screenBounds.getWidth(), 40);
		titleLabel.setAlignment(Pos.CENTER);
		
		sendMessagePane = new Pane();
		
		TextField messageTextField = new TextField();
		messageTextField.setPrefSize(screenBounds.getWidth() - 150, 40);
		
		Button sendMessageButton = new Button("Send");
		sendMessageButton.setPrefSize(100, 40);
		sendMessageButton.setLayoutX(screenBounds.getWidth() - 100);
		sendMessageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				serverInterface.sendMessage(messageTextField.getText());
			}			
		});
		
		sendMessagePane.getChildren().addAll(messageTextField, sendMessageButton);
	    
		roomPane.getChildren().addAll(messagesPane, sendMessagePane);
		
		messageReceiverThread = new MessageReceiverThread(this, serverInterface); 
		new Thread(messageReceiverThread).start();
		
	    return new Scene(roomPane);
	}

	private Scene createLobbyScene() {
		Pane lobbyPane = new Pane();
		
		Button createRoomButton = new Button("Create Room");
		createRoomButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Room room = serverInterface.createRoom("Titolo");
				roomScene = createRoomScene(room);
				stage.setScene(roomScene);
				stage.show();
			}			
		});
	    
	    lobbyGrid = new GridPane();
	    lobbyGrid.setLayoutY(60);
	    
	    lobbyPane.getChildren().addAll(lobbyGrid, createRoomButton);
	    return new Scene(lobbyPane);
	}

	private void fillLobbyGrid() {
		Room[] rooms = serverInterface.getRooms();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		
		int numberOfRooms = rooms.length;
	    int roomMargin = 5;
	    int roomsPerRow = (int)screenBounds.getWidth() / (lobbyRoomSquareSide + roomMargin);
	    int numberOfRows = numberOfRooms / roomsPerRow + ((numberOfRooms % roomsPerRow == 0) ? 0 : 1);
	    lobbyGrid.setPrefSize(roomsPerRow * (lobbyRoomSquareSide + roomMargin), numberOfRows * (lobbyRoomSquareSide + roomMargin));
	    
	    for(int y = 0; y < numberOfRows; y++){
		    for(int x = 0; x < ((y < numberOfRows - 1) ? roomsPerRow : numberOfRooms - (numberOfRows - 1) * roomsPerRow); x++){
		    	Room room = rooms[y * roomsPerRow + x];
		    	Pane roomPane = new Pane();
		    	roomPane.setPrefSize(lobbyRoomSquareSide, lobbyRoomSquareSide);
		    	
		    	Label label = new Label(room.getName());
		    	label.setPrefSize(lobbyRoomSquareSide, 40);
		    	label.setAlignment(Pos.CENTER);
		    	label.setLayoutY(5);
		    	roomPane.getChildren().add(label);
		    	
		    	Button button = new Button("Join room");
		    	button.setPrefSize(lobbyRoomSquareSide - 10, 40);
		    	button.setAlignment(Pos.CENTER);
		    	button.setLayoutX(5);
		    	button.setLayoutY(40);
		    	button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						Room joinedRoom = serverInterface.joinRoom(room.getID() + "");
						roomScene = createRoomScene(joinedRoom);
						stage.setScene(roomScene);
						stage.show();
					}
		    		
		    	});
		    	roomPane.getChildren().add(button);
		    	
		    	roomPane.setStyle("-fx-border-color: black;-fx-border-width: 2;");
		    	GridPane.setMargin(roomPane, new Insets(roomMargin));
		    	lobbyGrid.add(roomPane, x, y);
		    }
	    }
	}

	private Scene createHomeScene() {
		Pane homePane = new Pane();
	    homePane.setPrefSize(300, 300);
	    
	    TextField usernameTextField = new TextField();
	    usernameTextField.setPrefSize(300, 40);
	    usernameTextField.setPromptText("Select a username");
	    
	    TextField ipTextField = new TextField();
	    ipTextField.setPrefSize(300, 40);
	    ipTextField.setLayoutY(50);
	    ipTextField.setPromptText("Insert the server ip");
	    
	    TextField portTextField = new TextField();
	    portTextField.setPrefSize(300, 40);	  
	    portTextField.setLayoutY(100);
	    portTextField.setPromptText("Insert the server port number");
	    
	    Button okButton = new Button("OK");
	    okButton.setPrefSize(100, 40);
	    okButton.setLayoutY(150);
	    okButton.setLayoutX(100);
	    okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				int portNumber;
				try {
					portNumber = Integer.parseInt(portTextField.getText());
				}
				catch(NumberFormatException ex) {
					return;
				}
				
				if(!usernameTextField.getText().equals("") && Pattern.compile(IP_REGEX).matcher(ipTextField.getText()).matches()){
					serverInterface = new ServerInterface(ipTextField.getText(), portNumber, usernameTextField.getText());
					fillLobbyGrid();
					stage.setScene(lobbyScene);
					stage.centerOnScreen();
					stage.show();
				}
			}
	    });
	    
	    homePane.getChildren().addAll(usernameTextField, ipTextField, portTextField, okButton);
	    homeScene = new Scene(homePane);
		return homeScene;
	}
	
	public void displayMessage(String messageText, String senderName) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Label messageLabel = new Label("[" + senderName + "]: " + messageText);
				messageLabel.setLayoutY(messagesPane.getHeight());
				messagesPane.getChildren().add(messageLabel);	
				sendMessagePane.setLayoutY(messagesPane.getHeight() + 50);;
				stage.sizeToScene();
				stage.show();
			}			
		});
	}
	
	public Scene getHome(){
		return homeScene;
	}
	
	public Scene getLobby() {
		return lobbyScene;
	}
	
	public Scene getRoom() {
		return roomScene;
	}
	
	public void stop() {
		if(serverInterface != null)
			serverInterface.stop();
		if(messageReceiverThread != null)
			messageReceiverThread.stop();
	}
}

package client;

import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientView {
	private Stage stage;
	private Scene home, lobby, room;
	private ServerInterface serverInterface;
	
	private final int lobbyRoomSquareSide = 100;
	private static final String IP_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	public ClientView(Stage stage){
		this.stage = stage;
		
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		
		// Home scene
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
				
				if(!portTextField.getText().equals("") && Pattern.compile(IP_REGEX).matcher(ipTextField.getText()).matches()){
					serverInterface = new ServerInterface(ipTextField.getText(), portNumber);
					stage.setScene(lobby);
				}
			}
	    });
	    
	    homePane.getChildren().addAll(usernameTextField, ipTextField, portTextField, okButton);
	    home = new Scene(homePane);
	    
	    // Lobby scene
	    Pane lobbyPane = new Pane();
	    lobbyPane.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
	    
	    GridPane grid = new GridPane();
	    int numberOfRooms = 100; // TODO: fix with real server data
	    int roomMargin = 5;
	    int roomsPerRow = (int)lobbyPane.getPrefWidth() / (lobbyRoomSquareSide + roomMargin);
	    System.out.println(roomsPerRow);
	    int numberOfRows = numberOfRooms / roomsPerRow + ((numberOfRooms % roomsPerRow == 0) ? 0 : 1);
	    grid.setPrefSize(roomsPerRow * (lobbyRoomSquareSide + roomMargin), numberOfRows * (lobbyRoomSquareSide + roomMargin));
	    for(int y = 0; y < numberOfRows; y++){
		    for(int x = 0; x < roomsPerRow; x++){
		    	Pane roomPane = new Pane();
		    	roomPane.setPrefSize(lobbyRoomSquareSide, lobbyRoomSquareSide);
		    	roomPane.getChildren().add(new Label("Room (" + x + ", " + y + ")"));
		    	roomPane.setStyle("-fx-border-color: black;-fx-border-width: 2;");
		    	grid.add(roomPane, x, y);
			    GridPane.setMargin(roomPane, new Insets(roomMargin));
		    }
	    }
	    
	    lobbyPane.getChildren().addAll(grid);
	    lobby = new Scene(lobbyPane);	    
	    
	    // Room scene

		stage.setScene(home);
		stage.show();	
	}
	
	public Scene getHome(){
		return home;
	}
	
	public Scene getLobby() {
		return lobby;
	}
	
	public Scene getRoom() {
		return room;
	}
}

package client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApplication extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		ClientView clientView = new ClientView(stage);	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

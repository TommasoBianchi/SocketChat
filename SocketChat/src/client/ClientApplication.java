package client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApplication extends Application {
	private ClientView clientView;
	
	@Override
	public void start(Stage stage) throws Exception {
		clientView = new ClientView(stage);	
	}
	
	@Override
	public void stop() {
		clientView.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

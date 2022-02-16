package SongLibrary;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SongListController;
 
public class SongLib extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/SongLib.fxml"));
		
		// load the fxml
		AnchorPane root = (AnchorPane)loader.load();

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		SongListController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

    public static void main(String[] args) throws IOException {
    	launch(args);
	}
}
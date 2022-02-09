
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;
 
public class SongLib extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/list.fxml"));
		
		// load the fxml
		AnchorPane root = (AnchorPane)loader.load();

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		ListController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 200, 300);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

    public static void main(String[] args) {
        launch(args);
    }
}
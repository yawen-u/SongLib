package view;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.util.Optional;
import javafx.stage.Stage;
import SongLibrary.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class SongListController {

	@FXML         
	ListView<Song> listView;                

	private ObservableList<Song> obsList;              

	public void start(Stage mainStage) {      

		// create an ObservableList from saved test file
		obsList = FXCollections.observableArrayList(); 

		listView.setItems(obsList);
		Song first = new Song("26", "Lauv", "26", 2022);
		listView.getItems().add(first);

		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty();
		// .addListener(
		// 		(obs, oldVal, newVal) -> 
		// 		//showItem(mainStage)
		// 		showItemInputDialog(mainStage)
		// 		);
	}

	@FXML
	private Button closeButton;

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	
	// private void showItem(Stage mainStage) {                
	// 	Alert alert = new Alert(AlertType.INFORMATION);
	// 	alert.initOwner(mainStage);
	// 	alert.setTitle("List Item");
	// 	alert.setHeaderText(
	// 			"Selected list item properties");

	// 	String content = "Index: " + 
	// 			listView.getSelectionModel()
	// 	.getSelectedIndex() + 
	// 	"\nValue: " + 
	// 	listView.getSelectionModel()
	// 	.getSelectedItem();

	// 	alert.setContentText(content);
	// 	alert.showAndWait();
	// }
	
	// private void showItemInputDialog(Stage mainStage) {                
	// 	String item = listView.getSelectionModel().getSelectedItem();
	// 	int index = listView.getSelectionModel().getSelectedIndex();

	// 	TextInputDialog dialog = new TextInputDialog(item);
	// 	dialog.initOwner(mainStage); dialog.setTitle("List Item");
	// 	dialog.setHeaderText("Selected Item (Index: " + index + ")");
	// 	dialog.setContentText("Enter name: ");

	// 	Optional<String> result = dialog.showAndWait();
	// 	if (result.isPresent()) { obsList.set(index, result.get()); }
	// }

}

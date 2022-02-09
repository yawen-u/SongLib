package MainSceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// public class MainSceneController {

//     @FXML
//     private TextField tfTitle;

//     @FXML
//     void btnOKClicked(ActionEvent event) {
//         Stage mainWindow = (Stage) tfTitle.getScene().getWindow();
//         String title = tfTitle.getText();
//         mainWindow.setTitle(title);
//     }

// }
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;


public class ListController {

	@FXML         
	ListView<String> listView;                

	private ObservableList<String> obsList;              

	public void start(Stage mainStage) {                
		// create an ObservableList from an the reserved text file 
		obsList = FXCollections.observableArrayList(                               
				"Rams",                               
				"Bengals",
				"49ers",
				"Giants",
				"Packers",
				"Colts",
				"Cowboys",
				"Broncos",
				"Vikings",
				"Dolphins",
				"Titans",
				"Seahawks",
				"Steelers",
				"Jaguars"); 

		listView.setItems(obsList); 

		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				//showItem(mainStage)
				showItemInputDialog(mainStage)
				);

	}
	
	private void showItem(Stage mainStage) {                
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("List Item");
		alert.setHeaderText(
				"Selected list item properties");

		String content = "Index: " + 
				listView.getSelectionModel()
		.getSelectedIndex() + 
		"\nValue: " + 
		listView.getSelectionModel()
		.getSelectedItem();

		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void showItemInputDialog(Stage mainStage) {                
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { obsList.set(index, result.get()); }
	}

}

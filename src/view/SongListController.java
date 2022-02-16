package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import SongLibrary.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SongListController {

	@FXML         
	ListView<Song> listView;                

	private ObservableList<Song> obsList;       

	public void start(Stage mainStage) throws IOException {      

		// create an ObservableList from saved test file
		obsList = FXCollections.observableArrayList(); 

		listView.setItems(obsList);

		//Read txt file to library
		File myFile = new File("src/SongLibrary/SavedLib.txt");
        //System.out.println("Attempting to read from file in: "+ myFile.getCanonicalPath());
		int index = 0;

        try {
			File file = new File(myFile.getCanonicalPath());
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] SongInfo = data.split(", ");
				int year = Integer.parseInt(SongInfo[3]);
				Song curr = new Song(SongInfo[0], SongInfo[1], SongInfo[2], year);
				listView.getItems().add(index++, curr);
				//System.out.println(year);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItem(mainStage)
				//showItemInputDialog(mainStage)
				);
	}

	@FXML
	private Button closeButton;

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		//Save all the changes in the text file
		//........

		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}


	@FXML
	private Button addButton;

	@FXML
	private void handleAddButtonAction(ActionEvent event) {
		
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);

		VBox vbox = new VBox(new Text("Song Name: "), new Button("Save"));
		vbox.setAlignment(Pos.CENTER);

		dialogStage.setScene(new Scene(vbox, 300, 200)); 
		dialogStage.show();

	}
	
	private void showItem(Stage mainStage) {                
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("List Item");
		alert.setHeaderText("Selected list item properties");

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
		Song item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item.getName());
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { 
			Song edit = new Song(result.get(), item.getArtist(), item.getAlbum(), item.getYear());
			obsList.set(index, edit); 
		}
	}

}

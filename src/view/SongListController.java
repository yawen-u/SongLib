package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import SongLibrary.Song;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SongListController {

	@FXML         
	ListView<Song> listView;                

	private ObservableList<Song> obsList;
	
	// Buttons
	@FXML 
	private Button closeButton;
	@FXML 
	private Button addButton;
	@FXML 
	private Button editButton;

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

				Song curr = new Song(SongInfo[0], SongInfo[1], SongInfo[2], SongInfo[3]);
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
	
	private void showItemInputDialog() {                
		Song item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item.getName());
		dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { 
			Song edit = new Song(result.get(), item.getArtist(), item.getAlbum(), item.getYear());
			obsList.set(index, edit); 
		}
	}

	@FXML
	private void handleCloseButtonAction(ActionEvent event) throws IOException {
		//Save all the changes in the text file
		File myFile = new File("src/SongLibrary/SavedLib.txt");
		File file = new File(myFile.getCanonicalPath());
		try (FileWriter blankFile = new FileWriter(file, false)) {
			blankFile.flush();
		} 
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		for (Song each: obsList)
		{
			writer.append(each.getName());
			writer.append(", ");
			writer.append(each.getArtist());
			writer.append(", ");
			writer.append(each.getAlbum());
			writer.append(", ");
			writer.append(each.getYear());
			writer.append("\n");
			//System.out.println(each.getName());
		}
		writer.close();

		// close stage
		// Stage stage = (Stage) closeButton.getScene().getWindow();
		// stage.close();
		Platform.exit();
	}

	@FXML
	private void handleAddButtonAction(ActionEvent event) { //@TODO handle incorrect input, make it look prettier
		
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setTitle("Enter Song Details");

		TextField nameInput = new TextField("Song");
		TextField artistInput = new TextField("Artist");
		TextField albumInput = new TextField("Album");
		TextField yearInput = new TextField("Year");

		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			String name = nameInput.getText();
			String artist = artistInput.getText();
			String album = albumInput.getText();
			String year = yearInput.getText();

			// while(!(name == "Song"
			// || name.replaceAll("\\s", "") == ""
			// || artist == "Artist"
			// || artist.replaceAll("\\s", "") == "")){
			// 	//deals with incorrect inputs
			// }
			
			
			obsList.add(new Song(name, artist, album, year));
			

			dialogStage.close();
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			dialogStage.close();
		});

		VBox vbox = new VBox(
			nameInput, artistInput, albumInput,
			 yearInput, confirmButton, cancelButton);
		vbox.setAlignment(Pos.CENTER);

		dialogStage.setScene(new Scene(vbox, 300, 200)); 
		dialogStage.show();

	}

	@FXML
	private void handleEditButtonAction(ActionEvent event) {
		showItemInputDialog();
	}

}

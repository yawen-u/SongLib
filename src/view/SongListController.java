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
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	@FXML 
	private Button deleteButton;

	public void start(Stage mainStage) throws IOException {      

		mainStage.setTitle("Song Library");
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

		TextField nameInput = new TextField("Enter Song");
		nameInput.setOnMouseClicked(e -> {nameInput.clear();});
		TextField artistInput = new TextField("Enter Artist");
		artistInput.setOnMouseClicked(e -> {artistInput.clear();});
		TextField albumInput = new TextField("Enter Album");
		albumInput.setOnMouseClicked(e -> {albumInput.clear();});
		TextField yearInput = new TextField("Enter Year");
		yearInput.setOnMouseClicked(e -> {yearInput.clear();});

		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			String name = nameInput.getText();
			String artist = artistInput.getText();
			String album = albumInput.getText();
			String year = yearInput.getText();

			while( name.replaceAll("\\s", "") == ""
			|| artist.replaceAll("\\s", "") == ""){
				if(name == "Song"
				|| name.replaceAll("\\s", "") == "")
					nameInput.setText("Song cannot be empty");
				
				if(artist == "Artist"
				|| artist.replaceAll("\\s", "") == "")
					artistInput.setText("Artist cannot be empty");

				dialogStage.close();
				dialogStage.showAndWait();

				name = nameInput.getText();
				artist = artistInput.getText();
			}
			
			
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
		Song item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setTitle("Edit Song Details");

		Label label1 = new Label("Song: \t");
		Label label2 = new Label("Artist: \t");
		Label label3 = new Label("Year: \t");
		Label label4 = new Label("Album: \t");

		TextField nameInput = new TextField(item.getName());
		TextField artistInput = new TextField(item.getArtist());
		TextField albumInput = new TextField(item.getAlbum());
		TextField yearInput = new TextField(item.getYear());

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(label1, nameInput);
		hb1.setAlignment(Pos.BASELINE_CENTER);

		HBox hb2 = new HBox();
		hb2.getChildren().addAll(label2, artistInput);
		hb2.setAlignment(Pos.BASELINE_CENTER);

		HBox hb3 = new HBox();
		hb3.getChildren().addAll(label3, albumInput);
		hb3.setAlignment(Pos.BASELINE_CENTER);

		HBox hb4 = new HBox();
		hb4.getChildren().addAll(label4, yearInput);
		hb4.setAlignment(Pos.BASELINE_CENTER);

		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			if (nameInput != null || artistInput != null || albumInput != null || yearInput != null) { 
				Song edit = new Song(nameInput.getText(), artistInput.getText(), albumInput.getText(), yearInput.getText());
				obsList.set(index, edit); 
			}
			
			dialogStage.close();
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			dialogStage.close();
		});

		HBox hb5 = new HBox();
		hb5.getChildren().addAll(confirmButton, cancelButton);
		hb5.setSpacing(80);
		hb5.setAlignment(Pos.BASELINE_CENTER);

		VBox vbox = new VBox(hb1, hb2, hb3, hb4, hb5);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);

		dialogStage.setScene(new Scene(vbox, 300, 200)); 
		dialogStage.show();
	}

	@FXML
	private void handleDeleteButtonAction(ActionEvent event) {
		Song item = listView.getSelectionModel().getSelectedItem();
	
		Alert alert = new Alert(AlertType.WARNING, "Song Discription: ",ButtonType.YES, ButtonType.CANCEL);
		alert.setTitle("Delete");
		alert.setHeaderText("Are you sure you want to delete " + item.getName() + " ?");
		alert.setContentText("Artist: " + item.getArtist() + "\n" +
							 "Album:  " + item.getAlbum() + "\n" +
							 "Year: " + item.getYear() + "\n");

		if (ButtonType.YES != null) {
			listView.getItems().remove(item);
		}
		alert.showAndWait();
		event.consume();
	}

}

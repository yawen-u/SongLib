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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
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
	@FXML
	private TextArea description;

	public void start(Stage mainStage) throws IOException {

		mainStage.setTitle("Song Library");
		// create an ObservableList from saved test file
		obsList = FXCollections.observableArrayList();

		listView.setItems(obsList);

		// Read txt file to library
		File myFile = new File("src/SongLibrary/SavedLib.txt");
		// System.out.println("Attempting to read from file in: "+
		// myFile.getCanonicalPath());
		int index = 0;

		try {
			File file = new File(myFile.getCanonicalPath());
			Scanner myReader = new Scanner(file);
			if (file.length() == 0) {
				System.out.println("file empty");
			}
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] SongInfo = data.split(", ");

				Song curr = new Song(SongInfo[0], SongInfo[1], SongInfo[2], SongInfo[3]);
				listView.getItems().add(index++, curr);
				// System.out.println(year);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// select the first item
		if (listView.getItems().size() != 0) {
			listView.getSelectionModel().select(0);
			sortList();
			Song item = listView.getSelectionModel().getSelectedItem();
			description.setText("The song " + item.getName() +
				"\nby " + item.getArtist() +
				"\nwas recorded in " + item.getYear() +
				"\nunder the album of " + item.getAlbum() + ".");
	
			// set listener for the items
			listView
			.getSelectionModel()
			.selectedItemProperty()
			.addListener(new ChangeListener<Song>() {
				public void changed(ObservableValue<? extends Song> obs, Song oldVal, Song newVal) {

					if(newVal == null) description.setText("");
					
					description.setText("The song " + newVal.getName() +
							"\nby " + newVal.getArtist() +
							"\nwas recorded in " + newVal.getYear() +
							"\nunder the album of " + newVal.getAlbum() + ".");
				}
			});
		}
	}

	@FXML
	private void sortList(){
		listView.getItems().sort((o1,o2)->{
			if ( o1.getName().compareToIgnoreCase(o2.getName()) > 0 ) {        
				return 1;
			} else  {
				return 0;
			}
		});
	}

	@FXML
	private void handleCloseButtonAction(ActionEvent event) throws IOException {
		// Save all the changes in the text file
		File myFile = new File("src/SongLibrary/SavedLib.txt");
		File file = new File(myFile.getCanonicalPath());
		try (FileWriter blankFile = new FileWriter(file, false)) {
			blankFile.flush();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		for (Song each : listView.getItems()) {
			writer.append(each.getName());
			writer.append(", ");
			writer.append(each.getArtist());
			writer.append(", ");
			writer.append(each.getAlbum());
			writer.append(", ");
			writer.append(each.getYear());
			writer.append("\n");
			// System.out.println(each.getName());
		}
		writer.close();

		// close stage
		Platform.exit();
	}

	@FXML
	private void handleAddButtonAction(ActionEvent event) { 

		Song item = listView.getSelectionModel().getSelectedItem();

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
			if (nameInput != null && artistInput != null) {
				Song newSong = new Song(nameInput.getText(), artistInput.getText(), albumInput.getText(), yearInput.getText());
				listView.getItems().add(newSong);
				listView.getSelectionModel().select(newSong);
			}

			dialogStage.close();
		});
		
		sortList();

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
				// for (Song i : listView.getItems()) {
				// 	if (i.getName().equals(nameInput.getText())) {
				// 		System.out.println("duplicates");
				// 	}
				// }
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

		Alert alert = new Alert(AlertType.WARNING, "Song Discription: ", ButtonType.YES, ButtonType.CANCEL);
		alert.setTitle("Delete");
		alert.setHeaderText("Are you sure you want to delete " + item.getName() + " ?");
		alert.setContentText("Artist: " + item.getArtist() + "\n" +
				"Album:  " + item.getAlbum() + "\n" +
				"Year: " + item.getYear() + "\n");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			listView.getItems().remove(item);
		} else if (result.get() == ButtonType.CANCEL) {
			event.consume();
		}

	}

}

package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Aeropuerto;

public class TablaAnimalController {

	@FXML
	private TextField filterTxtf;

	@FXML
	private ImageView imagenMascota;

	@FXML
	private DatePicker primeraDatePicker;

	@FXML
	private TextArea observacionesTextarea;

	@FXML
	private TableView<Aeropuerto> animalTableView;

	@FXML
	private TableColumn<Aeropuerto, String> nombreColumn;

	@FXML
	private TableColumn<Aeropuerto, String> especieColumn;

	@FXML
	private TableColumn<Aeropuerto, String> razaColumn;

	@FXML
	private TableColumn<Aeropuerto, Character> sexoColumn;

	@FXML
	private TableColumn<Aeropuerto, Integer> edadColumn;

	@FXML
	private TableColumn<Aeropuerto, Float> pesoColumn;

	@FXML
	private Button modificarSimple;

	private ObservableList<Aeropuerto> data;
	private GestorDBAnimal gestorDB;

	public void initialize() {

		nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		especieColumn.setCellValueFactory(new PropertyValueFactory<>("especie"));
		razaColumn.setCellValueFactory(new PropertyValueFactory<>("raza"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
		edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
		pesoColumn.setCellValueFactory(new PropertyValueFactory<>("peso"));

		filterTxtf.textProperty().addListener(e -> {
			FilteredList<Aeropuerto> filteredData = new FilteredList<Aeropuerto>(data);
			filteredData.setPredicate(s -> s.getNombre().contains(filterTxtf.getText()));
			animalTableView.setItems(filteredData);
		});

		animalTableView.setOnMouseClicked(e -> {
			Aeropuerto selectedAnimal = animalTableView.getSelectionModel().getSelectedItem();
			if (selectedAnimal != null) {
				modificarSimple.setDisable(false);
				imagenMascota.setImage(new Image(selectedAnimal.getFoto()));
				primeraDatePicker.setValue(selectedAnimal.getPrimeraConsulta().toLocalDate());
				observacionesTextarea.setText(selectedAnimal.getObservaciones());
			}
		});

		gestorDB = new GestorDBAnimal();
		data = gestorDB.cargarPersonas();
		animalTableView.setItems(data);

		modificarSimple.setOnAction(e -> modificarDatosSuperiores());
		modificarSimple.setDisable(true);
	}

	@FXML
	void agregarAnimal(ActionEvent event) {
		int dataLenPreAdd = data.size();
		ventanaAgregarAnimal();
		if (data.size() != dataLenPreAdd)
			mostrarVentanaEmergente("Agregada nueva entrada", "Se ha añadido una nueva entrada", AlertType.INFORMATION);

	}

	@FXML
	void eliminarAnimal(ActionEvent event) {
		try {
			if (animalTableView.getSelectionModel().getSelectedItem() != null) {
				gestorDB.borrarAnimal(animalTableView.getSelectionModel().getSelectedItem());
				mostrarVentanaEmergente("Borrado con éxito", "Se ha eliminado un animal de la base de datos",
						AlertType.INFORMATION);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void modificarAnimal(ActionEvent event) {
		Aeropuerto oldAnimal = animalTableView.getSelectionModel().getSelectedItem();
		if (oldAnimal != null) {
			ventanaModificarAnimal(animalTableView.getSelectionModel().getSelectedItem());
			if (!oldAnimal.equals(animalTableView.getSelectionModel().getSelectedItem()))
				mostrarVentanaEmergente("Modificado", "Se ha modificado un animal de la base de datos",
						AlertType.INFORMATION);
		}
	}

	private boolean ventanaAgregarAnimal() {
		GridPane agregarGPane = new GridPane();
		TextField nombreTxtf = new TextField();
		TextField especieTxtf = new TextField();
		TextField razaTxtf = new TextField();
		TextField sexoTxtf = new TextField();
		TextField edadTxtf = new TextField();
		TextField pesoTxtf = new TextField();
		Button fileButton = new Button("Archivo");
		fileButton.setOnAction(e -> {
			Stage stage = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Elige la imagen para el animal");
			fileButton.getProperties().put("dest", fileChooser.showOpenDialog(stage).getAbsolutePath());
		});
		DatePicker dateFirst = new DatePicker();
		TextArea obsTextA = new TextArea();
		obsTextA.setPromptText("Observaciones");
		Button agregarButton = new Button("Agregar");
		agregarGPane.addRow(0, new Text("Nombre: "), nombreTxtf);
		agregarGPane.addRow(1, new Text("Especie: "), especieTxtf);
		agregarGPane.addRow(2, new Text("Raza: "), razaTxtf);
		agregarGPane.addRow(3, new Text("Sexo: "), sexoTxtf);
		agregarGPane.addRow(4, new Text("Edad: "), edadTxtf);
		agregarGPane.addRow(5, new Text("Peso: "), pesoTxtf);
		agregarGPane.addRow(6, new Text("Archivo: "), fileButton);
		agregarGPane.addRow(7, new Text("Fecha de la primera consulta: "), dateFirst);
		agregarGPane.addRow(8, obsTextA);
		agregarGPane.addRow(9, agregarButton);
		GridPane.setColumnSpan(agregarButton, 2);
		GridPane.setColumnSpan(obsTextA, 2);

		Scene agregarScene = new Scene(agregarGPane);
		agregarButton.setPadding(new Insets(10));
		Stage agregarPersonaStg = new Stage();

		agregarButton.setOnAction(e -> {
			try {
				String imagePath = fileButton.getProperties().get("dest").toString();
				imagePath = imagePath.replace("\\", "/")
						.substring(imagePath.indexOf('/', countChars(imagePath, '/') + 1));

				Aeropuerto newAnimal = new Aeropuerto(nombreTxtf.getText(), especieTxtf.getText(), razaTxtf.getText(),
						Character.toUpperCase(sexoTxtf.getText().charAt(0)), Integer.parseInt(edadTxtf.getText()),
						Double.parseDouble(pesoTxtf.getText()), obsTextA.getText(),
						new Date(dateFirst.getValue().toEpochDay() * 24 * 3600 * 1000), imagePath);
				if (!data.contains(newAnimal)) {
					if (newAnimal.getSexo() != 'M' && newAnimal.getSexo() != 'H') {
						mostrarVentanaEmergente("Sexo incorrecto", "El sexo del animal debe ser 'M' o 'H'",
								AlertType.ERROR);
						return;
					}
					data.add(newAnimal);
					try {
						gestorDB.addAnimal(newAnimal);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else
					mostrarVentanaEmergente("Entrada existente", "Esa persona ya está registrada", AlertType.ERROR);
				animalTableView.setItems(data);
				agregarPersonaStg.close();
				return;
			} catch (NumberFormatException numberFormat) {
				mostrarVentanaEmergente("Edad no es numero", "La edad debe ser un numero", AlertType.ERROR);
				return;
			}
		});
		agregarPersonaStg.setScene(agregarScene);
		agregarPersonaStg.initModality(Modality.APPLICATION_MODAL);
		agregarPersonaStg.showAndWait();
		return true;
	}

	private boolean ventanaModificarAnimal(Aeropuerto animalMod) {
		GridPane modificarGPane = new GridPane();
		TextField nombreTxtf = new TextField();
		nombreTxtf.setText(animalMod.getNombre());
		TextField especieTxtf = new TextField();
		especieTxtf.setText(animalMod.getEspecie());
		TextField razaTxtf = new TextField();
		razaTxtf.setText(animalMod.getRaza());
		TextField sexoTxtf = new TextField();
		sexoTxtf.setText(animalMod.getSexo() + "");
		TextField edadTxtf = new TextField();
		edadTxtf.setText(animalMod.getEdad() + "");
		TextField pesoTxtf = new TextField();
		pesoTxtf.setText(animalMod.getPeso() + "");
		Button fileButton = new Button("Archivo");
		fileButton.setOnAction(e -> {
			Stage stage = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Elige la imagen para el animal");
			fileButton.getProperties().put("dest", fileChooser.showOpenDialog(stage));
		});
		DatePicker dateFirst = new DatePicker();
		dateFirst.setValue(animalMod.getPrimeraConsulta().toLocalDate());
		TextArea obsTextA = new TextArea();
		obsTextA.setText(animalMod.getObservaciones());
		obsTextA.setPromptText("Observaciones");
		Button modificarButton = new Button("Modificar");
		modificarGPane.addRow(0, new Text("Nombre: "), nombreTxtf);
		modificarGPane.addRow(1, new Text("Especie: "), especieTxtf);
		modificarGPane.addRow(2, new Text("Raza: "), razaTxtf);
		modificarGPane.addRow(3, new Text("Sexo: "), sexoTxtf);
		modificarGPane.addRow(4, new Text("Edad: "), edadTxtf);
		modificarGPane.addRow(5, new Text("Peso: "), pesoTxtf);
		modificarGPane.addRow(6, new Text("Archivo: "), fileButton);
		modificarGPane.addRow(7, new Text("Fecha de la primera consulta: "), dateFirst);
		modificarGPane.addRow(8, obsTextA);
		modificarGPane.addRow(9, modificarButton);
		GridPane.setColumnSpan(modificarButton, 2);
		GridPane.setColumnSpan(obsTextA, 2);
		modificarButton.setMaxWidth(999);

		Scene modificarScene = new Scene(modificarGPane);
		modificarButton.setPadding(new Insets(10));
		Stage modificarAnimalStg = new Stage();

		modificarButton.setOnAction(e -> {
			try {
				Aeropuerto newAnimal = new Aeropuerto(nombreTxtf.getText(), especieTxtf.getText(), razaTxtf.getText(),
						Character.toUpperCase(sexoTxtf.getText().charAt(0)), Integer.parseInt(edadTxtf.getText()),
						Double.parseDouble(pesoTxtf.getText()), obsTextA.getText(),
						new Date(dateFirst.getValue().toEpochDay() * 24 * 3600 * 1000),
						fileButton.getProperties().get("dest").toString());
				if (!data.contains(newAnimal)) {
					if (newAnimal.getSexo() != 'M' && newAnimal.getSexo() != 'H') {
						mostrarVentanaEmergente("Sexo incorrecto", "El sexo del animal debe ser 'M' o 'H'",
								AlertType.ERROR);
						return;
					}
					data.set(data.indexOf(animalMod), newAnimal);
					try {
						gestorDB.modificarAnimal(animalMod, newAnimal);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else
					mostrarVentanaEmergente("Entrada existente", "Esa persona ya está registrada", AlertType.ERROR);
				animalTableView.setItems(data);
				modificarAnimalStg.close();
				return;
			} catch (NumberFormatException numberFormat) {
				mostrarVentanaEmergente("Edad no es numero", "La edad debe ser un numero", AlertType.ERROR);
				return;
			}
		});
		modificarAnimalStg.setScene(modificarScene);
		modificarAnimalStg.initModality(Modality.APPLICATION_MODAL);
		modificarAnimalStg.showAndWait();
		return true;
	}

	private static void mostrarVentanaEmergente(String titulo, String content, AlertType tipo) {
		Alert anadidoAnimal = new Alert(tipo);
		anadidoAnimal.setTitle(titulo);
		anadidoAnimal.setHeaderText(null);
		anadidoAnimal.setContentText(content);
		anadidoAnimal.showAndWait();
	}

	private void modificarDatosSuperiores() {
		Aeropuerto anim = new Aeropuerto(animalTableView.getSelectionModel().getSelectedItem());
		anim.setPrimeraConsulta(new Date(primeraDatePicker.getValue().toEpochDay() * 24 * 3600 * 1000));
		anim.setObservaciones(observacionesTextarea.getText());
		try {
			gestorDB.modificarAnimal(animalTableView.getSelectionModel().getSelectedItem(), anim);
			gestorDB = new GestorDBAnimal();
			data = gestorDB.cargarPersonas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		animalTableView.setItems(data);
		mostrarVentanaEmergente("Modificado con éxito",
				"Se han actualizado el/los campo/s observaciones y/o fecha de primera consulta", AlertType.INFORMATION);
	}

	/**
	 * Counts how many times a character appears in a given String.
	 * 
	 * @param text The text from where the characters will be counted
	 * @param ch   The character to be counted
	 * @return Integer with the number of times the char appears
	 */
	private static int countChars(String text, char ch) {
		int count = 0;
		for (char c : text.toCharArray()) {
			if (c == ch)
				count++;
		}
		return count;
	}
}

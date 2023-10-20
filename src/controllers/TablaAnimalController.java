package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.SQLException;

import javafx.collections.ObservableList;
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
import model.Animal;
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
    private TableView<Animal> animalTableView;
    
    @FXML
    private TableColumn<Animal, String> nombreColumn;
    
    @FXML
    private TableColumn<Animal, String> especieColumn;
    
    @FXML
    private TableColumn<Animal, String> razaColumn;

    @FXML
    private TableColumn<Animal, Character> sexoColumn;

    @FXML
    private TableColumn<Animal, Integer> edadColumn;
    
    @FXML
    private TableColumn<Animal, Float> pesoColumn;
    
    private ObservableList<Animal> data;
    private GestorDBAnimal gestorDB;

    public void initialize() {
    	
    	nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	especieColumn.setCellValueFactory(new PropertyValueFactory<>("especie"));
    	razaColumn.setCellValueFactory(new PropertyValueFactory<>("raza"));
    	sexoColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
    	edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
    	pesoColumn.setCellValueFactory(new PropertyValueFactory<>("peso"));

    	gestorDB = new GestorDBAnimal();
    	data = gestorDB.cargarPersonas();
    	animalTableView.setItems(data);
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

    }

    @FXML
    void modificarAnimal(ActionEvent event) {

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
    		fileButton.getProperties().put("dest", fileChooser.showOpenDialog(stage));
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
				Animal newAnimal = new Animal(nombreTxtf.getText(),especieTxtf.getText(),razaTxtf.getText()
						,sexoTxtf.getText().charAt(0),Integer.parseInt(edadTxtf.getText()),
						Double.parseDouble(pesoTxtf.getText()),obsTextA.getText(),
						new Date(dateFirst.getValue().toEpochDay()*24*3600*1000),
						new Image(this.getClass().getResource("/img/placeholder.png").toString()));
				if (! data.contains(newAnimal)) {					
					data.add(newAnimal);
					try {
						gestorDB.addAnimal(newAnimal);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				else
					mostrarVentanaEmergente("Entrada existente", "Esa persona ya está registrada",AlertType.ERROR);
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
    private static void mostrarVentanaEmergente(String titulo,String content, AlertType tipo) {
    	Alert anadidaPersona = new Alert(tipo);
		anadidaPersona.setTitle(titulo);
		anadidaPersona.setHeaderText(null);
		anadidaPersona.setContentText(content);
		anadidaPersona.showAndWait();
    }
}

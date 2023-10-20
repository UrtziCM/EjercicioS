package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Persona;

public class TablaPersonaController {

    @FXML
    private TableColumn<Persona, String> nombreColumn;
   
    @FXML
    private TableColumn<Persona, String> apellidosColumn;
    
    @FXML
    private TableColumn<Persona, Integer> edadColumn;
    
    @FXML
    private TextField filterTxtf;

    @FXML
    private TableView<Persona> personaTableView;

    @FXML
    private Button agregarButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button modifyButton;

    @FXML
    private Button exportarButton;
    
    @FXML
    private Button importarButton;
    
    
    private ObservableList<Persona> data;
    private GestorDBPersona gestorDB;
    
    @FXML
    public void initialize() {
    	nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
    	edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
    	gestorDB = new GestorDBPersona();
    	data = gestorDB.cargarPersonas();
    	agregarButton.setOnAction(e -> agregarPersona(e));
    	deleteButton.setOnAction(e -> borrarPersona(e));
    	/* Añadimos un Listener al texto de el textfield para ejecutarlo por cada carácter introducido*/
    	filterTxtf.textProperty().addListener(e -> {
    		/* Creamos una FilteredList con los datos de la tabla */
    		FilteredList<Persona> filteredData = new FilteredList<Persona>(data);
    		/* Establecemos la regla del filtro: Si no contiene el texto en el textfield no se muestra */
    		filteredData.setPredicate(s -> s.getNombre().contains(filterTxtf.getText()));
    		/* Ordenamos la lista con una SortedList*/
    		SortedList<Persona> filteredSortedData = new SortedList<Persona>(filteredData);
    		personaTableView.setItems(filteredSortedData); // Añadimos la lista ordenada a la tabla
    	});;
    	ContextMenu cMenuActions = new ContextMenu();
    	MenuItem modificarMenuI = new MenuItem("Modificar");
    	modificarMenuI.setOnAction(e -> modificarPersona(e));
    	MenuItem borrarMenuI = new MenuItem("Borrar");
    	borrarMenuI.setOnAction(e -> borrarPersona(e));
		cMenuActions.getItems().add(modificarMenuI);
		cMenuActions.getItems().add(borrarMenuI);
		personaTableView.setContextMenu(cMenuActions);
    	
    	personaTableView.setItems(data);
    }
    
    @FXML
    void agregarPersona(ActionEvent event) {
    	int dataLenPreAdd = data.size();
    	ventanaAgregarPersona();
    	if (data.size() != dataLenPreAdd)
    		mostrarVentanaEmergente("Agregada nueva entrada", "Se ha añadido una nueva entrada", AlertType.INFORMATION);
    }

    @FXML
    void borrarPersona(ActionEvent event) {
    	if (personaTableView.getSelectionModel().getSelectedItem() != null) {
    		data.remove(personaTableView.getSelectionModel().getSelectedItem());
    		mostrarVentanaEmergente("Borrada entrada", "Se ha borrado la entrada elegida", AlertType.INFORMATION);    		
    	}

    }
    

    @FXML
    void modificarPersona(ActionEvent event) {
    	Persona oldPers = personaTableView.getSelectionModel().getSelectedItem();
		if (comprobarModificacion(personaTableView.getSelectionModel().getSelectedItem())) {  
			if (!oldPers.equals(personaTableView.getSelectionModel().getSelectedItem()))
				mostrarVentanaEmergente("Modificada una entrada", "Se ha modificado una entrada con éxito", AlertType.INFORMATION);
		}
    }
     
    private boolean comprobarModificacion(Persona pers) {
    	if (pers == null) return false;
		try {				
			ventanaModificarPersona(pers);
		} catch (NumberFormatException numberFormat) {
			mostrarVentanaEmergente("Edad no es numero", "La edad debe ser un numero", AlertType.ERROR);
			return false;
		}
		return true;
    }
 
    private static void mostrarVentanaEmergente(String titulo,String content, AlertType tipo) {
    	Alert anadidaPersona = new Alert(tipo);
		anadidaPersona.setTitle(titulo);
		anadidaPersona.setHeaderText(null);
		anadidaPersona.setContentText(content);
		anadidaPersona.showAndWait();
    }
    private boolean ventanaAgregarPersona() {
    	GridPane agregarGPane = new GridPane();
    	TextField nombreTxtf = new TextField();
    	TextField apellidosTxtf = new TextField();
    	TextField edadTxtf = new TextField();
    	Button agregarButton = new Button("Agregar");
    	agregarGPane.addRow(0, new Text("Nombre: "), nombreTxtf);
		agregarGPane.addRow(1, new Text("Apellidos: "), apellidosTxtf);
		agregarGPane.addRow(2, new Text("Edad: "), edadTxtf);
		agregarGPane.addRow(3, agregarButton);
		GridPane.setColumnSpan(agregarButton, 2);
		agregarButton.setMaxWidth(agregarGPane.getWidth());

		Scene agregarScene = new Scene(agregarGPane);
		Stage agregarPersonaStg = new Stage();
		
		agregarButton.setOnAction(e -> {
			try {
				Persona newPersona = new Persona(nombreTxtf.getText(),apellidosTxtf.getText(),Integer.parseInt(edadTxtf.getText()));
				if (! data.contains(newPersona)) {					
					data.add(newPersona);
					try {
						gestorDB.addPersona(newPersona);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				else
					mostrarVentanaEmergente("Entrada existente", "Esa persona ya está registrada",AlertType.ERROR);
				personaTableView.setItems(data);
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
    
    private void ventanaModificarPersona(Persona pers) {
    	GridPane agregarGPane = new GridPane();
    	TextField nombreTxtf = new TextField();
    	nombreTxtf.setText(pers.getNombre());
    	TextField apellidosTxtf = new TextField();
    	apellidosTxtf.setText(pers.getApellido());
    	TextField edadTxtf = new TextField();
    	edadTxtf.setText(pers.getEdad()+"");
    	Button agregarButton = new Button("Agregar");
    	agregarGPane.addRow(0, new Text("Nombre: "), nombreTxtf);
		agregarGPane.addRow(1, new Text("Apellidos: "), apellidosTxtf);
		agregarGPane.addRow(2, new Text("Edad: "), edadTxtf);
		agregarGPane.addRow(3, agregarButton);
		GridPane.setColumnSpan(agregarButton, 2);
		agregarButton.setMaxWidth(agregarGPane.getWidth());

		Scene agregarScene = new Scene(agregarGPane);
		Stage modificarPersonaStg = new Stage();
		
		agregarButton.setOnAction(e -> {
			try {
				int indexPersona = data.indexOf(pers);
				Persona newPersona = new Persona(nombreTxtf.getText(),apellidosTxtf.getText(),Integer.parseInt(edadTxtf.getText()));
				data.set(indexPersona,newPersona);
				personaTableView.setItems(data);
				gestorDB.modificarPersona(pers,newPersona);
				modificarPersonaStg.close();
			} catch (NumberFormatException numberFormat) {
				mostrarVentanaEmergente("Edad no es numero", "La edad debe ser un numero", AlertType.ERROR);
				return;
			} catch (SQLException sqlE) {}			
		});
		modificarPersonaStg.setScene(agregarScene);
		modificarPersonaStg.initModality(Modality.APPLICATION_MODAL);
		modificarPersonaStg.showAndWait();
    }
    

}

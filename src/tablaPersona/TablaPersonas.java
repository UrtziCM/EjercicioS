package tablaPersona;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TablaPersonas extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/tablaPersonas3.fxml"));
    	Scene scene = new Scene( root, 300, 275);
    	scene.getStylesheets().add(this.getClass().getResource("/css/main.css").toExternalForm());
    	stage.getIcons().add(new Image(this.getClass().getResource("/img/Cooper.png").toString())));
        stage.setTitle("PERSONAS");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
		launch(args);
	}
}

package tablaAnimal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TablaAnimales extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/veterinaria.fxml"));
    	Scene scene = new Scene( root );
    	scene.getStylesheets().add(this.getClass().getResource("/css/main.css").toExternalForm());
        stage.setTitle("Veterinaria");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
		launch(args);
	}
}

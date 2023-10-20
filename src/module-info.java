/**
 * 
 */
/**
 * 
 */
module EjercicioS {
	requires java.sql;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	opens tablaAnimal to javafx.graphics,javafx.base,javafx.controls,javafx.fxml;
	opens model to javafx.base,javafx.controls;
	opens controllers to javafx.graphics,javafx.base,javafx.controls,javafx.fxml;
}
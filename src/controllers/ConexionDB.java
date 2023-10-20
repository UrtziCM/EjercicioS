package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private Connection conexion;
    public ConexionDB() throws SQLException {
        String host = "localhost";
        String baseDatos = "personas";
        String usuario = "dm2";
        String password = "dm2";
        String cadenaConexion = "jdbc:mysql://" + host + ":3306/" + baseDatos;
        conexion = DriverManager.getConnection(cadenaConexion, usuario, password);
        conexion.setAutoCommit(true);
    }
    public Connection getConexion() {
        return conexion;
    }
	public void closeConexion() {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

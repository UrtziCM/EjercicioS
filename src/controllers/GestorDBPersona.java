package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Persona;

public class GestorDBPersona {
    private ConexionDB conexion;

    public ObservableList<Persona> cargarPersonas()  {
    	
    	ObservableList<Persona> personas = FXCollections.observableArrayList();
        try {
            conexion = new ConexionDB();        	
        	String consulta = "SELECT * FROM Persona";
        	PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);      
        	ResultSet rs = pstmt.executeQuery();   
				
			 while (rs.next()) {
		            String nombre = rs.getString("nombre");
		            String apellidos = rs.getString("apellidos");
		            int edad = rs.getInt("edad");
	                Persona p = new Persona(nombre,apellidos,edad);
	                System.out.println(p.toString());
	                personas.add(p);        
        }             
		rs.close();       
        conexion.closeConexion();
        
	    } catch (SQLException e) {	    	
	    	e.printStackTrace();
	    }    
        return personas;    
    }

	public void addPersona(Persona newPersona) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "INSERT INTO Persona(nombre,apellidos,edad) VALUES ('" + newPersona.getNombre() + "','" + newPersona.getApellido()+ "',"+ newPersona.getEdad() +")";
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
	public void borrarPersona(Persona newPersona) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "DELETE FROM Persona WHERE nombre='" + newPersona.getNombre() + "' apellido='" + newPersona.getApellido() + "' edad=" + newPersona.getEdad();
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
	public void modificarPersona(Persona oldPersona,Persona newPersona) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "UPDATE Persona "
	    		+ "SET nombre='"+newPersona.getNombre()+"',"
    				+ "apellidos='"+newPersona.getApellido()+"',"
					+ "edad="+newPersona.getEdad()
				+ " WHERE nombre='" + oldPersona.getNombre()	+ "' AND apellidos='" + oldPersona.getApellido() 
					+ "' AND edad=" + oldPersona.getEdad();
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
}
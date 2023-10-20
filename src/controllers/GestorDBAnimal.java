package controllers;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import model.Animal;

public class GestorDBAnimal {
    private ConexionDB conexion;

    public ObservableList<Animal> cargarPersonas()  {
    	
    	ObservableList<Animal> animales = FXCollections.observableArrayList();
        try {
            conexion = new ConexionDB();        	
        	String consulta = "SELECT * FROM Animal";
        	PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);      
        	ResultSet rs = pstmt.executeQuery();   
				
			 while (rs.next()) {
			 	int id = rs.getInt("id");
	            String nombre = rs.getString("nombre");
	            String especie = rs.getString("especie");
	            String raza = rs.getString("raza");
	            char sexo = rs.getString("sexo").charAt(0);
	            int edad = rs.getInt("edad");
	            double peso = rs.getDouble("peso");
	            String observaciones = rs.getString("observaciones");
	            Date primeraConsulta = rs.getDate("primeraConsulta");
	            Image foto = new Image(this.getClass().getResource("/img/placeholder.png").toString());
                Animal a = new Animal(id, nombre,especie,raza, sexo, edad, peso, observaciones, primeraConsulta, foto);
                animales.add(a);        
        }             
		rs.close();       
        conexion.closeConexion();
        
	    } catch (SQLException e) {	    	
	    	e.printStackTrace();
	    }    
        return animales;
    }

	public void addAnimal(Animal newAnimal) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "INSERT INTO `Animal` (`nombre`, `especie`, `raza`, `sexo`, `edad`, `peso`, `observaciones`, `primeraConsulta`, `foto`) "
	    		+ "VALUES ('"+newAnimal.getNombre()+"',"
				+ " '"+newAnimal.getEspecie()+"',"
				+ " '"+newAnimal.getRaza()+"',"
				+ " '"+newAnimal.getSexo()+"',"
				+ " '"+newAnimal.getEdad()+"',"
				+ " '"+newAnimal.getPeso()+"',"
				+ " '"+newAnimal.getObservaciones()+"',"
				+ " '"+newAnimal.getPrimeraConsulta().toString()+"',"
				+ " '"+newAnimal.getFoto()+"'"
				+ ")";
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
	public void borrarAnimal(Animal animal) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "DELETE FROM Animal WHERE id=" + animal.getId();
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
	public void modificarAnimal(Animal oldAnimal,Animal newAnimal) throws SQLException {
		conexion = new ConexionDB(); 
	    Statement stmt = conexion.getConexion().createStatement();
	    String sql = "UPDATE Animal "
	    		+ "SET nombre='"+newAnimal.getNombre()+"',"
    				+ "especie='"+newAnimal.getEspecie()+"',"
    				+ "raza='"+newAnimal.getRaza()+"',"
    				+ "sexo='"+newAnimal.getSexo()+"',"
					+ "edad='"+newAnimal.getEdad()+"',"
					+ "peso='"+newAnimal.getPeso()+"',"
					+ "observaciones='"+newAnimal.getObservaciones()+"',"
					+ "foto='"+newAnimal.getFoto()
				+ " WHERE id=" + oldAnimal.getId();
	    stmt.executeUpdate(sql);
	    conexion.closeConexion();
	}
}
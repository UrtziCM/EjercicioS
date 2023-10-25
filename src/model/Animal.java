package model;

import java.sql.Date;
import java.util.Objects;

import javafx.scene.image.Image;

public class Animal {
	int id;
	String nombre,especie,raza;
	char sexo;
	int edad;
	double peso;
	String observaciones;
	Date primeraConsulta;
	String foto;
	public Animal(int id, String nombre, String especie, String raza, char sexo, int edad, double peso,
			String observaciones, Date primeraConsulta, String foto) {
		this.id = id;
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.sexo = sexo;
		this.edad = edad;
		this.peso = peso;
		this.observaciones = observaciones;
		this.primeraConsulta = primeraConsulta;
		this.foto = foto;
	}
	public Animal(String nombre, String especie, String raza, char sexo, int edad, double peso,
			String observaciones, Date primeraConsulta, String foto) {
		this.id = id;
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.sexo = sexo;
		this.edad = edad;
		this.peso = peso;
		this.observaciones = observaciones;
		this.primeraConsulta = primeraConsulta;
		this.foto = foto;
	}
	
	public Animal(Animal anim) {
		this.id = anim.id;
		this.nombre = anim.nombre;
		this.especie = anim.especie;
		this.raza = anim.raza;
		this.sexo = anim.sexo;
		this.edad = anim.edad;
		this.peso = anim.peso;
		this.observaciones = anim.observaciones;
		this.primeraConsulta = anim.primeraConsulta;
		this.foto = anim.foto;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEspecie() {
		return especie;
	}
	public void setEspecie(String especie) {
		this.especie = especie;
	}
	public String getRaza() {
		return raza;
	}
	public void setRaza(String raza) {
		this.raza = raza;
	}
	public char getSexo() {
		return sexo;
	}
	public void setSexo(char sexo) {
		this.sexo = sexo;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Date getPrimeraConsulta() {
		return primeraConsulta;
	}
	public void setPrimeraConsulta(Date primeraConsulta) {
		this.primeraConsulta = primeraConsulta;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animal other = (Animal) obj;
		return id == other.id;
	}
	
	
}

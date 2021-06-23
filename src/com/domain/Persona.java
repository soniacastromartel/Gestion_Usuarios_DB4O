package com.domain;

/**
 *
 * @author Sonia Castro (soniacastromartel@gmail.com)
 */
public class Persona {
    private String nombre;
    private String password;
    private boolean admin;

    public Persona() {
	
    }

    public Persona(String nombre, String password, boolean admin) {
	this.nombre = nombre;
	this.password = password;
	this.admin = admin;
    }

    public String getNombre() {
	return nombre;
    }

    public void setNombre(String nombre) {
	this.nombre = nombre;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isAdmin() {
	return admin;
    }

    public void setAdmin(boolean admin) {
	this.admin = admin;
    }

    @Override
    public String toString() {
	return "Persona{" + "nombre=" + nombre + ", password=" + password + ", admin=" + admin + '}';
    }
    
    
    
    
}

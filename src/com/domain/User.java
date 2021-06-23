package com.domain;

/**
 *
 * @author Sonia Castro (soniacastromartel@gmail.com)
 */
public class User {

    private String clave;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    public User() {
    }

    public User(String clave, String nombre, String apellido, String email, String telefono) {
	this.clave = clave;
	this.nombre = nombre;
	this.apellido = apellido;
	this.email = email;
	this.telefono = telefono;
    }

    public String getClave() {
	return clave;
    }

    public void setClave(String clave) {
	this.clave = clave;
    }

    public String getNombre() {
	return nombre;
    }

    public void setNombre(String nombre) {
	this.nombre = nombre;
    }

    public String getApellido() {
	return apellido;
    }

    public void setApellido(String apellido) {
	this.apellido = apellido;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getTelefono() {
	return telefono;
    }

    public void setTelefono(String telefono) {
	this.telefono = telefono;
    }

    @Override
    public String toString() {
	return "User{" + "clave=" + clave + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", telefono=" + telefono + '}';
    }

}

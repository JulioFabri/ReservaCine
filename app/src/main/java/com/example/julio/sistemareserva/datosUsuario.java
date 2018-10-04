package com.example.julio.sistemareserva;

public class datosUsuario {
    private String nombre, apellido, email;
    private int id;

    public datosUsuario(String nombre, String apellido, String email, int id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.id = id;
    }

    public datosUsuario() {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

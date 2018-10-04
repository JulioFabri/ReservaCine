package com.example.julio.sistemareserva;

public class datosCartelera {
    private int idSala, imagen;
    private String idFecha, pelicula, descripcion;

    public datosCartelera() {
    }

    public datosCartelera(int idsala, String idfecha, String peli, String desc, int ima) {
        this.idSala=idsala;
        this.idFecha=idfecha;
        this.pelicula=peli;
        this.descripcion=desc;
        this.imagen=ima;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getIdFecha() {
        return idFecha;
    }

    public void setIdFecha(String idFecha) {
        this.idFecha = idFecha;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen=imagen;
    }
}

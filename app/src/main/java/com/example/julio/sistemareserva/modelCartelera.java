package com.example.julio.sistemareserva;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.widget.ImageView;

public class modelCartelera implements Parcelable {
    private String nombre;
    private String descripcion;
    private int id;
    //private int imagenCartelera;
    //private Bitmap bitImagen;
    private String urlImagen;

    public modelCartelera(String nombre, String descripcion,int id, String urlImagen /*int imagenCartelera, Bitmap bitImagen*/) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.id=id;
        //this.imagenCartelera = imagenCartelera;
        //this.bitImagen = bitImagen;
        this.urlImagen = urlImagen;
    }
    public modelCartelera() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id=id;}

/*
    public int getImagenCartelera() {
        return imagenCartelera;
    }

    public void setImagenCartelera(int imagenCartelera) {
        this.imagenCartelera = imagenCartelera;
    }
*/
/*
    public Bitmap getBitImagen() {
        return bitImagen;
    }

    public void setBitImagen(String bitmap) {
        try {
            byte[] bytecode= Base64.decode(bitmap, Base64.DEFAULT);
            this.bitImagen = BitmapFactory.decodeByteArray(bytecode, 0, bytecode.length);
        } catch (Exception e)  {
            e.printStackTrace();
        }

    }
*/
    public String getUrlImagen() {
        return urlImagen;
    }
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
    //PARA PASAR UN OBJETO COMO PARAMETRO DE INTENT A OTRA ACTIVITY

    protected modelCartelera(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        id = in.readInt();
        //imagenCartelera = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(id);
        //dest.writeInt(imagenCartelera);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<modelCartelera> CREATOR = new Parcelable.Creator<modelCartelera>() {
        @Override
        public modelCartelera createFromParcel(Parcel in) {
            return new modelCartelera(in);
        }

        @Override
        public modelCartelera[] newArray(int size) {
            return new modelCartelera[size];
        }
    };
}
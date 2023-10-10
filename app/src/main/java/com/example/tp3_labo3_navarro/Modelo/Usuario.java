package com.example.tp3_labo3_navarro.Modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String apellido;
    private long dni;
    private String mail;
    private String clave;

    private String foto;



    public Usuario(String nombre, String apellido, long dni, String mail, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.clave = clave;
        this.foto = dni +".png";
    }

    public Usuario(){

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

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}

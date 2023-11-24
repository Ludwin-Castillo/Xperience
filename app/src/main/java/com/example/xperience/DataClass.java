package com.example.xperience;

public class DataClass {

    private String titulo;
    private String image;
    private String categoria;
    private String sinopsis;
    private String hora;
    private String duracion;
    private String key;

    private String Lang;

    public DataClass(String titulo, String categoria, String sinopsis, String hora, String duracion, String imagenUrl) {
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataClass() {
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImage() {
        return image;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getHora() {
        return hora;
    }

    public String getDuracion() {
        return duracion;
    }

    public DataClass(String titulo, String categoria, String sinopsis, String hora, String duracion) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.sinopsis = sinopsis;
        this.hora = hora;
        this.duracion = duracion;
    }


}
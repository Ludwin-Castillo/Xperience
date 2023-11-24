package com.example.xperience;

public class DataClass {

    private String titulo;
    private String image;
    private String categoria;
    private String sinopsis;
    private String hora;
    private String duracion;
    private String Key;
    private String Lang;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    // Constructor sin argumentos necesario para Firebase
    public DataClass() {
        // Puedes dejarlo vacío o realizar alguna inicialización si es necesario
    }

    public DataClass(String titulo, String categoria, String sinopsis, String hora, String duracion) {
        // Constructor original
    }

    // Resto de tus métodos getter y setter

    public DataClass(String titulo, String image, String categoria, String sinopsis, String hora, String duracion, String key, String lang) {
        this.titulo = titulo;
        this.image = image;
        this.categoria = categoria;
        this.sinopsis = sinopsis;
        this.hora = hora;
        this.duracion = duracion;
        Key = key;
        Lang = lang;
    }
}

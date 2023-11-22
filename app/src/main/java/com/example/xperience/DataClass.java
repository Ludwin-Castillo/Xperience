package com.example.xperience;

public class DataClass {

    private String titulo;
    private String image;
    private String categoria;
    private String sinopsis;
    private String hora;
    private String duracion;

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

    public DataClass(String titulo, String categoria, String sinopsis, String hora, String duracion, String imageURL) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.sinopsis = sinopsis;
        this.hora = hora;
        this.duracion = duracion;
        this.image = imageURL;
    }
}
package com.company.securebike.auxiliares;

public class MyDoes {

    String color;
    String marca;
    String matricula;
    String keydoes;
    String usuario;
    String talla;
    String rasgos;


    public MyDoes() {
    }

    public MyDoes(String color, String marca, String matricula, String keydoes, String usuario, String talla, String rasgos) {
        this.color = color;
        this.marca = marca;
        this.matricula = matricula;
        this.keydoes = keydoes;
        this.usuario = usuario;
        this.talla = talla;
        this.rasgos = rasgos;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getRasgos() {
        return rasgos;
    }

    public void setRasgos(String rasgos) {
        this.rasgos = rasgos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getKeydoes() {
        return keydoes;
    }

    public void setKeydoes(String keydoes) {
        this.keydoes = keydoes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setDatedoes(String marca) {
        this.marca = marca;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}

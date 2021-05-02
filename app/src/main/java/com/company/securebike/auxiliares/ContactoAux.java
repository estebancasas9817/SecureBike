package com.company.securebike.auxiliares;

public class ContactoAux {

    private String nombre;
    private String numero;

    public ContactoAux()
    {

    }

    public ContactoAux(String nombrep, String numerop)
    {
        nombre = new String(nombrep);
        numero = new String(numerop);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}

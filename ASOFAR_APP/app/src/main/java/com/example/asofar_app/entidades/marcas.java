package com.example.asofar_app.entidades;

public class marcas {
    private String idmarcas;
    private String marcasnombre;


    public marcas() {
    }

    public marcas(String idmarcas, String marcasnombre) {
        this.idmarcas = idmarcas;
        this.marcasnombre = marcasnombre;
    }

    public String getIdmarcas() {
        return idmarcas;
    }

    public void setIdmarcas(String idmarcas) {
        this.idmarcas = idmarcas;
    }

    public String getMarcasnombre() {
        return marcasnombre;
    }

    public void setMarcasnombre(String marcasnombre) {
        this.marcasnombre = marcasnombre;
    }

    @Override
    public String toString() {
        return marcasnombre;
    }
}

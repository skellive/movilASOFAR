package com.example.asofar_app.entidades;

public class medidas {
    private String idmedidas;
    private String medidasnombre;

    public medidas() {
    }

    public medidas(String idmedidas, String medidasnombre) {
        this.idmedidas = idmedidas;
        this.medidasnombre = medidasnombre;
    }


    public String getIdmedidas() {
        return idmedidas;
    }

    public void setIdmedidas(String idmedidas) {
        this.idmedidas = idmedidas;
    }

    public String getMedidasnombre() {
        return medidasnombre;
    }

    public void setMedidasnombre(String medidasnombre) {
        this.medidasnombre = medidasnombre;
    }

    @Override
    public String toString() {
        return medidasnombre;
    }
}

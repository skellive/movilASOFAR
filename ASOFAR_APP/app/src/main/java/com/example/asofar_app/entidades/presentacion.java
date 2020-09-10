package com.example.asofar_app.entidades;

public class presentacion {

    private String idpresentacion;
    private String presentacionnombre;

    public presentacion() {
    }

    public presentacion(String idpresentacion, String presentacionnombre) {
        this.idpresentacion = idpresentacion;
        this.presentacionnombre = presentacionnombre;
    }

    public String getIdpresentacion() {
        return idpresentacion;
    }

    public void setIdpresentacion(String idpresentacion) {
        this.idpresentacion = idpresentacion;
    }

    public String getPresentacionnombre() {
        return presentacionnombre;
    }

    public void setPresentacionnombre(String presentacionnombre) {
        this.presentacionnombre = presentacionnombre;
    }

    @Override
    public String toString() {
        return presentacionnombre;
    }
}

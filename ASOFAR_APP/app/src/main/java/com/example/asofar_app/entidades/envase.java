package com.example.asofar_app.entidades;

public class envase {
    private String idenvase;
    private String envasenombre;

    public envase() {
    }

    public envase(String idenvase, String envasenombre) {
        this.idenvase = idenvase;
        this.envasenombre = envasenombre;
    }

    public String getIdenvase() {
        return idenvase;
    }

    public void setIdenvase(String idenvase) {
        this.idenvase = idenvase;
    }

    public String getEnvasenombre() {
        return envasenombre;
    }

    public void setEnvasenombre(String envasenombre) {
        this.envasenombre = envasenombre;
    }

    @Override
    public String toString() {
        return  envasenombre;
    }
}

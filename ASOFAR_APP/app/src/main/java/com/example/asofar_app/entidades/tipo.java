package com.example.asofar_app.entidades;

public class tipo {
    private String idtipo;
    private String tiponombre;

    public tipo() {
    }

    public tipo(String idtipo, String tiponombre) {
        this.idtipo = idtipo;
        this.tiponombre = tiponombre;
    }

    public String getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(String idtipo) {
        this.idtipo = idtipo;
    }

    public String getTiponombre() {
        return tiponombre;
    }

    public void setTiponombre(String tiponombre) {
        this.tiponombre = tiponombre;
    }

    @Override
    public String toString() {
        return tiponombre;
    }
}

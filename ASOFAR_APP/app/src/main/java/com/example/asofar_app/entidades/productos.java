package com.example.asofar_app.entidades;

public class productos {

    private Integer id;
    private String cod_barra;
    private String nom_producto;
    private String fecha_creacion;

    public productos(Integer id, String cod_barra, String nom_producto, String fecha_creacion) {
        this.id = id;
        this.cod_barra = cod_barra;
        this.nom_producto = nom_producto;
        this.fecha_creacion = fecha_creacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    public String getNom_producto() {
        return nom_producto;
    }

    public void setNom_producto(String nom_producto) {
        this.nom_producto = nom_producto;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}

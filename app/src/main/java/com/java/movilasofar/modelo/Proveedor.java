package com.java.movilasofar.modelo;

 public class Proveedor {
    private int id_proveedor;
    private String nombre;

    public Proveedor(){}

    public Proveedor(int id_proveedor, String nombre){
        this.setId_proveedor(id_proveedor);
        this.setNombre(nombre);
    }

     public int getId_proveedor() {
         return id_proveedor;
     }

     public void setId_proveedor(int id_proveedor) {
         this.id_proveedor = id_proveedor;
     }

     public String getNombre() {
         return nombre;
     }

     public void setNombre(String nombre) {
         this.nombre = nombre;
     }
 }

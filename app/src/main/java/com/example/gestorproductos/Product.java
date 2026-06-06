package com.example.gestorproductos;

public class Product {

    private int id;
    private String nombre;
    private double precio;
    private String imagen;
    private int tienda_id;

    public Product(){}

    public Product(int id, String nombre, double precio, String imagen, int tienda_id) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.tienda_id = tienda_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTienda_id() {
        return tienda_id;
    }

    public void setTienda_id(int tienda_id) {
        this.tienda_id = tienda_id;
    }
}

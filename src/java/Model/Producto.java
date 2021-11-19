/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author juand
 */
public class Producto {

    private String nombre;
    private String idProducto;
    private byte[] foto;
    private double precio;
    private String precio_Mascara;
    private int stock;

    private CATEGORIA categoria;
    private StreamedContent imagenDisplay;

    private ImageIcon imagenDisplay2;

    private int cantComprada;
    private double totalCantComprada;
    private String totalCantComprada_Mascara;
    private int activo;
    private String displayActivoInactivo = "";

    /**
     * Constructor con parámetros
     *
     * @param nombre
     * @param foto
     * @param precio
     * @param cantMinVenta
     * @param categoria
     * @param idProducto
     * @param imagenDisplay
     * @param cantComprada
     * @param totalCantComprada
     */
    public Producto(String nombre, String idProducto, byte[] foto, double precio, int cantMinVenta, CATEGORIA categoria,
            StreamedContent imagenDisplay, int cantComprada) {
        this.nombre = nombre;
        this.idProducto = idProducto;
        this.foto = foto;
        this.precio = precio;
        this.stock = cantMinVenta;
        this.categoria = categoria;
        this.imagenDisplay = imagenDisplay;
        this.cantComprada = cantComprada;
        this.totalCantComprada = cantComprada * (int) precio;

    }

    /**
     * Constructor sin parámetros
     */
    public Producto() {

    }
    //----------------------------------------------------------------------

    public String getPrecio_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return precio_Mascara = "₡" + ds.format(getPrecio());
    }

    public String getTotalCantComprada_Mascara() {
         DecimalFormat ds = new DecimalFormat("#,###.##");
        return totalCantComprada_Mascara = "₡" + ds.format(getTotalCantComprada());
    }

    //----------------------------------------------------------------------
    @Override
    public String toString() {
        return this.getNombre() + " " + this.getIdProducto(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Producto(CATEGORIA categoria) {
        this.categoria = categoria;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public CATEGORIA getCategoria() {
        return categoria;
    }

    public void setCategoria(CATEGORIA categoria) {
        this.categoria = categoria;
    }

    public StreamedContent getImagenDisplay() {

        return imagenDisplay;
    }

    public void setImagenDisplay(StreamedContent imagenDisplay) {
        this.imagenDisplay = imagenDisplay;
    }

    public int getCantComprada() {
        return cantComprada;
    }

    public void setCantComprada(int cantComprada) {
        this.cantComprada = cantComprada;
    }

    public double getTotalCantComprada() {
        this.totalCantComprada = cantComprada * (int) precio;
        return totalCantComprada;
    }

    public void setTotalCantComprada(double totalCantComprada) {
        this.totalCantComprada = totalCantComprada;
    }

    //------------------
    public ImageIcon getImagenDisplay2() {
        return imagenDisplay2;
    }

    public void setImagenDisplay2(ImageIcon imagenDisplay2) {
        this.imagenDisplay2 = imagenDisplay2;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
        displayActivoInactivo = (activo == 1) ? "Activo" : "Inactivo";
    }

    public String getDisplayActivoInactivo() {
        return displayActivoInactivo;
    }

    public void setDisplayActivoInactivo(String displayActivoInactivo) {
        this.displayActivoInactivo = displayActivoInactivo;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;
import Model.CATEGORIA;
import Model.Producto;
import ModelGestor.Logica_Productos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.rmi.server.LogStream;
import java.sql.SQLException;
import java.util.Base64;
import java.util.LinkedList;
import javafx.scene.layout.Region;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.naming.NamingException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author juand
 */
@Named(value = "bean_MantenimientoProductos")
@SessionScoped
public class Bean_Mantenimiento_Productos implements Serializable {

    private String etiquetaAyuda = "";
    // DATOS GENERALES DEL "PRODUCTO"
    private String nombre = "";
    private String idProducto = "";
    private UploadedFile foto = null;
    private byte[] displayFoto = "prueba".getBytes();
    private String precio = "";
    private int cantMinVenta = 1;
    private String categoria = "";
    private String mensaje = "";
    private String buscarID = "";
    private Producto producto;
    private String btnValue = "Guardar";
    private Logica_Productos lnProductos = new Logica_Productos();

    private LinkedList<Producto> productosDB = new LinkedList();
    private LinkedList<Producto> productosTable = new LinkedList();
    //LO ENCARGADO DE LOS COMBO-BOX
    private LinkedList<SelectItem> listaCategoria = new LinkedList<>();

    private String idCategoria = "0";

    /**
     * Creates a new instance of Bean_MantenimientoProductos
     */
    public Bean_Mantenimiento_Productos() throws SNMPExceptions, SQLException {

        actualizarListaProductos();
    }
    int n = 0;

//--VALIDACIONES----------------------------------------------------------------------------------------------------------------------------------------
    public boolean validarSeleccion() {
        if (producto == null) {
            return true;
        }
        return false;
    }

    public boolean validarNulos() {
        try {
            if (nombre.trim().equals("")) {
                return true;
            }
            if (idProducto.trim().equals("")) {
                return true;
            }
            if (idCategoria.equals("0")) {
                return true;
            }
            if (cantMinVenta == 0) {
                return true;
            }
            if (precio.trim().equals("")) {
                return true;
            }
            if (displayFoto.equals("prueba".getBytes())) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public boolean validarBuscarID() {
        if (buscarID.equals("")) {
            return true;
        }
        return false;
    }

    public boolean validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {
        return lnProductos.validaExistencia_Identificacion(pIdentificacion);
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//--FUNCIONES DE LA PÁGINA------------------------------------------------------------------------------------------------------------------------
    public void actualizarListaProductos() throws SNMPExceptions, SQLException {
        productosDB = new LinkedList();
        productosTable = new LinkedList();
        try {
            productosDB = lnProductos.getListaProductosMantenimiento();
            productosTable = productosDB;
        } catch (Exception e) {
            mensaje = "Ocurrió un error al tratar de cargar los productos de la DB";
        }

    }

    public void fotoProducto(FileUploadEvent event) throws IOException {

        foto = null;

        FacesMessage message = new FacesMessage("Perfecto", event.getFile().getFileName() + " se ha cargado.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        foto = event.getFile();
        displayFoto = foto.getContent();

    }

    public void ayuda() {
        if (n == 0) {
            n = 1;
            etiquetaAyuda = "Puede seleccionar un producto de la tabla y dar click en inspeccionar para cargar sus datos a los controles, si desea modificar asegurese de seleccionar un producto primero, los productos deshabilidatos no aparecen en el catálogo de productos";
        } else {
            n = 0;
            etiquetaAyuda = "";
        }

    }

    public void cargarDatosProductos() {
        if (!validarSeleccion()) {
            nombre = producto.getNombre();
            idProducto = producto.getIdProducto();
            precio = producto.getPrecio() + "";
            if (producto.getCategoria().toString().equals(CATEGORIA.CONGELADO.toString())) {
                idCategoria = "1";
            } else {
                if (producto.getCategoria().toString().equals(CATEGORIA.CONSERVA.toString())) {
                    idCategoria = "2";
                } else {
                    idCategoria = "3";
                }
            }
            cantMinVenta = producto.getStock();
            displayFoto = producto.getFoto();

        } else {

            mensaje = "Seleccione un producto primero";
        }
    }

    public void limpiar() {
        producto = new Producto();
        productosTable = productosDB;
        setPrecio(null);
        setNombre(null);
        setIdProducto(null);
        setIdCategoria("0");
        setFoto(null);
        setDisplayFoto("prueba".getBytes());
        setCantMinVenta(1);
        setCategoria(null);
        setMensaje(null);
        setBuscarID(null);

    }

    public void buscar() {
        if (!validarBuscarID()) {
            mensaje = "";
            productosTable = new LinkedList();
            for (Producto productoDB : productosDB) {
                if (productoDB.getIdProducto().equals(buscarID)) {
                    productosTable.add(productoDB);
                }
            }
        } else {
            mensaje = "Ingrese un ID en buscar producto";
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    //--CRUD DE PRODUCTOS----------------------------------------------------------------------------------------------------------------------------

    public void actualizarProducto() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        if (!validarSeleccion()) {
            if (!validarNulos()) {
                this.mensaje = "";

                Producto viejo = producto;
                Producto nuevo = new Producto();
                nuevo.setIdProducto(idProducto.trim());
                nuevo.setNombre(nombre);
                nuevo.setFoto(displayFoto);
                nuevo.setPrecio(Float.parseFloat(precio));
                nuevo.setStock(cantMinVenta);
                nuevo.setActivo(viejo.getActivo());
                if (idCategoria.equals("1")) {
                    nuevo.setCategoria(CATEGORIA.CONGELADO);
                } else {
                    if (idCategoria.equals("2")) {
                        nuevo.setCategoria(CATEGORIA.CONSERVA);
                    } else {
                        nuevo.setCategoria(CATEGORIA.ENLATADO);
                    }
                }

//                try {
                if (nuevo.getIdProducto().equals(viejo.getIdProducto())) {
                    lnProductos.updateProducto(viejo, nuevo);
                    actualizarListaProductos();
                    limpiar();
                    mensaje = "Producto Actualizado";
                } else {

                    if (!validaExistencia_Identificacion(nuevo.getIdProducto())) {
                        lnProductos.updateProducto(viejo, nuevo);
                        actualizarListaProductos();
                        limpiar();
                        mensaje = "Producto Actualizado";
                    } else {
                        limpiar();
                        mensaje = "El ID que está ingresando ya existe en la DB, ingrese uno diferente";
                    }
                }
                lnProductos = new Logica_Productos();

//                } catch (Exception e) {
//                    mensaje = "Ocurrió un problema con la DB";
//                }
            } else {
                this.mensaje = "Ingrese datos correctamente";
            }

        } else {
            this.mensaje = "Seleccione un producto primero";
        }

    }

    public void cambiarEstadoProducto(){
        if (!validarSeleccion()) {
            mensaje="";
            if (producto.getActivo()==1) {
                try {
                      inhabilitarProducto();
                } catch (Exception e) {
                    mensaje = "Ocurrió un error en la DB";
                    
                }
              
            }else{
                  try {
                      habilitarProducto();
                } catch (Exception e) {
                    mensaje = "Ocurrió un error en la DB";
                }
            }
        }else{
        mensaje = "Seleccione un producto primero";
        
        }
    
    }
    public void habilitarProducto() {
        try {
            if (!validarSeleccion()) {
                mensaje = "";

                lnProductos.habilitarProducto(producto.getIdProducto());

                actualizarListaProductos();
            } else {
                mensaje = "Seleccione un producto primero";
            }
        } catch (Exception e) {
            mensaje = "Ocurrió un error en la DB";

        }
    }

    public void inhabilitarProducto() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        try {
            if (!validarSeleccion()) {
                mensaje = "";

                lnProductos.deleteProducto(producto.getIdProducto());

                actualizarListaProductos();
            } else {
                mensaje = "Seleccione un producto primero";
            }
        } catch (Exception e) {
            mensaje = "Ocurrió un error en la DB";

        }

    }

    public void insertarProducto() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        //Validación

        if (!validarNulos()) {
            this.mensaje = "";
            if (!validaExistencia_Identificacion(getIdProducto())) {
                mensaje = "";
                Producto producto = new Producto();
                producto.setIdProducto(idProducto.trim());
                producto.setNombre(nombre);
                producto.setFoto(displayFoto);
                producto.setPrecio(Float.parseFloat(precio));
                producto.setStock(cantMinVenta);
                if (idCategoria.equals("1")) {
                    producto.setCategoria(CATEGORIA.CONGELADO);
                } else {
                    if (idCategoria.equals("2")) {
                        producto.setCategoria(CATEGORIA.CONSERVA);
                    } else {
                        producto.setCategoria(CATEGORIA.ENLATADO);
                    }
                }

                lnProductos.insertProducto(producto);
                actualizarListaProductos();
                lnProductos = new Logica_Productos();
                limpiar();
                this.mensaje = "Producto Registrado";
            } else {
                this.mensaje = "La Identificación ya existe ";
            }

        } else {
            this.mensaje = "Ingrese los datos correctamente";
        }

    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--SETS Y GETS-----------------------------------------------------------------------------------------------------------------------------------------------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public UploadedFile getFoto() {
        return foto;
    }

    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getCantMinVenta() {
        return cantMinVenta;
    }

    public void setCantMinVenta(int cantMinVenta) {
        this.cantMinVenta = cantMinVenta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getEtiquetaAyuda() {
        return etiquetaAyuda;
    }

    public void setEtiquetaAyuda(String etiquetaAyuda) {
        this.etiquetaAyuda = etiquetaAyuda;
    }

    public LinkedList<SelectItem> getListaCategoria() {
        listaCategoria = new LinkedList<>();
        listaCategoria.add(new SelectItem(0, "- SELECCIONAR -"));
        listaCategoria.add(new SelectItem(1, "Congelado"));
        listaCategoria.add(new SelectItem(2, "Conserva"));
        listaCategoria.add(new SelectItem(3, "Enlatado"));
        return listaCategoria;
    }

    public void setListaCategoria(LinkedList<SelectItem> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public byte[] getDisplayFoto() {
        return displayFoto;
    }

    public void setDisplayFoto(byte[] displayFoto) {
        this.displayFoto = displayFoto;
    }

    public String getBuscarID() {
        return buscarID;
    }

    public void setBuscarID(String buscarID) {
        this.buscarID = buscarID;
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
    public LinkedList<Producto> getProductosDB() {
        return productosDB;
    }

    public void setProductosDB(LinkedList<Producto> productosDB) {
        this.productosDB = productosDB;
    }

    public LinkedList<Producto> getProductosTable() {
        return productosTable;
    }

    public void setProductosTable(LinkedList<Producto> productosTable) {
        this.productosTable = productosTable;
    }

    public String getBtnValue() {
        return btnValue;
    }

    public void setBtnValue(String btnValue) {
        this.btnValue = btnValue;
    }

}

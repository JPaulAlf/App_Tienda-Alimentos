/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;
import Model.CATEGORIA;
import Model.Cliente;
import Model.ESTADO_PEDIDO;
import Model.Producto;
import Model.Transaccion;
import ModelGestor.Logica_Transaccion;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.naming.NamingException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author John P. Alfaro
 */
@Named(value = "bean_Mantenimiento_Despacho")
@SessionScoped
public class Bean_Mantenimiento_Despacho implements Serializable {

    private Logica_Transaccion log = new Logica_Transaccion();

    private LinkedList<Transaccion> listaTransacciones = new LinkedList();
    private Transaccion transaccionSeleccionada = new Transaccion();

    private LinkedList<Producto> listaProductosTransaccion = new LinkedList();
    private Producto productoSeleccionado = new Producto();

    private String alertTabla = "";
    private String alertTabla2 = "";

    private String estadoPedido = "--$$$--";
    private String fechaEstimadaPedido = "--$$$--";
    private String subtotalPedido = "--$$$--";
    private String descuentoPedido = "--$$$--";
    private String impuestoPedido = "--$$$--";
    private String totalPedido = "--$$$--";

    private String banderaBoton = "true";
    private String banderaBoton_Estilo = "background-color: #F8F1E5; color: #F8F1E5; border: 1px solid #2F3131;";

    /**
     * Creates a new instance of Bean_Mantenimiento_Despacho
     */
    public Bean_Mantenimiento_Despacho() throws SNMPExceptions, SQLException {
        this.llenaLista();
    }
    //=====================================================
    //-------------------------------------------------------
    //    LOGICA DE LA PAGINA
    //-------------------------------------------------------

    public void llenaLista() throws SNMPExceptions, SQLException {
         listaTransacciones = new LinkedList();
        log = new Logica_Transaccion();
        this.listaProductosTransaccion.clear();
        this.setListaTransacciones((LinkedList<Transaccion>) this.log.obtieneLista_Mantenimiento_Despacho());
    }

    public void limpiar() {
        this.transaccionSeleccionada = new Transaccion();
        this.listaProductosTransaccion.clear();
        this.estadoPedido = "--$$$--";
        this.fechaEstimadaPedido = "--$$$--";
        this.subtotalPedido = "--$$$--";
        this.descuentoPedido = "--$$$--";
        this.impuestoPedido = "--$$$--";
        this.totalPedido = "--$$$--";
        this.setBanderaBoton("true");
        this.setBanderaBoton_Estilo("background-color: #F8F1E5; color: #F8F1E5; border: 1px solid #2F3131;");
    }

    public void verDetalles() {
        if (transaccionSeleccionada != null) {

            this.setBanderaBoton("false");
            this.setBanderaBoton_Estilo("background-color: #a12312;border: 0px solid #2F3131;");

            listaProductosTransaccion.clear();
            if (listaProductosTransaccion.isEmpty()) {
                for (Producto item : transaccionSeleccionada.getListaProductos()) {
                    this.listaProductosTransaccion.add(item);
                }
            }

            this.totalPedido = transaccionSeleccionada.getTotal_Mascara();
            this.estadoPedido = transaccionSeleccionada.getEstadoPedido().toString();
            this.fechaEstimadaPedido = transaccionSeleccionada.getFecEstimada_Mascara();
            this.subtotalPedido = transaccionSeleccionada.getSubtotal_Mascara();
            this.impuestoPedido = transaccionSeleccionada.getImpuestoMonto_Mascara();
            this.descuentoPedido = transaccionSeleccionada.getDescuentoMonto_Mascara();

        }

    }

    public void despacharPedido() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        if (this.transaccionSeleccionada != null) {

            this.estadoPedido = "--$$$--";
            this.fechaEstimadaPedido = "--$$$--";
            this.subtotalPedido = "--$$$--";
            this.descuentoPedido = "--$$$--";
            this.impuestoPedido = "--$$$--";
            this.totalPedido = "--$$$--";
            this.setBanderaBoton("true");
            this.setBanderaBoton_Estilo("background-color: #F8F1E5; color: #F8F1E5; border: 1px solid #2F3131;");

            String fechaActual = DateTimeFormatter.ofPattern("dd/MMM/yyyy").format(LocalDateTime.now());
            String horaActual = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());

            this.log.apruebaPedido_Despacho(transaccionSeleccionada, fechaActual, horaActual);
            this.llenaLista();

            this.listaTransacciones.remove(this.transaccionSeleccionada);
            this.transaccionSeleccionada = new Transaccion();
            this.listaProductosTransaccion.clear();
        }
    }
    //=====================================================
    //
    //
    //

    public LinkedList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaTransacciones(LinkedList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
    }

    public Transaccion getTransaccionSeleccionada() {
        return transaccionSeleccionada;
    }

    public void setTransaccionSeleccionada(Transaccion transaccionSeleccionada) {
        this.transaccionSeleccionada = transaccionSeleccionada;
    }

    public LinkedList<Producto> getListaProductosTransaccion() {
        return listaProductosTransaccion;
    }

    public void setListaProductosTransaccion(LinkedList<Producto> listaProductosTransaccion) {
        this.listaProductosTransaccion = listaProductosTransaccion;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public String getAlertTabla() {
        return alertTabla;
    }

    public void setAlertTabla(String alertTabla) {
        this.alertTabla = alertTabla;
    }

    public String getAlertTabla2() {
        return alertTabla2;
    }

    public void setAlertTabla2(String alertTabla2) {
        this.alertTabla2 = alertTabla2;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getFechaEstimadaPedido() {
        return fechaEstimadaPedido;
    }

    public void setFechaEstimadaPedido(String fechaEstimadaPedido) {
        this.fechaEstimadaPedido = fechaEstimadaPedido;
    }

    public String getSubtotalPedido() {
        return subtotalPedido;
    }

    public void setSubtotalPedido(String subtotalPedido) {
        this.subtotalPedido = subtotalPedido;
    }

    public String getDescuentoPedido() {
        return descuentoPedido;
    }

    public void setDescuentoPedido(String descuentoPedido) {
        this.descuentoPedido = descuentoPedido;
    }

    public String getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(String totalPedido) {
        this.totalPedido = totalPedido;
    }

    public String getBanderaBoton() {
        return banderaBoton;
    }

    public void setBanderaBoton(String banderaBoton) {
        this.banderaBoton = banderaBoton;
    }

    public String getBanderaBoton_Estilo() {
        return banderaBoton_Estilo;
    }

    public void setBanderaBoton_Estilo(String banderaBoton_Estilo) {
        this.banderaBoton_Estilo = banderaBoton_Estilo;
    }

    public String getImpuestoPedido() {
        return impuestoPedido;
    }

    public void setImpuestoPedido(String impuestoPedido) {
        this.impuestoPedido = impuestoPedido;
    }

}

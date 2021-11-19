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
import Model.Transaccion;
import Model.Producto;
import ModelGestor.Logica_Transaccion;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author John P. Alfaro
 */
@Named(value = "bean_Cliente_Pedido")
@SessionScoped
public class Bean_Perfil_Pedidos implements Serializable {

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

    /**
     * Creates a new instance of Bean_Cliente_Pedido
     */
    public Bean_Perfil_Pedidos() throws SNMPExceptions, SQLException {
        this.llenaLista();
    }

    //=====================================================
    //-------------------------------------------------------
    //    LOGICA DE LA PAGINA
    //-------------------------------------------------------
    public void llenaLista() throws SNMPExceptions, SQLException {

        Logica_Transaccion log = new Logica_Transaccion();
        listaTransacciones = new LinkedList<>();
        listaTransacciones.clear();

        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;
        this.setListaTransacciones((LinkedList<Transaccion>) log.obtieneLista_Cliente_Transacciones(clienteLogeado));
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
    }

    public void verDetalles() {
        if (transaccionSeleccionada != null) {

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

    //=====================================================
    //
    //
    //
    public LinkedList<Producto> getListaProductosTransaccion() {
        return listaProductosTransaccion;
    }

    public void setListaProductosTransaccion(LinkedList<Producto> listaProductosTransaccion) {
        this.listaProductosTransaccion = listaProductosTransaccion;
    }

    public LinkedList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaTransacciones(LinkedList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
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

    public Transaccion getTransaccionSeleccionada() {
        return transaccionSeleccionada;
    }

    public void setTransaccionSeleccionada(Transaccion transaccionSeleccionada) {
        this.transaccionSeleccionada = transaccionSeleccionada;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
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

    public String getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(String totalPedido) {
        this.totalPedido = totalPedido;
    }

    public String getDescuentoPedido() {
        return descuentoPedido;
    }

    public void setDescuentoPedido(String descuentoPedido) {
        this.descuentoPedido = descuentoPedido;
    }

    public String getImpuestoPedido() {
        return impuestoPedido;
    }

    public void setImpuestoPedido(String impuestoPedido) {
        this.impuestoPedido = impuestoPedido;
    }

}

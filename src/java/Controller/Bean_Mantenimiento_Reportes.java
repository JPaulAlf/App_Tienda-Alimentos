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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.naming.NamingException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author juand
 */
@Named(value = "bean_Mantenimiento_Reportes")
@SessionScoped
public class Bean_Mantenimiento_Reportes implements Serializable {

    private Logica_Transaccion log = new Logica_Transaccion();

    private LinkedList<Transaccion> listaTransacciones = new LinkedList();
    private Transaccion transaccionSeleccionada = new Transaccion();

    private LinkedList<Producto> listaProductosTransaccion = new LinkedList();
    private Producto productoSeleccionado = new Producto();

    private List<Date> range;

    private LinkedList<String> listaEstados = new LinkedList();
    private String estado = "Cualquiera";
    private String tipoPago = "0";

    private String alertTabla = "";
    private String alertTabla2 = "";

    private String estadoPedido = "--$$$--";
    private String fechaEstimadaPedido = "--$$$--";
    private String subtotalPedido = "--$$$--";
    private String descuentoPedido = "--$$$--";
    private String impuestoPedido = "--$$$--";
    private String totalPedido = "--$$$--";

    /**
     * Creates a new instance of Bean_Mantenimiento_Despacho
     */
    public Bean_Mantenimiento_Reportes() throws SNMPExceptions, SQLException {
        List<Transaccion> lista = this.log.obtieneLista_Mantenimiento_Reportes();
        listaTransacciones = new LinkedList();

        for (Transaccion transaccion : lista) {
            if (tipoPago.equals("1")) {
                if (transaccion.isEsEfectivo()) {
                    listaTransacciones.add(transaccion);
                }
            } else {
                if (!transaccion.isEsEfectivo()) {
                    listaTransacciones.add(transaccion);
                }
            }

        }
    }
    //=====================================================
    //-------------------------------------------------------
    //    LOGICA DE LA PAGINA
    //-------------------------------------------------------

    public void llenaLista() throws SNMPExceptions, SQLException {
        List<Transaccion> lista = this.log.obtieneLista_Mantenimiento_Reportes();

        if (range != null) {
            if (range.size() == 2) {
                listaTransacciones = new LinkedList();
                alertTabla = "";

                for (Transaccion transaccion : lista) {

                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    LocalDate localDate = transaccion.getFecEstimada().toLocalDate();
                    Date date = Date
                            .from(localDate.atStartOfDay(defaultZoneId).toInstant());

                    if ((date.after(range.get(0))|| date.equals(range.get(0))) && (date.before(range.get(1)) || date.equals(range.get(1)))) {
                        if (tipoPago.equals("1")) {
                            if (transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        } else {
                            if (!transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        }
                    }

                }
            } else {
                alertTabla = "seleccione un rango de fechas primero";
            }
        } else {
            listaTransacciones = new LinkedList();
            alertTabla = "";

            for (Transaccion transaccion : lista) {

                if (tipoPago.equals("1")) {
                    if (transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }
                } else {
                    if (!transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }

                }
            }
        }

    }

    public void llenaListaPendiente() throws SNMPExceptions, SQLException {
         List<Transaccion> lista = this.log.obtieneLista_Mantenimiento_Facturacion();

        if (range != null) {
            if (range.size() == 2) {
                listaTransacciones = new LinkedList();
                alertTabla = "";

                for (Transaccion transaccion : lista) {

                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    LocalDate localDate = transaccion.getFecEstimada().toLocalDate();
                    Date date = Date
                            .from(localDate.atStartOfDay(defaultZoneId).toInstant());

                    if ((date.after(range.get(0))|| date.equals(range.get(0))) && (date.before(range.get(1)) || date.equals(range.get(1)))) {
                        if (tipoPago.equals("1")) {
                            if (transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        } else {
                            if (!transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        }
                    }

                }
            } else {
                alertTabla = "seleccione un rango de fechas primero";
            }
        } else {
            listaTransacciones = new LinkedList();
            alertTabla = "";

            for (Transaccion transaccion : lista) {

                if (tipoPago.equals("1")) {
                    if (transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }
                } else {
                    if (!transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }

                }
            }
        }
    }

    public void llenaListaProceso() throws SNMPExceptions, SQLException {
          List<Transaccion> lista = this.log.obtieneLista_Mantenimiento_Despacho();

        if (range != null) {
            if (range.size() == 2) {
                listaTransacciones = new LinkedList();
                alertTabla = "";

                for (Transaccion transaccion : lista) {

                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    LocalDate localDate = transaccion.getFecEstimada().toLocalDate();
                    Date date = Date
                            .from(localDate.atStartOfDay(defaultZoneId).toInstant());

                    if ((date.after(range.get(0))|| date.equals(range.get(0))) && (date.before(range.get(1)) || date.equals(range.get(1)))) {
                        if (tipoPago.equals("1")) {
                            if (transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        } else {
                            if (!transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        }
                    }

                }
            } else {
                alertTabla = "seleccione un rango de fechas primero";
            }
        } else {
            listaTransacciones = new LinkedList();
            alertTabla = "";

            for (Transaccion transaccion : lista) {

                if (tipoPago.equals("1")) {
                    if (transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }
                } else {
                    if (!transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }

                }
            }
        }
    }

    public void llenaListaFinalizado() throws SNMPExceptions, SQLException {
         List<Transaccion> lista = this.log.obtieneLista_Mantenimiento_Finalizado();

        if (range != null) {
            if (range.size() == 2) {
                listaTransacciones = new LinkedList();
                alertTabla = "";

                for (Transaccion transaccion : lista) {

                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    LocalDate localDate = transaccion.getFecEstimada().toLocalDate();
                    Date date = Date
                            .from(localDate.atStartOfDay(defaultZoneId).toInstant());

                    if ((date.after(range.get(0))|| date.equals(range.get(0))) && (date.before(range.get(1)) || date.equals(range.get(1)))) {
                        if (tipoPago.equals("1")) {
                            if (transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        } else {
                            if (!transaccion.isEsEfectivo()) {
                                listaTransacciones.add(transaccion);
                            }
                        }
                    }

                }
            } else {
                alertTabla = "seleccione un rango de fechas primero";
            }
        } else {
            listaTransacciones = new LinkedList();
            alertTabla = "";

            for (Transaccion transaccion : lista) {

                if (tipoPago.equals("1")) {
                    if (transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }
                } else {
                    if (!transaccion.isEsEfectivo()) {
                        listaTransacciones.add(transaccion);
                    }

                }
            }
        }
    }

    public void limpiar() {
        this.transaccionSeleccionada = new Transaccion();
        this.listaProductosTransaccion.clear();
        this.estadoPedido = "--$$$--";
        this.fechaEstimadaPedido = "--$$$--";
        this.subtotalPedido = "--$$$--";
        this.descuentoPedido = "--$$$--";
        this.totalPedido = "--$$$--";
        this.impuestoPedido = "--$$$--";
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
            this.descuentoPedido = transaccionSeleccionada.getDescuentoMonto_Mascara();
            this.impuestoPedido = transaccionSeleccionada.getImpuestoMonto_Mascara();
        }

    }

    public void filtrarReporte() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        if (this.estado != null) {

            if (tipoPago.equals("0")) {
                if (estado.equals("Cualquiera")) {
                    llenaLista();
                }
                if (estado.equals(ESTADO_PEDIDO.PENDIENTE.toString())) {
                    llenaListaPendiente();
                }
                if (estado.equals(ESTADO_PEDIDO.PROCESO.toString())) {
                    llenaListaProceso();
                }
                if (estado.equals(ESTADO_PEDIDO.FINALIZADO.toString())) {
                    llenaListaFinalizado();
                }
            } else {

                if (estado.equals("Cualquiera")) {
                    llenaLista();
                }
                if (estado.equals(ESTADO_PEDIDO.PENDIENTE.toString())) {
                    llenaListaPendiente();
                }
                if (estado.equals(ESTADO_PEDIDO.PROCESO.toString())) {
                    llenaListaProceso();
                }
                if (estado.equals(ESTADO_PEDIDO.FINALIZADO.toString())) {
                    llenaListaFinalizado();
                }
            }

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

    public LinkedList<String> getListaEstados() {
        listaEstados = new LinkedList();
        listaEstados.add("Cualquiera");
        listaEstados.add(ESTADO_PEDIDO.PENDIENTE.toString());
        listaEstados.add(ESTADO_PEDIDO.PROCESO.toString());
        listaEstados.add(ESTADO_PEDIDO.FINALIZADO.toString());

        return listaEstados;
    }

    public void setListaEstados(LinkedList listaEstados) {
        this.listaEstados = listaEstados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getImpuestoPedido() {
        return impuestoPedido;
    }

    public void setImpuestoPedido(String impuestoPedido) {
        this.impuestoPedido = impuestoPedido;
    }

    public List<Date> getRange() {
        return range;
    }

    public void setRange(List<Date> range) {
        this.range = range;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author juand
 */
public class Transaccion {
//VALOR DE DESCUENTO SE MODIFICA EN EL CONSTRUCTOR DE LA INSTANCIA 
    //EL IMPUESTO SE GUARDA EN LA VARIABLE impuestoMonto
    //EL PORCENTAJE DEL DESCUENTO Y DEL IMPUESTO NO VARIAN A NO SER QUE SE CAMBIE SU VALOR EN EL CONSTRUCTOR

    private String idTransaccion;
    private String numeroDespacho;

    private Cliente cliente;

    private LocalDateTime fecEstimada;
    private String fecEstimada_Mascara;

    private HORARIO horarioEntrega;
    private String direccionEntrega;
   
    private List<Producto> listaProductos = new ArrayList<>();
    
    private ESTADO_PEDIDO estadoPedido;
    
    private TIPO_ENTREGA tipoEntrega;
    
    private boolean hayDescuento = false;
    
    private boolean esEfectivo;
    private String detEsEfectivo;
    
    private double IVA, descuento, descuentoMonto, impuestoMonto, subtotal, total;
    private String IVA_Mascara, descuento_Mascara, descuentoMonto_Mascara, impuestoMonto_Mascara, subtotal_Mascara, total_Mascara;

//------------------------------------------------- 
//------------------------------------------------- 
    /**
     * Constructor sin parámetros
     *
     * @param cliente
     * @param direccionEntrega
     * @param horarioEntrega
     * @param listaProductos
     * @param estadoPedido
     * @param IVA
     * @param descuento
     * @param esEfectivo
     * @param fecEstimada
     * @param idTransaccion
     * @param subtotal
     * @param total
     */
    public Transaccion(String idTransaccion, Cliente cliente, LocalDateTime fecEstimada, HORARIO horarioEntrega, String direccionEntrega,
            List<Producto> listaProductos, ESTADO_PEDIDO estadoPedido, boolean esEfectivo, double IVA, double descuento, double subtotal, double total) {
        this.idTransaccion = idTransaccion;
        this.cliente = cliente;
        this.fecEstimada = fecEstimada;
        this.horarioEntrega = horarioEntrega;
        this.direccionEntrega = direccionEntrega;
        this.listaProductos = listaProductos;
        this.estadoPedido = estadoPedido;
        this.esEfectivo = esEfectivo;
        this.IVA = IVA;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.total = total;
        this.descuento = 0.25;
        this.IVA = 0.13;

    }

    /**
     * Constructor sin parámetros
     */
    public Transaccion() {
        listaProductos = new LinkedList();
        this.descuento = 0.25;
        this.IVA = 0.15;

    }
//-------------------------------------------------
//-------------------------------------------------

    public String getFecEstimada_Mascara() {
        return fecEstimada_Mascara = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(getFecEstimada());
    }

    public String getIVA_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return IVA_Mascara = "₡" + ds.format(getIVA());
    }

    public String getDescuento_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return descuento_Mascara = "₡" + ds.format(getDescuento());
    }

    public String getDescuentoMonto_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return descuentoMonto_Mascara = "₡" + ds.format(getDescuentoMonto());
    }

    public String getImpuestoMonto_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return impuestoMonto_Mascara = "₡" + ds.format(getImpuestoMonto());
    }

    public String getSubtotal_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return subtotal_Mascara = "₡" + ds.format(getSubtotal());
    }

    public String getTotal_Mascara() {
        DecimalFormat ds = new DecimalFormat("#,###.##");
        return total_Mascara = "₡" + ds.format(getTotal());
    }

//-------------------------------------------------
//-------------------------------------------------
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecEstimada() {
        return fecEstimada;
    }

    public void setFecEstimada(LocalDateTime fecEstimada) {
        this.fecEstimada = fecEstimada;
    }

    public HORARIO getHorarioEntrega() {
        return horarioEntrega;
    }

    public void setHorarioEntrega(HORARIO horarioEntrega) {
        this.horarioEntrega = horarioEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public ESTADO_PEDIDO getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(ESTADO_PEDIDO estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public boolean isEsEfectivo() {
        return esEfectivo;
    }

    public void setEsEfectivo(boolean esEfectivo) {
        this.esEfectivo = esEfectivo;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDetEsEfectivo() {
        return esEfectivo ? "Efectivo" : "CxC";
    }

    public void setDetEsEfectivo(String detEsEfectivo) {
        this.detEsEfectivo = detEsEfectivo;
    }

    public boolean isHayDescuento() {
        return hayDescuento;
    }

    public void setHayDescuento(boolean hayDescuento) {
        this.hayDescuento = hayDescuento;
    }

    public double getImpuestoMonto() {
        return impuestoMonto;
    }

    public void setImpuestoMonto(double impuestoMonto) {
        this.impuestoMonto = impuestoMonto;
    }

    public double getDescuentoMonto() {
        return descuentoMonto;
    }

    public void setDescuentoMonto(double descuentoMonto) {
        this.descuentoMonto = descuentoMonto;
    }

    public TIPO_ENTREGA getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TIPO_ENTREGA tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getNumeroDespacho() {
        return numeroDespacho;
    }

    public void setNumeroDespacho(String numeroDespacho) {
        this.numeroDespacho = numeroDespacho;
    }

    
//-------------------------------------------------
//-------------------------------------------------
    public void descuentoTransaccion() {
        this.descuento = 0.0;
    }

    public void subtotalTransaccion() {
        double tt = 0;
        for (Producto listaProducto : listaProductos) {
            tt += listaProducto.getTotalCantComprada();
        }
        this.subtotal = tt;
    }

    public void impuestoTransaccion() {
        impuestoMonto = (getSubtotal() - descuentoMonto) * IVA;

    }

    public String totalTransaccion() {
        this.subtotalTransaccion();
        this.descuentoAplica();
        this.impuestoTransaccion();
        this.total = (subtotal - descuentoMonto) + impuestoMonto;
        DecimalFormat ds = new DecimalFormat("#,###.##");
        
        return "₡" + ds.format(total);

    }

    public void descuentoAplica() {

        int contador = 0;
        for (Producto producto : listaProductos) {
            contador += producto.getCantComprada();
        }
        if (contador >= 5) {
            setHayDescuento(true);
            descuentoMonto = subtotal * descuento;
        } else {
            setHayDescuento(false);
            descuentoMonto = 0.0;
        }
    }
}

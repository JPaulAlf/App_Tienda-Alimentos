/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;

import Model.Cliente;
import Model.Correo;
import Model.Direccion;
import Model.HORARIO;
import Model.Producto;
import Model.TIPO_ENTREGA;
import Model.Transaccion;
import ModelGestor.Logica_Productos;
import ModelGestor.Logica_Transaccion;

import java.io.IOException;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.LinkedList;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author juand
 */
@Named(value = "bean_Cliente_Productos")
@SessionScoped
public class Bean_Cliente_Productos implements Serializable {
//LISTAS CATALOGO

    private LinkedList<Producto> listaProductosCatalogo = new LinkedList();
    private Producto productoSeleccionado = new Producto();
    //----------------------------------------------------------------------------------------------------------
    //ALERTAS---------------------------------------------------------------------------------------------
    private String alertTabla = "";
    private String alertCarrito = "";
    private String alertDetalle = "";
//------------------------------------------------------------------------------------------------------
// LISTA CARRITO----------------------------------------------------------------------------------------------
    private LinkedList<Producto> carrito = new LinkedList();
    private Producto productoSeleccionadoCarrito = new Producto();
//---------------------------------------------------------------------------------------------------------------------

// COMBOS DETALLE------------------------------------------------------------------------------------------------------------------------
    private LinkedList<SelectItem> listaTipoEntrega = new LinkedList();
    private String tipoEntrega_ID = "0";
    private LinkedList<Direccion> listaDirecciones = new LinkedList();
    private Direccion direccionSeleccionada = new Direccion();
    private String tipoPago = "0";
    private LinkedList<SelectItem> listaHorario = new LinkedList();
    private String horarioSeleccionado = "0";

    //------------------------------------------------------------------------------------------------------------------------------------
    private String subtotal = "----";
    private String impuestos = "----";
    private String total = "----";
    private String descuento = "----";
    private Transaccion pedido = new Transaccion();
    private String fechaEstimada = "";
    private static Cliente cliente = new Cliente();

    /**
     * Creates a new instance of Bean_Cliente_Productos
     *
     * @throws DAO.SNMPExceptions
     */
    public Bean_Cliente_Productos() throws IOException, SNMPExceptions {
        Logica_Productos lnProductos = new Logica_Productos();

        pedido.setFecEstimada(LocalDateTime.now().plusDays(5));
        fechaEstimada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(pedido.getFecEstimada());

        try {
            listaProductosCatalogo = new LinkedList();
            listaProductosCatalogo = lnProductos.getListaProductos();

        } catch (Exception e) {
            alertTabla = "Hubo un problema con la bd";
            return;
        }

        listaHorario.add(new SelectItem(0, "--SELECCIONE--"));

    }

    //CATALOGO---------------------------------------------------------------------------------
    public void actualizarCatalogo() {
        Logica_Productos lnProductos = new Logica_Productos();
        try {
            listaProductosCatalogo = new LinkedList();
            listaProductosCatalogo = lnProductos.getListaProductos();

        } catch (Exception e) {
            alertTabla = "Hubo un problema con la bd";
            return;
        }

    }

    public void agregarCarrito() {

        if (productoSeleccionado == null) {
            alertTabla = "Seleccione un producto primero";
            return;
        } else {
            alertTabla = "";
        }

        //VALIDACIÓN  DE CANTIDAD DE PRODUCTOS
        if (carrito.contains(productoSeleccionado)) {
            int indicePoducto = carrito.indexOf(productoSeleccionado);
            if (carrito.get(indicePoducto).getCantComprada() == carrito.get(indicePoducto).getStock()) {
                alertTabla = "No se puede agregar este producto al carrito por que excede su stock";
                return;
            } else {
                alertTabla = "";
            }
            carrito.get(indicePoducto).setCantComprada(carrito.get(indicePoducto).getCantComprada() + 1);
        } else {
            productoSeleccionado.setCantComprada(1);
            carrito.add(productoSeleccionado);
        }

        pedido.setListaProductos(carrito);
        pedido.totalTransaccion();

        DecimalFormat ds = new DecimalFormat("#,###.##");
        subtotal = "₡" + ds.format(pedido.getSubtotal());
        descuento = "₡" + ds.format(pedido.getDescuentoMonto());
        impuestos = "₡" + ds.format(pedido.getImpuestoMonto());
        total = pedido.totalTransaccion();
        alertTabla = "Producto Agregado";
    }

    public void limpiarSeleccion() {

        if (productoSeleccionado == null) {
            return;
        }

        productoSeleccionado = new Producto();
        alertTabla = "";
    }
    //------------------------------------------------------------------------------------------------------

    //CARRITO-----------------------------------------------------------------------------------------
    public void eliminarProductoCarrito() {

        if (carrito.isEmpty()) {
            alertCarrito = "Agregue productos al carrito primero";
            return;
        }

        if (productoSeleccionadoCarrito == null) {
            alertCarrito = "Seleccione un producto primero";
            return;
        }

        int indicePoducto = carrito.indexOf(productoSeleccionadoCarrito);

        if (carrito.get(indicePoducto).getCantComprada() == 1) {

            carrito.remove(carrito.get(indicePoducto));

        } else {

            carrito.get(indicePoducto).setCantComprada(carrito.get(indicePoducto).getCantComprada() - 1);

        }
        pedido.setListaProductos(carrito);
        pedido.totalTransaccion();
        DecimalFormat ds = new DecimalFormat("#,###.##");
        subtotal = "₡" + ds.format(pedido.getSubtotal());
        descuento = "₡" + ds.format(pedido.getDescuentoMonto());
        impuestos = "₡" + ds.format(pedido.getImpuestoMonto());
        total = pedido.totalTransaccion();

    }

    public void detalleCompra() {
        if (carrito.isEmpty()) {
            alertCarrito = "Agregue productos al carrito primero";
            return;
        }
        try {
            alertCarrito = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("cliente-productos-inicioSesion.xhtml");
        } catch (Exception e) {
        }
    }
//------------------------------------------------------------------------------------------------------------------

//DETALLE------------------------------------------------------------------------------------------------------
    public void formalizarPedido() throws ClassNotFoundException, SQLException {
        Logica_Transaccion lnTransaccion = new Logica_Transaccion();
        if (!this.validarNulos()) {
            alertDetalle = "";

            pedido.setCliente(cliente);

            pedido.setDireccionEntrega(direccionSeleccionada.getSennas());

            String horario = listaHorario.get(Integer.parseInt(horarioSeleccionado)).getLabel();

            pedido.setHorarioEntrega((horario.equals(HORARIO.MANNANA.toString())
                    ? HORARIO.MANNANA : (horario.equals(HORARIO.NOCHE.toString()) ? HORARIO.NOCHE : HORARIO.TARDE)));

            String tipoEntrega = listaTipoEntrega.get(Integer.parseInt(tipoEntrega_ID)).getLabel();

            pedido.setTipoEntrega((tipoEntrega.equals(TIPO_ENTREGA.DIRECTO.toString())
                    ? TIPO_ENTREGA.DIRECTO : (tipoEntrega.equals(TIPO_ENTREGA.ENCOMIENDA.toString()) ? TIPO_ENTREGA.ENCOMIENDA : TIPO_ENTREGA.SIN_ENVIO)));
            pedido.setEsEfectivo((tipoPago.equals("1")));

            lnTransaccion.insertarEncabezadoTransaccion(pedido);
            for (Producto producto : pedido.getListaProductos()) {
                lnTransaccion.insertarDetalleTransaccion(producto);
            }

            //------------------ENVÍO DE CORREO----------------------------------------
              try {
                 FacesContext.getCurrentInstance().getExternalContext().redirect("cliente-productos-finalizacion.xhtml");
                
                 for (Producto producto : pedido.getListaProductos()) {
                lnTransaccion.updateStock(producto);
            }
            } catch (Exception e) {
            }
            Correo correo = new Correo();
            correo.enviarCorreoCompra(pedido);
           
            
           
            limpiarTodo();
            iniciar();
           
           
        } else {

        }

    }
    public void iniciar(){
     Logica_Productos lnProductos = new Logica_Productos();

        pedido.setFecEstimada(LocalDateTime.now().plusDays(5));
        fechaEstimada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(pedido.getFecEstimada());

        try {
            listaProductosCatalogo = new LinkedList();
            listaProductosCatalogo = lnProductos.getListaProductos();

        } catch (Exception e) {
            alertTabla = "Hubo un problema con la bd";
            return;
        }

        listaHorario.add(new SelectItem(0, "--SELECCIONE--"));
    
    
    }
    public void limpiarTodo() {

        listaProductosCatalogo = new LinkedList();
        productoSeleccionado = new Producto();

        alertTabla = "";
        alertCarrito = "";
        alertDetalle = "";

        carrito = new LinkedList();
        productoSeleccionadoCarrito = new Producto();

        listaTipoEntrega = new LinkedList();
        tipoEntrega_ID = "0";
        listaDirecciones = new LinkedList();
        direccionSeleccionada = new Direccion();
        tipoPago = "0";
        listaHorario = new LinkedList();
        horarioSeleccionado = "0";

        subtotal = "----";
        impuestos = "----";
        total = "----";
        descuento = "----";
        pedido = new Transaccion();
        fechaEstimada = "";
        cliente = new Cliente();

    }

//---------------------------------------------------------------------------------------------------------------
//VALIDACIONES-------------------------------------------------------------------------------------------------------------------------------------
    public boolean validarNulos() {
        SelectItem entregaItem = new SelectItem();
        SelectItem horarioItem = new SelectItem();
        if (direccionSeleccionada == null) {
            alertDetalle = "seleccione una dirección";
            return true;
        }

        for (SelectItem selectItem : getListaHorario()) {
            if (String.valueOf(selectItem.getValue()).equals(getHorarioSeleccionado())) {
                horarioItem = selectItem;
            }
        }

        if (horarioItem.getLabel().trim().equals("--SELECCIONE--")) {
            alertDetalle = "Seleccione el horario";
            return true;
        } else {
            alertDetalle = "";
        }

        for (SelectItem selectItem : getListaTipoEntrega()) {
            if (String.valueOf(selectItem.getValue()).equals(getTipoEntrega_ID())) {
                entregaItem = selectItem;
            }
        }

        if (entregaItem.getLabel().trim().equals("--SELECCIONE--")) {
            alertDetalle = "Seleccione el tipo de entrega";
            return true;
        } else {
            alertDetalle = "";
        }

        return false;

    }
//---------------------------------------------------------------------------------------------------------------------------------------------------

    //SETS Y GETS DE LOS ATRIBUTOS DEL BEAN------------------------------------------------------------------
    public LinkedList<Producto> getListaProductosCatalogo() {
        return listaProductosCatalogo;
    }

    public void setListaProductosCatalogo(LinkedList<Producto> listaProductosCatalogo) {
        this.listaProductosCatalogo = listaProductosCatalogo;
    }

    public String getAlertTabla() {
        return alertTabla;
    }

    public void setAlertTabla(String alertTabla) {
        this.alertTabla = alertTabla;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public LinkedList<Producto> getCarrito() {
        return carrito;
    }

    public void setCarrito(LinkedList<Producto> carrito) {
        this.carrito = carrito;
    }

    public Producto getProductoSeleccionadoCarrito() {
        return productoSeleccionadoCarrito;
    }

    public void setProductoSeleccionadoCarrito(Producto productoSeleccionadoCarrito) {
        this.productoSeleccionadoCarrito = productoSeleccionadoCarrito;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Transaccion getPedido() {
        return pedido;
    }

    public void setPedido(Transaccion pedido) {
        this.pedido = pedido;
    }

    public String getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(String fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Direccion getDireccionSeleccionada() {
        return direccionSeleccionada;
    }

    public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
    }

    public String getHorarioSeleccionado() {
        return horarioSeleccionado;
    }

    public void setHorarioSeleccionado(String horarioSeleccionado) {
        this.horarioSeleccionado = horarioSeleccionado;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getTipoEntrega_ID() {

        return tipoEntrega_ID;
    }

    public void setTipoEntrega_ID(String tipoEntrega_ID) {

        this.tipoEntrega_ID = tipoEntrega_ID;
    }

    public String getAlertCarrito() {
        return alertCarrito;
    }

    public void setAlertCarrito(String alertCarrito) {
        this.alertCarrito = alertCarrito;
    }

    public String getAlertDetalle() {
        return alertDetalle;
    }

    public void setAlertDetalle(String alertDetalle) {
        this.alertDetalle = alertDetalle;
    }

    public LinkedList<SelectItem> getListaTipoEntrega() {
        listaTipoEntrega = new LinkedList();
        listaTipoEntrega.add(new SelectItem(0, "--SELECCIONE--"));
        listaTipoEntrega.add(new SelectItem(1, TIPO_ENTREGA.DIRECTO.toString()));
        listaTipoEntrega.add(new SelectItem(2, TIPO_ENTREGA.ENCOMIENDA.toString()));
        listaTipoEntrega.add(new SelectItem(3, TIPO_ENTREGA.SIN_ENVIO.toString()));

        return listaTipoEntrega;
    }

    public LinkedList<Direccion> getListaDirecciones() {
        listaDirecciones = new LinkedList();
        for (Direccion direccion : cliente.getPosDirecciones()) {
            listaDirecciones.add(direccion);
        }
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public LinkedList<SelectItem> getListaHorario() {
        listaHorario = new LinkedList();
        listaHorario.add(new SelectItem(0, "--SELECCIONE--"));
        int i = 1;
        for (HORARIO horario : cliente.getHorariosEntrega()) {
            listaHorario.add(new SelectItem(i, horario.toString()));
            i++;
        }
        return listaHorario;
    }
}

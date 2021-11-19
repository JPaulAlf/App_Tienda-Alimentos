/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelGestor;

import DAO.AccesoDatos;
import DAO.SNMPExceptions;
import Model.CATEGORIA;
import Model.Cliente;
import Model.ESTADO_PEDIDO;
import Model.HORARIO;
import Model.Producto;
import Model.TIPO_ENTREGA;
import Model.Transaccion;

import ModelDB.ProductoDB;
import ModelDB.TransaccionDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author juand
 */
public class Logica_Transaccion {

    //
    //
    //Realizar compra
    public void insertarEncabezadoTransaccion(Transaccion pedido) throws ClassNotFoundException, SQLException {
        TransaccionDB factura = new TransaccionDB();
        factura.insertarEncabezadoTransaccion(pedido);
    }

    public void insertarDetalleTransaccion(Producto producto) throws ClassNotFoundException, SQLException {
         TransaccionDB factura = new TransaccionDB();
        factura.insertarDetalleTransaccion(producto);
    }

    //
    //
    //Cliente: VER TRANSACCIONES REALIZADAS
    public List<Transaccion> obtieneLista_Cliente_Transacciones(Cliente pCliente) throws SNMPExceptions, SQLException {
        TransaccionDB tDB = new TransaccionDB();
        return tDB.obtieneLista_Cliente_Transacciones(pCliente);
    }

    //
    //
    //Listas de mantenimiento: Facturacion y Despacho con sus respectivas acciones
    public List<Transaccion> obtieneLista_Mantenimiento_Facturacion() throws SNMPExceptions, SQLException {
        TransaccionDB tDB = new TransaccionDB();
        return tDB.obtieneLista_Mantenimiento_Facturacion();
    }

    public void apruebaPedido_Facturacion(Transaccion pTransaccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        TransaccionDB tDB = new TransaccionDB();
        tDB.apruebaPedido_Facturacion(pTransaccion);
    }

    public List<Transaccion> obtieneLista_Mantenimiento_Despacho() throws SNMPExceptions, SQLException {
        TransaccionDB tDB = new TransaccionDB();
        return tDB.obtieneLista_Mantenimiento_Despacho();
    }

    public void apruebaPedido_Despacho(Transaccion pTransaccion, String pFecha, String pHora) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        TransaccionDB tDB = new TransaccionDB();
        tDB.apruebaPedido_Despacho(pTransaccion, pFecha, pHora);
    }

    public void updateStock(Producto producto) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ProductoDB productoDB = new ProductoDB();
        productoDB.updateStock(producto);

    }

    public List<Transaccion> obtieneLista_Mantenimiento_Finalizado() throws SNMPExceptions, SQLException {
        TransaccionDB tDB = new TransaccionDB();
        return tDB.obtieneLista_Mantenimiento_Finalizado();
    }

    public List<Transaccion> obtieneLista_Mantenimiento_Reportes() throws SNMPExceptions, SQLException {
        TransaccionDB tDB = new TransaccionDB();
        return tDB.obtieneLista_Mantenimiento_Reportes();
    }
}

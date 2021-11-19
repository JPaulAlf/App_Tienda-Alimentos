/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDB;

import DAO.AccesoDatos;
import DAO.SNMPExceptions;
import Model.CATEGORIA;
import Model.Cliente;
import Model.ESTADO_PEDIDO;
import Model.HORARIO;
import Model.Producto;
import Model.TIPO_ENTREGA;
import Model.Transaccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.naming.NamingException;

/**
 *
 * @author John P. Alfaro
 */
public class TransaccionDB {

    private AccesoDatos accesoDatos = new AccesoDatos();
    private Connection conn;

    public TransaccionDB(Connection conn) {
        accesoDatos = new AccesoDatos();
        accesoDatos.setDbConn(conn);
    }

    public TransaccionDB() {
        super();
    }

    //
    //
    // Obtiene los productos de cada transaccion efectuada
    public List<Producto> obtieneLista_Transacciones_Productos(Transaccion pTransaccion) throws SNMPExceptions, SQLException {
        String select = "";
        List<Producto> listaProductos = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_TRANSACCIONES_PRODUCTOS] " + Integer.parseInt(pTransaccion.getIdTransaccion()) + "";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Producto producto = new Producto();
                producto.setIdProducto(rsPA.getString("Identificacion"));
                producto.setNombre(rsPA.getString("Nombre"));
                producto.setFoto(rsPA.getBytes("Fotografia"));
                producto.setPrecio(rsPA.getFloat("Precio"));
                producto.setStock(rsPA.getInt("Stock"));
                producto.setCategoria((rsPA.getInt("IDCategoria") == 1) ? CATEGORIA.CONGELADO
                        : (rsPA.getInt("IDCategoria") == 2) ? CATEGORIA.CONSERVA : CATEGORIA.ENLATADO);

                producto.setTotalCantComprada(rsPA.getFloat("PrecioTotal"));
                producto.setCantComprada(rsPA.getInt("Cantidad"));

                listaProductos.add(producto);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaProductos;
    }

    //
    //
    //Cliente: VER TRANSACCIONES REALIZADAS
    public List<Transaccion> obtieneLista_Cliente_Transacciones(Cliente pCliente) throws SNMPExceptions, SQLException {
        String select = "";
        List<Transaccion> listaTransacciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_CLIENTES_TRANSACCIONES] '" + pCliente.getIdentificacion() + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Transaccion trans = new Transaccion();
                trans.setIdTransaccion(Integer.toString(rsPA.getInt("ID")));
                trans.setNumeroDespacho(Integer.toString(rsPA.getInt("IDDespacho")));
                trans.setCliente(pCliente);

                LocalDate fechaBD = LocalDate.parse(rsPA.getString("FechaEstimada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                trans.setFecEstimada(fechaBD.atTime(LocalTime.MIN));

                trans.setHorarioEntrega((rsPA.getString("Horario") == "Mañana") ? HORARIO.MANNANA
                        : (rsPA.getString("Horario") == "Tarde") ? HORARIO.TARDE : HORARIO.NOCHE);
                trans.setDireccionEntrega(rsPA.getString("Direccion"));

                trans.setListaProductos(obtieneLista_Transacciones_Productos(trans));

                if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 0 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PROCESO);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 1) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.FINALIZADO);
                }

                trans.setTipoEntrega((rsPA.getString("TipoEntrega") == "Encomienda") ? TIPO_ENTREGA.ENCOMIENDA
                        : (rsPA.getString("TipoEntrega") == "Directo") ? TIPO_ENTREGA.DIRECTO : TIPO_ENTREGA.SIN_ENVIO);

                trans.setEsEfectivo(rsPA.getInt("esEfectivo") == 1 ? true : false);

                trans.setDescuentoMonto(rsPA.getFloat("Descuento"));
                trans.setImpuestoMonto(rsPA.getFloat("IVA"));
                trans.setSubtotal(rsPA.getFloat("Subtotal"));
                trans.setTotal(rsPA.getFloat("Total"));

                listaTransacciones.add(trans);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaTransacciones;
    }

    //
    //
    //Listas de mantenimiento: Facturacion, Despacho y Reportes 
    public List<Transaccion> obtieneLista_Mantenimiento_Facturacion() throws SNMPExceptions, SQLException {
        String select = "";
        List<Transaccion> listaTransacciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_MANTENIMIENTO_FACTURACION] ";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Transaccion trans = new Transaccion();
                trans.setIdTransaccion(Integer.toString(rsPA.getInt("ID")));
                trans.setNumeroDespacho(Integer.toString(rsPA.getInt("IDDespacho")));

                LocalDate fechaBD = LocalDate.parse(rsPA.getString("FechaEstimada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                trans.setFecEstimada(fechaBD.atTime(LocalTime.MIN));

                trans.setHorarioEntrega((rsPA.getString("Horario") == "Mañana") ? HORARIO.MANNANA
                        : (rsPA.getString("Horario") == "Tarde") ? HORARIO.TARDE : HORARIO.NOCHE);
                trans.setDireccionEntrega(rsPA.getString("Direccion"));

                trans.setListaProductos(obtieneLista_Transacciones_Productos(trans));

                if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 0 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PROCESO);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 1) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.FINALIZADO);
                }

                trans.setTipoEntrega((rsPA.getString("TipoEntrega") == "Encomienda") ? TIPO_ENTREGA.ENCOMIENDA
                        : (rsPA.getString("TipoEntrega") == "Directo") ? TIPO_ENTREGA.DIRECTO : TIPO_ENTREGA.SIN_ENVIO);

                trans.setEsEfectivo(rsPA.getInt("esEfectivo") == 1 ? true : false);

                trans.setDescuentoMonto(rsPA.getFloat("Descuento"));
                trans.setImpuestoMonto(rsPA.getFloat("IVA"));
                trans.setSubtotal(rsPA.getFloat("Subtotal"));
                trans.setTotal(rsPA.getFloat("Total"));

                listaTransacciones.add(trans);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaTransacciones;
    }

    public void apruebaPedido_Facturacion(Transaccion pTransaccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";

        try {
            System.out.println(pTransaccion.getIdTransaccion());

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_APROBAR_MANTENIMIENTO_FACTURACION] ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setInt(1, Integer.parseInt(pTransaccion.getIdTransaccion()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public List<Transaccion> obtieneLista_Mantenimiento_Despacho() throws SNMPExceptions, SQLException {
        String select = "";
        List<Transaccion> listaTransacciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_MANTENIMIENTO_DESPACHO] ";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Transaccion trans = new Transaccion();
                trans.setIdTransaccion(Integer.toString(rsPA.getInt("ID")));
                trans.setNumeroDespacho(Integer.toString(rsPA.getInt("IDDespacho")));

                LocalDate fechaBD = LocalDate.parse(rsPA.getString("FechaEstimada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                trans.setFecEstimada(fechaBD.atTime(LocalTime.MIN));

                trans.setHorarioEntrega((rsPA.getString("Horario") == "Mañana") ? HORARIO.MANNANA
                        : (rsPA.getString("Horario") == "Tarde") ? HORARIO.TARDE : HORARIO.NOCHE);
                trans.setDireccionEntrega(rsPA.getString("Direccion"));

                trans.setListaProductos(obtieneLista_Transacciones_Productos(trans));

                if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 0 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PROCESO);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 1) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.FINALIZADO);
                }

                trans.setTipoEntrega((rsPA.getString("TipoEntrega") == "Encomienda") ? TIPO_ENTREGA.ENCOMIENDA
                        : (rsPA.getString("TipoEntrega") == "Directo") ? TIPO_ENTREGA.DIRECTO : TIPO_ENTREGA.SIN_ENVIO);

                trans.setEsEfectivo(rsPA.getInt("esEfectivo") == 1 ? true : false);

                trans.setDescuentoMonto(rsPA.getFloat("Descuento"));
                trans.setImpuestoMonto(rsPA.getFloat("IVA"));
                trans.setSubtotal(rsPA.getFloat("Subtotal"));
                trans.setTotal(rsPA.getFloat("Total"));

                listaTransacciones.add(trans);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaTransacciones;
    }

    public void apruebaPedido_Despacho(Transaccion pTransaccion, String pFecha, String pHora) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";

        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_APROBAR_MANTENIMIENTO_DESPACHO] ?, ?, ?, ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setInt(1, Integer.parseInt(pTransaccion.getIdTransaccion()));
            ps.setInt(2, Integer.parseInt(pTransaccion.getNumeroDespacho()));
            ps.setString(3, pFecha);
            ps.setString(4, pHora);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public List<Transaccion> obtieneLista_Mantenimiento_Finalizado() throws SNMPExceptions, SQLException {
        String select = "";
        List<Transaccion> listaTransacciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_MANTENIMIENTO_FINALIZADO] ";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Transaccion trans = new Transaccion();
                trans.setIdTransaccion(Integer.toString(rsPA.getInt("ID")));
                trans.setNumeroDespacho(Integer.toString(rsPA.getInt("IDDespacho")));

                LocalDate fechaBD = LocalDate.parse(rsPA.getString("FechaEstimada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                trans.setFecEstimada(fechaBD.atTime(LocalTime.MIN));

                trans.setHorarioEntrega((rsPA.getString("Horario") == "Mañana") ? HORARIO.MANNANA
                        : (rsPA.getString("Horario") == "Tarde") ? HORARIO.TARDE : HORARIO.NOCHE);
                trans.setDireccionEntrega(rsPA.getString("Direccion"));

                trans.setListaProductos(obtieneLista_Transacciones_Productos(trans));

                if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 0 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PROCESO);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 1) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.FINALIZADO);
                }

                trans.setTipoEntrega((rsPA.getString("TipoEntrega") == "Encomienda") ? TIPO_ENTREGA.ENCOMIENDA
                        : (rsPA.getString("TipoEntrega") == "Directo") ? TIPO_ENTREGA.DIRECTO : TIPO_ENTREGA.SIN_ENVIO);

                trans.setEsEfectivo(rsPA.getInt("esEfectivo") == 1 ? true : false);

                trans.setDescuentoMonto(rsPA.getFloat("Descuento"));
                trans.setImpuestoMonto(rsPA.getFloat("IVA"));
                trans.setSubtotal(rsPA.getFloat("Subtotal"));
                trans.setTotal(rsPA.getFloat("Total"));

                listaTransacciones.add(trans);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaTransacciones;
    }

    public List<Transaccion> obtieneLista_Mantenimiento_Reportes() throws SNMPExceptions, SQLException {
        String select = "";
        List<Transaccion> listaTransacciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_MANTENIMIENTO_REPORTES] ";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {

                Transaccion trans = new Transaccion();
                trans.setIdTransaccion(Integer.toString(rsPA.getInt("ID")));
                trans.setNumeroDespacho(Integer.toString(rsPA.getInt("IDDespacho")));

                LocalDate fechaBD = LocalDate.parse(rsPA.getString("FechaEstimada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                trans.setFecEstimada(fechaBD.atTime(LocalTime.MIN));

                trans.setHorarioEntrega((rsPA.getString("Horario") == "Mañana") ? HORARIO.MANNANA
                        : (rsPA.getString("Horario") == "Tarde") ? HORARIO.TARDE : HORARIO.NOCHE);
                trans.setDireccionEntrega(rsPA.getString("Direccion"));

                trans.setListaProductos(obtieneLista_Transacciones_Productos(trans));

                if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 0 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 0) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.PROCESO);
                } else if (rsPA.getInt("esPendiente") == 1 && rsPA.getInt("esProceso") == 1 && rsPA.getInt("esFinalizado") == 1) {
                    trans.setEstadoPedido(ESTADO_PEDIDO.FINALIZADO);
                }

                trans.setTipoEntrega((rsPA.getString("TipoEntrega") == "Encomienda") ? TIPO_ENTREGA.ENCOMIENDA
                        : (rsPA.getString("TipoEntrega") == "Directo") ? TIPO_ENTREGA.DIRECTO : TIPO_ENTREGA.SIN_ENVIO);

                trans.setEsEfectivo(rsPA.getInt("esEfectivo") == 1 ? true : false);

                trans.setDescuentoMonto(rsPA.getFloat("Descuento"));
                trans.setImpuestoMonto(rsPA.getFloat("IVA"));
                trans.setSubtotal(rsPA.getFloat("Subtotal"));
                trans.setTotal(rsPA.getFloat("Total"));

                listaTransacciones.add(trans);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaTransacciones;
    }

    //
    //
    // Metodos para realizar una compra!
    public int validarInicioSesionCliente(String correo, String contrasenna) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_TRANSACCION_INICIO_SESION '" + correo + "','" + contrasenna + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;

    }

    public void insertarEncabezadoTransaccion(Transaccion pedido) throws ClassNotFoundException, SQLException {
        String select = "";

        AccesoDatos accesoDatos = new AccesoDatos();

        select = "[dbo].[PA_INSERTAR_ENCABEZADO_TRANSACCION] ?,?,?,?,?,?,?,?,?,?,?,?,?";
        PreparedStatement ps = accesoDatos.getPS(select);
        ps.setString(1, pedido.getCliente().getIdentificacion());
        ps.setString(2, pedido.getDireccionEntrega());
        ps.setString(3, pedido.getHorarioEntrega().toString());
        ps.setString(4, pedido.getFecEstimada_Mascara());
        ps.setInt(5, pedido.isEsEfectivo() ? 1 : 0);
        ps.setFloat(6, (float) pedido.getImpuestoMonto());
        ps.setFloat(7, (float) pedido.getDescuentoMonto());
        ps.setFloat(8, (float) pedido.getSubtotal());
        ps.setFloat(9, (float) pedido.getTotal());
        ps.setInt(10, 1);
        ps.setInt(11, 0);
        ps.setInt(12, 0);
        ps.setString(13, pedido.getTipoEntrega().toString());
        ps.execute();

    }

    public void insertarDetalleTransaccion(Producto producto) throws ClassNotFoundException, SQLException {
        String select = "";

        AccesoDatos accesoDatos = new AccesoDatos();

        select = "[dbo].[PA_INSERTAR_DETALLE_TRANSACCION] ?,?,?";
        PreparedStatement ps = accesoDatos.getPS(select);
        ps.setString(1, producto.getIdProducto());
        ps.setInt(2, producto.getCantComprada());
        ps.setFloat(3, (float) producto.getTotalCantComprada());

        ps.execute();

    }

}

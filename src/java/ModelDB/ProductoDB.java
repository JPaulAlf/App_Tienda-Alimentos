/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDB;

import DAO.AccesoDatos;
import DAO.SNMPExceptions;
import Model.CATEGORIA;
import Model.Producto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Base64;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.persistence.Convert;

/**
 *
 * @author John P. Alfaro
 */
public class ProductoDB {

    private AccesoDatos accesoDatos = new AccesoDatos();
    private Connection conn;

    public ProductoDB(Connection conn) {
        accesoDatos = new AccesoDatos();
        accesoDatos.setDbConn(conn);
    }

    public ProductoDB() {
        super();
    }

    public LinkedList<Producto> getProductos() throws SNMPExceptions, SQLException {
        String select = "";
        LinkedList<Producto> listaProducto = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_PRODUCTOS]";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rsPA.getString("Identificacion"));
                producto.setNombre(rsPA.getString("Nombre"));
                producto.setFoto(rsPA.getBytes("Fotografia"));
                producto.getImagenDisplay();
                producto.setPrecio(rsPA.getFloat("Precio"));
                producto.setStock(rsPA.getInt("Stock"));
                producto.setCategoria((rsPA.getInt("IDCategoria") == 1) ? CATEGORIA.CONGELADO : (rsPA.getInt("IDCategoria") == 2)
                        ? CATEGORIA.CONSERVA : CATEGORIA.ENLATADO);

                listaProducto.add(producto);
                System.out.println("______________________"+producto.getIdProducto()+"_________________________");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaProducto;
    }

    public LinkedList<Producto> getProductosMantenimiento() throws NamingException, ClassNotFoundException, SNMPExceptions {
        String select = "";
        LinkedList<Producto> listaProducto = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_PRODUCTOS_MANTENIMIENTO]";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rsPA.getString("Identificacion"));
                producto.setNombre(rsPA.getString("Nombre"));
                producto.setFoto(rsPA.getBytes("Fotografia"));
                producto.getImagenDisplay();
                producto.setPrecio(rsPA.getFloat("Precio"));
                producto.setStock(rsPA.getInt("Stock"));
                producto.setCategoria((rsPA.getInt("IDCategoria") == 1) ? CATEGORIA.CONGELADO : (rsPA.getInt("IDCategoria") == 2)
                        ? CATEGORIA.CONSERVA : CATEGORIA.ENLATADO);
                producto.setActivo(rsPA.getInt("Activo/Inactivo"));

                listaProducto.add(producto);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaProducto;
    }

    public void insertProducto(Producto producto) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        LinkedList<Producto> listaProducto = new LinkedList<>();

        AccesoDatos accesoDatos = new AccesoDatos();
        int categoria = 0;
        if (producto.getCategoria().equals(CATEGORIA.CONGELADO)) {
            categoria = 1;
        } else {
            if (producto.getCategoria().equals(CATEGORIA.CONSERVA)) {
                categoria = 2;
            } else {
                categoria = 3;

            }

        }
        select = "[dbo].[PA_INSERTAR_PRODUCTO] ?, ?, ?,?,?,?,?";
        PreparedStatement ps = accesoDatos.getPS(select);
        ps.setString(1, producto.getIdProducto());
        ps.setString(2, producto.getNombre());
        ps.setBytes(3, producto.getFoto());
        ps.setFloat(4, (float) producto.getPrecio());
        ps.setInt(5, producto.getStock());
        ps.setInt(6, categoria);
        ps.setInt(7, 1);
      
        ps.execute();

    }

    public int validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_PRODUCTO_IDENTIFICACION  ?";

            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pIdentificacion);

            ResultSet rsPA = ps.executeQuery();
            while (rsPA.next()) {

                numeroRespuesta = rsPA.getInt("Numero");
            }
            ps.close();
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    public int deleteProducto(String pIdentificacion) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_ELIMINAR_PRODUCTO  ?";

            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pIdentificacion);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    public void updateProducto(Producto viejo, Producto nuevo) throws ClassNotFoundException, SQLException {
        String select = "";
        LinkedList<Producto> listaProducto = new LinkedList<>();

        AccesoDatos accesoDatos = new AccesoDatos();
        int categoria = 0;
        if (nuevo.getCategoria().equals(CATEGORIA.CONGELADO)) {
            categoria = 1;
        } else {
            if (nuevo.getCategoria().equals(CATEGORIA.CONSERVA)) {
                categoria = 2;
            } else {
                categoria = 3;

            }

        }
        select = "[dbo].[PA_MODIFICAR_PRODUCTO] ?, ?, ?, ?, ? ,?  ,?,?";
        PreparedStatement ps = accesoDatos.getPS(select);
        ps.setString(1, nuevo.getIdProducto());
        ps.setString(2, nuevo.getNombre());
        ps.setBytes(3, nuevo.getFoto());
        ps.setFloat(4, (float) nuevo.getPrecio());
        ps.setInt(5, nuevo.getStock());
        ps.setInt(6, categoria);
        ps.setInt(7, nuevo.getActivo());
        ps.setString(8, viejo.getIdProducto());
        ps.execute();

    }
 public void updateStock(Producto producto) throws ClassNotFoundException, SQLException {
        String select = "";
        LinkedList<Producto> listaProducto = new LinkedList<>();

        AccesoDatos accesoDatos = new AccesoDatos();
     
        select = "[dbo].[PA_MODIFICAR_STOCK] ?,?";
        PreparedStatement ps = accesoDatos.getPS(select);
        ps.setString(1, producto.getIdProducto());
        ps.setInt(2, producto.getStock()-producto.getCantComprada());
       
        ps.execute();

    }
    public int habilitarProducto(String pIdentificacion) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_HABILITAR_PRODUCTO  ?";

            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pIdentificacion);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }
}

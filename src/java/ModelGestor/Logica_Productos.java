/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelGestor;

import DAO.SNMPExceptions;
import Model.Direccion;
import Model.Producto;
import ModelDB.ProductoDB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author juand
 */
public class Logica_Productos {

    private LinkedList<Producto> listaProductos = new LinkedList();

    public Logica_Productos() {
    }

    public LinkedList<Producto> getListaProductos() throws SNMPExceptions, SQLException {

        ProductoDB productoDB = new ProductoDB();
        listaProductos = new LinkedList();
        listaProductos = productoDB.getProductos();
        return listaProductos;

    }
    public LinkedList<Producto> getListaProductosMantenimiento() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {

        ProductoDB productoDB = new ProductoDB();
        listaProductos = new LinkedList();
        listaProductos = productoDB.getProductosMantenimiento();
        return listaProductos;

    }

    public void insertProducto(Producto producto) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ProductoDB productoDB = new ProductoDB();
        productoDB.insertProducto(producto);

    }
    public void updateProducto(Producto viejo, Producto nuevo) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ProductoDB productoDB = new ProductoDB();
        productoDB.updateProducto(viejo, nuevo);

    }
    
    

      public void deleteProducto(String pIdentificacion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ProductoDB productoDB = new ProductoDB();
        productoDB.deleteProducto(pIdentificacion);

    }
       public void habilitarProducto(String pIdentificacion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ProductoDB productoDB = new ProductoDB();
        productoDB.habilitarProducto(pIdentificacion);

    }

    public boolean validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {

        ProductoDB productoDB = new ProductoDB();

     

        return productoDB.validaExistencia_Identificacion(pIdentificacion) == 1;
    }
}

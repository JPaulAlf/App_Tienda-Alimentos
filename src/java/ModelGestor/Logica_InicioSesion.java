/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelGestor;

import DAO.SNMPExceptions;
import Model.Cliente;
import ModelDB.ClienteDB;
import ModelDB.TransaccionDB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.naming.NamingException;

/**
 *
 * @author John P. Alfaro
 */
public class Logica_InicioSesion {

    public Logica_InicioSesion() {
    }

    public boolean validaInicioSesion_Administrador(String pCorreoElectronico, String pContrasenia) throws SNMPExceptions, SQLException {
        ClienteDB cDB = new ClienteDB();
        return cDB.validaInicioSesion_Administrador(pCorreoElectronico, pContrasenia) == 1;
    }

    public boolean validarInicioSesionCliente(String correo, String contrasenna) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        TransaccionDB facturaDB = new TransaccionDB();
        return facturaDB.validarInicioSesionCliente(correo, contrasenna) == 1;

    }

    public Cliente recuperarCliente(String correo, String contrasenna) throws SNMPExceptions, SQLException {
        ClienteDB clienteDB = new ClienteDB();
        LinkedList<Cliente> lista = (LinkedList<Cliente>) clienteDB.obtieneLista_Clientes();
        for (Cliente cliente : lista) {
            if (cliente.getCorreo().equals(correo) && cliente.getContrasenia().equals(contrasenna)) {
                return cliente;
            }
        }
        return null;
    }
}
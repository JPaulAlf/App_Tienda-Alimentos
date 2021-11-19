/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;
import Model.Cliente;
import ModelGestor.Logica_Form_Clientes;
import ModelGestor.Logica_InicioSesion;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.context.FacesContext;

/**
 *
 * @author Admin
 */
@Named(value = "Bean_MantenimientoInicioSesion")
@SessionScoped
public class Bean_General_InicioSesion implements Serializable {

    Logica_InicioSesion log = new Logica_InicioSesion();
    private String contraseniaUsuario = "";
    private String correoElectronico = "";
    public static Cliente clienteInicioSesion = null;
    private String errBanndera = "";

    /**
     * Creates a new instance of Bean_InicioSesion
     */
    public Bean_General_InicioSesion() {
    }

    public String getContraseniaUsuario() {
        return contraseniaUsuario;
    }

    public void setContraseniaUsuario(String contraseniaUsuario) {
        this.contraseniaUsuario = contraseniaUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getErrBanndera() {
        return errBanndera;
    }

    public void setErrBanndera(String errBanndera) {
        this.errBanndera = errBanndera;
    }

    public void validarInicioSesion() throws SNMPExceptions, SQLException {
        try {

            Logica_Form_Clientes logCl = new Logica_Form_Clientes();
            int contadorExistenciaUsuario = 0;
            for (Cliente cl : logCl.obtieneListaCliente()) {

                if (cl.getContrasenia().equals(this.getContraseniaUsuario()) && cl.getCorreo().equals(this.getCorreoElectronico()) && cl.isAprobado()) {

                    contadorExistenciaUsuario = 1;
                    this.setErrBanndera("");
                    clienteInicioSesion = cl;                    
                    
                    
                    
                    FacesContext.getCurrentInstance().getExternalContext().redirect("perfil-bienvenida.xhtml");

                } else if (cl.getContrasenia().equals(this.getContraseniaUsuario()) && cl.getCorreo().equals(this.getCorreoElectronico()) && cl.isDenegado() == false && cl.isAprobado() == false) {

                    contadorExistenciaUsuario = 1;
                    this.setErrBanndera("El usuario aun no es procesado por un encargado. Gracias");

                } else if (cl.getContrasenia().equals(this.getContraseniaUsuario()) && cl.getCorreo().equals(this.getCorreoElectronico()) && cl.isDenegado()) {

                    contadorExistenciaUsuario = 1;
                    this.setErrBanndera("El usuario ha sido denegado por un encargado. Gracias");

                }
            }

            if (log.validaInicioSesion_Administrador(correoElectronico, contraseniaUsuario)) {

                contadorExistenciaUsuario = 1;
                this.setErrBanndera("");
                FacesContext.getCurrentInstance().getExternalContext().redirect("mantenimiento-bienvenida.xhtml");
                clienteInicioSesion = null;
            }

            if (contadorExistenciaUsuario == 0) {
                this.setErrBanndera("El usuario ingresado no se ha encontrado, favor revisar los datos sumistrados. Gracias");
            }

            contadorExistenciaUsuario = 0;

        } catch (Exception e) {
        }
    }
}

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
import ModelGestor.Logica_Transaccion;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;

/**
 *
 * @author juand
 */
@Named(value = "bean_Cliente_Productos_InicioSesion")
@SessionScoped
public class Bean_Cliente_Productos_InicioSesion implements Serializable {

    private String correo = "";
    private String contrasenna = "";
    private String alert = "";

    /**
     * Creates a new instance of Bean_Cliente_Productos_InicioSesion
     */
    public Bean_Cliente_Productos_InicioSesion() {
    }

    public void validarInicioSesion() throws SNMPExceptions, IOException {
        Logica_InicioSesion lnCliente = new Logica_InicioSesion();
        try {
            if (!validarNulos()) {
                if (correo.equals("admin") && contrasenna.equals("admin")) {
                    alert = "No se puede ingresar como admin";
                    return;
                }
                alert = "";
                Logica_Form_Clientes logCl = new Logica_Form_Clientes();
                if (lnCliente.validarInicioSesionCliente(correo, contrasenna)) {
                    for (Cliente cliente : logCl.obtieneListaCliente()) {
                        if (cliente.getCorreo().equals(correo) && cliente.getContrasenia().equals(contrasenna) && cliente.isActivo()) {
                             if (cliente.isAprobado()) {
                            Bean_Cliente_Productos beanProductos = new Bean_Cliente_Productos();

                            beanProductos.setCliente(cliente);

                            FacesContext.getCurrentInstance().getExternalContext().redirect("cliente-productos-detalle.xhtml");
                        } else {
                            if (!cliente.isAprobado() && !cliente.isDenegado()) {
                                alert = "El usuario aun no es procesado por un encargado. Gracias";
                            } else {
                                if (cliente.isDenegado()) {
                                    alert = "El usuario ha sido denegado por un encargado. Gracias";
                                }

                            }

                        }
                        }
                       
                    }

                } else {
                    alert = "El usuario ingresado no se ha encontrado, favor revisar los datos sumistrados. Gracias";
                }
            }

        } catch (Exception e) {
            alert = "Ocurrió un error en DB";
        }

    }

    public boolean validarNulos() {
        if (correo.trim().equals("")) {
            alert = "Ingrese su correo";
            return true;
        }
        if (contrasenna.trim().equals("")) {
            alert = "Ingrese su contraseña";
            return true;
        }
        return false;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

}

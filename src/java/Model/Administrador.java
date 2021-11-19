/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Administrador con capacidad de realizar crud del sistema
 *
 * @author juand
 */
public class Administrador {

    private String correo, contrasenna;

    /**
     * Constructor con parámetros
     *
     * @param correo
     * @param contrasenna
     */
    public Administrador(String correo, String contrasenna) {
        this.correo = correo;
        this.contrasenna = contrasenna;
    }

    /**
     * Constructor sin parámetros
     */
    public Administrador() {
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

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Dirección dada por el Cliente
 *
 * @author juand
 *
 */
public class Direccion {

    private String provincia, canton, distrito, barrio, sennas, estado;
    private int idNumDireccion,idProvincia, idCanton, idDistrito, idBarrio;
    private boolean activo;

    /**
     * Constructor con parámetros
     *
     * @param provincia
     * @param canton
     * @param distrito
     * @param barrio
     * @param sennas
     */
    public Direccion(String provincia, String canton, String distrito, String barrio, String sennas) {
        this.provincia = provincia;
        this.canton = canton;
        this.distrito = distrito;
        this.barrio = barrio;
        this.sennas = sennas;
    }

    /**
     * Constructor sin Parámetros
     */
    public Direccion() {
    }

    //__________________________________________________________________________________
    //__________________________________________________________________________________
    public String esActivo() {
        return this.activo ? "Está activo" : "Está inactivo";
    }

    //__________________________________________________________________________________
    //__________________________________________________________________________________
    @Override
    public String toString() {
        return sennas; //To change body of generated methods, choose Tools | Templates.
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getSennas() {
        return sennas;
    }

    public void setSennas(String sennas) {
        this.sennas = sennas;
    }

    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public int getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(int idCanton) {
        this.idCanton = idCanton;
    }

    public int getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        this.idDistrito = idDistrito;
    }

    public int getIdBarrio() {
        return idBarrio;
    }

    public void setIdBarrio(int idBarrio) {
        this.idBarrio = idBarrio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdNumDireccion() {
        return idNumDireccion;
    }

    public void setIdNumDireccion(int idNumDireccion) {
        this.idNumDireccion = idNumDireccion;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase Cliente
 *
 * @author Admin,juand
 */
public class Cliente implements Serializable {

    private Direccion direccionPrincipal;
    private List<Direccion> posDirecciones;
    private String identificacion, nombre, telefono, correo, contrasenia;
    private List<HORARIO> horariosEntrega;
    private Tarjeta tarjetaCredito;
    private boolean aprobado, activo, denegado;

    /**
     * Constructor con parámetros
     *
     * @param direccionPrincipal
     * @param posDirecciones
     * @param identificacion
     * @param telefono
     * @param correo
     * @param horariosEntrega
     * @param aprobado
     */
    public Cliente(Direccion direccionPrincipal, List<Direccion> posDirecciones, String identificacion, String telefono, String correo, List<HORARIO> horariosEntrega, boolean aprobado) {
        this.direccionPrincipal = direccionPrincipal;
        this.posDirecciones = posDirecciones;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
        this.horariosEntrega = horariosEntrega;
        this.aprobado = aprobado;

    }

    /**
     * Constructor sin parámetros
     */
    public Cliente() {
        posDirecciones = new LinkedList();
        horariosEntrega = new LinkedList();
    }

    //__________________________________________________________________________________
    //__________________________________________________________________________________
    public String esActivo() {
        return this.activo ? "Está activo" : "Está inactivo";
    }

    public String esAprobado() {
        return this.aprobado ? "Está aprobado" : "- - - -";
    }

    public String esDenegado() {
        return this.denegado ? "Está denegado" : "- - - -";
    }

    //__________________________________________________________________________________
    //__________________________________________________________________________________
    public Direccion getDireccionPrincipal() {
        return direccionPrincipal;
    }

    public void setDireccionPrincipal(Direccion direccionPrincipal) {
        this.direccionPrincipal = direccionPrincipal;
    }

    public List<Direccion> getPosDirecciones() {
        return posDirecciones;
    }

    public void setPosDirecciones(List<Direccion> posDirecciones) {
        this.posDirecciones = posDirecciones;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<HORARIO> getHoraEntregaReq() {
        return horariosEntrega;
    }

    public void setHoraEntregaReq(List<HORARIO> horarioEntrega) {
        this.horariosEntrega = horarioEntrega;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public List<HORARIO> getHorariosEntrega() {
        return horariosEntrega;
    }

    public void setHorariosEntrega(List<HORARIO> horariosEntrega) {
        this.horariosEntrega = horariosEntrega;
    }

    public Tarjeta getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(Tarjeta tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isDenegado() {
        return denegado;
    }

    public void setDenegado(boolean denegado) {
        this.denegado = denegado;
    }

}

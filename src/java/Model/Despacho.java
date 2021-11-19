/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author juand
 */
public class Despacho {

    private String idDespacho;

    private Transaccion transaccion;
    private String fechaEnvio;
    private String horaEnvio;
    private Direccion direccionEnvio;
    private TIPO_ENTREGA tipoEntrega;

    public Despacho(String idDespacho, Transaccion transaccion, String fechaEnvio, String horaEnvio, Direccion direccionEnvio, TIPO_ENTREGA tipoEntrega) {
        this.idDespacho = idDespacho;
        this.transaccion = transaccion;
        this.fechaEnvio = fechaEnvio;
        this.horaEnvio = horaEnvio;
        this.direccionEnvio = direccionEnvio;
        this.tipoEntrega = tipoEntrega;
    }

    public Despacho() {
    }

    public String getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(String idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Transaccion getFactura() {
        return transaccion;
    }

    public void setFactura(Transaccion factura) {
        this.transaccion = factura;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getHoraEnvio() {
        return horaEnvio;
    }

    public void setHoraEnvio(String horaEnvio) {
        this.horaEnvio = horaEnvio;
    }

    public Direccion getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(Direccion direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public TIPO_ENTREGA getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TIPO_ENTREGA tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

}

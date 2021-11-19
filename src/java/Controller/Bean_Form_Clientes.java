/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;
import Model.Direccion;
import ModelGestor.Logica_Form_Clientes;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author John P. Alfaro
 */
@Named(value = "bean_FormInicio")
@SessionScoped
public class Bean_Form_Clientes implements Serializable {

    //INSTANCIA PARA LA LOGICA
    //** SE "RESETA" LA INSTANCIA, UNICAMENTE DESPUES DE REGISTRAR UN CLIENTE
    //VER METODO DE "registrarCliente()"
    //SI SE TIENE DUDAS
    private Logica_Form_Clientes log = new Logica_Form_Clientes();

    private String etiquetaErr = "";
    private String etiquetaDirec = "Registradas: 0";

    // DATOS GENERALES DEL USUARIO "CLIENTE"
    private String nombre = "";
    private String identificacion = "";
    private String telefono = "";
    private String correoElectronico = "";
    private String contrasenia = "";

    private String numeroTarjeta = "";
    private String numeroCVV = "";
    private String tipoTarjeta = "";
    private String vencimientoTarjeta = "";

    //LO ENCARGADO DE LOS COMBO-BOX
    private String direccionSenias = "";
    private LinkedList<SelectItem> listaProvincia = new LinkedList<>();
    private String idProvincia = "0";
    private String cantProvincia = "0";
    private LinkedList<SelectItem> listaCanton = new LinkedList<>();
    private String idCanton = "0";
    private String cantCanton = "0";
    private LinkedList<SelectItem> listaDistrito = new LinkedList<>();
    private String idDistrito = "0";
    private String cantDistrito = "0";
    private LinkedList<SelectItem> listaBarrio = new LinkedList<>();
    private String idBarrio = "0";
    private String cantBarrio = "0";

    //ACTIVA/DESACTIVA LOS COMBO-BOX
    private String estadoProvincia = "false";
    private String estadoCanton = "true";
    private String estadoDistrito = "true";
    private String estadoBarrio = "true";

    // HORARIOS DE ENTREGA SELECCIONADOS
    private List<String> horarioEntrega = new ArrayList<String>();

    /**
     * Creates a new instance of Bean_FormInicio
     */
    public Bean_Form_Clientes() {
    }
//
//
//
//
//--------------------------------------------------------------------------
    // LISTAS DE LOS COMBO-BOX DEL MANTENIMIENTO  
//--------------------------------------------------------------------------

    public LinkedList<SelectItem> getListaProvincia() throws SNMPExceptions, SQLException {
        return log.getListaProvincia();
    }

    public LinkedList<SelectItem> getListaCanton() throws SNMPExceptions, SQLException {
        return log.getListaCanton(Integer.parseInt(getIdProvincia()));
    }

    public LinkedList<SelectItem> getListaDistrito() throws SNMPExceptions, SQLException {
        return log.getListaDistrito(Integer.parseInt(getIdProvincia()), Integer.parseInt(getIdCanton()));
    }

    public LinkedList<SelectItem> getListaBarrio() throws SNMPExceptions, SQLException {
        return log.getListaBarrio(Integer.parseInt(getIdProvincia()), Integer.parseInt(getIdCanton()), Integer.parseInt(getIdDistrito()));
    }
//--------------------------------------------------------------------------
    // **FIN** LISTAS DE LOS COMBO-BOX DEL FORM DEL INICIO
//--------------------------------------------------------------------------
//
//
//
//

//--------------------------------------------------------------------------------
    // SETEA EL ESTADO DEL COMBO
//--------------------------------------------------------------------------------
    public String getEstadoProvincia() {
        return estadoProvincia;
    }

    public void setEstadoProvincia(String estadoProvincia) {
        this.estadoProvincia = estadoProvincia;
    }

    public String getEstadoCanton() throws SNMPExceptions, SQLException {
        SelectItem provinciaItem = getListaProvincia().get(Integer.parseInt(idProvincia));

        if (provinciaItem.getLabel() != "- SELECCIONAR -") {
            this.estadoCanton = "false";
        } else {
            this.estadoCanton = "true";
        }
        return estadoCanton;
    }

    public String getEstadoDistrito() throws SNMPExceptions, SQLException {
        SelectItem provinciaItem = getListaProvincia().get(Integer.parseInt(idProvincia));
        SelectItem cantonItem = getListaCanton().get(Integer.parseInt(idCanton));

        if (provinciaItem.getLabel() != "- SELECCIONAR -" && cantonItem.getLabel() != "- SELECCIONAR -") {
            this.estadoDistrito = "false";
        } else {
            this.estadoDistrito = "true";
        }
        return estadoDistrito;
    }

    public String getEstadoBarrio() throws SNMPExceptions, SQLException {
        SelectItem provinciaItem = getListaProvincia().get(Integer.parseInt(idProvincia));
        SelectItem cantonItem = getListaCanton().get(Integer.parseInt(idCanton));
        SelectItem distritoItem = getListaDistrito().get(Integer.parseInt(idDistrito));

        if (provinciaItem.getLabel() != "- SELECCIONAR -" && cantonItem.getLabel() != "- SELECCIONAR -"
                && distritoItem.getLabel() != "- SELECCIONAR -") {
            this.estadoBarrio = "false";
        } else {
            this.estadoBarrio = "true";
        }
        return estadoBarrio;
    }

    public void setEstadoCanton(String estadoCanton) {
        this.estadoCanton = estadoCanton;
    }

    public void setEstadoDistrito(String estadoDistrito) {
        this.estadoDistrito = estadoDistrito;
    }

    public void setEstadoBarrio(String estadoBarrio) {
        this.estadoBarrio = estadoBarrio;
    }

//
//
//
//
//--------------------------------------------------------------------------------
    // SETEA LA CANTIDAD DE ITEMS POR COMBO
//--------------------------------------------------------------------------------
    public String getCantProvincia() throws SNMPExceptions, SQLException {
        int cant = 0;
        for (SelectItem selectItem : getListaProvincia()) {
            cant++;
        }
        this.cantProvincia = "Cantidad: " + Integer.toString(cant - 1);
        return cantProvincia;
    }

    public String getCantCanton() throws SNMPExceptions, SQLException {
        int cant = 0;
        for (SelectItem selectItem : getListaCanton()) {
            cant++;
        }
        this.cantCanton = "Cantidad: " + Integer.toString(cant - 1);
        return cantCanton;
    }

    public String getCantDistrito() throws SNMPExceptions, SQLException {
        int cant = 0;
        for (SelectItem selectItem : getListaDistrito()) {
            cant++;
        }
        this.cantDistrito = "Cantidad: " + Integer.toString(cant - 1);
        return cantDistrito;
    }

    public String getCantBarrio() throws SNMPExceptions, SQLException {
        int cant = 0;
        for (SelectItem selectItem : getListaBarrio()) {
            cant++;
        }
        this.cantBarrio = "Cantidad: " + Integer.toString(cant - 1);
        return cantBarrio;
    }

//
//
//
//
//--------------------------------------------------------------------------
    // METODOS NECESARIOS PARA UN FUNCIONAMIENTO CORRECTO DE LA PAGINA
//--------------------------------------------------------------------------
    public boolean validaCamposNulos() {
        List<String> listaValoresForm = new ArrayList<String>();
        listaValoresForm.add(getNombre());
        listaValoresForm.add(getIdentificacion());
        listaValoresForm.add(getTelefono());
        listaValoresForm.add(getCorreoElectronico());
        listaValoresForm.add(getContrasenia());
        listaValoresForm.add(getDireccionSenias());

        listaValoresForm.add(getNumeroTarjeta());
        listaValoresForm.add(getNumeroCVV());
        listaValoresForm.add(getTipoTarjeta());
        listaValoresForm.add(getVencimientoTarjeta());

        return log.validaCamposNulos(listaValoresForm);
    }

    public boolean validaCorreoElectronico() {
        return log.validaCorreoElectronico(this.getCorreoElectronico());
    }

    public boolean validaTarjetaCredito() {
        List<String> listaValoresForm = new ArrayList<String>();

        listaValoresForm.add(getNumeroTarjeta().replaceAll("-", ""));
        listaValoresForm.add(getNumeroCVV().replaceAll("-", ""));
        listaValoresForm.add(getTipoTarjeta());
        listaValoresForm.add(getVencimientoTarjeta());

        return log.validaTarjeta(listaValoresForm);
    }

    public boolean validaTipoTarjeta() {
        return this.getTipoTarjeta().equals("-");
    }

    public boolean validaExistenciaDireccion() {
        return log.validaExistenciaDireccion();
    }

    public boolean validaExistenciaHorario() {
        return log.validaExistenciaHorario(this.horarioEntrega);
    }

    public boolean validaSeleccionDireccion() throws SNMPExceptions, SQLException {
        List<String> listaValoresCombos = new ArrayList<String>();
        SelectItem provinciaItem = new SelectItem();
        SelectItem cantonItem = new SelectItem();
        SelectItem distritoItem = new SelectItem();
        SelectItem barrioItem = new SelectItem();

        // HACER FOR .... RECORRER LA LISTA UNA A UNA! Y EXTRAER EL ELEMENTO
        for (SelectItem selectItem : getListaProvincia()) {
            if (String.valueOf(selectItem.getValue()).equals(idProvincia)) {
                provinciaItem = selectItem;
            }
        }
        for (SelectItem selectItem : getListaCanton()) {
            if (String.valueOf(selectItem.getValue()).equals(idCanton)) {
                cantonItem = selectItem;
            }
        }
        for (SelectItem selectItem : getListaDistrito()) {
            if (String.valueOf(selectItem.getValue()).equals(idDistrito)) {
                distritoItem = selectItem;
            }
        }
        for (SelectItem selectItem : getListaBarrio()) {
            if (String.valueOf(selectItem.getValue()).equals(idBarrio)) {
                barrioItem = selectItem;
            }
        }

        listaValoresCombos.add(provinciaItem.getLabel());
        listaValoresCombos.add(cantonItem.getLabel());
        listaValoresCombos.add(distritoItem.getLabel());
        listaValoresCombos.add(barrioItem.getLabel());
        listaValoresCombos.add(this.getDireccionSenias());

        return log.validaSeleccionDireccion(listaValoresCombos);
    }

    public boolean validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {
        return log.validaExistencia_Identificacion(pIdentificacion);
    }

    public boolean validaExistencia_CorreoElectronico(String pCorreoElectronico) throws SNMPExceptions, SQLException {
        return log.validaExistencia_CorreoElectronico(pCorreoElectronico);
    }

    public boolean validaExistencia_NumeroTarjeta(String pNumTarjeta) throws SNMPExceptions, SQLException {
        return log.validaExistencia_NumeroTarjeta(pNumTarjeta);
    }

    public void agregaDireccion() throws SNMPExceptions, SQLException {
        if (!validaSeleccionDireccion()) {
            this.setEtiquetaErr("");

            SelectItem provinciaItem = new SelectItem();
            SelectItem cantonItem = new SelectItem();
            SelectItem distritoItem = new SelectItem();
            SelectItem barrioItem = new SelectItem();

            // HACER FOR .... RECORRER LA LISTA UNA A UNA! Y EXTRAER EL ELEMENTO
            for (SelectItem selectItem : getListaProvincia()) {
                if (String.valueOf(selectItem.getValue()).equals(idProvincia)) {
                    provinciaItem = selectItem;
                }
            }
            for (SelectItem selectItem : getListaCanton()) {
                if (String.valueOf(selectItem.getValue()).equals(idCanton)) {
                    cantonItem = selectItem;
                }
            }
            for (SelectItem selectItem : getListaDistrito()) {
                if (String.valueOf(selectItem.getValue()).equals(idDistrito)) {
                    distritoItem = selectItem;
                }
            }
            for (SelectItem selectItem : getListaBarrio()) {
                if (String.valueOf(selectItem.getValue()).equals(idBarrio)) {
                    barrioItem = selectItem;
                }
            }

            Direccion direccionCliente = new Direccion();

            direccionCliente.setProvincia(provinciaItem.getLabel());
            direccionCliente.setIdProvincia(Integer.parseInt(this.getIdProvincia()));

            direccionCliente.setCanton(cantonItem.getLabel());
            direccionCliente.setIdCanton(Integer.parseInt(this.getIdCanton()));

            direccionCliente.setDistrito(distritoItem.getLabel());
            direccionCliente.setIdDistrito(Integer.parseInt(this.getIdDistrito()));

            direccionCliente.setBarrio(barrioItem.getLabel());
            direccionCliente.setIdBarrio(Integer.parseInt(this.getIdBarrio()));

            direccionCliente.setSennas(this.getDireccionSenias());

            log.agregaDireccion(direccionCliente);

            this.setEtiquetaDirec("Registradas: " + log.cantidadDireccionGuardadas());

        } else {
            this.setEtiquetaErr("Ingresar una direccion correcta");
        }
    }

    public void registrarCliente() throws SNMPExceptions, SQLException {

        if (!validaCamposNulos()) {
            this.setEtiquetaErr("");
            if (!validaExistencia_Identificacion(getIdentificacion())) {
                this.setEtiquetaErr("");
                if (validaCorreoElectronico()) {
                    this.setEtiquetaErr("");
                    if (!validaExistencia_CorreoElectronico(getCorreoElectronico())) {
                        this.setEtiquetaErr("");
                        if (!validaTipoTarjeta()) {
                            this.setEtiquetaErr("");
                            if (!validaTarjetaCredito()) {
                                this.setEtiquetaErr("");
                                if (!validaExistencia_NumeroTarjeta(getNumeroTarjeta())) {
                                    this.setEtiquetaErr("");
                                    if (!validaExistenciaDireccion()) {
                                        this.setEtiquetaErr("");
                                        if (!validaExistenciaHorario()) {
                                            this.setEtiquetaErr("");

                                            List<String> listaValoresCliente = new ArrayList<String>();

                                            listaValoresCliente.add(getIdentificacion());
                                            listaValoresCliente.add(getNombre());
                                            listaValoresCliente.add(getTelefono());

                                            listaValoresCliente.add(getNumeroTarjeta().replaceAll("-", ""));
                                            listaValoresCliente.add(getNumeroCVV().replaceAll("-", ""));
                                            listaValoresCliente.add(getTipoTarjeta());
                                            listaValoresCliente.add(getVencimientoTarjeta());

                                            listaValoresCliente.add(getCorreoElectronico());
                                            listaValoresCliente.add(getContrasenia());

                                            log.registrarCliente(listaValoresCliente, getHorarioEntrega());
                                            this.limpiaCampos();
                                            this.setEtiquetaErr("Usuario registrado correctamente!!!");

                                        } else {
                                            this.setEtiquetaErr("Seleccionar almenos un horario");
                                        }
                                    } else {
                                        this.setEtiquetaErr("Ingresar almenos una direccion");
                                    }
                                } else {
                                    this.setEtiquetaErr("Tarjeta de credito ya existente");
                                }
                            } else {
                                this.setEtiquetaErr("Tarjeta de credito invalida");
                            }
                        } else {
                            this.setEtiquetaErr("Seleccionar un tipo de Tarjeta");
                        }
                    } else {
                        this.setEtiquetaErr("Correo electronico ya existente");
                    }
                } else {
                    this.setEtiquetaErr("Correo electronico invalido");
                }
            } else {
                this.setEtiquetaErr("La identificacion ya existe");
            }
        } else {
            this.setEtiquetaErr("Se encontraron campos nulos/vacios");
        }

    }

    public void limpiaCampos() {

        this.setNombre("");
        this.setIdentificacion("");
        this.setTelefono("");
        this.setCorreoElectronico("");
        this.setContrasenia("");

        this.setNumeroTarjeta("");
        this.setNumeroCVV("");
        this.setTipoTarjeta("-");
        this.setVencimientoTarjeta("");

        this.setIdProvincia("0");
        this.setIdCanton("0");
        this.setEstadoCanton("true");
        this.setIdDistrito("0");
        this.setEstadoDistrito("true");
        this.setIdBarrio("0");
        this.setEstadoBarrio("true");
        this.setEtiquetaDirec("Registradas: 0");
        this.setDireccionSenias("");

        this.horarioEntrega.clear();

        log = new Logica_Form_Clientes();

    }

//
//
//
//
//================================================
//FUNCIONES PARA ACTIVAR/DESACTIVAR
//================================================
    public String estadoCantonFuncion() throws SNMPExceptions, SQLException {

        if (Integer.parseInt(this.idProvincia) > 0) {
            this.estadoCanton = "false";
            this.getListaCanton();
        } else {
            this.setEstadoCanton("true");
            this.setIdCanton("0");
            this.listaCanton.clear();
            this.getListaCanton();

            this.setEstadoDistrito("true");
            this.setIdDistrito("0");
            this.listaDistrito.clear();
            this.getListaDistrito();

            this.setEstadoBarrio("true");
            this.setIdBarrio("0");
            this.listaBarrio.clear();
            this.getListaBarrio();
        }

        return estadoCanton;
    }

    public String estadoDistritoFuncion() throws SNMPExceptions, SQLException {

        if (Integer.parseInt(this.getIdProvincia()) > 0 && Integer.parseInt(this.getIdCanton()) > 0) {
            this.estadoDistrito = "false";
            this.getListaDistrito();
        } else {
            this.setEstadoDistrito("true");
            this.setIdDistrito("0");
            this.listaDistrito.clear();
            this.getListaDistrito();

            this.setEstadoBarrio("true");
            this.setIdBarrio("0");
            this.listaBarrio.clear();
            this.getListaBarrio();
        }
        return estadoDistrito;
    }

    public String estadoBarrioFuncion() throws SNMPExceptions, SQLException {

        if (Integer.parseInt(this.getIdProvincia()) > 0 && Integer.parseInt(this.getIdCanton()) > 0
                && Integer.parseInt(this.getIdDistrito()) > 0) {
            this.estadoBarrio = "false";
            this.getListaBarrio();
        } else {
            this.setEstadoBarrio("true");
            this.setIdBarrio("0");
            this.listaBarrio.clear();
            this.getListaBarrio();
        }
        return estadoBarrio;
    }

//
//
//
//
//================================================
//SETS/GETS
//================================================
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDireccionSenias() {
        return direccionSenias;
    }

    public void setDireccionSenias(String direccionSenias) {
        this.direccionSenias = direccionSenias;
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }

    public String getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(String idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getIdBarrio() {
        return idBarrio;
    }

    public void setIdBarrio(String idBarrio) {
        this.idBarrio = idBarrio;
    }

    public List<String> getHorarioEntrega() {
        return horarioEntrega;
    }

    public void setHorarioEntrega(List<String> horarioEntrega) {
        this.horarioEntrega = horarioEntrega;
    }

    public String getEtiquetaErr() {
        return etiquetaErr;
    }

    public void setEtiquetaErr(String etiquetaErr) {
        this.etiquetaErr = etiquetaErr;
    }

    public String getEtiquetaDirec() {
        return etiquetaDirec;
    }

    public void setEtiquetaDirec(String etiquetaDirec) {
        this.etiquetaDirec = etiquetaDirec;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNumeroCVV() {
        return numeroCVV;
    }

    public void setNumeroCVV(String numeroCVV) {
        this.numeroCVV = numeroCVV;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getVencimientoTarjeta() {
        return vencimientoTarjeta;
    }

    public void setVencimientoTarjeta(String vencimientoTarjeta) {
        this.vencimientoTarjeta = vencimientoTarjeta;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

}

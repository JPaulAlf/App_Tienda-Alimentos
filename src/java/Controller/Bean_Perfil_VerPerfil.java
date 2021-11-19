/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SNMPExceptions;
import Model.Cliente;
import Model.Direccion;
import Model.HORARIO;
import ModelGestor.Logica_Form_Clientes;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

/**
 *
 * @author John P. Alfaro
 */
@Named(value = "bean_Perfil_VerPerfil")
@SessionScoped
public class Bean_Perfil_VerPerfil implements Serializable {

    //INSTANCIA PARA LA LOGICA
    //** SE "RESETA" LA INSTANCIA, UNICAMENTE DESPUES DE REGISTRAR UN CLIENTE
    //VER METODO DE "registrarCliente()"
    //SI SE TIENE DUDAS
    private Logica_Form_Clientes log = new Logica_Form_Clientes();

    private String etiquetaErr = "";
    private String etiquetaErr_Direcciones = "";
    private String etiquetaBandera_Direcciones = "true";

    private String etiquetaDirec = "Registradas: 0";

    private String etiquetaAyuda = "";
    private int bandera = 0;

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

    //VARIABLES PARA LA SELECCION DE USUARIO:
    private LinkedList<Cliente> listaClientes = new LinkedList();
    private LinkedList<Cliente> listaClientesAux = new LinkedList();
    private Cliente clienteSeleccionado = null;

    private LinkedList<Direccion> listaDirecciones = new LinkedList();
    private Direccion direccionSeleccionada = null;

    private String clienteBuscado = "";

    private boolean banderaCargaClientes = true;
    private String idAntiguoUsuario = "";

    /**
     * Creates a new instance of Bean_Perfil_VerPerfil
     */
    public Bean_Perfil_VerPerfil() {
        this.inspeccionarCliente();
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
    // METODOS NECESARIOS PARA SELECCION DE USUARIO Y LLENADO DE LA LISTA DISPONIBLE
//--------------------------------------------------------------------------

    public LinkedList<Cliente> getListaClientes() throws SNMPExceptions, SQLException {
        if (banderaCargaClientes == true) {
            return (LinkedList<Cliente>) log.obtieneListaCliente();
        } else {
            return this.listaClientes;
        }
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
//
//
//
//
//--------------------------------------------------------------------------
    // METODOS NECESARIOS PARA SELECCION DE USUARIO Y LLENADO DE LA LISTA DISPONIBLE
//--------------------------------------------------------------------------

    public LinkedList<Direccion> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public Direccion getDireccionSeleccionada() {
        return direccionSeleccionada;
    }

    public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
    }

//
//
//
//
//--------------------------------------------------------------------------
    // METODOS NECESARIOS PARA UN FUNCIONAMIENTO CORRECTO DE LA PAGINA
//--------------------------------------------------------------------------
    public boolean validaCamposNulos_TOTAL() {
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

    public boolean validaCamposNulos_PARCIAL() {
        List<String> listaValoresForm = new ArrayList<String>();
        listaValoresForm.add(getNombre());
        listaValoresForm.add(getIdentificacion());
        listaValoresForm.add(getTelefono());
        listaValoresForm.add(getCorreoElectronico());
        listaValoresForm.add(getContrasenia());

        listaValoresForm.add(getNumeroTarjeta());
        listaValoresForm.add(getNumeroCVV());
        listaValoresForm.add(getTipoTarjeta());
        listaValoresForm.add(getVencimientoTarjeta());

        return log.validaCamposNulos(listaValoresForm);
    }

    public boolean validaCamposNulos_Direccion() {
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

    public boolean validaCamposNulos_Horario() {
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
            System.out.println(log.cantidadDireccionGuardadas());

        } else {
            this.setEtiquetaErr("Ingresar una direccion correcta");
        }
    }

    public void ayudaEtiqueta() {
        if (bandera == 0) {
            bandera = 1;
            this.setEtiquetaAyuda("Aca podra modificar sus datos ingresados asi como agregar nuevos");
        } else {
            bandera = 0;
            this.setEtiquetaAyuda("");
        }
    }

    //
    //
    //
    //
    //================================================
    //METODOS UTILIZADOS CUANDO SE BUSCA UN CLIENTE POR IDENTIFICACION
    //================================================
    public void llenaListaCliente_Aux() throws SNMPExceptions, SQLException {
        this.listaClientesAux.addAll(log.obtieneListaCliente());
    }

    public void llenaListaCliente_Principal() throws SNMPExceptions, SQLException {
        this.listaClientes.addAll(log.obtieneListaCliente());
    }

    //
    //
    //
    //
    //================================================
    //METODOS BASICOS CRUD CLIENTE
    //================================================
    public void modificaCliente() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {

        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        this.idAntiguoUsuario = clienteLogeado.getIdentificacion();
        this.setEtiquetaErr("");

        if (!validaCamposNulos_PARCIAL()) {
            this.setEtiquetaErr("");
            if (validaCorreoElectronico()) {
                this.setEtiquetaErr("");
                if (!validaTarjetaCredito()) {
                    this.setEtiquetaErr("");

                    if (!clienteLogeado.getIdentificacion().equals(getIdentificacion())) {
                        if (validaExistencia_Identificacion(getIdentificacion())) {
                            this.setEtiquetaErr("La identificacion ya existe");
                        }
                    }
                    this.setEtiquetaErr("");

                    if (!clienteLogeado.getCorreo().equals(getCorreoElectronico())) {
                        if (validaExistencia_CorreoElectronico(getCorreoElectronico())) {
                            this.setEtiquetaErr("Correo electronico ya existente");
                        }
                    }
                    this.setEtiquetaErr("");

                    if (!clienteLogeado.getTarjetaCredito().getNumTarjeta().equals(getNumeroTarjeta())) {
                        if (validaExistencia_NumeroTarjeta(getNumeroTarjeta())) {
                            this.setEtiquetaErr("Tarjeta de credito ya existente");
                        }
                    }
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

                    this.log.modificaCliente(listaValoresCliente, idAntiguoUsuario);

                    this.setEtiquetaErr("Usuario MODIFICADO correctamente!!!");

                } else {
                    this.setEtiquetaErr("Tarjeta de credito invalida");
                }
            } else {
                this.setEtiquetaErr("Correo electronico invalido");
            }
        } else {
            this.setEtiquetaErr("Se encontraron campos nulos/vacios");
        }
    }

    public void inspeccionarCliente() {

        // Este proviene de ser asignado al iniciar sesion
        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        System.out.println("#######################################################################");
        System.out.println(clienteLogeado.getNombre());
        System.out.println("#######################################################################");

        this.setEtiquetaErr("");
        this.setEtiquetaBandera_Direcciones("false");
        this.idAntiguoUsuario = clienteLogeado.getIdentificacion();

        this.setNombre(clienteLogeado.getNombre());
        this.setIdentificacion(clienteLogeado.getIdentificacion());
        this.setTelefono(clienteLogeado.getTelefono());
        this.setCorreoElectronico(clienteLogeado.getCorreo());
        this.setContrasenia(clienteLogeado.getContrasenia());

        this.setNumeroTarjeta(clienteLogeado.getTarjetaCredito().getNumTarjeta());
        this.setNumeroCVV(clienteLogeado.getTarjetaCredito().getCVV());
        this.setTipoTarjeta(clienteLogeado.getTarjetaCredito().getTipoTarjeta().toString());
        this.setVencimientoTarjeta(clienteLogeado.getTarjetaCredito().getMesVencimineto() + clienteLogeado.getTarjetaCredito().getAnnoVencimiento());

        this.setIdProvincia("0");
        this.setIdCanton("0");
        this.setEstadoCanton("true");
        this.setIdDistrito("0");
        this.setEstadoDistrito("true");
        this.setIdBarrio("0");
        this.setEstadoBarrio("true");
        this.setDireccionSenias("");

        log = new Logica_Form_Clientes();
        for (Direccion posDireccione : clienteLogeado.getPosDirecciones()) {
            log.agregaDireccion(posDireccione);
        }
        this.setEtiquetaDirec("Registradas: " + log.cantidadDireccionGuardadas());

        for (HORARIO item : clienteLogeado.getHorariosEntrega()) {
            this.horarioEntrega.add(item.toString());
        }
        
        this.listaDirecciones = new LinkedList<>();
        this.listaDirecciones.clear();
                
        if (this.listaDirecciones.isEmpty()) {
            this.listaDirecciones = (LinkedList<Direccion>) clienteLogeado.getPosDirecciones();
        } else {
            this.listaDirecciones.clear();
            this.listaDirecciones = (LinkedList<Direccion>) clienteLogeado.getPosDirecciones();
        }

    }

    public void limpiaCampos() throws SNMPExceptions, SQLException {
        this.banderaCargaClientes = true;

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
        this.listaDirecciones.clear();

        this.listaClientes.clear();
        this.llenaListaCliente_Principal();
        this.setClienteBuscado("");

        this.direccionSeleccionada = null;

        this.setEtiquetaErr("");
        this.setEtiquetaErr_Direcciones("");
        this.setEtiquetaBandera_Direcciones("true");
        this.idAntiguoUsuario = "";

        log = new Logica_Form_Clientes();

    }
    //
    //================================================
    //METODOS BASICOS CRUD DIRECCION
    //================================================

    public void registrarDireccion() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        // Este proviene de ser asignado al iniciar sesion
        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        if (!(clienteLogeado.getPosDirecciones().size() == log.cantidadDireccionGuardadas())) {
            this.setEtiquetaErr_Direcciones("");

            if (!validaExistenciaDireccion()) {
                this.setEtiquetaErr_Direcciones("");

                this.log.registrarDireccion(clienteLogeado);
                this.setListaDirecciones((LinkedList<Direccion>) this.log.obtieneListaDireccion(clienteLogeado));

                // log = new Logica_Form_Clientes();
                this.setEtiquetaErr_Direcciones("Direcciones AGREGADAS correctamente!!!");

            } else {
                this.setEtiquetaErr_Direcciones("Ingresar almenos una direccion");
            }

        } else {
            this.setEtiquetaErr_Direcciones("Favor GUARDAR una nueva primero");
        }

    }

    public void modificaDireccion() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        // Este proviene de ser asignado al iniciar sesion
        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        if (direccionSeleccionada != null) {
            this.setEtiquetaErr_Direcciones("");

            if (!validaExistenciaDireccion()) {
                this.setEtiquetaErr_Direcciones("");

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

                direccionCliente.setIdNumDireccion(this.direccionSeleccionada.getIdNumDireccion());

                log.modificaDireccion(clienteLogeado, direccionCliente);
                this.setListaDirecciones((LinkedList<Direccion>) this.log.obtieneListaDireccion(clienteLogeado));

                // log = new Logica_Form_Clientes();
                this.setEtiquetaErr_Direcciones("Direccion MODIFICADA correctamente!!!");

            } else {
                this.setEtiquetaErr_Direcciones("Ingresar almenos una direccion");
            }

        } else {
            this.setEtiquetaErr_Direcciones("Favor seleccionar una direccion primero");
        }
    }

    public void cambiaEstadoDireccion() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        // Este proviene de ser asignado al iniciar sesion
        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        if (direccionSeleccionada != null) {
            this.setEtiquetaErr_Direcciones("");
            if (direccionSeleccionada.isActivo()) {
                this.log.deshabilitaDireccion(clienteLogeado, direccionSeleccionada);
                this.setListaDirecciones((LinkedList<Direccion>) this.log.obtieneListaDireccion(clienteLogeado));
            } else {
                this.log.habilitaDireccion(clienteLogeado, direccionSeleccionada);
                this.setListaDirecciones((LinkedList<Direccion>) this.log.obtieneListaDireccion(clienteLogeado));
            }
        } else {
            this.setEtiquetaErr_Direcciones("Favor seleccionar una direccion primero");
        }
    }

    public void inspeccionarDireccion() {

        if (direccionSeleccionada != null) {
            this.setEtiquetaErr_Direcciones("");
            this.setEtiquetaBandera_Direcciones("false");

            this.setIdProvincia(this.direccionSeleccionada.getIdProvincia() + "");
            this.setIdCanton(this.direccionSeleccionada.getIdCanton() + "");
            this.setEstadoCanton("false");
            this.setIdDistrito(this.direccionSeleccionada.getIdDistrito() + "");
            this.setEstadoDistrito("false");
            this.setIdBarrio(this.direccionSeleccionada.getIdBarrio() + "");
            this.setEstadoBarrio("false");
            this.setDireccionSenias(this.direccionSeleccionada.getSennas());

        } else {
            this.setEtiquetaErr_Direcciones("Favor seleccionar una direccion primero");
        }
    }

    public void modificaHorario() throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        // Este proviene de ser asignado al iniciar sesion
        Cliente clienteLogeado = Bean_General_InicioSesion.clienteInicioSesion;

        this.setEtiquetaErr("");
        if (!validaExistenciaHorario()) {
            this.setEtiquetaErr("");

            List<String> lista_V = new LinkedList<String>();
            List<String> lista_N = new LinkedList<String>();

            for (HORARIO item : clienteLogeado.getHorariosEntrega()) {
                lista_V.add(item.toString());
            }
            for (String item : this.horarioEntrega) {
                lista_N.add(item);
            }

            this.log.modificaHorario(clienteLogeado, lista_V, lista_N);

            log = new Logica_Form_Clientes();
            this.setEtiquetaErr("Horarios MODIFICADOS correctamente!!!");

        } else {
            this.setEtiquetaErr("Seleccionar almenos un horario");
        }

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
    //SETS Y GETS
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

    public String getEtiquetaAyuda() {
        return etiquetaAyuda;
    }

    public void setEtiquetaAyuda(String etiquetaAyuda) {
        this.etiquetaAyuda = etiquetaAyuda;
    }

    public String getClienteBuscado() {
        return clienteBuscado;
    }

    public void setClienteBuscado(String clienteBuscado) {
        this.clienteBuscado = clienteBuscado;
    }

    public String getEtiquetaErr_Direcciones() {
        return etiquetaErr_Direcciones;
    }

    public void setEtiquetaErr_Direcciones(String etiquetaErr_Direcciones) {
        this.etiquetaErr_Direcciones = etiquetaErr_Direcciones;
    }

    public String getEtiquetaBandera_Direcciones() {
        return etiquetaBandera_Direcciones;
    }

    public void setEtiquetaBandera_Direcciones(String etiquetaBandera_Direcciones) {
        this.etiquetaBandera_Direcciones = etiquetaBandera_Direcciones;
    }

}

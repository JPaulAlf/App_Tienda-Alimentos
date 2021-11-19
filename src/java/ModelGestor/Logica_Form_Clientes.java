/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelGestor;

import DAO.SNMPExceptions;
import Model.Cliente;
import Model.Correo;
import Model.Direccion;
import Model.HORARIO;
import Model.TIPO_TARJETA;
import Model.Tarjeta;
import ModelDB.ClienteDB;
import ModelDB.UbicacionesDB;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

/**
 *
 * @author John P. Alfaro
 */
public class Logica_Form_Clientes {

    // LISTA DE DIRECCIONES DEL CLIENTE A REGISTRARSE
    private List<Direccion> listaDireccion = new ArrayList<Direccion>();

    //LO ENCARGADO DE LOS COMBO-BOX
    private LinkedList<SelectItem> listaProvincia = new LinkedList<>();
    private LinkedList<SelectItem> listaCanton = new LinkedList<>();
    private LinkedList<SelectItem> listaDistrito = new LinkedList<>();
    private LinkedList<SelectItem> listaBarrio = new LinkedList<>();
//
//
//
//
//--------------------------------------------------------------------------
    // LISTAS DE LOS COMBO-BOX DEL FORM DEL INICIO
//--------------------------------------------------------------------------

    public LinkedList<SelectItem> getListaProvincia() throws SNMPExceptions, SQLException {

        LinkedList resultList = new LinkedList();
        resultList.add(new SelectItem(0, "- SELECCIONAR -"));

        UbicacionesDB ub = new UbicacionesDB();
        for (SelectItem selectItem : ub.recuperaProvincia()) {
            resultList.add(selectItem);
        }

        return resultList;
    }

    public LinkedList<SelectItem> getListaCanton(int pIdProvincia) throws SNMPExceptions, SQLException {
        LinkedList resultList = new LinkedList();
        resultList.add(new SelectItem(0, "- SELECCIONAR -"));

        if (pIdProvincia > 0) {
            UbicacionesDB ub = new UbicacionesDB();
            for (SelectItem selectItem : ub.recuperaCanton(pIdProvincia)) {
                resultList.add(selectItem);
            }
        }
        return resultList;
    }

    public LinkedList<SelectItem> getListaDistrito(int pIdProvincia, int pIdCanton) throws SNMPExceptions, SQLException {
        LinkedList resultList = new LinkedList();
        resultList.add(new SelectItem(0, "- SELECCIONAR -"));

        if (pIdProvincia > 0 && pIdCanton > 0) {
            UbicacionesDB ub = new UbicacionesDB();
            for (SelectItem selectItem : ub.recuperaDistrito(pIdProvincia, pIdCanton)) {
                resultList.add(selectItem);
            }
        }

        return resultList;
    }

    public LinkedList<SelectItem> getListaBarrio(int pIdProvincia, int pIdCanton, int pIdDistrito) throws SNMPExceptions, SQLException {
        LinkedList resultList = new LinkedList();
        resultList.add(new SelectItem(0, "- SELECCIONAR -"));

        if (pIdProvincia > 0 && pIdCanton > 0 && pIdDistrito > 0) {
            UbicacionesDB ub = new UbicacionesDB();
            for (SelectItem selectItem : ub.recuperaBarrio(pIdProvincia, pIdCanton, pIdDistrito)) {
                resultList.add(selectItem);
            }
        }

        return resultList;
    }
//
//
//
//
//--------------------------------------------------------------------------
    // METODOS NECESARIOS PARA UN FUNCIONAMIENTO CORRECTO DE LA PAGINA INDEX.XHTML
//--------------------------------------------------------------------------

    public boolean validaTarjeta(List<String> pListaTarjeta) {
        boolean bandera = false;
        Tarjeta t = new Tarjeta();
        t.setNumTarjeta(pListaTarjeta.get(0));
        t.setCVV(pListaTarjeta.get(1));
        t.setTipoTarjeta(pListaTarjeta.get(2).equals("Visa") ? TIPO_TARJETA.VISA : TIPO_TARJETA.MASTER_CARD);
        t.setMesVencimineto(pListaTarjeta.get(3).substring(0, 2));
        t.setAnnoVencimiento(pListaTarjeta.get(3).substring(3));

        try {
            if (t.validarFechaTarjeta() == true && t.validarNumeroTarjeta() == true && t.validarTipoTarjeta() == true) {
                return bandera;
            }
        } catch (ParseException ex) {
        }
        bandera = true;
        return bandera;
    }

    public boolean validaCamposNulos(List<String> pListaValoresForm) {
        boolean bandera = false;
        for (String itemLista : pListaValoresForm) {
            if (itemLista.trim().equals("") || itemLista.trim().equals("-")) {
                bandera = true;
            }
        }
        return bandera;
    }

    public boolean validaCorreoElectronico(String pCorreoElectronico) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(pCorreoElectronico);
        return mather.find();
    }

    public boolean validaSeleccionDireccion(List<String> pListaValoresCombos) {
        boolean bandera = false;
        for (String itemLista : pListaValoresCombos) {
            if (itemLista.trim().equals("") || itemLista.trim().equals("- SELECCIONAR -")) {
                bandera = true;
            }
        }
        return bandera;
    }

    public boolean validaExistenciaDireccion() {
        return this.listaDireccion.isEmpty();
    }

    public boolean validaExistenciaHorario(List<String> pListaHorarios) {
        return pListaHorarios.isEmpty();
    }

    public boolean validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {
        ClienteDB oClienteDB = new ClienteDB();
        return oClienteDB.validaExistencia_Identificacion(pIdentificacion) == 1;
    }

    public boolean validaExistencia_CorreoElectronico(String pCorreoElectronico) throws SNMPExceptions, SQLException {
        ClienteDB oClienteDB = new ClienteDB();
        return oClienteDB.validaExistencia_CorreoElectronico(pCorreoElectronico) == 1;
    }

    public boolean validaExistencia_NumeroTarjeta(String pNumTarjeta) throws SNMPExceptions, SQLException {
        ClienteDB oClienteDB = new ClienteDB();
        return oClienteDB.validaExistencia_NumeroTarjeta(pNumTarjeta) == 1;
    }

    public int cantidadDireccionGuardadas() {
        return this.listaDireccion.size();
    }

    public void agregaDireccion(Direccion pDireccion) {
        this.listaDireccion.add(pDireccion);
    }

//
//
//
//
//--------------------------------------------------------------------------
    // METODOS CRUD CLIENTE
//--------------------------------------------------------------------------
    public List<Cliente> obtieneListaCliente() throws SNMPExceptions, SQLException {
        ClienteDB oClienteDB = new ClienteDB();
        return oClienteDB.obtieneLista_Clientes();
    }

    public void registrarCliente(List<String> pListaValoresCliente, List<String> pListaHorarios) {

        Cliente oCliente = new Cliente();
        oCliente.setIdentificacion(pListaValoresCliente.get(0));
        oCliente.setNombre(pListaValoresCliente.get(1));
        oCliente.setTelefono(pListaValoresCliente.get(2));

        Tarjeta oTarjeta = new Tarjeta();
        oTarjeta.setNumTarjeta(pListaValoresCliente.get(3));
        oTarjeta.setCVV(pListaValoresCliente.get(4));
        oTarjeta.setTipoTarjeta(pListaValoresCliente.get(5).equals("Visa") ? TIPO_TARJETA.VISA : TIPO_TARJETA.MASTER_CARD);
        oTarjeta.setMesVencimineto(pListaValoresCliente.get(6).substring(0, 2));
        oTarjeta.setAnnoVencimiento(pListaValoresCliente.get(6).substring(3));

        oCliente.setCorreo(pListaValoresCliente.get(7));
        oCliente.setContrasenia(pListaValoresCliente.get(8));

        oCliente.setTarjetaCredito(oTarjeta);

        List<HORARIO> listaHorario = new ArrayList<HORARIO>();
        for (String itemLista : pListaHorarios) {
            if ("Mañana".equals(itemLista)) {
                listaHorario.add(HORARIO.MANNANA);
            }
            if ("Tarde".equals(itemLista)) {
                listaHorario.add(HORARIO.TARDE);
            }
            if ("Noche".equals(itemLista)) {
                listaHorario.add(HORARIO.NOCHE);
            }
        }

        oCliente.setHoraEntregaReq(listaHorario);
        oCliente.setPosDirecciones(this.listaDireccion);
        oCliente.setDireccionPrincipal(this.listaDireccion.get(0));
        oCliente.setAprobado(false);

        //INSTANCIA DE LA CLASE DE CONEXION A LA BASE DE DATOS
        ClienteDB oClienteDB = new ClienteDB();

        try {

            //REGISTRA EL CLIENTE EN LA BASE DE DATOS
            oClienteDB.registraCliente(oCliente);

            Correo oCorreo = new Correo();
            oCorreo.enviarCorreo_UsuarioRegistrado(oCliente);

        } catch (SNMPExceptions ex) {
            Logger.getLogger(Logica_Form_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Logica_Form_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(Logica_Form_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Logica_Form_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void modificaCliente(List<String> pListaValoresCliente, String pIdentificacionVieja) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {

        Cliente oCliente = new Cliente();
        oCliente.setIdentificacion(pListaValoresCliente.get(0));
        oCliente.setNombre(pListaValoresCliente.get(1));
        oCliente.setTelefono(pListaValoresCliente.get(2));

        Tarjeta oTarjeta = new Tarjeta();
        oTarjeta.setNumTarjeta(pListaValoresCliente.get(3));
        oTarjeta.setCVV(pListaValoresCliente.get(4));
        oTarjeta.setTipoTarjeta(pListaValoresCliente.get(5).equals("Visa") ? TIPO_TARJETA.VISA : TIPO_TARJETA.MASTER_CARD);
        oTarjeta.setMesVencimineto(pListaValoresCliente.get(6).substring(0, 2));
        oTarjeta.setAnnoVencimiento(pListaValoresCliente.get(6).substring(3));

        oCliente.setCorreo(pListaValoresCliente.get(7));
        oCliente.setContrasenia(pListaValoresCliente.get(8));

        oCliente.setTarjetaCredito(oTarjeta);

        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.modificaCliente(oCliente, pIdentificacionVieja);
    }

    public void apruebaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.apruebaCliente(pCliente);

        Correo oCorreo = new Correo();
        oCorreo.enviarCorreo_UsuarioAprobado(pCliente);
    }

    public void denegaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.denegaCliente(pCliente);

        Correo oCorreo = new Correo();
        oCorreo.enviarCorreo_UsuarioDenegado(pCliente);
    }

    public void habilitaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.habilitaCliente(pCliente);
    }

    public void deshabilitaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.deshabilitaCliente(pCliente);
    }

//
//
//
//
//--------------------------------------------------------------------------
    // METODOS CRUD DIRECCION
//--------------------------------------------------------------------------
    public List<Direccion> obtieneListaDireccion(Cliente pCliente) throws SNMPExceptions, SQLException {
        ClienteDB oClienteDB = new ClienteDB();
        return oClienteDB.obtieneLista_Cliente_Direcciones(pCliente);
    }

    public void registrarDireccion(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {

        List<Direccion> listaDireccionAux = new ArrayList<Direccion>();
        for (Direccion direccion : listaDireccion.subList(pCliente.getPosDirecciones().size(), listaDireccion.size())) {
            listaDireccionAux.add(direccion);
        }

        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.registraCliente_Direccion_Extra(pCliente, listaDireccionAux);

    }

    public void modificaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.modificaDireccion(pCliente, pDireccion);
    }

    public void habilitaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.habilitaDireccion(pCliente, pDireccion);
    }

    public void deshabilitaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.deshabilitaDireccion(pCliente, pDireccion);
    }

    public void modificaHorario(Cliente pCliente, List<String> pListaHorarios_V, List<String> pListaHorarios_N) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        ClienteDB oClienteDB = new ClienteDB();
        oClienteDB.modificaHorario(pCliente, pListaHorarios_V, pListaHorarios_N);
    }

}

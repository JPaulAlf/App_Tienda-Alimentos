/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDB;

import DAO.AccesoDatos;
import DAO.SNMPExceptions;
import Model.Cliente;
import Model.Direccion;
import Model.HORARIO;
import Model.TIPO_TARJETA;
import Model.Tarjeta;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

/**
 *
 * @author Admin
 */
public class ClienteDB {

    private AccesoDatos accesoDatos = new AccesoDatos();
    private Connection conn;

    public ClienteDB(Connection conn) {
        accesoDatos = new AccesoDatos();
        accesoDatos.setDbConn(conn);
    }

    public ClienteDB() {
        super();
    }

    //=================================================
    //REGISTRO DE CLIENTES NUEVOS
    public void registraCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_INSERTAR_CLIENTE] ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.setString(2, pCliente.getNombre());
            ps.setString(3, pCliente.getTelefono());
            ps.setString(4, pCliente.getTarjetaCredito().getNumTarjeta());
            ps.setString(5, pCliente.getTarjetaCredito().getMesVencimineto() + "/" + pCliente.getTarjetaCredito().getAnnoVencimiento());
            ps.setString(6, pCliente.getTarjetaCredito().getCVV());
            ps.setString(7, pCliente.getCorreo());
            ps.setString(8, pCliente.getContrasenia());
            ps.setInt(9, 0);//Cliente no aprobado aun, hasta que "admin" lo acepte
            ps.setInt(10, pCliente.getTarjetaCredito().getTipoTarjeta() == TIPO_TARJETA.VISA ? 1 : 2);
            ps.executeUpdate();

            this.registraCliente_Direccion(pCliente);
            this.registraCliente_Horario(pCliente);

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void registraCliente_Horario(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {
            AccesoDatos accesoDatos = new AccesoDatos();

            for (HORARIO itemHorario : pCliente.getHorariosEntrega()) {

                select = "[dbo].[PA_INSERTAR_CLIENTE_HORARIO] ?, ?";
                PreparedStatement ps = accesoDatos.getPS(select);
                ps.setString(1, pCliente.getIdentificacion());
                ps.setString(2, itemHorario.toString());
                ps.executeUpdate();

            }

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void registraCliente_Direccion(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            int esPrincipal = 1;
            for (Direccion itemDireccion : pCliente.getPosDirecciones()) {

                select = "[dbo].[PA_INSERTAR_CLIENTE_DIRECCION] ?, ?, ?, ?, ?, ?, ?";
                PreparedStatement ps = accesoDatos.getPS(select);
                ps.setString(1, pCliente.getIdentificacion());
                ps.setInt(2, itemDireccion.getIdProvincia());
                ps.setInt(3, itemDireccion.getIdCanton());
                ps.setInt(4, itemDireccion.getIdDistrito());
                ps.setInt(5, itemDireccion.getIdBarrio());
                ps.setString(6, itemDireccion.getSennas());
                ps.setInt(7, esPrincipal);

                ps.executeUpdate();

                esPrincipal = 0;
            }
        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void registraCliente_Direccion_Extra(Cliente pCliente, List<Direccion> pListaAux) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            int esPrincipal = 1;
            for (Direccion itemDireccion : pListaAux) {

                select = "[dbo].[PA_INSERTAR_CLIENTE_DIRECCION] ?, ?, ?, ?, ?, ?, ?";
                PreparedStatement ps = accesoDatos.getPS(select);
                ps.setString(1, pCliente.getIdentificacion());
                ps.setInt(2, itemDireccion.getIdProvincia());
                ps.setInt(3, itemDireccion.getIdCanton());
                ps.setInt(4, itemDireccion.getIdDistrito());
                ps.setInt(5, itemDireccion.getIdBarrio());
                ps.setString(6, itemDireccion.getSennas());
                ps.setInt(7, esPrincipal);

                ps.executeUpdate();

                esPrincipal = 0;
            }
        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    //=================================================
    //VALIDA CAMPOS ESPACIALES DE LOS CLIENTES QUE DEBEN SER UNICOS 
    // DEVUELVE UN 1 SI EXSISTE, SINO UN -1 SINO EXISTE
    public int validaExistencia_Identificacion(String pIdentificacion) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_CLIENTE_IDENTIFICACION '" + pIdentificacion + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    public int validaExistencia_CorreoElectronico(String pCorreoElectronico) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_CLIENTE_CORREO_ELECTRONICO '" + pCorreoElectronico + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    public int validaExistencia_NumeroTarjeta(String pNumTarjeta) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_CLIENTE_TARJETA '" + pNumTarjeta + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    public int validaExistencia_Horario(Cliente pCliente, String pHorario) throws SNMPExceptions, SQLException {
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_CLIENTE_HORARIO '" + pCliente.getIdentificacion() + "','" + pHorario + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    //=================================================
    //VALIDA LOS INCIO DE SESION DE LOS USUARIOS
    public int validaInicioSesion_Administrador(String pCorreoElectronico, String pContrasenia) throws SNMPExceptions, SQLException {

        //CREDENCIALES DEL ADMINISTRADOR
        //     CORREO: admin
        //CONTRASENIA: admin
        String select = "";
        int numeroRespuesta = 0;

        try {
            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_VALIDAR_CLIENTE_ES_ADMINISTRADOR '" + pCorreoElectronico + "','" + pContrasenia + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);
            while (rsPA.next()) {
                numeroRespuesta = rsPA.getInt("Numero");
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return numeroRespuesta;
    }

    //=================================================
    //ACTUALIZA ESTADOS 
    public void apruebaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_APROBAR_CLIENTE] ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void denegaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_DENEGAR_CLIENTE] ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void habilitaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_HABILITAR_CLIENTE] ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void deshabilitaCliente(Cliente pCliente) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_ELIMINAR_CLIENTE] ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void habilitaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_HABILITAR_CLIENTE_DIRECCION] ? , ?";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.setInt(2, pDireccion.getIdNumDireccion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void deshabilitaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "[dbo].[PA_ELIMINAR_CLIENTE_DIRECCION] ?, ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.setInt(2, pDireccion.getIdNumDireccion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    //=================================================
    //OBTIENE LOS USUARIOS DE LA BASE DE DATOS
    public List<Cliente> obtieneLista_Clientes() throws SNMPExceptions, SQLException {
        String select = "";
        List<Cliente> listaClientes = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_CLIENTES]";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {

                Cliente cl = new Cliente();

                cl.setIdentificacion(rsPA.getString("Identificacion").trim());
                cl.setNombre(rsPA.getString("Nombre"));
                cl.setTelefono(rsPA.getString("Telefono"));
                cl.setCorreo(rsPA.getString("Correo"));
                cl.setContrasenia(rsPA.getString("Contrasenia"));

                cl.setActivo((rsPA.getInt("Activo/Inactivo") == 1));

                //"Aprobado" = 0 = es NO APROBADO
                //"Aprobado" = 1 = es APROBADO
                //"Aprobado" = 2 = es DENEGADO
                cl.setAprobado((rsPA.getInt("Aprobado") == 1));
                cl.setDenegado((rsPA.getInt("Denegado") == 1));

                Tarjeta t = new Tarjeta();
                t.setNumTarjeta(rsPA.getString("NumeroTarjeta"));
                t.setCVV(rsPA.getString("CVV"));
                t.setMesVencimineto(rsPA.getString("FechaVencimiento"));
                t.setAnnoVencimiento(rsPA.getString("FechaVencimiento"));
                t.setTipoTarjeta(rsPA.getString("CatTarje").equals("VISA") ? TIPO_TARJETA.VISA : TIPO_TARJETA.MASTER_CARD);
                cl.setTarjetaCredito(t);

                List<Direccion> lsDireccion = new ArrayList<>();
                lsDireccion = obtieneLista_Cliente_Direcciones(cl);

                List<HORARIO> lsHorario = new ArrayList<>();
                lsHorario = obtieneLista_Cliente_Horarios(cl);

                cl.setPosDirecciones(lsDireccion);
                cl.setHorariosEntrega(lsHorario);

                if (!cl.getCorreo().equals("admin")) {
                    listaClientes.add(cl);
                }

            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaClientes;
    }

    public List<HORARIO> obtieneLista_Cliente_Horarios(Cliente pCliente) throws SNMPExceptions, SQLException {
        String select = "";
        List<HORARIO> listaHorarios = new ArrayList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_CLIENTES_HORARIOS] '" + pCliente.getIdentificacion() + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                if ("Mañana".equals(rsPA.getString("Nombre"))) {
                    listaHorarios.add(HORARIO.MANNANA);

                }
                if ("Tarde".equals(rsPA.getString("Nombre"))) {
                    listaHorarios.add(HORARIO.TARDE);

                }
                if ("Noche".equals(rsPA.getString("Nombre"))) {
                    listaHorarios.add(HORARIO.NOCHE);

                }

            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaHorarios;
    }

    public List<Direccion> obtieneLista_Cliente_Direcciones(Cliente pCliente) throws SNMPExceptions, SQLException {
        String select = "";
        List<Direccion> listaDirecciones = new LinkedList();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_RECUPERAR_CLIENTES_DIRECCIONES] '" + pCliente.getIdentificacion() + "'";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                Direccion direc = new Direccion();

                direc.setProvincia(rsPA.getString("descP"));
                direc.setIdProvincia(rsPA.getInt("IDProvincia"));

                direc.setCanton(rsPA.getString("descC"));
                direc.setIdCanton(rsPA.getInt("IDCanton"));

                direc.setDistrito(rsPA.getString("descD"));
                direc.setIdDistrito(rsPA.getInt("IDDistrito"));

                direc.setBarrio(rsPA.getString("descB"));
                direc.setIdBarrio(rsPA.getInt("IDBarrio"));

                direc.setSennas(rsPA.getString("Senias"));
                direc.setIdNumDireccion(rsPA.getInt("numDirec"));
                direc.setActivo(rsPA.getInt("Activo/Inactivo") == 1);

                listaDirecciones.add(direc);
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (SNMPExceptions | ClassNotFoundException | NamingException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaDirecciones;
    }

    //=================================================
    //CRUD MODIFICA 
    public void modificaHorario(Cliente pCliente, List<String> pListaHorarios_V, List<String> pListaHorarios_N) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {
            AccesoDatos accesoDatos = new AccesoDatos();

            // AGREGA SI NO EXISTIA
            for (String itemNuevo : pListaHorarios_N) {
                if (itemNuevo.equals("Mañana")) {
                    if (!pListaHorarios_V.contains("Mañana")) {
                        if (validaExistencia_Horario(pCliente, itemNuevo) == -1) {
                            // AGREGA Y ACTIVA EL HORARIO
                            select = "[dbo].[PA_INSERTAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        } else {
                            //ACTIVA EL HORARIO QUE SE ENCONTRABA DESACTIVADO
                            select = "[dbo].[PA_HABILITAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        }
                    }
                }

                if (itemNuevo.equals("Tarde")) {
                    if (!pListaHorarios_V.contains("Tarde")) {
                        if (validaExistencia_Horario(pCliente, itemNuevo) == -1) {
                            // AGREGA Y ACTIVA EL HORARIO
                            select = "[dbo].[PA_INSERTAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        } else {
                            //ACTIVA EL HORARIO QUE SE ENCONTRABA DESACTIVADO
                            select = "[dbo].[PA_HABILITAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        }
                    }
                }

                if (itemNuevo.equals("Noche")) {
                    if (!pListaHorarios_V.contains("Noche")) {
                        if (validaExistencia_Horario(pCliente, itemNuevo) == -1) {
                            // AGREGA Y ACTIVA EL HORARIO
                            select = "[dbo].[PA_INSERTAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        } else {
                            //ACTIVA EL HORARIO QUE SE ENCONTRABA DESACTIVADO
                            select = "[dbo].[PA_HABILITAR_CLIENTE_HORARIO] ?, ?";
                            PreparedStatement ps = accesoDatos.getPS(select);
                            ps.setString(1, pCliente.getIdentificacion());
                            ps.setString(2, itemNuevo);
                            ps.executeUpdate();
                        }
                    }
                }
            }
            //---------------------------------------------------------
            // DESACTIVA SI EXISTIA
            for (String itemViejo : pListaHorarios_V) {
                if (itemViejo.equals("Mañana")) {
                    if (!pListaHorarios_N.contains("Mañana")) {
                        select = "[dbo].[PA_ELIMINAR_CLIENTE_HORARIO] ?, ?";
                        PreparedStatement ps = accesoDatos.getPS(select);
                        ps.setString(1, pCliente.getIdentificacion());
                        ps.setString(2, itemViejo);
                        ps.executeUpdate();
                    }
                }

                if (itemViejo.equals("Tarde")) {
                    if (!pListaHorarios_N.contains("Tarde")) {
                        select = "[dbo].[PA_ELIMINAR_CLIENTE_HORARIO] ?, ?";
                        PreparedStatement ps = accesoDatos.getPS(select);
                        ps.setString(1, pCliente.getIdentificacion());
                        ps.setString(2, itemViejo);
                        ps.executeUpdate();
                    }
                }

                if (itemViejo.equals("Noche")) {
                    if (!pListaHorarios_N.contains("Noche")) {
                        select = "[dbo].[PA_ELIMINAR_CLIENTE_HORARIO] ?, ?";
                        PreparedStatement ps = accesoDatos.getPS(select);
                        ps.setString(1, pCliente.getIdentificacion());
                        ps.setString(2, itemViejo);
                        ps.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void modificaCliente(Cliente pCliente, String pIdentificacionVieja) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_MODIFICAR_CLIENTE] ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.setString(2, pIdentificacionVieja);
            ps.setString(3, pCliente.getNombre());
            ps.setString(4, pCliente.getTelefono());
            ps.setString(5, pCliente.getTarjetaCredito().getNumTarjeta());
            ps.setString(6, pCliente.getTarjetaCredito().getMesVencimineto() + "/" + pCliente.getTarjetaCredito().getAnnoVencimiento());
            ps.setString(7, pCliente.getTarjetaCredito().getCVV());
            ps.setString(8, pCliente.getCorreo());
            ps.setString(9, pCliente.getContrasenia());
            ps.setInt(10, pCliente.getTarjetaCredito().getTipoTarjeta() == TIPO_TARJETA.VISA ? 1 : 2);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

    public void modificaDireccion(Cliente pCliente, Direccion pDireccion) throws SNMPExceptions, SQLException, NamingException, ClassNotFoundException {
        String select = "";
        try {

            AccesoDatos accesoDatos = new AccesoDatos();

            select = "[dbo].[PA_MODIFICAR_CLIENTE_DIRECCION] ?, ?, ?, ?, ?, ?, ?, ? ";
            PreparedStatement ps = accesoDatos.getPS(select);
            ps.setString(1, pCliente.getIdentificacion());
            ps.setInt(2, pDireccion.getIdNumDireccion());
            ps.setInt(3, pDireccion.getIdProvincia());
            ps.setInt(4, pDireccion.getIdCanton());
            ps.setInt(5, pDireccion.getIdDistrito());
            ps.setInt(6, pDireccion.getIdBarrio());
            ps.setString(7, pDireccion.getSennas());
            ps.setInt(8, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
    }

}

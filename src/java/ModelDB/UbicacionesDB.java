/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDB;

import DAO.AccesoDatos;
import DAO.SNMPExceptions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.sql.Connection;
import javax.faces.model.SelectItem;

/**
 *
 * @author John P. Alfaro
 */
public class UbicacionesDB {

    private AccesoDatos accesoDatos = new AccesoDatos();
    private Connection conn;

    public UbicacionesDB(Connection conn) {
        accesoDatos = new AccesoDatos();
        accesoDatos.setDbConn(conn);
    }

    public UbicacionesDB() {
        super();
    }

    public LinkedList<SelectItem> recuperaProvincia() throws SNMPExceptions, SQLException {
        String select = "";
        LinkedList<SelectItem> listaProvincia = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "SELECT Cod_Provincia, Dsc_Provincia FROM Provincias";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                if (rsPA.getInt("Cod_Provincia") > 0) {
                    listaProvincia.add(new SelectItem(rsPA.getInt("Cod_Provincia"), rsPA.getString("Dsc_Provincia")));

                }
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaProvincia;
    }

    public LinkedList<SelectItem> recuperaCanton(int pIdProvincia) throws SNMPExceptions, SQLException {
        String select = "";
        LinkedList<SelectItem> listaCanton = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_RECUPERA_CANTON " + Integer.toString(pIdProvincia) + "";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                if (rsPA.getInt("Cod_Canton") > 0) {
                    listaCanton.add(new SelectItem(rsPA.getInt("Cod_Canton"), rsPA.getString("DSC_Canton")));

                }
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaCanton;
    }

    public LinkedList<SelectItem> recuperaDistrito(int pIdProvincia, int pIdCanton) throws SNMPExceptions, SQLException {
        String select = "";
        LinkedList<SelectItem> listaDistrito = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_RECUPERA_DISTRITO " + Integer.toString(pIdProvincia) + "," + Integer.toString(pIdCanton) + "";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                if (rsPA.getInt("Cod_Distrito") > 0) {
                    listaDistrito.add(new SelectItem(rsPA.getInt("Cod_Distrito"), rsPA.getString("Dsc_Distrito")));

                }
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaDistrito;
    }

    public LinkedList<SelectItem> recuperaBarrio(int pIdProvincia, int pIdCanton, int pIdDistrito) throws SNMPExceptions, SQLException {
        String select = "";
        LinkedList<SelectItem> listaBarrio = new LinkedList<>();

        try {

            AccesoDatos accesoDatos = new AccesoDatos();
            select = "PA_RECUPERA_BARRIO " + Integer.toString(pIdProvincia) + "," + Integer.toString(pIdCanton) + "," + Integer.toString(pIdDistrito) + "";
            ResultSet rsPA = accesoDatos.ejecutaSQLRetornaRS(select);

            while (rsPA.next()) {
                if (rsPA.getInt("Cod_Barrio") > 0) {
                    listaBarrio.add(new SelectItem(rsPA.getInt("Cod_Barrio"), rsPA.getString("Dsc_Barrio")));

                }
            }
            rsPA.close();

        } catch (SQLException e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            throw new SNMPExceptions(SNMPExceptions.SQL_EXCEPTION, e.getMessage());
        } finally {

        }
        return listaBarrio;
    }

}

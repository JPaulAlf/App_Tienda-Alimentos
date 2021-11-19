/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author John P. Alfaro
 */
public class Tarjeta {

    private String numTarjeta;
    private String CVV;
    private String mesVencimineto;
    private String annoVencimiento;
    private TIPO_TARJETA tipoTarjeta;

    public Tarjeta() {

    }

    public Tarjeta(String numTarjeta, String CVV, String mesVencimineto, String annoVencimiento, TIPO_TARJETA tipoTarjeta) {
        this.numTarjeta = numTarjeta;
        this.CVV = CVV;
        this.mesVencimineto = mesVencimineto;
        this.annoVencimiento = annoVencimiento;
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getMesVencimineto() {
        return mesVencimineto;
    }

    public void setMesVencimineto(String mesVencimineto) {
        this.mesVencimineto = mesVencimineto;
    }

    public String getAnnoVencimiento() {
        return annoVencimiento;
    }

    public void setAnnoVencimiento(String annoVencimiento) {
        this.annoVencimiento = annoVencimiento;
    }

    public TIPO_TARJETA getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TIPO_TARJETA tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public String toString() {
        return "\n\nNumero de tarjeta: " + this.numTarjeta
                + "\nTipo de tarjeta: " + this.tipoTarjeta.toString()
                + "\nMes de vencimiento: " + this.mesVencimineto
                + "\nAño de vencimiento: " + this.annoVencimiento;
    }

    // metodos tipo *public* para que puedan ser llamados de cualquier lado del sistema...
    public boolean validarFechaTarjeta() throws ParseException {
        String fecha = mesVencimineto + "/" + annoVencimiento;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        Date caducidad = simpleDateFormat.parse(fecha);

        if (caducidad.after(new Date())) {
            return true;
        }
        return false;
    }

    public boolean validarNumeroTarjeta() {
        int s1 = 0, s2 = 0;
        String reversa = new StringBuffer(numTarjeta).reverse().toString();
        for (int i = 0; i < reversa.length(); i++) {
            int digito = Character.digit(reversa.charAt(i), 10);
            if (i % 2 == 0) {
                s1 += digito;
            } else {
                s2 += 2 * digito;
                if (digito >= 5) {
                    s2 -= 9;
                }
            }
        }
        if ((s1 + s2) % 10 == 0) {
            return true;
        }
        return false;
    }

    public boolean validarTipoTarjeta() {
        int longTarjeta = numTarjeta.length();
        int longCVV = CVV.length();
        char a, b, c;
        switch (tipoTarjeta) {
            case VISA:
                if (longTarjeta == 16 && longCVV == 3) {
                    a = numTarjeta.charAt(0);
                    if (a == '4') {
                        return true;
                    }
                }
                break;
            case MASTER_CARD:
                if (longTarjeta == 16 && longCVV == 3) {
                    b = numTarjeta.charAt(0);
                    c = numTarjeta.charAt(1);
                    if (b == '5' && c == '1' || b == '5' && c == '2' || b == '5' && c == '3' || b == '5' && c == '4' || b == '5' && c == '5') {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

}

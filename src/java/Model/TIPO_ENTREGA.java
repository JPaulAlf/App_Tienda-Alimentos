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
public enum TIPO_ENTREGA {
    DIRECTO(){
        @Override
        public String toString() {
            return "Directo" ;//To change body of generated methods, choose Tools | Templates.
        }
    },SIN_ENVIO(){
        @Override
        public String toString() {
            return "Sin envío"; //To change body of generated methods, choose Tools | Templates.
        }
    }, ENCOMIENDA(){
        @Override
        public String toString() {
            return "Encomienda"; //To change body of generated methods, choose Tools | Templates.
        }
    };
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author John P. Alfaro
 */
public enum TIPO_TARJETA {
       MASTER_CARD(){
        @Override
        public String toString() {
            return "Master Card" ;//To change body of generated methods, choose Tools | Templates.
        }
    },VISA(){
        @Override
        public String toString() {
            return "Visa"; //To change body of generated methods, choose Tools | Templates.
        }
    };
}

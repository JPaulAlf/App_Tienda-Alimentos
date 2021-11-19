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
public enum ESTADO_PEDIDO {
    PENDIENTE(){
        @Override
        public String toString() {
            return "Pendiente"; //To change body of generated methods, choose Tools | Templates.
        }
    }, PROCESO(){
        @Override
        public String toString() {
            return "Proceso"; //To change body of generated methods, choose Tools | Templates.
        }
    },FINALIZADO(){
        @Override
        public String toString() {
            return "Finalizado"; //To change body of generated methods, choose Tools | Templates.
        }
    };
}

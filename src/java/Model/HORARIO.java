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
public enum HORARIO {

    MANNANA() {
        @Override
        public String toString() {
            return "Mañana";
        }
    }, TARDE() {
        @Override
        public String toString() {
            return "Tarde";
        }
    }, NOCHE() {
        @Override
        public String toString() {
            return "Noche";
        }
    };
}

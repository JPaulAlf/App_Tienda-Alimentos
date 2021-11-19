/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Categorías de los productos hechos por la empresa de alimentos preparados
 *
 * @author juand
 */
public enum CATEGORIA {
    CONGELADO() {
        @Override
        public String toString() {
            return "Congelado";
        }

    }, ENLATADO() {
        @Override
        public String toString() {
            return "Enlatado";
        }
    }, CONSERVA() {
        @Override
        public String toString() {
            return "Conserva";
        }
    };
}

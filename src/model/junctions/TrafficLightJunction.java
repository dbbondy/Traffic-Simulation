/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import model.Lane;

/**
 *
 * @author Dan
 */
public class TrafficLightJunction extends Junction{

    public static final String name = "Traffic Light Junction";
    
    @Override
    public void distributeNewCars(int cars, int trucks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void manageJunction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    @Override
    public String toString(){
        return name;
    }
}

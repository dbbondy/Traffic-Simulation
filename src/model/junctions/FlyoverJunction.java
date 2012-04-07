/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

/**
 *
 * @author Dan
 */
public class FlyoverJunction extends Junction {
    
    public static final String name = "Flyover Junction";

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

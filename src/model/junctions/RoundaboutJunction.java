/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import model.Lane;
import model.RoadDesigner;

/**
 *
 * @author Dan
 */
public class RoundaboutJunction extends Junction{
    
    private Lane testLane;
    
    public RoundaboutJunction(){
        testLane = new Lane(400, 0, 0);
        testLane.add(RoadDesigner.buildStraight(300, testLane));
        
        registerLane(testLane);
    }
    
    

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
        return "Roundabout Junction";
    }
}

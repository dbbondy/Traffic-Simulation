/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import model.*;

/**
 *
 * @author Dan
 */
public class RoundaboutJunction extends Junction{
    
    public static final String name = "Roundabout Junction";
    
    private Lane testLane;
    
    public RoundaboutJunction(){
        testLane = new Lane(400, 0, 0, TurnDirection.RIGHT_AND_STRAIGHT);
        testLane.add(RoadDesigner.buildStraight(300, testLane));
        testLane.add(RoadDesigner.buildLargeTurn(-90, testLane, 4));
        testLane.add(RoadDesigner.buildStraight(300, testLane));
        registerLane(testLane);
        
    }

    @Override
    public String toString(){
        return name;
    }
}

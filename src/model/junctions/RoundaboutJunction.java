/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import model.*;

/**
 *
 * @author Dan
 */
public class RoundaboutJunction extends Junction{
    
    private Lane testLane;
    
    public RoundaboutJunction(){
        testLane = new Lane(400, 0, 0);
        testLane.add(RoadDesigner.buildStraight(300, testLane));
        testLane.add(RoadDesigner.buildLargeTurn(-90, testLane, 4));
        testLane.add(RoadDesigner.buildStraight(300, testLane));
        registerLane(testLane);
        
    }

    

    @Override
    public void manageJunction() {
        for(Lane l : getLanes()){
            for(Vehicle v : l.getVehicles()){
                Segment head = v.getHeadSegment();
                if (head.getNextSegment() != null) {
                    v.setHeadSegment(head.getNextSegment());
                } else {
                    v.setHeadSegment(l.getFirstSegment());
                }
            }
        }
    }

    @Override
    public String toString(){
        return "Roundabout Junction";
    }
}

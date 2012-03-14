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
        
        randomCars(testLane);
        
        
    }
    
    private void randomCars(Lane lane) {
        ArrayList<Segment> segments = lane.getLaneSegments();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int get = (segments.size() / 10) * i;
            Segment s = segments.get(get);
            if (r.nextInt(3) < 2) {
                new Car(lane, s, new Color(0, 6 * 16 + 6, 9 * 16 + 9));
            } else {
                new Truck(lane, s, new Color(9 * 16 + 9, 0, 0));
            }
        }
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

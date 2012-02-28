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
public class TwoLaneJunction extends Junction {

    public TwoLaneJunction() {        
        Lane l1 = new Lane(400, 0, 0);        
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildTurn(90, l1));
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildLargeTurn(-90, l1, 5));
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildLargeTurn(-90, l1, 5));
        l1.add(RoadDesigner.getStraight(400, l1));        
        
        Lane l2 = new Lane(400 + Segment.WIDTH + 2, 0, 0);        
        l2.add(RoadDesigner.getStraight(100 + (Segment.WIDTH - 14), l2));
        l2.add(RoadDesigner.buildLargeTurn(90, l2, 5));
        l2.add(RoadDesigner.getStraight(100, l2));
        l2.add(RoadDesigner.buildTurn(-90, l2));
        l2.add(RoadDesigner.getStraight(100 - (Segment.WIDTH - 6), l2));
        l2.add(RoadDesigner.buildTurn(-90, l2));
        l2.add(RoadDesigner.getStraight(400, l2));
        
        registerLane(l1);
        registerLane(l2);
        
        randomCars(l1);
        randomCars(l2);
    }
    
    private void randomCars(Lane lane) {
        ArrayList<Segment> segments = lane.getLaneSegments();        
        Random r = new Random();        
        for (int i = 0; i < 10; i++) {
            int get = (segments.size() / 10) * i;
            Segment s = segments.get(get);
            if (r.nextBoolean()) new Car(lane, s, Color.GREEN);
            else new Truck(lane, s, Color.RED);
        }   
    }
    
    @Override
    public void distributeNewCars(int cars, int trucks) {        
        // TODO
    }

    @Override
    public void manageJunction(){
        // TODO
    }
    
}

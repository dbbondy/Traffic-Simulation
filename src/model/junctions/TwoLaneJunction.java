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
    
    private Lane bottomRightwardsLane;
    private Lane bottomLeftwardsLane;
    private Lane rightLeftwardsLane;
    private Lane leftRightwardsLane;
    private Lane topRightwardsLane;

    public TwoLaneJunction() {     
        
        
        registerLane(bottomRightwardsLane);
        //registerLane(topRightwardsLane);
        registerLane(bottomLeftwardsLane);
        registerLane(leftRightwardsLane);
        registerLane(rightLeftwardsLane);
        
        randomCars(bottomRightwardsLane);
        randomCars(bottomLeftwardsLane);
        randomCars(leftRightwardsLane);
        randomCars(rightLeftwardsLane);
        //randomCars(topRightwardsLane);
        /*Lane l1 = new Lane(400, 0, 0);        
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildLargeTurn(90, l1, 1));
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildLargeTurn(-90, l1, 3));
        l1.add(RoadDesigner.getStraight(100, l1));
        l1.add(RoadDesigner.buildLargeTurn(-90, l1, 3));
        l1.add(RoadDesigner.getStraight(400, l1));        
        
        Lane l2 = new Lane(400 + Segment.WIDTH + 2, 0, 0);        
        l2.add(RoadDesigner.getStraight(102, l2));
        l2.add(RoadDesigner.buildLargeTurn(90, l2, 3));
        l2.add(RoadDesigner.getStraight(100, l2));
        l2.add(RoadDesigner.buildLargeTurn(-90, l2, 1));
        l2.add(RoadDesigner.getStraight(96, l2));
        l2.add(RoadDesigner.buildLargeTurn(-90, l2, 1));
        l2.add(RoadDesigner.getStraight(400, l2));
        
        registerLane(l1);
        registerLane(l2);
        
        randomCars(l1);
       randomCars(l2);*/
    }
    
    private void randomCars(Lane lane) {
        ArrayList<Segment> segments = lane.getLaneSegments();        
        Random r = new Random();        
        for (int i = 0; i < 10; i++) {
            int get = (segments.size() / 10) * i;
            Segment s = segments.get(get);
            if (r.nextInt(3) < 2) new Car(lane, s, new Color(0, 6*16+6, 9*16+9));
            else new Truck(lane, s, new Color(9*16+9, 0, 0));
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

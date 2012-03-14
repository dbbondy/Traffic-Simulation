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

    private Lane bottomUpwardsLane;
    private Lane bottomUpwardsLane2;
    private Lane topDownwardsLane;
    private Lane topDownwardsLane2;
    private Lane leftRightwardsLane;
    private Lane leftRightwardsLane2;
    private Lane rightLeftwardsLane;
    private Lane rightLeftwardsLane2;
    


    public TwoLaneJunction() {
        bottomUpwardsLane = new Lane(400, 645, 180);
        bottomUpwardsLane2 = new Lane(400 - Segment.WIDTH - 2, 645, 180);
        topDownwardsLane = new Lane((400 - (Segment.WIDTH * 2)) - 6, 0, 0);
        topDownwardsLane2 = new Lane((400 - Segment.WIDTH * 3) - 8, 0, 0);
        leftRightwardsLane = new Lane(0, 350, 270);
        leftRightwardsLane2 = new Lane(0, 350 - Segment.WIDTH - 2, 270);
        
        rightLeftwardsLane = new Lane(800, (350 - (Segment.WIDTH * 2) - 6), 90);
        rightLeftwardsLane2 = new Lane(800, (350 - (Segment.WIDTH * 3) - 8), 90);
        
        buildRoads();

        registerLane(bottomUpwardsLane);
        registerLane(bottomUpwardsLane2);
        registerLane(topDownwardsLane);
        registerLane(topDownwardsLane2);
        registerLane(leftRightwardsLane);
        registerLane(leftRightwardsLane2);
        registerLane(rightLeftwardsLane);
        registerLane(rightLeftwardsLane2);


        randomCars(bottomUpwardsLane);
        randomCars(bottomUpwardsLane2);
        randomCars(topDownwardsLane);
        randomCars(topDownwardsLane2);
        randomCars(leftRightwardsLane);
        randomCars(leftRightwardsLane2);
        randomCars(rightLeftwardsLane);
        randomCars(rightLeftwardsLane2);


        //randomCars(topRightwardsLane);
        /*
         * Lane l1 = new Lane(400, 0, 0); l1.add(RoadDesigner.getStraight(100, l1)); l1.add(RoadDesigner.buildLargeTurn(90, l1, 1)); l1.add(RoadDesigner.getStraight(100, l1));
         * l1.add(RoadDesigner.buildLargeTurn(-90, l1, 3)); l1.add(RoadDesigner.getStraight(100, l1)); l1.add(RoadDesigner.buildLargeTurn(-90, l1, 3)); l1.add(RoadDesigner.getStraight(400, l1)); *
         * Lane l2 = new Lane(400 + Segment.WIDTH + 2, 0, 0); l2.add(RoadDesigner.getStraight(102, l2)); l2.add(RoadDesigner.buildLargeTurn(90, l2, 3)); l2.add(RoadDesigner.getStraight(100, l2));
         * l2.add(RoadDesigner.buildLargeTurn(-90, l2, 1)); l2.add(RoadDesigner.getStraight(96, l2)); l2.add(RoadDesigner.buildLargeTurn(-90, l2, 1)); l2.add(RoadDesigner.getStraight(400, l2));
         *
         * registerLane(l1); registerLane(l2);
         *
         * randomCars(l1); randomCars(l2);
         */
    }
    
    
     private void buildRoads(){
       
         
        Segment[] s1 = RoadDesigner.buildStraight(685, bottomUpwardsLane);
        Segment[] s2 = RoadDesigner.buildStraight(685, bottomUpwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        bottomUpwardsLane.add(s1);
        bottomUpwardsLane2.add(s2);
        s1 = RoadDesigner.buildStraight(685, topDownwardsLane);
        s2 = RoadDesigner.buildStraight(685, topDownwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        topDownwardsLane.add(s1);
        topDownwardsLane2.add(s2);
        
        s1 = RoadDesigner.buildStraight(800, leftRightwardsLane);
        s2 = RoadDesigner.buildStraight(800, leftRightwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        leftRightwardsLane.add(s1);
        leftRightwardsLane2.add(s2);
        
        s1 = RoadDesigner.buildStraight(800, rightLeftwardsLane);
        s2 = RoadDesigner.buildStraight(800, rightLeftwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        rightLeftwardsLane.add(s1);
        rightLeftwardsLane2.add(s2);
         
       /*
         //setting initial roads before adding corner turning segments
        Segment[] s1 = RoadDesigner.buildStraight(282, bottomUpwardsLane);
        Segment[] s2 = RoadDesigner.buildStraight(282, bottomUpwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        bottomUpwardsLane.add(s1);
        bottomUpwardsLane2.add(s2);
       
        
        s1 = RoadDesigner.buildStraight(237, topDownwardsLane);
        s2 = RoadDesigner.buildStraight(237, topDownwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        topDownwardsLane.add(s1);
        topDownwardsLane2.add(s2);
        
        s1 = RoadDesigner.buildStraight(409, leftRightwardsLane);
        s2 = RoadDesigner.buildStraight(409, leftRightwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        leftRightwardsLane.add(s1);
        leftRightwardsLane2.add(s2);
        
        s1 = RoadDesigner.buildStraight(387, rightLeftwardsLane);
        s2 = RoadDesigner.buildStraight(387, rightLeftwardsLane2);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        rightLeftwardsLane.add(s1);
        rightLeftwardsLane2.add(s2);
        
         //connection from bottom lane to turn right CORRECT
        s1 = RoadDesigner.buildStraight(1, bottomUpwardsLane);
        s2 = RoadDesigner.buildStraight(1, leftRightwardsLane);
        RoadDesigner.setUpConnectionOverlap(s1, s2);
        bottomUpwardsLane.add(s1);
        leftRightwardsLane.add(s2);
       
        /*
        //connection from top lane to turn left CORRECT
        s1 = RoadDesigner.buildStraight(1, topDownwardsLane);
        s2 = RoadDesigner.buildStraight(1, rightLeftwardsLane);
        RoadDesigner.setUpConnectionsAdjacent(s1, s2);
        topDownwardsLane.add(s1);
        rightLeftwardsLane.add(s2);
        
       
        
        //connection from bottom lane to turn left
        s1 = RoadDesigner.buildStraight(1, bottomUpwardsLane2);
        s2 = RoadDesigner.buildStraight(1, leftRightwardsLane2);
        RoadDesigner.setUpConnectionOverlap(s1, s2);
        bottomUpwardsLane2.add(s1);
        leftRightwardsLane2.add(s2);
        */
        
        
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
        // TODO
    }

    @Override
    public void manageJunction() {
        // TODO
    }

    @Override
    public String toString() {
        return "Two-Lane Junction";
    }
}

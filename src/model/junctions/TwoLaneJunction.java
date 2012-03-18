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
        bottomUpwardsLane = new Lane(400, 648, 180);
        bottomUpwardsLane2 = new Lane(400 - Segment.WIDTH - 2, 648, 180);
        topDownwardsLane = new Lane((400 - (Segment.WIDTH * 2)) - 6, 0, 0);
        topDownwardsLane2 = new Lane((400 - Segment.WIDTH * 3) - 8, 0, 0);
        leftRightwardsLane = new Lane(0, 350, 270);
        leftRightwardsLane2 = new Lane(0, 350 - Segment.WIDTH - 2, 270);
        
        rightLeftwardsLane = new Lane(800, (350 - (Segment.WIDTH * 2) - 6), 90);
        rightLeftwardsLane2 = new Lane(800, (350 - (Segment.WIDTH * 3) - 8), 90);
        
        buildRoads();
        setUpIntersectionConnections();
        
        registerLane(bottomUpwardsLane);
        registerLane(bottomUpwardsLane2);
        registerLane(topDownwardsLane);
        registerLane(topDownwardsLane2);
        registerLane(leftRightwardsLane);
        registerLane(leftRightwardsLane2);
        registerLane(rightLeftwardsLane);
        registerLane(rightLeftwardsLane2);

/*
        randomCars(bottomUpwardsLane);
        randomCars(bottomUpwardsLane2);
        randomCars(topDownwardsLane);
        randomCars(topDownwardsLane2);
        randomCars(leftRightwardsLane);
        randomCars(leftRightwardsLane2);
        randomCars(rightLeftwardsLane);
        randomCars(rightLeftwardsLane2);

*/
      
    }
    
    
     private void buildRoads(){
        RoadDesigner.buildLanes(685, bottomUpwardsLane, bottomUpwardsLane2);
        RoadDesigner.buildLanes(685, topDownwardsLane, topDownwardsLane2);
        RoadDesigner.buildLanes(800, leftRightwardsLane, leftRightwardsLane2);
        RoadDesigner.buildLanes(800, rightLeftwardsLane, rightLeftwardsLane2);
        
    }
     
     private void setUpIntersectionConnections(){
         
         //bottom upwards lane intersections.
         Segment s1 = bottomUpwardsLane.getLaneSegments().get(287);
         Segment s2 = leftRightwardsLane.getLaneSegments().get(411);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         s1 = bottomUpwardsLane.getLaneSegments().get(383);
         s2 = rightLeftwardsLane2.getLaneSegments().get(389);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         s1 = bottomUpwardsLane2.getLaneSegments().get(337);
         s2 = rightLeftwardsLane.getLaneSegments().get(435);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         //top downwards lane intersections
         s1 = topDownwardsLane.getLaneSegments().get(339);
         s2 = leftRightwardsLane.getLaneSegments().get(361);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         s1 = topDownwardsLane2.getLaneSegments().get(265);
         s2 = rightLeftwardsLane2.getLaneSegments().get(484);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         //left rightwards lane intersections
         s1 = leftRightwardsLane.getLaneSegments().get(315);
         s2 = topDownwardsLane2.getLaneSegments().get(361);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         s1 = leftRightwardsLane2.getLaneSegments().get(365);
         s2 = bottomUpwardsLane2.getLaneSegments().get(333);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         //right leftwards lane intersections
         s1 = rightLeftwardsLane.getLaneSegments().get(439);
         s2 = topDownwardsLane.getLaneSegments().get(311);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
         s1 = rightLeftwardsLane2.getLaneSegments().get(389);
         s2 = topDownwardsLane2.getLaneSegments().get(383);
         RoadDesigner.setUpConnectionOverlap(s1, s2);
         
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

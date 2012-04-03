/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import model.*;
import view.SimulationPanel;

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
    private int numberOfVehicles;
    
    


    public TwoLaneJunction() {
        
        bottomUpwardsLane = new Lane(400, SimulationPanel.HEIGHT, 180);
        bottomUpwardsLane2 = new Lane(400 - Segment.WIDTH, SimulationPanel.HEIGHT, 180);
        
        topDownwardsLane = new Lane((400 - (Segment.WIDTH * 2)), 0, 0);
        topDownwardsLane2 = new Lane((400 - Segment.WIDTH * 3), 0, 0);
        
        leftRightwardsLane = new Lane(0, 350, 270);
        leftRightwardsLane2 = new Lane(0, 350 - Segment.WIDTH, 270);
        
        rightLeftwardsLane = new Lane(800, (350 - (Segment.WIDTH * 2)), 90);
        rightLeftwardsLane2 = new Lane(800, (350 - (Segment.WIDTH * 3)), 90);
        
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
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, bottomUpwardsLane, bottomUpwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, topDownwardsLane, topDownwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, leftRightwardsLane, leftRightwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, rightLeftwardsLane, rightLeftwardsLane2);
    }
     
     private void setUpIntersectionConnections(){
         
         Segment s1;
         Segment s2;
         
         int width50 = Segment.WIDTH / 2;
         
         s1 = topDownwardsLane2.getLaneSegments().get(rightLeftwardsLane2.getYStart() - width50);
         s2 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane2.getXStart() - width50));
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane.getXStart() + width50));
         s2 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane2.getYStart() - width50));
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = leftRightwardsLane.getLaneSegments().get(topDownwardsLane2.getXStart() - width50);
         s2 = topDownwardsLane2.getLaneSegments().get(leftRightwardsLane.getYStart() + width50);
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane.getYStart() + width50));
         s2 = leftRightwardsLane.getLaneSegments().get(bottomUpwardsLane.getXStart() + width50);
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane.getXStart() + width50));
         s2 = topDownwardsLane.getLaneSegments().get(rightLeftwardsLane.getYStart() + width50);
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane.getYStart() + width50));
         s2 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane2.getXStart() - width50));
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

         s1 = leftRightwardsLane2.getLaneSegments().get(bottomUpwardsLane2.getXStart() - width50);
         s2 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane2.getYStart() - width50));
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         s1 = topDownwardsLane.getLaneSegments().get(leftRightwardsLane2.getYStart() - width50);
         s2 = leftRightwardsLane2.getLaneSegments().get(topDownwardsLane.getXStart() + width50);
         s1.addConnectedSegment(s2, ConnectionType.OVERLAP);
         
         
     }
     
   
    /* // TODO: move somewhere more suitable
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
    }*/

    @Override
    public void distributeNewCars(int cars, int trucks) {
        // TODO
        int density = (int)Simulation.getOption(Simulation.DENSITY);
        if(numberOfVehicles < density){
            if(density - numberOfVehicles == density){ // if num of vehicles is 0.
                for(Lane l : getLanes()){
                    //BLAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
                }
            }else if((density - numberOfVehicles) > getLanes().size()){ // if difference between density and number of vehicles on road is greater than the number of lanes
                
            }else{ //else the difference between density value and number of vehicles on road is less than the number of lanes.
                
            }
            
        }
    }
    
    @Override
    protected Lane chooseEmptyLane(){
        
        for(Lane l : getLanes()){
            Segment firstSegment = l.getFirstSegment();
            Vehicle closestVehicleToStart = l.getVehicleAhead(firstSegment);
            Segment closestVehiclePosition = closestVehicleToStart.getHeadSegment();
            if(closestVehicleToStart.equals(firstSegment)){ //if there is a vehicle in the first segment of this lane.
                continue;
            }else if(l.getLaneSegments().indexOf(closestVehicleToStart) - 1 > closestVehicleToStart.getLength()){ //if there is no vehicle immediately in front, but is long enough to occupy some space of where the vehicle is to be positioned
                continue;
            }else{
                return l;
            }
        }
        return null;
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

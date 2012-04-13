/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Dan
 */
public class DriverAI {
 
    public static final int DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE = 100;
    
    protected Vehicle vehicle;
    protected Desire desire; // TODO: auto routing based on desired destination!
    protected Random rnd;
    protected int safeLaneChangeID; // the ID of the segment that is the last safe point of changing lanes for turning
    protected Lane vehicleLane;
    
    public DriverAI(Vehicle vehicle) {
        this.vehicle = vehicle;
        rnd = Randomizer.getInstance();
        Desire[] allDesires = Desire.values();
        desire = allDesires[rnd.nextInt(allDesires.length)];
        vehicleLane = vehicle.getLane();
    }
    
    protected void getSafeLaneChangeIndex(){
        ArrayList<Segment> laneSegments = vehicleLane.getLaneSegments();
        for(int i = 0; i < laneSegments.size(); i++){
            Map<Segment, ConnectionType> connectedSegments = laneSegments.get(i).getConnectedSegments();
            if(connectedSegments.containsValue(ConnectionType.OVERLAP)){
                safeLaneChangeID = laneSegments.get(i - DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE).id();
            }
        }
    }
    
    public void act(){
       
        
    }
    
    protected Desire getDesire(){
        return desire;
    }
    
    protected void changeLane(){
        
    }
    
    protected boolean isSafeDistanceAhead(){
        return false;
    }
    
    
    
}

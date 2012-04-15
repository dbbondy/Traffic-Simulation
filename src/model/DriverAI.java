/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
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

    protected void getSafeLaneChangeIndex() {
        ArrayList<Segment> laneSegments = vehicleLane.getLaneSegments();
        for (int i = 0; i < laneSegments.size(); i++) {
            Map<Segment, ConnectionType> connectedSegments = laneSegments.get(i).getConnectedSegments();
            if (connectedSegments.containsValue(ConnectionType.OVERLAP)) {
                safeLaneChangeID = laneSegments.get(i - DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE).id();
            }
        }
    }

    public void act() {
        int stoppingTimeDistance = ((vehicle.getSpeed()) / (vehicle.getMaxDecelerationRate())); // number of time steps it will take to stop
        int crashTimeDistance = Integer.MIN_VALUE;
        if (vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) != null) { 
            int distance = vehicle.findVehDistanceAhead();
            crashTimeDistance = (distance * 100) / vehicle.getSpeed();
        }else if(vehicleLane.getVehicleBehind(vehicle.getHeadSegment()) != null){
            Vehicle behind = vehicleLane.getVehicleBehind(vehicle.getHeadSegment());
            int distance = vehicle.findVehDistanceBehind();
            crashTimeDistance = (distance * 100) / behind.getSpeed();
        }

        if (desire.toString().equals("TURN_LEFT") && vehicleLane.getTurnDirection().toString().equals("RIGHT")) {
            decideLaneChangeAI(stoppingTimeDistance, crashTimeDistance);
            return;
        } else if (desire.toString().equals("TURN_RIGHT") && vehicleLane.getTurnDirection().toString().equals("LEFT")) {
            decideLaneChangeAI(stoppingTimeDistance, crashTimeDistance);
            return;
        }else{
            performStraightLaneAI(stoppingTimeDistance, crashTimeDistance);
        }
        
        //if we are in correct lane then do AI based in that.
        

    }
    
    private boolean safeLaneChangeProximity(){
        if(safeLaneChangeID - vehicle.getHeadSegment().id() < 100){
            return true;
        }
        return false;
    }
    
    protected void performStraightLaneAI(int stoppingTimeDistance, int crashTimeDistance){
        if(crashTimeDistance == Integer.MIN_VALUE){ //if there is no car in our lane
            vehicle.accelerate(vehicle.getMaxAccelerationRate());
        }
        if(vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) == null){ //if no vehicle ahead, accelerate freely
            vehicle.accelerate(vehicle.getMaxAccelerationRate());
        }else if(vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) != null){
            int distance = vehicle.findVehDistanceAhead();
            crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            if(crashTimeDistance > stoppingTimeDistance){
                vehicle.accelerate(5);
            }else if(crashTimeDistance == stoppingTimeDistance){
                vehicle.decelerate(2);
            }else{
                vehicle.decelerate(vehicle.getMaxDecelerationRate());
            }
        }
    }

    private void decideLaneChangeAI(int stoppingTimeDistance, int crashTimeDistance) {
        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        Lane adjacentLane = getAdjacentLane(vehicle.getHeadSegment());
        if (adjacentLane.getVehicles().isEmpty()) {
            changeLane();
            return;
        } else {
            if (adjacentLane.getVehicleAhead(adjacentSeg) != null) {
                Vehicle ahead = adjacentLane.getVehicleAhead(adjacentSeg);
                int distance = ahead.findVehDistanceAhead();
                crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            } else if (adjacentLane.isVehicleAtSegment(adjacentSeg)) { //if there is a vehicle in the immediately adjacent segment to us
                vehicle.decelerate(5); //TODO: maybe think about constants for the deceleration and accel rate
                return;
            } else { 
                Vehicle behind = adjacentLane.getVehicleBehind(adjacentSeg);
                int distance = behind.findVehDistanceBehind();
                crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            }
        }
        if(safeLaneChangeProximity()){
            vehicle.decelerate(5);
        }
        
        if(stoppingTimeDistance < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE){
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane();

        } else {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
        }

    }

    private Lane getAdjacentLane(Segment s) {
        Segment adjacentSeg = getAdjacentSegment(s);
        return adjacentSeg.getLane();
    }

    private Segment getAdjacentSegment(Segment s) {
        Segment adjacentSeg = null;
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        if (connectedSegments.containsValue(ConnectionType.NEXT_TO_LEFT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_LEFT) {
                    adjacentSeg = entry.getKey();
                }
            }
        } else if (connectedSegments.containsValue(ConnectionType.NEXT_TO_RIGHT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_RIGHT) {
                    adjacentSeg = entry.getKey();
                }
            }
        }
        return adjacentSeg;
    }

    protected Desire getDesire() {
        return desire;
    }

    protected void changeLane() {

        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        Lane adjacentLane = getAdjacentLane(vehicle.getHeadSegment());
        vehicle.setHeadSegment(adjacentSeg);
        adjacentLane.addVehicle(vehicle);
    }

    protected boolean isSafeDistanceAhead() {
        return false;
    }
}

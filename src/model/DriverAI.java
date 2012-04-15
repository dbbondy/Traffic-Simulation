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
        int stoppingTimeDistance = Integer.MAX_VALUE;
        int crashTimeDistance = Integer.MAX_VALUE;
        if (vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) != null) {
            stoppingTimeDistance = ((vehicle.getSpeed()) / (vehicle.getMaxDecelerationRate())); // number of time steps it will take to stop
            int distance = vehicle.findVehDistanceAhead();
            crashTimeDistance = (distance * 100) / vehicle.getSpeed();
        }else if(vehicleLane.getVehicleBehind(vehicle.getHeadSegment()) != null){
            Vehicle behind = vehicleLane.getVehicleBehind(vehicle.getHeadSegment());
            stoppingTimeDistance = ((vehicle.getSpeed()) / (vehicle.getMaxDecelerationRate()));
            int distance = vehicle.findVehDistanceBehind();
            crashTimeDistance = (distance * 100) / behind.getSpeed();
        }

        if (desire.toString().equals("TURN_LEFT") && vehicleLane.getTurnDirection().toString().equals("RIGHT")) {
            decideLaneChangeAI(stoppingTimeDistance, crashTimeDistance);
            return;
        } else if (desire.toString().equals("TURN_RIGHT") && vehicleLane.getTurnDirection().toString().equals("LEFT")) {
            decideLaneChangeAI(stoppingTimeDistance, crashTimeDistance);
            return;
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
                vehicle.decelerate(10); //TODO: maybe think about constants for the deceleration and accel rate
                return;
            } else {
                Vehicle behind = adjacentLane.getVehicleBehind(adjacentSeg);
                int distance = behind.findVehDistanceBehind();
                crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            }
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane();
            return;
        } else {
            vehicle.decelerate(10);
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

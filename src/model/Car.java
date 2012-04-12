package model;

// my awesome change
import controller.Simulation;
import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// this is another change
/**
 *
 * @author Daniel Bond
 */
public class Car extends Vehicle {

    private static final int WIDTH = 12;
    private static final int LENGTH = 20;

    public Car() {
    }

    public Car(Lane lane) {
        super(lane, lane.getFirstSegment(), null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(), null, null, c);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg, Color c) {
        super(lane, seg, null, null, c);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg) {
        super(lane, seg, null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c) {
        super(lane, seg, inFront, behind, c);
        setDimensions(WIDTH, LENGTH);
    }

    @Override
    public void act() {
        advanceVehicle(currentSpeed);

        Vehicle ahead = currentLane.getVehicleAhead(this.getHeadSegment());

        int aheadIndex = currentLane.getLaneSegments().indexOf(ahead);
        int currentIndex = currentLane.getLaneSegments().indexOf(this);
        
        if(currentLane.getVehicles().size() == 1){
            accelerate();
            return;
        }
        
        
        

    }

    public void accelerate() {
        this.currentSpeed++;
    }

    public void decelerate() {
        this.currentSpeed--;
    }

    public boolean adjacentLaneAvailability() {
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        Set<Entry<Segment, ConnectionType>> allEntries = connectedSegments.entrySet();
        Segment adjacentSeg = null;
        for (Entry e : allEntries) {
            if (e.getValue() == ConnectionType.NEXT_TO) {
                adjacentSeg = (Segment) e.getKey();
            }
        }
        
        if (adjacentSeg == null) { // there are no "adjacent" segments so we cannot change lanes adjacent
            return false;
        }
        
        Lane adjacentLane = adjacentSeg.getLane();
        
        if(adjacentLane.getVehicles().isEmpty()){ // if there are no vehicles in the adjacent lane we can change lanes.
            return true;
        }
        
        Vehicle vAhead = adjacentLane.getVehicleAhead(adjacentSeg);
        Vehicle vBehind = adjacentLane.getVehicleBehind(adjacentSeg);
        int aheadIndex = adjacentLane.getLaneSegments().indexOf(vAhead.getHeadSegment());
        int behindIndex = adjacentLane.getLaneSegments().indexOf(vBehind.getHeadSegment());
        int currentIndex = adjacentLane.getLaneSegments().indexOf(adjacentSeg);

        if (aheadIndex != -1 && (aheadIndex - currentIndex) > Lane.SAFE_VEHICLE_DISTANCE) { //if vehicle ahead exists and the distance is greater than the safe distance (10 segments)

            if ((this.getSpeed() - vAhead.getSpeed()) > Lane.SAFE_SPEED_DIFFERENTIAL) { //if this vehicle is going faster than 5 speed more than the vehicle ahead in the adjacent lane
                return false;
            }

        } else if (behindIndex != -1 && (currentIndex - behindIndex) > Lane.SAFE_VEHICLE_DISTANCE) { //if vehicle behind exists and the distance is greater than the safe distance (10 segments)

            if ((vBehind.getSpeed() - this.getSpeed()) > Lane.SAFE_SPEED_DIFFERENTIAL) { //if this vehicle is going slower than 5 speed more than the vehicle behind in the adjacent lane
                return false;
            }
        }
        //if no errors were found in the transition between lanes, it is safe to state we can change lanes.
        return true;
    }
}

package model;

import controller.Simulation;
import java.awt.Color;

/**
 *
 * @author Daniel Bond
 */
public class Truck extends Vehicle{
    
    private static final int WIDTH = 14;
    private static final int LENGTH = 30;
    
    public Truck() {}
    
    public Truck(Lane lane) {
        super(lane, lane.getFirstSegment(),null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Truck(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(),null, null, c);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Truck(Lane lane, Segment seg, Color c) {
        super(lane, seg,null, null, c);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Truck(Lane lane, Segment seg) {
        super(lane, seg,null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Truck(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c){
        super(lane, seg, inFront, behind, c);
        setDimensions(WIDTH, LENGTH);
    }

    @Override
    public void act() {
        
        /*
        System.out.println("this " + this.getHeadSegment());
        System.out.println("last " + currentLane.getLastSegment());
        advanceVehicle(currentSpeed);

        Vehicle ahead = currentLane.getVehicleAhead(this.getHeadSegment());
        Vehicle behind = currentLane.getVehicleBehind(this.getHeadSegment());
        //if there is only ourselves in the lane, then we can just blindly accelerate up until the maximum speed
        if (currentLane.getVehicles().size() == 1 && (currentSpeed < (Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED))) {
            accelerate();
            return;
        }

        if ((findVehDistanceAhead(ahead) == -1 || findVehDistanceAhead(ahead) > 5)
                && (currentSpeed < (Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED))) {
            accelerate();
            return;
        }
        
        if(findVehDistanceAhead(ahead) != -1 && findVehDistanceAhead(ahead) <= 5  
                && (currentSpeed < (Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED))){
            decelerate();
            return;
        }

        if (adjacentLaneAvailability()) {
            changeLaneAdjacent();
            return;
        }
        */
    }
    
}

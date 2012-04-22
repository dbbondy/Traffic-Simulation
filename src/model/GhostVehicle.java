package model;

import java.awt.Color;

/**
 * A dummy object for use in the tree-searching algorithms from the {@link java.util.Collections} class
 * @author Daniel Bond
 */
public class GhostVehicle extends Vehicle {
    
    public GhostVehicle(Lane lane, Segment segment) {
        super(lane, segment, null, null, Color.BLACK);
        setDimensions(0, 0);                
    }

    @Override
    public void act() {
    }
    
}

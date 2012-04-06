package model;

import java.awt.Color;

/**
 *
 * @author Daniel Bond
 */
public class Truck extends Vehicle{
    
    private static final int WIDTH = 14;
    private static final int LENGTH = 30;
    
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

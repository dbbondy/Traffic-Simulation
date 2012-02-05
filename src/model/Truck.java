package model;

/**
 *
 * @author Daniel Bond
 */
public class Truck extends Vehicle{
    
   
    private Lane currentLane; // the lane it is contained within.
    
    
    public Truck(Lane lane){
        currentLane = lane;
        
    }

    @Override
    public void act() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
 
    
}

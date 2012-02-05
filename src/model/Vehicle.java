package model;
/**
 *
 * @author Daniel Bond
 */
public abstract class Vehicle {

    protected int currentSpeed;
    protected Segment headSegment;
    protected static final int LENGTH_OF_VEHICLE = 3;
        
    protected Vehicle vehicleInFront;
    protected Vehicle vehicleBehind;
    
    public abstract void act();

    public int getSpeed(){
        return currentSpeed;
    }
    
    public Segment getHeadSegment() {
        return this.headSegment;
    }
    
    public void setHeadSegment(Segment headSegment) {
        this.headSegment = headSegment;
    }
    
    public void updateSpeed(int newSpeed){
        currentSpeed = newSpeed;
    }
    
   /*  public static Vehicle getVehicle(int carRatio, int truckRatio){
        return new Car(); //just a placeholder right now until i figure out how to create based on ratio.
    }
     */
}

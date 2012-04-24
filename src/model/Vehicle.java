package model;

import controller.Simulation;
import java.awt.Color;

/**
 * Abstract representation of a vehicle
 * @author Daniel Bond
 */
public abstract class Vehicle {

    protected int width = 14; // default width of a vehicle
    protected int length = 22; // default length of a vehicle
    protected Color color; // default colour of a vehicle
    protected int currentSpeed; // current speed of a vehicle
    protected Segment headSegment; // head segment of a vehicle
    private int nextSegmentPercent; // the percentage of which we are at the next segment in the lane
    protected int maxAccelerationRate; // the maximum rate of acceleration a vehicle has
    protected int maxDecelerationRate; // the maximum rate of deceleration a vehicle has
    protected DriverAI ai; // the driver intelligence
    
    // a little buffer distance (bumper) that reduces rounding errors and
    // adds a more realistic spacing in tight situations
    public static final int BUMPER_DISTANCE = 5;

    public Vehicle() {
    }

    public Vehicle(Lane lane, Segment segment, Vehicle inFront, Vehicle behind, Color c) {
        headSegment = segment;
        color = c;
        
        currentSpeed = (int) ((3.0/5.0) * (int) Simulation.getOption(Simulation.MAXIMUM_SPEED));
        Vehicle vAhead = getLane().getVehicleAhead(headSegment);
        if (vAhead != null) currentSpeed = vAhead.currentSpeed;
        
        lane.addVehicle(this);
        nextSegmentPercent = 0;
        maxAccelerationRate = 0;
        maxDecelerationRate = 0;
        ai = new DriverAI(this);
    }

    /**
     * General "acting" function for vehicles
     */
    public abstract void act();

    /**
     * Sets the dimensions of a vehicle
     * @param w the width of the vehicle
     * @param l the length of the vehicle
     */
    public void setDimensions(int w, int l) {
        this.width = w;
        this.length = l;
    }
    
    /**
     * Increases the vehicles speed
     * @param acceleration the increase in speed in the vehicle
     */
    protected void accelerate(int acceleration) {
       int maxSpeed = (Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED);
        if(acceleration > maxAccelerationRate) 
            acceleration = maxAccelerationRate;
        currentSpeed += acceleration;
        if (currentSpeed > maxSpeed) 
            currentSpeed = maxSpeed;
    }

    /**
     * Decreases the vehicles speed
     * @param deceleration the decrease in speed in the vehicle
     */
    protected void decelerate(int deceleration) {
        if(deceleration > maxDecelerationRate){
            deceleration = maxDecelerationRate;
        }
        currentSpeed -= deceleration;
        if(currentSpeed < 0){
            currentSpeed = 0;
        }
    }

    /**
     * Advances the vehicle in the road
     * @param value the amount to advance the vehicle by 
     */
    public void advanceVehicle(int value) {
        int newValue = value + nextSegmentPercent;
        nextSegmentPercent = newValue % 100;
        int advanceSegments = newValue / 100;
        for (int i = 0; i < advanceSegments; i++) {
            Segment next = headSegment.getNextSegment();
            if (next == null) return;
            headSegment = next;
        }
    }
    
    /**
     * Advances the vehicle in the road (test only)
     * @param value the amount to advance the vehicle by 
     * @return the expected segment if committed
     */
    public Segment advanceVehicleTest(int value) {
        int newValue = value + nextSegmentPercent;
        int advanceSegments = newValue / 100;
        Segment expectedSegment = headSegment;
        for (int i = 0; i < advanceSegments; i++) {
            Segment next = expectedSegment.getNextSegment();
            if (next == null) break;
            expectedSegment = next;
        }
        return expectedSegment;
    }
    
    /**
     * Finds the distance between this vehicle and the vehicle ahead
     * We assume that a vehicle ahead exists for this method to work correctly.
     * If this assumption does not hold at method invocation, then we throw a {@link java.lang.RuntimeException}
     * @return the distance between this vehicle and the vehicle ahead
     */
    public int findVehDistanceAhead() {
        Vehicle vAhead = getLane().getVehicleAhead(headSegment);
        if (vAhead == null) throw new RuntimeException("no vehicle ahead");
        // we reduce actual distance by one to prevent overlap due to partial segment progression
        return ((vAhead.headSegment.id() - vAhead.length) - headSegment.id()) - 1;
    }

    /**
     * Finds the distance between this vehicle and the vehicle behind
     * We assume that a vehicle behind exists for this method to work correctly.
     * If this assumption does not hold at method invocation, then we throw a {@link java.lang.RuntimeException}
     * @return the distance between this vehicle and the vehicle behind
     */
    public int findVehDistanceBehind() {
        Vehicle vBehind = getLane().getVehicleBehind(headSegment);
        if (vBehind == null) throw new RuntimeException("no vehicle behind");
        // we reduce actual distance by one to prevent overlap due to partial segment progression
        return ((headSegment.id() - length) - vBehind.headSegment.id()) - 1;
    }

    /**
     * Gets the lane that this vehicle is currently in
     * @return the lane the vehicle is currently in
     */
    public Lane getLane(){
        return headSegment.getLane();
    }

    /**
     * Gets the width of the vehicle
     * @return the width of the vehicle
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the length of the vehicle
     * @return the length of the vehicle
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Gets the colour of this vehicle
     * @return the RGB {@link java.awt.Color} object representing this vehicles colour
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets the current speed of the vehicle
     * @return the current speed of the vehicle
     */
    public int getSpeed() {
        return currentSpeed;
    }

    /**
     * Gets the segment at the head of the vehicle
     * @return the segment at the head of the vehicle
     */
    public Segment getHeadSegment() {
        return this.headSegment;
    }

    /**
     * Gets the maximum acceleration rate of this vehicle
     * @return the maximum acceleration rate of this vehicle
     */
    public int getMaxAccelerationRate() {
        return maxAccelerationRate;
    }

    /**
     * Gets the maximum deceleration rate of this vehicle
     * @return the maximum deceleration rate of this vehicle
     */
    public int getMaxDecelerationRate() {
        return maxDecelerationRate;
    }
    
    /**
     * Sets the segment at the head of the vehicle
     * @param headSegment the new head segment of the vehicle
     */
    public void setHeadSegment(Segment headSegment) {
        this.headSegment = headSegment;
    }

    /**
     * Sets the current speed of the vehicle
     * @param newSpeed the new current speed of the vehicle
     */
    public void setSpeed(int newSpeed) {
        currentSpeed = newSpeed;
    }

    /**
     * Sets the colour of the vehicle
     * @param color the new colour of the vehicle
     */
    public void setColor(Color color) {
        this.color = color;
    } 
}

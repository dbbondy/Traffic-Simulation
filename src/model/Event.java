package model;

/**
 * Models a statistical event for output to a file
 * @author Daniel Bond
 */
public class Event {
    
    private String name; // the name of the event
    private int timeStep; // the time step that the event happened on
    private int aggression; // the aggression value at the point the event occurred
    private int minDensity; // the minimum density value at the point the event occurred
    private int maxDensity; // the maximum density value at the point the event occurred
    private int ratioCars; // the ratio of cars value at the point the event occurred
    private int ratioTrucks;  // the ratio of trucks value at the point the event occurred
    private int maxSpeed; // the maximum speed value of vehicles at the point the event occurred

    public Event(String name, int timeStep, int aggression, 
            int minDensity, int maxDensity, int ratioCars, 
            int ratioTrucks, int maxSpeed) {
        
        this.name = name;
        this.timeStep = timeStep;
        this.aggression = aggression;
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.ratioCars = ratioCars;
        this.ratioTrucks = ratioTrucks;
        this.maxSpeed = maxSpeed;
    }
    
    /**
     * gets the aggression value of the event
     * @return the aggression value of the event
     */
    public int getAggression() {
        return aggression;
    }

    /**
     * gets the maximum density value of the event
     * @return the maximum density value of the event
     */
    public int getMaxDensity() {
        return maxDensity;
    }
    
    /**
     * gets the minimum density value of the event
     * @return the minimum density value of the event
     */
    public int getMinDensity() {
        return minDensity;
    }

    /**
     * gets the maximum speed value of the event
     * @return the maximum speed value of the event
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * gets the name of the event
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * gets the ratio of cars value of the event
     * @return the ratio of cars value of the event
     */
    public int getRatioCars() {
        return ratioCars;
    }

    /**
     * gets the ratio of trucks value of the event
     * @return the ratio of trucks value of the event
     */
    public int getRatioTrucks() {
        return ratioTrucks;
    }

    /**
     * gets the time step that the event occurred on
     * @return the time step of the event
     */
    public int getTimeStep() {
        return timeStep;
    }
    
}

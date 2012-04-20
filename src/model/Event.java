/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dan
 */
public class Event {
    
    private String name;
    private int timeStep;
    private int aggression; 
    private int minDensity;
    private int maxDensity;
    private int ratioCars;
    private int ratioTrucks; 
    private int maxSpeed;

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

    public int getAggression() {
        return aggression;
    }

    public int getMaxDensity() {
        return maxDensity;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getMinDensity() {
        return minDensity;
    }

    public String getName() {
        return name;
    }

    public int getRatioCars() {
        return ratioCars;
    }

    public int getRatioTrucks() {
        return ratioTrucks;
    }

    public int getTimeStep() {
        return timeStep;
    }
    
    
    
}

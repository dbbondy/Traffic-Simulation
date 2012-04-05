/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.*;
import model.junctions.Junction;

/**
 *
 * @author Dan
 */
public final class State implements Serializable {

    
    private Integer timeStep;
    private String currentJunction;
    private Integer minDensity;
    private Integer maxDensity;
    private Integer aggression;
    private Integer carRatio;
    private Integer truckRatio;

    public State(int timeStep, String currentJunction, int minDensity,int maxDensity, int aggression, int carRatio, int truckRatio) {
        this.timeStep = timeStep;
        this.currentJunction = currentJunction;
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.aggression = aggression;
        this.carRatio = carRatio;
        this.truckRatio = truckRatio;
    }

    public int getAggression() {
        return aggression;
    }

    public int getCarRatio() {
        return carRatio;
    }

    public String getJunction() {
        return currentJunction;
    }

    public int getMinDensity() {
        return minDensity;
    }
    
    public int getMaxDensity(){
        return maxDensity;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public int getTruckRatio() {
        return truckRatio;
    }
}

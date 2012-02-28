/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

/**
 *
 * @author Dan
 */

public class GhostVehicle extends Vehicle {
    
    public GhostVehicle(Lane lane, Segment segment) {
        super(lane, segment, Color.BLACK);
        setDimensions(0, 0);                
    }

    @Override
    public void act() {
    }
    
}

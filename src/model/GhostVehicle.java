/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dan
 */
public class GhostVehicle extends Vehicle{
    
    public GhostVehicle(Segment segment){
        headSegment = segment;
    }

    @Override
    public void act() {
    }
    
}

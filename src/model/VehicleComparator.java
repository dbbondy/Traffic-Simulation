/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Comparator;

/**
 *
 * @author Dan
 */
public class VehicleComparator implements Comparator<Vehicle> {

    private VehicleComparator() { }
    private static VehicleComparator instance;
    
    public static VehicleComparator getInstance() {
        if (instance == null) instance = new VehicleComparator();
        return instance;
    }
    
    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        return v1.getHeadSegment().id() - v2.getHeadSegment().id();
    }
    
}

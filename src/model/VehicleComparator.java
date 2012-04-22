package model;

import java.util.Comparator;

/**
 * A custom comparator for use in tree-based searching algorithms
 * @author Daniel Bond
 */
public class VehicleComparator implements Comparator<Vehicle> {

    private VehicleComparator() { }
    private static VehicleComparator instance; // the comparator instance
    
    /**
     * Gets the instance of this comparator.
     * If the instance is null, then we create a new instance and return it
     * @return the instance of this comparator
     */
    public static VehicleComparator getInstance() {
        if (instance == null) instance = new VehicleComparator();
        return instance;
    }
    
    /**
     * Compares two vehicles to produce an ordering of vehicles.
     * The ordering is decided by each vehicles position within a lane
     * and compares the id of each vehicles head segment
     * @param v1 the first vehicle to compare
     * @param v2 the second vehicle to compare
     * @return > 0 if v1 is in front of v2
     * @return 0 if v1 and v2 are in the same segment
     * @return < 0 if v2 is ahead of v1
     */
    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        return v1.getHeadSegment().id() - v2.getHeadSegment().id();
    }
    
}

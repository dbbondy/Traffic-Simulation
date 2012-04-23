
package model;

import java.util.ArrayList;

/**
 *
 * @author Daniel Bond
 */
public abstract class VehicleBinarySearch {
 
    public static Vehicle findVehicleAhead(ArrayList<Vehicle> vehicles, Segment s){
                
        int segmentID = s.id();
        
        if (vehicles.isEmpty()) return null;
        
        int indexMin = 0;
        int indexMax = vehicles.size() - 1;
        
        while (indexMax >= indexMin) {            
            int indexMid = (indexMin + indexMax) / 2;
            int midSegmentID = vehicles.get(indexMid).getHeadSegment().id();     
            if (midSegmentID <= segmentID){
                indexMin = indexMid + 1;
            } else {
                indexMax = indexMid - 1;
            }
        }
        
        // none of the cars are ahead of us
        if (indexMin == vehicles.size()) {
            return null;
        }
        return vehicles.get(indexMin);
    }
    
    public static Vehicle findVehicleBehind(ArrayList<Vehicle> vehicles, Segment s){
        
        int segmentID = s.id();
        
        if (vehicles.isEmpty()) return null;
        
        int indexMin = 0;
        int indexMax = vehicles.size() - 1;
        
        while (indexMax >= indexMin) {            
            int indexMid = (indexMin + indexMax) / 2;
            int midSegmentID = vehicles.get(indexMid).getHeadSegment().id();            
            if (midSegmentID < segmentID) {
                indexMin = indexMid + 1;
            }else {
                indexMax = indexMid - 1;
            }
        }
       
        // none of the cars are behind us
        if (indexMin == 0) {
            return null;
        }
        
        return vehicles.get(indexMin - 1);
    }
    
    public static void addVehicle(ArrayList<Vehicle> vehicles, Vehicle vehicle){
       
        int segmentID = vehicle.getHeadSegment().id();
        
        // often we add at start of lane so this line allows
        // us to skip the binary search if that if required
        // we also skip if no other vehicles
        if (vehicles.isEmpty() || vehicles.get(0).getHeadSegment().id() > segmentID) {
            vehicles.add(0, vehicle);
            return;
        }
        
        int indexMin = 0;
        int indexMax = vehicles.size() - 1;
        
        while (indexMax >= indexMin) {            
            int indexMid = (indexMin + indexMax) / 2;
            int midSegmentID = vehicles.get(indexMid).getHeadSegment().id();            
            if (midSegmentID < segmentID) {  //if we are greater than the mid section (segment id) 1,2,3,4,5,6,7,8,9,10
                indexMin = indexMid + 1;
            } else if (midSegmentID > segmentID) {
                indexMax = indexMid - 1;
            } else {
                // we should never add a car in a position such 
                // that a car already exists here as it is collision
                throw new RuntimeException("Vehicle Collision");
            }
        }
        
        vehicles.add(indexMin, vehicle); 
    }
}

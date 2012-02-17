package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


//TODO: create classes for each invidividual junction and just create instances of Lane necessary amounts of times
//TODO: split "buildLaneSegments" into "buildStraightSection(x)"  and "buildLeftCurve()" and "buildRightCurve()"
//TODO: continue to work on graphics

/**
 *
 * @author Dan
 */
public class Lane {

    private int xCoord;
    private int yCoord;
    private Segment[] laneSegments; 
    public static final int SEGMENT_DENSITY = 1;
    
    private int initialAngle;
    
    private ArrayList<Vehicle> vehicles;
    
    public Lane(int xCoord, int yCoord, int initialAngle) {
        buildLaneSegments();
        vehicles = new ArrayList<>(32); 
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.initialAngle = initialAngle;
    }
    
    public int getInitialAngle() {
        return this.initialAngle;
    }
        
    public Segment getFirstSegment() {
        return laneSegments[0];
    }
    
    public Segment getLastSegment(){
        return laneSegments[laneSegments.length - 1];
    }
    
    public int findSegmentPosition(Vehicle vehicle){
        Comparator com = VehicleComparator.getInstance();
        int index = Collections.binarySearch(vehicles, vehicle, com);
        
        if(index < 0){
            return -1;
        }
        //not even sure if this is even needed. i THINK i need it. 
        return index;
    }
    
    // 
    //    3 ZxZ straight bits
    //    1 30 degree left
    //    1 30 degree left
    //    1 45 degree right
    //    3 ZvZ straight bits
    //    
    //    // build10Xsegments(x)
    //
    //
    
    public ArrayList<Vehicle> getVehicles(){
        return vehicles;
    }
    
    private void buildLaneSegments(){ 
            
        // todo: this properly
        
        RoadDesigner d = new RoadDesigner();
        laneSegments = new Segment[80];
        
        Segment b = null;
        for(int i = 0; i < 80; i++)
        {
            Segment a = new Segment(this, 5, 0);
            a.setPreviousSegment(b);
            if (b!=null)b.setNextSegment(a);
            b = a;
            laneSegments[i] = a;     
        }     
    }
    
    public Vehicle getVehicleAhead(Segment segment) {
        Comparator com = VehicleComparator.getInstance();
        
        // ghost vehicle represents imaginary vehicle positioned at the desired segment
        Vehicle ghostVehicle = new GhostVehicle(segment);
        int index = Collections.binarySearch(vehicles, ghostVehicle, com);
        
        // when index returned is negative it represents the location the object would be inserted in but made negative to indicate that it wasn't found
        if (index < 0){
            index = (index * -1) - 1;
        } 
        vehicles.remove(ghostVehicle);
        if (index == vehicles.size()){
            return null;
        }
        Vehicle ahead = vehicles.get(index);        
        return ahead;
    }    
    
    public Vehicle getVehicleBehind(Segment segment) {
        Comparator com = VehicleComparator.getInstance();
        
        // ghost vehicle represents imaginary vehicle positioned at the desired segment
        Vehicle ghostVehicle = new GhostVehicle(segment);        
        int index = Collections.binarySearch(vehicles, ghostVehicle, com);
       
        // when index returned is negative it represents the location the object would be inserted in but made negative to indicate that it wasn't found
        if (index < 0){
            index = (index * -1) - 1;
        }
        vehicles.remove(ghostVehicle);
        if (index == 0){
            return null;
        }
        Vehicle behind = vehicles.get(index-1);
        return behind;
    }
    
    public void addVehicle(Vehicle vehicle) {
        Comparator com = VehicleComparator.getInstance();
        int index = Collections.binarySearch(vehicles, vehicle, com);
        
        // when index returned is negative it represents
        // the location the object would be inserted in
        // but made negative to indicate that it wasn't found
        if (index < 0){
            index = (index * -1) - 1;
        }
        vehicles.add(index, vehicle);
    }
    
    public Segment getFrontVehicle(){
        
        int currentFront = 0;
        Segment headSegmentFront = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getHeadSegment().id() > currentFront) { //if the ID > current segment. it is in front
                currentFront = vehicle.getHeadSegment().id();
                headSegmentFront = vehicle.getHeadSegment();
            }
        }
        return headSegmentFront;
    }
    
    public int getXStart(){
        return xCoord;
    }
    
    public int getYStart(){
        return yCoord;
    }
    
    public void updateVehicles(){ //will be more complex than this later on. just a placeholder body at the moment
        for(Vehicle v : vehicles){
            Segment s = v.getHeadSegment();
            Segment s1 = s.getNextSegment();
            if(s1 == null){ 
                continue;
            }
            v.setHeadSegment(s1);
        }
    }
    
}

package model;

import exceptions.NoLaneExistsException;
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
    
    private ArrayList<Vehicle> vehicles;
    
    public Lane(int xCoord, int yCoord) {
        buildLaneSegments();
        vehicles = new ArrayList<Vehicle>(32); 
        this.xCoord = xCoord;
        this.yCoord = yCoord;        
    }
    
    public Segment getFirstSegment() {
        return laneSegments[0];
    }
    
    public Segment getLastSegment(){
        return laneSegments[laneSegments.length - 1];
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
    
    private void buildLaneSegments(){ 
            
        RoadDesigner d = new RoadDesigner();
        laneSegments = new Segment[20];
        
        int sizeCounter = 10;
        Segment[] tempSegments = null;
        for(int i = 0 ; i < laneSegments.length; i++){
            if (i % sizeCounter == 0) {
                try{
                    tempSegments = d.build10Segments(this);
                    laneSegments[i] = tempSegments[0];
                }catch(NoLaneExistsException e){
                    e.printStackTrace();
                }
            }
            laneSegments[i] = tempSegments[(i % sizeCounter)];
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
    
    
    
}
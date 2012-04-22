package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to model a singular segment of a lane
 * @author Daniel Bond
 */
public class Segment {
    
    private int length; // the length of the segment
    private int angle; // the angle of the segment
    public static final int WIDTH = 22; // the width of the segment
    public static final int LENGTH = 1; // the length of the segment
    private Segment next = null; // reference to the segment in front
    private Segment previous = null; // reference to the segment behind
    private Map<Segment, ConnectionType> connectedSegments; // map of all connected segments to this segment
    private Lane lane; // the lane this segment is a part of
    private int id; // the lane specific id of this segment
    private int renderAngle; // the angle in which this segment is rendered at
    private double renderX; // the x-coordinate in which this segment is rendered at
    private double renderY; // the y-coordinate in which this segment is rendered at
    
    public static final int VEHICLE_HEAD_OFFSET = 10; // the offset from the vehicle ahead used for computation

    public Segment(Lane lane, int length, int angle){
        this.length = length;
        this.angle = angle;
        this.lane = lane;
        this.id = lane.getNewSegmentID();
        connectedSegments = new HashMap<>();
    }
    
    /**
     * Gets the lane the segment is contained within
     * @return the lane the segment is contained within
     */
    public Lane getLane() {
        return this.lane;
    }
    
    /**
     * Gets the ID of this segment
     * @return the id of this segment
     */
    public int id() {
        return this.id;
    }

    /**
     * Gets the x-coordinate that this segment is rendered at
     * @return the x-coordinate that this segment is rendered at
     */
    public double getRenderX() {
        return renderX;
    }

    /**
     * Sets the x-coordinate that this segment is rendered at
     * @param renderX the value to use as the x-coordinate for rendering
     */
    public void setRenderX(double renderX) {
        this.renderX = renderX;
    }

    /**
     * Gets the y-coordinate that the segment is rendered at
     * @return the y-coordinate that this segment is rendered at
     */
    public double getRenderY() {
        return renderY;
    }

    /**
     * Sets the y-coordinate that this segment will be rendered at
     * @param renderY the value to use as the y-coordinate for rendering
     */
    public void setRenderY(double renderY) {
        this.renderY = renderY;
    }
    
    /**
     * Gets the length of this segment
     * @return the length of the segment
     */
    public int getLength() {
        return this.length;
    }
    
    /**
     * Gets the angle of this segment
     * @return the angle of this segment
     */
    public int getAngle() {
        return this.angle;
    }
    
    /**
     * Sets the angle in which this segment is rendered at.
     * @param angle the angle to render the segment at.
     */
    public void setRenderAngle(int angle){
        renderAngle = angle;
    }
    
    /**
     * Gets the angle that the segment is rendered at
     * @return the angle that this segment is rendered at.
     */
    public int getRenderAngle(){
        return renderAngle;
    }
    
    /**
     * Adds a connected segment to this segment
     * @param segment the segment to connect to this segment
     * @param type the type of connection
     */
    public void addConnectedSegment(Segment segment, ConnectionType type){
        connectedSegments.put(segment, type);
    }
    
    /**
     * Add a collection of segments to be connected to this segment
     * @param segments the collection of segments to connect to this segment
     * @param type the type of connection for these segments
     */
    public void addConnectedSegments(Segment[] segments, ConnectionType type){
        for (Segment s : segments) 
            addConnectedSegment(s, type);
    }
    
    /**
     * Gets all the connected segments as a Map.
     * @return the Map representation of the connected segments to this segment
     */
    public Map<Segment, ConnectionType> getConnectedSegments(){
        return connectedSegments;
    }
    
    /**
     * Sets the reference to the next segment in front of this segment
     * @param segment the segment that is "in front" of this segment
     */
    public void setNextSegment(Segment segment){
        if (segment != null) segment.previous = this;
        next = segment;        
    }
    
    /**
     * Sets the reference to the previous segment behind this segment
     * @param segment the segment that is "behind" this segment
     */
    public void setPreviousSegment(Segment segment){
        if (segment != null) segment.next = this;
        previous = segment;        
    }
    
    /**
     * Gets the segment denoted to be "in front" of this segment
     * @return the segment that is in front of this segment
     */
    public Segment getNextSegment(){
        return next;
    }
    
    /**
     * Gets the segment denoted to be "behind" this segment
     * @return the segment that is behind this segment
     */
    public Segment getPreviousSegment(){
        return previous;
    }
}

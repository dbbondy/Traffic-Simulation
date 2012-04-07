package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dan
 */
public class Segment{
    
    private int length;
    private int angle;
    public static final int WIDTH = 22;
    public static final int LENGTH = 1;
    private Segment next = null;
    private Segment previous = null;
    private Map<Segment, ConnectionType> connectedSegments;
    private Lane lane;
    private static int segmentCounter = 0;
    private int id;
    private int renderAngle;
    private double renderX;
    private double renderY;
    
    public static final int VEHICLE_HEAD_OFFSET = 10;

    public Segment(Lane lane, int length, int angle){
        this.length = length;
        this.angle = angle;
        this.lane = lane;
        this.id = segmentCounter++;
        connectedSegments = new HashMap<>();
    }
    
    public Lane getLane() {
        return this.lane;
    }
    
    public int id() {
        return this.id;
    }

    public double getRenderX() {
        return renderX;
    }

    public void setRenderX(double renderX) {
        this.renderX = renderX;
    }

    public double getRenderY() {
        return renderY;
    }

    public void setRenderY(double renderY) {
        this.renderY = renderY;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public int getAngle() {
        return this.angle;
    }
    
    public void addConnectedSegment(Segment segment, ConnectionType type){
        connectedSegments.put(segment, type);
    }
    
    public void addConnectedSegments(Segment[] segments, ConnectionType type){
        for (Segment s : segments) addConnectedSegment(s, type);
    }
    
    public Map<Segment, ConnectionType> getConnectedSegments(){
        return connectedSegments;
    }
    
    public void setNextSegment(Segment segment){
        if (segment != null) segment.previous = this;
        next = segment;        
    }
    
    public void setPreviousSegment(Segment segment){
        if (segment != null) segment.next = this;
        previous = segment;        
    }
    
    public Segment getNextSegment(){
        return next;
    }
    
    public Segment getPreviousSegment(){
        return previous;
    }
    
    public void setRenderAngle(int angle){
        renderAngle = angle;
    }
    
    public int getRenderAngle(){
        return renderAngle;
    }
    
}

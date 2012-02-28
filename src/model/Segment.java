package model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dan
 */
public class Segment {
    
    private int length;
    private int angle;
    public static final int WIDTH = 22;
    public static final int LENGTH = 1;
    private Segment next;
    private Segment previous;
    private List<Segment> connectedSegments;
    private Lane lane;
    private ConnectionType connType;
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
        connectedSegments = new ArrayList<Segment>();
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
    
    public void setConnectionType(ConnectionType type){
        connType = type;
    }
    
    public void addConnectedSegment(Segment segment){
        connectedSegments.add(segment);
    }
    
    public void addConnectedSegments(Segment[] segments){
        connectedSegments.addAll(Arrays.asList(segments));
    }
    
    public List<Segment> getConnectedSegments(){
        return connectedSegments;
    }
    
    public void setNextSegment(Segment segment){
        segment.previous = this;
        next = segment;        
    }
    
    public void setPreviousSegment(Segment segment){
        segment.next = this;
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

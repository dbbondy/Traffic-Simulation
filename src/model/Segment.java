package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dan
 */
public class Segment {
    
    private int length;
    private double angle;
    public static final int WIDTH = 22;
    private Segment next;
    private Segment previous;
    private List<Segment> connectedSegments;
    private Lane lane;
    private ConnectionType connType;
    private static int segmentCounter = 0;
    private int id;
    private double renderX; // known after rendering
    private double renderY; // known after rendering
    private double renderAngle;

    public Segment(Lane lane, int length, double angle){
        this.length = length;
        this.angle = angle;
        this.lane = lane;
        this.id = segmentCounter++;
        connectedSegments = new ArrayList<Segment>();
    }

    public double getRenderAngle() {
        return renderAngle;
    }

    public void setRenderAngle(double renderAngle) {
        this.renderAngle = renderAngle;
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
    
    public int id() {
        return this.id;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public double getAngle() {
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
        next = segment;
    }
    
    public void setPreviousSegment(Segment segment){
        previous = segment;
    }
    
    public Segment getNextSegment(){
        return next;
    }
    
    public Segment getPreviousSegment(){
        return previous;
    }
    
}

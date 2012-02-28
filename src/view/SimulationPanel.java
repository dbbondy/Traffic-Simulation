/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Lane;
import model.Segment;
import model.Vehicle;
import model.junctions.Junction;

/**
 *
 * @author Dan
 */
public class SimulationPanel extends JPanel {

    private BufferedImage image; //junction image
    private Junction currentJunction;

    public SimulationPanel() {
        super();
        initPanel();

    }

    private void initPanel() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.white);
        currentJunction = (Junction)Simulation.getOption(Simulation.JUNCTION_TYPE);
    }

    private void drawVehicles(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        ArrayList<Lane> lanes = currentJunction.getLanes();
        for(Lane l : lanes){
            for(Vehicle v : l.getVehicles()){
                Segment head = v.getHeadSegment();
                double vehicleX = head.getRenderX();
                double vehicleY = head.getRenderY();
                int vehicleAngle = head.getRenderAngle();
                graphics.rotate((Math.PI * (vehicleAngle / 180.0)), vehicleX, vehicleY);
                double startX = vehicleX - (v.getWidth() / 2);
                // we draw it slightly further forward (steering wheel from front)
                // so that it looks better going round corners. 
                double startY = vehicleY - (v.getLength() - Segment.VEHICLE_HEAD_OFFSET);
                graphics.setColor(v.getColor());
                Shape rect = new RoundRectangle2D.Double(startX, startY, v.getWidth(), v.getLength(), 5, 5);
                graphics.fill(rect);
                graphics.rotate(-(Math.PI * (vehicleAngle / 180.0)), vehicleX, vehicleY);
            }
        }
    }

    private void renderStraightToImage(Graphics2D graphics, int angle, double x, double y, int length) {
        
        // convert the angle to radians
        double angleRadians = Math.PI * (angle/180.0);
        int width = Segment.WIDTH;
        
        // <x> is the "bottom center" and <startX> is the "bottom left". 
        double startX = (x - (width / 2)); 
        double startY = (y);
        
        // rotate the canvas about the "bottom center" of the section
        graphics.rotate(angleRadians, x, y);
        
        Shape straight = new Rectangle2D.Double(startX, startY, width, length);
        graphics.fill(straight);
        
        // undo rotation of canvas
        graphics.rotate(-angleRadians, x, y);
        
    }
    
    private double[] renderCornerToImage(Graphics2D graphics, int angle, double x, double y, int cornerAngle) {
        
        // convert the angle to radians
        double angleRadians = Math.PI * (angle/180.0);          
        double centerX;
        double centerY;
        
        if (cornerAngle >= 0) { // clockwise direction
            centerX = x - Math.cos(angleRadians) * (Segment.WIDTH / 2);
            centerY = y - Math.sin(angleRadians) * (Segment.WIDTH / 2);
        } else { // anti-clockwise direction
            centerX = x + Math.cos(angleRadians) * (Segment.WIDTH / 2);
            centerY = y + Math.sin(angleRadians) * (Segment.WIDTH / 2);
        }
        
        // bounds of a sphere such that the arc
        // is part of that sphere
        double startX = centerX - Segment.WIDTH;
        double startY = centerY - Segment.WIDTH;
        int boxSize = Segment.WIDTH * 2;
        
        int startAngle = angle;
        
        if (cornerAngle < 0) {
            startAngle += 180;
        }
        
        Rectangle2D bounds = new Rectangle2D.Double(startX, startY, boxSize, boxSize);
        Shape corner = new Arc2D.Double(bounds, -startAngle, -cornerAngle, Arc2D.Double.PIE);
        graphics.fill(corner);
        
        // return the center co-ords so we can calculate
        // the new currentX currentY outside 
        return new double[] { centerX, centerY };       
    }
    
    private void renderToImage() {
        
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // create a graphics object from the buffered image object
        Graphics2D graphics = image.createGraphics();
        ArrayList<Lane> lanes = currentJunction.getLanes();         
        
        int angle;
        double currentX, currentY;
        
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics.setColor(Color.GRAY);   
        
        for (Lane lane : lanes) { // for every lane in the junction
            
            // co-ords for the start a segment (in the middle)
            // initially set to the start of the first segment 
            // within the lane. 
            currentX = lane.getXStart(); 
            currentY = lane.getYStart(); 
            
            // angle the lane starts at            
            angle = lane.getInitialAngle(); 
            
            Segment next = lane.getFirstSegment(); 
            Segment last = lane.getLastSegment();
            
            // we create a "dummy" segment that is opposite 
            // to the last segment (angle => straight, straight => angle)
            // so that the code detects the change and renders
            // everything up until that point. this allows
            // us to avoid duplicating code outside the 
            // while loop block.
            int voidAngle = last.getAngle() == 0 ? 1 : 0;
            Segment voidSegment = new Segment(lane, 0, voidAngle);
            last.setNextSegment(voidSegment);
            
            // used to indicate if the current section (collection of segments) 
            // we are processing form a straight section or corner
            boolean currentTypeStraight;
            
            // used to indicate if the current section (collection of segments)
            // we are processing has a positive angle (clockwise angle)
            // so that we can detect a change in rotation 
            // (clockwise => anti-clockwise and the reverse). 
            boolean positiveAngle;
            
            // sum of all lengths or angles in a section 
            // so that we can render it all at once
            int concatValue = 0;            
            
            if (next.getAngle() == 0) { // a straight section
                currentTypeStraight = true;
                concatValue = next.getLength();
                positiveAngle = next.getAngle() >= 0;
            } else { // a corner section
                currentTypeStraight = false;
                positiveAngle = next.getAngle() >= 0;
                concatValue = next.getAngle();
            }
            
            next.setRenderAngle(angle);
            next.setRenderX(currentX);
            next.setRenderY(currentY);
                    
            while ((next = next.getNextSegment()) != null) { // while more segments
                
                // we need to render the straight section we were just
                // looping over as the next section is a corner
                if (next.getAngle() != 0 && currentTypeStraight) {
                    renderStraightToImage(graphics, angle, currentX, currentY, concatValue); 
                    
                    // Calculate the coordinates of the next segment based on the
                    // coordinates of the first segment in the section being rendered
                    // and the distance travelled by that section. The angle of the
                    // straight section is used to compute the x and y components. 
                    currentX -= Math.sin((Math.PI * (angle/180.0))) * concatValue;
                    currentY += Math.cos((Math.PI * (angle/180.0))) * concatValue;
                    
                    next.setRenderX(currentX);
                    next.setRenderY(currentY);
                    next.setRenderAngle(angle);
                    
                    concatValue = next.getAngle();
                    currentTypeStraight = false;
                    positiveAngle = next.getAngle() >= 0;
                    continue;
                }
                
                // if next section is straight and current section is a corner
                // or next section has opposite rotation to current section (clockwise/anti-clockwise)
                if ((next.getAngle() == 0 && !currentTypeStraight) || next.getAngle() >= 0 != positiveAngle) {
                    double[] center = renderCornerToImage(graphics, angle, currentX, currentY, concatValue);  
                    
                    // calculate the coordinates of the next segment based on the center of 
                    // rotation, the initial angle and the angle of rotation
                    double za = 360 - (concatValue + (concatValue < 0 ? angle + 180 : angle));
                    double zaRadians = (Math.PI * (za/180.0));
                    currentX = center[0] + (Math.cos(zaRadians) * (Segment.WIDTH/2));
                    currentY = center[1] - (Math.sin(zaRadians) * (Segment.WIDTH/2));
                    
                    angle += concatValue;
                    concatValue = next.getLength();
                    currentTypeStraight = next.getAngle() == 0;
                    positiveAngle = next.getAngle() >= 0;
                    continue;
                }
                
                // step over additional segments that make up 
                // the straight section currently being processed
                if (next.getAngle() == 0 && currentTypeStraight) {
                    concatValue += next.getLength();
                    double renderX = currentX - Math.sin((Math.PI * (angle/180.0))) * concatValue;
                    double renderY = currentY + Math.cos((Math.PI * (angle/180.0))) * concatValue;
                    next.setRenderX(renderX);
                    next.setRenderY(renderY);
                    next.setRenderAngle(angle);
                    continue;
                }
                
                // as above (for a corner)
                if (next.getAngle() != 0 && !currentTypeStraight) {
                    concatValue += next.getAngle();
                    continue;
                }
                
            }
        }

    }

    @Override
    public void paintComponent(Graphics graphics1) {
        super.paintComponent(graphics1);        
        
        // no need to paint if we can't see it
        if (getWidth() == 0 || getHeight() == 0) return;
        
        // create cache image if necessary
        if (null == image
                || image.getWidth() != getWidth()
                || image.getHeight() != getHeight()) {
            renderToImage();
        }
        
        Graphics2D graphics = (Graphics2D) graphics1;
        
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // redraw the junction from the image cache
        graphics.drawImage(image, 0, 0, null);        
        drawVehicles(graphics);
    }
}

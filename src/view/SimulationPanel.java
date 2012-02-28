/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
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

    private void drawVehicles(Graphics g, int angle, double startX, double startY, double endX, double endY){
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(Color.red);
        ArrayList<Lane> lanes = currentJunction.getLanes();
        for(Lane l : lanes){
            ArrayList<Segment> laneSegments = l.getLaneSegments();
            for(Vehicle v : l.getVehicles()){
                Segment head = v.getHeadSegment();
                head.
                
                
                /* TODO: 1. do a road at an angle
                    2. add corner bits in between straights
                    the cars on straights
                    after a corner
                    are broken
                    roads coming from the left, right, bottom
                    are classed as angles
                    the cars are in the right position
                    but facing the wrong way*/
              
            }
        }
    }

    private void renderStraightToImage(Graphics2D graphics, int angle, double x, double y, int length) {
        // convert the angle to radians
        double angleRadians = Math.PI * (angle/180.0);
        int width = Segment.WIDTH;
        double startX = (x - (width / 2));
        double startY = (y);
        graphics.rotate(angleRadians, x, y);
        Shape straight = new Rectangle2D.Double(startX, startY, width, length);
        graphics.fill(straight);
        graphics.rotate(-angleRadians, x, y);
    }
    
    private double[] renderCornerToImage(Graphics2D graphics, int angle, double x, double y, int cornerAngle) {
        
        // convert the angle to radians
        double angleRadians = Math.PI * (angle/180.0);          
        double centerX;
        double centerY;
        
        if (cornerAngle >= 0) {
            centerX = x - Math.cos(angleRadians) * (Segment.WIDTH / 2);
            centerY = y - Math.sin(angleRadians) * (Segment.WIDTH / 2);
        } else {
            centerX = x + Math.cos(angleRadians) * (Segment.WIDTH / 2);
            centerY = y + Math.sin(angleRadians) * (Segment.WIDTH / 2);
        }
        
        double startX = centerX - Segment.WIDTH;
        double startY = centerY - Segment.WIDTH;
        int boxSize = Segment.WIDTH * 2;
        int startAngle = angle;
        
        if (cornerAngle < 0) {
            startAngle += 180;
        }
        
        graphics.setColor(Color.DARK_GRAY); 
        Rectangle2D bounds = new Rectangle2D.Double(startX, startY, boxSize, boxSize);
        Shape corner = new Arc2D.Double(bounds, -startAngle, -cornerAngle, Arc2D.Double.PIE);
        graphics.fill(corner);
        graphics.setColor(Color.GRAY); 
        
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
        
        for (Lane lane : lanes) {
            
            currentX = lane.getXStart(); 
            currentY = lane.getYStart();
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
            
            boolean currentTypeStraight;
            boolean positiveAngle;
            
            int concatValue = 0;            
            
            if (next.getAngle() == 0) {
                currentTypeStraight = true;
                concatValue = next.getLength();
                positiveAngle = next.getAngle() >= 0;
            } else {
                currentTypeStraight = false;
                positiveAngle = next.getAngle() >= 0;
                concatValue = next.getAngle();
            }
                    
            while ((next = next.getNextSegment()) != null) { // while more segments
                
                // we need to render the straight as we are now moving to corner
                if (next.getAngle() != 0 && currentTypeStraight) {
                    renderStraightToImage(graphics, angle, currentX, currentY, concatValue); 
                    
                    double startX = currentX - (Segment.WIDTH / 2);
                    double startY = currentY;
                    
                    currentX -= Math.sin((Math.PI * (angle/180.0))) * concatValue;
                    currentY += Math.cos((Math.PI * (angle/180.0))) * concatValue;
                    
                    double endX = currentX - (Segment.WIDTH / 2);
                    double endY = currentY;
                    
                    drawVehicles(graphics, angle, startX, startY, endX, endY);
                    
                    concatValue = next.getAngle();
                    currentTypeStraight = false;
                    positiveAngle = next.getAngle() >= 0;
                    continue;
                }
                
                // we need to render the corner as we are now moving to straight or a corner in the opposite direction
                if ((next.getAngle() == 0 && !currentTypeStraight) || next.getAngle() >= 0 != positiveAngle) {
                    double[] center = renderCornerToImage(graphics, angle, currentX, currentY, concatValue);                    
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
                
                if (next.getAngle() == 0 && currentTypeStraight) {
                    concatValue += next.getLength();
                    continue;
                }
                
                if (next.getAngle() != 0 && !currentTypeStraight) {
                    concatValue += next.getAngle();
                    continue;
                }
                
            }
        }

    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);        
        
        // no need to paint if we can't see it
        if (getWidth() == 0 || getHeight() == 0) return;
        
        // create cache image if necessary
        if (null == image
                || image.getWidth() != getWidth()
                || image.getHeight() != getHeight()) {
            renderToImage();
        }
        
        // redraw the junction from the image cache
        graphics.drawImage(image, 0, 0, null);        
        //drawVehicles(graphics);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
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
                double currentX = head.getRenderX();
                double currentY = head.getRenderY();
                int angle = head.getRenderAngle();                
                
                graphics.rotate((Math.PI * (angle/180.0)), currentX, currentY);
                graphics.fillRect((int) currentX - 13, (int) currentY - 46, 26, 46);
                graphics.rotate(-(Math.PI * (angle/180.0)), currentX, currentY);
                
            }
        }
    }

    private void renderStraightToImage(Graphics2D graphics, int angle, double x, double y, int length) {
        // convert the angle to radians
        double angleRadians = Math.PI * (angle/180.0);
        int width = Segment.WIDTH;
        int startX = (int) (x - (width / 2));
        int startY = (int) (y);
        graphics.rotate(angleRadians, x, y);
        graphics.fillRect(startX, startY, width, length);
        graphics.rotate(-angleRadians, x, y);
    }
    
    private void renderCornerToImage(Graphics2D graphics, int angle, double x, double y, int cornerAngle) {
        
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
        
        int startX = (int) centerX - Segment.WIDTH;
        int startY = (int) centerY - Segment.WIDTH;
        int boxSize = Segment.WIDTH * 2;
        int startAngle = (-angle) - 90;
        
        graphics.setColor(Color.DARK_GRAY); 
        graphics.fillArc(startX, startY, boxSize, boxSize, startAngle, cornerAngle);
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
            boolean currentTypeStraight;
            boolean positiveAngle;
            
            int concatValue = 0;            
            
            if (next.getAngle() == 0) {
                currentTypeStraight = true;
                concatValue = next.getLength();
                positiveAngle = true;
            } else {
                currentTypeStraight = false;
                positiveAngle = next.getAngle() >= 0;
                concatValue = next.getAngle();
            }
                    
            while ((next = next.getNextSegment()) != null) { // while more segments
                
                // we need to render the corner as we are now moving to straight
                if (next.getAngle() == 0 && !currentTypeStraight) {
                    renderCornerToImage(graphics, angle, currentX, currentY, concatValue);
                    // currentX = 
                    // currentY = 
                    concatValue = next.getLength();
                    return;
                }
                
                // we need to render the straight as we are now moving to corner
                if (next.getAngle() != 0 && currentTypeStraight) {
                    renderStraightToImage(graphics, angle, currentX, currentY, concatValue); 
                    currentX -= Math.sin((Math.PI * (angle/180.0))) * concatValue;
                    currentY += Math.cos((Math.PI * (angle/180.0))) * concatValue;
                    concatValue = next.getAngle();
                    currentTypeStraight = false;
                    continue;
                }
                
                // we need to do another corner because we changed from
                // clockwise to anti-clockwise or the reverse
                if (next.getAngle() != 0 && next.getAngle() >= 0 != positiveAngle) {
                    renderCornerToImage(graphics, angle, currentX, currentY, concatValue);
                    // currentX = 
                    // currentY = 
                    concatValue = next.getAngle();
                    continue;
                }
                
                if (next.getAngle() == 0 && currentTypeStraight) {
                    concatValue += next.getLength();
                    continue;
                }
                
                if (next.getAngle() != 0 && !currentTypeStraight) {
                    concatValue += next.getAngle();
                    currentTypeStraight = false;
                    positiveAngle = next.getAngle() >= 0;
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
        drawVehicles(graphics);
    }
}

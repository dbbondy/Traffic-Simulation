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
                double angle = head.getRenderAngle();
                
                
                graphics.rotate((Math.PI * (angle/180)), currentX, currentY);
                graphics.fillRect((int) currentX - 13, (int) currentY - 46, 26, 46);
                graphics.rotate(-(Math.PI * (angle/180)), currentX, currentY);
                
            }
        }
    }

    private void renderToImage() {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // create a graphics object from the buffered image object
        Graphics2D graphics = image.createGraphics();
        ArrayList<Lane> lanes = currentJunction.getLanes();         
        
        double angle;
        double currentX, currentY;
        
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF);
        
        graphics.setColor(Color.GRAY);   
        
        
        for (Lane lane : lanes) {
            // the rendering is from top/bottom left not from center
            currentX = lane.getXStart(); 
            currentY = lane.getYStart();
            angle = lane.getInitialAngle();
            
            boolean cornerLast = false;
            
            Segment next = lane.getFirstSegment();
            int x = 0;
            while (next != null) { // while more segments
                
                next.setRenderX(currentX);
                next.setRenderY(currentY);
                next.setRenderAngle(angle);
                
                // draw an angle segment
                if (next.getAngle() != 0) {
                    
                    graphics.setColor(Color.DARK_GRAY);
                    
                    if (next.getAngle() > 0) {
                        graphics.fillArc((int) (currentX - (Segment.WIDTH / 2)), (int) (currentY - Segment.WIDTH), 
                            Segment.WIDTH*2, Segment.WIDTH*2, (int) -angle, -next.getAngle());
                        angle += next.getAngle();
                    } else {
                        graphics.fillArc((int) (currentX - (Segment.WIDTH * 1.5)), (int) (currentY - Segment.WIDTH), 
                            Segment.WIDTH*2, Segment.WIDTH*2, 180 + (int) -angle, -next.getAngle());
                        angle += next.getAngle();
                    }
                    
                    cornerLast = true;
                    
                // draw a straight segment
                } else {
                    
                    if (cornerLast) {
                        // currentX = currentX + (Segment.WIDTH * Math.cos(90 - ((next.getAngle() + angle) % 360)));
                        // currentY = currentY + (Segment.WIDTH * Math.sin(90 - ((next.getAngle() + angle) % 360)));
                        graphics.setColor(Color.red);
                        graphics.fillRect((int) currentX - 1, (int) currentY - 1, 3, 3);
                        cornerLast = false;
                    }
                    
                    graphics.setColor(Color.gray);
                    
                    graphics.rotate((Math.PI * (angle/180)), currentX, currentY);
                    graphics.fillRect((int) (currentX - (Segment.WIDTH / 2)), (int) (currentY), Segment.WIDTH, next.getLength());
                    graphics.rotate(-(Math.PI * (angle/180)), currentX, currentY);
                    
                    currentX -= Math.sin((Math.PI * (angle/180))) * next.getLength();
                    currentY += Math.cos((Math.PI * (angle/180))) * next.getLength();
                }     
                next = next.getNextSegment();
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

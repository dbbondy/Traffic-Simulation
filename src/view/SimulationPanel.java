/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import model.junctions.Junction;


/**
 *
 * @author Dan
 */
public class SimulationPanel extends JPanel {

    private BufferedImage image;
    private boolean imageIsInvalid;

    public SimulationPanel() {
        super();
        initPanel();

    }

    private void initPanel() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.white);


        //System.out.println(getWidth());
    }

    private boolean imageIsInvalid() {
        return imageIsInvalid;
    }
    
    private void renderToImage(){
        
        // create a graphics object from the buffered image object
        Graphics2D graphics = image.createGraphics();
        Junction junc = (Junction)Simulation.getOption(Simulation.JUNCTION_TYPE);
        if(junc instanceof model.junctions.TwoLaneJunction){
            drawTwoLaneJunc(graphics);
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        }
        // draw the junction we want from that graphics object. 
        // image = 
    }

    
    private void drawTwoLaneJunc(Graphics graphics){
            //draw rectangles
            graphics.drawRect(350, 375, 75, 320); // bottom rect
            graphics.drawRect(350, 0, 75, 300); // top rect
            graphics.drawRect(425, 300, 375, 75); // right rect
            graphics.drawRect(0, 300, 350, 75); // left rect
            graphics.drawRect(350, 300, 75, 75); // junction centre
            graphics.setColor(Color.gray);
            //fill rectangles
            graphics.fillRect(350, 375, 75, 320);// fill bottom rect
            graphics.fillRect(350, 0, 75, 300); // fill top rect
            graphics.fillRect(425, 300, 375, 75); // fill right rect
            graphics.fillRect(0, 300, 350, 75); // fill left rect
            graphics.fillRect(350, 300, 75, 75); //fill junction centre
            //draw divider lines
            graphics.setColor(Color.BLUE);
            graphics.drawLine(387, 375, 387, 700); // divider line for bottom rect
            graphics.drawLine(387, 0, 387, 300); // divider for top rect
            graphics.drawLine(0, 337, 350, 337); // divider for right rect
            graphics.drawLine(425, 337, 800, 337); //divider for left rect
            //draw road dividers
            graphics.setColor(Color.white);
            graphics.drawLine(350, 375, 425, 375); // draw bottom rect dividers
            graphics.drawLine(350, 395, 425, 395);

            graphics.drawLine(350, 300, 425, 300); // draw top rect dividers
            graphics.drawLine(350, 280, 425, 280);

            graphics.drawLine(350, 300, 350, 375); // draw left rect dividers
            graphics.drawLine(330, 300, 330, 375);

            graphics.drawLine(425, 300, 425, 375); // draw right rect dividers
            graphics.drawLine(445, 300, 445, 375);
    }
    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        // create cache image if necessary
        if (null == image
                || image.getWidth() != getWidth()
                || image.getHeight() != getHeight()) {
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            imageIsInvalid = true;
        }

        // render to cache if needed
        if (imageIsInvalid()) {
            renderToImage();
        }

        // redraw component from cache
        // TODO: take the clip into account
        g.drawImage(image, this.getWidth(), this.getHeight(), null);

        Junction junc = (Junction) Simulation.getOption(Simulation.JUNCTION_TYPE);
        if (junc instanceof model.junctions.TwoLaneJunction) {
           drawTwoLaneJunc(graphics);
        }

        if (junc instanceof model.junctions.RoundaboutJunction) {
            graphics.drawRect(300, 300, 100, 200);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(300, 300, 100, 200);
        }



    }
}

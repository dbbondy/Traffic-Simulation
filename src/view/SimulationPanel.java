package view;

import controller.Simulation;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
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
 * Class for handling the rendering of junctions and other graphics primitives key to the simulation
 *
 * @author Daniel Bond
 * @author Jonathan Pike ( contact: mats@staite.net )
 */
public class SimulationPanel extends JPanel {

    private BufferedImage image; //the junction image
    private Junction currentJunction; // the current junction we are working with
    public static final int WIDTH = 800; // the width of the panel
    public static final int HEIGHT = 680; // the height of the panel
    private Dimension size;
    private ArrayList<PostRenderGraphic> shapesForPostRender; // collection of shapes we want to render again after a rendering cycle has been performed

    public SimulationPanel() {
        initPanel();
    }

    /**
     * Initialise the panel
     */
    private void initPanel() {
        size = new Dimension(WIDTH, HEIGHT);
        this.setBackground(new Color(10 * 16 + 5, 13 * 16 + 6, 10 * 16 + 3)); // 5B8059   A5 D6 A3    A=10
        this.shapesForPostRender = new ArrayList<>();
    }

    /**
     * Gets the preferred size of the panel
     *
     * @return the {@link java.awt.Dimension} object representing the preferred size of this panel
     */
    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    /**
     * Gets the maximum size of the panel
     *
     * @return the {@link java.awt.Dimension} object representing the maximum size of this panel
     */
    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    /**
     * Gets the minimum size of the panel
     *
     * @return the {@link java.awt.Dimension} object representing the minimum size of this panel
     */
    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    /**
     * Draws the vehicles onto the screen
     *
     * @param g the graphics object we want to use to draw
     */
    private synchronized void drawVehicles(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        ArrayList<Lane> lanes = currentJunction.getLanes();
        for (Lane l : lanes) {
            for (Vehicle v : l.getVehicles()) {
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
                Shape rect = new RoundRectangle2D.Double(startX, startY, v.getWidth(), v.getLength(), 8, 8);
                graphics.fill(rect);
                graphics.rotate(-(Math.PI * (vehicleAngle / 180.0)), vehicleX, vehicleY);
            }
        }
    }

    /**
     * Renders a section of road that is straight, to the junction
     *
     * @param graphics the graphics object we will use for the rendering
     * @param angle the angle that the straight section will have
     * @param x the x-coordinate of the straight section
     * @param y the y-coordinate of the straight section
     * @param length the length of the straight section
     */
    private void renderStraightToImage(Graphics2D graphics, int angle, double x, double y, int length) {

        // convert the angle to radians
        double angleRadians = Math.PI * (angle / 180.0);
        int width = Segment.WIDTH;

        // x is the "bottom center" and startX is the "bottom left". 
        double startX = (x - (width / 2));
        double startY = (y);

        // rotate the canvas about the "bottom center" of the section
        graphics.rotate(angleRadians, x, y);

        Shape straight = new Rectangle2D.Double(startX, startY, width, length + 1);
        graphics.fill(straight);

        Shape lineLeft = new Line2D.Double(startX, startY, startX, startY + length);
        Shape lineRight = new Line2D.Double(startX + width, startY, startX + width, startY + length);
        this.shapesForPostRender.add(new PostRenderGraphic(lineLeft, angleRadians, x, y));
        this.shapesForPostRender.add(new PostRenderGraphic(lineRight, angleRadians, x, y));

        // undo rotation of canvas
        graphics.rotate(-angleRadians, x, y);
    }

    /**
     * Renders a section that is curved, to the screen
     *
     * @param graphics the graphics object we will use for rendering
     * @param angle the angle that the corner will be drawn from
     * @param x the x-coordinate of the corner
     * @param y the y-coordinate of the corner
     * @param cornerAngle the angle that the corner will extend for
     * @return the x and y coordinates of the center of the corner as a 2 element array
     */
    private double[] renderCornerToImage(Graphics2D graphics, int angle, double x, double y, int cornerAngle) {

        // convert the angle to radians
        double angleRadians = Math.PI * (angle / 180.0);
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

        // fix for bad corner anti aliasing / inaccuracy
        double extraCornerAngle = cornerAngle >= 0 ? cornerAngle + 1 : cornerAngle - 1;

        Rectangle2D bounds = new Rectangle2D.Double(startX, startY, boxSize, boxSize);
        Shape corner = new Arc2D.Double(bounds, -startAngle, -extraCornerAngle, Arc2D.Double.PIE);
        graphics.fill(corner);

        Shape cornerOuter = new Arc2D.Double(bounds, -startAngle, -cornerAngle, Arc2D.Double.OPEN);
        this.shapesForPostRender.add(new PostRenderGraphic(cornerOuter, 0, 0, 0));

        // return the center co-ords so we can calculate
        // the new currentX currentY outside 
        return new double[]{centerX, centerY};
    }

    /**
     * Render the junction to an image
     */
    private void renderToImage() {

        // clear from post render
        this.shapesForPostRender.clear();

        // we reload the current junction every time we have to render it again
        currentJunction = (Junction) Simulation.getOption(Simulation.JUNCTION_TYPE);

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
                    currentX -= Math.sin((Math.PI * (angle / 180.0))) * concatValue;
                    currentY += Math.cos((Math.PI * (angle / 180.0))) * concatValue;

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
                    double zaRadians = (Math.PI * (za / 180.0));
                    currentX = center[0] + (Math.cos(zaRadians) * (Segment.WIDTH / 2));
                    currentY = center[1] - (Math.sin(zaRadians) * (Segment.WIDTH / 2));

                    angle += concatValue;
                    concatValue = next.getLength();
                    currentTypeStraight = next.getAngle() == 0;
                    positiveAngle = next.getAngle() >= 0;

                    next.setRenderX(currentX);
                    next.setRenderY(currentY);
                    next.setRenderAngle(angle);

                    continue;
                }

                // step over additional segments that make up 
                // the straight section currently being processed
                if (next.getAngle() == 0 && currentTypeStraight) {
                    double renderX = currentX - Math.sin((Math.PI * (angle / 180.0))) * concatValue;
                    double renderY = currentY + Math.cos((Math.PI * (angle / 180.0))) * concatValue;
                    next.setRenderX(renderX);
                    next.setRenderY(renderY);
                    next.setRenderAngle(angle);
                    concatValue += next.getLength();
                    continue;
                }

                // as above (for a corner)
                if (next.getAngle() != 0 && !currentTypeStraight) {
                    double angleRadians = Math.PI * (angle / 180.0);
                    double centerX, centerY;

                    if (concatValue >= 0) { // clockwise direction
                        centerX = currentX - Math.cos(angleRadians) * (Segment.WIDTH / 2);
                        centerY = currentY - Math.sin(angleRadians) * (Segment.WIDTH / 2);
                    } else { // anti-clockwise direction
                        centerX = currentX + Math.cos(angleRadians) * (Segment.WIDTH / 2);
                        centerY = currentY + Math.sin(angleRadians) * (Segment.WIDTH / 2);
                    }

                    double za = 360 - (concatValue + (concatValue < 0 ? angle + 180 : angle));
                    double zaRadians = (Math.PI * (za / 180.0));
                    double renderX = centerX + (Math.cos(zaRadians) * (Segment.WIDTH / 2));
                    double renderY = centerY - (Math.sin(zaRadians) * (Segment.WIDTH / 2));
                    int renderAngle = angle + concatValue;
                    next.setRenderX(renderX);
                    next.setRenderY(renderY);
                    next.setRenderAngle(renderAngle);
                    concatValue += next.getAngle();
                    continue;
                }

            }

            last.setNextSegment(null);
        }

        graphics.setColor(new Color(80, 80, 80));
        for (PostRenderGraphic over : this.shapesForPostRender) {
            if (over.renderAngle != 0) {
                graphics.rotate(over.renderAngle, over.rotateX, over.rotateY);
                graphics.draw(over.shape);
                graphics.rotate(-over.renderAngle, over.rotateX, over.rotateY);
            } else {
                graphics.draw(over.shape);
            }
        }

        /* TEST CODE FOR REVIEWING CONNECTIONS AT INTERSECTION
         * int colorIndex = 0;
         *
         * Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.ORANGE, Color.PINK, Color.WHITE, new Color(10*16+12, 4*16, 12*16+7) };
         *
         * for (Lane lane : lanes) {
         *
         * Segment next = lane.getFirstSegment();
         *
         * while (true) {
         *
         * Set<Segment> segments = next.getConnectedSegments().keySet();
         *
         * for (Segment segment : segments) { ConnectionType ct = next.getConnectedSegments().get(segment); if (ct != ConnectionType.OVERLAP) continue; graphics.setColor(colors[colorIndex]); int x =
         * (int) next.getRenderX(); int y = (int) next.getRenderY(); graphics.fillRect(x-2, y-2, 4, 4); x = (int) segment.getRenderX(); y = (int) segment.getRenderY(); graphics.fillRect(x-2, y-2, 4,
         * 4); colorIndex++; }
         *
         * // no more segments so we exit the loop if ((next = next.getNextSegment()) == null) break;
         *
         * }
         *
         * }
         */
    }

    /**
     * Clears the cache for the current junction and the buffered image from this class
     */
    public synchronized void clearCache() {
        currentJunction = null;
        image = null;
    }

    /**
     * Main painting method
     *
     * @param graphics1 the graphics object we will use to render the junction
     */
    @Override
    public synchronized void paintComponent(Graphics graphics1) {

        // prevent AI while rendering
        synchronized (Simulation.class) {

            super.paintComponent(graphics1);

            // no need to paint if we can't see it
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }

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

            if (Simulation.isPaused()) {

                graphics.setColor(new Color(1 * 16 + 13, 4 * 16 + 0, 1 * 16 + 13)); // 1D401D
                graphics.fillRect(this.getWidth() - 50, 20, 10, 40);
                graphics.fillRect(this.getWidth() - 30, 20, 10, 40);
            }

        }
    }

    /**
     * Inner class that allows us to store shapes that require extra rendering attention in the junction
     * @author Daniel Bond
     */
    private class PostRenderGraphic {

        public Shape shape; // the shape we want to render again
        public double renderAngle; // the angle at which we will render the shape
        public double rotateX; // the x-coordinate at which we will render the shape
        public double rotateY; // the y-coordinate at which we will render the shape

        public PostRenderGraphic(Shape shape, double renderAngle, double rotateX, double rotateY) {
            this.shape = shape;
            this.renderAngle = renderAngle;
            this.rotateX = rotateX;
            this.rotateY = rotateY;
        }
    }
}
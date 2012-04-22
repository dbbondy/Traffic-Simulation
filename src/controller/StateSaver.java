package controller;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.*;
import model.junctions.Junction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class for saving the state of a simulation to a file
 * @author Daniel Bond
 */
public class StateSaver {

    /**
     * Save the state of the simulation to a file
     * @param file the file to save the data to
     * @throws Exception 
     */
    public static void saveState(File file) throws Exception {
        
        //prevent processing whilst we save the file
        synchronized (Simulation.class) {

            // We only need a fraction of the information stored by Java
            // so we manually save the important bits as xml. 

            // Note that if we tried to serialize using standard means
            // we would have thousands of segments that needed storing. 

            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElm = doc.createElement("trafficSimulation");
            Element vehiclesElm = doc.createElement("vehicles");
            Element detailsElm = doc.createElement("details");

            rootElm.appendChild(vehiclesElm);
            rootElm.appendChild(detailsElm);

            Junction junc = (Junction) Simulation.getOption(Simulation.JUNCTION_TYPE);
            ArrayList<Vehicle> vehicles = junc.getVehicles();

            for (Vehicle vehicle : vehicles) { // for all vehicles in the junction

                // store the relevant information 
                Segment segment = vehicle.getHeadSegment();
                Lane lane = segment.getLane();

                String type = "vehicle";

                if (vehicle instanceof Car)
                    type = "car";
                if (vehicle instanceof Truck)
                    type = "truck";

                // create elements for the values
                Element vehicleElm = doc.createElement(type);
                Element widthElm = doc.createElement("width");
                Element lengthElm = doc.createElement("length");
                Element colorElm = doc.createElement("color");
                Element speedElm = doc.createElement("speed");        
                Element segmentElm = doc.createElement("segment");
                Element laneElm = doc.createElement("lane");

                // store the values from each individual vehicle
                String width = Integer.toString(vehicle.getWidth());
                String length = Integer.toString(vehicle.getLength());
                String color = Integer.toString(vehicle.getColor().getRGB());
                String speed = Integer.toString(vehicle.getSpeed());
                String segmentIndex = Integer.toString(lane.getLaneSegments().indexOf(segment));
                String laneIndex = Integer.toString(junc.getLanes().indexOf(lane));

                // append the data to the vehicle element
                vehicleElm.appendChild(widthElm);
                vehicleElm.appendChild(lengthElm);
                vehicleElm.appendChild(colorElm);
                vehicleElm.appendChild(speedElm);
                vehicleElm.appendChild(segmentElm);
                vehicleElm.appendChild(laneElm);

                // set the content of each of element.
                widthElm.setTextContent(width);
                lengthElm.setTextContent(length);
                colorElm.setTextContent(color);
                speedElm.setTextContent(speed);
                segmentElm.setTextContent(segmentIndex);
                laneElm.setTextContent(laneIndex);
                
                // append the singular vehicle element to the plural vehicles element as a child
                vehiclesElm.appendChild(vehicleElm);

            }
            
            // get the values from the environment variables and the count of vehicles passed through the junction
            String timeStep = ((Integer) Simulation.getOption(Simulation.TIME_STEP)).toString();
            String junction = ((Junction) Simulation.getOption(Simulation.JUNCTION_TYPE)).toString();
            String minDensity = ((Integer) Simulation.getOption(Simulation.MIN_DENSITY)).toString();
            String maxDensity = ((Integer) Simulation.getOption(Simulation.MAX_DENSITY)).toString();
            String aggression = ((Integer) Simulation.getOption(Simulation.AGGRESSION)).toString();
            String carRatio = ((Integer) Simulation.getOption(Simulation.CAR_RATIO)).toString();
            String truckRatio = ((Integer) Simulation.getOption(Simulation.TRUCK_RATIO)).toString();
            String maxSpeed = ((Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED)).toString();
            String carCount = Integer.toString(SimulationStats.getCarCount());
            String truckCount = Integer.toString(SimulationStats.getTruckCount());

            // create elements for these values
            Element timeStepElm = doc.createElement(Simulation.TIME_STEP);
            Element junctionElm = doc.createElement(Simulation.JUNCTION_TYPE);
            Element minDensityElm = doc.createElement(Simulation.MIN_DENSITY);
            Element maxDensityElm = doc.createElement(Simulation.MAX_DENSITY);
            Element aggressionElm = doc.createElement(Simulation.AGGRESSION);
            Element carRatioElm = doc.createElement(Simulation.CAR_RATIO);
            Element truckRatioElm = doc.createElement(Simulation.TRUCK_RATIO);
            Element maxSpeedElm = doc.createElement(Simulation.MAXIMUM_SPEED);
            Element carCountElm = doc.createElement("carCount");
            Element truckCountElm = doc.createElement("truckCount");

            // set the text content of these values
            timeStepElm.setTextContent(timeStep);
            junctionElm.setTextContent(junction);
            minDensityElm.setTextContent(minDensity);
            maxDensityElm.setTextContent(maxDensity);
            aggressionElm.setTextContent(aggression);
            carRatioElm.setTextContent(carRatio);
            truckRatioElm.setTextContent(truckRatio);
            maxSpeedElm.setTextContent(maxSpeed);
            carCountElm.setTextContent(carCount);
            truckCountElm.setTextContent(truckCount);

            // we then append the values to the details element
            detailsElm.appendChild(timeStepElm);
            detailsElm.appendChild(junctionElm);
            detailsElm.appendChild(minDensityElm);
            detailsElm.appendChild(maxDensityElm);
            detailsElm.appendChild(aggressionElm);
            detailsElm.appendChild(carRatioElm);
            detailsElm.appendChild(truckRatioElm);
            detailsElm.appendChild(maxSpeedElm);
            detailsElm.appendChild(carCountElm);
            detailsElm.appendChild(truckCountElm);

            doc.appendChild(rootElm);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            
        }
        
    }
}

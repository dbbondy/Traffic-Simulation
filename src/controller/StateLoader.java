package controller;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import model.*;
import model.junctions.Junction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for loading a state file into the simulation
 * @author Daniel Bond
 */
public class StateLoader {

    /**
     * Loads the state of a simulation into the program
     * @param file the file to load in.
     * @throws Exception 
     */
    public static void loadState(File file) throws Exception {
       
        //prevent processing whilst we load the file
        synchronized (Simulation.class) { 
        
            Simulation.reset();
            
            if (!Simulation.isPaused()) // if not paused
                Simulation.pause();  // pause the simulation
            
            Simulation.start(); 

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            ArrayList<Vehicle> vehicles = new ArrayList<>();
            Junction junc = null;

            NodeList detailsNodeList = doc.getElementsByTagName("details").item(0).getChildNodes(); // get all children of a element named "details"

            for (int i = 0; i < detailsNodeList.getLength(); i++) {

                Node detailNode = detailsNodeList.item(i);

                switch (detailNode.getNodeName()) {
                    case "carCount": 
                        int carCount = Integer.parseInt(detailNode.getTextContent().trim());
                        SimulationStats.setCarCount(carCount);
                        break;
                    case "truckCount":
                        int truckCount = Integer.parseInt(detailNode.getTextContent().trim());
                        SimulationStats.setTruckCount(truckCount);
                        break;
                    case Simulation.JUNCTION_TYPE:
                        junc = (Junction) Junction.getJunctionTypeByName(detailNode.getTextContent().trim()).newInstance();
                        Simulation.setOption(Simulation.JUNCTION_TYPE, junc);
                        break;
                    default:
                        if (Simulation.getOption(detailNode.getNodeName()) != null) {
                            int value = Integer.parseInt(detailNode.getTextContent().trim());
                            Simulation.setOption(detailNode.getNodeName(), value);
                        }
                }
                System.out.println("");
            }

            NodeList vehiclesNodeList = doc.getElementsByTagName("vehicles").item(0).getChildNodes(); // gets all children of an element named "vehicles"

            for (int i = 0; i < vehiclesNodeList.getLength(); i++) {

                Node vehicleNode = vehiclesNodeList.item(i);
                if (vehicleNode.getNodeType() != Node.ELEMENT_NODE) continue; 

                switch (vehicleNode.getNodeName()) { // switch on each node name of the node list
                    case "car": vehicles.add(new Car()); break;
                    case "truck": vehicles.add(new Truck()); break;
                    default: continue;
                }

            }

            for (int i = 0; i < vehiclesNodeList.getLength(); i++) {          

                Node vehicleNode = vehiclesNodeList.item(i);
                if (vehicleNode.getNodeType() != Node.ELEMENT_NODE) continue;
                Element vehicleElm = (Element) vehicleNode;

                Vehicle vehicle = vehicles.get(i);
                // get the values of the variables we need to construct the simulation again.
                String widthStr = vehicleElm.getElementsByTagName("width").item(0).getTextContent().trim();
                String lengthStr = vehicleElm.getElementsByTagName("length").item(0).getTextContent().trim();
                String colorStr = vehicleElm.getElementsByTagName("color").item(0).getTextContent().trim();
                String speedStr = vehicleElm.getElementsByTagName("speed").item(0).getTextContent().trim();
                String segmentStr = vehicleElm.getElementsByTagName("segment").item(0).getTextContent().trim();
                String laneStr = vehicleElm.getElementsByTagName("lane").item(0).getTextContent().trim();

                // parse them
                int width = Integer.parseInt(widthStr);
                int length = Integer.parseInt(lengthStr);
                int speed = Integer.parseInt(speedStr);
                int segmentIndex = Integer.parseInt(segmentStr);
                int laneIndex = Integer.parseInt(laneStr);

                Color color = new Color(Integer.parseInt(colorStr));

                
                vehicle.setDimensions(width, length);
                vehicle.setColor(color);
                vehicle.setSpeed(speed);

                Lane lane = junc.getLanes().get(laneIndex);
                Segment segment = lane.getLaneSegments().get(segmentIndex);
                vehicle.setHeadSegment(segment);

                lane.addVehicle(vehicle);

            }
            
            junc.updateNumberOfVehicles();
            
        }
        
    }
}

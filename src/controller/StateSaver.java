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

public class StateSaver {

    public static void saveState(File file) throws Exception {
        
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

            for (Vehicle vehicle : vehicles) {

                Segment segment = vehicle.getHeadSegment();
                Lane lane = segment.getLane();

                String type = "vehicle";

                if (vehicle instanceof Car) type = "car";
                if (vehicle instanceof Truck) type = "truck";

                Element vehicleElm = doc.createElement(type);
                Element widthElm = doc.createElement("width");
                Element lengthElm = doc.createElement("length");
                Element colorElm = doc.createElement("color");
                Element speedElm = doc.createElement("speed");
                Element aheadElm = doc.createElement("ahead");
                Element behindElm = doc.createElement("behind");            
                Element segmentElm = doc.createElement("segment");
                Element laneElm = doc.createElement("lane");

                System.out.println(segment.id());

                String width = Integer.toString(vehicle.getWidth());
                String length = Integer.toString(vehicle.getLength());
                String color = Integer.toString(vehicle.getColor().getRGB());
                String speed = Integer.toString(vehicle.getSpeed());
                String ahead = Integer.toString(vehicles.indexOf(vehicle.getVehicleInFront()));
                String behind = Integer.toString(vehicles.indexOf(vehicle.getVehicleBehind()));
                String segmentIndex = Integer.toString(lane.getLaneSegments().indexOf(segment));
                String laneIndex = Integer.toString(junc.getLanes().indexOf(lane));

                vehicleElm.appendChild(widthElm);
                vehicleElm.appendChild(lengthElm);
                vehicleElm.appendChild(colorElm);
                vehicleElm.appendChild(speedElm);
                vehicleElm.appendChild(aheadElm);
                vehicleElm.appendChild(behindElm);
                vehicleElm.appendChild(segmentElm);
                vehicleElm.appendChild(laneElm);

                widthElm.setTextContent(width);
                lengthElm.setTextContent(length);
                colorElm.setTextContent(color);
                speedElm.setTextContent(speed);
                aheadElm.setTextContent(ahead);
                behindElm.setTextContent(behind);
                segmentElm.setTextContent(segmentIndex);
                laneElm.setTextContent(laneIndex);

                vehiclesElm.appendChild(vehicleElm);

            }

            String timeStep = ((Integer) Simulation.getOption(Simulation.TIME_STEP)).toString();
            String junction = ((Junction) Simulation.getOption(Simulation.JUNCTION_TYPE)).toString();
            String minDensity = ((Integer) Simulation.getOption(Simulation.MIN_DENSITY)).toString();
            String maxDensity = ((Integer) Simulation.getOption(Simulation.MAX_DENSITY)).toString();
            String aggression = ((Integer) Simulation.getOption(Simulation.AGGRESSION)).toString();
            String carRatio = ((Integer) Simulation.getOption(Simulation.CAR_RATIO)).toString();
            String truckRatio = ((Integer) Simulation.getOption(Simulation.TRUCK_RATIO)).toString();
            String carCount = Integer.toString(SimulationStats.getCarCount());
            String truckCount = Integer.toString(SimulationStats.getTruckCount());

            Element timeStepElm = doc.createElement(Simulation.TIME_STEP);
            Element junctionElm = doc.createElement(Simulation.JUNCTION_TYPE);
            Element minDensityElm = doc.createElement(Simulation.MIN_DENSITY);
            Element maxDensityElm = doc.createElement(Simulation.MAX_DENSITY);
            Element aggressionElm = doc.createElement(Simulation.AGGRESSION);
            Element carRatioElm = doc.createElement(Simulation.CAR_RATIO);
            Element truckRatioElm = doc.createElement(Simulation.TRUCK_RATIO);
            Element carCountElm = doc.createElement("carCount");
            Element truckCountElm = doc.createElement("truckCount");

            timeStepElm.setTextContent(timeStep);
            junctionElm.setTextContent(junction);
            minDensityElm.setTextContent(minDensity);
            maxDensityElm.setTextContent(maxDensity);
            aggressionElm.setTextContent(aggression);
            carRatioElm.setTextContent(carRatio);
            truckRatioElm.setTextContent(truckRatio);
            carCountElm.setTextContent(carCount);
            truckCountElm.setTextContent(truckCount);

            detailsElm.appendChild(timeStepElm);
            detailsElm.appendChild(junctionElm);
            detailsElm.appendChild(minDensityElm);
            detailsElm.appendChild(maxDensityElm);
            detailsElm.appendChild(aggressionElm);
            detailsElm.appendChild(carRatioElm);
            detailsElm.appendChild(truckRatioElm);
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

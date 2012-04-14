
import model.Car;
import model.Lane;
import model.RoadDesigner;
import model.TurnDirection;

/**
 *
 * @author Dan
 */
public class TestMain {

    public static void main(String[] args) {
        Lane l = new Lane(0, 0, 0, TurnDirection.LEFT);
        RoadDesigner.buildLane(100, l);
        Car c = new Car();
        c.setHeadSegment(l.getFirstSegment().getNextSegment());
        l.addVehicle(c);
        System.out.println(l.getVehicleBehind(l.getFirstSegment().getNextSegment()));
        
    }
}

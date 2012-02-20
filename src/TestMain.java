
import model.RoadDesigner;
import model.Segment;

/**
 *
 * @author Dan
 */
public class TestMain {

    public static void main(String[] args) {
        double x = 300;
        System.out.println("x is: " + x);
        for(int i = 0; i < 18; i++){
            x = x + Math.sin(5);
            double result = x + Math.sin(5);
            System.out.println("result is: " + result);
        }
        
      
      
    }

}


import controller.Simulation;
import javax.swing.UIManager;

/**
 *
 * @author Dan
 */
public class TrafficSimulationMVC {
 
    
    public static void main(String[] args) {
        
        try
        {
            // set the system look and feel and then ask for update
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        Simulation.init();
        
    }
    
}

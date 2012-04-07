
import controller.Simulation;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;

/**
 *
 * @author Dan
 */
public class TrafficSimulationMVC {
 
    
    public static void main(String[] args) throws Exception {
        
        // set the system look and feel and then ask for update
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());        
        Simulation.init();
        
    }
    
}

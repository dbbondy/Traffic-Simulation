
package view;

import controller.Simulation;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;
import model.SimulationStats;

/**
 * Class for the panel of details explaining what the values of the simulation currently are.
 * @author Daniel Bond
 */
public class DetailsPanel extends JPanel {

    private JLabel vehicleMinDensityDetail; // create labels for the details
    private JLabel vehicleMaxDensityDetail;
    private JLabel carAggressiveDetail;
    private JLabel ratioCarsDetail;
    private JLabel ratioTrucksDetail;
    private JLabel maximumSpeedDetail;
    private JLabel timeDetail;
    private JLabel carsTr;
    private JLabel trucksTr;
    
    private JLabel editing; // for use when we use advanced keyboard editing of JLabel values
    private static final String KEYBOARD_LABEL_DEFAULT_VALUE = "      ";

    public DetailsPanel() {
        super();
        initComponents();
        this.setBackground(new Color(250, 250, 250));
        this.setMaximumSize(new Dimension(220, 200));
        this.setMinimumSize(new Dimension(220, 0));
        this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    }

    /**
     * Initialise the components of this panel
     */
    private void initComponents() {
        
        // create the detail labels and add them to panels along with their values
        JLabel vehicleMinDensityDetailL = new JLabel("Vehicle Minimum Density");
        vehicleMinDensityDetail = new JLabel(Simulation.getOption(Simulation.MIN_DENSITY).toString());
        JPanel vehicleMinDensityDetailP = new JPanel();
        vehicleMinDensityDetailP.setBackground(null);
        vehicleMinDensityDetailP.setLayout(new BorderLayout());
        vehicleMinDensityDetailP.add(vehicleMinDensityDetailL, BorderLayout.LINE_START);
        vehicleMinDensityDetailP.add(vehicleMinDensityDetail, BorderLayout.LINE_END);
        
        JLabel vehicleMaxDensityDetailL = new JLabel("Vehicle Maximum Density");
        vehicleMaxDensityDetail = new JLabel(Simulation.getOption(Simulation.MAX_DENSITY).toString());
        JPanel vehicleMaxDensityDetailP = new JPanel();
        vehicleMaxDensityDetailP.setBackground(null);
        vehicleMaxDensityDetailP.setLayout(new BorderLayout());
        vehicleMaxDensityDetailP.add(vehicleMaxDensityDetailL, BorderLayout.LINE_START);
        vehicleMaxDensityDetailP.add(vehicleMaxDensityDetail, BorderLayout.LINE_END);
        
        JLabel carAggressiveDetailL = new JLabel("Vehicle Aggression"); 
        carAggressiveDetail = new JLabel(Simulation.getOption(Simulation.AGGRESSION).toString());
        JPanel carAggressiveDetailP = new JPanel();
        carAggressiveDetailP.setBackground(null);
        carAggressiveDetailP.setLayout(new BorderLayout());
        carAggressiveDetailP.add(carAggressiveDetailL, BorderLayout.LINE_START);
        carAggressiveDetailP.add(carAggressiveDetail, BorderLayout.LINE_END);
        
        JLabel ratioCarsDetailL = new JLabel("Ratio of Cars");
        ratioCarsDetail = new JLabel(Simulation.getOption(Simulation.CAR_RATIO).toString());
        JPanel ratioCarsDetailP = new JPanel();
        ratioCarsDetailP.setBackground(null);
        ratioCarsDetailP.setLayout(new BorderLayout());
        ratioCarsDetailP.add(ratioCarsDetailL, BorderLayout.LINE_START);
        ratioCarsDetailP.add(ratioCarsDetail, BorderLayout.LINE_END);
        
        JLabel ratioTrucksDetailL = new JLabel("Ratio of Trucks");
        ratioTrucksDetail = new JLabel(Simulation.getOption(Simulation.TRUCK_RATIO).toString());
        JPanel ratioTrucksDetailP = new JPanel();
        ratioTrucksDetailP.setBackground(null);
        ratioTrucksDetailP.setLayout(new BorderLayout());
        ratioTrucksDetailP.add(ratioTrucksDetailL, BorderLayout.LINE_START);
        ratioTrucksDetailP.add(ratioTrucksDetail, BorderLayout.LINE_END);
        
        JLabel maximumSpeedDetailL = new JLabel("Maximum Speed");
        maximumSpeedDetail = new JLabel(Simulation.getOption(Simulation.MAXIMUM_SPEED).toString());
        JPanel maximumSpeedDetailP = new JPanel();
        maximumSpeedDetailP.setBackground(null);
        maximumSpeedDetailP.setLayout(new BorderLayout());
        maximumSpeedDetailP.add(maximumSpeedDetailL, BorderLayout.LINE_START);
        maximumSpeedDetailP.add(maximumSpeedDetail, BorderLayout.LINE_END);
        
        JLabel timeDetailL = new JLabel("Time Step");
        timeDetail = new JLabel(Simulation.getOption(Simulation.TIME_STEP).toString());
        JPanel timeDetailP = new JPanel();
        timeDetailP.setBackground(null);
        timeDetailP.setLayout(new BorderLayout());
        timeDetailP.add(timeDetailL, BorderLayout.LINE_START);
        timeDetailP.add(timeDetail, BorderLayout.LINE_END);
        
        JLabel carsTrL = new JLabel("Cars Travelled");
        carsTr = new JLabel(Integer.toString(SimulationStats.getCarCount()));
        JPanel carsTrP = new JPanel();
        carsTrP.setBackground(null);
        carsTrP.setLayout(new BorderLayout());
        carsTrP.add(carsTrL, BorderLayout.LINE_START);
        carsTrP.add(carsTr, BorderLayout.LINE_END);
        
        JLabel trucksTrL = new JLabel("Trucks Travelled");
        trucksTr = new JLabel(Integer.toString(SimulationStats.getTruckCount()));
        JPanel trucksTrP = new JPanel();
        trucksTrP.setBackground(null);
        trucksTrP.setLayout(new BorderLayout());
        trucksTrP.add(trucksTrL, BorderLayout.LINE_START);
        trucksTrP.add(trucksTr, BorderLayout.LINE_END);
        
        vehicleMinDensityDetail.setName(Simulation.MIN_DENSITY);
        vehicleMaxDensityDetail.setName(Simulation.MAX_DENSITY);
        carAggressiveDetail.setName(Simulation.AGGRESSION);
        ratioCarsDetail.setName(Simulation.CAR_RATIO);
        ratioTrucksDetail.setName(Simulation.TRUCK_RATIO);
        maximumSpeedDetail.setName(Simulation.MAXIMUM_SPEED);
        timeDetail.setName(Simulation.TIME_STEP); 
        
        Border padding = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        
        // set the borders of the detail labels
        vehicleMinDensityDetailP.setBorder(padding);
        vehicleMaxDensityDetailP.setBorder(padding);
        carAggressiveDetailP.setBorder(padding);
        ratioCarsDetailP.setBorder(padding);
        ratioTrucksDetailP.setBorder(padding);
        maximumSpeedDetailP.setBorder(padding);
        timeDetailP.setBorder(padding);
        carsTrP.setBorder(padding);
        trucksTrP.setBorder(padding);
        
        JSeparator seperator = new JSeparator(JSeparator.HORIZONTAL);
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        // add the components
        this.add(vehicleMinDensityDetailP);
        this.add(vehicleMaxDensityDetailP);
        this.add(carAggressiveDetailP);
        this.add(ratioCarsDetailP);
        this.add(ratioTrucksDetailP);
        this.add(maximumSpeedDetailP);
        this.add(timeDetailP);
        this.add(Box.createVerticalStrut(6));
        this.add(seperator);
        this.add(Box.createVerticalStrut(6));
        this.add(carsTrP);
        this.add(trucksTrP);
        
        // add ability to edit some of the labels from the keyboard
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                synchronized (Simulation.class) {
                    if (e.getID() == KeyEvent.KEY_PRESSED && editing != null) { // if we pressed a key and there is no label being currently edited
                        if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') { 
                            if (editing.getText().equals(KEYBOARD_LABEL_DEFAULT_VALUE)) editing.setText("");
                            editing.setText(editing.getText() + e.getKeyChar()); // set the text that we input
                            e.consume();
                            return true;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // if we pressed enter
                            finishEditing(); // stop editing the label
                            e.consume();
                            return true;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { // if we wanted to delete a character
                            if(editing.getText().length() == 0){
                                editing.setText(KEYBOARD_LABEL_DEFAULT_VALUE);
                                e.consume();
                                return true;
                            }
                            if (!editing.getText().equals(KEYBOARD_LABEL_DEFAULT_VALUE)) {
                                if (editing.getText().length() == 1) { // if there was only one character to begin with
                                    editing.setText(KEYBOARD_LABEL_DEFAULT_VALUE); // go back to default value
                                    e.consume();
                                    return true;
                                } else { // else we just delete the end character of the string
                                    editing.setText(editing.getText().substring(0, editing.getText().length()-2));
                                    e.consume();
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
        });
        // create the mouse listener so that we can click on the labels and edit the values
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!Simulation.isPaused()) // if we weren't paused
                        Simulation.pause(); // pause it
                    finishEditing();
                    editing = (JLabel) e.getSource();
                    editing.setBackground(new Color(10*16+1, 14*16+6, 15*16+7)); // a1d6f7
                    editing.setOpaque(true);
                    editing.setText(KEYBOARD_LABEL_DEFAULT_VALUE);
                }
            }
        };
        // add the mouse listener to the appropriate labels we want to be able to edit
        vehicleMaxDensityDetail.addMouseListener(ml);
        vehicleMinDensityDetail.addMouseListener(ml);
        carAggressiveDetail.addMouseListener(ml);
        ratioCarsDetail.addMouseListener(ml);
        ratioTrucksDetail.addMouseListener(ml);
        maximumSpeedDetail.addMouseListener(ml);
        
    }
    
    /**
     * Sets the values of the labels to the environment variables so the changes come into effect
     */
    public void finishEditing(){
        if (editing != null) { // if we are editing a label
            JLabel edited = editing;
            editing.setBackground(null);
            editing.setOpaque(false);
            editing = null;
            if (edited.getText().equals(KEYBOARD_LABEL_DEFAULT_VALUE)) {
                edited.setText(Integer.toString((Integer) Simulation.getOption(edited.getName())));
            } else {
                try {
                    int value = Integer.parseInt(edited.getText());                    
                    if (edited.getName().equals(Simulation.MAX_DENSITY)) {
                        if (value > SettingsWindow.MAX_DENSITY_MAXIMUM) {
                            throw new RuntimeException(SettingsWindow.DENSITY_RANGE_ERROR);
                        }
                        if (value < (Integer) Simulation.getOption(Simulation.MIN_DENSITY)) {                            
                            throw new RuntimeException(SettingsWindow.DENSITY_DIFFERENCE_ERROR);
                        }
                    }                    
                    if (edited.getName().equals(Simulation.MIN_DENSITY)) {
                        if (value > SettingsWindow.MIN_DENSITY_MAXIMUM) {
                            throw new RuntimeException(SettingsWindow.DENSITY_RANGE_ERROR);
                        }
                        if (value > (Integer) Simulation.getOption(Simulation.MAX_DENSITY)) {                            
                            throw new RuntimeException(SettingsWindow.DENSITY_DIFFERENCE_ERROR);
                        }
                    }                    
                    if (edited.getName().equals(Simulation.AGGRESSION)) {
                        if (value > SettingsWindow.AGGRESSION_MAXIMUM) {
                            throw new RuntimeException(SettingsWindow.AGGRESSION_RANGE_ERROR);
                        }
                    }
                    if(edited.getName().equals(Simulation.MAXIMUM_SPEED)){
                        if(value > SettingsWindow.MAXIMUM_SPEED_MAXIMUM){
                            throw new RuntimeException(SettingsWindow.MAXIMUM_SPEED_ERROR);
                        }
                    }
                    Simulation.setOption(edited.getName(), value);
                } 
                catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    edited.setText(Integer.toString((Integer) Simulation.getOption(edited.getName())));
                }
            }
        }
    }

    /**
     * Sets the time label with the given text
     * @param s the string to set as the text
     */
    public void setTimeText(String s){
        finishEditing();
        timeDetail.setText(s);
    }
    
    /**
     * Sets the minimum density label with the given text
     * @param s the string to set as the text
     */
    public void setVehicleMinDensityText(String s) {
        finishEditing();
        vehicleMinDensityDetail.setText(s);
    }
    
    /**
     * Sets the maximum density label with the given text
     * @param s the string to set as the text
     */
    public void setVehicleMaxDensityText(String s){
        finishEditing();
        vehicleMaxDensityDetail.setText(s);
    }
    
    /**
     * Sets the vehicle aggression label with the given text
     * @param s the string to set as the text
     */
    public void setVehicleAggressionText(String s){
        finishEditing();
        carAggressiveDetail.setText(s);
    }
    
    /**
     * Sets the ratio of cars label with the given text
     * @param s the string to set as the text
     */
    public void setRatioCarsText(String s){
        finishEditing();
        ratioCarsDetail.setText(s);
    }
    
    /**
     * Sets the ratio of trucks label with the given text
     * @param s the string to set as the text
     */
    public void setRatioTrucksText(String s){
        finishEditing();
        ratioTrucksDetail.setText(s);
    }
    
    /**
     * Sets the maximum speed label with the given text
     * @param s the string to set as the text
     */
    public void setMaximumSpeedText(String s){
        finishEditing();
        maximumSpeedDetail.setText(s);
    }
    
    /**
     * Sets the car counter label with the given text
     * @param s the string to set as the text
     */
    public void setCarCountText(String s){
        carsTr.setText(s);
    }
    
    /**
     * Sets the truck counter label with the given text
     * @param s the string to set as the text
     */
    public void setTruckCountText(String s){
        trucksTr.setText(s);
    }
}

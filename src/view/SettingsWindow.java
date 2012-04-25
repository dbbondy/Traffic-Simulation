
package view;

import controller.Simulation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import model.junctions.Junction;

/**
 * Class to represent the window from which the user can edit the values of the simulation variables
 * @author Daniel Bond
 */
public class SettingsWindow extends JFrame {
    
    // constant for the error message for the density range
    public static final String DENSITY_RANGE_ERROR = "You cannot have a density level greater than 100";
    
    // constant for the error message for the aggression range
    public static final String AGGRESSION_RANGE_ERROR = "You cannot have an aggression level greater than 100";
    
    // constant for the error message for the density difference
    public static final String DENSITY_DIFFERENCE_ERROR  = "Maximum density must be greater than or equal to minimum density. ";
    
    // constant for the error message for the maximum speed range
    public static final String MAXIMUM_SPEED_ERROR = "You cannot have a maximum speed of greater than 800.";
    
    // default value for the minimum density
    public static final String MIN_DENSITY_DEFAULT = "10"; 
    // default value for the maximum density
    public static final String MAX_DENSITY_DEFAULT = "20";
    // default value for the vehicle aggression
    public static final String AGGRESSION_DEFAULT = "25";
    // default value for the car ratio
    public static final String CAR_RATIO_DEFAULT = "5";
    // default value for the truck ratio
    public static final String TRUCK_RATIO_DEFAULT = "5";
    // default for the maximum speed
    public static final String MAXIMUM_SPEED_DEFAULT = "250";
    
    // maximum value for the minimum density
    public static final int MIN_DENSITY_MAXIMUM = 100;
    // maximum value for the maximum density
    public static final int MAX_DENSITY_MAXIMUM = 100;
    // maximum value for the aggression
    public static final int AGGRESSION_MAXIMUM = 100;
    // maximum value for the maximum speed
    public static final int MAXIMUM_SPEED_MAXIMUM = 800;
    
    private Container contentPane; // the content pane for this window
    private JLabel minDensityLbl; // labels for the environment variables
    private JLabel maxDensityLbl;
    private JLabel aggressionLbl;
    private JLabel carRatioLbl;
    private JLabel truckRatioLbl;
    private JLabel maximumSpeedLbl;
    private JLabel junctionLbl;
    private JTextField minDensityField; // text fields for the environment variables
    private JTextField maxDensityField;
    private JTextField aggressionField;
    private JTextField carRatioField;
    private JTextField truckRatioField;
    private JTextField maximumSpeedField;
    private JButton submitBtn; // submit button for the window
    private JButton defaultBtn; // default button for the values of the variables on the window
    private GridBagConstraints cons; // reference to the constraints class for our window layour
    private JTextField[] fields; //. the collection of text fields
    private JComboBox<String> junctions; // the list of junctions we can choose from

    
    public SettingsWindow() {
        initComponents();
        addComponents();
        addListeners();
        if (((Integer) Simulation.getOption(Simulation.TIME_STEP)) > 0) { // if this window was created after simulation had begun
            setCurrentValues(); // show current values in the text fields
        } else { // we need to show default values in the text fields
            setDefaultValues();
        }
        this.setTitle("Settings");
        this.setVisible(true);
    }
    
    /**
     * Initialise the components
     */
    private void initComponents() {
        contentPane = this.getContentPane();
        minDensityLbl = new JLabel("Minimum density of vehicles");
        maxDensityLbl = new JLabel("Maximum density of vehicles");
        aggressionLbl = new JLabel("Aggression of drivers in simulation");
        carRatioLbl = new JLabel("Ratio of Cars");
        truckRatioLbl = new JLabel("Ratio of Trucks");
        maximumSpeedLbl = new JLabel("Maximum Speed of Vehicles");
        junctionLbl = new JLabel("Junction to be simulated");
        minDensityField = new JTextField(20);
        maxDensityField = new JTextField(20);
        aggressionField = new JTextField(20);
        carRatioField = new JTextField(20);
        truckRatioField = new JTextField(20);
        maximumSpeedField = new JTextField(20);
        cons = new GridBagConstraints();
        submitBtn = new JButton("Submit");
        defaultBtn = new JButton("Default Values");
        this.setResizable(false);
        contentPane.setLayout(new GridBagLayout());
        fields = new JTextField[6];
        String[] junctionOptions = Junction.getJunctionNames().toArray(new String[0]);
        junctions = new JComboBox<>(junctionOptions);
        
        //customises tab usage so that tabs can be used to modify values
        TabFocusTraversal tft = new TabFocusTraversal();
        tft.addIndexedComponent(minDensityField);
        tft.addIndexedComponent(maxDensityField);
        tft.addIndexedComponent(aggressionField);
        tft.addIndexedComponent(carRatioField);
        tft.addIndexedComponent(truckRatioField);
        tft.addIndexedComponent(maximumSpeedField);
        tft.addIndexedComponent(junctions);
        tft.addIndexedComponent(submitBtn);
        tft.addIndexedComponent(defaultBtn);
        setFocusTraversalPolicy(tft);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); //centers the frame in the screen
    }
    
    /**
     * Add the components to their respective containers
     */
    private void addComponents() {
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.ipadx = 5;
        cons.ipady = 5;
        contentPane.add(minDensityLbl, cons);
        
        cons.gridy = 1;
        contentPane.add(maxDensityLbl, cons);
        
        cons.gridy = 2;
        contentPane.add(aggressionLbl, cons);
        
        cons.gridy = 3;
        contentPane.add(carRatioLbl, cons);
        
        cons.gridy = 4;
        contentPane.add(truckRatioLbl, cons);
        
        cons.gridy = 5;
       contentPane.add(maximumSpeedLbl, cons);
        
        cons.gridy = 6;
        contentPane.add(junctionLbl, cons);
        
        cons.gridx = 1;
        cons.gridy = 0;
        
        contentPane.add(minDensityField, cons);
        
        cons.gridy = 1;
        contentPane.add(maxDensityField, cons);
        
        cons.gridy = 2;
        contentPane.add(aggressionField, cons);
        
        cons.gridy = 3;
        contentPane.add(carRatioField, cons);
        
        cons.gridy = 4;
        contentPane.add(truckRatioField, cons);
        
        cons.gridy = 5;
        contentPane.add(maximumSpeedField, cons);
        
        cons.gridy = 6;
        contentPane.add(junctions, cons);
        
        cons.gridx = 2;
        cons.gridy = 1;
        contentPane.add(submitBtn, cons);
        
        cons.gridy = 2;
        contentPane.add(defaultBtn, cons);
        
        minDensityField.setName(Simulation.MIN_DENSITY);
        fields[0] = minDensityField;
        
        maxDensityField.setName(Simulation.MAX_DENSITY);
        fields[1] = maxDensityField;
        
        aggressionField.setName(Simulation.AGGRESSION);
        fields[2] = aggressionField;
        
        carRatioField.setName(Simulation.CAR_RATIO);
        fields[3] = carRatioField;
        
        truckRatioField.setName(Simulation.TRUCK_RATIO);
        fields[4] = truckRatioField;
        
        maximumSpeedField.setName(Simulation.MAXIMUM_SPEED);
        fields[5] = maximumSpeedField;
        
        this.pack();
        
    }
    
    /**
     * Add the listeners to the appropriate components that require them
     */
    private void addListeners() {
        submitBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                int newValues = 0;
                for (JTextField field : fields) { // for all text fields
                    if (!field.getText().isEmpty()) {
                        try {
                            int value = Integer.parseInt(field.getText());
                            
                            // determine what the field is, and whether the value is a safe value
                            if (value < 0) {
                                showErrMessage("You have inputted negative values for 1 or more fields. Please enter sensible, positive values", "Error");
                                return;
                            }
                            
                            if (field.getName().equals(Simulation.MIN_DENSITY)) {
                                if (value > MIN_DENSITY_MAXIMUM) {
                                    showErrMessage(DENSITY_RANGE_ERROR, "Error");
                                    return;
                                }
                            }
                            
                            if (field.getName().equals(Simulation.MAX_DENSITY)) {
                                if (value > MAX_DENSITY_MAXIMUM) {
                                    showErrMessage(DENSITY_RANGE_ERROR, "Error");
                                    return;
                                }
                            }
                            
                            if (field.getName().equals(Simulation.AGGRESSION)) {
                                if (value > AGGRESSION_MAXIMUM) {
                                    showErrMessage(AGGRESSION_RANGE_ERROR, "Error");
                                    return;
                                }
                            }
                            
                            if(field.getName().equals(Simulation.MAXIMUM_SPEED)){
                                if(value > MAXIMUM_SPEED_MAXIMUM){
                                    showErrMessage(MAXIMUM_SPEED_ERROR, "Error");
                                    return;
                                }
                            }
                            
                            Simulation.setOption(field.getName(), value);
                            newValues++;
                        } catch (NumberFormatException nfe) {
                            showErrMessage("Some inputted values are incorrect. Please enter correct values", "Error");
                            return;
                        }
                    }
                }
                
                if (newValues < fields.length) {
                    showErrMessage("You need to enter values for all fields", "Error");
                    return;
                }
                
                if ((int) Simulation.getOption(Simulation.MIN_DENSITY) > (int) Simulation.getOption(Simulation.MAX_DENSITY)) {
                    showErrMessage(DENSITY_DIFFERENCE_ERROR, "Error");
                    return;
                }
                
                if ((int) Simulation.getOption(Simulation.MAX_DENSITY) < (int) Simulation.getOption(Simulation.MIN_DENSITY)) {
                    showErrMessage(DENSITY_DIFFERENCE_ERROR, "Error");
                    return;
                }
                
                determineJuncInit();
                Simulation.settingsChanged();
                
                
                
                synchronized (Simulation.getSimulationThread()) { //notifying simulation thread that we are done with waiting.
                    Simulation.getSimulationThread().notify();
                }
                SettingsWindow.this.dispose(); // garbage collect this window as we are done.
            }
        });
        
        defaultBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setDefaultValues();
            }
        });
        
    }
    
    /**
     * Set default values for the text fields
     */
    private void setDefaultValues() {
        
        minDensityField.setText(MIN_DENSITY_DEFAULT);
        maxDensityField.setText(MAX_DENSITY_DEFAULT);
        aggressionField.setText(AGGRESSION_DEFAULT);
        carRatioField.setText(CAR_RATIO_DEFAULT);
        truckRatioField.setText(TRUCK_RATIO_DEFAULT);
        maximumSpeedField.setText(MAXIMUM_SPEED_DEFAULT);
        junctions.setSelectedIndex(1);
    }
    
    /**
     * Get the current simulation values and set the text fields with those values
     */
    private void setCurrentValues() {
        minDensityField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.MIN_DENSITY)));
        maxDensityField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.MAX_DENSITY)));
        aggressionField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.AGGRESSION)));
        carRatioField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.CAR_RATIO)));
        truckRatioField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.TRUCK_RATIO)));
        maximumSpeedField.setText(Integer.toString((Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED)));
        
        Junction j = (Junction) Simulation.getOption(Simulation.JUNCTION_TYPE);
        String name = j.toString();
        junctions.setSelectedItem(name);
    }
    
    /**
     * Determines which junction will be initialised based upon the selection from the list
     */
    private void determineJuncInit() {
        try {
            String junction = (String) junctions.getSelectedItem();
            Simulation.setOption(Simulation.JUNCTION_TYPE, Junction.getJunctionTypeByName(junction).newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to produce error messages for invalid input
     * @param message the message to display
     * @param title the title of the error window
     */
    private void showErrMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Customised FocusTraversalPolicy class to allow for customised tab navigation
     * @author Daniel Bond
     */
    private class TabFocusTraversal extends FocusTraversalPolicy {
        
        private ArrayList<Component> components = new ArrayList<>(); // the collection of components we want to specify a tab order for
        
        /**
         * Add a component to the set of indices
         * @param component the component to add
         */
        public void addIndexedComponent(Component component) {
            components.add(component);
        }
        
        /**
         * Gets the component after the <code> aComponent </code> component
         * @param aContainer not used in this implementation
         * @param aComponent the component used as an index to get the component after
         * @return the component after the component passed in
         */
        @Override
        public Component getComponentAfter(Container aContainer, Component aComponent) {
            int atIndex = components.indexOf(aComponent);
            int nextIndex = (atIndex + 1) % components.size();
            return components.get(nextIndex);
        }
        
        /**
         * Gets the component before the <code> aComponent </code> component
         * @param aContainer not used in this implementation
         * @param aComponent the component used as an index to get the component before
         * @return the component before the component passed in
         */
        @Override
        public Component getComponentBefore(Container aContainer, Component aComponent) {
            int atIndex = components.indexOf(aComponent);
            int nextIndex = (atIndex + components.size() - 1) % components.size();
            return components.get(nextIndex);
        }
        
        /**
         * Gets the first component in the collection
         * @param aContainer not used in this implementation
         * @return the first component in the collection
         */
        @Override
        public Component getFirstComponent(Container aContainer) {
            return components.get(0);
        }

        /**
         * Gets the last component in the collection
         * @param aContainer not used in this implementation
         * @return the last component in the collection
         */
        @Override
        public Component getLastComponent(Container aContainer) {
            return components.get(components.size());
        }

        /**
         * Gets the default component in the collection.
         * This method will return the first component as the default component.
         * This method will return the exact same output as <code> getFirstComponent </code> 
         * @param aContainer not used in this implementation
         * @return the default component of the collection
         */
        @Override
        public Component getDefaultComponent(Container aContainer) {
            return components.get(0);
        }
    }
}

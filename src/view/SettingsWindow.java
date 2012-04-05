/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import model.junctions.Junction;



/**
 *
 * @author Dan
 */
public class SettingsWindow extends JFrame {

    //TODO: the settings window closes even if an error occurs, fix this logic. 
    //TODO: there are todo's in the user interface class. do those too.
    private Container contentPane;
    private JLabel minDensityLbl;
    private JLabel maxDensityLbl;
    private JLabel aggressionLbl;
    private JLabel carRatioLbl;
    private JLabel truckRatioLbl;
    private JLabel junctionLbl;
    private JTextField minDensityField;
    private JTextField maxDensityField;
    private JTextField aggressionField;
    private JTextField carRatioField;
    private JTextField truckRatioField;
    private JButton submitBtn;
    private JButton defaultBtn;
    private GridBagConstraints cons;
    private JTextField[] fields;
    private JComboBox<String> junctions;

    public SettingsWindow() {
        initComponents();
        addComponents();
        addListeners();  
        this.setTitle("Settings");
        this.setVisible(true); 
    }

    private void initComponents() {
        contentPane = this.getContentPane();
        minDensityLbl = new JLabel("Minimum density of vehicles");
        maxDensityLbl = new JLabel("Maximum density of vehicles");
        aggressionLbl = new JLabel("Aggression of drivers in simulation");
        carRatioLbl = new JLabel("Ratio of Cars");
        truckRatioLbl = new JLabel("Ratio of Trucks");
        junctionLbl = new JLabel("Junction to be simulated: ");
        minDensityField = new JTextField(20);
        maxDensityField = new JTextField(20);
        aggressionField = new JTextField(20);
        carRatioField = new JTextField(20);
        truckRatioField = new JTextField(20);
        cons = new GridBagConstraints();
        submitBtn = new JButton("Submit");
        defaultBtn = new JButton("Default Values");
        this.setResizable(false);
        contentPane.setLayout(new GridBagLayout());
        fields = new JTextField[5];

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] junctionOptions = {"<<please enter a choice>>", "Two-Lane Junction", "Roundabout Junction", "Traffic light Junction", "Flyover Junction", "Plain Junction"};
        junctions = new JComboBox<String>(junctionOptions);

        this.setLocationRelativeTo(null); //centers the frame in the screen
        
        

    }

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

        this.pack();

    }

    private void addListeners() {
        submitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int newValues = 0;
                for (JTextField field : fields) {
                    if (!field.getText().isEmpty()) {
                        try {
                            int value = Integer.parseInt(field.getText());

                            if (value < 0) {
                                showErrMessage("You have inputted negative values for 1 or more fields. Please enter sensible, positive values", "Error");
                                return;
                            }
                            
                            if(field.getName().equals(Simulation.MIN_DENSITY)){
                                if(value > 100){
                                    showErrMessage("You cannot have an density level greater than 100", "Error");
                                    return;
                                }
                            }
                            
                            if(field.getName().equals(Simulation.MAX_DENSITY)){
                                if(value > 100){
                                    showErrMessage("You cannot have an density level greater than 100", "Error");
                                    return;
                                }
                            }
                            
                            if (field.getName().equals(Simulation.AGGRESSION)) {
                                if (value > 100) {
                                    showErrMessage("You cannot have an aggression level greater than 100", "Error");
                                    return;
                                }
                            }
                            
                            if (field.getName().equals(Simulation.CAR_RATIO)) {
                                if(value > 10){
                                    showErrMessage("You cannot have a car ratio level greater than 10", "Error");
                                    return;
                                }
                            }
                            
                            if (field.getName().equals(Simulation.TRUCK_RATIO)) {
                                if(value > 10){
                                    showErrMessage("You cannot have a truck ratio level greater than 10", "Error");
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
                
                if(((int)Simulation.getOption(Simulation.CAR_RATIO)) + ((int)Simulation.getOption(Simulation.TRUCK_RATIO)) > 10){
                    showErrMessage("The ratio of cars : trucks must sum to 10.", "Error");
                    return;
                }
                
                if (junctions.getSelectedIndex() == 0) {
                    showErrMessage("You haven't selected a junction. Please select a junction", "Error");
                    return;
                }

                if (newValues < fields.length) {
                    showErrMessage("You need to enter values for all fields", "Error");
                    return;
                }
                
                if((int)Simulation.getOption(Simulation.MIN_DENSITY) > (int)Simulation.getOption(Simulation.MAX_DENSITY)){
                    showErrMessage("Min > max is not allowed. Try again", "Error");
                    return;
                }
                
                if((int)Simulation.getOption(Simulation.MAX_DENSITY) < (int)Simulation.getOption(Simulation.MIN_DENSITY)){
                    showErrMessage("Max < min is not allowed. Try again", "Error");
                    return;
                }
                
                determineJuncInit();
                Simulation.settingsChanged();
                
               
                
                synchronized(Simulation.getSimulationThread()){ //notifying simulation thread that we are done with waiting.
                    Simulation.getSimulationThread().notify();
                }
                
                SettingsWindow.this.dispose();
            }
        });

        defaultBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                minDensityField.setText("10");
                maxDensityField.setText("25");
                aggressionField.setText("25");
                carRatioField.setText("5");
                truckRatioField.setText("5");
                junctions.setSelectedIndex(1);
            }
        });
    }
    
    public boolean isJuncDifferent(){
         String junction = (String)junctions.getSelectedItem();
         Junction currentJunction = (Junction)Simulation.getOption(Simulation.JUNCTION_TYPE);
         
         if(currentJunction.toString().equals(junction)){
             return false;
         }
         return true;
    }
    
    private void determineJuncInit(){
        String junction = (String)junctions.getSelectedItem();
                switch (junction) {
                    case "Two-Lane Junction":
                        Simulation.setOption(Simulation.JUNCTION_TYPE, new model.junctions.TwoLaneJunction());
                        break;
                    case "Roundabout Junction":
                        Simulation.setOption(Simulation.JUNCTION_TYPE, new model.junctions.RoundaboutJunction());
                        break;
                    case "Plain Junction":
                        Simulation.setOption(Simulation.JUNCTION_TYPE, new model.junctions.PlainJunction());
                        break;
                    case "Traffic light Junction":
                        Simulation.setOption(Simulation.JUNCTION_TYPE, new model.junctions.TrafficLightJunction());
                        break;
                    case "Flyover Junction":
                        Simulation.setOption(Simulation.JUNCTION_TYPE, new model.junctions.FlyoverJunction());
                        break;
                }
    }

    private void showErrMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    
}

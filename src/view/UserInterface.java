/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Dan
 */
public class UserInterface extends JFrame {

    private Container contentPane;
    private JPanel buttonPanel;
    private JButton startSim;
    private JButton pauseSim;
    private JButton changeSettings;
    private DetailsPanel detailPanel;
    private SimulationPanel simPanel;

    public UserInterface() {
        super("Traffic Simulation");
        initComponents();
        addComponents();
        addListeners();
        this.setVisible(true);
    }

    public void updateGUI() {
        
        detailPanel.setTimeText(Simulation.getOption(Simulation.TIME_STEP).toString());
        detailPanel.setVehicleDensityText(Simulation.getOption(Simulation.DENSITY).toString());
        detailPanel.setRatioCarsText(Simulation.getOption(Simulation.CAR_RATIO).toString());
        detailPanel.setRatioTrucksText(Simulation.getOption(Simulation.TRUCK_RATIO).toString());
        detailPanel.setVehicleAggressionText(Simulation.getOption(Simulation.AGGRESSION).toString());
        updateButtonState();
        simPanel.repaint();
    }

    public void reloadGUI() {
        simPanel.clearCache();
    }

    private void updateButtonState() {
        
        if(Simulation.isPaused() && !Simulation.isStarted()){ //if paused and simulation stopped
            pauseSim.setText("Pause Simulation");
            startSim.setText("Start Simulation");
            pauseSim.setEnabled(false);
        }else if(Simulation.isPaused() && Simulation.isStarted()){ // if paused and simulated is running
            pauseSim.setText("Resume Simulation");
            startSim.setText("Stop Simulation");
            pauseSim.setEnabled(true);
        }else if(!Simulation.isPaused() && Simulation.isStarted()){ // if not paused and simulation is running
            pauseSim.setText("Pause Simulation");
            startSim.setText("Stop Simulation");
            pauseSim.setEnabled(true);
        }else if(!Simulation.isPaused() && !Simulation.isStarted()){ //if not paused and not started
            pauseSim.setText("Pause Simulation");
            startSim.setText("Start Simulation");
            pauseSim.setEnabled(false);
        }
    }

    private void initComponents() {

        contentPane = this.getContentPane();
        startSim = new JButton("Start Simulation");
        pauseSim = new JButton("Pause Simulation");
        pauseSim.setEnabled(false);
        changeSettings = new JButton("Change Settings");
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        detailPanel = new DetailsPanel();
        simPanel = new SimulationPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int width = SimulationPanel.WIDTH + 220;
        width += this.getInsets().left;
        width += this.getInsets().right;
        
        int height = SimulationPanel.HEIGHT;
        height += this.getInsets().top;
        height += this.getInsets().bottom;
        
        Dimension size = new Dimension(width, height);
        
        this.getContentPane().setPreferredSize(size);
        this.getContentPane().setMaximumSize(size);
        this.setResizable(false);

    }

    private void addComponents() {

        buttonPanel.add(startSim);
        buttonPanel.add(pauseSim);
        buttonPanel.add(changeSettings);
        buttonPanel.setBackground(new Color(235, 235, 235));
        
        JPanel simulationContainer = new JPanel();
        simulationContainer.setLayout(new BoxLayout(simulationContainer, BoxLayout.X_AXIS));
        simulationContainer.add(simPanel);        
        
        JPanel sideBar = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(220, 0);
            }
        };
        
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(new Color(250, 250, 250));
        sideBar.add(detailPanel); 
        // put something else in sidebar LIKE THE SETTINGS!
        
        contentPane.add(simulationContainer, BorderLayout.CENTER);
        contentPane.add(sideBar, BorderLayout.LINE_END);
        contentPane.add(buttonPanel, BorderLayout.PAGE_START);
        
        this.pack();
    }

    //TODO
    private void addListeners() {

        startSim.addActionListener(new ActionListener() { //maybe change this to be just a stop button and make the simulation run automatically. 

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Simulation.isStarted()) { //if it has already started. we are now in a stop simulation state
                    Simulation.reset();
                    updateButtonState();
                    System.out.println(Simulation.isStarted());
                    System.out.println(Simulation.isPaused());
                } else { //else start
                    Simulation.start();
                    updateButtonState();
                    Simulation.Simulate();
                }
            }
        });

        pauseSim.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Simulation.isPaused()) { //if we are already paused. unpause
                    Simulation.pause();
                    updateButtonState();
                    detailPanel.repaint();
                    onUnPauseButtonPress();
                } else { //pause the simulation
                    Simulation.pause();
                    updateButtonState();
                    detailPanel.repaint();
                }
            }
        });

        changeSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsWindow();
                if(!Simulation.isPaused()){ //if it's not paused, we need to change the state of the interface and the simulation
                    Simulation.pause();
                    updateButtonState();
                } // else we do nothing
                
            }
        });

    }

    private void onUnPauseButtonPress() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify();
        }
    }
}

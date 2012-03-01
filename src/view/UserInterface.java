/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
        detailPanel.setTimeText("Current time step is: " + Simulation.getOption(Simulation.TIME_STEP));
        detailPanel.setVehicleDensityText("Current car density is: " + Simulation.getOption(Simulation.DENSITY));
        detailPanel.setRatioCarsText("Current ratio of Cars is :" + Simulation.getOption(Simulation.CAR_RATIO));
        detailPanel.setRatioTrucksText("Current ratio of Trucks is: " + Simulation.getOption(Simulation.TRUCK_RATIO));
        detailPanel.setVehicleAggressionText("Current vehicle aggression is: " + Simulation.getOption(Simulation.AGGRESSION));
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
        buttonPanel.setLayout(new FlowLayout());

        detailPanel = new DetailsPanel();
        simPanel = new SimulationPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1024, 768));

    }

    private void addComponents() {

        buttonPanel.add(startSim);
        buttonPanel.add(pauseSim);
        buttonPanel.add(changeSettings);
        contentPane.add(detailPanel, BorderLayout.EAST);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        contentPane.add(simPanel, BorderLayout.CENTER);
        this.pack();
    }

    //TODO
    private void addListeners() {

        startSim.addActionListener(new ActionListener() { //maybe change this to be just a stop button and make the simulation run automatically. 

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Simulation.isStarted()) { //if it has already started. we are now in a stop simulation state
                    Simulation.start();
                    updateButtonState();
                    Simulation.reset();
                    Simulation.pause(); //not paused anymore because we have stopped
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
                Simulation.pause();
                updateButtonState();
            }
        });

    }

    private void onUnPauseButtonPress() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify();
        }
    }
}

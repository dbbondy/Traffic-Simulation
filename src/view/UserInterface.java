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
import model.DetailsPanel;

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
        detailPanel.repaint();
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

    private void addListeners() {

        startSim.addActionListener(new ActionListener() { //maybe change this to be just a stop button and make the simulation run automatically. 

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Simulation.isStarted()) {
                    Simulation.start();
                    startSim.setText("Start Simulation");
                    pauseSim.setText("Pause Simulation");
                    pauseSim.setEnabled(false);
                    return;
                }
                Simulation.start();
                startSim.setText("Stop Simulation");
                pauseSim.setEnabled(true);

            }
        });

        pauseSim.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Simulation.isPaused()) {
                    Simulation.pause();
                    pauseSim.setText("Pause Simulation");
                    detailPanel.repaint();
                    onUnPauseButtonPress();
                    return;
                }

                Simulation.pause();
                pauseSim.setText("Resume Simulation");
                detailPanel.repaint();
            }
        });

        changeSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                new SettingsWindow();
            }
        });

    }

    private void onUnPauseButtonPress() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify();
        }
    }
    //TODO: move details panel into it's own class, have the paintComponent override handle updates to the details on itself. then have this class' updateGUI call repaint()
    //TODO: the pause button listener on the interface will have
}

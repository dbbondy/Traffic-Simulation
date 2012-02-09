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
    private JPanel detailPanel;
    private JLabel carDensityDetail;
    private JLabel carAggressiveDetail;
    private JLabel ratioCarsDetail;
    private JLabel ratioTrucksDetail;
    private JLabel time;
    private JLabel pausedLbl;
    private SimulationPanel simPanel;

    public UserInterface() {
        super("Traffic Simulation");
        initComponents();
        addComponents();
        addListeners();
        this.setVisible(true);


    }

    public void updateGUI() {
        repaint();
    }

    private void initComponents() {

        contentPane = this.getContentPane();
        startSim = new JButton("Start Simulation");
        pauseSim = new JButton("Pause Simulation");
        pauseSim.setEnabled(false);
        changeSettings = new JButton("Change Settings");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        detailPanel = new JPanel();
        carDensityDetail = new JLabel("Current car density is: " + Simulation.getOption(Simulation.DENSITY));
        carAggressiveDetail = new JLabel("Current vehicle aggression is: " + Simulation.getOption(Simulation.AGGRESSION));
        ratioCarsDetail = new JLabel("Current ratio of Cars is :" + Simulation.getOption(Simulation.CAR_RATIO));
        ratioTrucksDetail = new JLabel("Current ratio of Trucks is: " + Simulation.getOption(Simulation.TRUCK_RATIO));
        time = new JLabel("Current time step is: ");
        pausedLbl = new JLabel("");

        simPanel = new SimulationPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1024, 768));

    }

    private void addComponents() {

        buttonPanel.add(startSim);
        buttonPanel.add(pauseSim);
        buttonPanel.add(changeSettings);

        GridBagConstraints cons = new GridBagConstraints();
        detailPanel.setLayout(new GridBagLayout());

        cons.gridx = 0;
        cons.gridy = 0;
        cons.ipadx = 20;
        cons.ipady = 20;
        detailPanel.add(carDensityDetail, cons);

        cons.gridy = 1;
        detailPanel.add(carAggressiveDetail, cons);

        cons.gridy = 2;
        detailPanel.add(ratioCarsDetail, cons);

        cons.gridy = 3;
        detailPanel.add(ratioTrucksDetail, cons);

        cons.gridy = 4;
        detailPanel.add(time, cons);

        cons.gridy = 5;
        detailPanel.add(pausedLbl, cons);

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
                    pausedLbl.setText("");
                    return;
                }

                Simulation.pause();
                pauseSim.setText("Resume Simulation");
                pausedLbl.setText("Simulation is paused");
            }
        });

        changeSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                new SettingsWindow();
            }
        });

    }

    public void onUnPauseButtonPress() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify(); //set this up correctly.
        }
    }
    //TODO: move details panel into it's own class, have the paintComponent override handle updates to the details on itself. then have this class' updateGUI call repaint()
    //TODO: the pause button listener on the interface will have
}

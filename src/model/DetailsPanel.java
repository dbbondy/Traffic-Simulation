/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Simulation;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Dan
 */
public class DetailsPanel extends JPanel {

    private JLabel carDensityDetail;
    private JLabel carAggressiveDetail;
    private JLabel ratioCarsDetail;
    private JLabel ratioTrucksDetail;
    private JLabel time;
    private JLabel pausedLbl;

    public DetailsPanel() {
        super();
        initComponents();
        addComponents();
    }

    private void initComponents() {
        carDensityDetail = new JLabel("Current car density is: " + Simulation.getOption(Simulation.DENSITY));
        carAggressiveDetail = new JLabel("Current vehicle aggression is: " + Simulation.getOption(Simulation.AGGRESSION));
        ratioCarsDetail = new JLabel("Current ratio of Cars is :" + Simulation.getOption(Simulation.CAR_RATIO));
        ratioTrucksDetail = new JLabel("Current ratio of Trucks is: " + Simulation.getOption(Simulation.TRUCK_RATIO));
        time = new JLabel("Current time step is: " + Simulation.getOption(Simulation.TIME_STEP));
        pausedLbl = new JLabel("");
    }

    private void addComponents() {
        GridBagConstraints cons = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        cons.gridx = 0;
        cons.gridy = 0;
        cons.ipadx = 20;
        cons.ipady = 20;
        this.add(carDensityDetail, cons);

        cons.gridy = 1;
        this.add(carAggressiveDetail, cons);

        cons.gridy = 2;
        this.add(ratioCarsDetail, cons);

        cons.gridy = 3;
        this.add(ratioTrucksDetail, cons);

        cons.gridy = 4;
        this.add(time, cons);

        cons.gridy = 5;
        this.add(pausedLbl, cons);
    }

    @Override
    public void paintComponent(Graphics g) {

        carDensityDetail.setText("Current car density is: " + Simulation.getOption(Simulation.DENSITY));
        carAggressiveDetail.setText("Current vehicle aggression is: " + Simulation.getOption(Simulation.AGGRESSION));
        ratioCarsDetail.setText("Current ratio of Cars is :" + Simulation.getOption(Simulation.CAR_RATIO));
        ratioTrucksDetail.setText("Current ratio of Trucks is: " + Simulation.getOption(Simulation.TRUCK_RATIO));
        time.setText("Current time step is: " + Simulation.getOption(Simulation.TIME_STEP));
        pausedLbl.setText("Simulation is paused");
        
        if(!Simulation.isPaused()){
            pausedLbl.setText("");
        }
    }
}

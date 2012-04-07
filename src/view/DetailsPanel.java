/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import model.Car;
import model.SimulationStats;
import model.Truck;

/**
 *
 * @author Dan
 */
public class DetailsPanel extends JPanel {

    private JLabel vehicleMinDensityDetail;
    private JLabel vehicleMaxDensityDetail;
    private JLabel carAggressiveDetail;
    private JLabel ratioCarsDetail;
    private JLabel ratioTrucksDetail;
    private JLabel time;
    private JLabel carsTr;
    private JLabel trucksTr;

    public DetailsPanel() {
        super();
        initComponents();
        this.setBackground(new Color(250, 250, 250));
        this.setMaximumSize(new Dimension(220, 180));
        this.setMinimumSize(new Dimension(220, 0));
        this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    }

    private void initComponents() {
        
        JLabel vehicleMinDensityDetailL = new JLabel("Vehicle Minimum Density");
        vehicleMinDensityDetail = new JLabel(Simulation.getOption(Simulation.MIN_DENSITY).toString());
        vehicleMaxDensityDetail = new JLabel(Simulation.getOption(Simulation.MAX_DENSITY).toString());
        JPanel vehicleMinDensityP = new JPanel();
        vehicleMinDensityP.setBackground(null);
        vehicleMinDensityP.setLayout(new BorderLayout());
        vehicleMinDensityP.add(vehicleMinDensityDetailL, BorderLayout.LINE_START);
        vehicleMinDensityP.add(vehicleMinDensityDetail, BorderLayout.LINE_END);
        
        JLabel vehicleMaxDensityDetailL = new JLabel("Vehicle Maximum Density");
        vehicleMaxDensityDetail = new JLabel(Simulation.getOption(Simulation.MAX_DENSITY).toString());
        JPanel vehicleMaxDensityP = new JPanel();
        vehicleMaxDensityP.setBackground(null);
        vehicleMaxDensityP.setLayout(new BorderLayout());
        vehicleMaxDensityP.add(vehicleMaxDensityDetailL, BorderLayout.LINE_START);
        vehicleMaxDensityP.add(vehicleMaxDensityDetail, BorderLayout.LINE_END);
        
        JLabel carAggressiveDetailL = new JLabel("Vehicle Aggression"); // + Simulation.getOption(Simulation.AGGRESSION));
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
        
        JLabel timeL = new JLabel("Time Step");
        time = new JLabel(Simulation.getOption(Simulation.TIME_STEP).toString());
        JPanel timeP = new JPanel();
        timeP.setBackground(null);
        timeP.setLayout(new BorderLayout());
        timeP.add(timeL, BorderLayout.LINE_START);
        timeP.add(time, BorderLayout.LINE_END);
        
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
        
        Border padding = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        
        vehicleMinDensityDetail.setBorder(padding);
        vehicleMaxDensityDetail.setBorder(padding);
        carAggressiveDetail.setBorder(padding);
        ratioCarsDetail.setBorder(padding);
        ratioTrucksDetail.setBorder(padding);
        time.setBorder(padding);
        carsTr.setBorder(padding);
        trucksTr.setBorder(padding);
        
        JSeparator seperator = new JSeparator(JSeparator.HORIZONTAL);
        
        vehicleMinDensityDetailL.setBorder(padding);
        vehicleMaxDensityDetailL.setBorder(padding);
        carAggressiveDetailL.setBorder(padding);
        ratioCarsDetailL.setBorder(padding);
        ratioTrucksDetailL.setBorder(padding);
        timeL.setBorder(padding);
        carsTrL.setBorder(padding);
        trucksTrL.setBorder(padding);
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this.add(vehicleMinDensityP);
        this.add(vehicleMaxDensityP);
        this.add(carAggressiveDetailP);
        this.add(ratioCarsDetailP);
        this.add(ratioTrucksDetailP);
        this.add(timeP);
        this.add(Box.createVerticalStrut(6));
        this.add(seperator);
        this.add(Box.createVerticalStrut(6));
        this.add(carsTrP);
        this.add(trucksTrP);
        
        
        
    }

    void setTimeText(String s){
        time.setText(s);
    }
    
    void setVehicleMinDensityText(String s) {
        vehicleMinDensityDetail.setText(s);
    }
    
    void setVehicleMaxDensityText(String s){
        vehicleMaxDensityDetail.setText(s);
    }
    
    void setVehicleAggressionText(String s){
        carAggressiveDetail.setText(s);
    }
    
    void setRatioCarsText(String s){
        ratioCarsDetail.setText(s);
    }
    
    void setRatioTrucksText(String s){
        ratioTrucksDetail.setText(s);
    }
    
    void setCarCountText(String s){
        carsTr.setText(s);
    }
    
    void setTruckCountText(String s){
        trucksTr.setText(s);
    }
    
}

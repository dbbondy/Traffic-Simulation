/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

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

    public DetailsPanel() {
        super();
        initComponents();
        this.setBackground(new Color(250, 250, 250));
        this.setMaximumSize(new Dimension(220, 120));
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
        
        Border padding = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        
        vehicleMinDensityDetail.setBorder(padding);
        vehicleMaxDensityDetail.setBorder(padding);
        carAggressiveDetail.setBorder(padding);
        ratioCarsDetail.setBorder(padding);
        ratioTrucksDetail.setBorder(padding);
        time.setBorder(padding);
        
        vehicleMinDensityDetailL.setBorder(padding);
        vehicleMaxDensityDetailL.setBorder(padding);
        carAggressiveDetailL.setBorder(padding);
        ratioCarsDetailL.setBorder(padding);
        ratioTrucksDetailL.setBorder(padding);
        timeL.setBorder(padding);
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this.add(vehicleMinDensityP);
        this.add(vehicleMaxDensityP);
        this.add(carAggressiveDetailP);
        this.add(ratioCarsDetailP);
        this.add(ratioTrucksDetailP);
        this.add(timeP);
        
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
    
}

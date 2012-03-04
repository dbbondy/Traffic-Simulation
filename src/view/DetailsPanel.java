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

    private JLabel carDensityDetail;
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
        
        JLabel carDensityDetailL = new JLabel("Car Density");
        carDensityDetail = new JLabel(Simulation.getOption(Simulation.DENSITY).toString());
        JPanel carDensityDetailP = new JPanel();
        carDensityDetailP.setBackground(null);
        carDensityDetailP.setLayout(new BorderLayout());
        carDensityDetailP.add(carDensityDetailL, BorderLayout.LINE_START);
        carDensityDetailP.add(carDensityDetail, BorderLayout.LINE_END);
        
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
        
        carDensityDetail.setBorder(padding);
        carAggressiveDetail.setBorder(padding);
        ratioCarsDetail.setBorder(padding);
        ratioTrucksDetail.setBorder(padding);
        time.setBorder(padding);
        
        carDensityDetailL.setBorder(padding);
        carAggressiveDetailL.setBorder(padding);
        ratioCarsDetailL.setBorder(padding);
        ratioTrucksDetailL.setBorder(padding);
        timeL.setBorder(padding);
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this.add(carDensityDetailP);
        this.add(carAggressiveDetailP);
        this.add(ratioCarsDetailP);
        this.add(ratioTrucksDetailP);
        this.add(timeP);
        
    }

    void setTimeText(String s){
        time.setText(s);
    }
    
    void setVehicleDensityText(String s) {
        carDensityDetail.setText(s);
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

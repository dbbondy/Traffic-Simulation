/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulation;
import controller.StateLoader;
import controller.StateSaver;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import model.SimulationStats;
import model.junctions.Junction;

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
    private JButton saveJunc;
    private JButton loadJunc;
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
        detailPanel.setVehicleMinDensityText(Simulation.getOption(Simulation.MIN_DENSITY).toString());
        detailPanel.setVehicleMaxDensityText(Simulation.getOption(Simulation.MAX_DENSITY).toString());
        detailPanel.setRatioCarsText(Simulation.getOption(Simulation.CAR_RATIO).toString());
        detailPanel.setRatioTrucksText(Simulation.getOption(Simulation.TRUCK_RATIO).toString());
        detailPanel.setVehicleAggressionText(Simulation.getOption(Simulation.AGGRESSION).toString());
        detailPanel.setMaximumSpeedText(Simulation.getOption(Simulation.MAXIMUM_SPEED).toString());
        detailPanel.setCarCountText(Integer.toString(SimulationStats.getCarCount()));
        detailPanel.setTruckCountText(Integer.toString(SimulationStats.getTruckCount()));
        updateButtonState();
        simPanel.repaint();
    }

    public void reloadGUI() {
        simPanel.clearCache();
    }

    private void updateButtonState() {

        if (Simulation.isPaused() && !Simulation.isStarted()) { //if paused and simulation stopped
            pauseSim.setText("Pause Simulation");
            startSim.setText("Start Simulation");
            pauseSim.setEnabled(false);
        } else if (Simulation.isPaused() && Simulation.isStarted()) { // if paused and simulated is running
            pauseSim.setText("Resume Simulation");
            startSim.setText("Stop Simulation");
            pauseSim.setEnabled(true);
        } else if (!Simulation.isPaused() && Simulation.isStarted()) { // if not paused and simulation is running
            pauseSim.setText("Pause Simulation");
            startSim.setText("Stop Simulation");
            pauseSim.setEnabled(true);
        } else if (!Simulation.isPaused() && !Simulation.isStarted()) { //if not paused and not started
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
        saveJunc = new JButton("Save");
        loadJunc = new JButton("Load");

        buttonPanel = new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 33);
            }
        };

        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        detailPanel = new DetailsPanel();
        simPanel = new SimulationPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width = SimulationPanel.WIDTH + 220;
        width += this.getInsets().left;
        width += this.getInsets().right;

        int height = SimulationPanel.HEIGHT + 33;
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
        buttonPanel.add(loadJunc);
        buttonPanel.add(saveJunc);
        buttonPanel.setBackground(new Color(235, 235, 235));

        JPanel simulationContainer = new JPanel();
        simulationContainer.setLayout(new BoxLayout(simulationContainer, BoxLayout.X_AXIS));
        simulationContainer.add(simPanel);

        JPanel sideBar = new JPanel() {

            @Override
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
                    updateGUI();
                } else { //else start
                    Simulation.start();
                    updateButtonState();
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
                if (!Simulation.isPaused()) { //if it's not paused, we need to change the state of the interface and the simulation
                    if (!Simulation.isStarted()) { //if not started, we don't need to change buttons or pause the simulation.
                        return;
                    }
                    Simulation.pause();
                    updateButtonState();
                } // else we do nothing

            }
        });

        saveJunc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Simulation.isPaused()) {
                    Simulation.pause();
                }

                final JFileChooser fileChooser = new JFileChooser() {

                    @Override
                    public void approveSelection() {
                        File f = getSelectedFile();
                        if (f.exists() && getDialogType() == SAVE_DIALOG) {
                            int result = JOptionPane.showConfirmDialog(this, "This file already exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    super.approveSelection();
                                    return;
                                case JOptionPane.NO_OPTION:
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    super.cancelSelection();
                                    return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new CustomFilter());
                int returnOption = fileChooser.showSaveDialog(saveJunc);
                if (returnOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().endsWith(Simulation.FILE_EXT)) {
                        selectedFile = new File(selectedFile.getAbsolutePath().concat(Simulation.FILE_EXT));
                    }
                    try {
                        StateSaver.saveState(selectedFile);
                    } catch (Exception ex) {
                        displayNotification("Error: unable to save simulation.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        loadJunc.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!Simulation.isPaused()) {
                            Simulation.pause();
                        }
                        final JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.addChoosableFileFilter(new CustomFilter());

                        int returnOption = fileChooser.showOpenDialog(loadJunc);
                        if (returnOption == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            try {
                                StateLoader.loadState(selectedFile);
                                reloadGUI();
                                updateGUI();
                            } catch (Exception ex) {
                                displayNotification("Error: unable to load simulation.");
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void onUnPauseButtonPress() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify();
        }
    }

    private void displayNotification(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Notification",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

class CustomFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return (f.getName().endsWith(Simulation.FILE_EXT));
    }

    @Override
    public String getDescription() {
        return "Traffic Simulation";
    }
}

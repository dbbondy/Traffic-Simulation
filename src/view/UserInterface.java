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

/**
 * The Main User Interface class for the program
 *
 * @author Daniel Bond
 */
public class UserInterface extends JFrame {

    private Container contentPane; // the content pane for this window
    private JPanel buttonPanel; // panel for the buttons
    private JButton startSim; //buttons for the interface
    private JButton pauseSim;
    private JButton changeSettings;
    private JButton saveJunc;
    private JButton loadJunc;
    private JButton outputStatistics;
    private DetailsPanel detailPanel; // details panel with all environment details information
    private SimulationPanel simPanel; // simulation panel displaying the junction in a rendered state.

    public UserInterface() {
        super("Traffic Simulation");
        initComponents();
        addComponents();
        addListeners();
        this.setVisible(true);
    }

    /**
     * Update the GUI with the latest values and repaint the rendering area
     */
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

    /**
     * Clears the junction from the GUI
     */
    public void clearJunctionCache() {
        simPanel.clearCache();
    }

    /**
     * Updates the buttons editable states to reflect the current simulation state
     */
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
            outputStatistics.setEnabled(true);
        }
    }

    /**
     * Initialise the components
     */
    private void initComponents() {
        contentPane = this.getContentPane();
        startSim = new JButton("Start Simulation");
        pauseSim = new JButton("Pause Simulation");
        pauseSim.setEnabled(false);
        changeSettings = new JButton("Change Settings");
        saveJunc = new JButton("Save");
        loadJunc = new JButton("Load");
        outputStatistics = new JButton("Output Statistics");
        outputStatistics.setEnabled(false);

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

    /**
     * Add the components to their respective containers
     */
    private void addComponents() {
        buttonPanel.add(startSim);
        buttonPanel.add(pauseSim);
        buttonPanel.add(changeSettings);
        buttonPanel.add(loadJunc);
        buttonPanel.add(saveJunc);
        buttonPanel.add(outputStatistics);
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

    /**
     * Add listeners to components that require them
     */
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
                    onResume();
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
                    public void approveSelection() { // provide functionality to ask user if they want to overwrite an existing file
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
                if (returnOption == JFileChooser.APPROVE_OPTION) { // if the user clicked "save"
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().endsWith(Simulation.FILE_EXT)) {
                        selectedFile = new File(selectedFile.getAbsolutePath().concat(Simulation.FILE_EXT));
                    }
                    try {
                        StateSaver.saveState(selectedFile); // save the state of the simulation
                    } catch (Exception ex) {
                        displayNotification("Error: unable to save simulation.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        loadJunc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Simulation.isPaused()) { // if we aren't paused
                    Simulation.pause(); // pause it
                }
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new CustomFilter());

                int returnOption = fileChooser.showOpenDialog(loadJunc);
                if (returnOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        StateLoader.loadState(selectedFile); // load the selected simulation file in
                        clearJunctionCache();
                        updateGUI();
                    } catch (Exception ex) {
                        displayNotification("Error: unable to load simulation.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        outputStatistics.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser() {

                    @Override
                    public void approveSelection() {// provide functionality to ask user if they want to overwrite an existing file
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
                fileChooser.setAcceptAllFileFilterUsed(true);

                int returnOption = fileChooser.showSaveDialog(outputStatistics);
                if (returnOption == JFileChooser.APPROVE_OPTION) { // if the user clicked "save"
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().endsWith(Simulation.STATS_FILE_EXT)) { // if the file does not include the default file extension for text output
                        selectedFile = new File(selectedFile.getAbsolutePath().concat(Simulation.STATS_FILE_EXT));
                    }
                    SimulationStats.startBuffer(selectedFile.getAbsolutePath());
                    SimulationStats.outputEvents();
                }
            }
        });

    }

    /**
     * Notifies the secondary simulation processing thread to resume
     */
    private void onResume() {
        synchronized (Simulation.getSimulationThread()) {
            Simulation.getSimulationThread().notify();
        }
    }

    /**
     * Helper method to display notifications to the user
     *
     * @param message the message to display to the user
     */
    private void displayNotification(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Notification",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Custom file filter for open and save dialogs on the file system
     * @author Daniel Bond
     */
    private class CustomFilter extends FileFilter {

        /**
         * Predicate to determine if a file is accepted by the filter
         * @param f the file to check
         * @return <code> true </code> if the file is accepted by the filter. <code> false </code> otherwise
         */
        @Override
        public boolean accept(File f) {
            return (f.getName().endsWith(Simulation.FILE_EXT));
        }

        /**
         * Gets the description of this custom file filter
         * @return the description of this custom file filter.
         */
        @Override
        public String getDescription() {
            return "Traffic Simulation";
        }
    }
}

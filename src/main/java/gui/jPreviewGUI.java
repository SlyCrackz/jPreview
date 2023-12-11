package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class jPreviewGUI {
    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private RunningProgramsFetcher fetcher;
    private DefaultTableModel tableModel;
    private Timer updateTimer; // Timer to update the table

    public jPreviewGUI() {
        // Initialize the GUI components
        frame = new JFrame("jPreview - Program Preview");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the table model
        tableModel = new DefaultTableModel(new Object[]{"Window ID", "Class", "PID", "Focus"}, 0);
        table = new JTable(tableModel);

        // Initialize the fetcher
        fetcher = new RunningProgramsFetcher();

        // Add table to scroll pane
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Initialize and start the timer
        updateTimer = new Timer(500, e -> updateProgramList()); // Update every second
        updateTimer.start();


    }

    public void show() {
        // Initial fetch and display of the list of running programs
        updateProgramList();

        // Show the GUI
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private void updateProgramList() {
        List<String[]> data = fetcher.getRunningPrograms();
        tableModel.setRowCount(0); // Clear existing data

        // Use a regular for loop instead of foreach
        for (int i = 0; i < data.size(); i++) {
            tableModel.addRow(data.get(i));
        }
    }


}

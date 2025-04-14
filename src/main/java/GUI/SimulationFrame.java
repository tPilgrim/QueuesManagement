package GUI;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimulationFrame extends JFrame {

    private JTextField txtClients, txtQueues, txtSimTime, txtMinArrival, txtMaxArrival, txtMinService, txtMaxService;
    private JButton validateButton, startButton;
    private JComboBox<String> strategyComboBox;

    public SimulationFrame() {
        setTitle("Queue Management Simulation");
        setSize(600, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(9, 2));

        inputPanel.add(new JLabel("Number of Clients:"));
        txtClients = new JTextField();
        inputPanel.add(txtClients);

        inputPanel.add(new JLabel("Number of Queues:"));
        txtQueues = new JTextField();
        inputPanel.add(txtQueues);

        inputPanel.add(new JLabel("Simulation Time:"));
        txtSimTime = new JTextField();
        inputPanel.add(txtSimTime);

        inputPanel.add(new JLabel("Min Arrival Time:"));
        txtMinArrival = new JTextField();
        inputPanel.add(txtMinArrival);

        inputPanel.add(new JLabel("Max Arrival Time:"));
        txtMaxArrival = new JTextField();
        inputPanel.add(txtMaxArrival);

        inputPanel.add(new JLabel("Min Service Time:"));
        txtMinService = new JTextField();
        inputPanel.add(txtMinService);

        inputPanel.add(new JLabel("Max Service Time:"));
        txtMaxService = new JTextField();
        inputPanel.add(txtMaxService);

        inputPanel.add(new JLabel("Strategy:"));
        strategyComboBox = new JComboBox<>(new String[]{"Shortest Time", "Shortest Queue"});
        inputPanel.add(strategyComboBox);

        validateButton = new JButton("Validate Input Data");
        inputPanel.add(validateButton);

        startButton = new JButton("Start Simulation");
        startButton.setEnabled(false);
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);

        validateButton.addActionListener((ActionEvent e) -> validateAndPrompt());
        startButton.addActionListener((ActionEvent e) -> startSimulation());

        setVisible(true);
    }

    private void validateAndPrompt() {
        try {
            int clients = Integer.parseInt(txtClients.getText().trim());
            int queues = Integer.parseInt(txtQueues.getText().trim());
            int simTime = Integer.parseInt(txtSimTime.getText().trim());
            int minArrival = Integer.parseInt(txtMinArrival.getText().trim());
            int maxArrival = Integer.parseInt(txtMaxArrival.getText().trim());
            int minService = Integer.parseInt(txtMinService.getText().trim());
            int maxService = Integer.parseInt(txtMaxService.getText().trim());

            if (clients <= 0) {
                showError("Numarul de clienti trebuie sa fie mai mare decat 0.");
                startButton.setEnabled(false);
                return;
            }

            if (queues <= 0) {
                showError("Numarul de cozi trebuie sa fie mai mare decat 0.");
                startButton.setEnabled(false);
                return;
            }

            if (simTime <= 0) {
                showError("Timpul de simulare trebuie sa fie mai mare decat 0.");
                startButton.setEnabled(false);
                return;
            }

            if (minArrival > maxArrival) {
                showError("Timpul minim de sosire trebuie sa fie mai mare decat timpul maxim de sosire.");
                startButton.setEnabled(false);
                return;
            }

            if (minService > maxService) {
                showError("Timpul minim de procesare trebuie sa fie mai mare decat timpul maxim de procesare.");
                startButton.setEnabled(false);
                return;
            }

            startButton.setEnabled(true);

        } catch (NumberFormatException ex) {
            showError("Inputul nu este valid.");
        }
    }

    private void startSimulation() {
        int clients = Integer.parseInt(txtClients.getText().trim());
        int queues = Integer.parseInt(txtQueues.getText().trim());
        int simTime = Integer.parseInt(txtSimTime.getText().trim());
        int minArrival = Integer.parseInt(txtMinArrival.getText().trim());
        int maxArrival = Integer.parseInt(txtMaxArrival.getText().trim());
        int minService = Integer.parseInt(txtMinService.getText().trim());
        int maxService = Integer.parseInt(txtMaxService.getText().trim());

        String selectedStrategy = (String) strategyComboBox.getSelectedItem();
        SelectionPolicy policy;
        if ("Shortest Queue".equals(selectedStrategy)) {
            policy = SelectionPolicy.SHORTEST_QUEUE;
        } else {
            policy = SelectionPolicy.SHORTEST_TIME;
        }

        SimulationManager manager = new SimulationManager(clients, queues, simTime, minArrival, maxArrival, minService, maxService, policy);

        Thread simThread = new Thread(manager);
        simThread.start();
        startButton.setEnabled(false);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();
        frame.setVisible(true);
    }
}

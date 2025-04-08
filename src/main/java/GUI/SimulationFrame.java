package GUI;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimulationFrame extends JFrame {

    private JTextField txtClients, txtQueues, txtSimTime, txtMinArrival, txtMaxArrival, txtMinService, txtMaxService;
    private JTextArea logArea;
    private JButton validateButton, startButton;
    private boolean isRunning;

    public SimulationFrame() {
        setTitle("Queue Management Simulation");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));

        inputPanel.add(new JLabel("Number of Clients:"));
        txtClients = new JTextField("4");
        inputPanel.add(txtClients);

        inputPanel.add(new JLabel("Number of Queues:"));
        txtQueues = new JTextField("2");
        inputPanel.add(txtQueues);

        inputPanel.add(new JLabel("Simulation Time:"));
        txtSimTime = new JTextField("10");
        inputPanel.add(txtSimTime);

        inputPanel.add(new JLabel("Min Arrival Time:"));
        txtMinArrival = new JTextField("2");
        inputPanel.add(txtMinArrival);

        inputPanel.add(new JLabel("Max Arrival Time:"));
        txtMaxArrival = new JTextField("8");
        inputPanel.add(txtMaxArrival);

        inputPanel.add(new JLabel("Min Service Time:"));
        txtMinService = new JTextField("2");
        inputPanel.add(txtMinService);

        inputPanel.add(new JLabel("Max Service Time:"));
        txtMaxService = new JTextField("4");
        inputPanel.add(txtMaxService);

        validateButton = new JButton("Validate Input Data");
        inputPanel.add(validateButton);

        startButton = new JButton("Start Simulation");
        startButton.setEnabled(false);
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

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
        if(!isRunning) {
            logArea.setText("");
            int clients = Integer.parseInt(txtClients.getText().trim());
            int queues = Integer.parseInt(txtQueues.getText().trim());
            int simTime = Integer.parseInt(txtSimTime.getText().trim());
            int minArrival = Integer.parseInt(txtMinArrival.getText().trim());
            int maxArrival = Integer.parseInt(txtMaxArrival.getText().trim());
            int minService = Integer.parseInt(txtMinService.getText().trim());
            int maxService = Integer.parseInt(txtMaxService.getText().trim());

            SimulationManager manager = new SimulationManager(clients, queues, simTime, minArrival, maxArrival, minService, maxService,this);

            Thread simThread = new Thread(manager);
            simThread.start();
            isRunning = true;
            startButton.setEnabled(false);
            validateButton.setEnabled(false);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public void updateLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public void setRunning() {
        isRunning = false;
        validateButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();
        frame.setVisible(true);
    }
}

package GUI;

import BusinessLogic.SimulationManager;
import Model.Server;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationView extends JFrame {

    private SimulationManager simulationManager;
    private List<Server> servers;

    public SimulationView(SimulationManager manager) {
        this.simulationManager = manager;
        this.servers = manager.getServers();

        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateView() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawSimulation(g);
    }

    private void drawSimulation(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        g.drawString("Time: " + (simulationManager.getTime() - 1), 50, 70);
        g.drawString("Peak Hour: " + simulationManager.getPeakHour(), 150, 70);
        g.drawString("Average Waiting Time: " + simulationManager.getAvgWaitingTime(), 300, 70);

        g.drawString("Waiting Tasks:", 50, 110);

        int xOffset = 200;
        for (Task task : simulationManager.getGeneratedTasks()) {
            drawQueueTasks(g, task, xOffset, 80);
            xOffset += 110;
        }

        int yOffset = 160;
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(50, yOffset - 30, 120, 40);
            g.setColor(Color.WHITE);
            g.drawString("Queue " + (i + 1) + ":", 60, yOffset);

            xOffset = 180;
            synchronized (server.getTasks()) {
                for (Task task : server.getTasks()) {
                    drawQueueTasks(g, task, xOffset, yOffset - 30);
                    xOffset += 110;
                }
            }

            yOffset += 50;
        }
    }

    private void drawQueueTasks(Graphics g, Task task, int x, int y) {
        g.setColor(new Color(160, 160, 160));
        g.fillRect(x, y, 100, 40);
        g.setColor(Color.BLACK);
        g.drawString(task.getId() + "," + task.getArrivalTime() + "," + task.taskProcessingTime(), x + 20, y + 30);
    }
}

package BusinessLogic;

import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager implements Runnable {

    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServers = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    //private SimulationFrame frame;
    private List<Task> generatedTasks;

    public SimulationManager() {
        scheduler = new Scheduler(numberOfServers, maxProcessingTime);
        //=> create and start numberOfServers threads
        scheduler.changeStrategy(selectionPolicy);
        // initialize frame to display simulation
        generateNRandomTasks();
    }

    private void generateNRandomTasks(){
        generatedTasks = new ArrayList<>();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = (int) (Math.random() * (maxProcessingTime - minProcessingTime + 1)) + minProcessingTime;
            int arrivalTime = (int) (Math.random() * timeLimit);
            Task task = new Task(i,arrivalTime, processingTime);
            generatedTasks.add(task);
        }

        for (int i = 1; i < generatedTasks.size(); i++) {
            Task key = generatedTasks.get(i);
            int j = i - 1;

            while (j >= 0 && generatedTasks.get(j).getArrivalTime() > key.getArrivalTime()) {
                generatedTasks.set(j + 1, generatedTasks.get(j));
                j = j - 1;
            }

            generatedTasks.set(j + 1, key);
        }
    }

    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime < timeLimit){
            List<Task> tasksToDispatch = new ArrayList<>();
            for (Task task : generatedTasks) {
                if (task.getArrivalTime() == currentTime) {
                    tasksToDispatch.add(task);
                }
            }

            for (Task task : tasksToDispatch) {
                scheduler.dispachTask(task);
                generatedTasks.remove(task);
            }

            // update UI frame
            currentTime++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }
}

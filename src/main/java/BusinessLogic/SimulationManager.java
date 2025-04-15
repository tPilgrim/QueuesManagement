package BusinessLogic;

import GUI.SimulationView;
import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationManager implements Runnable {

    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int numberOfServers;
    public int numberOfClients;

    private Scheduler scheduler;
    private List<Task> generatedTasks;
    private BufferedWriter logWriter;

    private int currentTime;
    private SimulationView simulationView;

    private int peakHour = 0;
    private int maxTasksInQueues = 0;
    private float waitingTime = 0;
    private int dipachedTasks = 0;

    public SimulationManager(int numberOfClients, int numberOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, SelectionPolicy policy) {
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;

        scheduler = new Scheduler(numberOfServers);
        scheduler.changeStrategy(policy);
        generatedTasks = new ArrayList<>();
        simulationView = new SimulationView(this);

        generateNRandomTasks();

        try {
            logWriter = new BufferedWriter(new FileWriter("simulation_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateNRandomTasks(){
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = (int) (Math.random() * (maxProcessingTime - minProcessingTime + 1)) + minProcessingTime;
            int arrivalTime = (int) (Math.random() * (maxArrivalTime - minArrivalTime + 1)) + minArrivalTime;
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
        boolean emptyQueue = false;
        int activeQueue;

        while (currentTime <= timeLimit && !emptyQueue) {
            activeQueue = 0;
            for (int i = 0; i < scheduler.getServers().size(); i++) {
                List<Task> queueTasks = scheduler.getServers().get(i).getTasks();
                if (queueTasks.size()!=0) {
                    activeQueue++;
                }
            }

            if(generatedTasks.isEmpty() && activeQueue == 0){
                emptyQueue = true;
            }

            List<Task> tasksToDispatch = new ArrayList<>();
            for (Task task : generatedTasks) {
                if (task.getArrivalTime() <= currentTime) {
                    tasksToDispatch.add(task);
                }
            }

            for (Task task : tasksToDispatch) {
                scheduler.dispachTask(task);
                dipachedTasks++;
                generatedTasks.remove(task);
            }

            StringBuilder log = new StringBuilder();
            log.append("Time ").append(currentTime).append("\n");

            log.append("Waiting clients: ");
            if (generatedTasks.isEmpty()) {
                log.append("\n");
            } else {
                for (Task t : generatedTasks) {
                    log.append(String.format("(%d,%d,%d); ", t.getId(), t.getArrivalTime(), t.taskProcessingTime()));
                }
                log.append("\n");
            }

            for (int i = 0; i < scheduler.getServers().size(); i++) {
                log.append("Queue ").append(i + 1).append(": ");
                waitingTime += scheduler.getServers().get(i).getTasks().size();
                List<Task> queueTasks = scheduler.getServers().get(i).getTasks();
                synchronized (queueTasks){
                    for (Task t : queueTasks) {
                        int taskId = t.getId();
                        int arrival = t.getArrivalTime();
                        int service = t.taskProcessingTime();
                        log.append(String.format("(%d,%d,%d); ", taskId, arrival, service));
                    }
                }
                log.append("\n");
            }

            try {
                logWriter.write(log.toString());
                logWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int totalTasksInQueues = 0;
            for (Server server : scheduler.getServers()) {
                totalTasksInQueues += server.getNumberOfTasks();
            }
            if (totalTasksInQueues > maxTasksInQueues) {
                maxTasksInQueues = totalTasksInQueues;
                peakHour = currentTime;
            }

            currentTime++;

            simulationView.updateView();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            logWriter.write("Peak Hour: " + peakHour);
            logWriter.newLine();
            logWriter.write("Average Waiting Time: " + waitingTime/dipachedTasks);
            logWriter.newLine();
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scheduler.shutdown();
    }

    public List<Server> getServers() {
        return scheduler.getServers();
    }

    public List<Task> getGeneratedTasks() {
        return generatedTasks;
    }

    public int getTime(){
        return currentTime;
    }

    public int getPeakHour(){
        return  peakHour;
    }

    public float getAvgWaitingTime(){
        if(dipachedTasks == 0)
        {
            return 0;
        }
        else
        {
            return waitingTime/dipachedTasks;
        }
    }
}

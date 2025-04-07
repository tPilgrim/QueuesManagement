package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task newTask){
        this.tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.taskProcessingTime());
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (!tasks.isEmpty()) {
                    Task task = tasks.peek();
                    if (task != null) {
                        task.decrementServiceTime();

                        if (task.taskProcessingTime() == 0) {
                            tasks.take();
                            waitingPeriod.addAndGet(-task.taskProcessingTime());
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public int getNumberOfTasks() {
        return tasks.size();
    }
}
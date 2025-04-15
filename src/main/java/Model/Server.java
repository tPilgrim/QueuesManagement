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

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (tasks) {
                    if (!tasks.isEmpty()) {
                        Task task = tasks.peek();
                        if (task != null) {
                            task.decrementServiceTime();
                            waitingPeriod.decrementAndGet();

                            if (task.taskProcessingTime() == 0) {
                                tasks.take();
                            }
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<Task> getTasks() {
        synchronized (tasks) {
            return new ArrayList<>(tasks);
        }
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public int getNumberOfTasks() {
        synchronized (tasks) {
            return tasks.size();
        }
    }
}
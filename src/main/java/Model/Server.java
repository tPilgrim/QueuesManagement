package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        this.tasks = null;
        this.waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task newTask){
        this.tasks.add(newTask);
        waitingPeriod.incrementAndGet();
    }

    public void run(){
        Thread nextTask = new Thread(() -> {
            try {
                while (true) {
                    Task task = tasks.take();
                    Thread.sleep(task.taskProcessingTime());
                    waitingPeriod.decrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public Task[] getTasks(){
        return this.tasks.toArray(new Task[0]);
    }
}
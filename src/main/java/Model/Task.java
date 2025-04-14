package Model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public synchronized int taskProcessingTime(){
        return serviceTime;
    }

    public synchronized int getArrivalTime() {
        return arrivalTime;
    }

    public int getId() {
        return id;
    }

    public synchronized void decrementServiceTime() {
        serviceTime--;
    }
}

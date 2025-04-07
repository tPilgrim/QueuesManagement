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

    public int taskProcessingTime(){
        return serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void decrementServiceTime() {
        serviceTime--;
    }
}

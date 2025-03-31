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
        return 0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}

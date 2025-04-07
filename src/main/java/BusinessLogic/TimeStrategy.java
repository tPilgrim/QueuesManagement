package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server server = null;
        int minWaitingTime = Integer.MAX_VALUE;

        for (Server s : servers) {
            int waitingTime = s.getWaitingPeriod().get();
            if (waitingTime < minWaitingTime) {
                minWaitingTime = waitingTime;
                server = s;
            }
        }

        if (server != null) {
            server.addTask(t);
        }
    }
}

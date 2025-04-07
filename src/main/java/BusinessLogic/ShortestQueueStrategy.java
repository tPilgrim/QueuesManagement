package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server server = null;
        int minQueueSize = Integer.MAX_VALUE;

        for (Server s : servers) {
            int queueSize = s.getNumberOfTasks();
            if (queueSize < minQueueSize) {
                minQueueSize = queueSize;
                server = s;
            }
        }

        if (server != null) {
            server.addTask(t);
        }
    }
}

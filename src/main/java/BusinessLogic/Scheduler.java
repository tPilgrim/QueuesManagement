package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private Strategy strategy;
    private ExecutorService executor;

    public Scheduler(int maxNoServers) {
        this.maxNoServers = maxNoServers;
        this.servers = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(maxNoServers);

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            executor.submit(server);
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public void dispachTask(Task t){
        strategy.addTask(servers,t);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}

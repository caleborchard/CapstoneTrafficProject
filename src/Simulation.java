import java.util.*;

public class Simulation {
    private double currentTime;
    private LoopingQueue<Station> globalStationQueue;
    VehicleInfo trainInfo;
    SimulationConfig simConfig;

    public Simulation(String stationConfigFile, SimulationConfig simConfigIn) {
        simConfig = simConfigIn;
        globalStationQueue = new LoopingQueue<Station>();
        currentTime = 0.0;
        Config config = new Config();
        List<Config.StationConfig> stationConfigs = config.getStationConfigs(stationConfigFile);
        for(Config.StationConfig s : stationConfigs) {
            globalStationQueue.enqueue(new Station(
                    s.getStationName(),
                    s.getOriginDistance(),
                    s.getPopulation(),
                    s.getNumWorkers(),
                    new VehicleInfo(
                            simConfig.busCapacity,
                            simConfig.numBusses,
                            simConfig.busSpeed
                    )
            ));
        }
        trainInfo = new VehicleInfo(simConfig.trainCapacity, simConfig.numTrains, simConfig.trainSpeed);
    }

    public OutputDataConfig run(int stops) {
        List<BatchServerQueue> trains = new ArrayList<>();
        for(int i = 0; i < simConfig.numTrains; i++) {
            LoopingQueue<Station> newQueue = globalStationQueue.cloneQueue();
            for(int j = 0; j < i; j++) { newQueue.dequeue(); } //TODO: Find optimal train placement and direction

            trains.add(new BatchServerQueue(trainInfo, newQueue));
        }

        for (int i = 0; i <= stops; i++) {
            double maxTravelTime = 0.0;
            Map<BatchServerQueue, Double> travelTimes = new HashMap<>();

            // First pass: calculate travel times and find max
            for (BatchServerQueue t : trains) {
                double travelTime = t.stopAtStation(currentTime + t.getTimeOffset());
                travelTimes.put(t, travelTime);
                maxTravelTime = Math.max(travelTime, maxTravelTime);
            }
            currentTime += maxTravelTime;

            // Second pass: update offsets
            for (BatchServerQueue t : trains) {
                double travelTime = travelTimes.get(t);
                t.setTimeOffset(t.getTimeOffset() + travelTime - maxTravelTime);
                //System.out.println(t.toString() + ", CurrentTime=" + (currentTime + t.getTimeOffset()));
            }
        }

        // collecting metrics across all trains
        int totalCompletedJobs = trains.stream().mapToInt(BatchServerQueue::getCompletedJobs).sum();
        double cumulativeServiceTime = trains.stream().mapToDouble(BatchServerQueue::getTotalServiceTime).sum();
        double avgServiceTime = totalCompletedJobs > 0 ? cumulativeServiceTime / totalCompletedJobs : 0.0;
        double longestServiceTime  = trains.stream().mapToDouble(BatchServerQueue::getTotalServiceTime).max().orElse(0.0);

        return new OutputDataConfig(
                simConfig.numTrains,
                simConfig.numBusses,
                avgServiceTime,
                longestServiceTime,
                totalCompletedJobs
        );
    }

    //TODO: Add unit tests
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Simulation {
    private double currentTime;
    private LoopingQueue<Station> globalStationQueue;
    VehicleInfo trainInfo = new VehicleInfo(
            500,
            Integer.MAX_VALUE, //Does not do anything for trains
            250
    );
    int numTrains = 1; //Not currently used

    public Simulation(String stationConfigFile) {
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
                            s.getBusCapacity(),
                            s.getNumBusses(),
                            s.getBusSpeed()
                    )
            ));
        }
    }

    public void run(int stops) {
        for(int i = 0; i <= stops; i++) {
            BatchServerQueue train = new BatchServerQueue(trainInfo, globalStationQueue);
            double travelTime = train.stopAtStation(currentTime);
            currentTime += travelTime;
        }
    }

    //TODO: Add unit tests
}

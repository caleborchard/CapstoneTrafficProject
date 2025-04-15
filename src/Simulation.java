import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Simulation {
    private double currentTime;
    private LoopingQueue<Station> globalStationQueue;

    public Simulation(String stationConfigFile) {
        currentTime = 0.0;
        Config config = new Config();
        List<Config.StationConfig> stationConfigs = config.getStationConfigs(stationConfigFile);
    }

    public void run(int stops) {
    }

    //CSV Format: stationName,originDistance,population,numWorkers,busCapacity,busFrequency,busSpeed.
    //TODO: Test code, add unit tests, finish rest of class :)
}

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Config {
    protected static class StationConfig {
        private String stationName;
        private double originDistance;
        private int population;
        private int numWorkers;

        public StationConfig(String stationName, double originDistance, int population, int numWorkers) {
            this.stationName = stationName;
            this.originDistance = originDistance;
            this.population = population;
            this.numWorkers = numWorkers;
        }

        // Getters for each field
        public String getStationName() { return stationName; }
        public double getOriginDistance() { return originDistance; }
        public int getPopulation() { return population; }
        public int getNumWorkers() { return numWorkers; }
    }

    public List<StationConfig> getStationConfigs(String stationConfigFile) {
        return loadStationConfigs(stationConfigFile);
    }

    //CSV Format: stationName,originDistance,population,numWorkers,busCapacity,numBusses,busSpeed.
    private List<StationConfig> loadStationConfigs(String stationConfigFile) {
        List<StationConfig> stationConfigs = new ArrayList<>();
        try {
            File file = new File(stationConfigFile);
            Scanner configReader = new Scanner(file);
            configReader.nextLine();
            while (configReader.hasNextLine()) {
                String[] configData = configReader.nextLine().split(",");
                if(configData.length < 4) continue;

                String stationName = configData[0];
                double originDistance = Double.parseDouble(configData[1]);
                int population = Integer.parseInt(configData[2]);
                int numWorkers = Integer.parseInt(configData[3]);

                stationConfigs.add(new StationConfig(stationName, originDistance, population, numWorkers));
            }
            configReader.close();
            System.out.println("Number of stations: " + stationConfigs.size());
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        }
        return stationConfigs;
    }
}

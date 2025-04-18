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
        private int busCapacity;
        private int busFrequency;
        private int busSpeed;

        public StationConfig(String stationName, double originDistance, int population, int numWorkers,
                             int busCapacity, int busFrequency, int busSpeed) {
            this.stationName = stationName;
            this.originDistance = originDistance;
            this.population = population;
            this.numWorkers = numWorkers;
            this.busCapacity = busCapacity;
            this.busFrequency = busFrequency;
            this.busSpeed = busSpeed;
        }

        // Getters for each field
        public String getStationName() { return stationName; }
        public double getOriginDistance() { return originDistance; }
        public int getPopulation() { return population; }
        public int getNumWorkers() { return numWorkers; }
        public int getBusCapacity() { return busCapacity; }
        public int getBusFrequency() { return busFrequency; }
        public int getBusSpeed() { return busSpeed; }
    }

    public List<StationConfig> getStationConfigs(String stationConfigFile) {
        return loadStationConfigs(stationConfigFile);
    }

    private List<StationConfig> loadStationConfigs(String stationConfigFile) {
        List<StationConfig> stationConfigs = new ArrayList<>();
        try {
            File file = new File(stationConfigFile);
            Scanner configReader = new Scanner(file);
            configReader.nextLine();
            while (configReader.hasNextLine()) {
                String[] configData = configReader.nextLine().split(",");
                if(configData.length < 7) continue;

                String stationName = configData[0];
                double originDistance = Double.parseDouble(configData[1]);
                int population = Integer.parseInt(configData[2]);
                int numWorkers = Integer.parseInt(configData[3]);
                int busCapacity = Integer.parseInt(configData[4]);
                int busFrequency = Integer.parseInt(configData[5]);
                int busSpeed = Integer.parseInt(configData[6]);

                stationConfigs.add(new StationConfig(stationName, originDistance, population, numWorkers, busCapacity, busFrequency, busSpeed));
            }
            configReader.close();;
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        }
        return stationConfigs;
    }
}

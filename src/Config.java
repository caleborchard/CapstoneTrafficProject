import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
            //System.out.println("Number of stations: " + stationConfigs.size());
        } catch (FileNotFoundException e) {
            //System.out.println("Invalid file path");
        }
        return stationConfigs;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("Config");

        // Test Case 1: Valid CSV file with correct data
        try {
            File tempFile = File.createTempFile("testConfigValid", ".csv");
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.println("stationName,originDistance,population,numWorkers");
                writer.println("StationA,100.5,5000,200");
                writer.println("StationB,200.0,6000,300");
            }
            Config config = new Config();
            List<StationConfig> stations = config.getStationConfigs(tempFile.getAbsolutePath());

            // Check number of stations loaded
            result.recordNewTask(stations.size() == 2);

            // Validate StationA data
            if (stations.size() > 0) {
                StationConfig stationA = stations.get(0);
                result.recordNewTask("StationA".equals(stationA.getStationName()));
                result.recordNewTask(stationA.getOriginDistance() == 100.5);
                result.recordNewTask(stationA.getPopulation() == 5000);
                result.recordNewTask(stationA.getNumWorkers() == 200);
            } else {
                result.recordNewTask(false);
                result.recordNewTask(false);
                result.recordNewTask(false);
                result.recordNewTask(false);
            }

            // Validate StationB data
            if (stations.size() > 1) {
                StationConfig stationB = stations.get(1);
                result.recordNewTask("StationB".equals(stationB.getStationName()));
                result.recordNewTask(stationB.getOriginDistance() == 200.0);
                result.recordNewTask(stationB.getPopulation() == 6000);
                result.recordNewTask(stationB.getNumWorkers() == 300);
            } else {
                result.recordNewTask(false);
                result.recordNewTask(false);
                result.recordNewTask(false);
                result.recordNewTask(false);
            }

            tempFile.delete();
        } catch (IOException e) {
            for (int i = 0; i < 9; i++) result.recordNewTask(false);
        }

        // Test Case 2: Invalid file path handling
        String invalidPath = "invalid_path.csv";
        Config config = new Config();
        List<StationConfig> stations = config.getStationConfigs(invalidPath);
        result.recordNewTask(stations.isEmpty());

        // Test Case 3: CSV with invalid lines
        try {
            File tempFile = File.createTempFile("testConfigInvalid", ".csv");
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.println("stationName,originDistance,population,numWorkers");
                writer.println("StationC,150.0,4000,100");
                writer.println("InvalidLine,300,500");
                writer.println("StationD,250.0,7000,350");
            }
            List<StationConfig> stations3 = config.getStationConfigs(tempFile.getAbsolutePath());

            result.recordNewTask(stations3.size() == 2);  // Should skip invalid line
            result.recordNewTask(stations3.get(0).getStationName().equals("StationC"));
            result.recordNewTask(stations3.get(1).getStationName().equals("StationD"));

            tempFile.delete();
        } catch (IOException e) {
            result.recordNewTask(false);
            result.recordNewTask(false);
            result.recordNewTask(false);
        }

        return result;
    }
}

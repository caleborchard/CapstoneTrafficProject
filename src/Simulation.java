import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Simulation {
    private double currentTime;
    private LoopingQueue<Station> globalStationQueue;

    public Simulation(String configDirectory)
    {
        currentTime = 0.0;
        loadStationConfig(configDirectory);
    }

    public void run(int stops) {
    }

    //CSV Format: stationName,originDistance,population,numWorkers,busCapacity,busFrequency,busSpeed.
    //TODO: Test code, add unit tests, finish rest of class :)
    private void loadStationConfig(String stationConfigDirectory) {
        try {
            File configFile = new File(stationConfigDirectory);
            Scanner configReader = new Scanner(configFile);
            configReader.nextLine();
            while(configReader.hasNextLine()) {
                String[] configData = configReader.nextLine().split(",");
                //Enum could be used for this next part for better readability, but code would be more verbose
                globalStationQueue.enqueue(new Station(configData[0], Double.parseDouble(configData[1]), Integer.parseInt(configData[2]),
                        Integer.parseInt(configData[3]), new VehicleInfo(Integer.parseInt(configData[4]), Integer.parseInt(configData[5]), Integer.parseInt(configData[6]))));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        }
    }
}

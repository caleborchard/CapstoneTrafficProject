import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        out.print("Please enter your station configuration file directory:");

        Scanner ln = new Scanner(System.in);
        String stationConfigFile = ln.nextLine();
        if(stationConfigFile.isEmpty()) { stationConfigFile = "C:\\JavaProjects\\CapstoneTrafficProject\\stations.csv"; } //Caleb: So that I don't have to paste it in each time for myself
        // Just a handful of (trains, buses) combos to smoke-test
        int[][] vehicleNumber = {
                {1, 1},
                {1, 10},
                {1, 20},
                {1, 30},
                {1, 40},
                {1, 50},
                {2, 10},
                {2, 20},
                {3, 30},
                {4, 40},
                {5, 10},
                {5, 50}
        };

        List<OutputDataConfig> results = new ArrayList<>();

        for (int[] num : vehicleNumber) {
            int numTrains = num[0];
            int numBuses  = num[1];

            SimulationConfig simulationConfig = new SimulationConfig(
                    numTrains,
                    500,
                    250,
                    numBuses,
                    50,
                    15
            );

                Simulation simulation = new Simulation(stationConfigFile, simulationConfig);
                results.add(simulation.run(50));
                System.out.println();
        }

        System.out.println("\nSimulation results: ");
        results.forEach(System.out::println);

        String csvPath = "results.csv";
        writeResultsCsv(csvPath, results);

    }

    private static void writeResultsCsv(String filePath, List<OutputDataConfig> results) {
        // header row
        String header = "Trains,Buses,AvgServiceTime,LongestServiceTime,CompletedJobs";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(header);
                writer.newLine();

                for (OutputDataConfig o : results) {
                    // build a CSV line from the fields
                    String line = String.format(
                            "%d,%d,%.2f,%.2f,%d",
                            o.numTrains,
                            o.numBuses,
                            o.avgServiceTime,
                            o.longestServiceTime,
                            o.totalCompletedJobs
                    );
                    writer.write(line);
                    writer.newLine();
                }

            } catch (IOException e) {
                System.err.println("Error writing to CSV file: " + e.getMessage());
            }
    }
}


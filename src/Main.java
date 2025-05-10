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

        int numTrainsRange = 16;
        int numBussesRange = 50;
        int[][] vehicleNumber = new int[numTrainsRange * numBussesRange][2];
        int index = 0;
        for(int train = 1; train <= numTrainsRange; train++) {
            for(int bus = 1; bus <= numBussesRange; bus++) {
                vehicleNumber[index][0] = train;
                vehicleNumber[index][1] = bus;
                index++;
            }
        }

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
                results.add(simulation.run(100));
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


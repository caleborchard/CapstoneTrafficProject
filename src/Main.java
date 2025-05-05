import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        out.println("Please enter your station configuration file directory:");

        Scanner ln = new Scanner(System.in);
        String stationConfigFile = ln.nextLine();
        if(stationConfigFile.isEmpty()) { stationConfigFile = "C:\\JavaProjects\\CapstoneTrafficProject\\stations.csv"; } //Caleb: So that I don't have to paste it in each time for myself

        List<OutputDataConfig> results = new ArrayList<>();
        for (int trains = 1; trains <= 5; trains++) {
            for (int buses = 10; buses <= 50; buses +=10) {
                //Simulation variables, change em up each time according to however we decide.
                SimulationConfig simulationConfig = new SimulationConfig(
                        trains,
                        500,
                        250,
                        buses,
                        50,
                        30
                );

                Simulation simulation = new Simulation(stationConfigFile, simulationConfig);
                OutputDataConfig out = simulation.run(30);
                results.add(out);
            }
        }

        System.out.println("\nSimulation results: ");
        for (OutputDataConfig o : results) {
            System.out.println(o);
        }

    }
}
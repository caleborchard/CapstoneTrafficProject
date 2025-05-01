import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Please enter your station configuration file directory:");

        Scanner ln = new Scanner(System.in);
        String stationConfigFile = ln.nextLine();
        if(stationConfigFile.isEmpty()) { stationConfigFile = "C:\\JavaProjects\\CapstoneTrafficProject\\stations.csv"; } //Caleb: So that I don't have to paste it in each time for myself

        //Simulation variables, change em up each time according to however we decide.
        SimulationConfig simulationConfig = new SimulationConfig(
                2,
                500,
                250,
                15,
                50,
                30
        );

        System.out.println("Reading from " + stationConfigFile);
        Simulation simulation = new Simulation(stationConfigFile, simulationConfig);
        simulation.run(15);
    }
}
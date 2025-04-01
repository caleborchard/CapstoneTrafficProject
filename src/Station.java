import java.util.ArrayList;
import java.util.List; //I'm just using the default Java list even though it isn't the best because we don't need to reinvent the wheel for this project.

public class Station {
    private String name;
    private double distanceFromOriginStation;
    private int population;
    private int numWorkers;
    private VehicleInfo busInfo;

    public String getName() { return name; }
    public int getPopulation() { return population; }
    public int getNumWorkers() { return numWorkers; }

    private double lastPickupTime;

    public double getLastPickupTime() { return lastPickupTime; }

    public Station(String stationName, double originDistance, int pop, int numWorkers, VehicleInfo busInfoIn) {
        name = stationName;
        distanceFromOriginStation = originDistance;
        population = pop;
        busInfo = busInfoIn;

        lastPickupTime = 0;
    }

    public Job[] getBusArrivals(double currentTime) {
        List<Job> busArrivals = new ArrayList<>();
        double busTime = getLastPickupTime();

        while (busTime < currentTime) {
            for (int i = 0; i < busInfo.getVehicleCapacity(); i++) {
                busArrivals.add(new Job(busTime, getName()));
            }
            busTime += busInfo.getArrivalFrequency(); // Move update after adding jobs
        }

        return busArrivals.toArray(new Job[0]); // Convert to array
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult();

        Station station1 = new Station("frederick", 0, 1000, 1000, new VehicleInfo(10, 5));
        result.recordNewTask(station1.getBusArrivals(10).length == 20);

        return result;
    }
}

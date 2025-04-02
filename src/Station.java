import java.util.ArrayList;
import java.util.List; //I'm just using the default Java list even though it isn't the best because we don't need to reinvent the wheel for this project.

public class Station {
    private String name;
    private double distanceFromOriginStation; //In kilometers
    private int population;
    private int numWorkers;
    private VehicleInfo busInfo;
    public Queue<Job> stationWaiters;

    public String getName() { return name; }
    public int getPopulation() { return population; }
    public int getNumWorkers() { return numWorkers; }
    public double getDistanceFromOriginStation() { return distanceFromOriginStation; }

    private double lastPickupTime;

    public double getLastPickupTime() { return lastPickupTime; }

    public Station(String stationName, double originDistance, int pop, int numWorkers, VehicleInfo busInfoIn) {
        name = stationName;
        distanceFromOriginStation = originDistance;
        population = pop;
        busInfo = busInfoIn;
        stationWaiters = new Queue<Job>();

        lastPickupTime = 0;
    }

    public void getBusArrivals(double currentTime) {
        List<Job> busArrivals = new ArrayList<>();
        double busTime = getLastPickupTime();

        while (busTime < currentTime) {
            for (int i = 0; i < busInfo.getVehicleCapacity(); i++) {
                busArrivals.add(new Job(busTime, getName())); //TODO: Departing station
            }
            busTime += busInfo.getArrivalFrequency(); // Move update after adding jobs
        }

        for(Job j : busArrivals) {
            stationWaiters.enqueue(j);
        }
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("Station");

        Station station1 = new Station("frederick", 0, 1000, 1000, new VehicleInfo(10, 5, 50));
        station1.getBusArrivals(10);
        result.recordNewTask(station1.stationWaiters.length == 20);

        return result;
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        this.numWorkers = numWorkers;

        lastPickupTime = 0;
    }

    public void getBusArrivals(double currentTime, CityInfoHolder[] cityInfo) {
        List<Job> busArrivals = new ArrayList<>();
        double busTime = getLastPickupTime();

        while (busTime < currentTime) {
            for (int i = 0; i < busInfo.getVehicleCapacity(); i++) {
                busArrivals.add(new Job(busTime, getName(), pickStation(cityInfo)));
            }
            busTime += busInfo.getArrivalFrequency();
        }

        for(Job j : busArrivals) {
            stationWaiters.enqueue(j);
        }
    }

    private String pickStation(CityInfoHolder[] cityInfo) {
        int totalWorkers = 0;
        for(CityInfoHolder c : cityInfo) {
            if(!c.getName().equals(getName())) totalWorkers += c.getNumWorkers();
        }

        if (totalWorkers == 0) return null;

        Random r = new Random();
        int randInt = r.nextInt(totalWorkers);
        for(CityInfoHolder c : cityInfo) {
            if(!c.getName().equals(getName())) {
                randInt -= c.getNumWorkers();
                if(randInt < 0) return c.getName();
            }
        }
        return null;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("Station");

        Station station1 = new Station("frederick", 0, 1000, 1000, new VehicleInfo(10, 5, 50));
        station1.getBusArrivals(10, new CityInfoHolder[] {new CityInfoHolder("frederick", 1000)});
        result.recordNewTask(station1.stationWaiters.length == 20);

        return result;
    }
}

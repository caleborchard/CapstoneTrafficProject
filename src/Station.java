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

    private final double lambda;
    private Queue<Job> busStopWaiters = new Queue<>();
    private ExponentialDistribution arrivalDistribution;

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
        lambda = getPopulation()/((double)11985);
        //TODO: This is a temporary solution to get lambda value, decide with team best approach to this.
        arrivalDistribution = new ExponentialDistribution(lambda);
    }

    public void generateBusStopWaiters(double startTime, double endTime, CityInfoHolder[] cityInfo) {
        double currentTime = startTime;

        while (currentTime < endTime) {
            Job job = new Job(currentTime, getName(), pickStation(cityInfo));
            busStopWaiters.enqueue(job);

            double nextArrival = arrivalDistribution.sample();
            currentTime += nextArrival;
            //System.out.println(nextArrival);
            if (currentTime >= endTime) break;
        }
    }

    public void getBusArrivals(double currentTime, CityInfoHolder[] cityInfo) {
        generateBusStopWaiters(getLastPickupTime(), currentTime, cityInfo);
        double busTime = getLastPickupTime();

        while (busTime < currentTime) {
            int capacity = busInfo.getVehicleCapacity();
            int count = 0;

            while (!busStopWaiters.isQueueEmpty() && count < capacity) {
                Job job = busStopWaiters.current.value;
                if (job.getTimeOfCreation() <= busTime) {
                    stationWaiters.enqueue(busStopWaiters.dequeue());
                    count++;
                } else { break; }
            }
            busTime += busInfo.getTravelTime(5, 18);
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
        //TODO: Make unit tests for Station class.

        return result;
    }
}

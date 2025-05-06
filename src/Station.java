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
        lambda = getPopulation()/((double)11985); //TODO: Retrieve median population instead of hardcoded value, maybe add multiplier
        arrivalDistribution = new ExponentialDistribution(lambda);
    }

    public void generateBusStopWaiters(double startTime, double endTime, CityInfoHolder[] cityInfo) {
        double localCurrentTime = startTime;

        while (localCurrentTime < endTime) {
            Job job = new Job(localCurrentTime, getName(), pickStation(cityInfo));
            busStopWaiters.enqueue(job);

            double nextArrival = arrivalDistribution.sample();
            localCurrentTime += nextArrival;
            //System.out.println(nextArrival);
            if (localCurrentTime >= endTime) break;
        }
    }

    public void getBusArrivals(double currentTime, CityInfoHolder[] cityInfo) {
        generateBusStopWaiters(getLastPickupTime(), currentTime, cityInfo);
        double busTime = getLastPickupTime();

        while (busTime < currentTime) {
            int numVehicles = busInfo.getNumVehicles();
            int capacityPerBus = busInfo.getVehicleCapacity();

            for (int i = 0; i < numVehicles; i++) {
                int count = 0;

                while (!busStopWaiters.isQueueEmpty() && count < capacityPerBus) {
                    Job job = busStopWaiters.current.value;
                    if (job.getTimeOfCreation() <= busTime) {
                        stationWaiters.enqueue(busStopWaiters.dequeue());
                        count++;
                    } else {
                        break;
                    }
                }
            }

            double travelTime = busInfo.getTravelTime(5, 18);
            busTime += travelTime;
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

        // Test 1: Constructor initializes fields correctly
        try {
            Station station = new Station("TestStation", 5.5, 2000, 500, new VehicleInfo(20, 10, 30));
            assert station.getName().equals("TestStation") : "Name not initialized correctly";
            assert station.getDistanceFromOriginStation() == 5.5 : "Distance not initialized correctly";
            assert station.getPopulation() == 2000 : "Population not initialized correctly";
            assert station.getNumWorkers() == 500 : "NumWorkers not initialized correctly";
            assert station.getLastPickupTime() == 0 : "LastPickupTime not initialized to 0";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 2: pickStation selects the only other city
        try {
            Station station = new Station("A", 0, 100, 50, new VehicleInfo(10, 5, 20));
            CityInfoHolder[] cities = { new CityInfoHolder("A", 50), new CityInfoHolder("B", 100) };
            String picked = station.pickStation(cities);
            assert picked != null && picked.equals("B") : "pickStation did not select the only other city";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 3: getBusArrivals updates lastPickupTime
        try {
            Station station = new Station("Test", 0, 1000, 500, new VehicleInfo(10, 5, 50));
            CityInfoHolder[] cities = { new CityInfoHolder("Test", 500) };
            station.getBusArrivals(10.0, cities);
            assert station.getLastPickupTime() > 0 : "lastPickupTime not updated after getBusArrivals";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 4: generateBusStopWaiters with zero time range creates no jobs
        try {
            Station station = new Station("Test", 0, 1000, 500, new VehicleInfo(10, 5, 50));
            CityInfoHolder[] cities = { new CityInfoHolder("Test", 500) };
            station.getBusArrivals(0.0, cities);
            assert station.stationWaiters.isQueueEmpty() : "stationWaiters should be empty when time range is zero";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 5: Bus capacity limits jobs moved to stationWaiters (example with capacity 2)
        try {
            VehicleInfo busInfo = new VehicleInfo(2, 5, 50);
            Station station = new Station("Test", 0, 100000, 500, busInfo); // High population to generate jobs
            CityInfoHolder[] cities = { new CityInfoHolder("Test", 500), new CityInfoHolder("Other", 500) };

            // Assuming generateBusStopWaiters produces jobs; process up to time 10
            station.getBusArrivals(10.0, cities);

            // Check if the number of jobs in stationWaiters does not exceed possible bus arrivals
            // This is a heuristic check as exact numbers depend on random distributions
            int jobCount = 0;
            while (!station.stationWaiters.isQueueEmpty()) {
                station.stationWaiters.dequeue();
                jobCount++;
            }
            int maxPossible = (int) (10.0 / busInfo.getTravelTime(5, 18)) * busInfo.getVehicleCapacity();
            assert jobCount <= maxPossible : "stationWaiters exceeded expected job count based on bus capacity";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        return result;
    }
}

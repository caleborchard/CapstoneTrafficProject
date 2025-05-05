import java.util.ArrayList;
import java.util.List;

public class BatchServerQueue {
    private final LoopingQueue<Station> stationQueue;
    private final Queue<Job> currentPassengers;
    private Station currentStation;
    //private double nextStopTime;
    private final VehicleInfo trainInfo;
    private double distanceFromOriginStation = 0;
    private double timeOffset = 0.0;

    public int passengerCount() { return currentPassengers.getLength(); }
    public double getTimeOffset() { return timeOffset; }
    public void setTimeOffset(double offset) { this.timeOffset = offset; }
    public Station getCurrentStation() { return currentStation; }

    public BatchServerQueue(VehicleInfo vehicleInfo, LoopingQueue<Station> stationQueue) {
        this.stationQueue = stationQueue;
        currentPassengers = new Queue<>();
        trainInfo = vehicleInfo;
        currentStation = null;
        //nextStopTime = Double.MAX_VALUE;
    }

    public double stopAtStation(double currentTime) {
        currentStation = stationQueue.dequeue();

        double stationDistanceFromOrigin = currentStation.getDistanceFromOriginStation();
        double distanceToTravel = Math.abs(stationDistanceFromOrigin - distanceFromOriginStation);
        double timeToTravel = (distanceToTravel / trainInfo.getVehicleSpeed())*60;
        distanceFromOriginStation = stationDistanceFromOrigin;

        currentStation.getBusArrivals(currentTime, stationQueue.getStationNames());

        Job deq = currentStation.stationWaiters.dequeue();
        while(deq != null && currentPassengers.length < trainInfo.getVehicleCapacity()) {
            currentPassengers.enqueue(deq);
            deq = currentStation.stationWaiters.dequeue();
        }

        List<Job> passengerList = new ArrayList<>();
        for(int i = 0; i < currentPassengers.length; i++) {
            passengerList.add(currentPassengers.dequeue());
        }
        for(Job j : passengerList) {
            if(currentStation.getName().equals(j.getDestStation())) {
                j.complete(currentTime);
                //System.out.println("Completed:" + j.getOnboardingStation() + " to " + j.getDestStation() + " in " + (j.getServiceEndTime()-j.getTimeOfCreation()) + " minutes.");
            } else { currentPassengers.enqueue(j); }
        }
        return timeToTravel;
    }

    public String toString() { return "Stopping at " + currentStation.getName() + ". Number of passengers: " + passengerCount(); }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("BatchServerQueue");

        // Test 1: Initialization
        try {
            VehicleInfo vehicleInfo = new VehicleInfo(50, 1, 10); // Capacity=50, Count=1, Speed=10
            LoopingQueue<Station> stations = new LoopingQueue<>();
            stations.enqueue(new Station("A", 0, 100, 50, null)); // Name, Distance, Pop, Workers, BusInfo
            stations.enqueue(new Station("B", 10, 200, 100, null));
            BatchServerQueue queue = new BatchServerQueue(vehicleInfo, stations);
            assert queue.getCurrentStation() == null;
            assert queue.passengerCount() == 0;
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 2: Time Calculation Between Stations
        try {
            VehicleInfo vehicleInfo = new VehicleInfo(50, 1, 50); // Speed=50 km/h
            VehicleInfo busInfo = new VehicleInfo(10, 5, 50);
            LoopingQueue<Station> stations = new LoopingQueue<>();
            stations.enqueue(new Station("A", 10, 0, 0, busInfo));
            stations.enqueue(new Station("B", 30, 0, 0, busInfo));
            BatchServerQueue queue = new BatchServerQueue(vehicleInfo, stations);

            double timeToTravel = queue.stopAtStation(0.0);
            assert timeToTravel == 24.0 : "Expected 24.0 minutes, got " + timeToTravel; // (30-10)/50 * 60 = 24
            assert queue.distanceFromOriginStation == 10;

            timeToTravel = queue.stopAtStation(timeToTravel);
            assert timeToTravel == 0.0; // Loop back to first station (distance 10 → 30 → 10)
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 3: Loading Passengers Up To Capacity
        try {
            VehicleInfo vehicleInfo = new VehicleInfo(2, 1, 50); // Capacity=2
            Station stationA = new Station("A", 0, 0, 0, null);
            stationA.stationWaiters.enqueue(new Job(0.0, "A", "B"));
            stationA.stationWaiters.enqueue(new Job(0.0, "A", "C"));
            stationA.stationWaiters.enqueue(new Job(0.0, "A", "D"));

            LoopingQueue<Station> stations = new LoopingQueue<>();
            stations.enqueue(stationA);
            stations.enqueue(new Station("B", 10, 0, 0, null));

            BatchServerQueue queue = new BatchServerQueue(vehicleInfo, stations);
            queue.stopAtStation(0.0);

            assert queue.passengerCount() == 2 : "Expected 2 passengers, got " + queue.passengerCount();
            assert stationA.stationWaiters.getLength() == 1 : "Expected 1 waiter remaining";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        // Test 4: Unloading Passengers At Destination
        try {
            VehicleInfo busInfo = new VehicleInfo(10, 5, 50);
            Station stationA = new Station("A", 0, 0, 0, busInfo);
            Station stationB = new Station("B", 10, 0, 0, busInfo);
            Job job1 = new Job(0.0, "A", "B");
            Job job2 = new Job(0.0, "A", "C");

            stationA.stationWaiters.enqueue(job1);
            stationA.stationWaiters.enqueue(job2);

            LoopingQueue<Station> stations = new LoopingQueue<>();
            stations.enqueue(stationA);
            stations.enqueue(stationB);

            BatchServerQueue queue = new BatchServerQueue(new VehicleInfo(10, 1, 50), stations);
            queue.stopAtStation(0.0); // Load all 2 passengers (capacity=10)
            queue.stopAtStation(12.0); // Stop at B

            assert queue.passengerCount() == 1 : "Expected 1 passenger remaining";
            assert job1.getServiceEndTime() == 12.0 : "Job1 should complete at 12.0";
            result.recordNewTask(true);
        } catch (AssertionError e) {
            result.recordNewTask(false);
        }

        return result;
    }
}
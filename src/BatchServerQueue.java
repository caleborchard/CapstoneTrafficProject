import java.util.ArrayList;
import java.util.List;

public class BatchServerQueue {
    private final LoopingQueue<Station> stationQueue;
    private final Queue<Job> currentPassengers;
    private Station currentStation;
    //private double nextStopTime;
    private final VehicleInfo trainInfo;
    private double distanceFromOriginStation = 0;

    public BatchServerQueue(VehicleInfo vehicleInfo, LoopingQueue<Station> stationQueue) {
        this.stationQueue = stationQueue;
        currentPassengers = new Queue<>();
        trainInfo = vehicleInfo;
        currentStation = null;
        //nextStopTime = Double.MAX_VALUE;
    }

    public int passengerCount() { return currentPassengers.getLength(); }

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

        //TODO: Fix departure code.
        List<Job> passengerList = new ArrayList<>();
        for(int i = 0; i < currentPassengers.length; i++) {
            passengerList.add(currentPassengers.dequeue());
        }
        for(Job j : passengerList) {
            if(currentStation.getName().equals(j.getDestStation())) {
                j.complete(currentTime);
                System.out.println("!!!"+j+"!!!");
            } else { currentPassengers.enqueue(j); }
        }
        System.out.println("Stopping at " + currentStation.getName() + ". Number of passengers: " + passengerCount() + ", CurrentTime=" + currentTime);
        return timeToTravel;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("BatchServerQueue");

        double currentTime = 0.0;
        LoopingQueue<Station> globalStationQueue = new LoopingQueue<>();
        for(int i = 1; i <= 10; i++) {
            globalStationQueue.enqueue(new Station("stationNum" + i, i * 50, 1, 1, new VehicleInfo(30, 15, 50)));
        }

        BatchServerQueue train1 = new BatchServerQueue(new VehicleInfo(500, 30, 250), globalStationQueue);

        for(int i = 0; i < 49; i++) {
            currentTime += train1.stopAtStation(currentTime);
        }
        return result;
    }
}

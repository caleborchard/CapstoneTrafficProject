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
    private int completedJobs = 0;
    private double totalServiceTime = 0.0;

    public int passengerCount() { return currentPassengers.getLength(); }
    public double getTimeOffset() { return timeOffset; }
    public void setTimeOffset(double offset) { this.timeOffset = offset; }
    public int getCompletedJobs() { return completedJobs; }
    public double getTotalServiceTime() { return totalServiceTime; }

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
                completedJobs++;
                totalServiceTime += (j.getServiceEndTime() - j.getTimeOfCreation());
                //System.out.println("Completed:" + j.getOnboardingStation() + " to " + j.getDestStation() + " in " + (j.getServiceEndTime()-j.getTimeOfCreation()) + " minutes.");
            } else { currentPassengers.enqueue(j); }
        }
        return timeToTravel;
    }

    public String toString() { return "Stopping at " + currentStation.getName() + ". Number of passengers: " + passengerCount(); }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("BatchServerQueue");

        double currentTime = 0.0;
        LoopingQueue<Station> globalStationQueue = new LoopingQueue<>();
        for(int i = 1; i <= 10; i++) {
            globalStationQueue.enqueue(new Station("stationNum" + i, i * 50, 500, 500, new VehicleInfo(30, 15, 50)));
        }

        BatchServerQueue train1 = new BatchServerQueue(new VehicleInfo(500, 30, 250), globalStationQueue);

        for(int i = 0; i < 20; i++) {
            currentTime += train1.stopAtStation(currentTime);
        }
        return result;
    }
}

public class BatchServerQueue {
    private final LoopingQueue<Station> stationQueue;
    private Queue<Job> currentPassengers;
    private Station currentStation;
    private double nextStopTime;
    private VehicleInfo trainInfo;
    private double distanceFromOriginStation = 0;

    public BatchServerQueue(VehicleInfo vehicleInfo, LoopingQueue<Station> stationQueue) {
        this.stationQueue = stationQueue;
        currentPassengers = new Queue<Job>();
        trainInfo = vehicleInfo;
        currentStation = null;
        nextStopTime = Double.MAX_VALUE;
    }

    public int passengerCount() { return currentPassengers.getLength(); }

    public double stopAtStation(double currentTime) {
        currentStation = stationQueue.dequeue();

        double stationDistanceFromOrigin = currentStation.getDistanceFromOriginStation();
        double distanceToTravel = Math.abs(stationDistanceFromOrigin - distanceFromOriginStation);
        double timeToTravel = (distanceToTravel / trainInfo.getVehicleSpeed())*60;
        distanceFromOriginStation = stationDistanceFromOrigin;

        currentStation.getBusArrivals(currentTime);
        Job deq = currentStation.stationWaiters.dequeue();
        while(deq != null && currentPassengers.length < trainInfo.getVehicleCapacity()) {
            currentPassengers.enqueue(deq);
            deq = currentStation.stationWaiters.dequeue();
        }
        System.out.println("Stopping at " + currentStation.getName() + ". Number of passengers: " + passengerCount() + ", CurrentTime=" + currentTime);
        //TODO: Passenger unloading

        return timeToTravel;
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("BatchServerQueue");

        double currentTime = 0.0;
        LoopingQueue<Station> globalStationQueue = new LoopingQueue<Station>();
        for(int i = 1; i < 10; i++) {
            globalStationQueue.enqueue(new Station("stationNum" + i, i*50, 0, 0, new VehicleInfo(30, 15, 50)));
        }

        BatchServerQueue train1 = new BatchServerQueue(new VehicleInfo(500, 30, 250), globalStationQueue);

        for(int i = 0; i < 19; i++) {
            currentTime += train1.stopAtStation(currentTime);
            //System.out.println(train1.currentStation.getName());
        }
        return result;
    }
}

public class BatchServerQueue {
    private LoopingQueue<Station> stationQueue;
    private Queue<Job> currentPassengers;
    private Station currentStation;
    private double nextStopTime;
    private VehicleInfo trainInfo;

    public BatchServerQueue(VehicleInfo vehicleInfo, LoopingQueue<Station> stationQueue) {
        this.stationQueue = stationQueue;
        currentPassengers = new Queue<Job>();
        trainInfo = vehicleInfo;
        currentStation = null;
        nextStopTime = Double.MAX_VALUE;
    }

    public int passengerCount() { return currentPassengers.getLength(); }

    public void stopAtStation(double currentTime) {
        currentStation = stationQueue.dequeue();

        currentTime += (currentStation.getDistanceFromOriginStation() / trainInfo.getVehicleSpeed())*60;

        currentStation.getBusArrivals(currentTime);
        Job deq = currentStation.stationWaiters.dequeue();
        while(deq != null && currentPassengers.length < trainInfo.getVehicleCapacity()) {
            currentPassengers.enqueue(deq);
            deq = currentStation.stationWaiters.dequeue();
            //System.out.println("Passenger boarded");
        }
        System.out.println("Number of passengers: " + passengerCount() + ", CurrentTime=" + currentTime);
        //TODO: Passenger unloading
    }

    public static UnitTestResult UnitTest() {
        UnitTestResult result = new UnitTestResult("BatchServerQueue");

        double currentTime = 0.0;
        LoopingQueue<Station> globalStationQueue = new LoopingQueue<Station>();
        for(int i = 0; i < 10; i++) {
            globalStationQueue.enqueue(new Station("stationNum" + i, i*50, 1000000, 500000, new VehicleInfo(30, 15, 50)));
        }

        BatchServerQueue train1 = new BatchServerQueue(new VehicleInfo(500, 30, 250), globalStationQueue);

        for(int i = 0; i < 10; i++) {
            train1.stopAtStation(currentTime);
            System.out.println(train1.currentStation.getName());
        }
        return result;
    }
}

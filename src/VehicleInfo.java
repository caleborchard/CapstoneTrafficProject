import java.util.Random;

public class VehicleInfo {
    private int vehicleCapacity; //number of people that can fit on a vehicle
    private int numVehicles; //number of vehicles available for tasks
    private int vehicleSpeed; //average road speed in kmh
    private Random r;

    public int getVehicleCapacity() { return vehicleCapacity; }
    public int getVehicleSpeed() { return vehicleSpeed; }
    public int getNumVehicles() { return numVehicles; }
    public double getTravelTime(double distMin, double distMax) {
        //min 5, max 18 for Frederick as an example.
        double dist = r.nextDouble(distMax-distMin) + distMin;
        return (dist/getVehicleSpeed())*60;
    }

    public VehicleInfo(int capacity, int count, int speed) {
        vehicleCapacity = capacity;
        numVehicles = count;
        vehicleSpeed = speed;
        r = new Random();
    }
}
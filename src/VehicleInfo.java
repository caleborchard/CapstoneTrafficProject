import java.util.Random;

public class VehicleInfo {
    private int vehicleCapacity; //number of people that can fit on a vehicle
    private int numVehicles; //minutes between arrivals
    private int vehicleSpeed; //average road speed in kmh

    public int getVehicleCapacity() { return vehicleCapacity; }
    public int getVehicleSpeed() { return vehicleSpeed; }
    public int getNumVehicles() { return numVehicles; }
    public double getTravelTime(double distMin, double distMax) {
        //TODO: Find a way to calculate travel time of busses, probably with distributed variance.
        return 5;
    }

    public VehicleInfo(int capacity, int count, int speed) {
        vehicleCapacity = capacity;
        numVehicles = count;
        vehicleSpeed = speed;
    }
}

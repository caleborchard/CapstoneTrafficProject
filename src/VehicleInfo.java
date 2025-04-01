public class VehicleInfo {
    private int vehicleCapacity; //number of people that can fit on a vehicle
    private int arrivalFrequency; //minutes between arrivals
    private int vehicleSpeed; //average road speed in kmh

    public int getVehicleCapacity() { return vehicleCapacity; }
    public int getArrivalFrequency() { return arrivalFrequency; }
    public int getVehicleSpeed() { return vehicleSpeed; }

    public VehicleInfo(int capacity, int frequency, int speed) {
        vehicleCapacity = capacity;
        arrivalFrequency = frequency;
        vehicleSpeed = speed;
    }
}

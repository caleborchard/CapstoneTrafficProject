public class VehicleInfo {
    private int vehicleCapacity;
    private int arrivalFrequency;

    public int getVehicleCapacity() { return vehicleCapacity; }
    public int getArrivalFrequency() { return arrivalFrequency; }

    public VehicleInfo(int capacity, int frequency) { vehicleCapacity = capacity; arrivalFrequency = frequency; }
}

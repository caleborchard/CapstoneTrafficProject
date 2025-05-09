public class CityInfoHolder {
    private String name;
    private int numWorkers;
    private double distanceFromOrigin;

    public String getName() { return name; }
    public int getNumWorkers() { return numWorkers; }
    public double getDistanceFromOrigin() { return distanceFromOrigin; }

    public CityInfoHolder(String name, int numWorkers, double distanceFromOrigin) {
        this.name = name;
        this.numWorkers = numWorkers;
        this.distanceFromOrigin = distanceFromOrigin;
    }
}

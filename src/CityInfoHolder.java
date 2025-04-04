public class CityInfoHolder {
    private String name;
    private int numWorkers;

    public String getName() { return name; }
    public int getNumWorkers() { return numWorkers; }

    public CityInfoHolder(String name, int numWorkers) {
        this.name = name;
        this.numWorkers = numWorkers;
    }
}

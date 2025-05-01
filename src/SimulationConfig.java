public class SimulationConfig {
    protected int numTrains;
    protected int trainCapacity;
    protected int trainSpeed;
    protected int numBusses;
    protected int busSpeed;
    protected int busCapacity;

    public SimulationConfig (int numTrains, int trainCapacity, int trainSpeed, int numBusses, int busSpeed, int busCapacity) {
        this.numTrains = numTrains;
        this.trainCapacity = trainCapacity;
        this.trainSpeed = trainSpeed;
        this.numBusses = numBusses;
        this.busSpeed = busSpeed;
        this.busCapacity = busCapacity;
    }
    //This sucks lol
}

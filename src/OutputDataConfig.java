public class OutputDataConfig {
    public final int numTrains;
    public final int numBuses;
    public final double avgTravelTime;
    public final double maxTravelTime;
    public final int totalCompleteJobs;

    public OutputDataConfig(int numTrains, int numBuses, double avgTravelTime, double maxTravelTime, int totalCompleteJobs) {
        this.numTrains = numTrains;
        this.numBuses = numBuses;
        this.avgTravelTime = avgTravelTime;
        this.maxTravelTime = maxTravelTime;
        this.totalCompleteJobs = totalCompleteJobs;
    }
}

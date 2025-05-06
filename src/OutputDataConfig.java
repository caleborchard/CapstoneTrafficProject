public class OutputDataConfig {
    public final int numTrains;
    public final int numBuses;
    public final double avgServiceTime;
    public final double longestServiceTime;
    public final int totalCompletedJobs;

    public OutputDataConfig(int numTrains, int numBuses, double avgTravelTime, double maxTravelTime, int totalCompleteJobs) {
        this.numTrains = numTrains;
        this.numBuses = numBuses;
        this.avgServiceTime = avgTravelTime;
        this.longestServiceTime = maxTravelTime;
        this.totalCompletedJobs = totalCompleteJobs;
    }
    @Override
    public String toString() {
        return String.format(
                "Trains=%d  Buses=%d  AvgServiceTime=%.2fmin  CompletedJobs=%d  Ratio (Smaller is better)=%.2f",
                numTrains, numBuses, avgServiceTime, totalCompletedJobs, (avgServiceTime/totalCompletedJobs)*100
        );
    }
}
public class Job {
    private double timeOfCreation;
    private double serviceEndTime;
    private String onboardingStation;
    private String destStation;

    public double getTimeOfCreation() { return timeOfCreation; }
    public double getServiceEndTime() { return serviceEndTime; }

    public Job(double currentTime, String onboardingStation) { timeOfCreation = currentTime; }

    public void complete(double currentTime) { serviceEndTime = currentTime; }
}

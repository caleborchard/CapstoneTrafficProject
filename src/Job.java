public class Job {
    private double timeOfCreation;
    private double serviceEndTime;
    private String onboardingStation;
    private String destStation;

    public double getTimeOfCreation() { return timeOfCreation; }
    public double getServiceEndTime() { return serviceEndTime; }
    public String getOnboardingStation() { return onboardingStation; }
    //TODO: Departing station

    public Job(double currentTime, String onboardingStation) { timeOfCreation = currentTime; this.onboardingStation = onboardingStation;}

    public void complete(double currentTime) { serviceEndTime = currentTime; }
}

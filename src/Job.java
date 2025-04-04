public class Job {
    private double timeOfCreation;
    private double serviceEndTime;
    private String onboardingStation;
    private String destStation;

    public double getTimeOfCreation() { return timeOfCreation; }
    public double getServiceEndTime() { return serviceEndTime; }
    public String getOnboardingStation() { return onboardingStation; }
    public String getDestStation() { return destStation; }

    public Job(double currentTime, String onboardingStation, String destStation) { timeOfCreation = currentTime; this.onboardingStation = onboardingStation;}

    public void complete(double currentTime) { serviceEndTime = currentTime; }
}

public class Job {
    private double timeOfCreation;
    private double serviceEndTime;
    private String onboardingStation;
    private String destStation;

    public double getTimeOfCreation() { return timeOfCreation; }
    public double getServiceEndTime() { return serviceEndTime; }
    public String getOnboardingStation() { return onboardingStation; }
    public String getDestStation() { return destStation; }

    public String toString() { return "Created: " + getTimeOfCreation() + " at " + getOnboardingStation() + ", going to " + getDestStation(); }

    public Job(double currentTime, String onboardingStation, String destStation)
    { timeOfCreation = currentTime; this.onboardingStation = onboardingStation; this.destStation = destStation; }

    public void complete(double currentTime) { serviceEndTime = currentTime; }
}

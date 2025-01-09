public class Staff {
    private String name;
    private int timeToService;
    private boolean working;
    private int totalWorkedTime;
    private int currentCycleTime;
    private ServiceType serviceType;

    public Staff(String name, int timeToService, ServiceType serviceType) {
        this.name = name;
        this.timeToService = timeToService;
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public int getTimeToService() {
        return timeToService;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getTotalWorkedTime() {
        return totalWorkedTime;
    }

    public void setTotalWorkedTime(int totalWorkedTime) {
        this.totalWorkedTime = totalWorkedTime;
    }

    public int getCurrentCycleTime() {
        return currentCycleTime;
    }

    public void setCurrentCycleTime(int currentCycleTime) {
        this.currentCycleTime = currentCycleTime;
    }

    public ServiceType getStaffType() {
        return serviceType;
    }

    public void setStaffType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}

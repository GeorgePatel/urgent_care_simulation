public class Patient {
    private String name;
    private int arrivalTime;
    private int serviceStartTime = 0;
    private ServiceType serviceType;

    public Patient(String name, int arrivalTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(int serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}

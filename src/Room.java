public class Room {
    private String roomNumber;
    private boolean occupied;
    private int totalOccupiedTime;
    private Patient currentPatient = null;
    private Staff currentStaff = null;
    private ServiceType currentServiceType = null;

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getTotalOccupiedTime() {
        return totalOccupiedTime;
    }

    public void setTotalOccupiedTime(int totalOccupiedTime) {
        this.totalOccupiedTime = totalOccupiedTime;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }

    public Staff getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(Staff currentStaff) {
        this.currentStaff = currentStaff;
    }

    public ServiceType getCurrentServiceType() {
        return currentServiceType;
    }

    public void setCurrentServiceType(ServiceType currentServiceType) {
        this.currentServiceType = currentServiceType;
    }
}

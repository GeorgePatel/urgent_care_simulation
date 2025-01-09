import java.io.*;
import java.util.concurrent.TimeUnit;

public class UrgentCare {
    // priority queue using array of queues
    private static int priorityQueueSize;
    private static Object[] priorityQueue;
    private static double[] arrivalProbabilities;

    private static int simIntervalTime;
    private static int totalSimTime;
    private static int currentTime = 0;

    // Resources
    private static int numRooms;
    private static Room[] rooms;
    private static Stack<Staff> doctors = new Stack<>();
    private static  Stack<Staff> nurses = new Stack<>();
    private static  Stack<Staff> adminAssistants = new Stack<>();

    // Queues
    private static Queue<Patient> checkoutQ = new Queue<>();
    private static Queue<Staff> aaQ = new Queue<>();
    private static Queue<Patient> beingServedQ = new Queue<>();
    private static Queue<Patient> servedQ = new Queue<>();

    private static int totalNumPatients = 0; // counter
    private static final String fileName = "simulationOut.txt"; // stores current status of Urgent Care

    private static PrintWriter printWriter;

    /**
     * Initialize resources for simulation
     */
    protected static void init() {
        int numDoctors = Simulation.getNumDoctors();
        int serviceTimeDoctor = Simulation.getServiceTimeDoctor();
        int numNurses = Simulation.getNumNurses();
        int serviceTimeNurse = Simulation.getServiceTimeNurse();
        int numAA = Simulation.getNumAA();
        int serviceTimeAA = Simulation.getServiceTimeAA();

        numRooms = Simulation.getNumRooms();
        priorityQueueSize = Simulation.getPriorityQueueSize();
        arrivalProbabilities = Simulation.getArrivalProbabilities();
        simIntervalTime = Simulation.getSimIntervalTime();
        totalSimTime = Simulation.getTotalSimTime();

        for (int i = 0; i < numDoctors; i++) {
            Staff doctor = new Staff("DOC"+(i+1), serviceTimeDoctor, ServiceType.DOCTOR);
            doctors.push(doctor);
        }
        for (int i = 0; i < numNurses; i++) {
            Staff nurse = new Staff("NURSE"+(i+1), serviceTimeNurse, ServiceType.NURSE);
            nurses.push(nurse);
        }
        for (int i = 0; i < numAA; i++) {
            Staff adminAssistant = new Staff("AA"+(i+1), serviceTimeAA, ServiceType.AA);
            adminAssistants.push(adminAssistant);
        }
        rooms = new Room[numRooms];
        for (int i = 0; i < numRooms; i++) {
            Room room = new Room("ROOM"+(i+1));
            rooms[i] = room;
        }
       priorityQueue = new Object[priorityQueueSize];
        for (int i = 0; i < priorityQueueSize; i++) {
            priorityQueue[i] = new Queue<Patient>();;
        }
    }

    /**
     * Main business logic of Urgent Care simulation
     */
    protected static void simulate() {
        try {
            for (currentTime = 0; currentTime < totalSimTime; currentTime += simIntervalTime) {
                System.out.println("Current time: " + currentTime);

                patientArrival();

                nurseServices();

                doctorServices();

                updatePatientServiced(ServiceType.DOCTOR); // if patient has already been served in the Room by Nurse or Doctor

                adminServices();

                updatePatientServiced(ServiceType.AA); // if patient from checkout queue has already been served by AdminA

                updateServiceTime();

                try {
                    TimeUnit.SECONDS.sleep(simIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SimulationOutputFormatter.printToFile();
                SimulationOutputFormatter.printStatsToFile();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Take patient from priority Queue and assign to a Nurse.
     */
    private static void nurseServices() {
        while (patientAvailable() && staffAvailable(nurses) && roomAvailable()) {
            Patient firstPatientQ = getAvailablePatient();
            firstPatientQ.setServiceType(ServiceType.NURSE);
            firstPatientQ.setServiceStartTime(currentTime);
            Staff nurse = getAvailableStaff(nurses);
            Room room = getAvailableRoom();

            // servePatient()
            room.setCurrentPatient(firstPatientQ);
            room.setCurrentStaff(nurse);
            room.setCurrentServiceType(ServiceType.NURSE);

            nurse.setWorking(true);
        }
    }

    /**
     * After nurses have served the patient, a Doctor can now serve the patient.
     */
    private static void doctorServices() {
        // servePatient()
        for (Room room : rooms) {
            if (room.isOccupied()
                    && room.getCurrentServiceType().equals(ServiceType.NURSE)
                    && room.getCurrentStaff() == null) {
                if (staffAvailable(doctors)) {
                    Staff doctor = getAvailableStaff(doctors);
                    room.setCurrentStaff(doctor);
                    room.setCurrentServiceType(ServiceType.DOCTOR);
                    room.getCurrentPatient().setServiceType(ServiceType.DOCTOR);
                    doctor.setWorking(true);
                }
            }
        }
        // patientServed()
        // After doctors have served the patient, the room becomes available and patient enters the checkout queue
        for (Room room : rooms) {
            if (room.isOccupied()
                    && room.getCurrentServiceType().equals(ServiceType.DOCTOR)
                    && room.getCurrentStaff() == null) {
                Patient patient = room.getCurrentPatient();
                room.setCurrentPatient(null);
                room.setOccupied(false);
                room.setCurrentServiceType(null);
                checkoutQ.add(patient);
            }
        }
    }

    /**
     * Available AdminAs will serve patients from the checkout queue.
     */
    private static void adminServices() {
        while (!checkoutQ.isEmpty() && staffAvailable(adminAssistants)) {
            Staff aa = getAvailableStaff(adminAssistants);
            aa.setWorking(true);
            aaQ.add(aa);
            Patient patient = checkoutQ.remove();
            patient.setServiceType(ServiceType.AA);
            beingServedQ.add(patient);
        }
    }

    /**
     * Update the status of Patients that have been served.
     */
    private static void updatePatientServiced(ServiceType serviceType) {
        if (serviceType == ServiceType.NURSE || serviceType == ServiceType.DOCTOR) { // Nurse or Doctor
            // if patient has already been served in the Room by Nurse or Doctor
            for (Room room : rooms) {
                if (room.isOccupied()) {
                    Staff staffInRoom = room.getCurrentStaff();
                    // once nurse AND doctor have both served patient, patient is in room but current staff is null
                    if (staffInRoom != null && staffInRoom.getCurrentCycleTime() >= staffInRoom.getTimeToService()) {
                        staffInRoom.setCurrentCycleTime(0);
                        staffInRoom.setWorking(false);
                        if (room.getCurrentServiceType().equals(ServiceType.NURSE)) {
                            nurses.push(staffInRoom);
                            room.getCurrentPatient().setServiceType(null);
                        } else if (room.getCurrentServiceType().equals(ServiceType.DOCTOR)) {
                            doctors.push(staffInRoom);
                            room.getCurrentPatient().setServiceType(null);
                        }
                        room.setCurrentStaff(null);
                    }
                }
            }
        } else if (serviceType == ServiceType.AA) { // Admin
            // if patient from checkout queue has already been served by AdminA
            while (aaQ.getFront() != null &&
                    aaQ.getFront().getData().getCurrentCycleTime() >=
                            aaQ.getFront().getData().getTimeToService()) {
                aaQ.getFront().getData().setCurrentCycleTime(0);
                aaQ.getFront().getData().setWorking(false);
                adminAssistants.push(aaQ.remove());
                Patient patient = beingServedQ.remove();
                patient.setServiceType(ServiceType.COMPLETE);
                servedQ.add(patient);
            }
        }
    }

    /**
     * Increase the service time cycle and total worked time for staff in room and its total occupied time,
     * and for AdminAs that are currently serving patients.
     */
    private static void updateServiceTime() {
        // Rooms, Nurses and Doctors
        for (Room room : rooms) {
            if (room.isOccupied()) {
                room.setTotalOccupiedTime(room.getTotalOccupiedTime() + simIntervalTime); // Patient can be in room by themselves if no Doctor is available after Nurse is done
                Staff staffInRoom = room.getCurrentStaff();
                if (staffInRoom != null) {
                    staffInRoom.setCurrentCycleTime(staffInRoom.getCurrentCycleTime() + simIntervalTime);
                    staffInRoom.setTotalWorkedTime(staffInRoom.getTotalWorkedTime() + simIntervalTime);
                }
            }
        }
        // AdminAs
        if (!aaQ.isEmpty()) {
            ListIterator<Staff> listIterator = new ListIterator<>(aaQ.getFront());
            while (listIterator.hasNext()) {
                Staff aa = listIterator.next();
                aa.setCurrentCycleTime(aa.getCurrentCycleTime() + simIntervalTime);
                aa.setTotalWorkedTime(aa.getTotalWorkedTime() + simIntervalTime);
            }
        }
    }


    private static void patientArrival() {
        for (int i = 0; i < priorityQueueSize; i++) {
            if (Math.random() < arrivalProbabilities[i]) {
                Patient newPatient = new Patient("PAT"+ ++totalNumPatients + "/U" + (i+1), currentTime);
                ((Queue<Patient>)priorityQueue[i]).add(newPatient);
            }
        }
    }

    private static boolean patientAvailable() {
        boolean available = false;
        for (int i = 0; i < priorityQueueSize && !available; i++) {
            if (!((Queue<Patient>) priorityQueue[i]).isEmpty()) available = true;
        }
        return available;
    }

    private static Patient getAvailablePatient() {
        for (int i = 0; i < priorityQueueSize; i++) {
            Queue<Patient> patientQ = (Queue<Patient>) priorityQueue[i];
            if (!patientQ.isEmpty()) {
                return patientQ.remove();
            }
        }
        return null; // there are no patients in the priority queue
    }

    private static boolean staffAvailable(Stack<Staff> staffStack) {
        return !staffStack.isEmpty();
    }

    private static Staff getAvailableStaff(Stack<Staff> staffStack) {
        if (staffAvailable(staffStack)) return staffStack.pop();
        return null; // if no staff is available to serve the Patient
    }

    private static boolean roomAvailable() {
        boolean available = false;
        for (int i = 0; i < numRooms && !available; i++) {
            if (!rooms[i].isOccupied()) available = true;
        }
        return available;
    }

    private static Room getAvailableRoom() {
        for (int i = 0; i < numRooms; i++) {
            if (!rooms[i].isOccupied()) {
                rooms[i].setOccupied(true);
                return rooms[i];
            }
        }
        return null;
    }

//    private static void printToFile() throws FileNotFoundException {
//        try {
//            printWriter = new PrintWriter(fileName);
//            // Patients waiting in the priority queue
//            for (int i = 0; i < priorityQueueSize; i++) {
//                printWriter.print("Q" + (i+1) + ": ");
//                ListIterator<Patient> listIterator = new ListIterator<>(((Queue<Patient>)priorityQueue[i]).getFront());
//                while (listIterator.hasNext()) {
//                    Patient patient = listIterator.next();
//                    printWriter.print(patient.getName() + " ");
//                }
//                printWriter.println();
//            }
//            printWriter.println();
//
//            // Nurses
//            printWriter.println("Available nurses to serve patients: ");
//            Node<Staff> nursesTop = nurses.getTop();
//            while (nursesTop != null) {
//                printWriter.println(nursesTop.getData().getName() + " ");
//                nursesTop = nursesTop.getLink();
//            }
//            printWriter.println();
//
//            // Doctors
//            printWriter.println("Available doctors to serve patients: ");
//            Node<Staff> doctorsTop = doctors.getTop();
//            while (doctorsTop != null) {
//                printWriter.println(doctorsTop.getData().getName() + " ");
//                doctorsTop = doctorsTop.getLink();
//            }
//            printWriter.println();
//
//
//            // Rooms
//            printWriter.println("Current availability of Rooms in the urgent care: ");
//            for (Room room : rooms) {
//                printWriter.print(room.getRoomNumber() + ": ");
//                Patient patient = room.getCurrentPatient();
//                Staff staffInRoom = room.getCurrentStaff();
//
//                if (patient != null) {
//                    printWriter.print(patient.getName());
//                }
//                if (staffInRoom != null) {
//                    printWriter.print(" being served by: " + staffInRoom.getName() + " ");
//                }
//
//                printWriter.println();
//            }
//            printWriter.println();
//
//            // Admin Assistants
//            printWriter.println("Available admin assistants to serve patients: ");
//            Node<Staff> adminAssistantsTop = adminAssistants.getTop();
//            while (adminAssistantsTop != null) {
//                printWriter.print(adminAssistantsTop.getData().getName() + " ");
//                adminAssistantsTop = adminAssistantsTop.getLink();
//            }
//            printWriter.println();
//            printWriter.println();
//
//            // Checkout queue
//            printWriter.print("Patients waiting in checkout queue: ");
//            printPatientQueues(checkoutQ);
//
//            // Admins assts currently serving
//            printWriter.print("Admin assistants currently checking out patients: ");
//            ListIterator<Staff> servingAAListIterator = new ListIterator<>(aaQ.getFront()
//            );
//            while (servingAAListIterator.hasNext()) {
//                Staff aa = servingAAListIterator.next();
//                printWriter.print(aa.getName() + " ");
//            }
//            printWriter.println();
//            // patients being served by AAs
//            printWriter.print("Patients being checked out by admin assistants: ");
//            printPatientQueues(beingServedQ);
//
//            // patients served by AAs
//            printWriter.print("Patients served by urgent care: ");
//            ListIterator<Patient> servedQTop = new ListIterator<>(servedQ.getFront()
//            );
//            while (servedQTop.hasNext()) {
//                Patient servedPatient = servedQTop.next();
//                printWriter.print(servedPatient.getName() + ":ST" + servedPatient.getServiceStartTime() + "-AT" + servedPatient.getArrivalTime() + " ");
//            }
//            printWriter.println();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            printWriter.close();
//        }
//    }
//
//    private static void printPatientQueues(Queue<Patient> patientQ) {
//        ListIterator<Patient> patientQ_Iterator = new ListIterator<>(patientQ.getFront()
//        );
//        while (patientQ_Iterator.hasNext()) {
//            Patient patient = patientQ_Iterator.next();
//            printWriter.print(patient.getName() + " ");
//        }
//        printWriter.println();
//        printWriter.println();
//    }
//
//    private static void printStatsToFile() throws FileNotFoundException {
//        File file = new File(fileName);
//        FileWriter fileWriter = null;
//        BufferedWriter bufferedWriter = null;
//        PrintWriter printWriter = null;
//        try {
//            fileWriter = new FileWriter(file, true);
//            bufferedWriter = new BufferedWriter(fileWriter);
//            printWriter = new PrintWriter(bufferedWriter);
//
//            // Served Patients
//            ListIterator<Patient> servedQIterator = new ListIterator<>(servedQ.getFront()
//            );
//            int totalPatientTime = 0;
//            while (servedQIterator.hasNext()) {
//                Patient patient = servedQIterator.next();
//                totalPatientTime += patient.getServiceStartTime() - patient.getArrivalTime();
//            }
//
//            if (!servedQ.isEmpty()) {
//                printWriter.println();
//                printWriter.println("-------------------------------------");
//                printWriter.println("URGENT CARE STATISTICS");
//                printWriter.println("-------------------------------------");
//                printWriter.println("Total Patients Served: " + servedQ.size() + " ");
//                printWriter.println();
//                printWriter.println("Average Wait Time: " + totalPatientTime / servedQ.size());
//                printWriter.println();
//                printWriter.println("Total Time Used: " + totalPatientTime + " ");
//                printWriter.println();
//            }
//
//            // Rooms total serve time
//            for (Room room : rooms) {
//                printWriter.println(room.getRoomNumber() + ":" + room.getTotalOccupiedTime() + "sec ");
//            }
//            printWriter.println();
//
//            // Nurses total serve time
//            Node<Staff> nursesTop = nurses.getTop();
//
//            while (nursesTop != null) {
//                Staff nurse = nursesTop.getData();
//                printWriter.println(nurse.getName() + ":" + nurse.getTotalWorkedTime() + "sec ");
//                nursesTop = nursesTop.getLink();
//            }
//            for (Room room : rooms) { // nurses serving patients
//                if (room.getCurrentStaff() != null
//                        && room.getCurrentServiceType().equals(ServiceType.NURSE)) { // getStaffType &&  room.getStaffType().equals("Nurse")
//                    Staff nurse = room.getCurrentStaff();
//                    printWriter.println(nurse.getName() + ":" + nurse.getTotalWorkedTime() + "sec ");
//                }
//            }
//            printWriter.println();
//
//            // Doctors total serve time
//            Node<Staff> doctorsTop = doctors.getTop();
//            while (doctorsTop != null) {
//                Staff doctor = doctorsTop.getData();
//                printWriter.println(doctor.getName() + ":" + doctor.getTotalWorkedTime() + "sec ");
//                doctorsTop = doctorsTop.getLink();
//            }
//            for (Room room : rooms) {
//                if (room.getCurrentStaff() != null
//                        && room.getCurrentServiceType().equals(ServiceType.DOCTOR)) {
//                    Staff doctor = room.getCurrentStaff();
//                    printWriter.println(doctor.getName() + ":" + doctor.getTotalWorkedTime() + "sec ");
//                }
//            }
//            printWriter.println();
//
//            // Admin asst total serve time
//            Node<Staff> adminAssistantsTop = adminAssistants.getTop();
//            while (adminAssistantsTop != null) {
//                Staff adminAssistant = adminAssistantsTop.getData();
//                printWriter.println(adminAssistant.getName() + ":" + adminAssistant.getTotalWorkedTime() + "sec ");
//                adminAssistantsTop = adminAssistantsTop.getLink();
//            }
//            ListIterator<Staff> aaQIterator = new ListIterator<>(aaQ.getFront());
//            while (aaQIterator.hasNext()) { // AAs currently serving patients
//                Staff aa = aaQIterator.next();
//                printWriter.println(aa.getName() + ":" + aa.getTotalWorkedTime() + "sec ");
//            }
//            printWriter.println();
//            printWriter.println();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            printWriter.close();
//        }
//    }

    public static Stack<Staff> getDoctors() {
        return doctors;
    }

    public static Stack<Staff> getNurses() {
        return nurses;
    }

    public static Room[] getRooms() {
        return rooms;
    }

    public static Stack<Staff> getAdminAssistants() {
        return adminAssistants;
    }

    public static Queue<Patient> getCheckoutQ() {
        return checkoutQ;
    }

    public static Queue<Staff> getAaQ() {
        return aaQ;
    }

    public static Queue<Patient> getBeingServedQ() {
        return beingServedQ;
    }

    public static Queue<Patient> getServedQ() {
        return servedQ;
    }

    public static Object[] getPriorityQueue() {
        return priorityQueue;
    }
}

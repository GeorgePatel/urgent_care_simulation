import java.io.*;

public class SimulationOutputFormatter {

    private static final String fileName = "frontend/simulationOut.txt"; // stores current status of Urgent Care
    private static PrintWriter printWriter;

    static void printToFile() throws FileNotFoundException {
        try {
            printWriter = new PrintWriter(fileName);
            // Patients waiting in the priority queue
            for (int i = 0; i < Simulation.getPriorityQueueSize(); i++) {
                printWriter.print("Q" + (i+1) + ": ");
                ListIterator<Patient> listIterator = new ListIterator<>(((Queue<Patient>) UrgentCare.getPriorityQueue()[i]).getFront());
                while (listIterator.hasNext()) {
                    Patient patient = listIterator.next();
                    printWriter.print(patient.getName() + " ");
                }
                printWriter.println();
            }
            printWriter.println();

            // Nurses
            printWriter.println("Available nurses to serve patients: ");
            Node<Staff> nursesTop = UrgentCare.getNurses().getTop();
            while (nursesTop != null) {
                printWriter.println(nursesTop.getData().getName() + " ");
                nursesTop = nursesTop.getLink();
            }
            printWriter.println();

            // Doctors
            printWriter.println("Available doctors to serve patients: ");
            Node<Staff> doctorsTop = UrgentCare.getDoctors().getTop();
            while (doctorsTop != null) {
                printWriter.println(doctorsTop.getData().getName() + " ");
                doctorsTop = doctorsTop.getLink();
            }
            printWriter.println();


            // Rooms
            printWriter.println("Current availability of Rooms in the urgent care: ");
            for (Room room : UrgentCare.getRooms()) {
                printWriter.print(room.getRoomNumber() + ": ");
                Patient patient = room.getCurrentPatient();
                Staff staffInRoom = room.getCurrentStaff();

                if (patient != null) {
                    printWriter.print(patient.getName());
                }
                if (staffInRoom != null) {
                    printWriter.print(" being served by: " + staffInRoom.getName() + " ");
                }

                printWriter.println();
            }
            printWriter.println();

            // Admin Assistants
            printWriter.println("Available admin assistants to serve patients: ");
            Node<Staff> adminAssistantsTop = UrgentCare.getAdminAssistants().getTop();
            while (adminAssistantsTop != null) {
                printWriter.print(adminAssistantsTop.getData().getName() + " ");
                adminAssistantsTop = adminAssistantsTop.getLink();
            }
            printWriter.println();
            printWriter.println();

            // Checkout queue
            printWriter.print("Patients waiting in checkout queue: ");
            printPatientQueues(UrgentCare.getCheckoutQ());

            // Admins assts currently serving
            printWriter.print("Admin assistants currently checking out patients: ");
            ListIterator<Staff> servingAAListIterator = new ListIterator<>(UrgentCare.getAaQ().getFront()
            );
            while (servingAAListIterator.hasNext()) {
                Staff aa = servingAAListIterator.next();
                printWriter.print(aa.getName() + " ");
            }
            printWriter.println();
            // patients being served by AAs
            printWriter.print("Patients being checked out by admin assistants: ");
            printPatientQueues(UrgentCare.getBeingServedQ());

            // patients served by AAs
            printWriter.print("Patients served by urgent care: ");
            ListIterator<Patient> servedQTop = new ListIterator<>(UrgentCare.getServedQ().getFront()
            );
            while (servedQTop.hasNext()) {
                Patient servedPatient = servedQTop.next();
                printWriter.print(servedPatient.getName() + ":ST" + servedPatient.getServiceStartTime() + "-AT" + servedPatient.getArrivalTime() + " ");
            }
            printWriter.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            printWriter.close();
        }
    }

    private static void printPatientQueues(Queue<Patient> patientQ) {
        ListIterator<Patient> patientQ_Iterator = new ListIterator<>(patientQ.getFront()
        );
        while (patientQ_Iterator.hasNext()) {
            Patient patient = patientQ_Iterator.next();
            printWriter.print(patient.getName() + " ");
        }
        printWriter.println();
        printWriter.println();
    }

    static void printStatsToFile() throws FileNotFoundException {
        File file = new File(fileName);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            // Served Patients
            ListIterator<Patient> servedQIterator = new ListIterator<>(UrgentCare.getServedQ().getFront()
            );
            int totalPatientTime = 0;
            while (servedQIterator.hasNext()) {
                Patient patient = servedQIterator.next();
                totalPatientTime += patient.getServiceStartTime() - patient.getArrivalTime();
            }

            if (!UrgentCare.getServedQ().isEmpty()) {
                printWriter.println();
                printWriter.println("-------------------------------------");
                printWriter.println("URGENT CARE STATISTICS");
                printWriter.println("-------------------------------------");
                printWriter.println("Total Patients Served: " + UrgentCare.getServedQ().size() + " ");
                printWriter.println();
                printWriter.println("Average Wait Time: " + totalPatientTime / UrgentCare.getServedQ().size());
                printWriter.println();
                printWriter.println("Total Time Used: " + totalPatientTime + " ");
                printWriter.println();
            }

            // Rooms total serve time
            for (Room room : UrgentCare.getRooms()) {
                printWriter.println(room.getRoomNumber() + ":" + room.getTotalOccupiedTime() + "sec ");
            }
            printWriter.println();

            // Nurses total serve time
            Node<Staff> nursesTop = UrgentCare.getNurses().getTop();

            while (nursesTop != null) {
                Staff nurse = nursesTop.getData();
                printWriter.println(nurse.getName() + ":" + nurse.getTotalWorkedTime() + "sec ");
                nursesTop = nursesTop.getLink();
            }
            for (Room room : UrgentCare.getRooms()) { // nurses serving patients
                if (room.getCurrentStaff() != null
                        && room.getCurrentServiceType().equals(ServiceType.NURSE)) { // getStaffType &&  room.getStaffType().equals("Nurse")
                    Staff nurse = room.getCurrentStaff();
                    printWriter.println(nurse.getName() + ":" + nurse.getTotalWorkedTime() + "sec ");
                }
            }
            printWriter.println();

            // Doctors total serve time
            Node<Staff> doctorsTop = UrgentCare.getDoctors().getTop();
            while (doctorsTop != null) {
                Staff doctor = doctorsTop.getData();
                printWriter.println(doctor.getName() + ":" + doctor.getTotalWorkedTime() + "sec ");
                doctorsTop = doctorsTop.getLink();
            }
            for (Room room : UrgentCare.getRooms()) {
                if (room.getCurrentStaff() != null
                        && room.getCurrentServiceType().equals(ServiceType.DOCTOR)) {
                    Staff doctor = room.getCurrentStaff();
                    printWriter.println(doctor.getName() + ":" + doctor.getTotalWorkedTime() + "sec ");
                }
            }
            printWriter.println();

            // Admin asst total serve time
            Node<Staff> adminAssistantsTop = UrgentCare.getAdminAssistants().getTop();
            while (adminAssistantsTop != null) {
                Staff adminAssistant = adminAssistantsTop.getData();
                printWriter.println(adminAssistant.getName() + ":" + adminAssistant.getTotalWorkedTime() + "sec ");
                adminAssistantsTop = adminAssistantsTop.getLink();
            }
            ListIterator<Staff> aaQIterator = new ListIterator<>(UrgentCare.getAaQ().getFront());
            while (aaQIterator.hasNext()) { // AAs currently serving patients
                Staff aa = aaQIterator.next();
                printWriter.println(aa.getName() + ":" + aa.getTotalWorkedTime() + "sec ");
            }
            printWriter.println();
            printWriter.println();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printWriter.close();
        }
    }
}

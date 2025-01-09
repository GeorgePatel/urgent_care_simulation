import java.util.Scanner;

public class Simulation {
    private static int numDoctors;
    private static int serviceTimeDoctor;
    private static int numNurses;
    private static int serviceTimeNurse;
    private static int numAA;
    private static int serviceTimeAA;
    private static int numRooms;
    private static int priorityQueueSize;
    private static double[] arrivalProbabilities;
    private static int simIntervalTime;
    private static int totalSimTime;

    public static void main(String[] args) {
        getInput();
        UrgentCare.init();
        System.out.println("Simulation started.");
        UrgentCare.simulate();
        System.out.println("Simulation finished.");
    }

    /**
     * Get input for resources and simulation metrics
     */
    private static void getInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Please input number of doctors: ");
            numDoctors = scanner.nextInt();
            if (numDoctors < 1) {
                throw new IllegalArgumentException("Number of doctors must be greater than 0.");
            }
            System.out.println("Please input Doctor service time: ");
            serviceTimeDoctor = scanner.nextInt();
            if (serviceTimeDoctor < 1) {
                throw new IllegalArgumentException("Service time must be greater than 0.");
            }

            System.out.println("Please input number of nurses: ");
            numNurses = scanner.nextInt();
            if (numNurses < 1) {
                throw new IllegalArgumentException("Number of nurses must be greater than 0.");
            }
            System.out.println("Please input Nurse service time: ");
            serviceTimeNurse = scanner.nextInt();
            if (serviceTimeNurse < 1) {
                throw new IllegalArgumentException("Service time must be greater than 0.");
            }

            System.out.println("Please input number of admin assistants: ");
            numAA = scanner.nextInt();
            if (numAA < 1) {
                throw new IllegalArgumentException("Number of admin assistants must be greater than 0.");
            }
            System.out.println("Please input Admin assistant service time: ");
            serviceTimeAA = scanner.nextInt();
            if (serviceTimeAA < 1) {
                throw new IllegalArgumentException("Service time must be greater than 0.");
            }

            System.out.println("Please input number of rooms: ");
            numRooms = scanner.nextInt();
            if (numRooms < 1) {
                throw new IllegalArgumentException("Number of rooms must be greater than 0.");
            }

            System.out.println("Please input number of queues for the patients: ");
            priorityQueueSize = scanner.nextInt();
            if (priorityQueueSize < 1) {
                throw new IllegalArgumentException("Number of queues must be greater than 0.");
            }
            arrivalProbabilities = new double[priorityQueueSize];

            System.out.println("Please input the probabilities for each queue: ");
            double previousProb = 0;
            for (int i = 0; i < priorityQueueSize; i++) {
                double prob = scanner.nextDouble();
                if (prob <= 0 || prob >= 1 || prob <= previousProb) {
                    throw new IllegalArgumentException("Probability must be between 0 and 1, and greater than the previous one.");
                }
                previousProb = prob;
                arrivalProbabilities[i] = prob;
            }

            System.out.println("Please input the interval time of the simulation: ");
            simIntervalTime = scanner.nextInt();
            if (simIntervalTime < 1) {
                throw new IllegalArgumentException("Interval time must be greater than 0.");
            }

            System.out.println("Please input the total time of the simulation: ");
            totalSimTime = scanner.nextInt();
            if (totalSimTime < 1) {
                throw new IllegalArgumentException("Total time must be greater than 0.");
            }
        } catch (Exception e) { // TODO: Handle the IllegalArgumentExceptions gracefully
            e.printStackTrace();
            System.exit(0);
        } finally {
            scanner.close();
        }
    }

    public static int getNumDoctors() {
        return numDoctors;
    }

    public static void setNumDoctors(int numDoctors) {
        Simulation.numDoctors = numDoctors;
    }

    public static int getServiceTimeDoctor() {
        return serviceTimeDoctor;
    }

    public static void setServiceTimeDoctor(int serviceTimeDoctor) {
        Simulation.serviceTimeDoctor = serviceTimeDoctor;
    }

    public static int getNumNurses() {
        return numNurses;
    }

    public static void setNumNurses(int numNurses) {
        Simulation.numNurses = numNurses;
    }

    public static int getServiceTimeNurse() {
        return serviceTimeNurse;
    }

    public static void setServiceTimeNurse(int serviceTimeNurse) {
        Simulation.serviceTimeNurse = serviceTimeNurse;
    }

    public static int getNumAA() {
        return numAA;
    }

    public static void setNumAA(int numAA) {
        Simulation.numAA = numAA;
    }

    public static int getServiceTimeAA() {
        return serviceTimeAA;
    }

    public static void setServiceTimeAA(int serviceTimeAA) {
        Simulation.serviceTimeAA = serviceTimeAA;
    }

    public static int getNumRooms() {
        return numRooms;
    }

    public static void setNumRooms(int numRooms) {
        Simulation.numRooms = numRooms;
    }

    public static int getPriorityQueueSize() {
        return priorityQueueSize;
    }

    public static void setPriorityQueueSize(int priorityQueueSize) {
        Simulation.priorityQueueSize = priorityQueueSize;
    }

    public static double[] getArrivalProbabilities() {
        return arrivalProbabilities;
    }

    public static void setArrivalProbabilities(double[] arrivalProbabilities) {
        Simulation.arrivalProbabilities = arrivalProbabilities;
    }

    public static int getSimIntervalTime() {
        return simIntervalTime;
    }

    public static void setSimIntervalTime(int simIntervalTime) {
        Simulation.simIntervalTime = simIntervalTime;
    }

    public static int getTotalSimTime() {
        return totalSimTime;
    }

    public static void setTotalSimTime(int totalSimTime) {
        Simulation.totalSimTime = totalSimTime;
    }
}

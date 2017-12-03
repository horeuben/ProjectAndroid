package reuben.projectandroid.Itinerary;
import java.util.ArrayList;

public class BruteForce {

    private static Travel travel;
    private static int bestTime = -1;
    private static double bestPrice = -1;
    private static Travel bestTravel = new Travel(0);
    private static int bestTransport;

    public static void bruteForce(ArrayList<Attraction> newList, ArrayList<Attraction> listOfAttractions, int transport) {
        if (!listOfAttractions.isEmpty()) {
            for(int i = 0; i<listOfAttractions.size(); i++) {
                Attraction justRemoved = listOfAttractions.remove(0);
                ArrayList<Attraction> newRoute = (ArrayList<Attraction>) newList.clone();
                newRoute.add(justRemoved);

                bruteForce(newRoute, listOfAttractions, transport);
                listOfAttractions.add(justRemoved);
            }
        }

        else {
            Travel newTravel = new Travel(newList);
            int newTime = newTravel.getTime(transport);
            if (bestTime == -1 || newTime < bestTime) {
                for (Attraction c: newTravel.getTravel()) {
                    bestTravel = new Travel(newList);
                }
                bestTime = newTime;
                bestPrice = newTravel.getPrice(transport);
            }
        }
    }

    public static void bruteForceBudget(ArrayList<Attraction> newList, ArrayList<Attraction> listOfAttractions, int transport, double budget) {
        bestTime = -1;
        bestPrice = -1;
        bestTravel = new Travel(0);
        bestTransport = transport;
        bruteForce(newList, listOfAttractions, transport);
        if (bestPrice > budget) {
            bruteForceBudget(new ArrayList<Attraction>(), travel.getTravel(), transport + 1, budget);
        }
    }

    public static int getBestTime() {
        return bestTime;
    }

    public static double getBestPrice() {
        return bestPrice;
    }

    public static Travel getBestTravel() {
        return bestTravel;
    }

    public static String getBestTransport() {
        if (bestTransport == 0)
            return "Taxi";
        else if (bestTransport == 1)
            return "Public Transport";
        else
            return "Walking";
    }

    public static Travel getTravel() {
        return travel;
    }

    public static void setTravel(ArrayList<Attraction> travelList) {
        BruteForce.travel = new Travel(travelList);
    }

    public static void averageRunBF(int i) {
        final long startTime = System.currentTimeMillis();

        for (int j = 0; j < i; j++) {
            ArrayList<Attraction> travelList = new ArrayList<>();
            for (int k = 0; k < 6; k++) {
                travelList.add(new Attraction(k));
            }
            // Create a list of attractions using the user input of itinerary, and setTravel.
            // Or you could input the list directly into the bruteForceBudget method.
            setTravel(travelList);
            // This is an example of how to use the brute force method.
            // Inputs are (new ArrayList<Attraction>(), the attractions in the itinerary, 0, and the budget).
            // Third arg is transport type, 0 is taxi, 1 is public transport, 2 is walking.
            // Outputs are in the private variables bestTime, bestPrice and bestTravel, use getters.
            bruteForceBudget(new ArrayList<Attraction>(), travel.getTravel(), 0, 20.00);
            for (Attraction a: getBestTravel().getTravel()) {
                System.out.print(a.getX() + " ");
            }
            System.out.print("| " + getBestTime() + " minutes | Transport type: " + getBestTransport() + " | ");
            System.out.println(getBestPrice() + " SGD");
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms" );
        System.out.println("Average execution time: " + (endTime - startTime)/i + "ms" );
    }

    public static void main(String[] args) {
        averageRunBF(1000);
    }
}
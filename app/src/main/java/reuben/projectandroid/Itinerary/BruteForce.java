package reuben.projectandroid.Itinerary;

import java.util.ArrayList;
import java.util.Collections;

public class BruteForce {

    private static Travel travel = new Travel(10);
    private static int bestTime = -1;
    private static double bestPrice = -1;

    public static void bruteForce(ArrayList<Attraction> newList, ArrayList<Attraction> listOfAttractions, int transport) {
        String bestTravel = "";

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
                bestTravel = "";
                for (Attraction c: newTravel.getTravel()) {
                    bestTravel += (c.getX() + " ");
                }
                bestTime = newTime;
                bestPrice = newTravel.getPrice(transport);

                System.out.println(bestTravel + bestTime + " " + bestPrice);
            }
        }
    }

    public static void bruteForceBudget(ArrayList<Attraction> newList, ArrayList<Attraction> listOfAttractions, int transport, double budget) {
        System.out.println("Using transport type " + transport);
        bruteForce(newList, listOfAttractions, transport);
        if (bestPrice > budget) {
            System.out.println("Travel price over budget, using next transport alternative.");
            bestTime = -1;
            bestPrice = -1;
            bruteForceBudget(new ArrayList<Attraction>(), travel.getTravel(), transport + 1, budget);
        }
    }

    public static void averageRunBF(int i) {
        final long startTime = System.currentTimeMillis();
        for (int j = 0; j < i; j++) {
            bruteForceBudget(new ArrayList<Attraction>(), travel.getTravel(), 2, 20.00);
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms" );
        System.out.println("Average execution time: " + (endTime - startTime)/i + "ms" );
    }

    public static void main(String[] args) {
        averageRunBF(100);
    }
}

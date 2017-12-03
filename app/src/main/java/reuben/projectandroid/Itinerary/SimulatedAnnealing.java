package reuben.projectandroid.Itinerary;

import java.util.ArrayList;

public class SimulatedAnnealing {

    private static Travel travel;
    private static int bestTime = -1;
    private static double bestPrice = -1;
    private static Travel bestTravel = new Travel(0);

    public static void simulateAnnealing(double startingTemperature, int numberOfIterations, double coolingRate, int transport, double budget) {

        bestTime = -1;
        bestPrice = -1;
        bestTravel = new Travel(0);

        System.out.println("Budget is $" + budget);
        System.out.println("Using transport type " + transport);
        System.out.println("Starting SA with temperature: " + startingTemperature + ", # of iterations: " + numberOfIterations + " and cooling rate: " + coolingRate);
        double t = startingTemperature;
        travel.generateInitialTravel();
        bestTime = travel.getTime(transport);
        bestPrice = travel.getPrice(transport);
        System.out.println("Initial time for travel: " + bestTime + " minutes");
        Travel currentSolution = travel;

        for (int i = 0; i < numberOfIterations; i++) {
            if (t > 0.1) {
                currentSolution.swapCities();
                int currentTime = currentSolution.getTime(transport);
                if (currentTime < bestTime) {
                    bestTime = currentTime;
                    double currentPrice = currentSolution.getPrice(transport);
                    bestPrice = currentPrice;
                    bestTravel = new Travel(currentSolution.getTravel());
                } else if (Math.exp((bestTime - currentTime) / t) < Math.random()) {
                    currentSolution.revertSwap();
                }
                t *= coolingRate;
            } else {
                continue;
            }
//            if (i % 100 == 0) {
//                System.out.print(bestSol);
//                System.out.print("Price: $" + bestPrice + " ");
//                System.out.print("Time: " + bestTime + " ");
//                System.out.println("Iteration #" + i);
//            }
        }

        if (bestPrice > budget) {
            System.out.println("Travel price over budget, using next transport alternative.");
            bestTime = -1;
            bestPrice = -1;
            bestTravel = new Travel(0);
            simulateAnnealing(startingTemperature, numberOfIterations, coolingRate, transport + 1, budget);
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

    public static void setTravel(ArrayList<Attraction> travelList) {
        SimulatedAnnealing.travel = new Travel(travelList);
    }

    public static void averageRunSA(int i) {
        final long startTime = System.currentTimeMillis();
        double totTime = 0;
        for (int j = 0; j < i; j++) {
            ArrayList<Attraction> travelList = new ArrayList<>();
            for (int k = 0; k < 6; k++) {
                travelList.add(new Attraction(k));
            }
            // Create a list of attractions using the user input of itinerary, and setTravel.
            // Or you could input the list directly into the bruteForceBudget method.
            setTravel(travelList);
            // This is an example of how to use the simulated annealing method.
            // Inputs are (1000000, 100000, 0.999, 0, and the budget).
            // Fourth arg is transport type, 0 is taxi, 1 is public transport, 2 is walking.
            // Outputs are in the private variables bestTime, bestPrice and bestTravel, use getters.
            simulateAnnealing(1000000, 100000, 0.999, 0, 20.00);
            totTime += bestTime;
            for (Attraction a: getBestTravel().getTravel()) {
                System.out.print(a.getX() + " ");
            }
            System.out.print("| " + getBestTime() + " minutes | ");
            System.out.println(getBestPrice() + " SGD");
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms" );
        System.out.println("Average execution time: " + (endTime - startTime)/i + "ms" );
        System.out.println("Average solution time: " + totTime/i + "minutes" );
    }

    public static void main(String[] args) {
        averageRunSA(1000);
    }

}
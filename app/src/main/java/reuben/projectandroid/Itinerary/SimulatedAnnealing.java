package reuben.projectandroid.Itinerary;

public class SimulatedAnnealing {

    private static Travel travel = new Travel(6);

    public static Travel getTravel() {
        return travel;
    }

    public static void setTravel(Travel travel) {
        SimulatedAnnealing.travel = travel;
    }

    public static double simulateAnnealing(double startingTemperature, int numberOfIterations, double coolingRate, int transport, double budget) {
        System.out.println("Budget is $" + budget);
        System.out.println("Using transport type " + transport);
        System.out.println("Starting SA with temperature: " + startingTemperature + ", # of iterations: " + numberOfIterations + " and cooling rate: " + coolingRate);
        double t = startingTemperature;
        travel.generateInitialTravel();
        double bestTime = travel.getTime(transport);
        double bestPrice = travel.getPrice(transport);
        System.out.println("Initial time for travel: " + bestTime + " minutes");
        Travel bestSolution = travel;
        Travel currentSolution = bestSolution;

        String bestSol = "";

        for (int i = 0; i < numberOfIterations; i++) {
            if (t > 0.1) {
                currentSolution.swapCities();
                double currentTime = currentSolution.getTime(transport);
                if (currentTime < bestTime) {
                    bestTime = currentTime;
                    double currentPrice = currentSolution.getPrice(transport);
                    bestPrice = currentPrice;
                    bestSol = "";
                    for (Attraction c: bestSolution.getTravel()) {
                        bestSol += (c.getX() + " ");
                    }
                } else if (Math.exp((bestTime - currentTime) / t) < Math.random()) {
                    currentSolution.revertSwap();
                }
                t *= coolingRate;
            } else {
                continue;
            }
            if (i % 100 == 0) {
                System.out.print(bestSol);
                System.out.print("Price: $" + bestPrice + " ");
                System.out.print("Time: " + bestTime + " ");
                System.out.println("Iteration #" + i);
            }
        }

        System.out.println("Travel path: " + bestSol);
        System.out.println("Price: $" + bestPrice);

        if (bestPrice > budget) {
            System.out.println("Travel price over budget, using next transport alternative.");
            return simulateAnnealing(startingTemperature, numberOfIterations, coolingRate, transport + 1, budget);
        }

        return bestTime;
    }

    public static void averageRunSA(int i) {
        final long startTime = System.currentTimeMillis();
        double totTime = 0;
        for (int j = 0; j < i; j++)
            totTime += simulateAnnealing(1000000,100000,0.999, 0, 20.00);
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms" );
        System.out.println("Average execution time: " + (endTime - startTime)/i + "ms" );
        System.out.println("Average solution time: " + totTime/i + "minutes" );
    }

    public static void main(String[] args) {
        averageRunSA(1000);
    }

}
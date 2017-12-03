package reuben.projectandroid.Itinerary;

import java.util.ArrayList;
import java.util.Collections;

public class Travel {

    private ArrayList<Attraction> travel = new ArrayList<>();
    private ArrayList<Attraction> previousTravel = new ArrayList<>();

    public Travel(int numberOfCities) {
        for (int i = 0; i < numberOfCities; i++) {
            travel.add(new Attraction(i));
        }
    }

    public Travel(ArrayList<Attraction> listOfCities) {
        for (Attraction c: listOfCities)
            travel.add(c);
    }

    public void generateInitialTravel() {
        if (travel.isEmpty())
            new Travel(10);
        Collections.shuffle(travel);
    }

    public void swapCities() {
        int a = generateRandomIndex();
        int b = generateRandomIndex();
        previousTravel = travel;
        Attraction x = travel.get(a);
        Attraction y = travel.get(b);
        travel.set(a, y);
        travel.set(b, x);
    }

    public void revertSwap() {
        travel = previousTravel;
    }

    private int generateRandomIndex() {
        return (int) (Math.random() * travel.size());
    }

    public Attraction getAttraction(int index) {
        return travel.get(index);
    }

    public int getTime(int transport) {
        int time = 0;
        for (int index = 0; index < travel.size(); index++) {
            Attraction starting = getAttraction(index);
            Attraction destination;
            if (index + 1 < travel.size()) {
                destination = getAttraction(index + 1);
            } else {
                destination = getAttraction(0);
            }
            time += starting.timeToAttraction(destination, transport);
        }
        return time;
    }

    public double getPrice(int transport) {
        int price = 0;
        for (int index = 0; index < travel.size(); index++) {
            Attraction starting = getAttraction(index);
            Attraction destination;
            if (index + 1 < travel.size()) {
                destination = getAttraction(index + 1);
            } else {
                destination = getAttraction(0);
            }
            price += starting.priceToAttraction(destination, transport);
        }
        return price;
    }

    public ArrayList<Attraction> getTravel() {
        return travel;
    }
}
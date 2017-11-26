package reuben.projectandroid.Database;

/**
 * Created by Jing Yun on 25/11/2017.
 */

public class ItineraryItem {
    private String name;

    public ItineraryItem(String inputString){
        this.name = inputString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

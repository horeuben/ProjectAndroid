package reuben.projectandroid.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.Database.DatabaseHandler;
import reuben.projectandroid.Itinerary.BruteForce;
import reuben.projectandroid.Itinerary.SimulatedAnnealing;
import reuben.projectandroid.Itinerary.Travel;
import reuben.projectandroid.R;

public class ItineraryResultsActivity extends AppCompatActivity {
    ArrayList<Integer> itiList;
    DatabaseHandler db;
    int type;
    double budget;
    TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Itinerary results");
        setContentView(R.layout.activity_itinerary_results);
        db = new DatabaseHandler(this);
        Bundle b = getIntent().getExtras();
        itiList = b.getIntegerArrayList("attractions");
        budget = b.getDouble("budget");
        type = b.getInt("type");
        output = (TextView)findViewById(R.id.textView_itioutput);
        new GetResultTask().execute();
    }


    private class GetResultTask extends AsyncTask<Void, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ItineraryResultsActivity.this);

        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
           // super.onPostExecute(result);
            output.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog.setMessage("Loading");
            //show dialog
            asyncDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String output ="";
            ArrayList<reuben.projectandroid.Itinerary.Attraction> travelList = new ArrayList<>();
            for (int i = 0; i < itiList.size(); i++) {
                travelList.add(new reuben.projectandroid.Itinerary.Attraction(itiList.get(i)));
            }
            switch (type) {
                //bruteforce
                case 0:
                    final long startTimeb = System.currentTimeMillis();

                    BruteForce.setTravel(travelList);
                    BruteForce.bruteForceBudget(new ArrayList<reuben.projectandroid.Itinerary.Attraction>(), BruteForce.getTravel().getTravel(), 0, budget);

                    for (int i =0;i<BruteForce.getBestTravel().getTravel().size();i++) {
                        reuben.projectandroid.Itinerary.Attraction a= BruteForce.getBestTravel().getTravel().get(i);
                        if(i==BruteForce.getBestTravel().getTravel().size()-1){
                            output+="End at "+ db.getAttractionNameFromID(a.getX()).getName()+".\n\n";
                        }
                        else if (i==0){
                            output+="Start at "+ db.getAttractionNameFromID(a.getX()).getName()+",\n";
                        }
                        else{
                            output+="next go to "+ db.getAttractionNameFromID(a.getX()).getName()+",\n";
                        }

                    }
                    final long endTimeb = System.currentTimeMillis();
                    output+="Time taken for travel: "+BruteForce.getBestTime()+" minutes\n"+"Price: $"+BruteForce.getBestPrice()+"\nTransport: "+BruteForce.getBestTransport() + "\nTotal execution time: " + (endTimeb - startTimeb) + "ms" ;

                    break;
                //sa
                case 1:
                    final long startTimes = System.currentTimeMillis();

                    SimulatedAnnealing.setTravel(travelList);
                    SimulatedAnnealing.simulateAnnealing(1000000, 100000, 0.999, 0, budget);

                    for (int i =0;i<SimulatedAnnealing.getBestTravel().getTravel().size();i++) {
                        reuben.projectandroid.Itinerary.Attraction a= SimulatedAnnealing.getBestTravel().getTravel().get(i);
                        if(i==SimulatedAnnealing.getBestTravel().getTravel().size()-1){
                            output+="End at "+ db.getAttractionNameFromID(a.getX()).getName()+".\n\n";
                        }
                        else if (i==0){
                            output+="Start at "+ db.getAttractionNameFromID(a.getX()).getName()+",\n";
                        }
                        else{
                            output+="next go to "+ db.getAttractionNameFromID(a.getX()).getName()+",\n";
                        }

                    }
                    final long endTimes = System.currentTimeMillis();
                    output+="Time taken for travel: "+SimulatedAnnealing.getBestTime()+" minutes\n"+"Price: $"+SimulatedAnnealing.getBestPrice()+"\nTransport: "+SimulatedAnnealing.getBestTransport() + "\nTotal execution time: " + (endTimes - startTimes) + "ms" ;

                    break;
            }
            return output;
        }
    }
}
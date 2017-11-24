package reuben.projectandroid.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import reuben.projectandroid.Fragments.AttractionListFragment;
import reuben.projectandroid.R;

public class MainActivity extends AppCompatActivity implements AttractionListFragment.OnFragmentInteractionListener{

    AttractionListFragment attractionListFragment;
    FragmentTransaction transaction;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_attractions:
                    setTitle("Attractions");
                    transaction.replace(R.id.fragment_container, attractionListFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_map:
                    setTitle("Map");

                    return true;
                case R.id.navigation_itinerary:
                    setTitle("Itinerary");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTitle("Attractions");
        attractionListFragment = new AttractionListFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, attractionListFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

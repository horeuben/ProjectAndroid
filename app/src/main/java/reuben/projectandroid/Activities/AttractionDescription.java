package reuben.projectandroid.Activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Attr;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.Database.ItineraryDatabaseHandler;
import reuben.projectandroid.Database.ItineraryItem;
import reuben.projectandroid.R;


//TODO: JY place id search reuturning 0 places
public class AttractionDescription extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "AttractionDescription";
    ImageView attractionImage;
    TextView textViewAdd, textViewNo,textViewDesc;
    private Place attractionChosen;
    private GoogleApiClient googleApiClient;
    private LatLng attrChosenCoord;


    private ToggleButton addAttrToIti;
    private ItineraryDatabaseHandler itineraryDatabaseHandler=new ItineraryDatabaseHandler(AttractionDescription.this);
    private String AttrName;
    private List<ItineraryItem> itineraryItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_description);
        //connnect the google API client so you can get the place fro the place id
        googleApiClient = new GoogleApiClient.Builder(AttractionDescription.this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();

        //get name, place id and desc from bundle, as well as description
        Bundle b = getIntent().getExtras();
        AttrName = b.getString("atrName");
        String desc = b.getString("atrDesc");
        String place_id = b.getString("atrPlaceid");
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.stockpic1);
        //Gets Place by placeid but not working-->always returns 0 results
        //get the place object from the ID
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(googleApiClient, place_id);
        //query the place ID and returns a bunch of results
        Log.i(LOG_TAG, "here?");
        placeResult.setResultCallback(placeResultCallback);

        attractionImage = (ImageView) findViewById(R.id.attraction_image);
        //using stock pic currently
        attractionImage.setImageBitmap(image);
        addAttrToIti = (ToggleButton) findViewById(R.id.addAttraction);
        addAttrToIti.setOnCheckedChangeListener(toggleBttnListener);
        textViewAdd = (TextView) findViewById(R.id.textView_add);
        textViewNo = (TextView) findViewById(R.id.textView_no);
        textViewDesc = (TextView) findViewById(R.id.textView_desc);
        textViewDesc.setText(desc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.attraction_bar);

        collapsingToolbarLayout.setTitle(AttrName);


    }

//TODO: the toggle bttn also does nothing
    private CompoundButton.OnCheckedChangeListener toggleBttnListener
        =new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked){
                //add item
                ItineraryItem iti = new ItineraryItem(AttrName);
                itineraryDatabaseHandler.createItiItem(iti);
                Log.i(LOG_TAG, "adding");
                //its adding but not appearing in itinerary
            }
            else{
                //create new Itinerary item and add it to the list view
                itineraryDatabaseHandler.deleteItiItem(AttrName);
                Log.i(LOG_TAG, "deleting");
                //add item to the database

            }
            //make all the entries into a list
            itineraryItemList = itineraryDatabaseHandler.getItiItems();

        }
    };

    private ResultCallback<PlaceBuffer> placeResultCallback
            =new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                Log.i(LOG_TAG, "not succeffult on result");
                Toast.makeText(AttractionDescription.this,"notsuccessul",Toast.LENGTH_SHORT);
                return;
            }
            else if (places.getCount()>0){
                attractionChosen = places.get(0);
                CharSequence attributions = places.getAttributions();
                textViewAdd.setText(Html.fromHtml((String) attractionChosen.getAddress()));
                textViewNo.setText(Html.fromHtml((String)attractionChosen.getPhoneNumber()));
                attrChosenCoord = attractionChosen.getLatLng();
            }
            else{
                //TODO: why isnt it returning anything?
                Log.i(LOG_TAG, String.valueOf(places.getCount()));
                Log.i(LOG_TAG, "GG");
            }
            places.release();
            //choose the first answer

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }



    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google Places API connection suspended.");

    }

}

package reuben.projectandroid.Activities;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Attr;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.Database.DatabaseHandler;
import reuben.projectandroid.Database.ItineraryDatabaseHandler;
import reuben.projectandroid.Database.ItineraryItem;
import reuben.projectandroid.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


//TODO: JY place id search reuturning 0 places
public class AttractionDescription extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback {

    private static final String LOG_TAG = "AttractionDescription";
    ImageView attractionImage;
    TextView textViewAdd, textViewNo,textViewDesc;
    private Place attractionChosen;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    private ItineraryDatabaseHandler itineraryDatabaseHandler=new ItineraryDatabaseHandler(AttractionDescription.this);
    private List<ItineraryItem> itineraryItemList;
    private int height,width;
    private FloatingActionButton fab;
    private Attraction attraction;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_description);
        db = new DatabaseHandler(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        //connnect the google API client so you can get the place fro the place id
        googleApiClient = new GoogleApiClient.Builder(AttractionDescription.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        //get name, place id and desc from bundle, as well as description
        Bundle b = getIntent().getExtras();
        attraction = new Attraction();
        attraction.setName(b.getString("atrName"));
        attraction.setPlaceid(b.getString("atrPlaceid"));
        attraction.setDescription(b.getString("atrDesc"));
        attraction.setInItinerary(b.getInt("atrInItinerary"));

        attractionImage = (ImageView) findViewById(R.id.attraction_image);
        textViewAdd = (TextView) findViewById(R.id.textView_add);
        textViewNo = (TextView) findViewById(R.id.textView_no);
        textViewDesc = (TextView) findViewById(R.id.textView_desc);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        if (attraction.getInItinerary() ==1)
            fab.setImageBitmap(textAsBitmap("Delete",14, Color.WHITE));
        else
            fab.setImageBitmap(textAsBitmap("Add",14, Color.WHITE));
        textViewDesc.setText(attraction.getDescription());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.attraction_bar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.attractionOnMap);
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = height*7/10;
        params.width = width;
        mapFragment.getView().setLayoutParams(params);
        mapFragment.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        CollapsingToolbarLayout.LayoutParams ctlparams = new CollapsingToolbarLayout.LayoutParams(collapsingToolbarLayout.getLayoutParams());
                        ctlparams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);
                        collapsingToolbarLayout.setLayoutParams(ctlparams);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        CollapsingToolbarLayout.LayoutParams ctlparamsa = new CollapsingToolbarLayout.LayoutParams(collapsingToolbarLayout.getLayoutParams());
                        ctlparamsa.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
                        collapsingToolbarLayout.setLayoutParams(ctlparamsa);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        collapsingToolbarLayout.setTitle(attraction.getName());
        mapFragment.getMapAsync(this);
        new GetPlaceTask().execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (attraction.getInItinerary()==0){
                    attraction.setInItinerary(1);
                    fab.setImageBitmap(textAsBitmap("Delete",14, Color.WHITE));
                    db.setInItinerary(attraction);
                }
                else{
                    attraction.setInItinerary(0);
                    fab.setImageBitmap(textAsBitmap("Add",14, Color.WHITE));
                    db.setInItinerary(attraction);
                }
            }
        });

    }


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
    private class GetPlaceTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(AttractionDescription.this);
        String typeStatus;


        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage("Loading");
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            googleApiClient.connect();
            boolean a = googleApiClient.isConnected();
            while(!a){
//                Toast.makeText(getApplicationContext(),"Still connecting",Toast.LENGTH_SHORT).show();
                a = googleApiClient.isConnected();
            }
            Places.GeoDataApi.getPlaceById(googleApiClient, attraction.getPlaceid()).setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(PlaceBuffer places) {
                    if(places.getStatus().isSuccess()) {
                        attractionChosen = places.get(0);
                        String no = attractionChosen.getPhoneNumber().toString();
                        String addr = attractionChosen.getAddress().toString();
                        LatLng latLng = attractionChosen.getLatLng();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(attractionChosen.getName().toString()));
                        float zoomLevel = 15.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        if (no.equals(""))textViewNo.setText("Not available");
                        else textViewNo.setText(no);
                        if (addr.equals("")) textViewAdd.setText("Not available");
                        else textViewAdd.setText(addr);
                        new GetPhotoTask().execute();
                    }else{
                        attractionChosen = null;
                        textViewNo.setText("Not available");
                        textViewAdd.setText("Not available");
                    }
                    // release the PlaceBuffer to prevent a memory leak
                    places.release();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog
            asyncDialog.dismiss();

            super.onPostExecute(result);
        }

    }
    private class GetPhotoTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(AttractionDescription.this);
        String typeStatus;


        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage("Loading");
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Places.GeoDataApi.getPlacePhotos(googleApiClient, attraction.getPlaceid()).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                @Override
                public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                    if( placePhotoMetadataResult.getStatus().isSuccess()){
                        PlacePhotoMetadataBuffer photoMetadataBuffer = placePhotoMetadataResult.getPhotoMetadata();
                        // Get the first photo in the list.
                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                        new GetBitmapTask().execute(photoMetadata);
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog
            asyncDialog.dismiss();

            super.onPostExecute(result);
        }

    }
    private class GetBitmapTask extends AsyncTask<PlacePhotoMetadata, Void, Bitmap> {
        ProgressDialog asyncDialog = new ProgressDialog(AttractionDescription.this);
        String typeStatus;


        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage("Loading");
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(PlacePhotoMetadata... arg0) {
            PlacePhotoMetadata r = arg0[0];
            r.getPhoto(googleApiClient).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                @Override
                public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                    if (placePhotoResult.getBitmap() != null) {
                        Bitmap image = placePhotoResult.getBitmap();
                        Bitmap resized = darkenBitMap(Bitmap.createScaledBitmap(image, width, height*3/10, true));
                        attractionImage.setImageBitmap(resized);
                        attractionImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //hide the dialog
            asyncDialog.dismiss();

            super.onPostExecute(result);
        }

    }
    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFFC0C0C0	, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}

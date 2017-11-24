package reuben.projectandroid.Activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.R;

public class AttractionDescription extends AppCompatActivity {

    ImageView attractionImage;
    TextView textViewAdd, textViewNo,textViewDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_description);
        //get name, place id and desc from bundle, as well as description
        Bundle b = getIntent().getExtras();
        String name = b.getString("atrName");
        String desc = b.getString("atrDesc");
        String place_id = b.getString("atrPlaceid");

        //do stuff to get place by id, then populate the data, placeid to get image, address, and phonenumber
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.stockpic1);

        attractionImage = (ImageView) findViewById(R.id.attraction_image);
        //using stock pic currently
        attractionImage.setImageBitmap(image);
        textViewAdd = (TextView) findViewById(R.id.textView_add);
        textViewNo = (TextView) findViewById(R.id.textView_no);
        textViewDesc = (TextView) findViewById(R.id.textView_desc);
        textViewAdd.setText("blk asbc goriella");
        textViewNo.setText("1234556789");
        textViewDesc.setText(desc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.attraction_bar);

        collapsingToolbarLayout.setTitle(name);


    }
}

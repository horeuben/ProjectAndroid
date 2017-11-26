package reuben.projectandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import reuben.projectandroid.R;

/**
 * Created by reube on 24/11/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 16;
    // Database Name
    private static final String DATABASE_NAME = "projectandroid_database";

    // Table names
    private static final String TABLE_ATTRACTIONS = "attractions";

    //Common Column names
    private static final String KEY_PLACE_ID = "place_id"; //unique identifier where we can get the place from places API through getplacebyid
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "desc";
    private static final String KEY_TYPE = "type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTRACTION_TABLE = "CREATE TABLE " + TABLE_ATTRACTIONS + "("+ KEY_PLACE_ID + " TEXT PRIMARY KEY ," + KEY_NAME + " TEXT,"+ KEY_TYPE + " TEXT,"+ KEY_DESC + " TEXT"+ ")";
        db.execSQL(CREATE_ATTRACTION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRACTIONS);
        // Create tables again
        onCreate(db);
    }

    //CRUD here
    public void createAttraction(Attraction attraction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, attraction.getName());
        values.put(KEY_DESC, attraction.getDescription());
        values.put(KEY_PLACE_ID, attraction.getPlaceid());
        values.put(KEY_TYPE, attraction.getType().name());
        // insert row
        db.insert(TABLE_ATTRACTIONS, null, values);
        db.close();

    }

    public List<Attraction> getAttractions(){
        List<Attraction> attractions = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTRACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Attraction attraction = new Attraction();
                attraction.setPlaceid(c.getString(c.getColumnIndex(KEY_PLACE_ID)));
                attraction.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                attraction.setDescription(c.getString(c.getColumnIndex(KEY_DESC)));
                attraction.setType(Attraction.AttractionType.valueOf(c.getString(c.getColumnIndex(KEY_TYPE))));
                attractions.add(attraction);

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return attractions;
    }

    public List<String> attrNameList(){
        List<String> attractionNamesList = new ArrayList<>();

        String selectQuery = "SELECT "+KEY_NAME+" FROM "+TABLE_ATTRACTIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        String placeName="";
        Log.i("DatabseHandler","here123");

        if (c.moveToFirst()) {
            do {
                placeName=c.getString(c.getColumnIndex(KEY_NAME));
                attractionNamesList.add(placeName);

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return attractionNamesList;

    }
}

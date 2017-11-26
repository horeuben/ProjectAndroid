package reuben.projectandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jing Yun on 25/11/2017.
 */

public class ItineraryDatabaseHandler extends SQLiteOpenHelper {
    private final Context context;
    private static final int DATABASE_VERSION=20;


    //table name
    private static final String TABLE_ITINERARY="ItineraryList";

    //common column names
    private static final String KEY_ATTRNAME="attractionName";


    public ItineraryDatabaseHandler(Context context) {
        super(context, TABLE_ITINERARY, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase itiDB) {
        String CREATE_ITINERARY_TABLE = "CREATE TABLE " + TABLE_ITINERARY + "("+ KEY_ATTRNAME + " TEXT PRIMARY KEY" +")";
        itiDB.execSQL(CREATE_ITINERARY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase itiDB, int i, int i1) {
        //drop older table if it exists
        itiDB.execSQL("DROP TABLE IF EXISTS " + TABLE_ITINERARY);
        // Create tables again
        onCreate(itiDB);

    }
    public void createItiItem(ItineraryItem itiItem){
        SQLiteDatabase itiDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ATTRNAME, itiItem.getName());
        itiDB.insert(TABLE_ITINERARY, null, values);
        itiDB.close();
    }

    //extracting the results
    public List<ItineraryItem> getItiItems(){
        List<ItineraryItem> itiList = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITINERARY;

        SQLiteDatabase itidb = this.getWritableDatabase();
        Cursor c = itidb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ItineraryItem itiItem = new ItineraryItem(c.getString(c.getColumnIndex(KEY_ATTRNAME)));
                itiList.add(itiItem);

            } while (c.moveToNext());
        }
        c.close();
        itidb.close();
        return itiList;
    }

    public void deleteItiItem(String id){
        //delete the row
        SQLiteDatabase itiDB = this.getWritableDatabase();
        String DELETE_ITINERARY_TABLE = "DELETE FROM " + TABLE_ITINERARY + " WHERE "+ KEY_ATTRNAME + "='"+id+"'";
        itiDB.execSQL(DELETE_ITINERARY_TABLE);
        itiDB.close();
    }

}

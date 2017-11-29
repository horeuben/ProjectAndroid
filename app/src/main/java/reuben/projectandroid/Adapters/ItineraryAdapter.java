package reuben.projectandroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.Database.DatabaseHandler;
import reuben.projectandroid.R;

/**
 * Created by reube on 15/11/2017.
 */

public class ItineraryAdapter extends ArrayAdapter<Attraction> {
    private DatabaseHandler db;
    public List<Attraction> attractions;
    Context context;
    public ItineraryAdapter(Activity context, List<Attraction> attractions){
        super(context,0,attractions);
        this.attractions = attractions;
        this.context = context;

    }
    private class ViewHolder {
        TextView name;
        ImageView image_type;
        Button delete_button;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        Attraction attraction = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        db = new DatabaseHandler(context);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.itinerary_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image_type = (ImageView) convertView.findViewById(R.id.image_iti);
            viewHolder.name = (TextView) convertView.findViewById(R.id.iti_name);
            viewHolder.delete_button = (Button) convertView.findViewById(R.id.deleteItiBttn);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.iti_name, viewHolder.name);
            convertView.setTag(R.id.image_iti, viewHolder.image_type);
            convertView.setTag(R.id.deleteItiBttn, viewHolder.delete_button);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(attraction.getName());
        if (attraction.getType()== Attraction.AttractionType.HOTEL)
            viewHolder.image_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_hotel));
        else if(attraction.getType()== Attraction.AttractionType.LOCAL_MALL)
            viewHolder.image_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_local_mall));
        else if(attraction.getType()== Attraction.AttractionType.LOCAL_SEE)
            viewHolder.image_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_local_see));
        else
            viewHolder.image_type.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_local_mall));

        viewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attraction a = attractions.get(position);
                a.setInItinerary(0);
                db.setInItinerary(a);
                attractions.remove(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.delete_button.setFocusable(false);
        viewHolder.delete_button.setFocusableInTouchMode(false);
        return convertView;
    }
}

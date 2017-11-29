package reuben.projectandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import reuben.projectandroid.Activities.AttractionDescription;
import reuben.projectandroid.Adapters.AttractionAdapter;
import reuben.projectandroid.Adapters.ItineraryAdapter;
import reuben.projectandroid.Database.Attraction;
import reuben.projectandroid.Database.DatabaseHandler;
import reuben.projectandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttractionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttractionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItineraryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    List<Attraction> attractions;
    private OnFragmentInteractionListener mListener;
    public static ItineraryAdapter adapter;
    private static Parcelable state;
    private DatabaseHandler db;

    public ItineraryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttractionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItineraryListFragment newInstance(String param1, String param2) {
        ItineraryListFragment fragment = new ItineraryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_itinerary_list, container, false);
        db = new DatabaseHandler(getActivity());
        //get all attractions from sqlite here
        attractions = db.getInItinerary();
        listView = (ListView) rootView.findViewById(R.id.itinerary_list);
        //before i put to adapter i need to go through solver to know where to go first
        adapter = new ItineraryAdapter(getActivity(),attractions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent attrIntent = new Intent(getActivity(), AttractionDescription.class);
                attrIntent.putExtra("atrName",attractions.get(position).getName());
                attrIntent.putExtra("atrDesc",attractions.get(position).getDescription());
                attrIntent.putExtra("atrType",attractions.get(position).getType());
                attrIntent.putExtra("atrPlaceid",attractions.get(position).getPlaceid()); //use this to getplacebyid
                attrIntent.putExtra("atrInItinerary", attractions.get(position).getInItinerary());
                startActivity(attrIntent);
            }

        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onPause() {
        state = listView.onSaveInstanceState();    //saves current scrolled state
        super.onPause();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(state!=null){
            listView.onRestoreInstanceState(state); //restores scrolled stategit
            adapter.notifyDataSetChanged();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        attractions = db.getInItinerary();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String loadJSONFromRaw(){
        String json = null;
        try{
            InputStream is= getResources().openRawResource(R.raw.attraction_details);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Log.i("attractionListFragment","here3");
            json = new String(buffer, "UTF-8");
        } catch (Exception ex){
            ex.printStackTrace();
            Log.i("attractionListFragment","here4");
            return null;
        }
        Log.i("attractionListFragment","json"+"is"+json);
        return json;

    }
}

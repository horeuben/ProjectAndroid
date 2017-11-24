package reuben.projectandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reuben.projectandroid.Activities.AttractionDescription;
import reuben.projectandroid.Activities.MainActivity;
import reuben.projectandroid.Adapters.AttractionAdapter;
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
public class AttractionListFragment extends Fragment {
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
    public static AttractionAdapter adapter;
    private static Parcelable state;
    private DatabaseHandler db;

    public AttractionListFragment() {
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
    public static AttractionListFragment newInstance(String param1, String param2) {
        AttractionListFragment fragment = new AttractionListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_attraction_list, container, false);
        db = new DatabaseHandler(getActivity());
        //get all attractions from sqlite here
        attractions = db.getAttractions();
        if(attractions.size()==0){
            Attraction a = new Attraction();
            a.setName("Marina Bay Sands"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_mbs)); a.setPlaceid("placeholder"); a.setType(Attraction.AttractionType.HOTEL);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Singapore Flyer"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_sflyer)); a.setPlaceid("placeholder12"); a.setType(Attraction.AttractionType.LOCAL_SEE);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Vivo City"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_vivo)); a.setPlaceid("placeholder1"); a.setType(Attraction.AttractionType.LOCAL_MALL);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Resorts World Sentosa"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_rws)); a.setPlaceid("placeholder3"); a.setType(Attraction.AttractionType.HOTEL);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Buddha Tooth Relic Temple"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_btrt)); a.setPlaceid("placeholder4"); a.setType(Attraction.AttractionType.LOCAL_SEE);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Zoo"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_zoo)); a.setPlaceid("placeholder5"); a.setType(Attraction.AttractionType.LOCAL_SEE);
            db.createAttraction(a);
            attractions = db.getAttractions();
        }
        adapter = new AttractionAdapter(getActivity(),attractions);
        listView = (ListView) rootView.findViewById(R.id.attraction_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent attrIntent = new Intent(getActivity(), AttractionDescription.class);
                attrIntent.putExtra("atrName",attractions.get(position).getName());
                attrIntent.putExtra("atrDesc",attractions.get(position).getDescription());
                attrIntent.putExtra("atrType",attractions.get(position).getType());
                attrIntent.putExtra("atrPlaceid",attractions.get(position).getPlaceid()); //use this to getplacebyid
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
        state = listView.onSaveInstanceState();    //saves current gridview scrolled state
        super.onPause();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(state!=null){
            listView.onRestoreInstanceState(state); //restores gridview scrolled state
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();

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
}

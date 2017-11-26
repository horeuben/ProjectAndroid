package reuben.projectandroid.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

import reuben.projectandroid.Adapters.ItineraryAdapter;
import reuben.projectandroid.Database.ItineraryDatabaseHandler;
import reuben.projectandroid.Database.ItineraryItem;
import reuben.projectandroid.R;


//TODO: delete button is no where to be found
//TODO: ensure the toggle buttons doesnt reset

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItineraryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItineraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItineraryFragment extends Fragment{
    private static final String LOG_TAG = "ItineraryFragment";
    View rootView;
    private Button DeleteButton;
    private ArrayAdapter<ItineraryItem> itineraryItemArrayAdapter;
    private List<ItineraryItem> itineraryItemList;
    private ListView ItiList;
    private OnFragmentInteractionListener m1Listener;
    private ItineraryDatabaseHandler itineraryDatabaseHandler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ItineraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItineraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItineraryFragment newInstance(String param1, String param2) {
        ItineraryFragment fragment = new ItineraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_itinerary, container, false);
        itineraryDatabaseHandler = new ItineraryDatabaseHandler(getActivity());
        itineraryItemList= itineraryDatabaseHandler.getItiItems();
        //TODO: upon instantiation the itinerary already has 3 items inside (i think it's remembering previous inputs)
        Log.i(LOG_TAG, String.valueOf(itineraryItemList.size()));

        ItiList = (ListView) rootView.findViewById(R.id.itinerary_list);
        itineraryItemArrayAdapter = new ItineraryAdapter(getActivity(),itineraryItemList);
        ItiList.setAdapter(itineraryItemArrayAdapter);
        DeleteButton = (Button) rootView.findViewById(R.id.deleteItiBttn);
        //TODO: delete bttn does jack shit
        //DeleteButton.setOnClickListener(this);
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
            m1Listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m1Listener = null;
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

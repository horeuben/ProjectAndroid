package reuben.projectandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reuben.projectandroid.Activities.AttractionDescription;
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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView searchBar;
    private ArrayAdapter searchBarAdapter;
    private ListView listView;
    List<Attraction> attractions;
    private OnFragmentInteractionListener mListener;
    public static AttractionAdapter adapter;
    public static AttractionAdapter searchAdapter;
    private static Parcelable state;
    private DatabaseHandler db;
    private List<String> attractionList;
    private FloatingActionButton searchButton;

    //these are commonly misspelled words
    private Map<String, List<String>> searchBase= new HashMap<String, List<String>>();
    {{  searchBase.put("Sentosa", new ArrayList<>(Arrays.asList("Resorts World Sentosa")));
        searchBase.put("Buddha",new ArrayList<>(Arrays.asList("Buddha Tooth Relic Temple")));
        searchBase.put("Animal",new ArrayList<>(Arrays.asList("Animal Concerns Research & Education Society ACRES","Society for the Prevention of Cruelty to Animals SPCA",
                "Oasis Second Chance Animal Shelter OSCAS","Animal Lovers League ALL","Causes for Animals CAS")));
        searchBase.put("Singapore",new ArrayList<>(Arrays.asList("Singapore Flyer","Singapore Zoo","Save Our Street Dogs SOSD Singapore","Action for Singapore Dogs ASD",
                "The House Rabbit Society of Singapore HRSS")));
        searchBase.put("Marina",new ArrayList<>(Arrays.asList("Marina Bay Sands")));
    }};

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
            a.setName("Marina Bay Sands"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_mbs)); a.setPlaceid("ChIJm3G3-AYZ2jERfByv0oRiOwA"); a.setType(Attraction.AttractionType.HOTEL); a.setId(0);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Singapore Flyer"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_sflyer)); a.setPlaceid("ChIJ_WTZdKoZ2jERK024PgmtUrs"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(1);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Vivo City"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_vivo)); a.setPlaceid("ChIJK7xLl1gZ2jERP_GdUY9XNLo"); a.setType(Attraction.AttractionType.LOCAL_MALL); a.setId(2);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Resorts World Sentosa"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_rws)); a.setPlaceid("ChIJLR75v_0b2jERJrR28stYwMU"); a.setType(Attraction.AttractionType.HOTEL); a.setId(3);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Buddha Tooth Relic Temple"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_btrt)); a.setPlaceid("ChIJ0bwmznIZ2jEREOCMNggtIBk"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(4);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Singapore Zoo"); a.setDescription(getActivity().getApplicationContext().getString(R.string.desc_zoo)); a.setPlaceid("ChIJr9wqENkT2jERkRs7pMj6FLQ"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(5);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("The Cat Cafe"); a.setDescription("Meow meow"); a.setPlaceid("id1"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(6);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Animal Concerns Research & Education Society ACRES"); a.setDescription("placeholder1"); a.setPlaceid("id2"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(7);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Society for the Prevention of Cruelty to Animals SPCA"); a.setDescription("placeholder2"); a.setPlaceid("id3"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(8);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Save Our Street Dogs SOSD Singapore"); a.setDescription("placedholder3"); a.setPlaceid("id4"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(9);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Oasis Second Chance Animal Shelter OSCAS"); a.setDescription("placedholder4"); a.setPlaceid("id5"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(10);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Animal Lovers League ALL"); a.setDescription("placedholder5"); a.setPlaceid("id6"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(11);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Causes for Animals CAS"); a.setDescription("placedholder6"); a.setPlaceid("id7"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(12);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("Action for Singapore Dogs ASD"); a.setDescription("placedholder7"); a.setPlaceid("id8"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(13);
            db.createAttraction(a);
            a = new Attraction();
            a.setName("The House Rabbit Society of Singapore HRSS"); a.setDescription("placedholder8"); a.setPlaceid("id9"); a.setType(Attraction.AttractionType.LOCAL_SEE); a.setId(14);
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
                attrIntent.putExtra("atrInItinerary", attractions.get(position).getInItinerary());
                attrIntent.putExtra("atrId", attractions.get(position).getId());
                startActivity(attrIntent);
            }
        });
        searchBar=(AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Attraction> Tempattractions = new ArrayList<>();
                for (Attraction a:attractions){
                    if (a.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        Tempattractions.add(a);
                    }
                }

                AttractionAdapter autoCompAdapter = new AttractionAdapter(getActivity(),Tempattractions);
                autoCompAdapter.notifyDataSetChanged();
                listView.setAdapter(autoCompAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        searchButton = (FloatingActionButton) rootView.findViewById(R.id.floatingSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Attraction> spellCheckResult = new ArrayList<Attraction>(){};
                String UserInput = searchBar.getText().toString();
                List<String> allMatches = new ArrayList<String>();
                for (String possibleMatch: searchBase.keySet()){
                    // in this weighted average score we will give LCS 60% and leven distance 40% weightage
                    //normalised score is from -1 to 1 (it will go to negative if leven distance > predicted word)
                    double levenDist = (double) LevenshteinDistance(UserInput,possibleMatch);
                    double longestSubseq = (double) LongestLCS(UserInput,possibleMatch);
                    //first part of normalised score measures how much of the possible match answer is actually levenshtein distance. this is weighted 40%
                    //EG. if we compare sentozzza and marina levenshtien dist is 8. len("marina")=6 so first part of normalised score is -0.1333
                    //second part of the normalized score calculate what prportion of the possible match is part of the LCS this is weigted with 60%
                    Double normalizedScore = (((possibleMatch.length()-levenDist)/possibleMatch.length())*0.4) + (longestSubseq/possibleMatch.length()*0.6);
                    //if normalized score is >50% that's a match!
                    if (normalizedScore>=0.5){
                        allMatches.addAll(searchBase.get(possibleMatch));
                    }
                }
                for (String name:allMatches){
                    Integer placeSelectedIndex = 0;
                    for (Attraction a : attractions){
                        if (a.getName().equals(name)){
                            placeSelectedIndex=attractions.indexOf(a);
                            spellCheckResult.add(attractions.get(placeSelectedIndex));
                        }
                    }
                }
                if (spellCheckResult.size()==0){
                    Toast.makeText(getActivity(),"No results found",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getActivity(),"results Input",Toast.LENGTH_SHORT).show();
                    searchAdapter=new AttractionAdapter(getActivity(),spellCheckResult);
                    listView.setAdapter(searchAdapter);
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
                }
                Log.i("AttrList","recorded input is" +UserInput);
                //use same adapter and same list
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
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        attractions = db.getAttractions();
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

    private int LevenshteinDistance(String Userinput, String Answer){
        char[] UserinputChar = Userinput.toCharArray();
        char[] AnswerChar = Answer.toCharArray();

        int[][] LevenshteinMat = new int [UserinputChar.length+1][AnswerChar.length+1];
        for (int i=0; i<=UserinputChar.length;i++){
            LevenshteinMat[i][0]=i;
        }
        for (int j=0; j<=AnswerChar.length;j++){
            LevenshteinMat[0][j]=j;
        }
        int substitutionCost;
        for (int l=1;l<=Userinput.length();l++){
            for (int k=1;k<=Answer.length();k++){
                if (UserinputChar[l-1]==AnswerChar[k-1]){
                    substitutionCost=0;
                }
                else{
                    substitutionCost=1;
                }
                //Log.i("levetian","l is"+Integer.toString(l)+" k is"+Integer.toString(k));
                LevenshteinMat[l][k] = Collections.min(Arrays.asList(LevenshteinMat[l-1][k]+1,
                        LevenshteinMat[l][k-1]+1, LevenshteinMat[l-1][k-1]+substitutionCost));
            }
        }
        //Toast.makeText("diff is"+Integer.toString(LevenshteinMat[UserinputChar.length][AnswerChar.length],Toast.LENGTH_SHORT).show();
        //Log.i("levitian","diff is"+Integer.toString(LevenshteinMat[UserinputChar.length][AnswerChar.length]));
        return LevenshteinMat[UserinputChar.length][AnswerChar.length];
    }

    private int LongestLCS(String Userinput, String Answer){
        //ignore capitalization
        char[] UserinputChar = Userinput.toLowerCase().toCharArray();
        char[] AnswerChar = Answer.toLowerCase().toCharArray();

        int[][] LCSMat = new int [UserinputChar.length+1][AnswerChar.length+1];
        for (int i=0; i<=UserinputChar.length;i++){
            LCSMat[i][0]=0;
        }
        for (int j=0; j<=AnswerChar.length;j++){
            LCSMat[0][j]=0;
        }
        for (int l=1;l<=Userinput.length();l++){
            for (int k=1;k<=Answer.length();k++) {
                if (UserinputChar[l-1]==AnswerChar[k-1]){
                    LCSMat[l][k]=LCSMat[l-1][k-1]+1;
                }
                else{
                    LCSMat[l][k]=Collections.max(Arrays.asList(LCSMat[l][k-1],LCSMat[l-1][k]));
                }
            }
        }
        Log.i("LCS",Userinput+"//"+Answer+"is"+Integer.toString(LCSMat[UserinputChar.length][AnswerChar.length]));
        return LCSMat[UserinputChar.length][AnswerChar.length];
    }


}

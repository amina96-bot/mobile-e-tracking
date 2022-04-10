package com.starbrands.stb_carburant.ui.dashbord;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.MainActivity;
import com.starbrands.stb_carburant.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //variables
    TextView tab11 ;
    TextView tab12 ;
    TextView tab13 ;
    TextView tab14 ;

    TextView tab21 ;
    TextView tab22 ;
    TextView tab23 ;
    TextView tab24 ;
    TextView tab25 ;
    TextView tab26 ;
    TextView tab27 ;
    TextView tab28 ;
    TextView tab29 ;
    TextView tab210 ;
    TextView tab211 ;
    TextView tab212 ;
    TextView tab213 ;

    TextView tab31 ;
    TextView tab32 ;
    TextView tab33 ;
    TextView tab34 ;
    TextView tab35 ;

    TextView tab41 ;
    TextView tab42 ;


    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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
        View view= inflater.inflate(R.layout.fragment_stats, container, false);
        DbHandler db = new DbHandler(getContext());
        try{
            tab11 = (TextView) view.findViewById(R.id.tab11);
            tab12 = (TextView) view.findViewById(R.id.tab12);
            tab13 = (TextView) view.findViewById(R.id.tab13);
            tab14 = (TextView) view.findViewById(R.id.tab14);

//            tab21 = (TextView) view.findViewById(R.id.tab21);
            tab22 = (TextView) view.findViewById(R.id.tab22);
            tab23 = (TextView) view.findViewById(R.id.tab23);
            tab24 = (TextView) view.findViewById(R.id.tab24);
            tab25 = (TextView) view.findViewById(R.id.tab25);
            tab26 = (TextView) view.findViewById(R.id.tab26);
            tab27 = (TextView) view.findViewById(R.id.tab27);
            tab28 = (TextView) view.findViewById(R.id.tab28);
            tab29 = (TextView) view.findViewById(R.id.tab29);
//            tab210 =(TextView) view.findViewById(R.id.tab210);
            tab211 =(TextView) view.findViewById(R.id.tab211);
            tab212 =(TextView) view.findViewById(R.id.tab212);
            tab213 =(TextView) view.findViewById(R.id.tab213);
            tab31 = (TextView) view.findViewById(R.id.tab31);
            tab32 = (TextView) view.findViewById(R.id.tab32);
            tab33 = (TextView) view.findViewById(R.id.tab33);
            tab34 = (TextView) view.findViewById(R.id.tab34);
            tab35 = (TextView) view.findViewById(R.id.tab35);
            tab41 = (TextView) view.findViewById(R.id.tab41);
            tab42 = (TextView) view.findViewById(R.id.tab42);

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.FLOOR);

            tab11.setText(String.valueOf(db.nbTrajets()));
            tab12.setText(String.valueOf(db.nbPleins()));
            tab13.setText(String.valueOf(  change(db.dernierKilometrage()-db.kilometrageInitial(),4)  ));
            tab14.setText(String.valueOf(db.qteeCarburant()));

            tab22.setText(String.valueOf(db.coutTotalPleins()));
            tab23.setText(String.valueOf(change(db.coutTotalPleins()/db.nbPleins(),4) ));
            tab24.setText(String.valueOf(db.prixMoyLitre()));
            tab25.setText(String.valueOf(change(Double.valueOf(db.consommationMoyenne()) * 100,4)));

            if(db.nbPleins()<2){
                tab26.setText("--");
            }else{
                tab26.setText(String.valueOf(  change((db.dernierKilometrage()-db.kilometrageInitial())/(db.nbPleins()-1) ,4) ));
            }
            tab27.setText(String.valueOf(change(db.qteeCarburant()/db.nbPleins(),4)));
            tab28.setText(String.valueOf(  change(db.coutTotalPleins()/(db.dernierKilometrage()-db.kilometrageInitial()),4) ));
            tab29.setText(String.valueOf(db.coutCarburantJour()));
            tab211.setText(String.valueOf(db.nbPaimentsNaftal()));
            tab212.setText(String.valueOf(db.nbPaimentsBonCarburant()));
            tab213.setText(String.valueOf(db.nbPaimentsEspece()));

            tab31.setText(String.valueOf(db.nbTrajets()));
            tab32.setText((String.valueOf(  change(db.distanceTotaleTrajet(),4) )));
            tab33.setText((String.valueOf(  change(db.distanceTrajetTravail(),4) ) ));
            tab34.setText((String.valueOf(   change(db.distanceTrajetMission(),4) ) ));
            tab35.setText((String.valueOf(   change( db.distanceTrajetPersonnel(),4) )));

            tab41.setText(String.valueOf(db.kilometrageInitial()));
            tab42.setText(String.valueOf(db.dernierKilometrage()));

        }catch (Exception e){
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    public void dialogBox(String text){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(text);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    static double change(double value, int decimalpoint)
    {
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);
        return value;
    }
}
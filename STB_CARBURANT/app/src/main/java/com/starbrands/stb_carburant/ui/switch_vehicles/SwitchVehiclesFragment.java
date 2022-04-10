package com.starbrands.stb_carburant.ui.switch_vehicles;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwitchVehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwitchVehiclesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout ll;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DbHandler db = new DbHandler(getContext());

    public SwitchVehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwitchVehiclesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwitchVehiclesFragment newInstance(String param1, String param2) {
        SwitchVehiclesFragment fragment = new SwitchVehiclesFragment();
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
        View view = inflater.inflate(R.layout.fragment_switch_vehicles, container, false);
        ll= (LinearLayout) view.findViewById(R.id.switch_vehicles_linear_layout);
        DbHandler db = new DbHandler(getContext());
        ArrayList<HashMap<String, String>> currentAffectations = db.GetCurrentAffectations();
        System.out.println("**************you have xx current active affectations: "+currentAffectations.size());

        ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

        for (HashMap<String, String> map : currentAffectations) {
            CheckBox checkBox = new CheckBox(this.getContext());
            checkBox.setText(map.get("vh_code")  + "  /  "+map.get("vh_immatriculaton") );
            if(map.get("chosen_aff").equals("1")){
                checkBox.setChecked(true);
            }
            ll.addView(checkBox);
            mCheckBoxes.add(checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        db.setChosenAffectationId(Integer.parseInt(map.get("af_id")));
                        for (int i = 0; i < mCheckBoxes.size(); i++) {
                            if (mCheckBoxes.get(i) == checkBox) {
                                checkBox.setChecked(true);
                                dialogBox_save(map.get("vh_code"),map.get("vh_immatriculaton"));
                            }
                            else
                                mCheckBoxes.get(i).setChecked(false);
                        }
                    }
                }

            });

        }

        return view;
    }

    public void dialogBox_save(String code, String immatriculation) {
        DbHandler db = new DbHandler(getContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("vous avez choisi le véhicule "+code+" / "+immatriculation +db.getChosenAffectationId());

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
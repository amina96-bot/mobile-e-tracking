package com.starbrands.stb_carburant.ui.dashbord;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoTrajetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoTrajetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textView_kilometrage_depart;
    TextView textView_kilometrage_arrivee;
    TextView textView_lieu_depart;
    TextView textView_lieu_arrivee;
    TextView textView_date_depart;
    TextView textView_date_arrivee;
    TextView textView_notes;
    TextView trajetStatus;

    CheckBox checkBoxTravail;
    CheckBox checkBoxMission;
    CheckBox checkBoxPersonnel;

    Button delete_button;
    Button edit_button;

    LinearLayout linearLayout5;
    LinearLayout linearLayout6;
    LinearLayout linearLayout7;

    public InfoTrajetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoTrajetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoTrajetFragment newInstance(String param1, String param2) {
        InfoTrajetFragment fragment = new InfoTrajetFragment();
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
        View view= inflater.inflate(R.layout.fragment_info_trajet, container, false);
        DbHandler db = new DbHandler(getContext());

        textView_kilometrage_depart = (TextView) view.findViewById(R.id.textView_kilometrage_depart);
        textView_kilometrage_arrivee = (TextView) view.findViewById(R.id.textView_kilometrage_arrivee);

        textView_lieu_depart = (TextView) view.findViewById(R.id.textView_lieu_depart);
        textView_lieu_arrivee = (TextView) view.findViewById(R.id.textView_lieu_arrivee);
        textView_date_depart = (TextView) view.findViewById(R.id.textView_date_depart);
        textView_date_arrivee = (TextView) view.findViewById(R.id.textView_date_arrivee);
        textView_notes = (TextView) view.findViewById(R.id.textView_notes);
        trajetStatus=(TextView) view.findViewById(R.id.trajetStatus);

        checkBoxTravail = (CheckBox) view.findViewById(R.id.checkBoxTravail);
        checkBoxMission = (CheckBox) view.findViewById(R.id.checkBoxMission);
        checkBoxPersonnel = (CheckBox) view.findViewById(R.id.checkBoxPersonnel);

        checkBoxTravail.setEnabled(false);
        checkBoxMission.setEnabled(false);
        checkBoxPersonnel.setEnabled(false);

        linearLayout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);
        linearLayout7 = (LinearLayout) view.findViewById(R.id.linearLayout7);

        //        delete_button = (Button) view.findViewById(R.id.delete_button);
        edit_button = (Button) view.findViewById(R.id.edit_button);

        Bundle args = getArguments();
        if (args != null) {
            int idTrajet= Integer.parseInt(args.getString("id"));
            SQLiteDatabase readableDb = db.getReadableDatabase();
            Cursor cursor= readableDb.rawQuery(" select * from  TRAJET where tr_id=" +idTrajet,null);

            if(cursor!=null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                textView_date_depart.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_date_depart"))));
                textView_date_arrivee.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_date_arrivee"))));

                textView_kilometrage_depart.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart"))));
                textView_kilometrage_arrivee.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee"))));

                textView_lieu_depart.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_lieu_depart"))));
                textView_lieu_arrivee.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_lieu_arrivee"))));

                textView_notes.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_note"))));

                String motif=String.valueOf(cursor.getString(cursor.getColumnIndex("tr_motif")));
                switch (motif){
                    case "TRAVAIL":
                        checkBoxTravail.setChecked(true);
                        checkBoxMission.setChecked(false);
                        checkBoxPersonnel.setChecked(false);
                        break;
                    case "MISSION":
                        checkBoxTravail.setChecked(false);
                        checkBoxMission.setChecked(true);
                        checkBoxPersonnel.setChecked(false);
                        break;
                    case "PERSONNEL":
                        checkBoxTravail.setChecked(false);
                        checkBoxMission.setChecked(false);
                        checkBoxPersonnel.setChecked(true);
                        break;
                }

                if(Integer.valueOf(cursor.getString(cursor.getColumnIndex("tr_type")))==0){ // nouveau trajet lancé synchrone
                    if(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee"))>0){ // trajet arreté
                        trajetStatus.setText("Enregistrement trajet terminé");
                        trajetStatus.setTextColor(0xFF4CAF50);

                        edit_button.setVisibility(View.INVISIBLE);


                    }else{ // trajet toujours en cours
                        linearLayout5.setVisibility(View.INVISIBLE);
                        ViewGroup.LayoutParams params = linearLayout5.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout5.setLayoutParams(params);

                        linearLayout6.setVisibility(View.INVISIBLE);
                        params = linearLayout6.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout6.setLayoutParams(params);

                        linearLayout7.setVisibility(View.INVISIBLE);
                        params = linearLayout7.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout7.setLayoutParams(params);

                        trajetStatus.setText("Enregistrement trajet en cours..");
                        trajetStatus.setTextColor(0xFFF44336);
                        edit_button.setVisibility(View.VISIBLE);
                        edit_button.setText("Arrêter le trajet");
                        edit_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle b = new Bundle();
                                b.putString("id", String.valueOf(idTrajet));

                                EditTrajetFragment editTrajetFragment= new EditTrajetFragment();
                                editTrajetFragment.setArguments(b);
                                InfoTrajetFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,editTrajetFragment).commit();
                            }
                        });
                    }
                }

                if(cursor.getInt(cursor.getColumnIndex("tr_type"))==1){ // ancien trajet saisi
                    trajetStatus.setText("Ancien trajet saisi");

                    if(cursor.getInt(cursor.getColumnIndex("tr_sync_id"))==0){
                        //trajet pas encore synchronise ==> modification possible
                        edit_button.setVisibility(View.VISIBLE);
                        edit_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle b = new Bundle();
                                b.putString("id", String.valueOf(idTrajet));

                                EditOldTrajetFragment editOldTrajetFragment= new EditOldTrajetFragment();
                                editOldTrajetFragment.setArguments(b);
                                InfoTrajetFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,editOldTrajetFragment).commit();
                            }
                        });

                    }else{
                        //trajet synchronise ==> modification impossible
                        edit_button.setVisibility(View.INVISIBLE);
                    }

                }
//                delete_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle b = new Bundle();
//                        b.putString("id", String.valueOf(idTrajet));
//                        b.putString("type", "tr");
//
//                        DeleteFragment deleteFragment= new DeleteFragment();
//                        deleteFragment.setArguments(b);
//                        InfoTrajetFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,deleteFragment).commit();
//                    }
//                });
            }
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Informations trajet");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return  view;
    }
}
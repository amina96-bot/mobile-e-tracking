package com.starbrands.stb_carburant.ui.trajet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;
import com.starbrands.stb_carburant.ui.dashbord.EditTrajetFragment;
import com.starbrands.stb_carburant.ui.dashbord.InfoTrajetFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrajetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrajetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_LOCATION = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LocationManager locationManager;

    int usr_id;

    String selected_date_depart = null;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    TextView calendar_text_depart;
    EditText kilometrage_view_depart;
    EditText lieu_view_depart;
    String motif_view;
    EditText notes_view;
    CheckBox checkBoxTravail;
    CheckBox checkBoxMission;
    CheckBox checkBoxPersonnel;

    LinearLayout linearLayout0;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    LinearLayout linearLayout5;
    LinearLayout linearLayout6;

    ImageButton imageButton;

    public TrajetFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrajetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrajetFragment newInstance(String param1, String param2) {
        TrajetFragment fragment = new TrajetFragment();
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
        View view = inflater.inflate(R.layout.fragment_trajet, container, false);
        DbHandler db = new DbHandler(getContext());
        try {
            usr_id = db.GetLastUser_id();
            kilometrage_view_depart = (EditText) view.findViewById(R.id.editText_kilometrage_depart);
            lieu_view_depart = (EditText) view.findViewById(R.id.editText_lieu_depart);

            checkBoxTravail = (CheckBox) view.findViewById(R.id.checkBoxTravail);
            checkBoxMission = (CheckBox) view.findViewById(R.id.checkBoxMission);
            checkBoxPersonnel = (CheckBox) view.findViewById(R.id.checkBoxPersonnel);

            linearLayout0 = (LinearLayout) view.findViewById(R.id.linearLayout0);
            linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
            linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
            linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
            linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);
            linearLayout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
            linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);


            notes_view = (EditText) view.findViewById(R.id.notes);

            linearLayout1.setVisibility(View.INVISIBLE);
            LayoutParams params = linearLayout1.getLayoutParams();
            params.height = 0;
            params.width = 0;
            linearLayout1.setLayoutParams(params);

            linearLayout2.setVisibility(View.INVISIBLE);
            params = linearLayout2.getLayoutParams();
            params.height = 0;
            params.width = 0;
            linearLayout2.setLayoutParams(params);

            linearLayout3.setVisibility(View.INVISIBLE);
            params = linearLayout3.getLayoutParams();
            params.height = 0;
            params.width = 0;
            linearLayout3.setLayoutParams(params);

            linearLayout4.setVisibility(View.INVISIBLE);
            params = linearLayout4.getLayoutParams();
            params.height = 0;
            params.width = 0;
            linearLayout4.setLayoutParams(params);

            linearLayout5.setVisibility(View.INVISIBLE);
            params = linearLayout5.getLayoutParams();
            params.height = 0;
            params.width = 0;
            linearLayout5.setLayoutParams(params);


            imageButton=(ImageButton) view.findViewById(R.id.nouveau_trajet);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    linearLayout0.setVisibility(View.INVISIBLE);
                    LayoutParams params = linearLayout0.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    linearLayout0.setLayoutParams(params);

                    linearLayout1.setVisibility(View.VISIBLE);
                    params = linearLayout1.getLayoutParams();
                    params.height = LayoutParams.WRAP_CONTENT;
                    params.width = LayoutParams.MATCH_PARENT;
                    linearLayout1.setLayoutParams(params);

                    linearLayout6.setVisibility(View.INVISIBLE);
                    params = linearLayout6.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    linearLayout6.setLayoutParams(params);
                }

            });

            checkBoxTravail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxTravail.isChecked()) {
                        motif_view = "TRAVAIL";
                        checkBoxMission.setChecked(false);
                        checkBoxPersonnel.setChecked(false);
                        linearLayout2.setVisibility(View.VISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.VISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.VISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout4.setLayoutParams(params);

                        linearLayout5.setVisibility(View.VISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout5.setLayoutParams(params);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.INVISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.INVISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout4.setLayoutParams(params);

                        linearLayout5.setVisibility(View.INVISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout5.setLayoutParams(params);
                    }
                }
            });

            checkBoxMission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxMission.isChecked()) {
                        motif_view = "MISSION";
                        checkBoxTravail.setChecked(false);
                        checkBoxPersonnel.setChecked(false);

                        linearLayout2.setVisibility(View.VISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.VISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.VISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout4.setLayoutParams(params);

                        linearLayout5.setVisibility(View.VISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout5.setLayoutParams(params);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.INVISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.INVISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout4.setLayoutParams(params);


                        linearLayout5.setVisibility(View.INVISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout5.setLayoutParams(params);
                    }
                }
            });

            checkBoxPersonnel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxPersonnel.isChecked()) {
                        motif_view = "PERSONNEL";
                        checkBoxTravail.setChecked(false);
                        checkBoxMission.setChecked(false);

                        linearLayout2.setVisibility(View.VISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.VISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.VISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout4.setLayoutParams(params);

                        linearLayout5.setVisibility(View.VISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = LayoutParams.WRAP_CONTENT;
                        params.width = LayoutParams.MATCH_PARENT;
                        linearLayout5.setLayoutParams(params);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        LayoutParams params = linearLayout2.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout2.setLayoutParams(params);

                        linearLayout3.setVisibility(View.INVISIBLE);
                        params = linearLayout3.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout3.setLayoutParams(params);

                        linearLayout4.setVisibility(View.INVISIBLE);
                        params = linearLayout4.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout4.setLayoutParams(params);

                        linearLayout5.setVisibility(View.INVISIBLE);
                        params = linearLayout5.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        linearLayout5.setLayoutParams(params);
                    }
                }
            });
            
            /*-----------------------------------------------boutton sauvgarder-----------------------------------------------*/
            ImageButton button = (ImageButton) view.findViewById(R.id.save_button3);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // get user id
                    int tr_usr_id = db.GetLastUser_id();

                    // get system date as date depart
                    String tr_date_depart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    // get kilometrage départ
                    double tr_kilometrage_depart = 0;
                    String kilometrage_depart = kilometrage_view_depart.getText().toString();
                    if (pattern.matcher(kilometrage_depart).matches())
                        tr_kilometrage_depart = Double.parseDouble(kilometrage_depart);

                    // get Lieu départ
                    String tr_lieu_depart = lieu_view_depart.getText().toString();

                    // get motif
                    String tr_motif = motif_view;

                    // get notes
                    String tr_note = notes_view.getText().toString();

                    int tr_sync_id = 0;
                    String tr_sync_date = "";

                    // save infos
                    if (tr_kilometrage_depart != 0 && !tr_lieu_depart.equals(null)) {
                                SQLiteDatabase readableDb = db.getReadableDatabase();
                                Cursor cursor= readableDb.rawQuery(" select date, kilometrage from " +
                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +  " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                        "  ) order by date",null);

                                if(cursor!=null && cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    String firstDate=String.valueOf(cursor.getString(cursor.getColumnIndex("date")));
                                    cursor.moveToLast();
                                    String lastDate =String.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                                    if(tr_date_depart.compareTo(lastDate) > 0){
                                        cursor.moveToLast();
                                        String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                        if (Double.parseDouble(km1) > tr_kilometrage_depart) {
                                            dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_depart);
                                        }else{
                                            db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,null,0,null,tr_note,tr_sync_id,tr_sync_date,0,db.getChosenAffectationId());
                                            dialogBox_save();
                                        }
                                    }else if(tr_date_depart.compareTo(firstDate) < 0){
                                        // date depart avant la premiere date            tr_date_depart ------>  firstDate
                                        cursor.moveToFirst();
                                        String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                        if (Double.parseDouble(km1) < tr_kilometrage_depart){
                                            dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_depart);
                                        }else{

                                            db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,null,0,null,tr_note,tr_sync_id,tr_sync_date,0,db.getChosenAffectationId());
                                            dialogBox_save();
                                        }
                                    }else if (tr_date_depart.compareTo(firstDate) > 0 && tr_date_depart.compareTo(lastDate) < 0){
                                        Cursor cursorBefore= readableDb.rawQuery(" select date, kilometrage from " +
                                                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +  " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                "  ) where date< "+"'"+tr_date_depart+"'"+" order by date asc",null);
                                        cursorBefore.moveToLast();
                                        String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                                        Cursor cursorAfter= readableDb.rawQuery(" select date, kilometrage from " +
                                                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +  " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                "  ) where date> "+"'"+tr_date_depart+"'"+" order by date asc",null);
                                        cursorAfter.moveToFirst();
                                        String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                                        if (tr_kilometrage_depart < Double.parseDouble(kmBefore)) {
                                            dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_depart);
                                        }else if(tr_kilometrage_depart > Double.parseDouble(kmAfter)){
                                            dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_depart);
                                        }else{
                                            db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,null,0,null,tr_note,tr_sync_id,tr_sync_date,0,db.getChosenAffectationId());
                                            dialogBox_save();
                                        }
                                    }
                                }else{
                                    // premier trajet
                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,null,0,null,tr_note,tr_sync_id,tr_sync_date,0,db.getChosenAffectationId());
                                    dialogBox_save();
                                }
                        }else {
                        dialogBox("Veuillez compléter les informations manquantes !");
                    }
                }
            });

            ImageButton button2 = (ImageButton) view.findViewById(R.id.old_trajet);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    DialogFragment newFragment = OldTrajetFragment.newInstance();
                    newFragment.show(ft, "dialog");
                }
            });
        } catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Ajouter un trajet");
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


        return view;
    }

    public void dialogBox(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(text);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dialogBox_save() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("Enregistrement d'un nouveau trajet en cours...");
        //--------------------------------------------------------------------------------------------------------------

        // add the buttons
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
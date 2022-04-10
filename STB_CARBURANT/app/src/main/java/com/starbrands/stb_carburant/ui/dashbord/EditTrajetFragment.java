package com.starbrands.stb_carburant.ui.dashbord;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTrajetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTrajetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int usr_id;
    String selected_date_depart = null;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    TextView calendar_text_depart;
    TextView kilometrage_view_depart;
    TextView lieu_view_depart;
    String motif_view;
    EditText notes_view;
    CheckBox checkBoxTravail;
    CheckBox checkBoxMission;
    CheckBox checkBoxPersonnel;

    LinearLayout linearLayout2;
    LinearLayout linearLayout4;

    String selected_date_arrivee = null;
    TextView calendar_text_arrivee;
    EditText kilometrage_view_arrivee;
    EditText lieu_view_arrivee;
    LinearLayout linearLayout3;

    ImageView save_button;
    ImageView stop_button;

    TextView save;
    TextView stop;

    int idTrajet=0;

    public EditTrajetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTrajetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTrajetFragment newInstance(String param1, String param2) {
        EditTrajetFragment fragment = new EditTrajetFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_trajet, container, false);
        DbHandler db = new DbHandler(getContext());
        try {
            usr_id = db.GetLastUser_id();
            calendar_text_depart = (TextView) view.findViewById(R.id.adf_text_calendar_depart);
            calendar_text_arrivee = (TextView) view.findViewById(R.id.adf_text_calendar_arrivee);

            kilometrage_view_depart = (TextView) view.findViewById(R.id.textView_kilometrage_depart);
            kilometrage_view_arrivee = (EditText) view.findViewById(R.id.editText_kilometrage_arrivee);

            lieu_view_depart = (TextView) view.findViewById(R.id.textView_lieu_depart);
            lieu_view_arrivee = (EditText) view.findViewById(R.id.editText_lieu_arrivee);

            checkBoxTravail = (CheckBox) view.findViewById(R.id.checkBoxTravail);
            checkBoxMission = (CheckBox) view.findViewById(R.id.checkBoxMission);
            checkBoxPersonnel = (CheckBox) view.findViewById(R.id.checkBoxPersonnel);

            checkBoxTravail.setEnabled(false);
            checkBoxMission.setEnabled(false);
            checkBoxPersonnel.setEnabled(false);

            linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
            linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
            linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);

            notes_view = (EditText) view.findViewById(R.id.notes);

            save_button = (ImageView) view.findViewById(R.id.save_button);
            stop_button = (ImageView) view.findViewById(R.id.stop_button);

            save = (TextView) view.findViewById(R.id.save);
            stop = (TextView) view.findViewById(R.id.stop);

            checkBoxTravail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxTravail.isChecked()) {
                        motif_view = "TRAVAIL";
                        checkBoxMission.setChecked(false);
                        checkBoxPersonnel.setChecked(false);
                        linearLayout2.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
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
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
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
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
                    }
                }
            });

            Bundle args = getArguments();
            if (args != null) {
                idTrajet= Integer.parseInt(args.getString("id"));
                SQLiteDatabase readableDb = db.getReadableDatabase();
                Cursor cursor= readableDb.rawQuery(" select * from  TRAJET where tr_id=" +idTrajet,null);
                if(cursor!=null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    selected_date_depart = String.valueOf(cursor.getString(cursor.getColumnIndex("tr_date_depart")));
                    calendar_text_depart.setText(selected_date_depart);

                    save_button.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.INVISIBLE);

                    kilometrage_view_depart.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart"))));

                    lieu_view_depart.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_lieu_depart"))));

                    notes_view.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("tr_note"))));

                    String motif = String.valueOf(cursor.getString(cursor.getColumnIndex("tr_motif")));
                    motif_view=motif;

                    switch (motif) {
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

                    //stop button on click
                    stop_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selected_date_arrivee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            calendar_text_arrivee.setText(selected_date_arrivee);

                            stop_button.setVisibility(View.INVISIBLE);
                            stop.setVisibility(View.INVISIBLE);
                            save_button.setVisibility(View.VISIBLE);
                            save.setVisibility(View.VISIBLE);
                        }
                    });

                    int tr_sync_id = cursor.getInt(cursor.getColumnIndex("tr_sync_id"));
                    String tr_sync_date = cursor.getString(cursor.getColumnIndex("tr_sync_date"));

                    /*-----------------------------------------------boutton update-----------------------------------------------*/
                    save_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // get user id
                            int tr_usr_id = db.GetLastUser_id();

                            String tr_date_depart = selected_date_depart;

                            String tr_date_arrivee = selected_date_arrivee;

                            // get kilometrage départ
                            double tr_kilometrage_depart = 0;
                            String kilometrage_depart = kilometrage_view_depart.getText().toString();
                            if (pattern.matcher(kilometrage_depart).matches())
                                tr_kilometrage_depart = Double.parseDouble(kilometrage_depart);

                            // get kilometrage arrivée
                            double tr_kilometrage_arrivee = 0;
                            String kilometrage_arrivee = kilometrage_view_arrivee.getText().toString();
                            if (pattern.matcher(kilometrage_arrivee).matches())
                                tr_kilometrage_arrivee = Double.parseDouble(kilometrage_arrivee);

                            // get Lieu départ
                            String tr_lieu_depart = lieu_view_depart.getText().toString();

                            // get Lieu arrivée
                            String tr_lieu_arrivee = lieu_view_arrivee.getText().toString();

                            // get motif
                            String tr_motif = motif_view;

                            // get notes
                            String tr_note = notes_view.getText().toString();

                            // save infos
                            if (tr_kilometrage_arrivee != 0) {
                                if (tr_date_depart.compareTo(tr_date_arrivee) > 0) {
                                    dialogBox("Attention, une date d'arrivée ne peut précéder une date de départ !");
                                } else if (tr_kilometrage_depart > tr_kilometrage_arrivee) {
                                    dialogBox("Attention, un kilométrage d'arrivée ne peut être inférieur à un kilométrage de départ !");
                                } else {
                                    SQLiteDatabase readableDb = db.getReadableDatabase();
                                    Cursor cursor = readableDb.rawQuery(" select date, kilometrage from " +
                                            "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                            " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                            " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_id!= " + idTrajet +
                                            " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null " + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 and tr_id!= " + idTrajet +
                                            "  ) order by date", null);

                                    if (cursor != null && cursor.getCount() > 0) {
                                        cursor.moveToFirst();
                                        String firstDate = String.valueOf(cursor.getString(cursor.getColumnIndex("date")));
                                        cursor.moveToLast();
                                        String lastDate = String.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                                        if (tr_date_arrivee.compareTo(lastDate) > 0) {
                                            cursor.moveToLast();
                                            String km2 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                            if (Double.parseDouble(km2) > tr_kilometrage_arrivee) {
                                                dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                            } else {
                                                db.updateTrajet(idTrajet, tr_usr_id, tr_motif, tr_date_depart, tr_kilometrage_depart, tr_lieu_depart, tr_date_arrivee, tr_kilometrage_arrivee, tr_lieu_arrivee, tr_note,tr_sync_id,tr_sync_date);

                                                if (db.existTrajetsEnCours()) {
                                                    // notification code  ------------------------------------------------------------------------------------------
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "my notification");
                                                    builder.setContentTitle("trajet en cours");
                                                    builder.setContentText("Vous avez au moins un trajet en cours");
                                                    builder.setSmallIcon(R.drawable.trajet_en_cours);
                                                    builder.setAutoCancel(false);
                                                    builder.setOngoing(true);
                                                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
                                                    managerCompat.notify(1, builder.build());
                                                    //--------------------------------------------------------------------------------------------------------------
                                                }

                                                dialogBox_edit();
                                            }
                                        } else if (tr_date_arrivee.compareTo(firstDate) < 0) {
                                            cursor.moveToFirst();
                                            String km2 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                            if (Double.parseDouble(km2) < tr_kilometrage_arrivee) {
                                                dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                            } else {
                                                db.updateTrajet(idTrajet, tr_usr_id, tr_motif, tr_date_depart, tr_kilometrage_depart, tr_lieu_depart, tr_date_arrivee, tr_kilometrage_arrivee, tr_lieu_arrivee, tr_note,tr_sync_id,tr_sync_date);
                                                dialogBox_edit();
                                            }
                                        } else if (tr_date_arrivee.compareTo(firstDate) > 0 && tr_date_arrivee.compareTo(lastDate) < 0) {
                                            Cursor cursorBefore = readableDb.rawQuery(" select date, kilometrage from " +
                                                    "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                    " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                                    " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_id!= " + idTrajet +
                                                    " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null " + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 and tr_id!= " + idTrajet +
                                                    "  ) where date< " + "'" + tr_date_arrivee + "'" + " order by date asc", null);
                                            cursorBefore.moveToLast();
                                            String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                                            Cursor cursorAfter = readableDb.rawQuery(" select date, kilometrage from " +
                                                    "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                    " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                                    " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_id!= " + idTrajet +
                                                    " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null " + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 and tr_id!= " + idTrajet +
                                                    "  ) where date> " + "'" + tr_date_arrivee + "'" + " order by date asc", null);
                                            cursorAfter.moveToFirst();
                                            String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                                            if (tr_kilometrage_arrivee < Double.parseDouble(kmBefore)) {
                                                dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                            } else if (tr_kilometrage_arrivee > Double.parseDouble(kmAfter)) {
                                                dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                            } else {
                                                db.updateTrajet(idTrajet, tr_usr_id, tr_motif, tr_date_depart, tr_kilometrage_depart, tr_lieu_depart, tr_date_arrivee, tr_kilometrage_arrivee, tr_lieu_arrivee, tr_note,tr_sync_id,tr_sync_date);
                                                dialogBox_edit();
                                            }
                                        }
                                    }
                                }
                            } else {
                                dialogBox("Veuillez compléter les informations manquantes !");
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Arrêter le trajet");
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

    public void dialogBox_edit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("Trajet terminé avec succès");

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
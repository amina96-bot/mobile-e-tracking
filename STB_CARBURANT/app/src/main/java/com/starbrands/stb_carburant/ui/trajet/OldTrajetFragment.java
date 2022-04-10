
package com.starbrands.stb_carburant.ui.trajet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
 * Use the {@link OldTrajetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OldTrajetFragment extends DialogFragment {

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

    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;

    LinearLayout linearLayout5;
    LinearLayout linearLayout6;
    LinearLayout linearLayout7;
    LinearLayout linearLayout8;
    LinearLayout linearLayout9;


    String selected_date_arrivee = null;
    TextView calendar_text_arrivee;
    EditText kilometrage_view_arrivee;
    EditText lieu_view_arrivee;

//    DatePicker datePicker ;
//    TimePicker timePicker;

    public OldTrajetFragment() {
    }


    public static OldTrajetFragment newInstance() {
        OldTrajetFragment fragment = new OldTrajetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_old_trajet, container, false);
        DbHandler db = new DbHandler(getContext());
        try {
            usr_id = db.GetLastUser_id();
            calendar_text_depart = (TextView) view.findViewById(R.id.adf_text_calendar_depart);
            calendar_text_arrivee = (TextView) view.findViewById(R.id.adf_text_calendar_arrivee);

            kilometrage_view_depart = (EditText) view.findViewById(R.id.editText_kilometrage_depart);
            kilometrage_view_arrivee = (EditText) view.findViewById(R.id.editText_kilometrage_arrivee);

            lieu_view_depart = (EditText) view.findViewById(R.id.editText_lieu_depart);
            lieu_view_arrivee = (EditText) view.findViewById(R.id.editText_lieu_arrivee);

            checkBoxTravail = (CheckBox) view.findViewById(R.id.checkBoxTravail);
            checkBoxMission = (CheckBox) view.findViewById(R.id.checkBoxMission);
            checkBoxPersonnel = (CheckBox) view.findViewById(R.id.checkBoxPersonnel);

            linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
            linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
            linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);
            linearLayout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
            linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);
            linearLayout7 = (LinearLayout) view.findViewById(R.id.linearLayout7);
            linearLayout8 = (LinearLayout) view.findViewById(R.id.linearLayout8);
            linearLayout9 = (LinearLayout) view.findViewById(R.id.linearLayout9);

            notes_view = (EditText) view.findViewById(R.id.notes);


//            DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
//            TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);


            linearLayout2.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
            linearLayout4.setVisibility(View.INVISIBLE);
            linearLayout5.setVisibility(View.INVISIBLE);
            linearLayout6.setVisibility(View.INVISIBLE);
            linearLayout7.setVisibility(View.INVISIBLE);
            linearLayout8.setVisibility(View.INVISIBLE);
            linearLayout9.setVisibility(View.INVISIBLE);

            // checkbox listener
            checkBoxTravail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxTravail.isChecked()) {
                        System.out.println("travail checked");
                        motif_view = "TRAVAIL";
                        checkBoxMission.setChecked(false);
                        checkBoxPersonnel.setChecked(false);
                        linearLayout2.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                        linearLayout5.setVisibility(View.VISIBLE);
                        linearLayout6.setVisibility(View.VISIBLE);
                        linearLayout7.setVisibility(View.VISIBLE);
                        linearLayout8.setVisibility(View.VISIBLE);
                        linearLayout9.setVisibility(View.VISIBLE);

                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
                        linearLayout5.setVisibility(View.INVISIBLE);
                        linearLayout6.setVisibility(View.INVISIBLE);
                        linearLayout7.setVisibility(View.INVISIBLE);
                        linearLayout8.setVisibility(View.INVISIBLE);
                        linearLayout9.setVisibility(View.INVISIBLE);
                    }
                }
            });

            checkBoxMission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxMission.isChecked()) {
                        System.out.println("mission checked");
                        motif_view = "MISSION";
                        checkBoxTravail.setChecked(false);
                        checkBoxPersonnel.setChecked(false);
                        linearLayout2.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                        linearLayout5.setVisibility(View.VISIBLE);
                        linearLayout6.setVisibility(View.VISIBLE);
                        linearLayout7.setVisibility(View.VISIBLE);
                        linearLayout8.setVisibility(View.VISIBLE);
                        linearLayout9.setVisibility(View.VISIBLE);

                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
                        linearLayout5.setVisibility(View.INVISIBLE);
                        linearLayout6.setVisibility(View.INVISIBLE);
                        linearLayout7.setVisibility(View.INVISIBLE);
                        linearLayout8.setVisibility(View.INVISIBLE);
                        linearLayout9.setVisibility(View.INVISIBLE);
                    }
                }
            });

            checkBoxPersonnel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxPersonnel.isChecked()) {
                        System.out.println("personnel checked");
                        motif_view = "PERSONNEL";
                        checkBoxTravail.setChecked(false);
                        checkBoxMission.setChecked(false);
                        linearLayout2.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                        linearLayout5.setVisibility(View.VISIBLE);
                        linearLayout6.setVisibility(View.VISIBLE);
                        linearLayout7.setVisibility(View.VISIBLE);
                        linearLayout8.setVisibility(View.VISIBLE);
                        linearLayout9.setVisibility(View.VISIBLE);

                    } else {
                        linearLayout2.setVisibility(View.INVISIBLE);
                        linearLayout3.setVisibility(View.INVISIBLE);
                        linearLayout4.setVisibility(View.INVISIBLE);
                        linearLayout5.setVisibility(View.INVISIBLE);
                        linearLayout6.setVisibility(View.INVISIBLE);
                        linearLayout7.setVisibility(View.INVISIBLE);
                        linearLayout8.setVisibility(View.INVISIBLE);
                        linearLayout9.setVisibility(View.INVISIBLE);
                    }
                }
            });

            /*-----------------------------------------------Calendrier départ-----------------------------------------------*/
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker= new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String hour = String.valueOf(selectedHour);
                    String minute = String.valueOf(selectedMinute);
                    if (hour.length() == 1)
                        hour = "0" + hour;
                    if (minute.length() == 1)
                        minute = "0" + minute;

                    calendar_text_depart.setText(calendar_text_depart.getText() + " " + hour + ":" + minute + ":00");
                    selected_date_depart = selected_date_depart + " " + hour + ":" + minute + ":00";
                }
            }, hour, minute, true);//Yes 24 hour time

            final Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    if (month.length() == 1)
                        month = "0" + month;
                    String day = String.valueOf(
                            dayOfMonth);
                    if (day.length() == 1)
                        day = "0" + day;
                    calendar_text_depart.setText(String.valueOf(year) + "-" + month + "-" + day);
                    selected_date_depart = String.valueOf(year) + "-" + month + "-" + day;
                    mTimePicker.show();
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            calendar_text_depart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartTime.getDatePicker().setMaxDate(new Date().getTime());
                    StartTime.show();
                }
            });

            ImageView calendar_depart = (ImageView) view.findViewById(R.id.adf_img_calendar_depart);
            calendar_depart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartTime.getDatePicker().setMaxDate(new Date().getTime());
                    StartTime.show();
                }
            });

            /*-----------------------------------------------Calendrier arrivée-----------------------------------------------*/
            Calendar mcurrentTime2 = Calendar.getInstance();
            int hour2 = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
            int minute2 = mcurrentTime2.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker2= new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker2, int selectedHour2, int selectedMinute2) {
                    String hour2 = String.valueOf(selectedHour2);
                    String minute2 = String.valueOf(selectedMinute2);
                    if (hour2.length() == 1)
                        hour2 = "0" + hour2;
                    if (minute2.length() == 1)
                        minute2 = "0" + minute2;

                    calendar_text_arrivee.setText(calendar_text_arrivee.getText() + " " + hour2 + ":" + minute2 + ":00");
                    selected_date_arrivee = selected_date_arrivee + " " + hour2 + ":" + minute2 + ":00";
                }
            }, hour2, minute2, true);//Yes 24 hour time

            final Calendar newCalendar2 = Calendar.getInstance();
            final DatePickerDialog StartTime2 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    if (month.length() == 1)
                        month = "0" + month;
                    String day = String.valueOf(dayOfMonth);
                    if (day.length() == 1)
                        day = "0" + day;
                    calendar_text_arrivee.setText(String.valueOf(year) + "-" + month + "-" + day);
                    selected_date_arrivee = String.valueOf(year) + "-" + month + "-" + day;
                    mTimePicker2.show();
                }
            }, newCalendar2.get(Calendar.YEAR), newCalendar2.get(Calendar.MONTH), newCalendar2.get(Calendar.DAY_OF_MONTH));

            calendar_text_arrivee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartTime2.getDatePicker().setMaxDate(new Date().getTime());
                    StartTime2.show();

                }
            });

            ImageView calendar_arrivee = (ImageView) view.findViewById(R.id.adf_img_calendar_arrivee);
            calendar_arrivee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartTime2.getDatePicker().setMaxDate(new Date().getTime());
                    StartTime2.show();
                }
            });

            /*-----------------------------------------------boutton sauvgarder-----------------------------------------------*/
            ImageView button = (ImageView) view.findViewById(R.id.save_button2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // get user id
                    int tr_usr_id = db.GetLastUser_id();

                    // get selected date départ or replace by sysdate
                    String tr_date_depart = "";
                    if (selected_date_depart== null)
                    {
                        selected_date_depart=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        tr_date_depart = selected_date_depart;
                    }
                    else
                        tr_date_depart = selected_date_depart;

                    // get selected date arrivée or replace by sysdate
                    String tr_date_arrivee = "";
                    if (selected_date_arrivee== null)
                    {
                        selected_date_arrivee=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        tr_date_arrivee = selected_date_arrivee;
                    }
                    else
                        tr_date_arrivee = selected_date_arrivee;


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

                    int tr_sync_id = 0;
                    String tr_sync_date = "";

                    // save infos
                    if (tr_kilometrage_depart != 0 && tr_kilometrage_arrivee != 0 && !tr_lieu_depart.equals(null) && !tr_lieu_arrivee.equals(null)) {
                        if(tr_date_depart.compareTo(tr_date_arrivee) > 0){
                            dialogBox("Attention, une date d'arrivée ne peut précéder une date de départ !");
                        }else if(tr_kilometrage_depart>tr_kilometrage_arrivee) {
                            dialogBox("Attention, un kilométrage d'arrivée ne peut être inférieur à un kilométrage de départ !");
                        }else{
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
                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                    dialogBox_save();
                                }
                            }else if(tr_date_depart.compareTo(firstDate) < 0){
                                // date depart avant la premiere date            tr_date_depart ------>  firstDate
                                cursor.moveToFirst();
                                String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                if (Double.parseDouble(km1) < tr_kilometrage_depart){
                                    dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_depart);
                                }else if ( tr_date_depart.compareTo(firstDate) > 0  &&  tr_date_depart.compareTo(lastDate) < 0){
                                    //on vérifie arrivee
                                            if(tr_date_arrivee.compareTo(lastDate) > 0){
                                                cursor.moveToLast();
                                                String km2 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                                if (Double.parseDouble(km2) > tr_kilometrage_arrivee) {
                                                    dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else{
                                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                                    dialogBox_save();
                                                }
                                            }else if(tr_date_arrivee.compareTo(firstDate) < 0){
                                                cursor.moveToFirst();
                                                String km2 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                                if (Double.parseDouble(km2) < tr_kilometrage_arrivee){
                                                    dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else{
                                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                                    dialogBox_save();
                                                }
                                            }else if (tr_date_arrivee.compareTo(firstDate) > 0 && tr_date_arrivee.compareTo(lastDate) < 0){
                                                Cursor cursorBefore= readableDb.rawQuery(" select date, kilometrage from " +
                                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +" and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                        "  ) where date< "+"'"+tr_date_arrivee+"'"+" order by date asc",null);
                                                cursorBefore.moveToLast();
                                                String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                                                Cursor cursorAfter= readableDb.rawQuery(" select date, kilometrage from " +
                                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +" and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                        "  ) where date> "+"'"+tr_date_arrivee+"'"+" order by date asc",null);
                                                cursorAfter.moveToFirst();
                                                String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                                                if (tr_kilometrage_arrivee < Double.parseDouble(kmBefore)) {
                                                    dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else if(tr_kilometrage_arrivee > Double.parseDouble(kmAfter)){
                                                    dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else{
                                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                                    dialogBox_save();
                                                }
                                            }
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
                                }else{ // aucun probleme pour depart
                                    // on vérifie arrivee
                                            if(tr_date_arrivee.compareTo(lastDate) > 0){
                                                cursor.moveToLast();
                                                String km2 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                                if (Double.parseDouble(km2) > tr_kilometrage_arrivee) {
                                                    dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else{
                                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                                    dialogBox_save();
                                                }
                                            }else if (tr_date_arrivee.compareTo(firstDate) > 0 && tr_date_arrivee.compareTo(lastDate) < 0){
                                                Cursor cursorBefore2= readableDb.rawQuery(" select date, kilometrage from " +
                                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                        "  ) where date< "+"'"+tr_date_arrivee+"'"+" order by date asc",null);
                                                cursorBefore2.moveToLast();
                                                String kmBefore2 = String.valueOf(cursorBefore2.getDouble(cursorBefore2.getColumnIndex("kilometrage")));

                                                Cursor cursorAfter2= readableDb.rawQuery(" select date, kilometrage from " +
                                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                        "  ) where date> "+"'"+tr_date_arrivee+"'"+" order by date asc",null);
                                                cursorAfter2.moveToFirst();
                                                String kmAfter2 = String.valueOf(cursorAfter2.getDouble(cursorAfter2.getColumnIndex("kilometrage")));

                                                if (tr_kilometrage_arrivee < Double.parseDouble(kmBefore2)) {
                                                    dialogBox("Un enregistrement daté du " + cursorBefore2.getString(cursorBefore2.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else if(tr_kilometrage_arrivee > Double.parseDouble(kmAfter2)){
                                                    dialogBox("Un enregistrement daté du " + cursorAfter2.getString(cursorAfter2.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + tr_date_arrivee);
                                                }else{
                                                    db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                                                    dialogBox_save();
                                                }
                                            }
                                }
                            }
                        }else{
                            // premier trajet
                            db.insertTrajet(tr_usr_id,tr_motif,tr_date_depart,tr_kilometrage_depart,tr_lieu_depart,tr_date_arrivee,tr_kilometrage_arrivee,tr_lieu_arrivee,tr_note,tr_sync_id,tr_sync_date,1,db.getChosenAffectationId());
                            dialogBox_save();
                        }
                            }
                    }else {
                        dialogBox("Veuillez compléter les informations manquantes !");
                    }
                }
            });
        } catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

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
        alertDialogBuilder.setMessage("Trajet sauvegardé avec succès");

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

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
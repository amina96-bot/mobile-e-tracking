package com.starbrands.stb_carburant.ui.dashbord;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditKilometrageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditKilometrageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int usr_id;
    String selected_date= null;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    TextView calendar_text;
    EditText kilometrage_view;
    ImageView save_button;
    int idKilometrage=0;

    ImageView picture_text_view;
    String photoPath=null;
    String photoname=null;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public EditKilometrageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditKilometrageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditKilometrageFragment newInstance(String param1, String param2) {
        EditKilometrageFragment fragment = new EditKilometrageFragment();
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
        View view= inflater.inflate(R.layout.fragment_edit_kilometrage, container, false);
        DbHandler db = new DbHandler(getContext());

        try {
            usr_id = db.GetLastUser_id();
            calendar_text = (TextView) view.findViewById(R.id.adf_text_calendar);
            kilometrage_view = (EditText) view.findViewById(R.id.editText_kilometrage);
            save_button = (ImageView) view.findViewById(R.id.update_button);
            picture_text_view = (ImageView) view.findViewById(R.id.km_counter_picture_edit);
            Bundle args = getArguments();
            if (args != null) {
                idKilometrage = Integer.parseInt(args.getString("id"));
                SQLiteDatabase readableDb = db.getReadableDatabase();
                Cursor cursor = readableDb.rawQuery(" select * from  KILOMETRAGE where km_id=" + idKilometrage, null);
                if(cursor!=null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    selected_date = String.valueOf(cursor.getString(cursor.getColumnIndex("km_date")));
                    calendar_text.setText(selected_date);
                    kilometrage_view.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("km_kilometrage"))));
                    picture_text_view.setImageURI(Uri.parse(String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_path")))));

                    photoPath=String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_path")));
                    photoname=String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_name")));
                    /*-----------------------------------------------Calendrier -----------------------------------------------*/
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String hour = String.valueOf(selectedHour);
                            String minute = String.valueOf(selectedMinute);
                            if (hour.length() == 1)
                                hour = "0" + hour;
                            if (minute.length() == 1)
                                minute = "0" + minute;

                            if(selected_date.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                                calendar_text.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date()));
                                selected_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date());
                            }else{
                                calendar_text.setText(calendar_text.getText() + " " + hour + ":" + minute + ":00");
                                selected_date = selected_date + " " + hour + ":" + minute + ":00";
                            }


                        }
                    }, hour, minute, true);//Yes 24 hour time

                    final Calendar newCalendar = Calendar.getInstance();
                    final DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String month = String.valueOf(monthOfYear + 1);
                            if (month.length() == 1)
                                month = "0" + month;
                            String day = String.valueOf(dayOfMonth);
                            if (day.length() == 1)
                                day = "0" + day;
                            calendar_text.setText(String.valueOf(year) + "-" + month + "-" + day);
                            selected_date= String.valueOf(year) + "-" + month + "-" + day;
                            mTimePicker.show();
                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    calendar_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StartTime.getDatePicker().setMaxDate(new Date().getTime());
                            StartTime.show();

                        }
                    });

                    ImageView calendar = (ImageView) view.findViewById(R.id.adf_img_calendar);
                    calendar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StartTime.getDatePicker().setMaxDate(new Date().getTime());
                            StartTime.show();
                        }
                    });

                    /*-----------------------------------------------prise d'image-----------------------------------------------*/
                    picture_text_view = (ImageView) view.findViewById(R.id.km_counter_picture_edit);
                    picture_text_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dispatchTakePictureIntent( db.GetLastUser_id());
                        }
                    });

                    /*-----------------------------------------------boutton sauvegarder-----------------------------------------------*/
                    save_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // get user id
                            int km_usr_id = db.GetLastUser_id();

                            String km_date = selected_date;

                            double km_kilometrage = 0;
                            String kilometrage = kilometrage_view.getText().toString();
                            if (pattern.matcher(kilometrage).matches())
                                km_kilometrage = Double.parseDouble(kilometrage);

                            int km_sync_id = cursor.getInt(cursor.getColumnIndex("km_sync_id"));
                            String km_sync_date = cursor.getString(cursor.getColumnIndex("km_sync_date"));

                            String km_file_path = photoPath;
                            String km_file_name= photoname;

                            // save infos
                            if (km_kilometrage != 0 ) {
                                SQLiteDatabase readableDb = db.getReadableDatabase();

                                Cursor cursor= readableDb.rawQuery(" select date, kilometrage from " +
                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +  " and km_id!= "+ idKilometrage+
                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +  " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                        "  ) order by date",null);

                                if(cursor!=null && cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    String firstDate=String.valueOf(cursor.getString(cursor.getColumnIndex("date")));
                                    cursor.moveToLast();
                                    String lastDate =String.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                                    if(km_date.compareTo(lastDate) > 0){
                                        cursor.moveToLast();
                                        String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                        if (Double.parseDouble(km1) > km_kilometrage) {
                                            dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + km_date);
                                        }else{
                                            db.updateKilometrage(idKilometrage,km_usr_id,km_kilometrage,km_date,km_sync_id,km_sync_date, km_file_path,km_file_name);
                                            dialogBox_edit();
                                        }

                                    }else if(km_date.compareTo(firstDate) < 0){
                                        // avant la premiere date
                                        cursor.moveToFirst();
                                        String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                        if (Double.parseDouble(km1) < km_kilometrage) {
                                            dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + km_date);
                                        }else{
                                            db.updateKilometrage(idKilometrage,km_usr_id,km_kilometrage,km_date,km_sync_id,km_sync_date,km_file_path,km_file_name);
                                            dialogBox_edit();
                                        }
                                    }else{
                                        // au milieu des deux dates
                                        Cursor cursorBefore= readableDb.rawQuery(" select date, kilometrage from " +
                                                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() + " and km_id!= "+ idKilometrage+
                                                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                "  ) where date< "+"'"+km_date+"'"+" order by date asc",null);

                                        cursorBefore.moveToLast();
                                        String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                                        Cursor cursorAfter= readableDb.rawQuery(" select date, kilometrage from " +
                                                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() + " and km_id!= "+ idKilometrage+
                                                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                                "  ) where date> "+"'"+km_date+"'"+" order by date asc",null);

                                        cursorAfter.moveToFirst();
                                        String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                                        if (km_kilometrage < Double.parseDouble(kmBefore)) {
                                            dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + km_date);
                                        }else if(km_kilometrage > Double.parseDouble(kmAfter)){
                                            dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + km_date);
                                        }else{
                                            db.updateKilometrage(idKilometrage,km_usr_id,km_kilometrage,km_date,km_sync_id,km_sync_date,km_file_path,km_file_name);
                                            dialogBox_edit();
                                        }
                                    }
                                }else{
                                    // Premier kilométrage
                                    db.updateKilometrage(idKilometrage,km_usr_id,km_kilometrage,km_date,km_sync_id,km_sync_date,km_file_path,km_file_name);
                                    dialogBox_edit();
                                }
                            }else {
                                dialogBox("Veuillez compléter les informations manquantes !");
                            }
                        }
                    });
                }
            }

        }catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Modifier demande de chargement");
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

    private void dispatchTakePictureIntent(int usr_id) {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile(usr_id);
                } catch (IOException ex) {
                    dialogBox(ex.getMessage());
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this.getContext(),
                            getActivity().getPackageName() + ".fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                }
            }
        } catch (Exception e) {
            dialogBox(e.getMessage());
        }
    }

    private File createImageFile(int usr_id) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = usr_id+"_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        photoname=image.getName();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            picture_text_view.setImageBitmap(bitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
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
        alertDialogBuilder.setMessage("Demande de chargement modifiée avec succès");

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
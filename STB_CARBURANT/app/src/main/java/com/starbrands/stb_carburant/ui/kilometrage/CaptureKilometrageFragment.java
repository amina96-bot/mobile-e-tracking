package com.starbrands.stb_carburant.ui.kilometrage;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
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
 * Use the {@link CaptureKilometrageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaptureKilometrageFragment extends Fragment implements LocationListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int usr_id;
    String Selected_date=null;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    TextView calendar_text ;
    EditText distanceview ;
    LocationManager locationManager;
    double latitude;
    double longitude;

    ImageView imageView;
    String currentPhotoPath=null;
    String currentPhotoname=null;

    public CaptureKilometrageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaptureKilometrageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaptureKilometrageFragment newInstance(String param1, String param2) {
        CaptureKilometrageFragment fragment = new CaptureKilometrageFragment();
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
        View view = inflater.inflate(R.layout.fragment_capture_kilometrage, container, false);
        DbHandler db = new DbHandler(getContext());
        try {
            usr_id=db.GetLastUser_id();
            calendar_text = (TextView) view.findViewById(R.id.adf_text_calendar2);
            distanceview = (EditText) view.findViewById(R.id.editText_Distance2);

            latitude=0;
            longitude=0;

            /*-----------------------------------------------Calendrier-----------------------------------------------*/
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String hour=String.valueOf(selectedHour);
                    String minute=String.valueOf(selectedMinute);
                    if(hour.length()==1)
                        hour="0"+hour;
                    if(minute.length()==1)
                        minute="0"+minute;

                    if(Selected_date.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                        calendar_text.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date()));
                        Selected_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date());
                    }else{
                        calendar_text.setText(calendar_text.getText()+" "+hour+":"+minute+":00");
                        Selected_date=Selected_date+" "+hour+":"+minute+":00";
                    }


                }
            }, hour, minute, true);//Yes 24 hour time

            final Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month=String.valueOf(monthOfYear+ 1);
                    if(month.length()==1)
                        month="0"+month;
                    String day=String.valueOf(dayOfMonth);
                    if(day.length()==1)
                        day="0"+day;
                    calendar_text.setText(String.valueOf(year) + "-" +month + "-" + day);
                    Selected_date=String.valueOf(year) + "-" + month + "-" + day;
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

            ImageView calendar = (ImageView) view.findViewById(R.id.adf_img_calendar2);
            calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartTime.getDatePicker().setMaxDate(new Date().getTime());
                    StartTime.show();
                }
            });

            /*-----------------------------------------------prise d'image-----------------------------------------------*/
            imageView = (ImageView) view.findViewById(R.id.km_counter_picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakePictureIntent( db.GetLastUser_id());
                }
            });
            /*-----------------------------------------------Coordonné GPS-----------------------------------------------*/
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
            }
            /*-----------------------------------------------boutton sauvgarder-----------------------------------------------*/
            ImageView button = (ImageView) view.findViewById(R.id.save_button2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get user id
                    int km_usr_id = db.GetLastUser_id();

                    // get selected date or replace by sysdate
                    String km_date = "";
                    if (Selected_date== null)
                    {
                        Selected_date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        km_date = Selected_date;
                    }
                    else
                        km_date = Selected_date;

                    // get distance
                    double km_distance = 0;
                    String distance_tmp = distanceview.getText().toString();
                    if (pattern.matcher(distance_tmp).matches())
                        km_distance = Double.parseDouble(distance_tmp);

                    // get latitude longitude
                    getLocation();
                    double km_latitude = latitude;
                    double km_longitude =longitude;

                    int km_sync_id = 0;
                    String km_sync_date = "";

                    // get file path
                    String km_file_path = currentPhotoPath;
                    String km_file_name= currentPhotoname;

                    // save infos
                    if(km_distance !=0 && km_file_path!=null && km_file_name!=null) {
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

                            if(km_date.compareTo(lastDate) > 0){
                                // apres la derniere date
                                cursor.moveToLast();
                                String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                if (Double.parseDouble(km1) > km_distance) {
                                    dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + km_date);
                                }else{
                                    db.insertKilometrage(km_usr_id,km_date,km_distance,km_latitude,km_longitude,km_sync_id, km_sync_date,km_file_path,km_file_name,"-1","-1","-1",db.getChosenAffectationId());
                                    dialogBox_save();
                                }

                            }else if(km_date.compareTo(firstDate) < 0){
                                // avant la premiere date
                                cursor.moveToFirst();
                                String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                                if (Double.parseDouble(km1) < km_distance) {
                                    dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + km_date);
                                }else{
                                    db.insertKilometrage(km_usr_id,km_date,km_distance,km_latitude,km_longitude,km_sync_id,km_sync_date,km_file_path,km_file_name,"-1","-1","-1",db.getChosenAffectationId());
                                    dialogBox_save();
                                }
                            }else if ( km_date.compareTo(firstDate) > 0  &&  km_date.compareTo(lastDate) < 0){
                                // au milieu des deux dates
                               Cursor cursorBefore= readableDb.rawQuery(" select date, kilometrage from " +
                                       "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                       " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                       " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                       " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                       "  ) where date< "+"'"+km_date+"'"+" order by date asc",null);

                                cursorBefore.moveToLast();
                                String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                                Cursor cursorAfter= readableDb.rawQuery(" select date, kilometrage from " +
                                        "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                        " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                        " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                        "  ) where date> "+"'"+km_date+"'"+" order by date asc",null);

                                cursorAfter.moveToFirst();
                                String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                                if (km_distance < Double.parseDouble(kmBefore)) {
                                    dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + km_date);
                                }else if(km_distance > Double.parseDouble(kmAfter)){
                                    dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + km_date);
                                }else{
                                    db.insertKilometrage(km_usr_id,km_date,km_distance,km_latitude,km_longitude,km_sync_id,km_sync_date,km_file_path,km_file_name,"-1","-1","-1",db.getChosenAffectationId());
                                    dialogBox_save();
                                }
                            }
                        }else{
                            // Premier kilométrage
                            db.insertKilometrage(km_usr_id,km_date,km_distance,km_latitude,km_longitude,km_sync_id,km_sync_date,km_file_path,km_file_name, "-1","-1","-1",db.getChosenAffectationId());
                            dialogBox_save();
                        }
                    }else{
                        dialogBox("Veuillez compléter les informations manquantes !");
                    }
                }
            });
        } catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Demande de chargement");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//                navController.navigate(R.id.nav_dashbaord);
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

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (android.location.LocationListener) this);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude =lat;
                longitude = longi;
            } else {
                Toast.makeText(getActivity(), "Localisation imposible", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dialogBox_save() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("Les informations saisies ont été sauvegardées");

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
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        dialogBox("Signal GPS perdu");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
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
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoname=image.getName();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
    }
}

package com.starbrands.stb_carburant.ui.fuel;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFuelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFuelFragment extends Fragment  implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_LOCATION = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    LocationManager locationManager;
    double latitude;
    double longitude;
    ImageView imageView;
    ImageView counterImageView;

    ImageView imageView0;

    int usr_id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String Selected_date=null;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    TextView calendar_text ;
    EditText distanceview ;
    EditText costview ;
    EditText commentview ;

    String photoPath=null;
    String photoname=null;


    String currentPhotoPath=null;
    String currentPhotoname=null;

    String currentCounterPhotoPath=null;
    String currentCounterPhotoname=null;


    public AddFuelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFuelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFuelFragment newInstance(String param1, String param2) {
        AddFuelFragment fragment = new AddFuelFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_fuel, container, false);
        //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        DbHandler db = new DbHandler(getContext());
        try {
        usr_id=db.GetLastUser_id();

         calendar_text = (TextView) view.findViewById(R.id.adf_text_calendar);
         distanceview = (EditText) view.findViewById(R.id.editText_Distance);
         costview = (EditText) view.findViewById(R.id.editTextAmount);
         commentview = (EditText) view.findViewById(R.id.editTextComment);
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

        ImageView calendar = (ImageView) view.findViewById(R.id.adf_img_calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime.getDatePicker().setMaxDate(new Date().getTime());
                StartTime.show();
            }
        });


        /*-----------------------------------------------prix carburant-----------------------------------------------*/
        double price = db.GetFuelCost(db.GetLastUser_id());
        TextView price_view = (TextView) view.findViewById(R.id.textView17);
        price_view.setText(String.valueOf(price));


        /*-----------------------------------------------Type de payment-----------------------------------------------*/
        ArrayList<HashMap<String, String>> ptlist = db.GetPAYMENT_TYPE();
        String[] ptlist_content = new String[ptlist.size()];
        for (int i = 0; i < ptlist.size(); i++) {
            ptlist_content[i] = ptlist.get(i).get("pt_description");
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.pt_spinner);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ptlist_content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(adapter);
        /*-----------------------------------------------Coordonné GPS-----------------------------------------------*/
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        /*-----------------------------------------------prise d'image-----------------------------------------------*/
        imageView = (ImageView) view.findViewById(R.id.picture_text_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent( db.GetLastUser_id());
            }
        });

        counterImageView = (ImageView) view.findViewById(R.id.km_counter_picture_text_view);
        counterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeCounterPictureIntent( db.GetLastUser_id());
            }
        });
        /*-----------------------------------------------boutton sauvgarder-----------------------------------------------*/
        ImageView button = (ImageView) view.findViewById(R.id.save_button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    dialogBox(Build.MANUFACTURER+"|"+Build.MODEL);
                //TextView dateview = (TextView) view.findViewById(R.id.adf_text_calendar);

                // get user id
                int pa_usr_id = db.GetLastUser_id();

                // get selected date or replace by sysdate
                String pa_date = "";
                if (Selected_date== null)
                {
                    Selected_date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    pa_date = Selected_date;
                }
                else
                        pa_date = Selected_date;

                // get distance
                double pa_distance = 0;
                String distance_tmp = distanceview.getText().toString();
                if (pattern.matcher(distance_tmp).matches())
                    pa_distance = Double.parseDouble(distance_tmp);

                // get price
                double pa_unit_price = price;

                // get payment amount
                double pa_cost = 0;
                String cost_tmp = costview.getText().toString();
                if (pattern.matcher(cost_tmp).matches())
                    pa_cost = Double.parseDouble(cost_tmp);

                // get latitude longitude
                getLocation();
                double pa_latitude = latitude;
                double pa_longitude =longitude;

                // get payment type id
                int pa_pt_id = db.GetAPAYMENT_TYPE_id(spinner.getSelectedItem().toString());

                int pa_sync_id = 0;
                String pa_sync_date = "";

                String pa_comment = String.valueOf(commentview.getText().toString());

                // get file path
                String pa_file_path = currentPhotoPath;
                String pa_file_name= currentPhotoname;

                // get km counter file path
                String pa_km_counter_file_path = currentCounterPhotoPath;
                String pa_km_counter_file_name= currentCounterPhotoname;

                if( pa_distance !=0 && pa_cost !=0 && pa_file_path!=null && pa_file_name!=null && pa_km_counter_file_path!=null  && pa_km_counter_file_name!=null)
                {
                    SQLiteDatabase readableDb = db.getReadableDatabase();
                    Cursor cursor= readableDb.rawQuery(" select date, kilometrage from " +
                            "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                            " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                            " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                            " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                            "  ) order by date",null);

                    if(cursor!=null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String firstDate=String.valueOf(cursor.getString(cursor.getColumnIndex("date")));
                        cursor.moveToLast();
                        String lastDate =String.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                        if(pa_date.compareTo(lastDate) > 0){
                            // apres la derniere date
                            cursor.moveToLast();
                            String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                            if (Double.parseDouble(km1) > pa_distance) {
                                dialogBox("Un enregistrement daté du " + lastDate + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + pa_date);
                            }else{
                                db.insertPayment(pa_usr_id,
                                        pa_date,
                                        pa_distance,
                                        pa_unit_price,
                                        pa_cost,
                                        pa_latitude,
                                        pa_longitude,
                                        pa_pt_id,
                                        pa_sync_id,
                                        pa_sync_date,
                                        pa_comment,
                                        pa_file_path,
                                        pa_file_name,
                                        pa_km_counter_file_path,
                                        pa_km_counter_file_name,
                                        db.getChosenAffectationId());
                                dialogBox_save();
                            }

                        }else if(pa_date.compareTo(firstDate) < 0){
                            // avant la premiere date
                            cursor.moveToFirst();
                            String km1 = String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage")));
                            if (Double.parseDouble(km1) < pa_distance) {
                                dialogBox("Un enregistrement daté du " + firstDate + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + pa_date);
                            }else{
                                db.insertPayment(pa_usr_id,
                                        pa_date,
                                        pa_distance,
                                        pa_unit_price,
                                        pa_cost,
                                        pa_latitude,
                                        pa_longitude,
                                        pa_pt_id,
                                        pa_sync_id,
                                        pa_sync_date,
                                        pa_comment,
                                        pa_file_path,
                                        pa_file_name,
                                        pa_km_counter_file_path,
                                        pa_km_counter_file_name,
                                        db.getChosenAffectationId());
                                dialogBox_save();
                            }
                        }else if ( pa_date.compareTo(firstDate) > 0  &&  pa_date.compareTo(lastDate) < 0){
                            // au milieu des deux dates
                            Cursor cursorBefore= readableDb.rawQuery(" select date, kilometrage from " +
                                    "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                    " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                    " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                    " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                    "  ) where date< "+"'"+pa_date+"'"+" order by date asc",null);
                            cursorBefore.moveToLast();
                            String kmBefore = String.valueOf(cursorBefore.getDouble(cursorBefore.getColumnIndex("kilometrage")));

                            Cursor cursorAfter= readableDb.rawQuery(" select date, kilometrage from " +
                                    "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + db.getChosenAffectationId() +
                                    " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + db.getChosenAffectationId()+
                                    " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() +
                                    " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + db.getChosenAffectationId() + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                                    "  ) where date> "+"'"+pa_date+"'"+" order by date asc",null);
                            cursorAfter.moveToFirst();
                            String kmAfter = String.valueOf(cursorAfter.getDouble(cursorAfter.getColumnIndex("kilometrage")));

                            if (pa_distance < Double.parseDouble(kmBefore)) {
                                dialogBox("Un enregistrement daté du " + cursorBefore.getString(cursorBefore.getColumnIndex("date")) + " a une valeur de kilométrage supérieure à la valeur que vous essayer d'entrer le " + pa_date);
                            }else if(pa_distance > Double.parseDouble(kmAfter)){
                                dialogBox("Un enregistrement daté du " + cursorAfter.getString(cursorAfter.getColumnIndex("date")) + " a une valeur de kilométrage inférieure à la valeur que vous essayer d'entrer le " + pa_date);
                            }else {
                                db.insertPayment(pa_usr_id,
                                        pa_date,
                                        pa_distance,
                                        pa_unit_price,
                                        pa_cost,
                                        pa_latitude,
                                        pa_longitude,
                                        pa_pt_id,
                                        pa_sync_id,
                                        pa_sync_date,
                                        pa_comment,
                                        pa_file_path,
                                        pa_file_name,
                                        pa_km_counter_file_path,
                                        pa_km_counter_file_name,
                                        db.getChosenAffectationId());

                                dialogBox_save();
                            }
                        }
                    }else{
                        //premier paiment
                        db.insertPayment(pa_usr_id,
                                pa_date,
                                pa_distance,
                                pa_unit_price,
                                pa_cost,
                                pa_latitude,
                                pa_longitude,
                                pa_pt_id,
                                pa_sync_id,
                                pa_sync_date,
                                pa_comment,
                                pa_file_path,
                                pa_file_name,
                                pa_km_counter_file_path,
                                pa_km_counter_file_name,
                                db.getChosenAffectationId());

                        dialogBox_save();
                    }
                }else
                {
                    dialogBox("Veuillez compléter les informations manquantes !");
                }


            }
        });
        } catch (Exception e) {
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Ajouter un plein carburant");
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
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoname=image.getName();

        photoPath=currentPhotoPath;
        photoname=currentPhotoname;
        imageView0=imageView;

        return image;
    }

    private void dispatchTakeCounterPictureIntent(int usr_id) {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createCounterImageFile(usr_id);
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

    private File createCounterImageFile(int usr_id) throws IOException {
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
        currentCounterPhotoPath = image.getAbsolutePath();
        currentCounterPhotoname=image.getName();

        photoPath=currentCounterPhotoPath;
        photoname=currentCounterPhotoname;
        imageView0=counterImageView;

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            imageView0.setImageBitmap(bitmap);
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
//        if (getActivity().checkSelfPermission(
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
//        {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
//        } else {


    /*    if(ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else{*/


        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                //dialogBox("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(getActivity(), "Localisation imposible", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }


    public void dialogBox_save() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("Sauvgarde avec success");

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
}
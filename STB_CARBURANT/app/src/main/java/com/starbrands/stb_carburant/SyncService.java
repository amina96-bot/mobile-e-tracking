package com.starbrands.stb_carburant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SyncService extends Service {

    String export;
    Handler handler;
    DbHandler db=new DbHandler(this);
    Request request;
    OkHttpClient client=new OkHttpClient();
    Response response;
    JSONArray array;
    private static final String IMGUR_CLIENT_ID = "1";
    String uri = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

//        if (intent.getAction().equals(Constants.ACTION_START_LOCATION_SERVICE)) {
            startForegroundService();
//        }else if (intent.getAction().equals( Constants.ACTION_STOP_LOCATION_SERVICE)) {
//            stopForeground(true);
//            stopSelf();
//        }
        return START_STICKY;
    }


    public void startForegroundService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startMyOwnForeground();
        }else{
            startForeground(3, new Notification());
        }
    }

    @Override
    public void onCreate() {
        handler=new Handler();
        uri=db.getAddress();
        Runnable run=new Runnable() {
            @Override
            public void run() {

                AsyncTask asyncTask= new AsyncTask() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        if(isInternetAvailable()) {
                            System.out.println("--------------------------------- Synchronisation automatique -------------------------------");
                            export = "";
                            try {

                                System.out.println("--------------------- Chargement des Departements---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Departement").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.DepartementExists(object.getInt("dep_id")))
                                        db.UpdateDepartement(object.getInt("dep_id"), object.getString("dep_description"));
                                    else
                                        db.insertDepartement(object.getInt("dep_id"), object.getString("dep_description"));
                                }

                                System.out.println("---------------------Chargement des utilisateurs---------------------");
                                client = new OkHttpClient();
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Users").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.UserExists(object.getInt("usr_id"))) {
                                        db.updateUser(object.getInt("usr_id"),
                                                object.getString("usr_name"),
                                                object.getString("usr_first_name"),
                                                object.getInt("usr_dep_id"),
                                                object.getString("usr_login"),
                                                object.getString("usr_password"),
                                                object.getInt("usr_active")
                                        );
                                    } else {
                                        db.insertUser(object.getInt("usr_id"),
                                                object.getString("usr_name"),
                                                object.getString("usr_first_name"),
                                                object.getInt("usr_dep_id"),
                                                object.getString("usr_login"),
                                                object.getString("usr_password"),
                                                0,
                                                object.getInt("usr_active")
                                        );
                                    }
                                }

                                System.out.println("---------------------Chargement des marques---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Brand").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.brandsExists(object.getInt("br_id")))
                                        db.UpdateBrand(object.getInt("br_id"), object.getString("br_description"));
                                    else
                                        db.insertBrand(object.getInt("br_id"), object.getString("br_description"));
                                }

                                System.out.println("---------------------Chargement des modeles---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Model").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.modelExists(object.getInt("md_id")))
                                        db.Updatemodel(object.getInt("md_id"), object.getString("md_description"), object.getInt("md_br_id"));
                                    else
                                        db.insertModel(object.getInt("md_id"), object.getString("md_description"), object.getInt("md_br_id"));
                                }

                                System.out.println("---------------------Chargement des carburants---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Fuel").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.FUELExists(object.getInt("fl_id")))
                                        db.UpdateFUEL(object.getInt("fl_id"), object.getString("fl_description"));
                                    else
                                        db.insertFUEL(object.getInt("fl_id"), object.getString("fl_description"));
                                }

                                System.out.println("---------------------Chargement des prix---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Cost").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.COSTExists(object.getInt("ct_id")))
                                        db.UpdateCOST(object.getInt("ct_id"),
                                                object.getInt("ct_fl_id"),
                                                object.getString("ct_date"),
                                                object.getDouble("ct_price"),
                                                object.getInt("ct_active"));
                                    else
                                        db.insertCOST(object.getInt("ct_id"),
                                                object.getInt("ct_fl_id"),
                                                object.getString("ct_date"),
                                                object.getDouble("ct_price"),
                                                object.getInt("ct_active"));
                                }

                                System.out.println("---------------------Chargement des payment type---------------------");
                                request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_PaymenetType").build();
                                response = client.newCall(request).execute();
                                array = new JSONArray(response.body().string());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (db.TypePaymentExists(object.getInt("pt_id")))
                                        db.UpdateTypePayment(object.getInt("pt_id"),
                                                object.getString("pt_description"));
                                    else
                                        db.insertTypePayment(object.getInt("pt_id"),
                                                object.getString("pt_description"));
                                }

                                if (!db.GetLastUser().equals("GUEST")) {

                                    // envoi des paiements
                                    System.out.println("---------------------envoi des paiements---------------------");
                                    ArrayList<HashMap<String, String>> Paylist = db.GetPayment(db.GetLastUser_id());
                                    int size = Paylist.size();
                                    for (int i = 0; i < size; i++) {
                                        client = new OkHttpClient();
                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("pa_id", Paylist.get(i).get("pa_id"))
                                                .addFormDataPart("pa_usr_id", Paylist.get(i).get("pa_usr_id"))
                                                .addFormDataPart("pa_date", Paylist.get(i).get("pa_date"))
                                                .addFormDataPart("pa_distance", Paylist.get(i).get("pa_distance"))
                                                .addFormDataPart("pa_unit_price", Paylist.get(i).get("pa_unit_price"))
                                                .addFormDataPart("pa_cost", Paylist.get(i).get("pa_cost"))
                                                .addFormDataPart("pa_latitude", Paylist.get(i).get("pa_latitude"))
                                                .addFormDataPart("pa_longitude", Paylist.get(i).get("pa_longitude"))
                                                .addFormDataPart("pa_pt_id", Paylist.get(i).get("pa_pt_id"))
                                                .addFormDataPart("pa_sync_id", Paylist.get(i).get("pa_sync_id"))
                                                .addFormDataPart("pa_sync_date", Paylist.get(i).get("pa_sync_date"))
                                                .addFormDataPart("pa_comment", Paylist.get(i).get("pa_comment"))
                                                .addFormDataPart("pa_file_path", Paylist.get(i).get("pa_file_path"))
                                                .addFormDataPart("pa_file_name", Paylist.get(i).get("pa_file_name"))
                                                .addFormDataPart("pa_km_counter_file_path", Paylist.get(i).get("pa_km_counter_file_path"))
                                                .addFormDataPart("pa_km_counter_file_name", Paylist.get(i).get("pa_km_counter_file_name"))
                                                .addFormDataPart("pa_aff_id", Paylist.get(i).get("pa_aff_id"))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(Paylist.get(i).get("pa_file_path"))))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"km_counter_file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(Paylist.get(i).get("pa_km_counter_file_path"))))
                                                .build();

                                        request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            response = client.newCall(request).execute();
                                            export = response.body().string();
                                            JSONObject object = new JSONObject(export);
                                            db.UpdatePayment_flag(object.getInt("pa_id"), object.getInt("pa_sync_id"), object.getString("pa_sync_date"), object.getString("pa_file_name"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // envoi des trajets
                                    System.out.println("---------------------envoi des trajets---------------------");
                                    ArrayList<HashMap<String, String>> trajetlist = db.GetTrajet(db.GetLastUser_id());
                                    int size2 = trajetlist.size();
                                    for (int i = 0; i < size2; i++) {
                                        client = new OkHttpClient();
                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("tr_id", trajetlist.get(i).get("tr_id"))
                                                .addFormDataPart("tr_usr_id", trajetlist.get(i).get("tr_usr_id"))
                                                .addFormDataPart("tr_motif", trajetlist.get(i).get("tr_motif"))
                                                .addFormDataPart("tr_date_depart", trajetlist.get(i).get("tr_date_depart"))
                                                .addFormDataPart("tr_kilometrage_depart", trajetlist.get(i).get("tr_kilometrage_depart"))
                                                .addFormDataPart("tr_lieu_depart", trajetlist.get(i).get("tr_lieu_depart"))
                                                .addFormDataPart("tr_date_arrivee", trajetlist.get(i).get("tr_date_arrivee"))
                                                .addFormDataPart("tr_kilometrage_arrivee", trajetlist.get(i).get("tr_kilometrage_arrivee"))
                                                .addFormDataPart("tr_lieu_arrivee", trajetlist.get(i).get("tr_lieu_arrivee"))
                                                .addFormDataPart("tr_note", trajetlist.get(i).get("tr_note"))
                                                .addFormDataPart("tr_sync_id", trajetlist.get(i).get("tr_sync_id"))
                                                .addFormDataPart("tr_sync_date", trajetlist.get(i).get("tr_sync_date"))
                                                .addFormDataPart("tr_type", trajetlist.get(i).get("tr_type"))
                                                .addFormDataPart("tr_aff_id", trajetlist.get(i).get("tr_aff_id"))
                                                .build();
                                        request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadTrajetData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            response = client.newCall(request).execute();
                                            export = response.body().string();
                                            JSONObject object = new JSONObject(export);
                                            db.UpdateTrajet_flag(object.getInt("tr_id"), object.getInt("tr_sync_id"), object.getString("tr_sync_date"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // envoi des kilométrages
                                    System.out.println("---------------------envoi des demandes de chargement---------------------");
                                    ArrayList<HashMap<String, String>> kilométragelist = db.GetKilometrage(db.GetLastUser_id());
                                    int size3 = kilométragelist.size();
                                    for (int i = 0; i < size3; i++) {
                                        client = new OkHttpClient();
                                        @SuppressLint("StaticFieldLeak") RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("km_id", kilométragelist.get(i).get("km_id"))
                                                .addFormDataPart("km_usr_id", kilométragelist.get(i).get("km_usr_id"))
                                                .addFormDataPart("km_date", kilométragelist.get(i).get("km_date"))
                                                .addFormDataPart("km_kilometrage", kilométragelist.get(i).get("km_kilometrage"))
                                                .addFormDataPart("km_latitude", kilométragelist.get(i).get("km_latitude"))
                                                .addFormDataPart("km_longitude", kilométragelist.get(i).get("km_longitude"))
                                                .addFormDataPart("km_sync_id", kilométragelist.get(i).get("km_sync_id"))
                                                .addFormDataPart("km_sync_date", kilométragelist.get(i).get("km_sync_date"))
                                                .addFormDataPart("km_file_path", kilométragelist.get(i).get("km_file_path"))
                                                .addFormDataPart("km_file_name", kilométragelist.get(i).get("km_file_name"))
                                                .addFormDataPart("km_aff_id", kilométragelist.get(i).get("km_aff_id"))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(kilométragelist.get(i).get("km_file_path"))))
                                                .build();
                                        request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadKilometrageData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            response = client.newCall(request).execute();
                                            export = response.body().string();
                                            JSONObject object = new JSONObject(export);
                                            db.UpdateKilometrage_flag(object.getInt("km_id"), object.getInt("km_sync_id"), object.getString("km_sync_date"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    System.out.println("---------------------Chargement des vehicules---------------------");
                                    request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Vehicule/query?usr_id=" + db.GetLastUser_id()).build();
                                    response = client.newCall(request).execute();
                                    array = new JSONArray(response.body().string());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (db.VEHICLEExists(object.getInt("vh_id")))
                                            db.UpdateVEHICLE(object.getInt("vh_id"),
                                                    object.getInt("vh_md_id"),
                                                    object.getInt("vh_fl_id"),
                                                    object.getString("vh_code"),
                                                    object.getString("vh_immatriculaton"),
                                                    object.getString("vh_creation_date"),
                                                    object.getInt("vh_active"));
                                        else
                                            db.insertVEHICLE(object.getInt("vh_id"),
                                                    object.getInt("vh_md_id"),
                                                    object.getInt("vh_fl_id"),
                                                    object.getString("vh_code"),
                                                    object.getString("vh_immatriculaton"),
                                                    object.getString("vh_creation_date"),
                                                    object.getInt("vh_active"));
                                    }

                                    System.out.println("---------------------Chargement des affectations---------------------");
                                    request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Affectation/query?usr_id=" + db.GetLastUser_id()).build();
                                    response = client.newCall(request).execute();
                                    array = new JSONArray(response.body().string());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (db.AFFECTATIONExists(object.getInt("af_id"))) {
                                            db.UpdateAFFECTATION(object.getInt("af_id"),
                                                    object.getInt("af_usr_id"),
                                                    object.getInt("af_vh_id"),
                                                    object.getString("af_date"),
                                                    object.getInt("af_active"));
                                        }else{
                                            db.insertAFFECTATION(object.getInt("af_id"),
                                                    object.getInt("af_usr_id"),
                                                    object.getInt("af_vh_id"),
                                                    object.getString("af_date"),
                                                    object.getInt("af_active"));
                                        }
                                    }

                                    // part 10 PAYMENT
                                    System.out.println("---------------------Chargement des payments---------------------");
                                    request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/GetPayment/query?usr_id=" + db.GetLastUser_id()).build();
                                    response = client.newCall(request).execute();
                                    array = new JSONArray(response.body().string());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (db.PAYMENTxists(object.getInt("pa_id"), object.getString("pa_file_name")))
                                            db.upadtePaymentFromServer(
                                                    object.getInt("pa_id")
                                                    , object.getInt("pa_usr_id")
                                                    , object.getString("pa_date")
                                                    , object.getDouble("pa_distance")
                                                    , object.getDouble("pa_unit_price")
                                                    , object.getDouble("pa_cost")
                                                    , object.getDouble("pa_latitude")
                                                    , object.getDouble("pa_longitude")
                                                    , object.getInt("pa_pt_id")
                                                    , object.getString("pa_sync_date")
                                                    , object.getString("pa_comment")
                                                    , object.getString("pa_file_path")
                                                    , object.getString("pa_file_name")
                                                    , object.getString("pa_km_counter_file_path")
                                                    , object.getString("pa_km_counter_file_name")
                                                    , object.getInt("pa_aff_id")
                                            );

                                        else
                                            db.insertPayment(
                                                    object.getInt("pa_usr_id")
                                                    , object.getString("pa_date")
                                                    , object.getDouble("pa_distance")
                                                    , object.getDouble("pa_unit_price")
                                                    , object.getDouble("pa_cost")
                                                    , object.getDouble("pa_latitude")
                                                    , object.getDouble("pa_longitude")
                                                    , object.getInt("pa_pt_id")
                                                    , object.getInt("pa_id")
                                                    , object.getString("pa_sync_date")
                                                    , object.getString("pa_comment")
                                                    , object.getString("pa_file_path")
                                                    , object.getString("pa_file_name")
                                                    , object.getString("pa_km_counter_file_path")
                                                    , object.getString("pa_km_counter_file_name")
                                                    ,object.getInt("pa_aff_id")
                                            );
                                    }

                                    System.out.println("---------------------Chargement des trajets---------------------");
                                    request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Trajet/query?usr_id=" + db.GetLastUser_id()).build();
                                    response = client.newCall(request).execute();
                                    array = new JSONArray(response.body().string());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (db.TRAJETExists(object.getInt("tr_id")))
                                            db.updateTrajetFromServer(
                                                    object.getInt("tr_usr_id")
                                                    , object.getString("tr_motif")
                                                    , object.getString("tr_date_depart")
                                                    , object.getDouble("tr_kilometrage_depart")
                                                    , object.getString("tr_lieu_depart")
                                                    , object.getString("tr_date_arrivee")
                                                    , object.getDouble("tr_kilometrage_arrivee")
                                                    , object.getString("tr_lieu_arrivee")
                                                    , object.getString("tr_note")
                                                    , object.getInt("tr_id")
                                                    , object.getString("tr_sync_date")
                                                    , object.getInt("tr_type")
                                                    ,object.getInt("tr_aff_id")
                                            );
                                        else
                                            db.insertTrajet(object.getInt("tr_usr_id")
                                                    , object.getString("tr_motif")
                                                    , object.getString("tr_date_depart")
                                                    , object.getDouble("tr_kilometrage_depart")
                                                    , object.getString("tr_lieu_depart")
                                                    , object.getString("tr_date_arrivee")
                                                    , object.getDouble("tr_kilometrage_arrivee")
                                                    , object.getString("tr_lieu_arrivee")
                                                    , object.getString("tr_note")
                                                    , object.getInt("tr_id")
                                                    ,object.getString("tr_sync_date")
                                                    ,object.getInt("tr_type")
                                                    ,object.getInt("tr_aff_id")
                                            );
                                    }

                                    System.out.println("---------------------Chargement des demandes de chargement---------------------");
                                    request = new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Kilometrage/query?usr_id=" + db.GetLastUser_id()).build();
                                    response = client.newCall(request).execute();
                                    array = new JSONArray(response.body().string());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (db.KILOMETRAGExists(object.getInt("km_id"))){

                                            db.updateKilometrageFromServer(
                                                    object.getInt("km_usr_id")
                                                    , object.getDouble("km_kilometrage")
                                                    , object.getString("km_date")
                                                    , object.getInt("km_id")
                                                    , object.getString("km_sync_date")
                                                    , object.getString("km_file_path")
                                                    , object.getString("km_file_name")
                                                    ,object.getString("accepted")
                                                    , object.getString("montant_recharge")
                                                    , object.getString("motif_refus")
                                                    ,object.getInt("km_aff_id")
                                            );
                                        }else
                                            db.insertKilometrage(object.getInt("km_usr_id")
                                                    , object.getString("km_date")
                                                    , object.getDouble("km_kilometrage")
                                                    , object.getDouble("km_latitude")
                                                    , object.getDouble("km_longitude")
                                                    , object.getInt("km_id")
                                                    ,object.getString("km_sync_date")
                                                    , object.getString("km_file_path")
                                                    , object.getString("km_file_name")
                                                    ,object.getString("accepted")
                                                    , object.getString("montant_recharge")
                                                    , object.getString("motif_refus")
                                                    ,object.getInt("km_aff_id")
                                            );
                                    }

                                    // envoi des coordonnées gps
                                    System.out.println("---------------------envoi des coordonnées gps---------------------");
                                    ArrayList<HashMap<String, String>> gpslist = db.GetGps(db.GetLastUser_id());
                                    System.out.println(gpslist.size());
                                    while (gpslist.size() > 0) {
                                        JSONArray gpsSubArray = new JSONArray();
                                        List<HashMap<String, String>> gpsSublist = new ArrayList<HashMap<String, String>>();
                                        if (gpslist.size() > 999) {
                                            gpsSublist = new ArrayList<HashMap<String, String>>(gpslist.subList(0, 999));
                                            gpslist = new ArrayList<HashMap<String, String>>(gpslist.subList(999, gpslist.size() - 1));
                                        } else {
                                            gpsSublist = gpslist;
                                            gpslist = new ArrayList<HashMap<String, String>>();
                                        }
                                        for (int j = 0; j < gpsSublist.size(); j++) {
                                            JSONObject gpsObject = new JSONObject();
                                            gpsObject.put("gps_id", gpsSublist.get(j).get("gps_id"));
                                            gpsObject.put("gps_usr_id", gpsSublist.get(j).get("gps_usr_id"));
                                            gpsObject.put("gps_latitude", gpsSublist.get(j).get("gps_latitude"));
                                            gpsObject.put("gps_longitude", gpsSublist.get(j).get("gps_longitude"));
                                            gpsObject.put("gps_date", gpsSublist.get(j).get("gps_date"));
                                            gpsObject.put("gps_aff_id", gpsSublist.get(j).get("gps_aff_id"));
                                            gpsSubArray.put(gpsObject);
                                        }

                                        client = new OkHttpClient();
                                        String pattern = "yyyyMMddHHmmss.SS";
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                        String date = simpleDateFormat.format(new Date());

                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("gps", gpsSubArray.toString())
                                                .addFormDataPart("userId", db.GetLastUser_id().toString() + date)
                                                .build();
                                        request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadGpsData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            response = client.newCall(request).execute();
                                            export = response.body().string();
                                            JSONArray synchronisationGpsArray = new JSONArray(export);
                                            System.out.println("export: " + export);
                                            for (int i = 0; i < synchronisationGpsArray.length(); i++) {
                                                JSONObject object = synchronisationGpsArray.getJSONObject(i);
                                                db.UpdateGps_flag(object.getInt("gps_id"), object.getInt("gps_sync_id"), object.getString("gps_sync_date"));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                if (!db.GetLastUser().equals("GUEST")) {
                                }
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                    }
                }.execute();

                System.out.println("synchronication every 15 minutes ");
                handler.postDelayed(this,900*1000);
            }
        };
        handler.post(run);
    }

    private void startMyOwnForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("App is running in background")
                    .setSmallIcon(R.drawable.icon3)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .build();

            startForeground(4, notification);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isInternetAvailable() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }else {
            connected = false;
        }
        return connected;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}

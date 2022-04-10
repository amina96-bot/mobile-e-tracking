package com.starbrands.stb_carburant.ui.parametre;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import com.starbrands.stb_carburant.BuildConfig;
import com.starbrands.stb_carburant.Constants;
import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.MainActivity;
import com.starbrands.stb_carburant.R;
import com.starbrands.stb_carburant.ScanService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ParametreFragment extends Fragment {

    String uri = "";
    private ParametreViewModel mViewModel;

    private static final String IMGUR_CLIENT_ID = "1";
    private ProgressDialog progress;
    String export;
    DbHandler db=new DbHandler(getContext());
    Button save_btn;
    ImageButton UpdateButton;
    EditText editTextAddress;
    public static ParametreFragment newInstance() {
        return new ParametreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.parametre_fragment, container, false);
        export="";
        //TextView tv= (TextView) view.findViewById(R.id.textView4);
        progress=new ProgressDialog(getContext());
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setMax(100);
        progress.setCancelable(false);
        DbHandler db = new DbHandler(getContext());
        uri=db.getAddress();
        System.out.println(uri);
        ImageButton DownloadButton=(ImageButton) view.findViewById(R.id.DownloadButton);
        ImageButton UploadButton=(ImageButton) view.findViewById(R.id.UploadButton);
        save_btn=(Button) view.findViewById((R.id.save_btn));
        UpdateButton=(ImageButton) view.findViewById(R.id.UpdateButton);
        editTextAddress= (EditText) view.findViewById(R.id.editTextAddress);
        editTextAddress.setText(db.getAddress());
        if(db.checkExistingActiveAffectation()){
            DownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isInternetAvailable()){
                        AsyncTask asyncTask= new AsyncTask() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.setMessage("Téléchargement en cours....");
                                        progress.setProgress(0);
                                        progress.show();
                                    }
                                });

                            }

                            @Override
                            protected Object doInBackground(Object[] objects) {
                                try {
                                    Thread.sleep(1000);

                                    String connected_user=db.GetLastUser();
                                    int count_table=0;
                                    if(connected_user.equals("GUEST"))
                                        count_table=7;
                                    else
                                        count_table=13;
                                    int current_progress=0;


                                    //part 1 Departements
                                    OkHttpClient client= new OkHttpClient();
                                    Request request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Departement").build();
                                    Response response=null;

                                    response=client.newCall(request).execute();
                                    JSONArray array= new JSONArray(response.body().string());

                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if( db.DepartementExists(object.getInt("dep_id")))
                                            db.UpdateDepartement(object.getInt("dep_id"),object.getString("dep_description"));
                                        else
                                            db.insertDepartement(object.getInt("dep_id"),object.getString("dep_description"));
                                    }

                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress = current_progress;

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement en cours: Départements");
                                            progress.setProgress(finalCurrent_progress);
                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 2 Utilisateurs
                                    client= new OkHttpClient();
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Users").build();
                                    response=null;

                                    response=client.newCall(request).execute();

                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.UserExists(object.getInt("usr_id"))){

                                            db.updateUser(object.getInt("usr_id"),
                                                    object.getString("usr_name"),
                                                    object.getString("usr_first_name"),
                                                    object.getInt("usr_dep_id"),
                                                    object.getString("usr_login"),
                                                    object.getString("usr_password"),
                                                    object.getInt("usr_active")
                                            );
                                        }
                                        else
                                        {
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
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress1 = current_progress;
                                    JSONArray finalArray = array;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement en cours: Utilisateurs");
                                            progress.setProgress(finalCurrent_progress1);

                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 3  BRAND
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Brand").build();
                                    response=client.newCall(request).execute();
                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.brandsExists(object.getInt("br_id")))
                                            db.UpdateBrand(object.getInt("br_id"),object.getString("br_description"));
                                        else
                                            db.insertBrand(object.getInt("br_id"),object.getString("br_description"));
                                    }
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress2 = current_progress;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement en cours: Marques");
                                            progress.setProgress(finalCurrent_progress2);
                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 4  MODELS
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Model").build();
                                    response=client.newCall(request).execute();
                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.modelExists(object.getInt("md_id")))
                                            db.Updatemodel(object.getInt("md_id"),object.getString("md_description"),object.getInt("md_br_id"));
                                        else
                                            db.insertModel(object.getInt("md_id"),object.getString("md_description"),object.getInt("md_br_id"));
                                    }
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress3 = current_progress;

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement en cours: Modèles");
                                            progress.setProgress(finalCurrent_progress3);
                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 5  FUEL
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Fuel").build();
                                    response=client.newCall(request).execute();
                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.FUELExists(object.getInt("fl_id")))
                                            db.UpdateFUEL(object.getInt("fl_id"),object.getString("fl_description"));
                                        else
                                            db.insertFUEL(object.getInt("fl_id"),object.getString("fl_description"));
                                    }
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress4 = current_progress;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement Cours: Carburants");
                                            progress.setProgress(finalCurrent_progress4);

                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 6  COST
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Cost").build();
                                    response=client.newCall(request).execute();
                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.COSTExists(object.getInt("ct_id")))
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
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress5 = current_progress;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement Cours: Prix Carburants");
                                            progress.setProgress(finalCurrent_progress5);

                                        }
                                    });
                                    Thread.sleep(500);

                                    // part 7  Payment_type
                                    request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_PaymenetType").build();
                                    response=client.newCall(request).execute();
                                    array= new JSONArray(response.body().string());
                                    for(int i=0;i<array.length();i++){
                                        JSONObject  object= array.getJSONObject(i);
                                        if(db.TypePaymentExists(object.getInt("pt_id")))
                                            db.UpdateTypePayment(object.getInt("pt_id"),
                                                    object.getString("pt_description"));
                                        else
                                            db.insertTypePayment(object.getInt("pt_id"),
                                                    object.getString("pt_description"));
                                    }
                                    current_progress=current_progress+(100/count_table);
                                    int finalCurrent_progress6 = current_progress;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setMessage("Téléchargement en cours: Type de paiement");
                                            progress.setProgress(finalCurrent_progress6);

                                        }
                                    });
                                    Thread.sleep(500);

                                    //si utilisateur différent de guest

                                    if(!connected_user.equals("GUEST"))
                                    {
                                        // part 8 vehicle
                                        request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Vehicule/query?usr_id="+db.GetLastUser_id()).build();
                                        response=client.newCall(request).execute();
                                        array= new JSONArray(response.body().string());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject  object= array.getJSONObject(i);
                                            if(db.VEHICLEExists(object.getInt("vh_id")))
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
                                        current_progress=current_progress+(100/count_table);
                                        int finalCurrent_progress7 = current_progress;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.setMessage("Téléchargement en cours: Véhicules");
                                                progress.setProgress(finalCurrent_progress7);

                                            }
                                        });
                                        Thread.sleep(500);

                                        // part 9 Affectation
                                        request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Affectation/query?usr_id="+db.GetLastUser_id()).build();
                                        response=client.newCall(request).execute();
                                        array= new JSONArray(response.body().string());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject  object= array.getJSONObject(i);
                                            if(db.AFFECTATIONExists(object.getInt("af_id")))
                                                db.UpdateAFFECTATION(object.getInt("af_id"),
                                                        object.getInt("af_usr_id"),
                                                        object.getInt("af_vh_id"),
                                                        object.getString("af_date"),
                                                        object.getInt("af_active")
                                                );
                                            else
                                                db.insertAFFECTATION(object.getInt("af_id"),
                                                        object.getInt("af_usr_id"),
                                                        object.getInt("af_vh_id"),
                                                        object.getString("af_date"),
                                                        object.getInt("af_active")
                                                );
                                        }
                                        current_progress=current_progress+(100/count_table);

                                        int finalCurrent_progress8 = current_progress;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.setMessage("Téléchargement en cours: Affectation");
                                                progress.setProgress(finalCurrent_progress8);

                                            }
                                        });
                                        Thread.sleep(500);

                                        // part 10 PAYMENT
                                        request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/GetPayment/query?usr_id="+db.GetLastUser_id()).build();
                                        response=client.newCall(request).execute();
                                        array= new JSONArray(response.body().string());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject  object= array.getJSONObject(i);
                                            if(db.PAYMENTxists(object.getInt("pa_id"),object.getString("pa_file_name")))
                                                db.upadtePaymentFromServer(
                                                        object.getInt("pa_id")
                                                        ,object.getInt("pa_usr_id")
                                                        ,object.getString("pa_date")
                                                        ,object.getDouble("pa_distance")
                                                        ,object.getDouble("pa_unit_price")
                                                        ,object.getDouble("pa_cost")
                                                        ,object.getDouble("pa_latitude")
                                                        ,object.getDouble("pa_longitude")
                                                        ,object.getInt("pa_pt_id")
                                                        ,object.getString("pa_sync_date")
                                                        ,object.getString("pa_comment")
                                                        ,object.getString("pa_file_path")
                                                        ,object.getString("pa_file_name")
                                                        ,object.getString("pa_km_counter_file_path")
                                                        ,object.getString("pa_km_counter_file_name")
                                                        , object.getInt("pa_aff_id")
                                                );

                                            else
                                                db.insertPayment(
                                                        object.getInt("pa_usr_id")
                                                        ,object.getString("pa_date")
                                                        ,object.getDouble("pa_distance")
                                                        ,object.getDouble("pa_unit_price")
                                                        ,object.getDouble("pa_cost")
                                                        ,object.getDouble("pa_latitude")
                                                        ,object.getDouble("pa_longitude")
                                                        ,object.getInt("pa_pt_id")
                                                        ,object.getInt("pa_id")
                                                        ,object.getString("pa_sync_date")
                                                        ,object.getString("pa_comment")
                                                        ,object.getString("pa_file_path")
                                                        ,object.getString("pa_file_name")
                                                        ,object.getString("pa_km_counter_file_path")
                                                        ,object.getString("pa_km_counter_file_name")
                                                        , object.getInt("pa_aff_id")
                                                );
                                        }
                                        current_progress=current_progress+(100/count_table);
                                        int finalCurrent_progress9 = current_progress;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.setMessage("Téléchargement en cours: Paiments");
                                                progress.setProgress(finalCurrent_progress9);

                                            }
                                        });
                                        Thread.sleep(500);

                                        // part 11 TRAJET
                                        request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Trajet/query?usr_id="+db.GetLastUser_id()).build();
                                        response=client.newCall(request).execute();
                                        array= new JSONArray(response.body().string());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject  object= array.getJSONObject(i);
                                            if(db.TRAJETExists(object.getInt("tr_id")))
                                                db.updateTrajetFromServer(
                                                        object.getInt("tr_usr_id")
                                                        ,object.getString("tr_motif")
                                                        ,object.getString("tr_date_depart")
                                                        ,object.getDouble("tr_kilometrage_depart")
                                                        ,object.getString("tr_lieu_depart")
                                                        ,object.getString("tr_date_arrivee")
                                                        ,object.getDouble("tr_kilometrage_arrivee")
                                                        ,object.getString("tr_lieu_arrivee")
                                                        ,object.getString("tr_note")

                                                        ,object.getInt("tr_id")
                                                        ,object.getString("tr_sync_date")
                                                        ,object.getInt("tr_type")
                                                        ,object.getInt("tr_aff_id")
                                                );
                                            else
                                                db.insertTrajet(object.getInt("tr_usr_id")
                                                        ,object.getString("tr_motif")
                                                        ,object.getString("tr_date_depart")
                                                        ,object.getDouble("tr_kilometrage_depart")
                                                        ,object.getString("tr_lieu_depart")
                                                        ,object.getString("tr_date_arrivee")
                                                        ,object.getDouble("tr_kilometrage_arrivee")
                                                        ,object.getString("tr_lieu_arrivee")
                                                        ,object.getString("tr_note")
                                                        ,object.getInt("tr_id") ,
                                                        object.getString("tr_sync_date"),
                                                        object.getInt("tr_type")
                                                        ,object.getInt("tr_aff_id")
                                                );
                                        }
                                        current_progress=current_progress+(100/count_table);
                                        int finalCurrent_progress10 = current_progress;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.setMessage("Téléchargement en cours: Trajets");
                                                progress.setProgress(finalCurrent_progress10);

                                            }
                                        });
                                        Thread.sleep(500);

                                        // part 12 KILOMETRAGE
                                        request= new Request.Builder().url("http://"+uri+"/STB_CARBURANT/webresources/Get_Kilometrage/query?usr_id="+db.GetLastUser_id()).build();
                                        response=client.newCall(request).execute();
                                        array= new JSONArray(response.body().string());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject  object= array.getJSONObject(i);

                                            if(db.KILOMETRAGExists(object.getInt("km_id")))
                                                db.updateKilometrageFromServer(
                                                        object.getInt("km_usr_id")
                                                        ,object.getDouble("km_kilometrage")
                                                        ,object.getString("km_date")
                                                        ,object.getInt("km_id")
                                                        ,object.getString("km_sync_date")
                                                        , object.getString("km_file_path")
                                                        , object.getString("km_file_name")
                                                        ,object.getString("accepted")
                                                        , object.getString("montant_recharge")
                                                        , object.getString("motif_refus")
                                                        ,object.getInt("km_aff_id")
                                                );
                                            else
                                                db.insertKilometrage(object.getInt("km_usr_id")
                                                        ,object.getString("km_date")
                                                        ,object.getDouble("km_kilometrage")
                                                        ,object.getDouble("km_latitude")
                                                        ,object.getDouble("km_longitude")

                                                        ,object.getInt("km_id")
                                                        ,object.getString("km_sync_date")
                                                        , object.getString("km_file_path")
                                                        , object.getString("km_file_name")
                                                        ,object.getString("accepted")
                                                        , object.getString("montant_recharge")
                                                        , object.getString("motif_refus")
                                                        ,object.getInt("km_aff_id")
                                                );
                                        }
                                        current_progress=current_progress+(100/count_table);
                                        int finalCurrent_progress11 = current_progress;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.setMessage("Téléchargement en cours: demandes de chargement");
                                                progress.setProgress(finalCurrent_progress11);

                                            }
                                        });
                                        Thread.sleep(500);                            }
                                    Thread.sleep(1000);

                                }catch (Exception e)
                                {e.printStackTrace();}

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        dialogBox("Téléchargement terminé");
                                    }
                                });
                            }
                        }.execute();
                    }else{
                        Toast.makeText(getContext(), "Vérifiez votre connexion internet svp", Toast.LENGTH_LONG).show();
                    }
                }

            });
            UploadButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if(isInternetAvailable()){
                        AsyncTask asyncTask=new AsyncTask() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progress.setIndeterminate(false);
                                progress.setProgress(0);
                                progress.setMessage("Envoi en cours...");
                                progress.show();
                            }

                            @Override
                            protected Object doInBackground(Object[] objects) {

                                try {
                                    // envoi des paiements
                                    System.out.println("envoi des paiements");
                                    ArrayList<HashMap<String,String>> Paylist=db.GetPayment(db.GetLastUser_id());

                                    int progress_inc=0;
                                    int size=Paylist.size();
                                    for (int i=0;i<size;i++){
                                        OkHttpClient client= new OkHttpClient();
                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("pa_id",Paylist.get(i).get("pa_id"))
                                                .addFormDataPart("pa_usr_id",Paylist.get(i).get("pa_usr_id"))
                                                .addFormDataPart("pa_date",Paylist.get(i).get("pa_date"))
                                                .addFormDataPart("pa_distance",Paylist.get(i).get("pa_distance"))
                                                .addFormDataPart("pa_unit_price",Paylist.get(i).get("pa_unit_price"))
                                                .addFormDataPart("pa_cost",Paylist.get(i).get("pa_cost"))
                                                .addFormDataPart("pa_latitude",Paylist.get(i).get("pa_latitude"))
                                                .addFormDataPart("pa_longitude",Paylist.get(i).get("pa_longitude"))
                                                .addFormDataPart("pa_pt_id",Paylist.get(i).get("pa_pt_id"))
                                                .addFormDataPart("pa_sync_id",Paylist.get(i).get("pa_sync_id"))
                                                .addFormDataPart("pa_sync_date",Paylist.get(i).get("pa_sync_date"))
                                                .addFormDataPart("pa_comment",Paylist.get(i).get("pa_comment"))
                                                .addFormDataPart("pa_file_path",Paylist.get(i).get("pa_file_path"))
                                                .addFormDataPart("pa_file_name",Paylist.get(i).get("pa_file_name"))
                                                .addFormDataPart("pa_km_counter_file_path",Paylist.get(i).get("pa_km_counter_file_path"))
                                                .addFormDataPart("pa_km_counter_file_name",Paylist.get(i).get("pa_km_counter_file_name"))
                                                .addFormDataPart("pa_aff_id", Paylist.get(i).get("pa_aff_id"))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(Paylist.get(i).get("pa_file_path"))))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"km_counter_file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(Paylist.get(i).get("pa_km_counter_file_path"))))
                                                .build();
                                        Request request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            export=response.body().string();

                                            JSONObject object=new JSONObject(export);

                                            db.UpdatePayment_flag(object.getInt("pa_id"),object.getInt("pa_sync_id"),object.getString("pa_sync_date"),object.getString("pa_file_name"));
                                            progress_inc=progress_inc+(100/size);
                                            progress.setProgress(progress_inc);
                                            Thread.sleep(1000);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // envoi des trajets
                                    System.out.println("envoi des trajets");
                                    ArrayList<HashMap<String,String>> trajetlist=db.GetTrajet(db.GetLastUser_id());
                                    int size2=trajetlist.size();
                                    for (int i=0;i<size2;i++){
                                        OkHttpClient client= new OkHttpClient();
                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("tr_id",trajetlist.get(i).get("tr_id"))
                                                .addFormDataPart("tr_usr_id",trajetlist.get(i).get("tr_usr_id"))
                                                .addFormDataPart("tr_motif",trajetlist.get(i).get("tr_motif"))
                                                .addFormDataPart("tr_date_depart",trajetlist.get(i).get("tr_date_depart"))
                                                .addFormDataPart("tr_kilometrage_depart",trajetlist.get(i).get("tr_kilometrage_depart"))
                                                .addFormDataPart("tr_lieu_depart",trajetlist.get(i).get("tr_lieu_depart"))
                                                .addFormDataPart("tr_date_arrivee",trajetlist.get(i).get("tr_date_arrivee"))
                                                .addFormDataPart("tr_kilometrage_arrivee",trajetlist.get(i).get("tr_kilometrage_arrivee"))
                                                .addFormDataPart("tr_lieu_arrivee",trajetlist.get(i).get("tr_lieu_arrivee"))
                                                .addFormDataPart("tr_note",trajetlist.get(i).get("tr_note"))
                                                .addFormDataPart("tr_sync_id",trajetlist.get(i).get("tr_sync_id"))
                                                .addFormDataPart("tr_sync_date",trajetlist.get(i).get("tr_sync_date"))
                                                .addFormDataPart("tr_type",trajetlist.get(i).get("tr_type"))
                                                .addFormDataPart("tr_aff_id", trajetlist.get(i).get("tr_aff_id"))
                                                .build();
                                        Request request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadTrajetData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            export=response.body().string();
                                            JSONObject object=new JSONObject(export);

                                            db.UpdateTrajet_flag(object.getInt("tr_id"),object.getInt("tr_sync_id"),object.getString("tr_sync_date"));

                                            progress_inc=progress_inc+(100/size2);
                                            progress.setProgress(progress_inc);
                                            Thread.sleep(1000);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // envoi des kilométrages
                                    System.out.println("envoi des demandes de chargement");
                                    ArrayList<HashMap<String,String>> kilométragelist=db.GetKilometrage(db.GetLastUser_id());
                                    int size3=kilométragelist.size();
                                    for (int i=0;i<size3;i++){
                                        OkHttpClient client= new OkHttpClient();
                                        @SuppressLint("StaticFieldLeak") RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("km_id",kilométragelist.get(i).get("km_id"))
                                                .addFormDataPart("km_usr_id",kilométragelist.get(i).get("km_usr_id"))
                                                .addFormDataPart("km_date",kilométragelist.get(i).get("km_date"))
                                                .addFormDataPart("km_kilometrage",kilométragelist.get(i).get("km_kilometrage"))
                                                .addFormDataPart("km_latitude",kilométragelist.get(i).get("km_latitude"))
                                                .addFormDataPart("km_longitude",kilométragelist.get(i).get("km_longitude"))
                                                .addFormDataPart("km_sync_id",kilométragelist.get(i).get("km_sync_id"))
                                                .addFormDataPart("km_sync_date",kilométragelist.get(i).get("km_sync_date"))
                                                .addFormDataPart("km_file_path", kilométragelist.get(i).get("km_file_path"))
                                                .addFormDataPart("km_file_name", kilométragelist.get(i).get("km_file_name"))
                                                .addFormDataPart("km_aff_id", kilométragelist.get(i).get("km_aff_id"))
                                                .addPart(
                                                        Headers.of("Content-Disposition", "form-data; name=\"file\""), //facultatif
                                                        RequestBody.create(MediaType.parse("image/jpeg"), new File(kilométragelist.get(i).get("km_file_path"))))
                                                .build();
                                        Request request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadKilometrageData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            export=response.body().string();
                                            JSONObject object=new JSONObject(export);
                                            db.UpdateKilometrage_flag(object.getInt("km_id"),object.getInt("km_sync_id"),object.getString("km_sync_date"));
                                            progress_inc=progress_inc+(100/size3);
                                            progress.setProgress(progress_inc);
                                            Thread.sleep(1000);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // envoi des coordonnées gps
                                    System.out.println("envoi des coordonnées gps");
                                    ArrayList<HashMap<String, String>> gpslist = db.GetGps(db.GetLastUser_id());
                                    System.out.println(gpslist.size());
                                    while( gpslist.size() >0){
                                        JSONArray gpsSubArray = new JSONArray();
                                        List<HashMap<String, String>> gpsSublist=new ArrayList<HashMap<String, String>>();
                                        if(gpslist.size() >999){
                                            gpsSublist= new ArrayList<HashMap<String, String>>(gpslist.subList(0,999)) ;
                                            gpslist= new ArrayList<HashMap<String, String>> (gpslist.subList(999,gpslist.size()-1));
                                        }else{
                                            gpsSublist=gpslist;
                                            gpslist=new ArrayList<HashMap<String, String>>();
                                        }
                                        for (int j = 0; j < gpsSublist.size() ; j++) {
                                            JSONObject gpsObject = new JSONObject();
                                            gpsObject.put("gps_id", gpsSublist.get(j).get("gps_id"));
                                            gpsObject.put("gps_usr_id", gpsSublist.get(j).get("gps_usr_id"));
                                            gpsObject.put("gps_latitude", gpsSublist.get(j).get("gps_latitude"));
                                            gpsObject.put("gps_longitude", gpsSublist.get(j).get("gps_longitude"));
                                            gpsObject.put("gps_date", gpsSublist.get(j).get("gps_date"));
                                            gpsObject.put("gps_aff_id", gpsSublist.get(j).get("gps_aff_id"));
                                            gpsSubArray.put(gpsObject);
                                        }

                                        OkHttpClient client = new OkHttpClient();
//                            client.setConnectTimeout(500, TimeUnit.SECONDS);
//                            client.setReadTimeout(500, TimeUnit.SECONDS);
                                        String pattern = "yyyyMMddHHmmss.SS";
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                        String date = simpleDateFormat.format(new Date());


                                        RequestBody requestBody = new MultipartBuilder()
                                                .type(MultipartBuilder.FORM)
                                                .addFormDataPart("gps", gpsSubArray.toString())
                                                .addFormDataPart("userId",db.GetLastUser_id().toString()+date)
                                                .build();

                                        Request request = new Request.Builder()
                                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                                .url("http://"+uri+"/STB_CARBURANT/UploadGpsData")
                                                .post(requestBody)
                                                .build();
                                        try {
                                            Response response = client.newCall(request).execute();
                                            export = response.body().string();
                                            System.out.println("export: "+export);
                                            JSONArray synchronisationGpsArray = new JSONArray(export);
                                            for (int i = 0; i < synchronisationGpsArray.length(); i++){
                                                JSONObject object =  synchronisationGpsArray.getJSONObject(i);
                                                db.UpdateGps_flag(object.getInt("gps_id"), object.getInt("gps_sync_id"), object.getString("gps_sync_date"));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                progress.dismiss();
                                dialogBox("Envoi terminé avec success");
                            }
                        }.execute();

                    }else{
                        Toast.makeText(getContext(), "Vérifiez votre connexion internet svp", Toast.LENGTH_LONG).show();
                    }
                }
            });

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.setAddress(String.valueOf(editTextAddress.getText()));
                }
            });

            UpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new BackgroundJob_upgradeApp().execute();
                    progress.show();
                }
            });
        }else{
            Toast.makeText(getContext(), "Vous n'avez aucune affectation de véhicule active!", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ParametreViewModel.class);
        // TODO: Use the ViewModel
    }
    public void dialogBox(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(text);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }else {
            connected = false;
        }
        return connected;
    }


    public void stopService(){
        Intent  _scanServiceIntent = new Intent(getActivity(), ScanService.class);
        _scanServiceIntent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
        getActivity().stopService(_scanServiceIntent);
    }
    public void startService(){
        Intent  _scanServiceIntent = new Intent(getActivity(), ScanService.class);
        _scanServiceIntent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        getActivity().startService(_scanServiceIntent);
    }


    private class BackgroundJob_upgradeApp extends AsyncTask<Void, Void, Void> {

        int err=0;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url= new URL("http://41.111.138.18:7019/STB_CARBURANT/apk/ETRAKING.apk");
                int taille = url.openConnection().getContentLength();
                BufferedInputStream in = new BufferedInputStream(url.openStream());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());

                FileOutputStream fout = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/"+currentDateandTime.replaceAll(" ","")+".apk");

                System.out.println("------------------------------ 1  "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/"+currentDateandTime.replaceAll(" ","")+".apk");

                float courant = 0;
                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                    courant = courant + count;

                    int progressbarstatus = Math.round(courant / taille * 100);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                                progress.setProgress(progressbarstatus);
                                System.out.println(progressbarstatus);
                        }
                    });
                }
                if (in != null)
                {
                    in.close();
                }
                if (fout != null)
                {
                    fout.close();
                }

                // run install
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setDataAndType(uriFromFile(getContext(), new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/"+currentDateandTime.replaceAll(" ","")+".apk")), "application/vnd.android.package-archive");

                System.out.println("------------------------------   2"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/"+currentDateandTime.replaceAll(" ","")+".apk");

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                getContext().getApplicationContext().startActivity(intent);

            }catch (final Exception e){
                err++;
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        System.out.println("exception   "+e.toString());
                    }
                });

            }//*/



            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid)
        {

                super.onPostExecute(aVoid);
                getActivity().runOnUiThread(new Runnable()
                { @Override
                public void run()
                {
                    progress.dismiss();
                    if(err==0)
                        dialogBox("Nouveau fichier téléchargé avec succès à :"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                    else
                        dialogBox("Echec de la mise à jour !!");
                }
                }); //
        }

        Uri uriFromFile(Context context, File file) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            } else {
                return Uri.fromFile(file);
            }
        }
    }

}

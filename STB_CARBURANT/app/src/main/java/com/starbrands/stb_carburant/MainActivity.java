package com.starbrands.stb_carburant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

    DbHandler db;
    EditText user;
    EditText password;
    LocationManager locationManager;
    boolean continu=false;
    TextView changePwd;
    int userId;
    String username;
    ProgressDialog progressDialog;
    public static boolean userEntered=false;
    Request request;
    OkHttpClient client=new OkHttpClient();
    Response response;
    JSONArray array;
    String uri = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = new DbHandler(MainActivity.this);
            uri=db.getAddress();
            getUserInformations();
            user.setText(db.GetLastUser());
            Button button=(Button) findViewById(R.id.button_login);
            button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       getUserInformations();
                       username = user.getText().toString();
                       userId = db.CheckUserConnexion(user.getText(), password.getText());

                       if (db.checkInitialisation() && !isInternetAvailable()) {
                           Toast.makeText(MainActivity.this, "Vérifiez votre connexion internet svp", Toast.LENGTH_LONG).show();
                       }else{
                           if (userId != 0) {
                               db.UpdateConnectedUser(userId);
                               locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                               if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                   OnGPS();
                               } else {
                                   continu = true;
                               }
                               if (continu) {
                                   if(db.checkInitialisation()){
                                       if(isInternetAvailable()) {
                                           progressDialog = ProgressDialog.show(MainActivity.this,
                                                   "Initialisation...",
                                                   "Téléchargement des données depuis le serveur");

                                                    AsyncTask asyncTask= new AsyncTask() {
                                       @Override
                                       protected void onPreExecute() {
                                           super.onPreExecute();
                                       }
                                       @Override
                                       protected Object doInBackground(Object[] objects) {
                                                   try {
                                                       System.out.println("--------------------- Chargement des Departements---------------------");
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Departement").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Users").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Brand").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Model").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Fuel").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_Cost").build();
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
                                                       request = new Request.Builder().url("http://" + uri + "/STB_CARBURANT/webresources/Get_PaymenetType").build();
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
                                                   }catch (Exception e) {
                                                   e.printStackTrace();
                                                    }
                                           return null;
                                       }
                                       @Override
                                       protected void onPostExecute(Object o) {
                                           super.onPostExecute(o);
                                           runOnUiThread(new Runnable() {
                                               public void run() {
                                                   final Toast toast = Toast.makeText(MainActivity.this, "Initialisation terminée", Toast.LENGTH_SHORT);
                                                   toast.show();
                                               }
                                           });
                                           progressDialog.dismiss();
                                       }
                                   }.execute();

                                       }else{
                                           runOnUiThread(new Runnable() {
                                               public void run() {
                                                   final Toast toast = Toast.makeText(MainActivity.this, "Vérifiez votre connexion internet svp", Toast.LENGTH_SHORT);
                                                   toast.show();
                                               }
                                           });
                                       }
                                   }else{
                                       if (!db.GetLastUser().equals("GUEST")) {

                                           if(!isMyServiceRunning(SyncService.class) ){
                                               startSyncService();
                                           }

                                           if(!isMyServiceRunning(ScanService.class) ){
                                               startService();
                                           }
                                           goHome();
                                       }
                                   }
                               }
                           } else{
                               dialogBox("Nom d'utilisateur ou mot de passe incorrect");
                               user.getText().clear();
                               password.getText().clear();
                               getUserInformations();
                           }
                       }
                   }
               });
            changePwd=(TextView) findViewById(R.id.change_password);
            changePwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e) {dialogBox(e.getMessage());}
    }

    public  void getUserInformations(){
        user=(EditText)findViewById(R.id.edit_usr_login);
        password=(EditText)findViewById(R.id.edit_usr_password);
    }

    public void dialogBox(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(text);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                continu=true;
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
    public void goHome(){
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
        userEntered=true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopService(){
        Intent  _scanServiceIntent = new Intent(MainActivity.this, ScanService.class);
//        _scanServiceIntent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
        startService(_scanServiceIntent);
    }
    public void startService(){
        Intent  _scanServiceIntent = new Intent(MainActivity.this, ScanService.class);
//        _scanServiceIntent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(_scanServiceIntent);
    }

    public void startSyncService(){
        Intent  _scanServiceIntent = new Intent(MainActivity.this, SyncService.class);
//        _scanServiceIntent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(_scanServiceIntent);
    }
}
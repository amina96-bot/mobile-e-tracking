package com.starbrands.stb_carburant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class Code {

//      try {
//        Request request = new Request.Builder().url("http://41.111.138.18:7019/STB_CARBURANT/webresources/Get_Affectation/query?usr_id=" + db.GetLastUser_id()).build();
//        Response response = (new OkHttpClient()).newCall(request).execute();
//        JSONArray array = new JSONArray(response.body().string());
//        if(array.length()>0){
//            checkExistActiveAffectation=true;
//        }
//    }catch (Exception e) {e.printStackTrace();}


//    MainActivity.AsyncTaskRunner runner = new MainActivity.AsyncTaskRunner();
//                       runner.execute(userId);

//    private class AsyncTaskRunner extends AsyncTask<Integer, String, String> {
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "Connexion...",
//                    "Patientez un moment svp!");
//        }
//
//        @Override
//        protected String doInBackground(Integer... params) {
//            publishProgress("Connexion en cours...");
//            try {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder().url("http://41.111.138.18:7019/STB_CARBURANT/webresources/Get_Affectation/query?usr_id=" + db.GetLastUser_id()).build();
//                Response response = client.newCall(request).execute();
//                JSONArray array = new JSONArray(response.body().string());
//
//                if (response.isSuccessful()) {
//                    System.out.println("----------------------------true-------------------------------");
//                    if (array.length() > 0) {
//                        bool = true;
//                    }
//                } else {
//                    System.out.println("----------------------------false-------------------------------");
//                }
//                Thread.sleep(500);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
//
//            try {
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }




}

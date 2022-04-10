package com.starbrands.stb_carburant;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ScanService extends Service {
    private LocationListener _locListener;
    LocationManager locationManager;

    DbHandler db=new DbHandler(this);

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
            startForeground(1, new Notification());
        }
    }

    @Override
    public void onCreate() {
        _locListener = new MyLocationListener();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                new CountDownTimer(5*1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        Toast.makeText(getApplicationContext(), "Veuillez autoriser cette appli à accéder aux services de localisation", Toast.LENGTH_LONG).show();
                    }
                    public void onFinish() {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                }.start();

            }
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, _locListener);
        }
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

            startForeground(2, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        locationManager.removeUpdates(_locListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            final Toast toast = Toast.makeText(getApplicationContext(), "Signal GPS perdu", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onLocationChanged(Location location) {
           double latitude=location.getLatitude();
           double longitude=location.getLongitude();

            //insert
            if (!db.GetLastUser().equals("GUEST") && db.checkExistingActiveAffectation() && MainActivity.userEntered && db.getChosenAffectationId()!=0) {
                db.insertGPS(db.GetLastUser_id(), latitude, longitude, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 0, "gps_sync_date",db.getChosenAffectationId());
//                Toast.makeText(getApplicationContext(), "************ "+latitude+"  ************** "+longitude+" ******************  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), Toast.LENGTH_SHORT).show();

                System.out.println("Gps inserted in database **************************  "+latitude+"  ************** "+longitude+" ******************************************  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" ******************************************  "+db.getChosenAffectationId());
            }

        }
    }
}

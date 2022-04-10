package com.starbrands.stb_carburant;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.starbrands.stb_carburant.ui.fuel.AddFuelFragment;
import com.starbrands.stb_carburant.ui.kilometrage.CaptureKilometrageFragment;
import com.starbrands.stb_carburant.ui.trajet.TrajetFragment;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    boolean isFABOpen;
    FragmentManager fragmentManager = getSupportFragmentManager();
    TextView user_header;
    TextView vehicule_header;
    DbHandler db = new DbHandler(Home.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_dashbaord,R.id.nav_parametre,R.id.nav_vehicles)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //-----------------------------------------------------------Float buttons-------------------------------------------------------------------------------
        isFABOpen=false;
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab1.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);

        if(db.checkExistingActiveAffectation() && db.checkExistChosenAffectation()){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFABOpen){
                        showFABMenu();
                        isFABOpen=true;
                    }else{
                        closeFABMenu();
                        isFABOpen=false;
                    }
                }
            });
        }else{
            if(!db.checkExistingActiveAffectation()){
                Toast.makeText(this, "Vous n'avez aucune affectation active!", Toast.LENGTH_LONG).show();
            }else{
                if(!db.checkExistChosenAffectation()){
                    db.chooseDefaultAffectation();
                }
            }
        }

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFuelFragment addFuelFragment= new AddFuelFragment();
                addFuelFragment.setArguments(b);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,addFuelFragment).commit();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureKilometrageFragment captureKilometrageFragment=new CaptureKilometrageFragment();
                captureKilometrageFragment.setArguments(b);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,captureKilometrageFragment).commit();

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrajetFragment trajetFragment=new TrajetFragment();
                trajetFragment.setArguments(b);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,trajetFragment).commit();
            }
        });

        //---------------------------------------------------- Set Notifications --------------------------------------------------------------------------------------
        try {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager= (NotificationManager) getApplicationContext()
                    .getSystemService(ns);
            if(db.existTrajetsEnCours()){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
                mBuilder.setSmallIcon(R.drawable.trajet_en_cours);
                mBuilder.setContentTitle("Trajet en cours");
                mBuilder.setContentText("Vous avez au moins un trajet en cours d'enregistrement");
                mBuilder.setPriority(Notification.PRIORITY_MAX);

                mNotificationManager =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }
                mNotificationManager.notify(0, mBuilder.build());

            }else{
                mNotificationManager.cancelAll();
            }
        }catch (Exception e){
            dialogBox(e.getMessage());
            e.printStackTrace();
        }
            //-----------------------------------------------------------Set user & vehicle information in header view-------------------------------------------------------------------------------
        View headerView = navigationView.getHeaderView(0);
        user_header =  (TextView) headerView.findViewById(R.id.user_header);
        vehicule_header = (TextView) headerView.findViewById(R.id.vehicule_header);

        user_header.setText(db.getUserFirstNameLastName());
        vehicule_header.setText(db.getBrandVehicle(db.getChosenVehicleId()) +" "+db.getModelDescriptionVehicle(db.getChosenVehicleId()) +" /"+db.getImmatriculationVehicle(db.getChosenVehicleId()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showFABMenu(){
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        fab3.setVisibility(View.VISIBLE);

        fab1.animate().setDuration(600);
        fab2.animate().setDuration(600);
        fab3.animate().setDuration(600);

        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_115));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_165));
    }

    private void closeFABMenu(){
        fab1.animate().translationY(0);
        fab1.setVisibility(View.INVISIBLE);
        fab2.animate().translationY(0);
        fab2.setVisibility(View.INVISIBLE);
        fab3.animate().translationY(0);
        fab3.setVisibility(View.INVISIBLE);
    }

    public void dialogBox(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(text);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
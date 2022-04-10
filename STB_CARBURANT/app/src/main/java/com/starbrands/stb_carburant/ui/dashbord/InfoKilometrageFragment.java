package com.starbrands.stb_carburant.ui.dashbord;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;
import com.starbrands.stb_carburant.ui.trajet.OldTrajetFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoKilometrageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoKilometrageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textView_kilometrage;
    TextView textView_date;
    Button delete_button;
    Button edit_button;
    ImageView picture_text_view;
    TextView demandeStatus;
    TextView demandeMontantRecharge;
    TextView demandeMotifRefus;

    public InfoKilometrageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoKilometrageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoKilometrageFragment newInstance(String param1, String param2) {
        InfoKilometrageFragment fragment = new InfoKilometrageFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);}
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_info_kilometrage, container, false);
        DbHandler db = new DbHandler(getContext());
        textView_kilometrage = (TextView) view.findViewById(R.id.kilometrage);
        textView_date = (TextView) view.findViewById(R.id.date);
        //                delete_button = (Button) view.findViewById(R.id.delete_button);
        edit_button = (Button) view.findViewById(R.id.edit_button);
        picture_text_view = (ImageView) view.findViewById(R.id.picture_km_counter);
        demandeStatus=(TextView) view.findViewById(R.id.demandeStatus);
        demandeMontantRecharge=(TextView) view.findViewById(R.id.demandeMontantRecharge);
        demandeMotifRefus=(TextView) view.findViewById(R.id.demandeMotifRefus);
        Bundle args = getArguments();
        if (args != null) {
            int idKilometrage = Integer.parseInt(args.getString("id"));
            SQLiteDatabase readableDb = db.getReadableDatabase();
            Cursor cursor = readableDb.rawQuery(" select * from  KILOMETRAGE where km_id=" + idKilometrage, null);

            if(cursor!=null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                textView_date.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("km_date"))));
                textView_kilometrage.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("km_kilometrage"))));
                picture_text_view.setImageURI(Uri.parse(String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_path")))));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("accepted")))==-1) {
                    demandeStatus.setText("Demande de chargement non traitée");
                }else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("accepted")))==0){
                    demandeStatus.setText("Demande de chargement refusée");
                    demandeStatus.setTextColor(0xFFF44336);
                    demandeMotifRefus.setText("Motif de refus: "+cursor.getString(cursor.getColumnIndex("motif_refus")));
                    demandeMotifRefus.setTextColor(0xFFF44336);
                }else{
                   if(db.kmCharge(idKilometrage).equals("-1")) {
                       demandeStatus.setText("Demande de chargement acceptée et en attente de confirmation(chargement non effectué)");
                       demandeStatus.setTextColor(Color.parseColor("#ff992b"));
                   }else{
                       demandeStatus.setText("Demande de chargement acceptée et confirmée ");
                       demandeStatus.setTextColor(0xFF4CAF50);
                       demandeMontantRecharge.setText("Montant rechargé: "+cursor.getString(cursor.getColumnIndex("montant_recharge")));
                       demandeMontantRecharge.setTextColor(0xFF4CAF50);
                   }


                }

                if(cursor.getInt(cursor.getColumnIndex("km_sync_id"))!=0){
                    edit_button.setVisibility(View.INVISIBLE);
                }

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        b.putString("id", String.valueOf(idKilometrage));

                        EditKilometrageFragment editKilometrageFragment= new EditKilometrageFragment();
                        editKilometrageFragment.setArguments(b);
                        InfoKilometrageFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,editKilometrageFragment).commit();
                    }
                });
//                delete_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle b = new Bundle();
//                        b.putString("id", String.valueOf(idKilometrage));
//                        b.putString("type", "km");
//                        DeleteFragment deleteFragment= new DeleteFragment();
//                        deleteFragment.setArguments(b);
//                        InfoKilometrageFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,deleteFragment).commit();
//                    }
//                });
            }
        }

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Informations demande de chargement");
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
}
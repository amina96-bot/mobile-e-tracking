package com.starbrands.stb_carburant.ui.dashbord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.MainActivity;
import com.starbrands.stb_carburant.R;
import com.starbrands.stb_carburant.ui.fuel.AddFuelFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeyNumbersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeyNumbersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView kilometrage ;
    TextView consoMoy ;
    private ListView list;
    private ListView listTrajets;
    ArrayList<Integer>imageIdTrajet=new ArrayList<>();
    ArrayList<String> dateDepartTrajet=new ArrayList<>() ;
    ArrayList<String> kilometrageDepartTrajet=new ArrayList<>() ;
    ArrayList<String> dateArriveeTrajet=new ArrayList<>() ;
    ArrayList<String> kilometrageArriveeTrajet=new ArrayList<>() ;

    ArrayList<Integer>imageId=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>() ;
    ArrayList<String> kilometrages=new ArrayList<>() ;

    public KeyNumbersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KeyNumbersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KeyNumbersFragment newInstance(String param1, String param2) {
        KeyNumbersFragment fragment = new KeyNumbersFragment();
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
        View view= inflater.inflate(R.layout.fragment_key_numbers, container, false);
        DbHandler db = new DbHandler(getContext());
        try{
        kilometrage = (TextView) view.findViewById(R.id.kilometrage);
        consoMoy = (TextView) view.findViewById(R.id.consoMoy);
        kilometrage.setText(String.valueOf(db.dernierKilometrage()));
        consoMoy.setText(String.valueOf( change(db.consommationMoyenne()* 100,4)));

        CustomList adapter = new CustomList((Activity) this.getContext(), imageId,date,kilometrages);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        ArrayList<HashMap<String, String>> journal = db.getJournal();

            for (HashMap<String, String> map : journal) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if(entry.getKey().equals("type")){
                        String type=entry.getValue();
                        switch (type) {
                            case "km":
                                if(  db.kmStatus(Integer.parseInt(Objects.requireNonNull(map.get("id")))  ) .equals("1") ) {
                                    if(!db.kmCharge(Integer.parseInt(Objects.requireNonNull(map.get("id")))  ) .equals("-1") ){
                                        imageId.add(R.drawable.confirmed_request_counter);
                                        }else{
                                        imageId.add(R.drawable.validated_request_counter);
                                            }
                                    }else if (db.kmStatus(Integer.parseInt(Objects.requireNonNull(map.get("id")))  ) .equals("0")){
                                    imageId.add(R.drawable.rejected_request_counter);
                                    }else{
                                        imageId.add(R.drawable.unprocessed_request_counter);
                                    }
                                break;
                            case "pa":
                                imageId.add(R.drawable.fuel);
                                break;
                            case "tr":
                                if(  db.trType(Integer.parseInt(Objects.requireNonNull(map.get("id")))) .equals("1") ){
                                    imageId.add(R.drawable.icon3);
                                }else{
                                    if(  db.trTermine(Integer.parseInt(Objects.requireNonNull(map.get("id"))))  ){
                                        imageId.add(R.drawable.trajet_termine);
                                    }else{
                                        imageId.add(R.drawable.trajet_en_cours);
                                    }
                                }
                                break;
                        }
                    }
                    if(entry.getKey().equals("date")){
                        date.add(entry.getValue());
                    }
                    if(entry.getKey().equals("kilometrage")){
                        kilometrages.add(entry.getValue());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Bundle b = new Bundle();
                    b.putString("id",journal.get(position).get("id"));

                    String type=journal.get(position).get("type");
                    switch (type){
                        case "km":
                            InfoKilometrageFragment infoKilometrageFragment= new InfoKilometrageFragment();
                            infoKilometrageFragment.setArguments(b);
                            KeyNumbersFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,infoKilometrageFragment).commit();
                            break;
                        case "pa":
                            InfoPaymentFragment infoPaymentFragment= new InfoPaymentFragment();
                            infoPaymentFragment.setArguments(b);
                            KeyNumbersFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,infoPaymentFragment).commit();                            break;
                        case "tr":
                            InfoTrajetFragment infoTrajetFragment= new InfoTrajetFragment();
                            infoTrajetFragment.setArguments(b);
                            KeyNumbersFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,infoTrajetFragment).commit();
                            break;
                    }
                }
            });

            CustomListTrajet adapterTrajet = new CustomListTrajet((Activity) this.getContext(), imageIdTrajet,dateDepartTrajet,kilometrageDepartTrajet,dateArriveeTrajet,kilometrageArriveeTrajet);
//            listTrajets = (ListView) view.findViewById(R.id.listViewTrajets);
//            listTrajets.setAdapter(adapterTrajet);

            ArrayList<HashMap<String, String>> trajets = db.trajets();
            for (HashMap<String, String> map : trajets) {
                for (Map.Entry<String, String> entry : map.entrySet()) {

                    if(entry.getKey().equals("date_depart")){
                        dateDepartTrajet.add(entry.getValue());
                    }
                    if(entry.getKey().equals("date_arrivee")){
                        dateArriveeTrajet.add(entry.getValue());
                    }
                    if(entry.getKey().equals("kilometrage_depart")){
                        kilometrageDepartTrajet.add(entry.getValue());
                    }
                    if(entry.getKey().equals("kilometrage_arrivee")){
                        kilometrageArriveeTrajet.add(entry.getValue());
                        if(entry.getValue().equals("--")){
                            imageIdTrajet.add(R.drawable.trajet_en_cours);
                        }else{
                            imageIdTrajet.add(R.drawable.trajet_termine);
                        }
                    }
                    adapterTrajet.notifyDataSetChanged();
                }
            }


        }catch (Exception e){
            dialogBox(e.getMessage());
            e.printStackTrace();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        return view;
    }

    public void dialogBox(String text){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(text);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    static double change(double value, int decimalpoint)
    {
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);
        return value;
    }
}
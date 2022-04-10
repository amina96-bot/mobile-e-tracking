package com.starbrands.stb_carburant.ui.dashbord;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPaymentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textView_kilometrage;
    TextView textView_date;
    TextView textView_montant;

    TextView textView_prix_unitaire;
    TextView textView_methode_paiement;
    TextView textView_comment;

    Button delete_button;
    Button edit_button;
    ImageView picture_text_view;
    ImageView picture_text_view2;

    public InfoPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoPaymentFragment newInstance(String param1, String param2) {
        InfoPaymentFragment fragment = new InfoPaymentFragment();
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
        View view= inflater.inflate(R.layout.fragment_info_payment, container, false);
        DbHandler db = new DbHandler(getContext());
        textView_kilometrage = (TextView) view.findViewById(R.id.textView_kilometrage);
        textView_date = (TextView) view.findViewById(R.id.textView_date);
        textView_montant = (TextView) view.findViewById(R.id.textView_montant);
        textView_prix_unitaire = (TextView) view.findViewById(R.id.textView_prix_unitaire);
        textView_methode_paiement = (TextView) view.findViewById(R.id.textView_methode_paiement);
        textView_comment = (TextView) view.findViewById(R.id.textView_comment);

        picture_text_view = (ImageView) view.findViewById(R.id.picture_text_view);
        picture_text_view2 = (ImageView) view.findViewById(R.id.picture_text_view2);
        edit_button = (Button) view.findViewById(R.id.edit_button);

        Bundle args = getArguments();
        if (args != null) {
            int idPayment = Integer.parseInt(args.getString("id"));
            SQLiteDatabase readableDb = db.getReadableDatabase();
            Cursor cursor = readableDb.rawQuery(" select * from  PAYMENT where pa_id=" + idPayment, null);

            if(cursor!=null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                textView_date.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("pa_date"))));
                textView_kilometrage.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_distance"))));
                textView_montant.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_cost"))));
                textView_prix_unitaire.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_unit_price"))));
                textView_comment.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("pa_comment"))));

                //get type payment type
                Cursor cursor2 = readableDb.rawQuery(" select * from  PAYMENT_TYPE where pt_id=" + cursor.getInt(cursor.getColumnIndex("pa_pt_id")), null);
                if(cursor2!=null && cursor2.getCount() > 0) {
                    cursor2.moveToFirst();
                    textView_methode_paiement.setText(String.valueOf(cursor2.getString(cursor2.getColumnIndex("pt_description"))));
                }

                picture_text_view.setImageURI(Uri.parse(String.valueOf(cursor.getString(cursor.getColumnIndex("pa_file_path")))));
                picture_text_view2.setImageURI(Uri.parse(String.valueOf(cursor.getString(cursor.getColumnIndex("pa_km_counter_file_path")))));

                if(cursor.getInt(cursor.getColumnIndex("pa_sync_id"))!=0){
                    edit_button.setVisibility(View.INVISIBLE);
                }

//                delete_button = (Button) view.findViewById(R.id.delete_button);

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        b.putString("id", String.valueOf(idPayment));

                        EditPaymentFragment editPaymentFragment= new EditPaymentFragment();
                        editPaymentFragment.setArguments(b);
                        InfoPaymentFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,editPaymentFragment).commit();
                    }
                });

//                delete_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle b = new Bundle();
//                        b.putString("id", String.valueOf(idPayment));
//                        b.putString("type", "pa");
//
//                        DeleteFragment deleteFragment= new DeleteFragment();
//                        deleteFragment.setArguments(b);
//                        InfoPaymentFragment.this.getParentFragmentManager().beginTransaction().replace(R.id.nav_dashbaord,deleteFragment).commit();
//                    }
//                });
            }

        }
        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Informations paiement carburant");
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

        return  view;
    }

}
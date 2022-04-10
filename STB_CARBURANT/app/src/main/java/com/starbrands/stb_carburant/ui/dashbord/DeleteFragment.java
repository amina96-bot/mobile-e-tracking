package com.starbrands.stb_carburant.ui.dashbord;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.starbrands.stb_carburant.DbHandler;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends DialogFragment {


    Button no_button;
    Button yes_button;
    int id;
    String type;

    public DeleteFragment() {
        // Required empty public constructor
    }
    public static DeleteFragment newInstance() {
        DeleteFragment fragment = new DeleteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_delete, container, false);

        no_button=(Button) view.findViewById(R.id.no_button);
        yes_button=(Button) view.findViewById(R.id.yes_button);


        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = getArguments();
                if (args != null) {
                    id = Integer.parseInt(args.getString("id"));
                    type = args.getString("type");

                    switch (type){
                        case "km":
                            DbHandler db = new DbHandler(getContext());
                            db.deleteKilometrage(id);
                            db.close();
                            break;
                        case "pa":
                            db = new DbHandler(getContext());
                            db.deletePayment(id);
                            db.close();
                            break;
                        case "tr":
                            db = new DbHandler(getContext());
                            db.deleteTrajet(id);
                            db.close();
                            break;
                    }
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                }
            }
        });


        return view;
    }
}
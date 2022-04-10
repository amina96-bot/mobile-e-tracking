package com.starbrands.stb_carburant.ui.dashbord;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.starbrands.stb_carburant.R;

import java.util.ArrayList;

public class CustomListTrajet extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<Integer> imageIdTrajet;
    private final ArrayList<String> dateDepartTrajet;
    private final ArrayList<String> kilometrageDepartTrajet;
    private final ArrayList<String> dateArriveeTrajet;
    private final ArrayList<String> kilometrageArriveeTrajet;

    public CustomListTrajet(Activity context,
                      ArrayList<Integer> imageIdTrajet,ArrayList<String> dateDepartTrajet,ArrayList<String> kilometrageDepartTrajet,ArrayList<String> dateArriveeTrajet,ArrayList<String> kilometrageArriveeTrajet) {
        super(context, R.layout.list_single_trajet,dateDepartTrajet);
        this.context = context;
        this.imageIdTrajet = imageIdTrajet;
        this.dateDepartTrajet = dateDepartTrajet;
        this.kilometrageDepartTrajet = kilometrageDepartTrajet;
        this.dateArriveeTrajet = dateArriveeTrajet;
        this.kilometrageArriveeTrajet = kilometrageArriveeTrajet;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_trajet, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_trajet);
        TextView txt_date_depart= (TextView) rowView.findViewById(R.id.txt_date_depart);
        TextView txt_kilometrage_depart = (TextView) rowView.findViewById(R.id.txt_kilometrage_depart);
        TextView txt_date_arrivee= (TextView) rowView.findViewById(R.id.txt_date_arrivee);
        TextView txt_kilometrage_arrivee = (TextView) rowView.findViewById(R.id.txt_kilometrage_arrivee);

        imageView.setImageResource(imageIdTrajet.get(position));
        txt_date_depart.setText(dateDepartTrajet.get(position));
        txt_date_arrivee.setText(dateArriveeTrajet.get(position));
        txt_kilometrage_depart.setText(kilometrageDepartTrajet.get(position));
        txt_kilometrage_arrivee.setText(kilometrageArriveeTrajet.get(position));

        return rowView;
    }
}

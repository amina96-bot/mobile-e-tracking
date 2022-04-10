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

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;

    private final ArrayList<String> date;
    private final ArrayList<String> kilometrage;
    private final ArrayList<Integer> imageId;

    public CustomList(Activity context,
                      ArrayList<Integer> imageId,ArrayList<String> date,ArrayList<String> kilometrage) {
        super(context, R.layout.list_single, date);
        this.context = context;
        this.imageId = imageId;
        this.date = date;
        this.kilometrage = kilometrage;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.txt2);

        txtTitle.setText(date.get(position));
        txtTitle2.setText(kilometrage.get(position));

        imageView.setImageResource(imageId.get(position));

        return rowView;
    }
}
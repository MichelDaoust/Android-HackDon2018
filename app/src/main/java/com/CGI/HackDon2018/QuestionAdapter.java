package com.CGI.HackDon2018;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;


public class QuestionAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> values;

    public QuestionAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.question_layout, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.question_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.question);
//        RadioButton radioButtonYes = (RadioButton) rowView.findViewById(R.id.radio_Yes);
//        RadioButton radioButtonNo = (RadioButton) rowView.findViewById(R.id.radio_No);

        textView.setText(values.get(position));

        // Change icon based on name
        String s = values.get(position);

        System.out.println(s);

//        if (s.equals("WindowsMobile")) {
//            imageView.setImageResource(R.drawable.windowsmobile_logo);

        return rowView;
    }







}

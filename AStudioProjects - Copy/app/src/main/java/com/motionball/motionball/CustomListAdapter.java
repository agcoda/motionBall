package com.motionball.motionball;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    // reference the activity
    private final Activity context;

    private final String[] BTNameArray;


    public CustomListAdapter(Activity context, String[] BTNameArrayParam){
        super(context, R.layout.listview_row, BTNameArrayParam);

        this.context=context;
        this.BTNameArray = BTNameArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView data_textview = (TextView) rowView.findViewById(R.id.data_textView);


        //this code sets the values of the objects to values from the arrays
        data_textview.setText(BTNameArray[position]);


        return rowView;
    }
}
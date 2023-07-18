package com.motionball.motionball;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataViewActivity extends AppCompatActivity {

    private ArrayList<String> tArray;
    private ArrayList<String> aArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        final Button RecordReturn = findViewById(R.id.Data_Return);
        RecordReturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                finish();
            }
        });

        Intent DynamicGraph = getIntent();

// take string arrays form the dynamicgraph view
        tArray  = DynamicGraph.getStringArrayListExtra("tArray");
        aArray  = DynamicGraph.getStringArrayListExtra("aArray");


        List<String> DataStringArrayList = new ArrayList<String>();
        DataStringArrayList.add("  t,      a" );
        for (int i = 0; i < tArray.size(); i++) {
            DataStringArrayList.add("  "+tArray.get(i) + ", " +  aArray.get(i));
        }

        ListView listView;

        Object[] DataObjectList = DataStringArrayList.toArray();
        String[] ATNameStringArray = Arrays.copyOf(DataObjectList, DataObjectList.length, String[].class);

        CustomListAdapter listAdapter = new CustomListAdapter(this, ATNameStringArray);
        listView = (ListView) findViewById(R.id.Data_ListView);
        listView.setAdapter(listAdapter);

    }
}





/*
  //This is a sample set of 3 data points with constant acceleration
   double t = 0.0;
   double[] at1 = {0, 0, 0, -1};
   double[] at2 = {0, 0, 0, -1};
   double[] at3 = {0, 0, 0, -1};


   // String[] myDataset = {at1.toString(),at2.toString(),at3.toString()};


    }

    private double AngleComputation(double[] A0, double[] A){

        // cos theta = a dot b / (mag a *mag b)
        double theta = Math.acos((A0[0]*A[0]+A0[1]*A[1]+A0[2]*A[2])/
                (Math.sqrt((A0[0]*A0[0]+A0[1]*A0[1]+A0[2]*A0[2])*(Math.sqrt((A[0]*A[0]+A[1]*A[1]+A[2]*A[2]))))));
        double thetaDeg = Math.toDegrees(theta)-90;
        return thetaDeg;
    }
    */


// Use to take dot product of two vectors, redundant
//public double DotProduct(double[] A0, double[] A){
//  double Dot;
//Dot = A0[0]*A[0]+A0[1]*A[1]+A0[2]*A[2];

//     return Dot;
//}
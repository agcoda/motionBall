package com.motionball.motionball;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;

public class freeFall_DynamicGraph extends AppCompatActivity {

    PointsGraphSeries<DataPoint> TASeries;
    GraphView TAScatterPlot;
    public ArrayList<XYValue> TAValueArray;
    public ArrayList<String> tArrayList;
    public ArrayList<String> aArrayList;

    private Button btnCollect;
    private Button btnClearData;
    private Button btnStop;

    private EditText TEdit;
    final String takeData = "Collect and Graph";
    final String stopData = "Stop Collection";

    CountDownTimer dataTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall__dynamic_graph);

        btnCollect = (Button) findViewById(R.id.btnCollect);
        btnClearData = (Button) findViewById(R.id.btnClearData);
        btnStop = (Button) findViewById(R.id.btnStop);



        TAScatterPlot = (GraphView) findViewById(R.id.graph);
        TAScatterPlot.setTitle("Acceleration vs Time");

        GridLabelRenderer TAGridLabel = TAScatterPlot.getGridLabelRenderer();
        TAGridLabel.setHorizontalAxisTitle("t(s)");
        TAGridLabel.setVerticalAxisTitle("a(m/s^2)");


        TAValueArray = new ArrayList<>();

        tArrayList = new ArrayList<String>();
        aArrayList = new ArrayList<String>();



        init();
    }

    


    private void init() {
        final Double[] A = new Double[4];
        A[1] = 0.0;
        A[2] = 0.0;
        A[3] = -9.8;
        TASeries = new PointsGraphSeries<>();

        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEdit.setText(String.valueOf(0.000));
                TAValueArray.clear();
                TAScatterPlot.removeAllSeries();
                TASeries = null;
                TASeries = new PointsGraphSeries<>();
            }
        });

        final CountDownTimer dataTimer = new CountDownTimer(10000, 100)
        {
            public void onTick(long millisUntilFinished) {
                double millisDouble = ((10000 - (millisUntilFinished)));

                //double t = millisDouble - (millisDouble % 100);
                //TEdit.setText(String.valueOf(t / 1000));
// changed this to take the raw time instead of correct it.
                Double Amag = Math.sqrt(A[1] * A[1] + A[2] * A[2] + A[3] * A[3]);
                TAValueArray.add(new XYValue(millisDouble, Amag));

                if (TAValueArray.size() != 0) {
                    createScatterPlot();
                } else {
                }
               // init();
            }
            public void onFinish() {
            }
        };

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   dataTimer.start();
                }

            });
        btnStop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(dataTimer != null)
               dataTimer.cancel();
           }

           });


        ;
    }


    private void createScatterPlot() {

        for (int i = 0; i < TAValueArray.size(); i++) {
            try {
                double t = TAValueArray.get(i).getX();
                double a = TAValueArray.get(i).getY();
                TASeries.appendData(new DataPoint(t, a), true, 200);
            } catch (IllegalArgumentException e) {
            }
        }
        TASeries.setSize(10);
        TAScatterPlot.addSeries(TASeries);
    }

    public void viewTable(View view) {
        if (tArrayList.size() != 0) {
            tArrayList.clear();
        }
        if (aArrayList.size() != 0) {
            aArrayList.clear();
        }
        if (TAValueArray.size() != 0) {

            //split the x,y object array into x and y array lists
            for (int i = 0; i < TAValueArray.size(); i++) {
                try {
                    double t = TAValueArray.get(i).getX();
                    tArrayList.add(Double.toString(t));
                    double a = TAValueArray.get(i).getY();
                    aArrayList.add(Double.toString(a));
                } catch (IllegalArgumentException e) {
                }
            }
            // convert those array lists into arrays
        /*Object[] tObjectList = tArrayList.toArray();
        String[] tStringArray = Arrays.copyOf(tObjectList, tObjectList.length, String[].class);

        Object[] aObjectList = aArrayList.toArray();
        String[] aStringArray = Arrays.copyOf(aObjectList, aObjectList.length, String[].class);
*/
            //pass those string array lists
            Intent openDataViewActivity = new Intent(this, DataViewActivity.class);
            openDataViewActivity.putStringArrayListExtra("tArray", tArrayList);
            openDataViewActivity.putStringArrayListExtra("aArray", aArrayList);
            startActivity(openDataViewActivity);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "No data to show, choose \"Collect and Graph\" first";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);

            toast.show();


        }
    }
}

                    /*    btnViewTable.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent openTestActivity = new Intent(this, DataViewActivity.class);
        Bundle ArrayBundle = new Bundle();
        //ArrayBundle.putArray
        startActivity(openTestActivity);
        }
        });  */
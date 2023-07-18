package com.motionball.motionball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FreeFallActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freefall);
    }
}
/*
        final Button RecordReturn = findViewById(R.id.RecordReturn);
        RecordReturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                finish();
            }
        });
    }

    public void RecordData(View view){
        Intent openDataViewActivity = new Intent(this, freeFall_DynamicGraph.class);
        startActivity(openDataViewActivity);
    }

    public void Calibrate(View view){
        double[] a0 = new double[3];

        //Grab data from bluetooth here

        a0[0] = 1;
        // this converts the doubles to strings for display in text
        String a0x = new Double(a0[0]).toString();
        String a0y = new Double(a0[1]).toString();
        String a0z = new Double(a0[2]).toString();
        // find all three text boxes
        final TextView A0xData = (TextView) findViewById(R.id.Record_A0x);
        final TextView A0yData = (TextView) findViewById(R.id.Record_A0y);
        final TextView A0zData = (TextView) findViewById(R.id.Record_A0z);
        //display the values
        A0xData.setText(a0x);
        A0yData.setText(a0y);
        A0zData.setText(a0z);

    }
}
*/
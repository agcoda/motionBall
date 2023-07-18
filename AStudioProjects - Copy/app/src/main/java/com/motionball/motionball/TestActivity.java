package com.motionball.motionball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final Button TestReturn = findViewById(R.id.TestReturn);
        TestReturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                finish();
            }
    });



    }

    public void ShowLiveData(View view){
        double[] a = new double[3];

        //Grab data from bluetooth here

        a[0] = 1;
        // this converts the doubles to strings for display in text
        String ax = new Double(a[0]).toString();
        String ay = new Double(a[1]).toString();
        String az = new Double(a[2]).toString();
        // find all three text boxes
        final TextView AxData = (TextView) findViewById(R.id.Test_Ax);
        final TextView AyData = (TextView) findViewById(R.id.Test_Ay);
        final TextView AzData = (TextView) findViewById(R.id.Test_Az);
        //display the values
        AxData.setText(ax);
        AyData.setText(ay);
        AzData.setText(az);

    }

    public void OpenBluetooth(View view){
        Intent openBluetoothActivity = new Intent(this, BluetoothConnect.class);
        startActivity(openBluetoothActivity);
    }
}

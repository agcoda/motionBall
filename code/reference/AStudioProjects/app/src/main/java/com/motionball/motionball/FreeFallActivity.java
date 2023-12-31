package com.motionball.motionball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FreeFallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freefall);

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
}

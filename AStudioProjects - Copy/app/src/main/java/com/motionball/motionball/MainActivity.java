package com.motionball.motionball;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    //opens the test screen
    public void OpenTest(View view){
        Intent openTestActivity = new Intent(this, TestActivity.class);
        startActivity(openTestActivity);
    }

    //opens the freefall screen
    public void OpenFreeFall(View view){
        Intent openFreeFallActivity = new Intent(this, FreeFallActivity.class);
        startActivity(openFreeFallActivity);
    }

// calls java class UpdateDialogueFragment and shows the dialogue
    public void UpdateCheck(View view){
        DialogFragment newFragment = new UpdateDialogueFragment();
        newFragment.show(getFragmentManager(), "Update");

    }

    public void ComingSoon(View view){
        DialogFragment newFragment = new ComingSoonFragment();
        newFragment.show(getFragmentManager(), "ComingSoon");

    }
}

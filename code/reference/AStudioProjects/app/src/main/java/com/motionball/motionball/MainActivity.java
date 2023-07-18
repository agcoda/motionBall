package com.motionball.motionball;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button Main_Feedback;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Main_Feedback = (Button) findViewById(R.id.btnFeedback);


        Main_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Type your issue below then hit ok");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }


    //opens the test screen
    public void OpenTest(View view){
        Intent openTestActivity = new Intent(this, BluetoothConnect.class);
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

    public void Feedback(View view){
        DialogFragment newFragment = new UpdateDialogueFragment();
        newFragment.show(getFragmentManager(), "Update");

    }

    public void ComingSoon(View view){
        DialogFragment newFragment = new ComingSoonFragment();
        newFragment.show(getFragmentManager(), "ComingSoon");

    }
}

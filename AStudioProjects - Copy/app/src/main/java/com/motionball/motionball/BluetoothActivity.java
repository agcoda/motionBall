package com.motionball.motionball;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


    }
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public void CheckBluetooth(View view){

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth so app cannot function
            DialogFragment NotSupported = new NotSupported(); //NotSupported should be named with Fragment at the end
            NotSupported.show(getFragmentManager(), "NoSupport");

        }


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }   else  {
            Context context = getApplicationContext();
            CharSequence text = "Bluetooth Enabled and Working";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER, 0, 0);

            toast.show();
        }
    }

    // This checks to see if any bluetooth devices are already paired
    public void CheckPaired(View view){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //declare array list
        List<String> BTNameArrayList = new ArrayList<String>();
        List<String> BTMACArrayList = new ArrayList<String>();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                BTNameArrayList.add(deviceName);
                BTMACArrayList.add(deviceHardwareAddress);
            }
        }

        Object[] BTNameObject = BTNameArrayList.toArray();
        String[] BTNameStringArray = Arrays.copyOf(BTNameObject, BTNameObject.length, String[].class);

        Object[] BTMACObject = BTMACArrayList.toArray();
        String[] BTMACStringArray = Arrays.copyOf(BTMACObject, BTMACObject.length, String[].class);

        ListView listView;

        CustomListAdapter NamelistAdapter = new CustomListAdapter(this, BTNameStringArray);
        listView = (ListView) findViewById(R.id.BT_NameListView);
        listView.setAdapter(NamelistAdapter);

        CustomListAdapter MAClistAdapter = new CustomListAdapter(this, BTMACStringArray);
        listView = (ListView) findViewById(R.id.BT_MACListView);
        listView.setAdapter(MAClistAdapter);

    }
    public void LaunchDiscovery(View view){
        Intent LaunchDiscovery = new Intent(this, BluetoothConnect.class);
        startActivity(LaunchDiscovery);

    }
}

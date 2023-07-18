package com.motionball.motionball;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.List;

public class freeFall_DynamicGraph extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "DynamicGraph";

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

    // 0 for acceleration, 1 for speed, 2 for position.
    private int viewType;

    CountDownTimer dataTimer;

    //begin bluetooth block
    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;

    BluetoothConnectionService mBluetoothConnection;

    Button btnStartConnection;
    Button btnSend;
    Button btnGet;

    EditText etSend;
    EditText etGet;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;

    //used for enable disable
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // when discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE_OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE_TURNING_ON");
                        break;
                }
            }
        }
    };
    //used for discoverable
    /* //This is not needed in this context
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // when discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,mBluetoothAdapter.ERROR);

                switch(mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive: SCAN_MODE_CONNECTABLE_DISCOVERABLE, ABLE TO RECEIVE CONNECTIONS");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: SCAN_MODE_CONNECTABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: SCAN_MODE_NONE");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "onReceive: STATE_CONNECTING");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "onReceive: STATE_CONNECTED");
                        break;
                }
            }
        }
    };
    */
    //used for discovery of new devices
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive:Action found");
            // when discovery finds a device
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);

                Log.d(TAG, "onReceive"+device.getName()+": "+device.getAddress());
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,mBluetoothAdapter.ERROR);
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive:Action found");
            // when discovery finds a device
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    // bonded already
                    mBTDevice = mDevice;
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    //bonding in process
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    //no bond
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall__dynamic_graph);

        final Button TestReturn = findViewById(R.id.Graph_Return);
        TestReturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                finish();
            }
    });

        btnCollect = (Button) findViewById(R.id.btnCollect);
        btnClearData = (Button) findViewById(R.id.btnClearData);
        btnStop = (Button) findViewById(R.id.btnStop);

        int viewType = 0;

        TAScatterPlot = (GraphView) findViewById(R.id.graph);
        TAScatterPlot.setTitle("Acceleration vs Time");

        GridLabelRenderer TAGridLabel = TAScatterPlot.getGridLabelRenderer();
        TAGridLabel.setHorizontalAxisTitle("t(ms)");
        TAGridLabel.setVerticalAxisTitle("a(m/s^2)");


        TAValueArray = new ArrayList<>();

        tArrayList = new ArrayList<String>();
        aArrayList = new ArrayList<String>();

        init();
        //begin bluetooth block

        Button btnONOFF = (Button) findViewById(R.id.btnONOFF);
        //Button btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        // data view not needed since its going straight to graph, BTConnect can be used to to test
        //btnSend = (Button) findViewById(R.id.btnSend);
        //btnGet = (Button) findViewById(R.id.btnGet);

        //etSend = (EditText) findViewById(R.id.editText);
        //etGet = (EditText) findViewById(R.id.editTextGet);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4,filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(freeFall_DynamicGraph.this);

        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConnection();
                Toast.makeText(getApplicationContext(), "Connect Success",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy(){
        //Log.d(TAG, "ondestroy called");
        super.onDestroy();
        try {
            unregisterReceiver(mBroadcastReceiver1);
        }catch(IllegalArgumentException e){}
        try {
            unregisterReceiver(mBroadcastReceiver3);
        }catch(IllegalArgumentException e){}
        try {
            unregisterReceiver(mBroadcastReceiver4);
        }catch(IllegalArgumentException e){}
    }


    private void init() {
     //   final Double[] A = new Double[4];
       // A[1] = 0.0;
      //  A[2] = 0.0;
      //  A[3] = -9.8;
        TASeries = new PointsGraphSeries<>();





        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TEdit.setText(String.valueOf(0.000));
                TAValueArray.clear();
                TAScatterPlot.removeAllSeries();
                TASeries = null;
                TASeries = new PointsGraphSeries<>();
            }
        });

        final CountDownTimer dataTimer = new CountDownTimer(10000, 500)
        {
            String TAIncString;
            List<String> TAIncList;
            List<String> TAIndivList;

            public void onTick(long millisUntilFinished) {
                double millisDouble = ((10000 - (millisUntilFinished)));

                ArrayList<Double> Ax = new ArrayList<>();
                ArrayList<Double> Ay = new ArrayList<>();
                ArrayList<Double> Az = new ArrayList<>();

                ArrayList<Double> Amag = new ArrayList<>();
                Double aSign = 1.0;

               String TAIncString = mBluetoothConnection.read();

                //TAIncString = "{-0.46,-3.32,9.04{-0.52,-3.32,9.01{-0.44,-3.36,9.05{-0.50,-3.30,8.98{-0.48,-3.33,9.04{-0.46,-3.32,9.04{-0.52,-3.32,9.01{-0.44,-3.36,9.05{-0.50,-3.30,8.98{-0.48,-3.33,9.04{-0.46,-3.32,9.04{-0.52,-3.32,9.01{-0.44,-3.36,9.05{-0.50,-3.30,8.98{-0.48,-3.33,9.04";
                TAIncList = Arrays.asList(TAIncString.split("\\{"));
                for(int i = 0; i<TAIncList.size(); i++) {
                    try {
                        TAIndivList = Arrays.asList((TAIncList.get(i + 1)).split(","));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        break;
                    }

                    try {
                        try {
                            Ax.add(Double.parseDouble(TAIndivList.get(0)));
                            Ay.add(Double.parseDouble(TAIndivList.get(1)));
                            Az.add(Double.parseDouble(TAIndivList.get(2)));
                        }catch(IndexOutOfBoundsException e){break;}

                    } catch (NumberFormatException e) {
                        Ax.add(9.8);
                        Ay.add(0.0);
                        Az.add(0.0);
                    }

                    try {
                        Amag.add(Math.sqrt(Ax.get(i) * Ax.get(i) + Ay.get(i) * Ay.get(i) + Az.get(i) * Az.get(i)));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        break;
                    }

                }
                try {
                    for (int j = 0; j < Amag.size(); j++) {
                        //the math here is millisinfturue -millisuntilfinished-(interval-100*j)

                            TAValueArray.add(new XYValue(millisDouble + 100 * j, -1.0*Amag.get(j)));

                        }
                    }catch(ArrayIndexOutOfBoundsException e){}
                Ax = null;
                Ay = null;
                Az = null;
                Amag = null;

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
                if(mBluetoothConnection != null) {
                    mBluetoothConnection.read();
                }
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
        // set manual X bounds
        TAScatterPlot.getViewport().setYAxisBoundsManual(true);
        TAScatterPlot.getViewport().setMinY(-20);
        TAScatterPlot.getViewport().setMaxY(20);

        TAScatterPlot.getViewport().setXAxisBoundsManual(true);
        TAScatterPlot.getViewport().setMinX(0);
        TAScatterPlot.getViewport().setMaxX(10000);

        // enable scaling and scrolling
        TAScatterPlot.getViewport().setScalable(true);
        TAScatterPlot.getViewport().setScalableY(true);

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
    // This handles the type buttons.
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Acceleration:
                if (checked)
                    viewType = 0;
                    break;
            case R.id.Speed:
                if (checked)
                    viewType = 1;
                    break;
            case R.id.Position:
                if (checked)
                    viewType = 2;
                    break;
        }
    }
    //Begin bluetooth method block
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        //initializes the connection
        mBluetoothConnection.startClient(device,uuid);
    }


    private void enableDisableBT() {
        if(mBluetoothAdapter == null){
            Log.d(TAG,"enableDisableBT: No BT capability");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);

            Toast.makeText(getApplicationContext(), "Bluetooth enabled",
                    Toast.LENGTH_SHORT).show();

        }
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);

            Toast.makeText(getApplicationContext(), "Bluetooth disabled",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void btnDiscover(View view) {
        Toast.makeText(getApplicationContext(), "Searching for devices",
                Toast.LENGTH_LONG).show();

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        // Need to request permissions in final version
        if(!mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();
        // button was clicked
        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            mBTDevices.get(position).createBond();
            mBTDevice = mBTDevices.get(position);
            mBluetoothConnection = new BluetoothConnectionService(freeFall_DynamicGraph.this);
        }
        Toast.makeText(getApplicationContext(), "Paired Successfully",
                Toast.LENGTH_SHORT).show();

    }
}


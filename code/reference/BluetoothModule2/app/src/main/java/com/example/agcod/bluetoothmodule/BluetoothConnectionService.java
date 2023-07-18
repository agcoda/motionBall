package com.example.agcod.bluetoothmodule;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            //this creates listening server socket
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
            } catch (IOException e) {

            }
            mServerSocket = tmp;

        }

        public void run() {

            BluetoothSocket socket = null;

            try {
                // this is a blockingn clal and will only return on a successful connection or exeption
                socket = mServerSocket.accept();
            } catch (IOException e) {
            }
            if (socket != null) {
                connected(socket, mDevice);
            }
        }

        public void cancel() {
            try {
                mServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            mDevice = device;
            deviceUUID = uuid;
        }


        public void run() {
            BluetoothSocket tmp = null;

            //Get a socket for connection with given device
            try {
                //tries to create insecure rfcomm socket
                tmp = mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
            }// could not create
            mSocket = tmp;
            //very memory intensive, must cancel after connection is made
            mBluetoothAdapter.cancelDiscovery();
            try {
                mSocket.connect();
            } catch (IOException e) {
                //close the socket
                try {
                    mSocket.close();
                } catch (IOException e1) {
                }
                //means this could not connect to the used UUID
            }
            connected(mSocket, mDevice);


        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
            }
        }


    }
    public synchronized void start(){
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth"
        , "Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }
// connected thread maintains the connection, sends data and receives data through IO streams
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket){
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss progress dialog when established
            mProgressDialog.dismiss();

            try {
                tmpIn   = mSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInStream = tmpIn;
            mOutStream = tmpOut;
        }
        public void run(){
            byte[] buffer = new byte[4]; //buffer store for the stream
            int bytes; //bytes returned from read
/*
            while(true){
                try {
                    bytes = mInStream.read(buffer);
                } catch (IOException e) {
                    break;
                }
                String incomingMessage = new String(buffer, 0, bytes);*/
            }

        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            try {
                mOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String read(){
            // Change the byte array size to match the incoming data here.

            byte[] buffer = new byte[20]; //buffer store for the stream
            int bytes; //bytes returned from read
            byte[] bufferclear = new byte[1024];

            try {
                bytes = mInStream.read(buffer);
            } catch (IOException e) {
                bytes = 0;
            }
            String incomingMessage = new String(buffer, 0, bytes);

            try {
                mInStream.read(bufferclear);
            } catch (IOException e) {

            }
            return incomingMessage;
        }
        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) {
            }
        }
    }
    private void connected(BluetoothSocket mSocket, BluetoothDevice mDevice){
        mConnectedThread = new ConnectedThread(mSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out){
        ConnectedThread r;
        mConnectedThread.write(out);

    }
    public String read(){
        ConnectedThread r;
        String value;
        value = mConnectedThread.read();
        return value;
    }
}

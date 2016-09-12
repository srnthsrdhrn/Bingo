package com.blackpanther.bingo;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ubuntu on 6/9/16.
 */
public class BluetoothService extends Service {
    private static final String NAME = "Arduino";
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    DataTransfer dataTransfer;
    boolean connected=false;
    AlertDialog dialog;
    Intent intent;
    View v;
    Activity activity;
    public BluetoothService(View ParentView,Activity activity){
        v= ParentView;
        this.activity = activity;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.intent= intent;
        return null;
    }
    public void ClientConnect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            if(bluetoothAdapter.isEnabled()) {

                if(!bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.startDiscovery();
                }
                Set<BluetoothDevice> paired_devices= bluetoothAdapter.getBondedDevices();
                if(!paired_devices.isEmpty()){
                    for(BluetoothDevice device: paired_devices){
                        listAdapter.add(device.getName()+"\n"+device.getAddress());
                    }
                    listAdapter.notifyDataSetChanged();
                    Snackbar snackbar =Snackbar.make(v,"If your Device is not displayed",Snackbar.LENGTH_LONG).
                            setAction("Click Here", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                                }
                            });
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();
                    builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String[] strings = listAdapter.getItem(i).split("\n");
                            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strings[1]);
                            new Client().execute(device);
                        }
                    });

                }else{
                    Snackbar.make(v,"No Paired Devices",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Click Here", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                                }
                            });
                }
                builder.setTitle("Paired Devices");
                dialog = builder.create();
                dialog.show();

            }else{
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(intent,1);
            }
        }else {
            Toast.makeText(this,"This Device Does not Support Bluetooth",Toast.LENGTH_SHORT).show();
        }


    }
    public void ServerConnect(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            if(bluetoothAdapter.isEnabled()) {
                new Server().execute(bluetoothAdapter);
            }else{
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(intent,1);
            }
        }else {
            Toast.makeText(this,"This Device Does not Support Bluetooth",Toast.LENGTH_SHORT).show();
        }


    }

    private class DataTransfer extends AsyncTask<Void,String,Void> {
        OutputStream outputStream;
        BluetoothSocket socket;
        InputStream inputStream;
        DataTransfer(BluetoothSocket socket ){

            try {
                this.socket = socket;
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int available=inputStream.available();
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()
                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = inputStream.read(buffer);
                        // Send the obtained bytes to the UI activity
                        String string = new String(buffer, 0,bytes);
                        publishProgress(string);
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (IOException e) {
                Toast.makeText(BluetoothService.this,"Connection Error, PLease check if the device is online",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }
        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {

            }
        }
        public void cancel() {
            try {
                if(socket!=null)
                    socket.close();
            } catch (IOException e) { }
        }
    }

    private class Server extends AsyncTask<BluetoothAdapter,Void,Void>{
        BluetoothServerSocket bluetoothServerSocket;
        @Override
        protected Void doInBackground(BluetoothAdapter... bluetoothAdapters) {
            try {
                bluetoothServerSocket=bluetoothAdapters[0].listenUsingInsecureRfcommWithServiceRecord(NAME,MY_UUID);
                BluetoothSocket socket;
                // Keep listening until exception occurs or a socket is returned
                while (true) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BluetoothService.this,"Bluetooth Hotspot Started",Toast.LENGTH_SHORT).show();
                            }
                        });
                        socket = bluetoothServerSocket.accept();
                    } catch (IOException e) {
                        Toast.makeText(BluetoothService.this,"Timed Out Try connecting again",Toast.LENGTH_LONG).show();
                        break;
                    }
                    // If a connection was accepted
                    if (socket != null) {
                        // Do work to manage the connection (in a separate thread)
                        manageConnectedSocket(socket);
                        try {
                            bluetoothServerSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BluetoothService.this,"Connected", Toast.LENGTH_LONG).show();
            }
        });
        connected=true;
        dataTransfer= new DataTransfer( mmSocket);
        dataTransfer.execute();

    }

    private class Client extends AsyncTask<BluetoothDevice,Void,Void>{
        @Override
        protected Void doInBackground(BluetoothDevice... bluetoothDevices) {
            try {
                BluetoothSocket Socket = bluetoothDevices[0].createRfcommSocketToServiceRecord(MY_UUID);
                // Cancel discovery because it will slow down the connection
                bluetoothAdapter.cancelDiscovery();

                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    Socket.connect();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and get out
                    try {
                        Socket.close();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BluetoothService.this,"Connection Error",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException closeException) { }
                    return null;
                }

                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(Socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

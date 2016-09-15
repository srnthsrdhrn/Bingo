package com.blackpanther.bingo;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ubuntu on 6/9/16.
 */
public class BluetoothService extends Service {
    Activity activity;
    Intent intent;
    boolean connected=false;
    DataTransfer dataTransfer;
    public BluetoothService(Activity activity) {
        this.activity= activity;
    }
    public void send(String msg ){
        dataTransfer.write(msg.getBytes());
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return null;
    }
    public void manageConnectedSocket(BluetoothSocket mmSocket) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(),"Connected", Toast.LENGTH_LONG).show();
            }
        });
        connected=true;
        dataTransfer= new DataTransfer( mmSocket);
        dataTransfer.execute();

    }
    public class DataTransfer extends AsyncTask<Void,String,Void> {
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
                Toast.makeText(activity.getApplicationContext(),"Connection Error, PLease check if the device is online",Toast.LENGTH_LONG).show();
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
}

package com.blackpanther.bingo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class GameChose extends AppCompatActivity {
    Button splayer,dplayer;
    private static final String NAME = "Bingo";
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    BluetoothService bluetoothService;
    AlertDialog dialog;
    int choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chose);
        startService(new Intent(GameChose.this,BluetoothService.class));
        bluetoothService= new BluetoothService(this);
        splayer = (Button) findViewById(R.id.single_player);
        dplayer = (Button) findViewById(R.id.double_player);
        splayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameChose.this,Gameplay.class));
            }
        });
        dplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameChose.this);
                String[] list = new String []{"Act as Server", "Act as Client"};
                builder.setSingleChoiceItems(list, 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                ServerConnect();
                                dialogInterface.dismiss();
                                choice=0;
                                break;
                            case 1:
                                ClientConnect();
                                dialogInterface.dismiss();
                                choice=1;
                                break;
                        }
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
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

                    Snackbar snackbar =Snackbar.make(findViewById(android.R.id.content),"If your Device is not displayed",Snackbar.LENGTH_LONG).
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
                    Snackbar.make(findViewById(android.R.id.content),"No Paired Devices",Snackbar.LENGTH_INDEFINITE)
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
                startActivityForResult(intent,1);
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
                startActivityForResult(intent,1);
            }
        }else {
            Toast.makeText(this,"This Device Does not Support Bluetooth",Toast.LENGTH_SHORT).show();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GameChose.this,"Bluetooth Hotspot Started",Toast.LENGTH_SHORT).show();
                            }
                        });
                        socket = bluetoothServerSocket.accept();
                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GameChose.this,"Timed Out Try connecting again",Toast.LENGTH_LONG).show();
                            }
                        });

                        break;
                    }
                    // If a connection was accepted
                    if (socket != null) {
                        // Do work to manage the connection (in a separate thread)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(GameChose.this,InputFragmentManager.class));
                            }
                        });
                        bluetoothService.manageConnectedSocket(socket);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GameChose.this,"Connection Error",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException closeException) { }
                    return null;
                }

                // Do work to manage the connection (in a separate thread)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(GameChose.this,InputFragmentManager.class));
                    }
                });
                bluetoothService.manageConnectedSocket(Socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(choice==0){
                ServerConnect();
            }else{
                ClientConnect();
            }
        }else{
            Toast.makeText(this,"Bluetooth Must be Turned on to connnect",Toast.LENGTH_LONG).show();
        }
    }

}

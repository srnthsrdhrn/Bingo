package com.blackpanther.bingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by singapore on 12-09-2016.
 */
public class InputFragmentManager extends AppCompatActivity{
    Button standard_board,manual_board,load_board;
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_fragment_manager_layout);
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();
        standard_board= (Button) findViewById(R.id.standard_board);
        manual_board = (Button) findViewById(R.id.manual_input);
        load_board = (Button) findViewById(R.id.load_saved_board);
        standard_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<5;i++)
                    for(int j=0;j<5;j++)
                        Boards.user[i][j]=i*5+j+1;
                startActivity(new Intent(InputFragmentManager.this,Gameplay.class));
            }
        });

        manual_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InputFragmentManager.this,ManualInputPage.class));
            }
        });
        load_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InputFragmentManager.this);
                final Cursor c = databaseHandler.GetData(db);
                if(c.moveToFirst()) {
                    builder.setSingleChoiceItems(c, 0, DatabaseHandler.GAME_NAME, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            c.move(i);
                           String msg= c.getString(c.getColumnIndex(DatabaseHandler.GAME_BOARD));
                            String[] values = msg.split("/");
                            String toast="";
                            for(int j=0;j<5;j++){
                                for(int k=0;k<5;k++) {
                                    Boards.user[j][k]= Integer.parseInt(values[j+k]);
                                    toast += values[j+k]+"\t";
                                }
                                toast+="\n";
                            }
                            Toast.makeText(InputFragmentManager.this,toast,Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setPositiveButton("Select Board", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                            startActivity(new Intent(InputFragmentManager.this,Gameplay.class));

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(InputFragmentManager.this,"No Saved Games",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

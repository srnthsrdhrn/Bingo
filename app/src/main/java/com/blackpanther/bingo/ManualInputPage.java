package com.blackpanther.bingo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ManualInputPage extends AppCompatActivity {

    public EditText[][] t= new EditText[5][5];
    Button StartGame,save;
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;
    EditText board_name;
    boolean flag =true;
    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        initialize();
        databaseHandler = new DatabaseHandler(ManualInputPage.this);
        db = databaseHandler.getWritableDatabase();
        StartGame = (Button) findViewById(R.id.Start);
        save = (Button) findViewById(R.id.save);
        board_name = (EditText) findViewById(R.id.board_name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    String board = "";
                    for (int i = 0; i < 5; i++)
                        for (int j = 0; j < 5; j++)
                            board = board + "/" + t[i][j].getText().toString();
                    String name = board_name.getText().toString();
                    databaseHandler.StoreData(db, name, board);
                    board_name.setText("");
                    flag=false;
                    Toast.makeText(ManualInputPage.this, "Board Saved", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(ManualInputPage.this,"Board Already Saved",Toast.LENGTH_SHORT).show();
                }
            }
        });
        StartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<5;i++)
                    for(int j=0;j<5;j++)
                        Boards.user[i][j]=Integer.parseInt(t[i][j].getText().toString());
                startActivity(new Intent(ManualInputPage.this,Gameplay.class));
            }
        });
    }

    private void initialize(){
        t[0][0] = (EditText) findViewById(R.id.editText1);
        t[0][1] = (EditText) findViewById(R.id.editText2);
        t[0][2] = (EditText) findViewById(R.id.editText3);
        t[0][3] = (EditText) findViewById(R.id.editText4);
        t[0][4] = (EditText) findViewById(R.id.editText5);
        t[1][0] = (EditText) findViewById(R.id.editText6);
        t[1][1] = (EditText) findViewById(R.id.editText7);
        t[1][2] = (EditText) findViewById(R.id.editText8);
        t[1][3] = (EditText) findViewById(R.id.editText9);
        t[1][4] = (EditText) findViewById(R.id.editText10);
        t[2][0] = (EditText) findViewById(R.id.editText11);
        t[2][1] = (EditText) findViewById(R.id.editText12);
        t[2][2] = (EditText) findViewById(R.id.editText13);
        t[2][3] = (EditText) findViewById(R.id.editText14);
        t[2][4] = (EditText) findViewById(R.id.editText15);
        t[3][0] = (EditText) findViewById(R.id.editText16);
        t[3][1] = (EditText) findViewById(R.id.editText17);
        t[3][2] = (EditText) findViewById(R.id.editText18);
        t[3][3] = (EditText) findViewById(R.id.editText19);
        t[3][4] = (EditText) findViewById(R.id.editText20);
        t[4][0] = (EditText) findViewById(R.id.editText21);
        t[4][1] = (EditText) findViewById(R.id.editText22);
        t[4][2] = (EditText) findViewById(R.id.editText23);
        t[4][3] = (EditText) findViewById(R.id.editText24);
        t[4][4] = (EditText) findViewById(R.id.editText25);
        InitializePcBoard();

    }
    private void InitializePcBoard(){
        Boards.pc[0][0]=20;Boards.pc[0][1]=19;Boards.pc[0][2]=18;Boards.pc[0][3]=17;Boards.pc[0][4]=16;
        Boards.pc[1][0]=21;Boards.pc[1][1]=22;Boards.pc[1][2]=23;Boards.pc[1][3]=24;Boards.pc[1][4]=25;
        Boards.pc[2][0]=11;Boards.pc[2][1]=12;Boards.pc[2][2]=13;Boards.pc[2][3]=14;Boards.pc[2][4]=15;
        Boards.pc[3][0]=10;Boards.pc[3][1]=9;Boards.pc[3][2]=8;Boards.pc[3][3]=7;Boards.pc[3][4]=6;
        Boards.pc[4][0]=1;Boards.pc[4][1]=2;Boards.pc[4][2]=3;Boards.pc[4][3]=4;Boards.pc[4][4]=5;
    }

}

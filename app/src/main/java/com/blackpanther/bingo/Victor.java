package com.blackpanther.bingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Victor extends AppCompatActivity {
    EditText[][] t= new EditText[5][5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victor);
        TextView result = (TextView)findViewById(R.id.textView);
        if(Gameplay.victor==1)
            result.setText("You have won over the AI!\n The computer's Board");
        else if(Gameplay.victor==0)
            result.setText("The AI won over you\n The computer's Board");
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
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                t[i][j].setText(String.valueOf(InputPage.pc[i][j]));
    }
    public void reset(View v){
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
        InputPage.user[i][j]=0;
        startActivity(new Intent(Victor.this,InputPage.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_victor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

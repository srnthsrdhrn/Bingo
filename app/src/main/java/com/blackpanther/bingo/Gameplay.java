package com.blackpanther.bingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Gameplay extends AppCompatActivity {
    public  Button[][] b = new Button[5][5];
    public static int victor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gameplay, menu);



        b[0][0]=(Button)findViewById(R.id.button1);
        b[0][1]=(Button)findViewById(R.id.button2);
        b[0][2]=(Button)findViewById(R.id.button3);
        b[0][3]=(Button)findViewById(R.id.button4);
        b[0][4]=(Button)findViewById(R.id.button5);
        b[1][0]=(Button)findViewById(R.id.button6);
        b[1][1]=(Button)findViewById(R.id.button7);
        b[1][2]=(Button)findViewById(R.id.button8);
        b[1][3]=(Button)findViewById(R.id.button9);
        b[1][4]=(Button)findViewById(R.id.button10);
        b[2][0]=(Button)findViewById(R.id.button11);
        b[2][1]=(Button)findViewById(R.id.button12);
        b[2][2]=(Button)findViewById(R.id.button13);
        b[2][3]=(Button)findViewById(R.id.button14);
        b[2][4]=(Button)findViewById(R.id.button15);
        b[3][0]=(Button)findViewById(R.id.button16);
        b[3][1]=(Button)findViewById(R.id.button17);
        b[3][2]=(Button)findViewById(R.id.button18);
        b[3][3]=(Button)findViewById(R.id.button19);
        b[3][4]=(Button)findViewById(R.id.button20);
        b[4][0]=(Button)findViewById(R.id.button21);
        b[4][1]=(Button)findViewById(R.id.button22);
        b[4][2]=(Button)findViewById(R.id.button23);
        b[4][3]=(Button)findViewById(R.id.button24);
        b[4][4]=(Button)findViewById(R.id.button25);

        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++) {
                b[i][j].setText(String.valueOf(InputPage.user[i][j]));
            }
        return true;
    }
    public void onButtonClick(View v){
        boolean flag=false;
        int temp= 0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                int i1 = v.getId();
                if (i1 == b[i][j].getId()) {
                    temp = InputPage.user[i][j];
                    InputPage.user[i][j] = 0;
                    b[i][j].setText("");
                    flag=true;
                    break;
                }
                if(flag)
                    break;
            }
        }
        flag=false;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                if (InputPage.pc[i][j] == temp) {
                    InputPage.pc[i][j] = 0;
                    flag = true;
                    break;
                }
            }
            if(flag)
                break;

        }
        flag=false;
        temp=0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++)
                if (InputPage.pc[i][j] != 0) {
                    temp = InputPage.pc[i][j];
                    InputPage.pc[i][j] = 0;
                    flag=true;
                    break;
                }
            if(flag)
                break;
        }
        flag=false;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++)
                if (InputPage.user[i][j] == temp) {
                    InputPage.user[i][j] = 0;
                    b[i][j].setText(" ");
                    flag=true;
                    break;
                }
            if(flag)
                break;
        }
        int count2=0,count1,count3;
        //checking user's board
        for(int i=0;i<5;i++)
        {
            count1=0;
            for(int j=0;j<5;j++)
            {
                if(InputPage.user[i][j]==0)
                    count1++;
            }
            if(count1==5)
            {
                count2++;
            }
        }
        for(int i=0;i<5;i++)
        {
            count3=0;
            for(int j=0;j<5;j++)
            {
                if(InputPage.user[j][i]==0)
                    count3++;
            }
            if(count3==5)
            {
                count2++;
            }
        }
        //checking computer's board
        int count04=0,count01,count03;
        for(int i=0;i<5;i++)
        {
            count01=0;
            for(int j=0;j<5;j++)
            {
                if(InputPage.pc[i][j]==0)
                    count01++;
            }
            if(count01==5)
            {
                count04++;
            }
        }
        for(int i=0;i<5;i++)
        {
            count03=0;
            for(int j=0;j<5;j++)
            {
                if(InputPage.pc[j][i]==0)
                    count03++;
            }
            if(count03==5)
            {
                count04++;
            }
        }

        if(count2==5)
        {
          victor=1;
          startActivity(new Intent(Gameplay.this,Victor.class));
        }
        else if(count04==5)
        {
           victor=0;
            startActivity(new Intent(Gameplay.this,Victor.class));
        }
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

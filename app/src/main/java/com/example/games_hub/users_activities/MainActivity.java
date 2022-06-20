package com.example.games_hub.users_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.games_hub.Data;
import com.example.games_hub.game.gameActivites.GameRoom;
import com.example.games_hub.R;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.Users.User;
import com.example.games_hub.server.ServerConnector;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _this = this;
        Data.doAnimation(findViewById(R.id.noInterner),R.anim.hide,this);
        //System.out.println(ServerConnector.getText(ServerLinks.getUserListURL()));
    }
    Context _this;
    boolean noInternet;
    @Override
    protected void onResume() {
        super.onResume();
        if(DataMeneger.getUserCode(getApplicationContext()).equals(""))
        {
            if(DataMeneger.getIfFirstTime(getApplicationContext()))
            {
                DataMeneger.cancelFirstTime(getApplicationContext());
                DataMeneger.setPage(Singup.class.toString(),getApplicationContext());
                startActivity(new Intent(MainActivity.this,Singup.class));
            }
            else
            {
                DataMeneger.setPage(Login.class.toString(),getApplicationContext());
                startActivity(new Intent(MainActivity.this,Login.class));
            }
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
        }
        else
        {
            noInternet = true;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final String data = ServerConnector.getText(DataMeneger.getUserUrl(getApplicationContext()));
                    if(!data.equals("ul"))
                    {
                        noInternet = false;
                        User user = new User(data,DataMeneger.getUserUrl(getApplicationContext()));
                        DataMeneger.saveUser(user,DataMeneger.getUserPassword(getApplicationContext()),getApplicationContext());
                        moveToUserPage();
                    }
                    else
                    {
                        noInternetAn();
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            Data.doAnimation(findViewById(R.id.logo),R.anim.wait_anim,this);
        }
        
    }
    private  void noInternetAn()
    {
        ((Button)(findViewById(R.id.tryAgainB))).setClickable(true);
        findViewById(R.id.logo).setVisibility(View.INVISIBLE);
        Data.doAnimation(findViewById(R.id.logo),R.anim.no_anim,this);
        Data.doAnimation(findViewById(R.id.noInterner),R.anim.no_anim,this);
    }
    private void moveToUserPage()
    {
        User user = DataMeneger.getUser(getApplicationContext());
        final String change = user.checkVersion(getApplicationContext(),this);
        if(change.equals(""))
        {
            DataMeneger.setPage(GameRoom.class.toString(),getApplicationContext());
            startActivity(new Intent(MainActivity.this,GameRoom.class));
        }
        else
        {
            DataMeneger.setUserVersionUpdateOrder(true,getApplicationContext());
            DataMeneger.setPage(UserPage.class.toString(),getApplicationContext());
            startActivity(new Intent(MainActivity.this,UserPage.class));
        }
        DataMeneger.setMoveAnimations(R.anim.no_anim,R.anim.no_anim,getApplicationContext());
    }

    public void tryAgain(View w)
    {
        startActivity(new Intent(MainActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.page_from_right,R.anim.page_to_left);
    }

}
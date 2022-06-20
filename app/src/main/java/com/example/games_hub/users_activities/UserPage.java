package com.example.games_hub.users_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.game.gameActivites.GameRoom;
import com.example.games_hub.server.ServerConnector;

public class UserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        updateUser();
    }
    User _user;
    ServerConnector serverConnector;
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = Data.getPageCheck(UserPage.this,getApplicationContext());
        if(i != null)
        {
            startActivity(i);
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
        }
        else
        {
            int[] ans = DataMeneger.getMoveAnimations(getApplicationContext());
            overridePendingTransition(ans[0],ans[1]);
            DataMeneger.setMoveAnimations(R.anim.no_anim,R.anim.no_anim,getApplicationContext());
        }
    }
    void updateUser()
    {
        final User user = DataMeneger.getUser(getApplicationContext());
        _user = user;
        TextView name = findViewById(R.id.userName);
        TextView level = findViewById(R.id.userlevel);
        name.setText(user.getUserName());
        level.setText("" + user.getLevel());
        System.out.println(user.toString());

        updateEco(findViewById(R.id.gemsNumber),findViewById(R.id.coinNumber),user);
        if(DataMeneger.getUserVersionUpdateOrder(getApplicationContext()))
        {
            serverConnector = new ServerConnector(user.getUrl(),getApplicationContext());
            View mainView = findViewById(R.id.userMenu);
            View updateView = findViewById(R.id.userUpdateMenu);
            mainView.setVisibility(View.INVISIBLE);
            Data.doAnimation(mainView,R.anim.dim_out_s,this);
            updateView.setVisibility(View.VISIBLE);
            Data.doAnimation(updateView,R.anim.dim_in_s,this);
        }
    }

    public void logOut(View v)
    {
        DataMeneger.setPage(Login.class.toString(),getApplicationContext());
        DataMeneger.logOutUser(getApplicationContext());
        startActivity(new Intent(UserPage.this,MainActivity.class));
        DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
    }

    public static void updateEco(TextView gems, TextView coins, User _user)
    {
        gems.setText("" + _user.getGems());
        coins.setText("" + _user.getCoins());
    }

    public void userUpdate(View v)
    {
        if(serverConnector.getLoaded())
        {
            serverConnector.setText(_user.getCode());
            View mainView = findViewById(R.id.userMenu);
            View updateView = findViewById(R.id.userUpdateMenu);
            mainView.setVisibility(View.VISIBLE);
            Data.doAnimation(mainView,R.anim.dim_in_f,this);
            updateView.setVisibility(View.INVISIBLE);
            Data.doAnimation(updateView,R.anim.dim_out_f,this);
            DataMeneger.setUserVersionUpdateOrder(false,getApplicationContext());
        }
        else
            Data.makeToast("no connection try again",this);
    }

    public void moveToGameRoom(View v)
    {
        DataMeneger.setPage(GameRoom.class.toString(),getApplicationContext());
        DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
        startActivity(new Intent(UserPage.this,GameRoom.class));
    }
}
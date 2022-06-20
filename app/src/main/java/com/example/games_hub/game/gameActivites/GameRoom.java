package com.example.games_hub.game.gameActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.game.games.chess.ChessHome;
import com.example.games_hub.users_activities.UserPage;

public class GameRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);
        _this = this;
        count = 0;
        final int games = 1;
        links = new Class[games];
        views = new View[games];
        addLink(getResources().getDrawable(R.drawable.chess_background), "chess", ChessHome.class);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = Data.getPageCheck(GameRoom.this,getApplicationContext());
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
        User user = DataMeneger.getUser(getApplicationContext());
        updateEco(findViewById(R.id.gemsNumber),findViewById(R.id.coinNumber),user);
    }

    Context _this;
    int count;
    Class[] links;
    View[] views;
    public void addLink(Drawable backRound, String name, Class page)
    {

        LinearLayout layout = findViewById(R.id.linearLayout);
        LinearLayout border = new LinearLayout(this);
        RelativeLayout.LayoutParams borderLay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,250);
        borderLay.setMargins(30,30,30,30);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            border.setForeground(backRound);
        }
        TextView title = new TextView(this);
        title.setText(name);
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        border.addView(title, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        border.setOnClickListener(buttonL);
        border.setLayoutParams(borderLay);
        layout.addView(border);

        views[count] = border;
        links[count] = page;
        count++;
    }
    View.OnClickListener buttonL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int num = -1;
            for(int i = 0; i < links.length; i++)
            {
                if(view.equals(views[i]))
                {
                    num = i;
                    break;
                }
            }
            if(num == -1)
                Data.makeToast("unknown game -1",_this);
            else
            {
                DataMeneger.setPage(links[num].toString(),getApplicationContext());
                DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
                startActivity(new Intent(GameRoom.this,links[num]));
            }
        }
    };
    public static void updateEco(TextView gems, TextView coins, User _user)
    {
        gems.setText("" + _user.getGems());
        coins.setText("" + _user.getCoins());
    }

    public void moveToUser(View view)
    {
        DataMeneger.setPage(UserPage.class.toString(),getApplicationContext());
        DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
        startActivity(new Intent(GameRoom.this,UserPage.class));
    }
}
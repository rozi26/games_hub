package com.example.games_hub.game.gameActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.game.games.chess.ChessBoard;
import com.example.games_hub.game.games.chess.ChessData;
import com.example.games_hub.game.games.chess.ChessHome;

public class GameList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_game_list);
        User user = DataMeneger.getUser(getApplicationContext());
        final int gameLength = DataMeneger.getGameCodeGameLength(getApplicationContext());
        games = new GameListGame[gameLength];
        views = new View[gameLength];
        for(int i = 0; i < gameLength; i++)
        {
            final String code = DataMeneger.getGameCodeGameCode(i,getApplicationContext());
            final boolean start = DataMeneger.getGameCodeGameStart(i,getApplicationContext());
            StringBuilder playerARank = new StringBuilder("");
            StringBuilder playerBRank = new StringBuilder("");
            StringBuilder playerBName = new StringBuilder("");
            StringBuilder date = new StringBuilder("");
            StringBuilder gameCode = new StringBuilder("");
            StringBuilder gameReport = new StringBuilder("");
            //System.out.print(code);
            int spaceCount = 0;
            for(int g = 0; g < code.length(); g++)
            {
                final char a = code.charAt(g);
                if(a == ' ')
                    spaceCount++;
                else if(spaceCount == 0)
                    playerARank.append(a);
                else if(spaceCount == 1)
                    playerBName.append(a);
                else if(spaceCount == 2)
                    playerBRank.append(a);
                else if(spaceCount == 3)
                    date.append(a);
                else if(spaceCount == 4)
                    gameCode.append(a);
                else
                    gameReport.append(a);
            }
            games[i] = new GameListGame(user.getUserName(),playerARank.toString(),playerBName.toString(),playerBRank.toString(),date.toString(), gameCode.toString(),start,gameReport.toString(), i);;
        }
        final LinearLayout layout = findViewById(R.id.linear);
        for(int i = gameLength - 1; i >= 0; i--)
        {
            RelativeLayout r = getBox(games[i]);
            views[i] = r;
            layout.addView(r);
        }

        if(gameLength > 8)
        {
            layout.setBackground(getResources().getDrawable(R.drawable.b1));
            findViewById(R.id.scroolView).setBackground(getResources().getDrawable(R.color.bastBlue));
        }
    }
    View[] views;
    GameListGame[] games;
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = Data.getPageCheck(GameList.this,getApplicationContext());
        if(i != null)
        {
            startActivity(i);
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
        }
        else
        {
            int[] ans = DataMeneger.getMoveAnimations(getApplicationContext());
            overridePendingTransition(ans[0],ans[1]);

            //let move to log
            DataMeneger.setPage(GameRoom.class.toString(),getApplicationContext());
            DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
        }
    }

    @SuppressLint("NewApi")
    private RelativeLayout getBox(GameListGame a)
    {

        RelativeLayout r = new RelativeLayout(this);
        r.setOnClickListener(ButtonC);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        rp.setMargins(30,30,30,0);
        r.setLayoutParams(rp);
        r.setBackground(getResources().getDrawable(R.drawable.round_textview_d));
        TextView playerAName = new TextView(this);
        TextView playerBName = new TextView(this);
        RelativeLayout.LayoutParams playerANP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams playerBNP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playerANP.setMargins(60,30,30,15);
        playerBNP.setMargins(30,30,60,15);
        playerBNP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        playerAName.setLayoutParams(playerANP);
        playerBName.setLayoutParams(playerBNP);
        playerAName.setText(a.playerAName);
        playerBName.setText(a.playerBName);
        playerAName.setTextSize(30);
        playerBName.setTextSize(30);
        playerAName.setTextColor(Color.BLACK);
        playerBName.setTextColor(Color.BLACK);
        playerAName.setId(View.generateViewId());
        playerBName.setId(View.generateViewId());
        r.addView(playerAName);
        r.addView(playerBName);
        TextView vs = new TextView(this);
        RelativeLayout.LayoutParams vsF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vsF.setMargins(0,30,0,0);
        vsF.addRule(RelativeLayout.LEFT_OF,playerBName.getId());
        vsF.addRule(RelativeLayout.RIGHT_OF,playerAName.getId());
        vs.setLayoutParams(vsF);
        vs.setText("VS");
        vs.setTextColor(Color.BLACK);
        vs.setTextSize(33);
        vs.setGravity(Gravity.CENTER);


        r.addView(vs);

        TextView date = new TextView(this);
        RelativeLayout.LayoutParams dateF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateF.addRule(RelativeLayout.BELOW,playerAName.getId());
        dateF.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        dateF.setMargins(30,15,15,15);
        date.setLayoutParams(dateF);
        date.setText(a.date);
        date.setTextColor(Color.BLACK);
        date.setTextSize(15);
        r.addView(date);
        TextView winner = new TextView(this);
        RelativeLayout.LayoutParams winnerF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        winnerF.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        winnerF.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        winnerF.addRule(RelativeLayout.BELOW,playerAName.getId());
        winnerF.setMargins(15,15,30,15);
        winner.setLayoutParams(winnerF);
        winner.setText(getWinnerText(a.code,a.getPlayAsWhite()));
        winner.setTextColor(Color.BLACK);
        winner.setTextSize(15);
        r.addView(winner);

        if(!a.getGameReport().equals(""))
        {
            TextView reviewText = new TextView(this);
            reviewText.setBackgroundColor(Color.rgb(10,91,254));
            RelativeLayout.LayoutParams frameLayoutF = new RelativeLayout.LayoutParams(300, 45);
            //frameLayoutF.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            frameLayoutF.setMargins(-60,15,0,0);
            reviewText.setLayoutParams(frameLayoutF);
            reviewText.setRotation(330);
            reviewText.setText("reviewed  ");
            reviewText.setTextSize(9f);
            reviewText.setGravity(Gravity.CENTER_HORIZONTAL);
            reviewText.setTextColor(Color.WHITE);
            r.addView(reviewText);
        }

        return r;
    }




    public static class GameListGame
    {
        final String playerAName;
        final String playerARank;
        final String playerBName;
        final String playerBRank;
        final String date;
        final String code;
        final String gameReport;
        final int index;
        boolean playAsWhite;

        public GameListGame(String _playerAName, String _playerARank, String _playerBName, String _playerBRank, String _date, String _code, boolean _playAsWhite, String _gameReport, int _index)
        {
            playerAName = _playerAName;
            playerARank = _playerARank;
            playerBName = _playerBName;
            playerBRank = _playerBRank;
            date = _date;
            code = _code;
            playAsWhite = _playAsWhite;
            gameReport = _gameReport;
            index = _index;
        }
        public String getPlayerAName()
        {
            return playerAName;
        }
        public String getPlayerARank()
        {
            return playerARank;
        }
        public String getPlayerBName()
        {
            return playerBName;
        }
        public String getPlayerBRank()
        {
            return  playerBRank;
        }
        public String getcode()
        {
            return code;
        }
        public String getDate()
        {
            return date;
        }
        public boolean getPlayAsWhite()
        {
            return playAsWhite;
        }

        public String getGameReport() {return  gameReport;}
        public int[] getGameReportInt()
        {
            if(gameReport.equals(""))
                return null;
            int[] list = new int[gameReport.length()];
            for(int i = 0; i < gameReport.length(); i++)
            {
                list[i] = Integer.parseInt(Character.toString(gameReport.charAt(i)));
            }
            return list;
        }
        public int getIndex()
        {
            return index;
        }

        public String toString()
        {
            return "code: " + code + "\nreport: " + gameReport;
        }
    }




    public void moveBack(View v)
    {
        moveBack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveBack();
    }
    void moveBack()
    {
        DataMeneger.setPage(ChessHome.class.toString(),getApplicationContext());
        DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
        Class c;

        startActivity(new Intent(GameList.this,ChessHome.class));
    }

    String getWinnerText(String code, boolean white)
    {
        final int gameCode = DataMeneger.getGameCode(getApplicationContext());
        int result = -1;
        if(gameCode == 1) // chess
            result = ChessData.getChessWinner(code);
        else
            return "error " + gameCode + " isn't real";
        if(!white)
            result = -result;
        if(result == -1)
            return "you lost";
        else if(result == 0)
            return  "draw";
        else if(result == 1)
            return  "you won";
        else
            return "error result = " + result;
    }

    View.OnClickListener ButtonC = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int number = -1;
            for(int i = 0; i < views.length; i++)
            {
                if(views[i].equals(view))
                {
                    number = i;
                    break;
                }
            }
            GameListGame game = games[number];
            DataMeneger.setWatchMode(true,getApplicationContext());
            DataMeneger.setWatchGame(game,getApplicationContext());
            DataMeneger.setPage(ChessBoard.class.toString(),getApplicationContext());
            startActivity(new Intent(GameList.this,ChessBoard.class));
            overridePendingTransition(R.anim.page_from_left,R.anim.page_to_right);
        }
    };
}
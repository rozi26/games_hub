package com.example.games_hub.game.games.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.game.gameActivites.GameList;
import com.example.games_hub.game.gameActivites.GameRoom;
import com.example.games_hub.server.ServerConnector;
import com.example.games_hub.server.ServerLinks;

public class ChessHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_home);
        User user = DataMeneger.getUser(getApplicationContext());
        ((TextView)findViewById(R.id.userName)).setText(user.getUserName());
        ((TextView)findViewById(R.id.userLevel)).setText("" + user.getChessRank());
        _this = this;
        modes = new mode[]
        {
                new mode(5,false,"chess vs computer",20),
                new mode(5,false,"chess vs computer",(int)Math.sqrt(user.getChessRank())),
                new mode(25,false,"chess vs computer",(int)(Math.sqrt(user.getChessRank()) * 1.02)),
                new mode(5,true,"online chess",0),
                new mode(25,true,"online chess",0),
                new mode(125,true,"online chess",0)
        };
        chessWaiting = new ServerConnector(ServerLinks.getChessWaitingServer(),getApplicationContext());
        views = new View[modes.length];
        LinearLayout l = findViewById(R.id.modesList);
        for(int i = 0; i < modes.length; i++)
        {
            RelativeLayout r = modes[i].getModeView();
            views[i] = r;
            r.setOnClickListener(buttonC);
            l.addView(r);
        }
        CreatedRequest = false;
        testRequestCancel();
    }
    mode[] modes;
    ServerConnector chessWaiting;
    View[] views;
    Context _this;
    @Override
    protected void onResume() {
        super.onResume();
        testRequestCancel();
        if(DataMeneger.getFakeEnter(getApplicationContext()))
        {
            DataMeneger.setFakeEnter(false,getApplicationContext());
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
            Data.doAnimation(findViewById(R.id.requestWait),R.anim.out_to_left_s,this);
        }
        else
        {
            Intent i = Data.getPageCheck(ChessHome.this,getApplicationContext());
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
    }
    private class mode
    {
        private final int price;
        private final boolean online;
        private final String text;
        private final int level;
        private mode(int _price,boolean _online, String _text, int _level)
        {
            price = _price;
            online = _online;
            text = _text;
            level = _level;
        }
        private RelativeLayout getModeView()
        {
            return Data.getGameTub(_this,price,online, getResources().getDrawable(R.drawable.round_textview_d),getResources().getDrawable(R.drawable.round_textview_l),text);
        }
    }
    boolean CreatedRequest;

    public void nameClick(View v)
    {
        Data.makeToast("your name",this);
    }
    public void levelClick(View v)
    {
        Data.makeToast("your chess rank",this);
    }

    View.OnClickListener buttonC = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int num = -1;
            for(int i = 0; i < views.length; i++)
            {
                if(views[i].equals(view))
                {
                    num = i;
                    break;
                }
            }
            mode m = modes[num];
            final User user = DataMeneger.getUser(getApplicationContext());
            if(m.price <= user.getCoins())
            {
                DataMeneger.setGameBat(m.price,getApplicationContext());
                if(m.online)
                {
                    boolean connectingError = false;
                    if(chessWaiting.getLoaded() && !CreatedRequest)
                    {
                        final int range = 1000;
                        final String waiters = ServerConnector.getText(chessWaiting.getUrl());
                        if(!waiters.equals("ul"))
                        {
                            boolean found = enterRequest(waiters,range);

                            if(!found) // create new request
                            {
                                DataMeneger.setFakeEnter(false,getApplicationContext());
                                CreatedRequest = true;
                                Data.makeToast("creating request",_this);
                                final String gameURL = ServerLinks.getNewURL(8);
                                String request = "A" + user.getChessRank() + " " + user.getUserName() + " " + gameURL + "\to";
                                chessWaiting.addText(request);
                                requestStart = waiters.length();
                                waitForAnswer(waiters.length(),ServerLinks.getChessWaitingServer(),new Data.GameRequest(user.getChessRank(),user.getUserName(),gameURL,true,true));
                            }
                        }
                    }
                    if(!chessWaiting.getLoaded() || connectingError)
                    {
                        Runnable whiteForLoading = new Runnable() {
                            @Override
                            public void run() {
                                int count = 0;
                                while (true)
                                {
                                    try {Thread.sleep(100);}
                                    catch (Exception e){}
                                    if(chessWaiting.getLoaded() || count > 50)
                                        break;
                                    else
                                        count++;
                                }
                                final int counter = count;
                                Runnable note = new Runnable() {
                                    @Override
                                    public void run() {
                                        if(counter > 50)
                                        {
                                            Data.makeToast("you don't have internet",_this);
                                            findViewById(R.id.logo).setVisibility(View.VISIBLE);
                                            Data.doAnimation(findViewById(R.id.logo),R.anim.out_from_right_s,_this);
                                            findViewById(R.id.requestWait).setVisibility(View.INVISIBLE);
                                            Data.doAnimation(findViewById(R.id.requestWait),R.anim.out_to_left_s,_this);
                                        }
                                        else
                                        {
                                            Data.makeToast("done",_this);
                                            onClick(view);
                                        }
                                    }
                                };
                                runOnUiThread(note);
                            }
                        };
                        ((TextView)findViewById(R.id.requestWaitText)).setText("connecting...");
                        showWaitingLogo();
                        Thread thread = new Thread(whiteForLoading);
                        thread.start();
                    }
                }
                else
                {
                    Data.GameRequest request = new Data.GameRequest(m.level,"computer","",true,false);
                    moveToRequest(request);
                }
            }
            else
                Data.makeToast("you don't have enough coins",_this);
        }
    };
    private boolean enterRequest(String waiters, int range)
    {
        Data.GameRequest request = lookForRequestWithName(waiters,range,"",true);
        if(request != null)
        {
            moveToRequest(request);
            return true;
        }
        return false;
        /*User user = DataMeneger.getUser(getApplicationContext());
        StringBuilder waitersBuilder = new StringBuilder(waiters);
        boolean newLine = true;
        boolean namePass = false;
        StringBuilder name = new StringBuilder();
        StringBuilder url = new StringBuilder();
        int rank = -1;
        int markIndex = -1;
        boolean found = false;
        for(int i = 0; i < waiters.length(); i++) // try to find existing request
        {
            final char a = waiters.charAt(i);
            if(newLine)
            {
                boolean skipRequest =  false;
                if(a != 'A') // the request is private
                {
                    skipRequest = true;
                }
                else // the request is public
                {
                    markIndex = i;
                    i++;
                    StringBuilder rankS = new StringBuilder();
                    while (waiters.charAt(i) != ' ')
                    {
                        rankS.append(waiters.charAt(i));
                        i++;
                    }
                    rank = Integer.parseInt(rankS.toString());
                    skipRequest = !Data.inRange(user.getChessRank(),rank,range);
                }
                if(!skipRequest)
                {
                    newLine = false;
                }

                if(skipRequest) // skip this request
                {
                    while (!(waiters.charAt(i) == '\t' && waiters.charAt(i+1) == 'o')) //skip this request
                    {
                        i++;
                    }
                    i++;
                }
            }
            else if(a == '\t' && waiters.charAt(i + 1) == 'o') // request is over
            {
                DataMeneger.setFakeEnter(false,getApplicationContext());
                Data.GameRequest request = new Data.GameRequest(rank,name.toString(),url.toString(),false,true);
                Data.makeToast("entering request " + request.toString(),_this);
                waitersBuilder.setCharAt(markIndex,'S');
                chessWaiting.setText(waitersBuilder.toString());
                moveToRequest(request);
                return true;
            }
            else
            {
                if(a == ' ')
                    namePass = true;
                else if(namePass)
                    url.append(a);
                else
                    name.append(a);
            }
        }
        return false;*/
    }
    int requestStart;
    private void waitForAnswer(int requestStart, String url, Data.GameRequest request)
    {
        ((TextView)findViewById(R.id.requestWaitText)).setText("looking for players");
        showWaitingLogo();
        Runnable lookFor = new Runnable() {
            @Override
            public void run() {
                boolean firstRun = true;
                while (true)
                {
                    try {Thread.sleep(100);} catch (Exception e){}
                    if(requestStart == -1)
                        break;
                    else
                    {
                        final String text = chessWaiting.getText();
                        if(text.length() > requestStart && text.charAt(requestStart) != 'A')
                        {
                            if(text.charAt(requestStart) == 'S')
                            {
                                CreatedRequest = false;
                                moveToRequest(request);
                            }
                            break;
                        }
                        else if(firstRun && text.length() > requestStart) // there missing request
                        {
                            firstRun = false;
                            Data.GameRequest myRequest = lookForRequestWithName(text,1000,DataMeneger.getUser(getApplicationContext()).getUserName(),false);
                            if(myRequest == null)
                            {
                                Runnable stopRequest = new Runnable() {
                                    @Override
                                    public void run() {
                                        CreatedRequest = false;
                                        findViewById(R.id.logo).setVisibility(View.VISIBLE);
                                        Data.doAnimation(findViewById(R.id.logo),R.anim.out_from_right_s,_this);
                                        findViewById(R.id.requestWait).setVisibility(View.INVISIBLE);
                                        Data.doAnimation(findViewById(R.id.requestWait),R.anim.out_to_left_s,_this);
                                        Data.makeToast("something went wrong try again",_this);
                                        chessWaiting.setText(text);
                                    }
                                };
                                runOnUiThread(stopRequest);
                                break;
                            }
                        }
                        //look for previeus request
                        final int range = 1000;
                        boolean found = enterRequest(text.substring(0,requestStart),range);
                        if(found)
                        {
                            cancelRequest();
                        }
                    }
                }
            }
        };
        Thread t = new Thread(lookFor);
        t.start();
    }
    private void moveToRequest(Data.GameRequest request)
    {
        DataMeneger.setWatchMode(false,getApplicationContext());
        DataMeneger.setGameRequest(request,getApplicationContext());
        DataMeneger.setPage(ChessBoard.class.toString(),getApplicationContext());
        startActivity(new Intent(ChessHome.this,ChessBoard.class));
        DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
        finish();
    }


    private Data.GameRequest lookForRequestWithName(String waiters, int range, String _name, boolean set)
    {
        User user = DataMeneger.getUser(getApplicationContext());
        StringBuilder waitersBuilder = new StringBuilder(waiters);
        boolean newLine = true;
        boolean namePass = false;
        StringBuilder name = new StringBuilder();
        StringBuilder url = new StringBuilder();
        int rank = -1;
        int markIndex = -1;
        boolean found = false;
        for(int i = 0; i < waiters.length(); i++) // try to find existing request
        {
            final char a = waiters.charAt(i);
            boolean skipRequest =  false;
            if(newLine)
            {
                if(a != 'A') // the request is private
                {
                    skipRequest = true;
                }
                else // the request is public
                {
                    markIndex = i;
                    i++;
                    StringBuilder rankS = new StringBuilder();
                    while (waiters.charAt(i) != ' ')
                    {
                        rankS.append(waiters.charAt(i));
                        i++;
                    }
                    rank = Integer.parseInt(rankS.toString());
                    skipRequest = !Data.inRange(user.getChessRank(),rank,range);
                }
                if(!skipRequest)
                {
                    newLine = false;
                }

            }
            else if(a == '\t' && waiters.charAt(i + 1) == 'o') // request is over
            {
                if(!_name.equals(""))
                    System.out.println("name [" + name.toString() + "] _name [" + _name + "]");
                if(_name.equals("") || name.toString().equals(_name))
                {
                    DataMeneger.setFakeEnter(false,getApplicationContext());
                    Data.GameRequest request = new Data.GameRequest(rank,name.toString(),url.toString(),false,true);
                    if(set)
                    {
                        Data.makeToast("entering request " + request.toString(),_this);
                        waitersBuilder.setCharAt(markIndex,'S');
                        chessWaiting.setText(waitersBuilder.toString());
                    }
                    return request;
                }
                skipRequest = true;
            }
            else
            {
                if(a == ' ')
                    namePass = true;
                else if(namePass)
                    url.append(a);
                else
                    name.append(a);
            }
            if(skipRequest) // skip this request
            {
                while (!(waiters.charAt(i) == '\t' && waiters.charAt(i+1) == 'o')) //skip this request
                {
                    i++;
                }
                i++;
            }
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataMeneger.setRequestCancel(2,getApplicationContext());
        cancelRequest();
    }

    @Override
    protected void onStop() {
        cancelRequest();
        DataMeneger.setRequestCancel(1,getApplicationContext());
        super.onStop();
    }

    void cancelRequest()
    {
        if(CreatedRequest && requestStart != -1)
        {
            CreatedRequest = false;
            DataMeneger.setPage(ChessHome.class.toString(),getApplicationContext());
            DataMeneger.setMoveAnimations(R.anim.no_anim,R.anim.no_anim,getApplicationContext());
            StringBuilder text = new StringBuilder(ServerConnector.getText(chessWaiting.getUrl()));
            text.setCharAt(requestStart,'C');
            requestStart = -1;
            chessWaiting.setText(text.toString());
        }
       // DataMeneger.setFakeEnter(true,getApplicationContext());
    }

    public void moveToGameList(View v)
    {
        DataMeneger.setRequestCancel(1,getApplicationContext());
        /*StringBuilder text = new StringBuilder(chessWaiting.getText());
        text.setCharAt(requestStart,'C');
        requestStart = -1;
        chessWaiting.setText(text.toString());*/
        cancelRequest();
        DataMeneger.setGameCode(1,getApplicationContext());
        DataMeneger.setPage(GameList.class.toString(),getApplicationContext());
        DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
        startActivity(new Intent(ChessHome.this,GameList.class));
    }
    void testRequestCancel()
    {
        if(DataMeneger.getRequestCancel(getApplicationContext()) != 0)
        {
            findViewById(R.id.requestWait).setVisibility(View.INVISIBLE);
            findViewById(R.id.logo).setVisibility(View.VISIBLE);
            if(DataMeneger.getRequestCancel(getApplicationContext()) == 2)
            {
                Data.doAnimation(findViewById(R.id.requestWait),R.anim.out_to_left_s,this);
                Data.doAnimation(findViewById(R.id.logo),R.anim.out_from_right_s,this);
            }
            DataMeneger.setRequestCancel(0,getApplicationContext());
        }
    }
    void showWaitingLogo()
    {
        if(findViewById(R.id.requestWait).getVisibility() == View.INVISIBLE)
        {
            View waitLayout = findViewById(R.id.requestWait);
            View logLayout = findViewById(R.id.logo);
            waitLayout.setVisibility(View.VISIBLE);
            Data.doAnimation(waitLayout,R.anim.out_from_left_s,this);
            logLayout.setVisibility(View.INVISIBLE);
            Data.doAnimation(logLayout,R.anim.out_to_right_s,this);
            Data.doAnimation(findViewById(R.id.waitingLogo),R.anim.rotate,this);
        }
    }
}
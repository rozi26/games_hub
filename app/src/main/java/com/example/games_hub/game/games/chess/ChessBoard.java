package com.example.games_hub.game.games.chess;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.games_hub.R;
import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.Users.User;
import com.example.games_hub.game.gameActivites.GameList;
import com.example.games_hub.server.ServerConnector;

public class ChessBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        User user = DataMeneger.getUser(getApplicationContext());
        if(DataMeneger.getFakeEnter(getApplicationContext()))
        {
            startActivity(new Intent(ChessBoard.this,ChessHome.class));
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
            finish();
        }
        else if(DataMeneger.getWatchMode(getApplicationContext()))
        {
            GRAY_PLACE = -1;
            CAN_PLAY = true;
            canBack = true;
            showForward();
            GameList.GameListGame game = DataMeneger.getWatchGame(getApplicationContext());
            MY_COLOR_Is_White = game.getPlayAsWhite();
            findViewById(R.id.StartMenu).setVisibility(View.INVISIBLE);
            findViewById(R.id.gameOverOpener).setVisibility(View.VISIBLE);
            resetBoard();
            gameRecord = new StringBuilder(game.getcode());
            System.out.println("record " + gameRecord.toString());
            gameRecorderNumber = 0;
            moveNumber = gameRecord.length() / 6;
            toolList = new ToolProp[16];
            for(int i = 0; i < toolList.length; i++)
            {
                if(MY_COLOR_Is_White)
                    toolList[i] = new ToolProp(i + 48);
                else
                    toolList[i] = new ToolProp((i < 8)?i + 8:i - 8);
            }
            ((TextView)findViewById(R.id.playerBName)).setText(game.getPlayerBName());
            ((TextView)findViewById(R.id.playerAName)).setText(game.getPlayerAName());
            ((TextView)findViewById(R.id.playerBRank)).setText(game.getPlayerBRank());
            ((TextView)findViewById(R.id.playerARank)).setText(game.getPlayerARank());
            ((TextView)findViewById(R.id.winnerText)).setText("watch mode");
            WATCH_MODE = true;
            findViewById(R.id.defultTop).setVisibility(View.INVISIBLE);
            findViewById(R.id.watchModeTop).setVisibility(View.VISIBLE);
            if(!game.getGameReport().equals(""))
            {
                REPORT_RANKS = game.getGameReportInt();
                findViewById(R.id.moveReport).setVisibility(View.VISIBLE);
            }
        }
        else
        {
            Request = DataMeneger.getGameRequest(getApplicationContext());
            MY_COLOR_Is_White = Request.isHost();
            Server = new ServerConnector(Request.getUrl(),getApplicationContext());
            whiteTurn = true;
            moveNumber = 0;
            GRAY_PLACE = -1;
            resetBoard();
            if(!MY_COLOR_Is_White)
                moveWatcher();

            for(int i = 0; i < 64; i++)
            {
                NumToPic(i).setClickable(false);
            }
            toolList = new ToolProp[16];
            for(int i = 0; i < toolList.length; i++)
            {
                if(MY_COLOR_Is_White)
                    toolList[i] = new ToolProp(i + 48);
                else
                    toolList[i] = new ToolProp((i < 8)?i + 8:i - 8);
            }
            CAN_PLAY = false;
            if(MY_COLOR_Is_White)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    findViewById(R.id.HoursChoose).setForeground(findViewById(R.id.whiteHorseImage).getForeground());
                    findViewById(R.id.BisChoose).setForeground(findViewById(R.id.whiteBisImage).getForeground());
                    findViewById(R.id.HookChoose).setForeground(findViewById(R.id.whiteHookImage).getForeground());
                    findViewById(R.id.QueenChoose).setForeground(findViewById(R.id.whiteQueenImage).getForeground());
                }
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    findViewById(R.id.HoursChoose).setForeground(findViewById(R.id.blackHorseImage).getForeground());
                    findViewById(R.id.BisChoose).setForeground(findViewById(R.id.blackBisImage).getForeground());
                    findViewById(R.id.HookChoose).setForeground(findViewById(R.id.blackHookImage).getForeground());
                    findViewById(R.id.QueenChoose).setForeground(findViewById(R.id.blackQueenImage).getForeground());
                }
            }
            ((TextView)findViewById(R.id.coinNumber)).setText("" + (user.getCoins() - DataMeneger.getGameBat(getApplicationContext())));
            ((TextView)findViewById(R.id.betNumber)).setText("" + (DataMeneger.getGameBat(getApplicationContext()) * 2));
            UserPage = new ServerConnector(user.getUrl(),getApplicationContext());
            gameRecord = new StringBuilder("");
            gameRecorderNumber = 0;
            subUserBetCoins();
            WATCH_MODE = false;
            blackMoves = new boolean[16];
            final int startTime = 60000 * 10;
            PLAYER_A_TIME = startTime;
            PLAYER_B_TIME = startTime;
        }
        _this = this;
    }
    int PLAYER_A_TIME;
    int PLAYER_B_TIME;
    boolean[] blackMoves;
    boolean WATCH_MODE;
    StringBuilder gameRecord;
    Context _this; // this context
    boolean CAN_PLAY; // if allowed to play
    ToolProp[] toolList; // list of witch tools moved
    Data.GameRequest Request; // the game request
    int moveNumber; // how many turn there war
    int gameRecorderNumber;
    boolean MY_COLOR_Is_White;
    boolean whiteTurn;
    ServerConnector Server;
    ServerConnector UserPage;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        setSize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void resetBoard()
    {
        colors = new char[64];
        tools = new char[64];
        clearColor();
        final String order = ChessData.getDefaultOrder();
        for(int i = 0; i < 64; i++)
        {
            tools[i] = order.charAt(i);
        }
        ChessData.updateBoard(ChessBoard.this,tools,colors,MY_COLOR_Is_White);
        setSize();
    }
    char[] colors;
    char[] tools;
    private ImageView NumToPic(int i)
    {
        return ChessData.NumToPic(i,ChessBoard.this);
    }
    private int PicToNum(View v)
    {
        return ChessData.PicToNum(v,ChessBoard.this);
    }
    private void setSize()
    {
        final int length = findViewById(R.id.length).getWidth();
        for(int i = 0; i < 64; i++)
        {
            ImageView x = NumToPic(i);
            x.getLayoutParams().height = length / 8;
            x.getLayoutParams().width = length / 8;
            x.requestLayout();
        }
        findViewById(R.id.board).getLayoutParams().height = length;
    }
    private void clearColor()
    {
        for(int i = 0; i < colors.length; i++)
        {
            if(i == GRAY_PLACE)
                colors[i] = 'r';
            else
                colors[i] = 'n';
        }
    }

    int prevLoc;
    int becomeLoc;
    public void blockClick(View v)
    {
         if(CAN_PLAY)
         {
             final boolean REAL_MOVE = ((whiteTurn && MY_COLOR_Is_White) || (!whiteTurn && !MY_COLOR_Is_White)) && gameRecorderNumber == moveNumber && !WATCH_MODE;
             final int loc = (MY_COLOR_Is_White)?PicToNum(v):ChessData.getReverser(PicToNum(v));
             if(colors[loc] == 'y' || colors[loc] == 'b' || ((colors[loc] == 'g' && MY_COLOR_Is_White) || (colors[loc] == 'o' && !MY_COLOR_Is_White)))
             {
                 if(REAL_MOVE)
                 {
                     if(colors[loc] == 'b')
                     {
                         int num;
                         if(MY_COLOR_Is_White)
                         {
                             if(loc == 58)
                                 num = 2;
                             else
                                 num = 3;
                         }
                         else
                         {
                             if(loc == 2)
                                 num = 0;
                             else
                                 num = 1;
                         }
                         doMove(num,num,0);
                     }
                     else if((MY_COLOR_Is_White && tools[prevLoc] == 'a' && loc < 8) || (!MY_COLOR_Is_White && tools[prevLoc] == 'g' && loc > 55)) // pawn get to the end
                     {
                         CAN_PLAY = false;
                         findViewById(R.id.StartMenu).setVisibility(View.VISIBLE);
                         Data.doAnimation(findViewById(R.id.StartMenu),R.anim.out_from_left_b,this);
                         findViewById(R.id.StartGameInter).setVisibility(View.INVISIBLE);
                         findViewById(R.id.ChooseToolInter).setVisibility(View.VISIBLE);
                         becomeLoc = loc;
                     }
                     else
                     {
                         doMove(prevLoc,loc,0);
                     }
                 }
                 else
                 {
                     if(WATCH_MODE)
                         Data.makeToast("you can't play in watch mode",this);
                     else if(gameRecorderNumber != moveNumber)
                         Data.makeToast("switch forward to play",this);
                     else
                         Data.makeToast("that is not your turn",this);
                 }
             }
             else if(prevLoc == loc)
             {
                 clearColor();
                 prevLoc = -1;
                 ChessData.updateColors(ChessBoard.this,colors,MY_COLOR_Is_White);
             }
             else if((MY_COLOR_Is_White && ChessData.isWhite(tools[loc])) || (!MY_COLOR_Is_White && ChessData.isBlack(tools[loc])))
             {
                 clearColor();
                 colors = ChessData.toToolMove(tools,loc,ChessBoard.this, getToolsMoves(),true);
                 colors[loc] = 'r';
                 fullUpdate();
                 prevLoc = loc;
             }
             update();
         }
         else
         {
             Data.makeToast("can't play",this);
         }
    }
    private void doMove(int from, int to, int become)
    {
        if(Request.isOnline())
            doOnlineMove(from,to,become);
        else
            doOfflineMove(from,to,become);
    }
    private void doOnlineMove(int from, int to, int become)
    {
        canBack = false;
        executeMove(from,to,become);
        whiteTurn = !whiteTurn;
        clearColor();
        ChessData.updateBoard(ChessBoard.this,tools,colors,MY_COLOR_Is_White);
        moveWatcher();
        Runnable writeMove = new Runnable() {
            @Override
            public void run() {
                if(become == 0)
                    Server.addText(Data.numberWithZeros(from,2) + Data.numberWithZeros(to,2));
                else
                {
                    int becomeTo = become * 3;
                    if(MY_COLOR_Is_White)
                    {
                        if(from == to + 8)
                            becomeTo += 1;
                        else if(from == to + 9)
                            becomeTo += 2;
                    }
                    else
                    {
                        if(from == to - 8)
                            becomeTo += 1;
                        else if(from == to - 9)
                            becomeTo += 2;
                    }
                    Data.makeToast("becomeTo = " + becomeTo,_this);
                    Server.addText(Data.numberWithZeros(from,2) + Data.numberWithZeros(becomeTo,2));
                }
            }
        };
        Handler handler = new Handler();
        handler.post(writeMove);
    }
    int GRAY_PLACE;
    boolean canBack;
    void executeMove(int from, int to, int become)
    {
        gameRecord.append(ChessData.getGameRecordCode(from,to,become,tools));
        gameRecorderNumber++;
        moveNumber++;
        GRAY_PLACE = from;
        if(gameRecorderNumber != moveNumber)
        {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fullForward(null);
                    Data.makeToast("forward error",_this);
                }
            };
            runOnUiThread(runnable);
        }
        if(gameRecorderNumber == 1)
        {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    showBackward();
                }
            };
            runOnUiThread(runnable);
        }
        if(from == to) // castle
        {
            if(from == 0)
            {
                GRAY_PLACE = 2;
                tools[0] = 'n';
                tools[2] = 'l';
                tools[3] = 'j';
                tools[4] = 'n';
            }
            else if(from == 1)
            {
                GRAY_PLACE = 5;
                tools[4] = 'n';
                tools[5] = 'j';
                tools[6] = 'k';
                tools[7] = 'n';
            }
            else if(from == 2)
            {
                GRAY_PLACE = 58;
                tools[56] = 'n';
                tools[58] = 'f';
                tools[59] = 'd';
                tools[60] = 'n';
            }
            else if(from == 3)
            {
                GRAY_PLACE = 62;
                tools[60] = 'n';
                tools[61] = 'd';
                tools[62] = 'f';
                tools[63] = 'n';
            }
        }
        else
        {
            AddMoveToToolsProp(from);
            final boolean whiteMove = ChessData.isWhite(tools[from]);
            if(!whiteMove)
                ChessData.updateMove(blackMoves,from,false);
            switch (become)
            {
                case 0: tools[to] = tools[from]; break;
                case 1: tools[to] = (whiteMove)?'b':'h'; break;
                case 2: tools[to] = (whiteMove)?'c':'i'; break;
                case 3: tools[to] = (whiteMove)?'d':'j'; break;
                case 4: tools[to] = (whiteMove)?'e':'k'; break;
            }
            tools[from] = 'n';
        }
        final int gameResult = ChessData.getGameResult(tools,MY_COLOR_Is_White,getToolsMoves(),blackMoves);
        if(gameResult != 0) // game over
        {
            gameOver(gameResult);
        }
    }
    private void doOfflineMove(int from, int to, int become)
    {
        executeMove(from,to,become);
        clearColor();
        ChessData.updateBoard(ChessBoard.this,tools,colors,MY_COLOR_Is_White);
        whiteTurn = false;
        if(CAN_PLAY)
        {
            canBack = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ChessAI AI = new ChessAI(Request.getRank());
                    final int[] botMove = AI.doAI(tools,false,0,true);
                    executeMove(botMove[0],botMove[1],botMove[2]);
                    clearColor();
                    whiteTurn = true;
                    Runnable update = new Runnable() {
                        @Override
                        public void run() {
                            ChessData.updateBoard(ChessBoard.this,tools,colors,MY_COLOR_Is_White);
                        }
                    };
                    runOnUiThread(update);
                    canBack = true;
                }
            };
            Thread thread = new Thread(runnable);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
    }
    private void moveWatcher()
    {

        Runnable watch = new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {Thread.sleep(100);}
                    catch (Exception e){}
                    final String text = ServerConnector.getText(Request.getUrl());
                    if(text.length() > moveNumber * 4 && Character.isDigit(text.charAt(1)))
                    {
                        final String move = text.substring(moveNumber * 4, moveNumber * 4 + 4);
                        final int from = ChessData.charsToInt(move.charAt(0),move.charAt(1));
                        final int to = ChessData.charsToInt(move.charAt(2),move.charAt(3));
                        if((MY_COLOR_Is_White && from > 47 && tools[from] == 'g') || (!MY_COLOR_Is_White && from < 16 && tools[from] == 'a'))
                        {
                            int becomeNumber = to / 3;
                            int toLoc;
                            if(MY_COLOR_Is_White)
                            {
                                if(to % 3 == 0)
                                    toLoc = from + 7;
                                else if(to % 3 == 1)
                                    toLoc = from + 8;
                                else
                                    toLoc = from + 9;
                            }
                            else
                            {
                                if(to % 3 == 0)
                                    toLoc = from - 7;
                                else if(to % 3 == 1)
                                    toLoc = from - 8;
                                else
                                    toLoc = from - 9;
                            }
                            executeMove(from,toLoc,becomeNumber);
                        }
                        else
                            executeMove(from,to,0);
                        whiteTurn = !whiteTurn;
                        canBack = true;
                        Runnable updateer = new Runnable() {
                            @Override
                            public void run() {
                                prevLoc = -1;
                                clearColor();
                                update();
                            }
                        };
                        runOnUiThread(updateer);
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(watch);
        thread.start();

    }


    public void startGame(View v)
    {
        if(Request.isOnline())
        {
            if(Server.getLoaded())
            {
                User user = DataMeneger.getUser(getApplicationContext());
                if(!Request.isHost())
                {
                    Server.addText(user.getUserName() + " " + user.getChessRank());
                    setUserData(user,Request.getName(),Integer.toString(Request.getRank()));

                    findViewById(R.id.StartMenu).setVisibility(View.INVISIBLE);
                    Data.doAnimation(findViewById(R.id.StartMenu),R.anim.out_to_left_b,this);
                    CAN_PLAY = true;
                }
                else
                {
                    final String text = Server.getText();
                    if(!text.equals(""))
                    {
                        StringBuilder name = new StringBuilder();
                        StringBuilder rank = new StringBuilder();
                        boolean pass = false;
                        for(int i = 0; i < text.length(); i++)
                        {
                            if(text.charAt(i) == ' ')
                                pass  =true;
                            else
                            {
                                if(pass)
                                    rank.append(text.charAt(i));
                                else
                                    name.append(text.charAt(i));
                            }
                        }
                        setUserData(user,name.toString(),rank.toString());
                        findViewById(R.id.StartMenu).setVisibility(View.INVISIBLE);
                        Data.doAnimation(findViewById(R.id.StartMenu),R.anim.out_to_left_b,this);
                        Server.setText(" ");
                        CAN_PLAY = true;
                    }
                    else
                        Data.makeToast("wait for the other player to connect",this);
                }
                for(int i = 0; i < 64; i++)
                {
                    NumToPic(i).setClickable(true);
                }
            }
            else
                Data.makeToast(Data.noInternetMessage(),this);
        }
        else
        {
            setUserData(DataMeneger.getUser(getApplicationContext()),Request.getName(),Integer.toString(Request.getRank() * Request.getRank()));
            findViewById(R.id.StartMenu).setVisibility(View.INVISIBLE);
            Data.doAnimation(findViewById(R.id.StartMenu),R.anim.out_to_left_b,this);
            CAN_PLAY = true;
            for(int i = 0; i < 64; i++)
            {
                NumToPic(i).setClickable(true);
            }
        }
    }

    void setUserData(User me, String playerName, String playerLevel)
    {
        ((TextView)findViewById(R.id.playerAName)).setText(me.getUserName());
        ((TextView)findViewById(R.id.playerARank)).setText("" + me.getChessRank());
        ((TextView)findViewById(R.id.playerBName)).setText(playerName);
        ((TextView)findViewById(R.id.playerBRank)).setText(playerLevel);
    }

    public void update(View v)
    {

    }
    private void update()
    {
        ChessData.updateTools(ChessBoard.this,tools,MY_COLOR_Is_White);
    }



    private class ToolProp
    {
        int index;
        boolean move;
        int setNumber;
        public ToolProp(int _index)
        {
            index = _index;
            move = false;
            setNumber = -1;
        }

        public boolean getIfMove()
        {
            return move;
        }
        public void setMove()
        {
            setMove(true);
        }
        public void setMove(boolean to)
        {
            if(!move && to)
                setNumber = gameRecorderNumber;
            move = to;
        }
        public int getIndex()
        {
            return index;
        }
        public int getSetNumber()
        {
            return setNumber;
        }
    }
    void AddMoveToToolsProp(int from)
    {
        for(int i = 0; i < toolList.length; i++)
        {
            if(toolList[i].getIndex() == from)
            {
                toolList[i].setMove();
                break;
            }
        }
    }
    boolean[] getToolsMoves()
    {
        boolean[] b = new boolean[toolList.length];
        for(int i = 0; i < b.length; i++)
        {
            b[i] = toolList[i].getIfMove();
        }
        String a = "";
        for(int i = 0; i < 16; i++)
        {
            a += ("[" + (b[i]?"T":"F") + "]");
            if(i == 7)
                a += "\n";
        }
        System.out.println(a);
        return b;
    }

    public void ToolChoose(View v)
    {
        CAN_PLAY = true;
        int become = -1;
        if(v.equals(findViewById(R.id.HoursChoose)))
            become = 1;
        else if(v.equals(findViewById(R.id.BisChoose)))
            become = 2;
        else if(v.equals(findViewById(R.id.HookChoose)))
            become = 3;
        else if(v.equals(findViewById(R.id.QueenChoose)))
            become = 4;
        doMove(prevLoc,becomeLoc,become);
        if(become == -1)
            Data.makeToast("error become is -1",this);
        findViewById(R.id.StartMenu).setVisibility(View.INVISIBLE);
        Data.doAnimation(findViewById(R.id.StartMenu),R.anim.out_to_left_s,this);
    }



    public void fullBack(View v)
    {
        if(canBack)
        {
            while (gameRecorderNumber != 0)
            {
                back(null);
            }
        }
    }
    public void back(View v)
    {
        if(canBack)
        {
            final String move = gameRecord.substring(gameRecorderNumber * 6 - 6,gameRecorderNumber * 6);
            final int from = ChessData.charsToInt(move.charAt(0),move.charAt(1));
            final int to = ChessData.charsToInt(move.charAt(2),move.charAt(3));
            final char eat = move.charAt(4);
            final int become = Integer.parseInt(Character.toString(move.charAt(5)));

            ChessData.undoMove(tools,from,to,become,eat,gameRecorderNumber % 2 == 1);
            clearColor();
            fullUpdate();
            if(gameRecorderNumber == moveNumber)
                showForward();
            gameRecorderNumber--;
            for(int i = 0; i < 16; i++)
            {
                if(toolList[i].getSetNumber() == gameRecorderNumber)
                    toolList[i].setMove(false);
            }
            if(gameRecorderNumber == 0)
                hideBackward();

            if(REPORT_RANKS != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                ((TextView)findViewById(R.id.fromText)).setText(ChessData.intBlockToStringBock(from));
                ((TextView)findViewById(R.id.toText)).setText(ChessData.intBlockToStringBock(to));
                ((TextView)findViewById(R.id.moveArrow)).setText("<-");
                View i = findViewById(R.id.moveLogo);
                switch (REPORT_RANKS[gameRecorderNumber])
                {
                    case 0: i.setForeground(findViewById(R.id.badLogo).getForeground()); break;
                    case 1: i.setForeground(findViewById(R.id.normalLogo).getForeground()); break;
                    case 2: i.setForeground(findViewById(R.id.okLogo).getForeground()); break;
                    case 3: i.setForeground(findViewById(R.id.bestLogo).getForeground()); break;
                }
            }
        }
    }
    public void fullForward(View v)
    {
        while (gameRecorderNumber != moveNumber)
        {
            forward(null);
        }
    }
    public void forward(View v)
    {
        final String move = gameRecord.substring(gameRecorderNumber * 6, gameRecorderNumber * 6 + 6);
        final int from = ChessData.charsToInt(move.charAt(0),move.charAt(1));
        final int to = ChessData.charsToInt(move.charAt(2),move.charAt(3));
        final int become = Integer.parseInt(Character.toString(move.charAt(5)));
        ChessData.doMove(tools,from,to,become,gameRecorderNumber % 2 == 0);
        if(gameRecorderNumber == 0)
            showBackward();
        if(from != to)
            AddMoveToToolsProp(from);
        gameRecorderNumber++;
        if(gameRecorderNumber == moveNumber)
            hideForward();
        clearColor();
        fullUpdate();
        if(REPORT_RANKS != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) // update move report
        {
            if(findViewById(R.id.moveReport).getVisibility() == View.INVISIBLE)
            {
                findViewById(R.id.moveReport).setVisibility(View.VISIBLE);
                Data.doAnimation(findViewById(R.id.moveReport),R.anim.out_from_right_s,this);
            }
            ((TextView)findViewById(R.id.fromText)).setText(ChessData.intBlockToStringBock(from));
            ((TextView)findViewById(R.id.toText)).setText(ChessData.intBlockToStringBock(to));
            ((TextView)findViewById(R.id.moveArrow)).setText("->");
            View i = findViewById(R.id.moveLogo);
            switch (REPORT_RANKS[gameRecorderNumber - 1])
            {
                case 0: i.setForeground(findViewById(R.id.badLogo).getForeground()); break;
                case 1: i.setForeground(findViewById(R.id.normalLogo).getForeground()); break;
                case 2: i.setForeground(findViewById(R.id.okLogo).getForeground()); break;
                case 3: i.setForeground(findViewById(R.id.bestLogo).getForeground()); break;
            }
        }
    }
    void showForward()
    {
        findViewById(R.id.fullForward).setVisibility(View.VISIBLE);
        findViewById(R.id.Forward).setVisibility(View.VISIBLE);
        findViewById(R.id.fullForward).setClickable(true);
        findViewById(R.id.Forward).setClickable(true);
        Data.doAnimation(findViewById(R.id.fullForward),R.anim.out_from_right_s,this);
        Data.doAnimation(findViewById(R.id.Forward),R.anim.out_from_right_b,this);
    }
    void hideForward()
    {
        findViewById(R.id.fullForward).setVisibility(View.INVISIBLE);
        findViewById(R.id.Forward).setVisibility(View.INVISIBLE);
        findViewById(R.id.fullForward).setClickable(false);
        findViewById(R.id.Forward).setClickable(false);
        Data.doAnimation(findViewById(R.id.fullForward),R.anim.out_to_right_s,this);
        Data.doAnimation(findViewById(R.id.Forward),R.anim.out_to_right_b,this);
    }
    void showBackward()
    {
        canBack = true;
        findViewById(R.id.fullBack).setVisibility(View.VISIBLE);
        findViewById(R.id.Back).setVisibility(View.VISIBLE);
        findViewById(R.id.fullBack).setClickable(true);
        findViewById(R.id.Back).setClickable(true);
        Data.doAnimation(findViewById(R.id.fullBack),R.anim.out_from_left_s,this);
        Data.doAnimation(findViewById(R.id.Back),R.anim.out_from_left_b,this);
    }
    void hideBackward()
    {
        findViewById(R.id.fullBack).setVisibility(View.INVISIBLE);
        findViewById(R.id.Back).setVisibility(View.INVISIBLE);
        findViewById(R.id.fullBack).setVisibility(View.INVISIBLE);
        findViewById(R.id.fullBack).setClickable(false);
        findViewById(R.id.Back).setClickable(false);
        Data.doAnimation(findViewById(R.id.fullBack),R.anim.out_to_left_s,this);
        Data.doAnimation(findViewById(R.id.Back),R.anim.out_to_left_b,this);
    }

    void fullUpdate()
    {
        ChessData.updateBoard(ChessBoard.this,tools,colors,MY_COLOR_Is_White);
    }


    void gameOver(int gameResult)
    {
        CAN_PLAY = false;
        User user = DataMeneger.getUser(getApplicationContext());
        Runnable gameOverAnim = new Runnable() {
            @Override
            public void run() {
                final int playerBRank = Integer.parseInt(((TextView)findViewById(R.id.playerBRank)).getText().toString());
                final String playerBName = ((TextView)findViewById(R.id.playerBName)).getText().toString();
                DataMeneger.writeNewChessGame(user.getChessRank() + " " + playerBName + " " + playerBRank + " " + Data.getUserTime() + " " + gameRecord.toString(),MY_COLOR_Is_White,getApplicationContext()); // save the game in the memory
                findViewById(R.id.gameOver).setVisibility(View.VISIBLE);
                Data.doAnimation(findViewById(R.id.gameOver),R.anim.out_from_right_s,_this);
                if(gameResult != 2)
                {
                    final boolean win = gameResult == 1;
                    if(win) {
                        ((TextView) findViewById(R.id.winnerText)).setText("you won");
                        user.addCoins(DataMeneger.getGameBat(getApplicationContext()) * 2);
                    }
                    else {
                        ((TextView) findViewById(R.id.winnerText)).setText("you lost");
                    }
                    user.addChessRank(ChessData.getScoreWorth(user.getChessRank(),playerBRank,win));
                    ((TextView)findViewById(R.id.playerARank)).setText("" + user.getChessRank());
                    if(Request.isOnline())
                    {
                        final int playerBNewRank = playerBRank + ChessData.getScoreWorth(playerBRank,user.getChessRank(),!win);
                        ((TextView)findViewById(R.id.playerBRank)).setText("" + playerBNewRank);
                    }
                    ((TextView)findViewById(R.id.coinNumber)).setText("" + user.getCoins());
                    UserPage.setText(user.getCode());
                    DataMeneger.saveUser(user,DataMeneger.getUserPassword(getApplicationContext()),getApplicationContext());
                }
                else
                    ((TextView)findViewById(R.id.winnerText)).setText("DRAW");
            }
        };
        runOnUiThread(gameOverAnim);
    }

    void subUserBetCoins() // sub the bet moany from the user
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try{Thread.sleep(100);}
                    catch (Exception e){}
                    if(UserPage.getLoaded())
                    {
                        User user = DataMeneger.getUser(getApplicationContext());
                        user.addCoins(-DataMeneger.getGameBat(getApplicationContext()));
                        DataMeneger.saveUser(user,getApplicationContext());
                        Runnable updater = new Runnable() {
                            @Override
                            public void run() {
                                UserPage.setText(user.getCode());
                            }
                        };
                        runOnUiThread(updater);
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    public void watchGame(View v)
    {
        View big = findViewById(R.id.gameOver);
        View small = findViewById(R.id.gameOverOpener);
        big.setVisibility(View.INVISIBLE);
        small.setVisibility(View.VISIBLE);
        final int bigHeight = big.getLayoutParams().height;
        final int bigWidth = big.getLayoutParams().width;
        small.getLayoutParams().height = bigHeight / 4;
        small.getLayoutParams().width = bigWidth / 10;
        Data.doAnimation(big,R.anim.side_keeper_in,this);
        //Data.doAnimation(small,R.anim.dim_in_f,this);
    }
    int[] REPORT_RANKS;
    public void gameReport(View v)
    {
        findViewById(R.id.mainContent).setVisibility(View.INVISIBLE);
        Data.doAnimation(findViewById(R.id.mainContent),R.anim.out_to_left_s,this);
        if(REPORT_RANKS == null) // create the game report
        {
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            Data.doAnimation(findViewById(R.id.loading),R.anim.out_from_right_s,this);
            ChessReporter chessReporter = new ChessReporter();
            Runnable calculate = new Runnable() {
                @Override
                public void run() {
                    chessReporter.run(gameRecord.toString());
                    ChessReporter.Move[] moves = chessReporter.getMoves();
                    REPORT_RANKS = new int[moves.length];
                    for(int i = 0; i < REPORT_RANKS.length; i++)
                    {
                        REPORT_RANKS[i] = moves[i].getRank();
                    }
                    Runnable update = new Runnable() {
                        @Override
                        public void run() {
                            updateGameReport();
                            View loader = findViewById(R.id.loading);
                            loader.setVisibility(View.INVISIBLE);
                            Data.doAnimation(loader,R.anim.out_to_left_s,_this);
                            StringBuilder reportString = new StringBuilder();
                            for(int i = 0; i < moves.length; i++)
                            {
                                reportString.append(Integer.toString(moves[i].getRank()));
                            }
                            if(WATCH_MODE)
                                DataMeneger.addToChessGame(" " + reportString.toString(),DataMeneger.getWatchGame(getApplicationContext()).getIndex(),getApplicationContext());
                            else
                                DataMeneger.addToChessGame(" " + reportString.toString(),DataMeneger.getGamesNumberChess(getApplicationContext()) - 1,getApplicationContext());
                        }
                    };
                    runOnUiThread(update);
                }
            };
            Thread calucalteThrade = new Thread(calculate);
            calucalteThrade.setPriority(Thread.MAX_PRIORITY);
            calucalteThrade.start();
            Runnable watchPresent = new Runnable() {
                @Override
                public void run() {
                    while (chessReporter.getPresent() != 100)
                    {
                        try{Thread.sleep(50);}
                        catch (Exception e){}
                        Runnable update = new Runnable() {
                            @Override
                            public void run() {
                                final double present = chessReporter.getPresent();
                                ((TextView)findViewById(R.id.present)).setText("" + (int)present + "%");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    ((ProgressBar)findViewById(R.id.loadProgress)).setProgress((int)(present),true);
                                }
                            }
                        };
                        runOnUiThread(update);
                    }
                }
            };
            Thread watcher = new Thread(watchPresent);
            watcher.start();
        }
        else
            updateGameReport();
    }
    void updateGameReport()
    {
        int[] ranksCount = new int[8];
        for(int i = 0; i < REPORT_RANKS.length; i++)
        {
            int index = REPORT_RANKS[i];
            if(i % 2 == 0)
                ranksCount[index]++;
            else
                ranksCount[index + 4]++;
        }
        findViewById(R.id.gameReportTable).setVisibility(View.VISIBLE);
        Data.doAnimation(findViewById(R.id.gameReportTable),R.anim.out_from_right_s,this);
        ((TextView)findViewById(R.id.playerA0)).setText("" + ranksCount[0]);
        ((TextView)findViewById(R.id.playerA1)).setText("" + ranksCount[1]);
        ((TextView)findViewById(R.id.playerA2)).setText("" + ranksCount[2]);
        ((TextView)findViewById(R.id.playerA3)).setText("" + ranksCount[3]);
        ((TextView)findViewById(R.id.playerB0)).setText("" + ranksCount[4]);
        ((TextView)findViewById(R.id.playerB1)).setText("" + ranksCount[5]);
        ((TextView)findViewById(R.id.playerB2)).setText("" + ranksCount[6]);
        ((TextView)findViewById(R.id.playerB3)).setText("" + ranksCount[7]);
        ((TextView)findViewById(R.id.playerAReportName)).setText(((TextView)findViewById(R.id.playerAName)).getText().toString());
        ((TextView)findViewById(R.id.playerBReportName)).setText(((TextView)findViewById(R.id.playerBName)).getText().toString());
    }
    public void exit(View v)
    {
        if(DataMeneger.getWatchMode(getApplicationContext()))
        {
            DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
            DataMeneger.setPage(GameList.class.toString(),getApplicationContext());
            startActivity(new Intent(ChessBoard.this,GameList.class));
        }
        else
        {
            DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
            DataMeneger.setPage(ChessHome.class.toString(),getApplicationContext());
            startActivity(new Intent(ChessBoard.this,ChessHome.class));
        }
        finish();
    }

    public void gameOverOpen(View v)
    {
        View big = findViewById(R.id.gameOver);
        View small = findViewById(R.id.gameOverOpener);
        big.setVisibility(View.VISIBLE);
        small.setVisibility(View.INVISIBLE);
        Data.doAnimation(big,R.anim.side_keeper_out,this);
    }

    public void returnToEnd(View v)
    {
        findViewById(R.id.gameReportTable).setVisibility(View.INVISIBLE);
        findViewById(R.id.mainContent).setVisibility(View.VISIBLE);
        Data.doAnimation(findViewById(R.id.gameReportTable),R.anim.out_to_right_s,this);
        Data.doAnimation(findViewById(R.id.mainContent),R.anim.out_from_left_s,this);
    }

    void timerMeneger()
    {

    }
}
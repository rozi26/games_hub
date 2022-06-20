package com.example.games_hub.game.games.chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.example.games_hub.Data;
import com.example.games_hub.R;

import java.util.Objects;

//import javax.swing.text.html.ImageView;

public class ChessData {

    public static ImageView NumToPic(int num, Activity a)
    {
        switch(num)
        {
            case 0: return a.findViewById(R.id.a0);
            case 1: return a.findViewById(R.id.a1);
            case 2: return a.findViewById(R.id.a2);
            case 3: return a.findViewById(R.id.a3);
            case 4: return a.findViewById(R.id.a4);
            case 5: return a.findViewById(R.id.a5);
            case 6: return a.findViewById(R.id.a6);
            case 7: return a.findViewById(R.id.a7);
            case 8: return a.findViewById(R.id.b0);
            case 9: return a.findViewById(R.id.b1);
            case 10: return a.findViewById(R.id.b2);
            case 11: return a.findViewById(R.id.b3);
            case 12: return a.findViewById(R.id.b4);
            case 13: return a.findViewById(R.id.b5);
            case 14: return a.findViewById(R.id.b6);
            case 15: return a.findViewById(R.id.b7);
            case 16: return a.findViewById(R.id.c0);
            case 17: return a.findViewById(R.id.c1);
            case 18: return a.findViewById(R.id.c2);
            case 19: return a.findViewById(R.id.c3);
            case 20: return a.findViewById(R.id.c4);
            case 21: return a.findViewById(R.id.c5);
            case 22: return a.findViewById(R.id.c6);
            case 23: return a.findViewById(R.id.c7);
            case 24: return a.findViewById(R.id.d0);
            case 25: return a.findViewById(R.id.d1);
            case 26: return a.findViewById(R.id.d2);
            case 27: return a.findViewById(R.id.d3);
            case 28: return a.findViewById(R.id.d4);
            case 29: return a.findViewById(R.id.d5);
            case 30: return a.findViewById(R.id.d6);
            case 31: return a.findViewById(R.id.d7);
            case 32: return a.findViewById(R.id.e0);
            case 33: return a.findViewById(R.id.e1);
            case 34: return a.findViewById(R.id.e2);
            case 35: return a.findViewById(R.id.e3);
            case 36: return a.findViewById(R.id.e4);
            case 37: return a.findViewById(R.id.e5);
            case 38: return a.findViewById(R.id.e6);
            case 39: return a.findViewById(R.id.e7);
            case 40: return a.findViewById(R.id.f0);
            case 41: return a.findViewById(R.id.f1);
            case 42: return a.findViewById(R.id.f2);
            case 43: return a.findViewById(R.id.f3);
            case 44: return a.findViewById(R.id.f4);
            case 45: return a.findViewById(R.id.f5);
            case 46: return a.findViewById(R.id.f6);
            case 47: return a.findViewById(R.id.f7);
            case 48: return a.findViewById(R.id.g0);
            case 49: return a.findViewById(R.id.g1);
            case 50: return a.findViewById(R.id.g2);
            case 51: return a.findViewById(R.id.g3);
            case 52: return a.findViewById(R.id.g4);
            case 53: return a.findViewById(R.id.g5);
            case 54: return a.findViewById(R.id.g6);
            case 55: return a.findViewById(R.id.g7);
            case 56: return a.findViewById(R.id.h0);
            case 57: return a.findViewById(R.id.h1);
            case 58: return a.findViewById(R.id.h2);
            case 59: return a.findViewById(R.id.h3);
            case 60: return a.findViewById(R.id.h4);
            case 61: return a.findViewById(R.id.h5);
            case 62: return a.findViewById(R.id.h6);
            case 63: return a.findViewById(R.id.h7);
        }
        return null;
    }
    public static int PicToNum(View im, Activity a)
    {
        for(int i = 0; i < 64; i++)
        {
            if(Objects.equals(NumToPic(i, a), im))
                return i;
        }
        return -1;
    }

    public static void updateBoard(Activity a, char[] tools, char[] colors, boolean white)
    {
        updateColors(a,colors,white);
        updateTools(a,tools,white);
    }
    public static void updateColors(Activity a, char[] colors, boolean white)
    {
        if(!white)
        {
            colors = toolReverser(colors);
        }
        for(int i = 0; i < 64; i++)
        {
            ImageView block = NumToPic(i,a);
            switch (colors[i])
            {
                case 'n': block.setBackgroundColor(((i % 2 == 1 ^ i % 16 > 7)? Color.BLACK:Color.WHITE)); break;
                case 'g': if(white)block.setBackgroundColor(Color.GREEN);else block.setBackgroundColor(Color.RED); break;
                case 'y': block.setBackgroundColor(Color.YELLOW); break;
                case 'o': if(white) block.setBackgroundColor(Color.RED); else block.setBackgroundColor(Color.GREEN); break;
                case 'r': block.setBackgroundColor(Color.GRAY); break;
                case 'b': block.setBackgroundColor(Color.BLUE); break;
            }
        }
    }
    public static void updateTools(Activity a, char[] tools, boolean white)
    {
        if(!white)
        {
            tools = toolReverser(tools);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean thereWhiteKing = false;
            boolean thereBlackKing = false;
            for(int i = 0; i < 64; i++)
            {
                ImageView block = NumToPic(i,a);
                switch (tools[i])
                {
                    case 'n':block.setForeground(null); break;
                    case 'a':block.setForeground((a.findViewById(R.id.whiteSolImage).getForeground())); break;
                    case 'b':block.setForeground((a.findViewById(R.id.whiteHorseImage).getForeground())); break;
                    case 'c':block.setForeground((a.findViewById(R.id.whiteBisImage).getForeground())); break;
                    case 'd':block.setForeground((a.findViewById(R.id.whiteHookImage).getForeground())); break;
                    case 'e':block.setForeground((a.findViewById(R.id.whiteQueenImage).getForeground())); break;
                    case 'f':block.setForeground((a.findViewById(R.id.whiteKingImage).getForeground())); thereWhiteKing = true; break;
                    case 'g':block.setForeground((a.findViewById(R.id.blackSolImage).getForeground())); break;
                    case 'h':block.setForeground((a.findViewById(R.id.blackHorseImage).getForeground())); break;
                    case 'i':block.setForeground((a.findViewById(R.id.blackBisImage).getForeground())); break;
                    case 'j':block.setForeground((a.findViewById(R.id.blackHookImage).getForeground())); break;
                    case 'k':block.setForeground((a.findViewById(R.id.blackQueenImage).getForeground())); break;
                    case 'l':block.setForeground((a.findViewById(R.id.blackKingImage).getForeground())); thereBlackKing = true; break;
                }
            }
        }
    }

    public static String getDefaultOrder()
    {
        return "jhiklihjggggggggnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnaaaaaaaadbcefcbd";
    }

    public static int getReverser(int loc)
    {
        return (56 - ((loc / 8) * 8)) + (7 -loc % 8);
    }
    public static char[] toolReverser(char[] tools)
    {
        char[] newTools = new char[64];
        for(int i = 0; i < 64; i++)
        {
            newTools[i] = tools[getReverser(i)];
        }
        return newTools;
    }

    public static boolean isBlack(char a)
    {
        return a == 'g' || a == 'h' || a == 'i' || a == 'j' || a == 'k' || a == 'l';
    }
    public static boolean isWhite(char a)
    {
        return a != 'n' && !isBlack(a);
    }

    public static char[] toToolMove(char[] tools, int loc, boolean[] Moves)
    {
        return toToolMove(tools,loc,null,Moves,false);
    }
    public static char[] toToolMove(char[] tools, int loc, Activity a, boolean[] ToolMoves, boolean update)
    {
        char[] colors;
        ToolMoves toolMoves = new ToolMoves(tools);
        boolean white;
        if(isWhite(tools[loc]))
        {
            toolMoves.doWhiteMove(loc, ToolMoves,update);
            white = true;
        }
        else
        {
            toolMoves.doBlackMove(loc, ToolMoves,update);
            white = false;
        }
        colors = toolMoves.getColors();

        int kingLocation = -1;
        for(int i = 0; i < 64; i++)
        {
            if(tools[i] == (white?'f':'l'))
            {
                kingLocation = i;
                break;
            }
        }
        final boolean kingMove = kingLocation == loc;
        for(int i = 0; i < 64; i++)
        {
            if(colors[i] == 'y' || (white && colors[i] == 'g') || (!white && colors[i] == 'o'))
            {
                final char SAVE_TOOL = tools[i];
                tools[i] = tools[loc];
                tools[loc] = 'n';

                if(BlockInDanger(tools,(kingMove)?i:kingLocation,white))
                    colors[i] = 'n';

                tools[loc] = tools[i];
                tools[i] = SAVE_TOOL;
            }
        }
        if(update) {

            updateColors(a, colors, white);
        }
        return colors;
    }
    private static class  ToolMoves
    {
        private  boolean left = false;
        private  boolean right = false;
        private  boolean up = false;
        private  boolean down = false;
        private char[] tools;
        private char[] colors;
        public ToolMoves(char[] _tools)
        {
            tools = _tools;
            colors = new char[64];
            for(int i = 0; i < 64; i++)
            {
                colors[i]  = 'n';
            }
        }
        public void clearColor()
        {
            for(int i = 0; i < 64; i++)
            {
                colors[i] = 'n';
            }
        }
        public char[] getColors()
        {
            return colors;
        }
        private char getTool(int loc)
        {
            return tools[loc];
        }
        void doWhiteMove(int loc, boolean[] moves, boolean first)
        {
            char kind = getTool(loc);
            switch (kind) {
                case 'a': whitePawnMove(loc); break;
                case 'b': hoursMove(loc); break;
                case 'c': bisMove(loc); break;
                case 'd': towerMove(loc); break;
                case 'e': queenMove(loc); break;
                case 'f': kingMove(loc);
                if(!moves[12] && first) // castle
                {
                    if (!moves[15] && RightCastleCheckW())
                        colors[62] = 'b';
                    if (!moves[8] && LeftCastleCheckW())
                        colors[58] = 'b';
                }
                break;
            }
        }
        void doBlackMove(int loc, boolean[] moves, boolean first)
        {
            char kind = getTool(loc);
            switch (kind) {
                case 'g': blackPawnMove(loc); break;
                case 'h': hoursMove(loc); break;
                case 'i': bisMove(loc); break;
                case 'j': towerMove(loc); break;
                case 'k': queenMove(loc); break;
                case 'l': kingMove(loc);
                if(!moves[4] && first) // castle
                {
                    if(!moves[7] && RightCastleCheckB())
                        colors[6] = 'b';
                    if(!moves[0] && LeftCastleCheckB())
                        colors[2] = 'b';
                }
                break;
            }
        }
        private void setDor(int loc)
        {
            left = loc % 8 == 0;
            right = loc % 8 == 7;
            up = loc < 8;
            down = loc > 55;
        }
        private void whitePawnMove(int loc)
        {
            setDor(loc);
            if(!up && tools[loc - 8] == 'n')
            {
                colors[loc - 8] = 'y';
                if(loc < 56 && loc >= 48 && tools[loc - 16] == 'n')
                    colors[loc - 16] = 'y';
            }
            if(!right && !up && isBlack(tools[loc - 7]))
                colors[loc - 7] = 'g';
            else if(!right && !up && isWhite(tools[loc - 7]))
                colors[loc - 7] = 'o';
            if(!left && !up && isBlack(tools[loc - 9]))
                colors[loc - 9] = 'g';
            else if(!left && !up && isWhite(tools[loc - 9]))
                colors[loc - 9] = 'o';
        }
        private  void blackPawnMove(int loc)
        {
            setDor(loc);
            if(!down && tools[loc + 8] == 'n')
            {
                colors[loc + 8] = 'y';
                if(loc < 16 && loc >= 8 && tools[loc + 16] == 'n')
                    colors[loc + 16] = 'y';
            }
            if(!right && !down && isBlack(tools[loc + 9]))
                colors[loc + 9] = 'g';
            else if(!right && !down && tools[loc + 9] != 'n')
                colors[loc + 9] = 'o';
            if(!left && !down && isBlack(tools[loc + 7]))
                colors[loc + 7] = 'g';
            else if(!left && !down && tools[loc + 7] != 'n')
                colors[loc + 7] = 'o';
        }
        private  void hoursMove(int loc)
        {
            setDor(loc);
            if(loc > 16 && !left)
            {
                if(tools[loc - 17] == 'n')
                    colors[loc - 17] = 'y';
                else if(isBlack(tools[loc - 17]))
                    colors[loc - 17] = 'g';
                else
                    colors[loc - 17] = 'o';
            }
            if(loc > 15 && !right)
            {
                if(tools[loc - 15] == 'n')
                    colors[loc - 15] = 'y';
                else if(isBlack(tools[loc - 15]))
                    colors[loc - 15] = 'g';
                else
                    colors[loc - 15] = 'o';
            }
            if(!(loc % 8 == 0 || loc % 8 == 1) && !up)
            {
                if(tools[loc - 10] == 'n')
                    colors[loc - 10] = 'y';
                else if(isBlack(tools[loc - 10]))
                    colors[loc - 10] = 'g';
                else
                    colors[loc - 10] = 'o';
            }
            if(!(loc % 8 == 6 || loc % 8 == 7) && !up)
            {
                if(tools[loc - 6] == 'n')
                    colors[loc - 6] = 'y';
                else if(isBlack(tools[loc - 6]))
                    colors[loc - 6] = 'g';
                else
                    colors[loc - 6] = 'o';
            }
            if(!(loc % 8 == 0 || loc % 8 == 1) && !down)
            {
                if(tools[loc + 6] == 'n')
                    colors[loc + 6] = 'y';
                else if(isBlack(tools[loc + 6]))
                    colors[loc + 6] = 'g';
                else
                    colors[loc + 6] = 'o';
            }
            if(!(loc % 8 == 6 || loc % 8 == 7) && !down)
            {
                if(tools[loc + 10] == 'n')
                    colors[loc + 10] = 'y';
                else if(isBlack(tools[loc + 10]))
                    colors[loc + 10] = 'g';
                else
                    colors[loc + 10] = 'o';
            }
            if(loc < 48 && !left)
            {
                if(tools[loc + 15] == 'n')
                    colors[loc + 15] = 'y';
                else if(isBlack(tools[loc + 15]))
                    colors[loc + 15] = 'g';
                else
                    colors[loc + 15] = 'o';
            }
            if(loc < 48 && !right)
            {
                if(tools[loc + 17] == 'n')
                    colors[loc + 17] = 'y';
                else if(isBlack(tools[loc + 17]))
                    colors[loc + 17] = 'g';
                else
                    colors[loc + 17] = 'o';
            }
        }
        private  void bisMove(int loc)
        {
            for(int i = loc + 7; i < 64; i+= 7)
            {
                if(i % 8 == 7)
                    break;
                else
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            for(int i = loc + 9; i < 64; i+= 9)
            {
                if(i % 8 == 0)
                    break;
                else
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            for(int i = loc - 7; i >= 0; i-= 7)
            {
                if(i % 8 == 0)
                    break;
                else
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            for(int i = loc - 9; i >= 0; i-= 9)
            {
                if(i % 8 == 7)
                    break;
                else
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
        }
        void towerMove(int loc)
        {
            setDor(loc);
            //System.out.println(loc);
            if(!up)
            {
                for(int i = loc - 8; i >= 0; i -= 8)
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            if(!down)
            {
                for(int i = loc + 8; i < 64; i += 8)
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            if(!right)
            {
                for(int i = loc + 1; i % 8 != 0; i++)
                {
                    if(isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    }
                    else if(isBlack(tools[i]))
                    {
                        colors[i] = 'g';
                        break;
                    }
                    else
                        colors[i] = 'y';
                }
            }
            if(!left)
            {
                for(int i = loc - 1; i % 8 != 7 && i != -1; i--) {
                    if (isWhite(tools[i])) {
                        colors[i] = 'o';
                        break;
                    } else if (isBlack(tools[i])) {
                        colors[i] = 'g';
                        break;
                    } else
                        colors[i] = 'y';
                }
            }
        }
        void queenMove(int loc)
        {
            bisMove(loc);
            towerMove(loc);
        }
        void kingMove(int loc)
        {
            setDor(loc);
            if(!up)
            {
                if(tools[loc - 8] == 'n')
                    colors[loc - 8] = 'y';
                else if(isBlack(tools[loc - 8]))
                    colors[loc - 8] = 'g';
                else if(tools[loc] - 8 != 'n')
                    colors[loc - 8] = 'o';
            }
            if(!right)
            {
                if(tools[loc + 1] == 'n')
                    colors[loc + 1] = 'y';
                else if(isBlack(tools[loc + 1]))
                    colors[loc + 1] = 'g';
                else if(tools[loc] + 1 != 'n')
                    colors[loc + 1] = 'o';
                if(!down)
                {
                    if(tools[loc + 9] == 'n')
                        colors[loc + 9] = 'y';
                    else if(isBlack(tools[loc + 9]))
                        colors[loc + 9] = 'g';
                    else if(tools[loc] + 9 != 'n')
                        colors[loc + 9] = 'o';
                }
                if(!up)
                {
                    if(tools[loc - 7] == 'n')
                        colors[loc - 7] = 'y';
                    else if(isBlack(tools[loc - 7]))
                        colors[loc - 7] = 'g';
                    else if(tools[loc] - 7 != 'n')
                        colors[loc - 7] = 'o';
                }
            }
            if(!down)
            {
                if(tools[loc + 8] == 'n')
                    colors[loc + 8] = 'y';
                else if(isBlack(tools[loc + 8]))
                    colors[loc + 8] = 'g';
                else if(tools[loc] + 8 != 'n')
                    colors[loc + 8] = 'o';
            }
            if(!left)
            {
                if(tools[loc - 1] == 'n')
                    colors[loc - 1] = 'y';
                else if(isBlack(tools[loc - 1]))
                    colors[loc - 1] = 'g';
                else if(tools[loc] - 1 != 'n')
                    colors[loc - 1] = 'o';
                if(!down)
                {
                    if(tools[loc + 7] == 'n')
                        colors[loc + 7] = 'y';
                    else if(isBlack(tools[loc + 7]))
                        colors[loc + 7] = 'g';
                    else if(tools[loc] + 7 != 'n')
                        colors[loc + 7] = 'o';
                }
                if(!up)
                {
                    if(tools[loc - 9] == 'n')
                        colors[loc - 9] = 'y';
                    else if(isBlack(tools[loc - 9]))
                        colors[loc - 9] = 'g';
                    else if(tools[loc] - 9 != 'n')
                        colors[loc - 9] = 'o';
                }
            }
        }

        boolean RightCastleCheckW()
        {
            if(tools[63] != 'd' || tools[62] != 'n' || tools[61] != 'n' || tools[60] != 'f')
                return false;
            for(int i = 60; i < 64; i++)
            {
                if(BlockInDanger(tools,i,true))
                    return false;
            }
            return true;
        }
        boolean LeftCastleCheckW()
        {
            if(tools[56] != 'd' || tools[57] != 'n' || tools[58] != 'n' || tools[59] != 'n' || tools[60] != 'f')
                return false;
            for(int i = 56; i < 61; i++)
            {
                if(BlockInDanger(tools,i,true))
                    return false;
            }
            return true;
        }
        boolean RightCastleCheckB()
        {
            if(tools[7] != 'j' || tools[6] != 'n' || tools[5] != 'n' || tools[4] != 'l')
                return false;
            for(int i = 4; i < 8; i++)
            {
                if(BlockInDanger(tools,i,false))
                    return false;
            }
            return true;
        }
        boolean LeftCastleCheckB()
        {
            if(tools[0] != 'j' || tools[1] != 'n' || tools[2] != 'n' || tools[3] != 'n' || tools[4] != 'l')
                return false;
            for(int i = 0; i < 5; i++)
            {
                if(BlockInDanger(tools,i,false))
                    return false;
            }
            return true;
        }
    }
    private static boolean BlockInDanger(char[] tools, int block, boolean white)
    {
        final boolean fakePlace = tools[block] == 'n';
        if(fakePlace)
            tools[block] = (white)?'a':'g';
        for(int i = 0; i < 64; i++)
        {
            if(white && isBlack(tools[i]))
            {
                ToolMoves toolMoves = new ToolMoves(tools);
                toolMoves.doBlackMove(i,new boolean[16],false);
                final char[] colors = toolMoves.getColors();
                if(colors[block] == 'o')
                {
                    if(fakePlace)
                        tools[block] = 'n';
                    return true;
                }
            }
            else if(!white && isWhite(tools[i]))
            {
                ToolMoves toolMoves = new ToolMoves(tools);
                toolMoves.doWhiteMove(i,new boolean[16],false);
                final char[] colors = toolMoves.getColors();
                if(colors[block] == 'g')
                {
                    if(fakePlace)
                        tools[block] = 'n';
                    return true;
                }
            }
        }
        if(fakePlace)
            tools[block] = 'n';
        return false;
    }

    public static int getGameResult(char[] tools, boolean white, boolean[] whiteMoves, boolean[] blacksMove)
    {
        /*
        -1 you lose
        0 normal
        1 you win
        2 draw
         */
        if(checkMath(tools,white,whiteMoves))
            return -1;
        else if(checkMath(tools,!white,blacksMove))
            return 1;
        return 0;
    }
    private static boolean checkMath(char[] tools, boolean white, boolean[] moves)
    {
        for(int i = 0; i < 64; i++)
        {
            if((white && isWhite(tools[i])) || (!white && isBlack(tools[i])))
            {
                char[] colors = toToolMove(tools,i,moves);
                for(int g = 0; g < 64; g++)
                {
                    if(colors[g] == 'y' || (white && colors[g] == 'g') || (!white && colors[g] == 'o'))
                        return false;
                }
            }
        }
        return true;
    }

    public static class GameResult
    {
        final private int winner;
        final private int moves;
        final private String gameCode;
        public GameResult(int _winner, String _gameCode)
        {
            winner = _winner;
            gameCode = _gameCode;
            moves = _gameCode.length() / 4;
        }
        public int getWinner()
        {
            return winner;
        }
        public int getMoves()
        {
            return moves;
        }
        public String getGameCode()
        {
            return gameCode;
        }
    }

    public static int getScoreWorth(int myRank, int otherRank, boolean won)
    {
        final int sameScore = 12;
        final int rangeScore = 25;
        final boolean thisBetter = myRank > otherRank;
        int rangeLevel = 0;
        while (true)
        {
            if(Data.inRange(myRank,otherRank,rangeScore * (int)Math.pow(2,(rangeLevel + 1))))
            {
                if(won)
                {
                    if(thisBetter)
                        return sameScore - rangeLevel;
                    else
                        return sameScore + rangeLevel;
                }
                else
                {
                    if(thisBetter)
                        return -(sameScore + rangeLevel);
                    else
                        return -(sameScore - rangeLevel);
                }
            }
            rangeLevel++;
        }
    }
    public static int locOpisade(int num) {
        return (7 - (num / 8)) * 8 + (num % 8);
    }
    public static void toolReversers(char[] tools) {
        char[] newTools = new char[64];
        for (int i = 0; i < 64; i++) {
            newTools[i] = tools[locOpisade(i)];
            if (newTools[i] < 103)
                newTools[i] = (char) ((int) (newTools[i]) + 6);
            else if (newTools[i] != 'n')
                newTools[i] = (char) ((int) (newTools[i]) - 6);
        }
        System.arraycopy(newTools, 0, tools, 0, 64);
    }

    public static String getGameRecordCode(int from, int to, int become, char[] tools)
    {
        return Data.numberWithZeros(from,2) + Data.numberWithZeros(to,2) + tools[to] + become;
    }

    public static int getChessWinner(String code)
    {
        /*
        -1 black
        0 draw
        1 white
         */
        final String defaulter = getDefaultOrder();
        char[] tools = new char[64];
        for(int i = 0; i < 64; i++)
        {
            tools[i] = defaulter.charAt(i);
        }
        int turn = 0;
        boolean[] whiteMove = new boolean[16];
        boolean[] blackMove = new boolean[16];
        while (true)
        {
            final String move = code.substring(turn * 6, turn * 6  + 6);
            final int from =  charsToInt(move.charAt(0),move.charAt(1));
            final int to = charsToInt(move.charAt(2),move.charAt(3));
            final int become = Integer.parseInt(Character.toString(move.charAt(5)));
            if(isWhite(tools[from])) updateMove(whiteMove,from,true);
            else updateMove(blackMove,from,false);
            doMove(tools,from,to,become,turn % 2 == 0);
            turn++;

            final int over = getGameResult(tools,true, whiteMove,blackMove);
            if(over != 0)
            {
                if(over == 2)
                    return 0;
                return over;
            }
        }
    }

    public static int charsToInt(char a, char b)
    {
        int num1 = Integer.parseInt(Character.toString(a)) * 10;
        return  num1 +  Integer.parseInt(Character.toString(b));
    }

    public static void doMove(char[] tools, int from, int to, int become, boolean white)
    {
        if(from == to)
        {
            switch (from)
            {
                case 0: tools[0] = 'n'; tools[2] = 'l'; tools[3] = 'j'; tools[4] = 'n'; break;
                case 1: tools[4] = 'n'; tools[5] = 'j'; tools[6] = 'l'; tools[7] = 'n'; break;
                case 2: tools[56] = 'n'; tools[58] = 'f'; tools[59] = 'd'; tools[60] = 'n'; break;
                case 3: tools[60] = 'n'; tools[61] = 'd'; tools[62] = 'f'; tools[63] = 'n'; break;
            }
        }
        else
        {
            switch (become)
            {
                case 0:tools[to] = tools[from]; break;
                case 1:tools[to] = (white)?'b':'h'; break;
                case 2:tools[to] = (white)?'c':'i'; break;
                case 3:tools[to] = (white)?'d':'j'; break;
                case 4:tools[to] = (white)?'e':'k'; break;
            }
            tools[from] = 'n';
        }
    }
    public static void undoMove(char[] tools, int from, int to, int become, char eat, boolean white)
    {
        if(from == to)
        {
            switch (from)
            {
                case 0: tools[0] = 'j'; tools[2] = 'n'; tools[3] = 'n'; tools[4] = 'l'; break;
                case 1: tools[4] = 'l'; tools[5] = 'n'; tools[6] = 'n'; tools[7] = 'j'; break;
                case 2: tools[56] = 'd'; tools[58] = 'n'; tools[59] = 'n'; tools[60] = 'f'; break;
                case 3: tools[60] = 'f'; tools[61] = 'n'; tools[62] = 'n'; tools[63] = 'd'; break;
            }
        }
        else
        {
            if(become == 0)
                tools[from] = tools[to];
            else
                tools[from] = (white)?'a':'g';
            tools[to] = eat;
        }
    }

    static void updateMove(boolean[] moves, int from, boolean white)
    {
        if(white && from > 48)
            moves[from - 48] = true;
        else if(!white && from < 16)
            moves[from < 8?from + 8:from -  8] = true;
    }
    public static String intBlockToStringBock(int index)
    {
        return "" + (char)((index % 8) + 97) + (8 - index / 8);
    }
}

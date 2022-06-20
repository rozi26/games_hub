package com.example.games_hub.game.games.chess;

import java.util.Arrays;

public class ChessReporter {

    private Move[] moves;
    private double whiteScore;
    private double blackScore;
    private double present;
    public Move[] getMoves()
    {
        return moves;
    }
    public double getPresent(){return  present;}
    public ChessReporter()
    {

    }
    public void run(String code)
    {
        final int moveLength = code.length() / 6;
        moves = new Move[moveLength];
        whiteScore = 0;
        blackScore = 0;
        present = 0;
        final double presentAdder = 100.0 / moveLength;
        boolean white = true;
        final String org = ChessData.getDefaultOrder();
        char[] tools = new char[64];
        for(int i = 0; i < org.length(); i++)
        {
            tools[i] = org.charAt(i);
        }
        boolean[] WhiteMoves = new boolean[16];
        boolean[] BlackMoves = new boolean[16];
        System.out.println("run");
        for(int i = 0; i < moveLength; i++)
        {
            final String move = code.substring(i * 6, i * 6 + 6);
            final int from = ChessData.charsToInt(move.charAt(0),move.charAt(1));
            final int to = ChessData.charsToInt(move.charAt(2),move.charAt(3));
            final int become = Integer.parseInt(Character.toString(move.charAt(5)));
            final int[][] moveReport = getMovesReport(tools,white,(white)?WhiteMoves:BlackMoves);
            int[] orderList = new int[moveReport.length];
            Arrays.fill(orderList, -3000);
            int sum = moveReport[0][3];
            int moveIndex = 0;
            orderList[0] = sum;
            final double presentAdder2 = presentAdder / moveReport.length;
            present += presentAdder2;
            for(int g = 1; g < moveReport.length; g++)
            {
                final int score = moveReport[g][3];
                for(int h = 0; h <= g; h++)
                {
                    if(score < orderList[h])
                    {
                        for(int f = orderList.length - 1; f > h; f--)
                        {
                            orderList[f] = orderList[f - 1];
                        }
                        orderList[h] = score;
                        break;
                    }
                    else if(orderList[h] == -3000)
                    {
                        orderList[h] = score;
                    }
                }
                sum += score;
                if(moveReport[g][0] == from && moveReport[g][1] == to)
                    moveIndex = g;
                present += presentAdder2;
            }
            for(int g = 0; g < orderList.length; g++)
            {
                System.out.print("[" + g + "," + orderList[g] + "]");
            }
            final int rank = setRank(moveReport[moveIndex][3],orderList);
            System.out.println(" " + rank + "[" + moveReport[moveIndex][3] + "]");
            moves[i] = new Move(rank,white,0);
            ChessData.doMove(tools,from,to,become,white);
            white = !white;
        }
        present = 100;
        for(int i = 0; i < moves.length; i++)
        {
            System.out.println("[" + i + "][" + (moves[i].getWhite()?'W':'B') + "] = " + moves[i].getRank());
        }
    }

    private int[][] getMovesReport(char[] tools, boolean white, boolean[] moves)
    {
        final int level = 2;
        int moveCount = 0;
        for(int a1 = 0; a1 < 64; a1++)
        {
            if ((white && ChessData.isWhite(tools[a1])) || (!white && ChessData.isBlack(tools[a1]))) {
                final char[] colors = ChessData.toToolMove(tools,a1,moves);
                for(int a2 = 0; a2 < 64; a2++)
                {
                    if (colors[a2] == 'y' || colors[a2] == 'b' || (white && colors[a2] == 'g') || (!white && colors[a2] == 'o'))
                        moveCount++;
                }
            }
        }
        int[][] list = new int[moveCount][];
        int count = 0;
        for(int a1 = 0; a1 < 64; a1++) {
            if ((white && ChessData.isWhite(tools[a1])) || (!white && ChessData.isBlack(tools[a1]))) {
                final char[] colors = ChessData.toToolMove(tools,a1,moves);
                for (int a2 = 0; a2 < 64; a2++) {
                    if (colors[a2] == 'y' || colors[a2] == 'b' || (white && colors[a2] == 'g') || (!white && colors[a2] == 'o')) {
                        int from;
                        int to;
                        if (colors[a2] == 'b') {
                            switch (a2) {
                                case 2:
                                    from = 0;
                                    to = 0;
                                    break;
                                case 6:
                                    from = 1;
                                    to = 1;
                                    break;
                                case 58:
                                    from = 2;
                                    to = 2;
                                    break;
                                case 62:
                                    from = 3;
                                    to = 3;
                                    break;
                                default:
                                    from = -1;
                                    to = -1;
                                    break; // crash
                            }
                        }
                        else {
                            from = a1;
                            to = a2;
                        }
                        final char eat = tools[to];
                        int become = 0;
                        if((white && tools[from] == 'a' && to < 8) || (!white && tools[from] == 'g' && to > 55))
                            become = 4;
                        ChessData.doMove(tools,from,to,become,white);
                        ChessAI ai = new ChessAI(level,true);
                        if(white)
                            list[count] = ai.doAI(tools,false,0,true);
                        else
                            list[count] = ai.doAIForWhite(tools);
                        list[count][0] = from;
                        list[count][1] = to;
                        list[count][3] = -list[count][3];
                        count++;
                        ChessData.undoMove(tools,from,to,become,eat,white);
                    }
                }
            }
        }
        return list;
    }


    private class CodeMove
    {
        final int from;
        final int to;
        private CodeMove(int _from, int _to)
        {
            from = _from;
            to = _to;
        }
    }
    public class Move
    {
        /*
        rank
        0 - mistake
        1 - bad
        2 - good
        3 - perfect
         */
        final private int rank;
        final private boolean white;
        final private double change;
        public Move(int _rank, boolean _white, double _change)
        {
            rank = _rank;
            white = _white;
            change = _change;
        }
        public int getRank()
        {
            return rank;
        }
        public boolean getWhite()
        {
            return white;
        }
        public double getChange()
        {
            return change;
        }
    }
    public double getWhiteScore()
    {
        return whiteScore;
    }
    public double getBlackScore()
    {
        return blackScore;
    }

    private int setRank(double score, int[] list)
    {
        if(score == list[list.length - 1])
        {
            if(list.length > 2 && score != list[list.length - 2])
                return 3;
            return 2;
        }
        double top25Sum = 0;
        int objects1 = 0;
        for(int i = list.length - 2; i >= (list.length - (list.length / 4)); i--)
        {
            top25Sum += list[i];
            objects1++;
        }
        if(score >= (top25Sum / objects1))
            return 2;
        double low50Sum = 0;
        int object2 = 0;
        for(int i = 0; i < list.length / 2; i++)
        {
            low50Sum += list[i];
            object2++;
        }
        if(score < (low50Sum / object2))
            return 0;
        return 1;
    }
}

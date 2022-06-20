package com.example.games_hub.game.games.chess;

import java.util.Random;

public class ChessAI {

    int treeRuns;


    final int loopLimit;
    int loopCount;
    boolean deBug = true;
    // boolean blackKingMove;
    boolean priorityBotAttactive = true; // if the priority bot is more agresive then defender
    char[] tools = new char[64];
    char[] colors = new char[64];
    int[] result = new int[8];

    /*
     result[0] = from
     result[1] = to
     result[2] = become
     result[3] = score
     result[4] = escape score
     result[5] = loopLimit
     result[6] = treeRuns
     result[7] = runningTime
     */
    public ChessAI(int _loopLimit, boolean real) {
        loopLimit = _loopLimit;
    }

    public ChessAI(int _loopLimit) {
        if (XFromY(_loopLimit % 10, 10))
            loopLimit = _loopLimit / 10 + 1;
        else
            loopLimit = _loopLimit / 10;
        treeRuns = 0;
    }

    public String getAIVersion() {
        return "RecursionEat 1.3";
    }

    public int[] doAIForWhite(char[] _tools) {
        ChessData.toolReversers(_tools);
        int[] result = doAI(_tools, false, 0, true);
        result[0] = ChessData.locOpisade(result[0]);
        result[1] = ChessData.locOpisade(result[1]);
        ChessData.toolReversers(_tools);
        return result;
    }

    public int[] doAI(char[] _tools, boolean _deBug, int _loopCount, boolean _attactive) {

        //blackKingMove = _blackKingMove;
        priorityBotAttactive = _attactive;
        loopCount = _loopCount;
        deBug = _deBug;
        for (int i = 0; i < 64; i++) {
            tools[i] = _tools[i];
        }
        final long startTime = System.currentTimeMillis();
        doPriorityMove(0);
        result[7] = (int) (System.currentTimeMillis() - startTime);
       /* if(result[3] != 0 || loopCount == 0)
            System.out.println("AI calculation time of " + (System.currentTimeMillis() - startTime) + " milliseconds (" + result[0] + " -> " + result[1] + ", rank: " + result[3] + ") (" + loopCount + ")");/**/
        return result;

    }

    void move(int from, int to, int rank, boolean escape) {
        if ((tools[from] == 'a' && to < 8) || (tools[from] == 'g' && to > 55)) {
            result[2] = 4;
        } else
            result[2] = 0;
        if (from == 1 && to == 1) {
            tools[4] = 'n';
            tools[5] = 'j';
            tools[6] = 'l';
            tools[7] = 'n';
        } else if (from == 2 && to == 2) {
            tools[0] = 'n';
            tools[2] = 'l';
            tools[3] = 'j';
            tools[4] = 'n';
        } else {
            tools[to] = tools[from];
            tools[from] = 'n';
        }
        result[0] = from;
        result[1] = to;
        result[3] = rank;
        result[4] = (escape) ? getPriorityScore(tools[from]) : 0;
        result[5] = loopLimit;
        result[6] = treeRuns;
    }

    //AI place
    void clearColor() {
        for (int i = 0; i < 64; i++) {
            colors[i] = 'n';
        }
    }

    char getTool(int in) {
        return tools[in];
    }


    int doPriorityMove(int prevScore) {
        int from = -1;
        int to = -1;
        int score = -50000;
        int passCount = 2;
        int castelCount = 0;
        for (int i = 0; i < 64; i++) {
            if (isBlack(tools[i])) {
                clearColor();
                doBlackMove(i);
                for (int g = 0; g < 64; g++) {
                    if (colors[g] == 'y' || colors[g] == 'o') {
                        treeRuns++;
                        int moveScore = 0;
                        if (colors[g] == 'o') {
                            if (tools[g] == 'f') // if eat black king
                            {
                                moveScore = 1000;
                            } else// if eat
                            {
                                if (loopCount == loopLimit)
                                    moveScore += (whiteCharIsProtect(tools, g)) ? 0 : getPriorityScore(tools[g]);
                                else
                                    moveScore += getPriorityScore(tools[g]);
                            }
                        }
                        if (tools[i] == 'g' && g > 55) // if pawn become queen
                            moveScore += getPriorityScore('k') - getPriorityScore('g') - 1;
                        /*if(castelCount < 0)
                        {
                            if(castelCount == -2 && !data.canLeftCastel(tools,blackKingMove)) // if cant do left castel
                                castelCount++;
                            if(castelCount == -1 && !data.canRightCastel(tools,blackKingMove)) // if cant do right castel
                                castelCount++;
                            if(castelCount != 0)
                            {
                                g--;
                                moveScore = 1;
                            }
                        }/**/
                        if (loopCount < loopLimit && moveScore != 1000) // sub to score of the bast move the opponent can do
                        {
                            final char[] toolsBackUp = tools.clone();
                            final char[] colorBackUp = colors.clone();
                            if (castelCount == 0) {
                                tools[g] = tools[i];
                                tools[i] = 'n';
                            } else {
                                if (castelCount == -2) {
                                    tools[0] = 'n';
                                    tools[2] = 'l';
                                    tools[3] = 'j';
                                    tools[4] = 'n';
                                } else {
                                    tools[4] = 'n';
                                    tools[5] = 'j';
                                    tools[6] = 'l';
                                    tools[7] = 'n';
                                }
                            }
                            ChessData.toolReversers(tools);
                            loopCount++;
                            moveScore -= doPriorityMove(0);
                            loopCount--;
                            for (int j = 0; j < 64; j++) {
                                tools[j] = toolsBackUp[j];
                                colors[j] = colorBackUp[j];
                            }
                        }
                        if (countTools(tools) < 6 && onlyKingCanMove(tools) && inDanger(findTool('l'), tools)) // check if the move will put the king on pat
                        {
                            boolean free = false;
                            final int kingLocaiton = findTool('l');
                            char[] kindMoves = ChessData.toToolMove(tools, kingLocaiton, new boolean[16]);
                            for (int j = 0; j < 64; j++) {
                                if (kindMoves[j] == 'y' || kindMoves[j] == 'o') {
                                    final char TO_EAT_SAVE = tools[j];
                                    tools[j] = tools[kingLocaiton];
                                    tools[kingLocaiton] = 'n';
                                    if (!inDanger(kingLocaiton, tools))
                                        free = true;
                                    tools[kingLocaiton] = tools[j];
                                    tools[j] = TO_EAT_SAVE;
                                    if (free)
                                        break;

                                }
                            }
                            if (!free) {
                                //System.out.println("-100 for pat");
                                score -= 100;
                            }
                        }

                        if ((moveScore > score) || (moveScore == score && (oneIn(passCount * 2)))) {
                            if (moveScore == score)
                                passCount++;
                            else
                                passCount = 2;
                            score = moveScore;
                            if (castelCount == 0) {
                                from = i;
                                to = g;
                            } else {
                                from = -castelCount;
                                to = -castelCount;
                            }
                        }
                        if (castelCount < 0)
                            castelCount++;
                    }
                }
                if (score == 1000)
                    break;
            }
        }

        if (from != -1 && loopCount == 0) {
            //System.out.println(passCount);
            //System.out.println("move " + from + " -> " + to + " score: " + score + " loop: " + loopCount);
            move(from, to, score, false);
        }
        /*else
            System.out.println("error loop over loop: " + loopCount);*/
        return score;
    }


    //AI DATA
    int getPriorityScore(char kind) {
        if (kind == 'a') {
            if (priorityBotAttactive)
                return 3;
            else
                return 1;

        } else if (kind == 'b' || kind == 'c') {
            if (priorityBotAttactive)
                return 6;
            else
                return 4;
        } else if (kind == 'd') {
            if (priorityBotAttactive)
                return 10;
            else
                return 8;
        } else if (kind == 'e') {
            if (priorityBotAttactive)
                return 19;
            else
                return 17;
        } else if (kind == 'f') // eating the enemy king is always the best move
        {
            return 1001;
        } else if (kind == 'g')
            return 2;
        else if (kind == 'h' || kind == 'i')
            return 6;
        else if (kind == 'j')
            return 9;
        else if (kind == 'k')
            return 18;
        else if (kind == 'l') // the highest priority the tool can get is his rank + other tool rank so the two highest rank tool are 8 and 9 so the kins must be above them in 18
            return 1000;
        return 0; // the kind is null
    }

    Random random = new Random();

    public boolean oneIn(int in) {
        return random.nextInt(in) == 0;
    }

    private int countTools(char[] tools) {
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if (tools[i] != 'n')
                count++;
        }
        return count;
    }

    private boolean whiteCharIsProtect(char[] tools, int loc) {
        for (int i = 0; i < 64; i++) {
            if (isWhite(tools[i]) && ChessData.toToolMove(tools, i, new boolean[16])[loc] == 'o')
                return true;
        }
        return false;
    }

    private boolean onlyKingCanMove(char[] tools) {
        boolean findKing = false;
        for (int i = 0; i < 64; i++) {
            if (isBlack(tools[i])) {
                if (tools[i] == 'l')
                    findKing = true;
                else {
                    char[] move = ChessData.toToolMove(tools, i, new boolean[16]);
                    for (int g = 0; g < 64; g++) {
                        if (move[g] == 'y' || move[g] == 'o')
                            return false;
                    }
                }
            }
        }
        return findKing;
    }

    public boolean inDanger(int in, char[] order) {
        clearColor();
        final boolean white = isWhite(order[in]);
        if (white)
            markBlackMoves();
        else
            markWhiteMoves();
        return colors[in] == ((white) ? 'o' : 'g');
    }

    //AI MOVES
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    void markBlackMoves() {
        for (int i = 0; i < 64; i++) {
            doBlackMove(i);
        }
    }

    void markWhiteMoves() {
        for (int i = 0; i < 64; i++) {
            doWhiteMove(i);
        }
    }

    void doMove(int loc) {
        if (isWhite(tools[loc]))
            doWhiteMove(loc);
        else if (tools[loc] != 'n')
            doBlackMove(loc);

    }

    void doWhiteMove(int loc) {
        char kind = getTool(loc);
        switch (kind) {
            case 'a':
                whitePawnMove(loc);
                break;
            case 'b':
                hoursMove(loc);
                break;
            case 'c':
                bisMove(loc);
                break;
            case 'd':
                towerMove(loc);
                break;
            case 'e':
                queenMove(loc);
                break;
            case 'f':
                kingMove(loc);
                break;
        }
    }

    void doBlackMove(int loc) {
        char kind = getTool(loc);
        switch (kind) {
            case 'g':
                blackPawnMove(loc);
                break;
            case 'h':
                hoursMove(loc);
                break;
            case 'i':
                bisMove(loc);
                break;
            case 'j':
                towerMove(loc);
                break;
            case 'k':
                queenMove(loc);
                break;
            case 'l':
                kingMove(loc);
                break;
        }
    }

    int findTool(char kind) {
        for (int i = 0; i < 64; i++) {
            if (tools[i] == kind)
                return i;
        }
        return -1;
    }

    int countTool(char kind) {
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if (tools[i] == kind)
                count++;
        }
        return count;
    }

    private void setDor(int loc) {
        left = loc % 8 == 0;
        right = loc % 8 == 7;
        up = loc < 8;
        down = loc > 55;
    }

    private void whitePawnMove(int loc) {
        setDor(loc);
        if (!up && tools[loc - 8] == 'n') {
            colors[loc - 8] = 'y';
            if (loc < 56 && loc >= 48 && tools[loc - 16] == 'n')
                colors[loc - 16] = 'y';
        }
        if (!right && !up && isBlack(tools[loc - 7]))
            colors[loc - 7] = 'g';
        else if (!right && !up && isWhite(tools[loc - 7]))
            colors[loc - 7] = 'o';
        if (!left && !up && isBlack(tools[loc - 9]))
            colors[loc - 9] = 'g';
        else if (!left && !up && isWhite(tools[loc - 9]))
            colors[loc - 9] = 'o';
    }

    private void blackPawnMove(int loc) {
        setDor(loc);
        if (!down && tools[loc + 8] == 'n') {
            colors[loc + 8] = 'y';
            if (loc < 16 && loc >= 8 && tools[loc + 16] == 'n')
                colors[loc + 16] = 'y';
        }
        if (!right && !down && isBlack(tools[loc + 9]))
            colors[loc + 9] = 'g';
        else if (!right && !down && tools[loc + 9] != 'n')
            colors[loc + 9] = 'o';
        if (!left && !down && isBlack(tools[loc + 7]))
            colors[loc + 7] = 'g';
        else if (!left && !down && tools[loc + 7] != 'n')
            colors[loc + 7] = 'o';
    }

    private void hoursMove(int loc) {
        setDor(loc);
        if (loc > 16 && !left) {
            if (tools[loc - 17] == 'n')
                colors[loc - 17] = 'y';
            else if (isBlack(tools[loc - 17]))
                colors[loc - 17] = 'g';
            else
                colors[loc - 17] = 'o';
        }
        if (loc > 15 && !right) {
            if (tools[loc - 15] == 'n')
                colors[loc - 15] = 'y';
            else if (isBlack(tools[loc - 15]))
                colors[loc - 15] = 'g';
            else
                colors[loc - 15] = 'o';
        }
        if (!(loc % 8 == 0 || loc % 8 == 1) && !up) {
            if (tools[loc - 10] == 'n')
                colors[loc - 10] = 'y';
            else if (isBlack(tools[loc - 10]))
                colors[loc - 10] = 'g';
            else
                colors[loc - 10] = 'o';
        }
        if (!(loc % 8 == 6 || loc % 8 == 7) && !up) {
            if (tools[loc - 6] == 'n')
                colors[loc - 6] = 'y';
            else if (isBlack(tools[loc - 6]))
                colors[loc - 6] = 'g';
            else
                colors[loc - 6] = 'o';
        }
        if (!(loc % 8 == 0 || loc % 8 == 1) && !down) {
            if (tools[loc + 6] == 'n')
                colors[loc + 6] = 'y';
            else if (isBlack(tools[loc + 6]))
                colors[loc + 6] = 'g';
            else
                colors[loc + 6] = 'o';
        }
        if (!(loc % 8 == 6 || loc % 8 == 7) && !down) {
            if (tools[loc + 10] == 'n')
                colors[loc + 10] = 'y';
            else if (isBlack(tools[loc + 10]))
                colors[loc + 10] = 'g';
            else
                colors[loc + 10] = 'o';
        }
        if (loc < 48 && !left) {
            if (tools[loc + 15] == 'n')
                colors[loc + 15] = 'y';
            else if (isBlack(tools[loc + 15]))
                colors[loc + 15] = 'g';
            else
                colors[loc + 15] = 'o';
        }
        if (loc < 48 && !right) {
            if (tools[loc + 17] == 'n')
                colors[loc + 17] = 'y';
            else if (isBlack(tools[loc + 17]))
                colors[loc + 17] = 'g';
            else
                colors[loc + 17] = 'o';
        }
    }

    private void bisMove(int loc) {
        for (int i = loc + 7; i < 64; i += 7) {
            if (i % 8 == 7)
                break;
            else {
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
        for (int i = loc + 9; i < 64; i += 9) {
            if (i % 8 == 0)
                break;
            else {
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
        for (int i = loc - 7; i >= 0; i -= 7) {
            if (i % 8 == 0)
                break;
            else {
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
        for (int i = loc - 9; i >= 0; i -= 9) {
            if (i % 8 == 7)
                break;
            else {
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

    void towerMove(int loc) {
        setDor(loc);
        //System.out.println(loc);
        if (!up) {
            for (int i = loc - 8; i >= 0; i -= 8) {
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
        if (!down) {
            for (int i = loc + 8; i < 64; i += 8) {
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
        if (!right) {
            for (int i = loc + 1; i % 8 != 0; i++) {
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
        if (!left) {
            for (int i = loc - 1; i % 8 != 7 && i != -1; i--) {
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

    void queenMove(int loc) {
        bisMove(loc);
        towerMove(loc);
    }

    void kingMove(int loc) {
        setDor(loc);
        if (!up) {
            if (tools[loc - 8] == 'n')
                colors[loc - 8] = 'y';
            else if (isBlack(tools[loc - 8]))
                colors[loc - 8] = 'g';
            else if (tools[loc] - 8 != 'n')
                colors[loc - 8] = 'o';
        }
        if (!right) {
            if (tools[loc + 1] == 'n')
                colors[loc + 1] = 'y';
            else if (isBlack(tools[loc + 1]))
                colors[loc + 1] = 'g';
            else if (tools[loc] + 1 != 'n')
                colors[loc + 1] = 'o';
            if (!down) {
                if (tools[loc + 9] == 'n')
                    colors[loc + 9] = 'y';
                else if (isBlack(tools[loc + 9]))
                    colors[loc + 9] = 'g';
                else if (tools[loc] + 9 != 'n')
                    colors[loc + 9] = 'o';
            }
            if (!up) {
                if (tools[loc - 7] == 'n')
                    colors[loc - 7] = 'y';
                else if (isBlack(tools[loc - 7]))
                    colors[loc - 7] = 'g';
                else if (tools[loc] - 7 != 'n')
                    colors[loc - 7] = 'o';
            }
        }
        if (!down) {
            if (tools[loc + 8] == 'n')
                colors[loc + 8] = 'y';
            else if (isBlack(tools[loc + 8]))
                colors[loc + 8] = 'g';
            else if (tools[loc] + 8 != 'n')
                colors[loc + 8] = 'o';
        }
        if (!left) {
            if (tools[loc - 1] == 'n')
                colors[loc - 1] = 'y';
            else if (isBlack(tools[loc - 1]))
                colors[loc - 1] = 'g';
            else if (tools[loc] - 1 != 'n')
                colors[loc - 1] = 'o';
            if (!down) {
                if (tools[loc + 7] == 'n')
                    colors[loc + 7] = 'y';
                else if (isBlack(tools[loc + 7]))
                    colors[loc + 7] = 'g';
                else if (tools[loc] + 7 != 'n')
                    colors[loc + 7] = 'o';
            }
            if (!up) {
                if (tools[loc - 9] == 'n')
                    colors[loc - 9] = 'y';
                else if (isBlack(tools[loc - 9]))
                    colors[loc - 9] = 'g';
                else if (tools[loc] - 9 != 'n')
                    colors[loc - 9] = 'o';
            }
        }
    }

    //AI class
    public class priorityMove implements Cloneable {
        private boolean _allow;
        private int _from;
        private int _to;

        private boolean _inDanger; // if the tool we move can get eat if don't move
        private boolean _getEatAfterMove; // if the tool we move can get eat after the move
        private char _eatKind; // the kind of tool the move eat (if don't eat anything equal 'n')
        private int _blockRank; // the rank of the tools the move block
        private int _unblockRank; // the rank of the tools the move unblock
        private String _porp;

        private int _forwardRank = 0; // add score to sol move because when sol get to the end he become queen
        private boolean _makeMat = false;
        private int _kingExternal = 0;

        public priorityMove() {

        }

        public priorityMove(int from, int to, boolean inDanger, char eatKind, boolean getEatAfterMove, int blockRank, int unblockRank, boolean allow) {
            _from = from;
            _to = to;
            _inDanger = inDanger;
            _eatKind = eatKind;
            _getEatAfterMove = getEatAfterMove;
            _blockRank = blockRank;
            _unblockRank = unblockRank;
            _allow = allow;
        }

        public void allow(boolean is) {
            _allow = is;
        }

        public boolean getAllow() {
            return _allow;
        }

        public int getFrom() {
            return _from;
        }

        public int getTo() {
            return _to;
        }

        public boolean ifInDanger() {
            return _inDanger;
        }

        public boolean ifGetEatAfterMove() {
            return _getEatAfterMove;
        }

        public char getEatKind() {
            return _eatKind;
        }

        public int getBlockRank() {
            return _blockRank;
        }

        public int getUnBlockRank() {
            return _unblockRank;
        }

        public int getForwardRank() {
            return _forwardRank;
        }

        public boolean isMakeMat() {
            return _makeMat;
        }

        public int getKindExternal() {
            return _kingExternal;
        }


        public void setForwardRank(int forwardRank) {
            _forwardRank = forwardRank;
        }

        public void setMakeMat(boolean makeMat) {
            _makeMat = makeMat;
        }

        public void setKindExternal(int external) {
            _kingExternal = external;
        }

        public void readMove() {
            System.out.println("(" + getAllow() + ") priority move " + getFrom() + " -> " + getTo() + ", inDanger: " + ifInDanger() + ", in danger after move: " + ifGetEatAfterMove() + ", eat: " + getEatKind() + ", block rank: " + _blockRank + ", unblock rank: " + _unblockRank + ((_forwardRank == 0) ? "" : ", forward rank: " + _forwardRank) + ((_makeMat) ? ", mat rank: 100" : "") + ((_kingExternal == 0) ? "" : ", king external: " + _kingExternal) + ", kind rank (" + getPriorityScore(tools[_from]) + ") prop (" + _porp + ")");
        }


        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public void addProp(String prop) {
            _porp = prop;
        }

        public String getProp() {
            return _porp;
        }

    }
    boolean isBlack(char a) {
        return a > 102 && a < 109;
    }

    boolean isWhite(char a) {
        return a > 96 && a < 103;
    }

    private boolean XFromY(int x, int y) {
        return (random.nextInt(y)) < x;
    }

}

package com.example.games_hub;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.games_hub.Users.User;
import com.example.games_hub.game.gameActivites.GameList;

public class DataMeneger {

    private static class saver
    {
        private static void setInt(String name, int to, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(name, to);
            editor.commit();
        }
        private static void addInt(String name,int add,Context context)
        {
            setInt(name,getInt(name,context) + add,context);
        }

        private static void setBoll(String name, boolean to, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(name, to);
            editor.commit();
        }

        private static void addString(String name, String to, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(name, (getString(name, context) + to));
            editor.commit();
        }

        private static void setString(String name, String to, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(name, to);
            editor.commit();
        }

        private static String getString(String name, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            return sp.getString(name, "");
        }

        private static int getInt(String name, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            return sp.getInt(name, 0);
        }

        private static boolean getBool(String name, Context context) {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            return sp.getBoolean(name, true);
        }
        private static void setLong(String name,long to,Context context)
        {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(name, to);
            editor.commit();
        }
        private static void addLong(String name,long add,Context context)
        {
            setLong(name,getLong(name,context) + add,context);
        }
        private static long getLong(String name,Context context)
        {
            SharedPreferences sp = context.getSharedPreferences("userlevel", Context.MODE_PRIVATE);
            return sp.getLong(name, 0l);
        }
    }


    public static String getUserCode(Context context)
    {
        return saver.getString("userData",context);
    }
    public static String getUserUrl(Context context)
    {
        return saver.getString("userURL",context);
    }
    public static String getUserPassword(Context context)
    {
        return saver.getString("userPassword",context);
    }
    public static User getUser(Context context)
    {
        return new User(getUserCode(context),getUserUrl(context));
    }
    public static void saveUser(User user,String password, Context context)
    {
        saver.setString("userData",user.getCode(),context);
        saver.setString("userURL",user.getUrl(),context);
        saver.setString("userPassword",password,context);
    }
    public static void saveUser(User user,Context context)
    {
        saveUser(user,getUserPassword(context),context);
    }
    public static void logOutUser(Context context)
    {
        saver.setString("userData","",context);
        saver.setString("userURL","",context);
    }

    public static String getPage(Context context)
    {
      return saver.getString("firstEnter",context);
    }
    public static void setPage(String name,Context context)
    {
        saver.setString("firstEnter",name,context);
    }

    public static void setMoveAnimations(int a1,int a2, Context context)
    {
        saver.setInt("animation1",a1,context);
        saver.setInt("animation2",a2,context);
    }
    public static int[] getMoveAnimations(Context context)
    {
        return new int[]{saver.getInt("animation1",context),saver.getInt("animation2",context)};
    }
    public static void setGameRequest(Data.GameRequest request, Context context)
    {
        saver.setString("GameRequestURL",request.getUrl(),context);
        saver.setString("GameRequestName",request.getName(),context);
        saver.setInt("GameRequestRank",request.getRank(),context);
        saver.setBoll("GameRequestHost",request.isHost(),context);
        saver.setBoll("GameRequestOnline",request.isOnline(),context);
    }
    public static Data.GameRequest getGameRequest(Context context)
    {
        return new Data.GameRequest(saver.getInt("GameRequestRank",context),saver.getString("GameRequestName",context),saver.getString("GameRequestURL",context),saver.getBool("GameRequestHost",context),saver.getBool("GameRequestOnline",context));
    }
    public static boolean getIfFirstTime(Context context)
    {
        return saver.getBool("firstTime",context);
    }
    public static void cancelFirstTime(Context context)
    {
        saver.setBoll("firstTime",false,context);
    }
    public static int getGameBat(Context context)
    {
        return saver.getInt("gameBat",context);
    }
    public static void setGameBat(int to, Context context)
    {
        saver.setInt("gameBat",to,context);
    }
    public static boolean getFakeEnter(Context context)
    {
        return !saver.getBool("fakeEnter",context);
    }
    public static void setFakeEnter(boolean to,Context context)
    {
        saver.setBoll("fakeEnter",!to,context);
    }
    public static int getGamesNumberChess(Context context)
    {
        return saver.getInt("gamesNumberChess",context);
    }
    private static void setGamesNumber(int to,Context context)
    {
        saver.setInt("gamesNumberChess",to,context);
    }
    private static void addGamesNumberChess(int add, Context context)
    {
        saver.addInt("gamesNumberChess",add,context);
    }
    public static void addToChessGame(String add,int index, Context context)
    {
        saver.addString("game" + index + "CodeChess",add,context);
    }
    public static void writeNewChessGame(String code,boolean white,Context context)
    {
        saver.setBoll("game " + getGamesNumberChess(context) + "BoolChess",white,context);
        saver.setString("game" + getGamesNumberChess(context) + "CodeChess",code,context);
        addGamesNumberChess(1,context);
    }
    public static String getChessGameCode(int index,Context context)
    {
        return saver.getString("game" + index + "CodeChess",context);
    }
    public static boolean getChessGameWhite(int index, Context context)
    {
        return saver.getBool("game " + index + "BoolChess",context);
    }
    public static void setGameCode(int to, Context context)
    {
        saver.setInt("gameCode",to,context);
    }
    public static int getGameCode(Context context)
    {
        return saver.getInt("gameCode",context);
    }
    public static int getGameCodeGameLength(Context context)
    {
        final int gameCode = getGameCode(context);
        switch (gameCode)
        {
            case 1: return getGamesNumberChess(context);
        }
        return -1;
    }
    public static String getGameCodeGameCode(int index, Context context)
    {
        final int gameCode = getGameCode(context);
        switch (gameCode)
        {
            case 1: return getChessGameCode(index,context);
        }
        return "";
    }
    public static boolean getGameCodeGameStart(int index, Context context)
    {
        final int gameCode = getGameCode(context);
        switch (gameCode)
        {
            case 1: return getChessGameWhite(index,context);
        }
        return false;
    }
    public static boolean getWatchMode(Context context)
    {
        return saver.getBool("watchMode",context);
    }
    public static void setWatchMode(boolean to,Context context)
    {
        saver.setBoll("watchMode",to,context);
    }
    public static GameList.GameListGame getWatchGame(Context context)
    {
        return new GameList.GameListGame(saver.getString("watchModePlayerAName",context),saver.getString("watchModePlayerARank",context),saver.getString("watchModePlayerBName",context),saver.getString("watchModePlayerBRank",context),saver.getString("watchModeDate",context),saver.getString("watchModeCode",context),saver.getBool("watchModeWhite",context),saver.getString("watchModeGameReport",context),saver.getInt("watchModeIndex",context));
    }
    public static void setWatchGame(GameList.GameListGame game,Context context)
    {
        saver.setString("watchModePlayerAName",game.getPlayerAName(),context);
        saver.setString("watchModePlayerARank",game.getPlayerARank(),context);
        saver.setString("watchModePlayerBName",game.getPlayerBName(),context);
        saver.setString("watchModePlayerBRank",game.getPlayerBRank(),context);
        saver.setString("watchModeGameReport",game.getGameReport(),context);
        saver.setString("watchModeCode",game.getcode(),context);
        saver.setString("watchModeDate",game.getDate(),context);
        saver.setBoll("watchModeWhite",game.getPlayAsWhite(),context);
        saver.setInt("watchModeIndex",game.getIndex(),context);
    }

    public static int getRequestCancel(Context context)
    {
        return saver.getInt("requestCanceli",context);
    }
    public static void setRequestCancel(int to,Context context)
    {
        saver.setInt("requestCanceli",to,context);
    }






    public static void setUserVersionUpdateOrder(boolean to, Context context)
    {
        saver.setBoll("UserVersionUpdateOrder",to,context);
    }
    public static boolean getUserVersionUpdateOrder(Context context)
    {
        return saver.getBool("UserVersionUpdateOrder",context);
    }
}

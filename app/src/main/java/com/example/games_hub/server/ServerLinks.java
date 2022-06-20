package com.example.games_hub.server;

import com.example.games_hub.R;

import java.util.Random;

public class ServerLinks {



    public static String getUserListURL()
    {
        return "https://notepad.link/bdasads";
    }

    public static String getNewURL(int afterNumber)
    {
        if(afterNumber > 5)
        {
            StringBuilder sb = new StringBuilder("");
            for(int i = 0; i < afterNumber; i++)
            {
                sb.append(getURLApproveChar());
            }
            return "https://notepad.link/" + sb.toString();
        }
        else
        {
           return "error after number " + afterNumber + " is to small";
        }
    }
    public static String getNewURL()
    {
        return getNewURL(6);
    }


    private static Random random = new Random();
    private static char getURLApproveChar()
    {
        final int num = random.nextInt(36);
        if(num < 10)
            return (char)(num + 48);
        return (char)(num +87);
    }
    public static boolean URLisClean(String url)
    {
        return ServerConnector.getText(url).equals("");
    }

    public static String getChessWaitingServer()
    {
        return "https://notepad.link/dsadlsc";
    }
}

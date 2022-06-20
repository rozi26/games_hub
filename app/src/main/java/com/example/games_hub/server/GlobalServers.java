package com.example.games_hub.server;

import android.os.StrictMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GlobalServers {

    public static String getDate()
    {
        String date = getDataFrom("https://www.calendardate.com/todays.htm","ttop");
        System.out.println(date);
        int count = 0;
        int i;
        for(i = 0; i < date.length(); i++)
        {
            if(date.charAt(i) == ' ')
            {
                count++;
                if(count == 4)
                    break;
            }
        }
        return date.substring(i + 1);
    }
    public static String getTime()
    {
        return getDataFrom("https://www.timeanddate.com/worldclock/timezone/utc","ct");
    }
    private static String getDataFrom(String url, String id)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Element a = Jsoup.connect(url).get().getElementById(id);
            return a.text();
        } catch (IOException e) {
            System.out.println("crash////////////////////");
            e.printStackTrace();
            return "unknown";
        }
    }
}

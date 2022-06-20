package com.example.games_hub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games_hub.game.gameActivites.GameRoom;
import com.example.games_hub.game.games.chess.ChessHome;
import com.example.games_hub.users_activities.Login;
import com.example.games_hub.users_activities.Singup;
import com.example.games_hub.users_activities.UserPage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Data {
    public static int getUserVersion()
    {
        return 3;
    }
    public static int getUserDataLength() {return 8;}
    public static int[] getUserDefaults()
    {
        return new int[]
                {
                        -1,-1,-1, // (name)(password hash)(create date)
                        1000,//coins
                        10, //gems
                        1, //level
                        getUserVersion(), //version
                        800 // chess rank
                };

    }
    public static String[] getUserPropNames()
    {
        return new String[]{"user name", "password", "create data", "coins", "gems", "level", "version","chess rank"};
    }
    public static void doAnimation(View v, int a, Context context)
    {
        v.startAnimation(AnimationUtils.loadAnimation(context,a));
    }
    public void getImages(int imageNum)
    {
        switch (imageNum)
        {
            case 0:
        }
    }
    public static void makeToast(String text, Context context)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static Intent getPageCheck(Context thisPage, Context context)
    {
        final String page = DataMeneger.getPage(context);
        final String thisPageText = thisPage.toString();
        final StringBuilder thisPageB = new StringBuilder();
        for(int i = 0; i < thisPageText.length(); i++)
        {
            char a = thisPageText.charAt(i);
            if(a == '@')
                break;
            else
                thisPageB.append(a);
        }
        System .out.println("thisPage ["  + thisPageB + "] page [" + page.substring(6) + "]");
        if(page.substring(6).equals(thisPageB.toString()))
            return null;
        if(page.equals(Login.class.toString()))
            return new Intent(thisPage,Login.class);
        else if(page.equals(UserPage.class.toString()))
            return new Intent(thisPage,UserPage.class);
        else if(page.equals(Singup.class.toString()))
            return new Intent(thisPage,Singup.class);
        else if(page.equals(GameRoom.class.toString()))
            return new Intent(thisPage,GameRoom.class);
        else if(page.equals(ChessHome.class.toString()))
            return new Intent(thisPage,ChessHome.class);
        return null;
    }


    public static RelativeLayout getGameTub(Context _this, int price, boolean online, Drawable background1,Drawable background2, String text)
    {
        RelativeLayout relativeLayout = new RelativeLayout(_this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,225);
        layoutParams.setMargins(15,15,15,0);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackground(background1);
        TextView title = new TextView(_this);
        title.setText(text);
        title.setTextSize(22.5f);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            title.setId(View.generateViewId());
        }
        relativeLayout.addView(title, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout relativeLayout2 = new RelativeLayout(_this);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.setMargins(15,15,15,15);
        layoutParams2.addRule(RelativeLayout.BELOW,title.getId());
        relativeLayout2.setLayoutParams(layoutParams2);
        relativeLayout2.setBackground(background2);
        ImageView imageView = new ImageView(_this);
        RelativeLayout.LayoutParams imagePar = new RelativeLayout.LayoutParams(90,90);
        imagePar.setMargins(15,15,15,15);
        imageView.setLayoutParams(imagePar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            imageView.setId(View.generateViewId());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setForeground(_this.getResources().getDrawable(R.drawable.coin1));
        }
        relativeLayout2.addView(imageView);
        TextView priceT = new TextView(_this);
        RelativeLayout.LayoutParams priceTParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,90);
        priceTParams.addRule(RelativeLayout.RIGHT_OF);
        priceTParams.setMargins(15,0,15,15);
        priceTParams.addRule(RelativeLayout.RIGHT_OF,imageView.getId());
        priceT.setLayoutParams(priceTParams);
        priceT.setText("" + price);
        priceT.setGravity(Gravity.CENTER);
        priceT.setTextSize(19.5f);
        priceT.setTextColor(Color.rgb(10,91,254));
        relativeLayout2.addView(priceT);

        relativeLayout.addView(relativeLayout2);
        return relativeLayout;
    }

    public static boolean inRange(int num1, int num2, int range)
    {
        return Math.abs(num1 - num2) < range;
    }

    public static class GameRequest
    {
        final private int rank;
        final private String name;
        final private String url;
        final private boolean host;
        final boolean online;
        public GameRequest(int _rank, String _name, String _url, boolean _host, boolean _online)
        {
            rank = _rank;
            name = _name;
            url = _url;
            host = _host;
            online = _online;
        }
        public int getRank()
        {
            return rank;
        }
        public String getName()
        {
            return name;
        }
        public String getUrl()
        {
            return url;
        }
        public boolean isHost()
        {
            return host;
        }
        public boolean isOnline()
        {
            return online;
        }
        public String toString()
        {
            return getName() + "|" + getRank() + "|" + isHost() + "|" + getUrl();
        }
    }

    public static String noInternetMessage()
    {
        return "there is no connection try again";
    }

    public static String numberWithZeros(int num, int zeros)
    {
        StringBuilder text = new StringBuilder();
        while (num < Math.pow(10,zeros - 1))
        {
            text.append("0");
            zeros--;
        }
        text.append(Integer.toString(num));
        return text.toString();
    }
    public static double round(double a, int after)
    {
        double acr = Math.pow(10,after);
        return (((int)(a * acr)) / acr);
    }
    public static String getUserTime()
    {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy-HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

}

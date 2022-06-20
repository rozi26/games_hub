package com.example.games_hub.Users;

import android.content.Context;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;

public class User extends UserProp{
    private String url;
    public User(String data, String _url)
    {
        super(data);
        url = _url;
    }
    public User(String[] data, String _url, boolean thisIsNewUser)
    {
        super(data);
        url = _url;
    }
    public String toString()
    {
        String[] prop = Data.getUserPropNames();
        final String[] data = getData();
        StringBuilder text = new StringBuilder("");
        for(int i = 0; i < data.length; i++)
        {
            text.append(prop[i]);
            text.append(": ");
            text.append(data[i]);
            text.append("\t");
        }
        return text.toString();
    }
    public String getUrl()
    {
        return url;
    }


    public String checkVersion(Context context, Context _this)
    {
        if(getVersion() != Data.getUserVersion())
        {
            final int[] defaults = Data.getUserDefaults();
            String[] newData = new String[Data.getUserDataLength()];
            final String[] data = getData();
            for(int i = 0; i < newData.length; i++)
            {
                if(i < data.length)
                    newData[i] = data[i];
                else
                    newData[i] = Integer.toString(defaults[i]);
            }
            newData[6] = Integer.toString( Data.getUserVersion());
            updateData(newData);
            DataMeneger.saveUser(this,DataMeneger.getUserPassword(context),context);
            return getCode();
        }
        return "";
    }
    public void addCoins(int amount)
    {
        changeData(3,amount);
    }
    public void addGems(int amount)
    {
        changeData(4,amount);
    }
    public void addLevel(int amount)
    {
        changeData(5,amount);
    }
    public void addChessRank(int amount)
    {
        changeData(7,amount);
    }
    private void changeData(int loc, int amount)
    {
        String[] data = getData();
        data[loc] = Integer.toString(Integer.parseInt(data[loc]) + amount);
    }


}

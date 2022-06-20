package com.example.games_hub.Users;

public class UserProp {
    private String[] data;
    /*
    0 - user name
    1 - password hash
    2 - createData
    3 - coins
    4 - gems
    5 - level
    6 - version
    7 - chess rank
     */

    protected UserProp(String code)
    {
        //System.out.println(code);
        final int codeLength = code.length();
        if(code.length() < 3 || !(code.charAt(codeLength - 1) == '|'))
            System.out.println("error wrong code [" + code + "]");
        int count = 0;
        for(int i = 0; i < codeLength
                ; i++)
        {
            if(code.charAt(i) == '|')
                count++;
        }
        data = new String[count];
        int passCount = 0;
        StringBuilder text = new  StringBuilder("");
        for(int i = 0; i < code.length(); i++)
        {
            if(code.charAt(i) == '|')
            {
                data[passCount] = text.toString();
                text = new StringBuilder("");
                passCount++;
                if(passCount == count)
                    break;
            }
            else
                text.append(code.charAt(i));
        }
    }
    protected UserProp(String[] _data)
    {
        updateData(_data);
    }

    public String getCode()
    {
        StringBuilder code = new StringBuilder("");
        for(int i = 0; i < data.length; i++)
        {
            code.append(data[i]);
            code.append("|");
        }
        return code.toString();
    }
    public String getUserName()
    {
        return data[0];
    }
    public String getPassword()
    {
        return data[1];
    }
    public String getCreateDate()
    {
        return data[2];
    }
    public int getCoins()
    {
        return Integer.parseInt(data[3]);
    }
    public int getGems()
    {
        return Integer.parseInt(data[4]);
    }
    public int getLevel()
    {
        return Integer.parseInt(data[5]);
    }
    public int getVersion(){return  Integer.parseInt(data[6]);}
    public int getChessRank(){return  Integer.parseInt(data[7]);}
    protected String[] getData()
    {
        return data;
    }

    protected void updateData(String[] _data)
    {
        data = new String[_data.length];
        for(int i = 0; i < _data.length; i++)
        {
            data[i] = _data[i];
        }
    }
}

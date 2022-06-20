package com.example.games_hub.securty;

import java.security.MessageDigest;

public class Encryption {

    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static String cryptString(String input, String password)
    {
        String modelHash = sha256(password + "a");
        String converter  = "";
        for(int i = 0; i < 64; i++)
        {
            converter += (Character.isDigit(modelHash.charAt(i))?modelHash.charAt(i):Character.toUpperCase(modelHash.charAt(i)));
        }
        String newText = "";
        for(int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) == '\n')
                newText += "ÿ";
            else
            {
                int num = (int)(input.charAt(i)) + (int)converter.charAt(((int)(converter.charAt(i % 64)) + (i/3)) % 64) - 48;
                if(num > 126)
                    num += 34;
                newText += (char)(num);
            }
        }
        return newText;
    }
    public static String unCryptString(String code, String password)
    {
        String modelHash = sha256(password + "a");
        String converter  = "";
        for(int i = 0; i < 64; i++)
        {
            converter += (Character.isDigit(modelHash.charAt(i))?modelHash.charAt(i):Character.toUpperCase(modelHash.charAt(i)));
        }
        String newText = "";
        for(int i = 0; i < code.length(); i++)
        {
            if(code.charAt(i) == 'ÿ')
                newText += "\n";
            else
            {
                int num = (int)(code.charAt(i)) - (int)converter.charAt(((int)(converter.charAt(i % 64)) + (i/3)) % 64) + 48;
                if(num > 126)
                    num -= 34;
                newText += (char)(num);
            }
        }
        return newText;
    }

    public static String lightCrypt(String text, String password)
    {
        final StringBuilder model = new StringBuilder(sha256(password + "a"));
        for(int i = 0; i < model.length(); i++)
        {
            model.setCharAt(i,(char)((int)model.charAt(i) > 57?(char)((int)(model.charAt(i)) - 38):(int)(model.charAt(i))));
        }
        StringBuilder data = new StringBuilder();
        int modelNumNumber = 0;
        for(int i = 0; i < text.length(); i++)
        {
            char a = text.charAt(i);
            final int modelNum = (int)model.charAt((modelNumNumber * password.length() / 3) % model.length());
            if(lightApprove(a))
            {
                a = (char)(((int)a - modelNum) +  49);
            }
            else if((int)a  > 32 && (int)a < 48) // 32 < a < 48
            {
                data.append((char)((((int)a + modelNum) % 2) + 125));
                a = ((char)((int)a + modelNum));
                modelNumNumber++;
            }
            else if(((int)a < 127 && (int)a > 122))
            {
                data.append("|");
                modelNumNumber++;
            }

            data.append(a);
            modelNumNumber++;
        }
        return data.toString();
    }
    public static String lightDecrypt(String text, String password)
    {
        final StringBuilder model = new StringBuilder(sha256(password + "a"));
        for(int i = 0; i < model.length(); i++)
        {
            model.setCharAt(i,(char)((int)model.charAt(i) > 57?(char)((int)(model.charAt(i)) - 38):(int)(model.charAt(i))));
        }
        StringBuilder data = new StringBuilder();
        for(int i = 0; i < text.length(); i++)
        {
            final int modelNum = (int)model.charAt((i * password.length() / 3) % model.length());
            char a = text.charAt(i);
            if((int)a < 124 && (int)a > 32)
            {
                a = (char)(((int)a - 49) + modelNum);
            }
            else if(a == '|')
            {
                i++;
                a = text.charAt(i);
            }
            else if((int)a > 124)
            {
                i++;
                a = text.charAt(i);
                a = (char)((int) a - modelNum);
            }
            data.append(a);
        }
        return data.toString();
    }
    private static boolean lightApprove(char a)
    {
        final int loc  =(int) a;
        return (loc > 47 && loc < 123);
    }
}

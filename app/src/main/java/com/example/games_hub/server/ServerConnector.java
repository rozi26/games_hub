package com.example.games_hub.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;


public class ServerConnector {
    final Object lock;
    private WebView wb;
    private Boolean loaded;
    final String url;
    public ServerConnector(String _url, Context _context)
    {
        loaded = false;
        lock = new Object();
        wb = new WebView(_context);
        wb.loadUrl(_url);
        url = _url;
        WebSettings ws = wb.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLightTouchEnabled(true);
        wb.setClickable(true);

        wb.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loaded = true;
            }
        });

    }
    public boolean getLoaded()
    {
        return loaded;
    }
    public String getUrl()
    {
        return url;
    }
    @SuppressLint("JavascriptInterface")
    public  String getText() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Element a = Jsoup.connect(url).get().getElementById("content");
            return a.text();
        } catch (IOException e) {
            System.out.println("crash////////////////////");
            e.printStackTrace();
        }
        return "error";
    }

    @SuppressLint("NewApi")
    public void setText(String to)
    {
        //System.out.println("start////////////////////////////////////////////////");
        wb.evaluateJavascript("textarea.value = \"" + to + "\";",null);
    }
    @SuppressLint("NewApi")
    public void addNewUserText(String add)
    {
        String text = getText();
        if(text.length() > 0)
            text = text.substring(0,text.length() - 1);
        setText(text + add + "a");
    }
    public void addText(String add)
    {
        String text = getText();
        setText(text + add);
    }
    public static String getText(String url)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Element a = Jsoup.connect(url).get().getElementById("content");
            return a.text();
        } catch (IOException e) {
            System.out.println("crash////////////////////");
            e.printStackTrace();
        }
        return "ul";
    }

}

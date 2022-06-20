package com.example.games_hub.users_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.securty.Encryption;
import com.example.games_hub.server.GlobalServers;
import com.example.games_hub.server.ServerConnector;
import com.example.games_hub.server.ServerLinks;

public class Singup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        prevState = false; // if the order was right or wrong in the last check
        stage = 0; // the stage of the sing up
        data = new String[3]; // the user data
        names = new String[]{"name", "password", "password"}; // the name of the use data
        EditText t = findViewById(R.id.dataText);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showOrderRight();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setStage();
        for(int i = 0; i < data.length; i++)
        {
            data[i] = "";
        }
        globalUserConnector = new ServerConnector(ServerLinks.getUserListURL(),getApplicationContext());
        userUrl = ServerLinks.getNewURL();
        userPageConnector = new ServerConnector(userUrl,getApplicationContext());
        getUserBaseText();
    }
    private void getUserBaseText()
    {
        Runnable getBaseText  = new Runnable() {
        @Override
        public void run() {
            userBaseText = ServerConnector.getText(ServerLinks.getUserListURL());
        }
    };
        Thread tr = new Thread(getBaseText);
        tr.start();
    }
    String userBaseText = "ul";
    String userUrl;
    ServerConnector userPageConnector;
    ServerConnector globalUserConnector;
    int stage;
    int minLetters;
    String[] names;
    String[] data;

    private void setStage()
    {
        String order = "error";
        EditText dataText = findViewById(R.id.dataText);
        switch (stage)
        {
            case 0: order = "choose your username"; minLetters = 4; dataText.setInputType(InputType.TYPE_CLASS_TEXT); break;
            case 1: order = "choose your password"; minLetters = 8; dataText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);break;
            case 2: order = "confirm your password"; minLetters = -1; dataText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD); break;
            case 3:
                order = "creating your account"; minLetters = 0; break;
        }
        TextView order_place = findViewById(R.id.order);
        order_place.setText(order);
        TextView order_text = findViewById(R.id.orderText);
        if(minLetters == 0)
            order_text.setText("");
        else if(minLetters > 0)
            order_text.setText("length of " + minLetters + " letters");
        else
            order_text.setText("equal to the " + names[-minLetters]);
    }
    boolean prevState;
    private void showOrderRight()
    {
        EditText t = findViewById(R.id.dataText);
        final String text = t.getText().toString();
        TextView order2 = findViewById(R.id.orderText2);
        for(int i = 0; i < text.length(); i++)
        {
            final char a = text.charAt(i);
            boolean wrong = true;
            String name = "";
            switch (a)
            {
                case ' ': name = "space"; break;
                case '\t': name = "tab"; break;
                case '\n': name = "new line"; break;
                case '-': name = "hyphen"; break;
                default: wrong = false;  break;
            }
            if(wrong)
            {
                order2.setText("the character " + name + " doesn't allowed");
                order2.setTextColor(Color.RED);
                break;
            }
            else
            {
                order2.setText("no special characters");
                order2.setTextColor(Color.GREEN);
            }
        }
        if(orderRight())
        {
            TextView orderShower = findViewById(R.id.orderText);
            orderShower.setTextColor(Color.GREEN);
        }
        else if(!orderRight())
        {
            TextView orderShower = findViewById(R.id.orderText);
            orderShower.setTextColor(Color.RED);
        }
        final boolean order2Right = ((TextView)findViewById(R.id.orderText2)).getText().charAt(0) == 'n';
        if(orderRight() && order2Right && !prevState)
        {
            prevState = true;
            View v = findViewById(R.id.moveForward);
            v.setVisibility(View.VISIBLE);
            Data.doAnimation(v,R.anim.out_from_right_s,this);
        }
        else if((!orderRight() || !order2Right)&& prevState)
        {
            prevState = false;
            View v = findViewById(R.id.moveForward);
            v.setVisibility(View.INVISIBLE);
            Data.doAnimation(v,R.anim.out_to_right_s,this);
        }
    }
    private boolean orderRight()
    {
        TextView t = findViewById(R.id.dataText);
        final String text = t.getText().toString();
        if(minLetters > 0)
        {
            return text.length() >= minLetters;
        }
        else
            return text.equals(data[-minLetters]);
    }

    public void moveBack(View v)
    {
        EditText real = findViewById(R.id.dataText);
        EditText fake = findViewById(R.id.fakeData);
        data[stage] = real.getText().toString();
        if(stage == 2)
        {
            findViewById(R.id.orderText2).setVisibility(View.VISIBLE);
            Data.doAnimation(findViewById(R.id.orderText2),R.anim.dim_in_f,this);
        }
        stage--;
        fake.setText(real.getText());
        real.setText(data[stage]);
        textSwitchAnimationBackward(real,fake);
        if(stage == 0)
        {
            v.setVisibility(View.INVISIBLE);
            Data.doAnimation(v,R.anim.out_to_left_s,this);
        }
        setStage();
        showOrderRight();
    }
    public void moveForward(View v)
    {
        EditText real = findViewById(R.id.dataText);
        EditText fake = findViewById(R.id.fakeData);
        fake.setText(real.getText().toString());
        data[stage] = real.getText().toString();
        boolean pass = true;
        if(stage == 0)
        {
            if(data[stage].equals("computer"))
            {
                pass = false;
                Data.makeToast("the name computer doesn't allowed",this);
            }
            if(pass && !checkIfNameRight(data[stage]))
            {
                pass = false;
                if(userBaseText.equals("ul"))
                {
                    Data.makeToast("no connection try again",this);
                    getUserBaseText();
                }
                else
                    Data.makeToast("the name is taken",this);
            }
        }
        if(pass)
        {
            stage++;
            setStage();
            if(stage == 3)
            {
                stage4Activate();
            }
            else
            {
                if(stage == 2)
                {
                    findViewById(R.id.orderText2).setVisibility(View.INVISIBLE);
                    Data.doAnimation(findViewById(R.id.orderText2),R.anim.dim_out_f,this);
                }
                real.setText(data[stage]);
                textSwitchAnimationForward(real,fake);
                real.setText(data[stage]);
                if(stage == 1)
                {
                    View vb = findViewById(R.id.moveBack);
                    vb.setVisibility(View.VISIBLE);
                    Data.doAnimation(vb,R.anim.out_from_left_s,this);
                }
            }
        }
    }
    private void textSwitchAnimationForward(View real, View fake)
    {
        Data.doAnimation(real,R.anim.out_from_right_s,this);
        Data.doAnimation(fake,R.anim.out_to_left_s,this);
    }
    private void textSwitchAnimationBackward(View real, View fake)
    {
        Data.doAnimation(real,R.anim.out_from_left_s,this);
        Data.doAnimation(fake,R.anim.out_to_right_s,this);
    }


    private void stage4Activate()
    {
        //visual
        View v = findViewById(R.id.moveForward);
        v.setVisibility(View.INVISIBLE);
        Data.doAnimation(v,R.anim.out_to_right_s,this);
        View vb = findViewById(R.id.moveBack);
        vb.setVisibility(View.INVISIBLE);
        Data.doAnimation(vb,R.anim.out_to_left_s,this);
        TextView title = findViewById(R.id.title);
        findViewById(R.id.fakeData).setVisibility(View.INVISIBLE);

        //active
        Runnable r = new Runnable() {
            @Override
            public void run() {
                createAcount();
            }
        };
        Handler h = new Handler();
        h.post(r);
    }
    private void createAcount()
    {
        String[] userProp = new String[Data.getUserDataLength()];
        System.out.println("sha = " + data[0] + data[1]);
        userProp[0] = data[0]; // user name
        userProp[1] = Encryption.sha256(data[0] + data[1]); // password
        userProp[2] = GlobalServers.getDate(); // create date
        final int[] userDefault = Data.getUserDefaults();
        for(int i = 3; i < userDefault.length; i++)
        {
            userProp[i] = Integer.toString(userDefault[i]);
        }
        /*userProp[3] =  "1000";// coins
        userProp[4] = "10"; // gems
        userProp[5] = "1"; // level
        userProp[6] = Integer.toString(Data.getUserVersion());*/
        User user = new User(userProp,userUrl,true);
        DataMeneger.saveUser(user,data[1], getApplicationContext());
        userPageConnector.setText(user.getCode());
        System.out.println(userUrl);
        //System.out.println("gt[" + globalUserConnector.getText() + "]");
        String incryptURL = Encryption.lightCrypt(userUrl,data[1]);
        StringBuilder newIncryptURL = new StringBuilder();
        System.out.println("before " + incryptURL.toString());
        for(int i = 0; i < incryptURL.length(); i++)
        {
            char a = incryptURL.charAt(i);
            if(a == '\\')
                newIncryptURL.append('\\');
            newIncryptURL.append(a);
        }
        System.out.println("after " + newIncryptURL.toString());
        final String globalText = data[0] + " " + newIncryptURL.toString() + '\t';
        System.out.println("[" + globalText + "]");
        globalUserConnector.addNewUserText(globalText);
        System.out.println("userpage: " + userPageConnector.getLoaded());
        //System.out.println("global page: " + globalUserConnector.getLoaded());
        findViewById(R.id.dataText).clearAnimation();
        DataMeneger.cancelFirstTime(getApplicationContext());
        DataMeneger.setPage(UserPage.class.toString(),getApplicationContext());
        DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
        startActivity(new Intent(Singup.this,UserPage.class));
    }
    public void singInC(View v)
    {
        DataMeneger.setPage(Login.class.toString(),getApplicationContext());
        startActivity(new Intent(Singup.this,Login.class));
        overridePendingTransition(R.anim.page_from_right,R.anim.page_to_left);
        DataMeneger.setMoveAnimations(R.anim.page_from_right,R.anim.page_to_left,getApplicationContext());
    }
    private boolean checkIfNameRight(String name)
    {
        if(userBaseText.equals("ul"))
            return false;
        boolean newText = true;
        StringBuilder nameB = new StringBuilder("");
        for(int i = 0; i < userBaseText.length(); i++)
        {
            final char a = userBaseText.charAt(i);
            if(newText)
            {
                if(a == ' ')
                {
                    if(nameB.toString().equals(name))
                        return false;
                    newText = false;
                    nameB = new StringBuilder("");
                }
                else
                    nameB.append(a);
            }
            if(!newText && a == '\t')
            {
                newText = true;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = Data.getPageCheck(Singup.this,getApplicationContext());
        if(i != null)
        {
            startActivity(i);
            overridePendingTransition(R.anim.no_anim,R.anim.no_anim);
        }
        else
        {
            int[] ans = DataMeneger.getMoveAnimations(getApplicationContext());
            overridePendingTransition(ans[0],ans[1]);
            DataMeneger.setMoveAnimations(R.anim.no_anim,R.anim.no_anim,getApplicationContext());
        }
    }
}
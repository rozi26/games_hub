package com.example.games_hub.users_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.games_hub.Data;
import com.example.games_hub.DataMeneger;
import com.example.games_hub.R;
import com.example.games_hub.Users.User;
import com.example.games_hub.securty.Encryption;
import com.example.games_hub.server.ServerConnector;
import com.example.games_hub.server.ServerLinks;

public class Login extends AppCompatActivity {
    boolean userFill;
    boolean passwordFill;
    boolean passwordPrev;
    boolean prevFill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        susDone = false;
        userFill = false;
        passwordFill = false;
        prevFill = false;
        passwordPrev = false;
        if(true) // change listen
        {
            EditText t1 = findViewById(R.id.userName);
            EditText t2 = findViewById(R.id.password);
            t1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    userFill = !t1.getText().toString().equals("");
                    if(passwordFill)
                        susDone = false;
                    checkFill();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            t2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    passwordFill = !t2.getText().toString().equals("");
                    checkFill();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        s = new ServerConnector(ServerLinks.getUserListURL(),getApplicationContext());
        TextView title = findViewById(R.id.title);
        _this = this;
        warninger = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = Data.getPageCheck(Login.this,getApplicationContext());
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

    ServerConnector s;
    String susURL;
    boolean susDone;
    private void checkFill()
    {
        if(passwordFill)
        {
            Runnable finer = new Runnable() {
                @Override
                public void run() {
                    final String text = ServerConnector.getText(ServerLinks.getUserListURL());
                    final String userName =  ((TextView)findViewById(R.id.userName)).getText().toString();
                    setSus(text,userName);
                    susDone = true;
                }
            };
            Thread t = new Thread(finer);
            t.start();
        }
        if(userFill && passwordFill && !prevFill)
        {
            prevFill = true;
            Button button = findViewById(R.id.nextButton);
            button.setVisibility(View.VISIBLE);
            Data.doAnimation(button,R.anim.out_from_left_b,this);
        }
        if((!userFill || !passwordFill) && prevFill)
        {
            prevFill = false;
            Button button = findViewById(R.id.nextButton);
            button.setVisibility(View.INVISIBLE);
            Data.doAnimation(button,R.anim.out_to_left_b,this);
        }
    }
    private void setSus(String text, String name)
    {
        susURL = "";
        boolean newLine = true;
        StringBuilder nameT = new StringBuilder("");
        StringBuilder url = new StringBuilder("");
        for(int i = 0; i < text.length(); i++)
        {
            //System.out.println("[" + text.charAt(i) + "](" + (int)(text.charAt(i)) + ")");
            if(newLine)
            {
                if(text.charAt(i) == ' ')
                    newLine = false;
                else
                    nameT.append(text.charAt(i));
            }
            else if(text.charAt(i) == '\t')
            {
                newLine = true;
                if(nameT.toString().equals(name))
                {
                    susURL = url.toString();
                    break;
                }
                nameT = new StringBuilder("");
                url = new StringBuilder("");
            }
            else
                url.append(text.charAt(i));
        }
    }
    Context _this;
    String warninger;
    boolean done;
    public void loginC(View v)
    {
        TextView warning = findViewById(R.id.error_teller);
        if(susDone)
        {
            if(!susURL.equals(""))
            {

                final String userName = ((TextView)(findViewById(R.id.userName))).getText().toString();
                final String password = ((TextView)(findViewById(R.id.password))).getText().toString();
                Runnable verify = new Runnable() {
                    @Override
                    public void run() {
                        done = true;
                        final String URL = Encryption.lightDecrypt(susURL,password);
                        System.out.println(password);
                        if(URL.startsWith("https://"))
                        {
                            System.out.println("url: ["+ URL + "]\nsusURL: [" + susURL + "]");
                            final String sus = ServerConnector.getText(URL);
                            System.out.println("sus: [" + sus + "]");
                            User user = new User(sus,URL);
                            System.out.println(user.getPassword());
                            System.out.println("sha = " + userName + password);
                            System.out.println(Encryption.sha256(userName + password));
                            if(Encryption.sha256(userName + password).equals(user.getPassword()))
                            {
                                DataMeneger.saveUser(user,password,getApplicationContext());
                                DataMeneger.setPage(UserPage.class.toString(),getApplicationContext());
                                startActivity(new Intent(Login.this,UserPage.class));
                                DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
                            }
                            else
                            {
                                done = false;
                                warninger = "wrong password";
                            }
                        }
                        else
                        {
                            done = false;
                            warninger = "wrong password";
                        }
                    }
                };
                Thread ver = new Thread(verify);
                ver.start();
                Runnable war = new Runnable() {
                    @Override
                    public void run() {
                        if(!warninger.equals(""))
                            printWarning(warninger);
                        if(!done)
                        {
                            Data.doAnimation(findViewById(R.id.userName),R.anim.out_from_right_s,_this);
                            Data.doAnimation(findViewById(R.id.password),R.anim.out_from_right_s,_this);
                            findViewById(R.id.userName).setVisibility(View.VISIBLE);
                            findViewById(R.id.password).setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            /*Data.doAnimation(findViewById(R.id.userName),R.anim.out_to_right_s,_this);
                            Data.doAnimation(findViewById(R.id.password),R.anim.out_to_right_s,_this);
                            findViewById(R.id.userName).setVisibility(View.INVISIBLE);
                            findViewById(R.id.password).setVisibility(View.INVISIBLE);*/
                        }
                    }
                };
                Handler h = new Handler();
                h.post(war);
                Data.doAnimation(findViewById(R.id.userName),R.anim.out_to_right_s,_this);
                Data.doAnimation(findViewById(R.id.password),R.anim.out_to_right_s,_this);
                findViewById(R.id.userName).setVisibility(View.INVISIBLE);
                findViewById(R.id.password).setVisibility(View.INVISIBLE);
            }
            else
            {
                printWarning("user name doesn't exist");
            }
        }
        else
        {
            printWarning("isn't connected try again");
        }
    }
    private void printWarning(String text)
    {
        TextView warning = findViewById(R.id.error_teller);
        warning.setText(text);
        Data.doAnimation(warning,R.anim.fade_slow,this);
        warninger = "";
    }
    public void singUpC(View v)
    {
        DataMeneger.setPage(Singup.class.toString(),getApplicationContext());
        startActivity(new Intent(Login.this,Singup.class));
        DataMeneger.setMoveAnimations(R.anim.page_from_left,R.anim.page_to_right,getApplicationContext());
    }


}
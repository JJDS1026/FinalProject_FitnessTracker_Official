package com.example.group6.finalproject_fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class LoginActivity extends AppCompatActivity {

    @ViewById
    EditText username;

    @ViewById
    EditText userpassword;

    @ViewById
    CheckBox remember;

    @ViewById
    TextView usernametext;

    @ViewById
    TextView userpasswordtext;

    @ViewById
    Button sign;

    @ViewById
    Button reg;

    @ViewById
    Button clear;

    SharedPreferences info;
    String u, p;
    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @AfterViews
    public void init()
    {
        SharedPreferences info = getSharedPreferences("data", 0);
        String welc = info.getString("UUIDS", "");
        String c = info.getString("check","");

        realm = Realm.getDefaultInstance();
        RealmResults<User> list = realm.where(User.class).findAll();
        User id =  realm.where(User.class).equalTo("uuid", welc).findFirst();
        if (id!=null){
            String nem = id.getUsername();
            String pes = id.getPassword();
            if (c.equals("true")){
                username.setText(nem);
                userpassword.setText(pes);
                remember.setChecked(true);
            }

        }


    }

    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }


    @Click
    public void sign()
    {
        String u = username.getText().toString();
        String p = userpassword.getText().toString();
        info = getSharedPreferences("data", 0);
        RealmResults<User> t = realm.where(User.class).findAll();
        User ename =  realm.where(User.class).equalTo("usernames", username.getText().toString()).findFirst();
        if (((realm.where(User.class)).count())==0){
            Toast.makeText(this, "Nothing saved", Toast.LENGTH_SHORT).show();}
        else {
            if (ename != null) {
                if ((ename.getUsername()).equals(u) && (ename.getPassword()).equals(p)){
                    info = getSharedPreferences("data", 0);
                    SharedPreferences.Editor editor = info.edit();
                    editor.putString("UUIDS",(ename.getUuid()));
                    editor.apply();
                    finish();

                    Intent w = new Intent(this, WelcomeActivity_.class);
                    startActivity(w);

                }
                else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                }
            }
            else{
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Click public void reg()
    {
        Intent h = new Intent(this, AdminActivity_.class);
        startActivity(h);
    }

    @Click public void clear()
    {
        info = getSharedPreferences("data", 0);
        info.edit().clear().apply();
        Toast.makeText(this, "Preferences cleared", Toast.LENGTH_SHORT).show();
    }

    @Click public void remember() {
        info = getSharedPreferences("data", 0);
        String u = username.getText().toString();
        String p = userpassword.getText().toString();
        if (remember.isChecked()){
            if (!u.equals("") && !p.equals("")){
                SharedPreferences info = getSharedPreferences("data", 0);
                RealmResults<User> list = realm.where(User.class).findAll();
                User id =  realm.where(User.class).equalTo("usernames", username.getText().toString()).findFirst();
                if (id!=null){
                    if ((id.getUsername()).equals(u) && (id.getPassword()).equals(p)){
                        SharedPreferences.Editor editor = info.edit();
                        editor.putString("check", "true");
                        editor.apply();
                    }
                }
            }
            else{
                SharedPreferences.Editor editor = info.edit();
                editor.putString("check", "false");
                editor.apply();
            }
        }
        else {
            SharedPreferences.Editor editor = info.edit();
            editor.putString("check", "false");
            editor.apply();
        }
    }
}
package madriaga.labs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class Admin extends AppCompatActivity {
    @ViewById
    RecyclerView recyclerView;
    @ViewById
    Button clearu;
    @ViewById
    Button addu;
    Realm realm;
    SharedPreferences info;
    AlertDialog.Builder builder;


    @AfterViews
    public void init(){


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        realm = Realm.getDefaultInstance();

        RealmResults<User> list = realm.where(User.class).findAll();
        UserAdapter ua = new UserAdapter(this, list, true);

        recyclerView.setAdapter(ua);




    }

    public void onDestroy()
    {
        super.onDestroy();
        if (!realm.isClosed())
        {
            realm.close();
        }
    }

    @Click
    public void addu()
    {

        Intent h = new Intent(this, Register_.class);
        startActivity(h);
    }

    @Click
    public void clearu()
    {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear all registered users?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();


            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Sure?");
        alert.show();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void delUser(User newUser)
    {
        if (newUser.isValid()) {
            realm.beginTransaction();
            newUser.deleteFromRealm();
            realm.commitTransaction();
        }
    }
    public void editrUser(User newUser)
    {
        info = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = info.edit();
        editor.putString("UUIDS",(newUser.getUuid()));
        editor.apply();
        finish();

        //String uuid = newUser.getUuid();


        Intent h = new Intent(this, EditUsers_.class);
        startActivity(h);
        //EditUsers_.intent(this).uuidString(uuid).start();

    }

}
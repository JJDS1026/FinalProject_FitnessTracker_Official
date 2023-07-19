package madriaga.labs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class Welcome extends AppCompatActivity {
    @ViewById
    TextView welcome;
    @ViewById
    TextView userheight;
    @ViewById
    TextView userweight;
    @ViewById
    TextView userfullname;
    @ViewById
    TextView userdesc;
    @ViewById
    Button savuse;
    @ViewById
    Button backuse;
    @ViewById
    Button deluse;

    @ViewById
    ImageView imageView3;

    Realm realm;
    public static int REQUEST_CODE_IMAGE_SCREEN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    @AfterViews
    public void init()
    {
        SharedPreferences info = getSharedPreferences("data", 0);
        String welc = info.getString("UUIDS", "");
        String rem = "";
        String c = info.getString("check","");

        realm = Realm.getDefaultInstance();
        RealmResults<User> list = realm.where(User.class).findAll();
        User id =  realm.where(User.class).equalTo("uuid", welc).findFirst();
        String nem = id.getUsernames();

        if (c.equals("true")){
            rem = (", you will be remembered!");
            welcome.setText("Welcome, "+nem +rem);
        }
        else {
            welcome.setText("Welcome, "+nem +rem);
        }
        File imageDir = getExternalCacheDir();
        File imageFile = new File(imageDir, welc+".jpeg");
        if (imageFile.exists()) {
            refreshImageView(imageView3, imageFile);
        }
    }
    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }
    private void refreshImageView(ImageView imageView, File savedImage) {


        // this will put the image saved to the file system to the imageview
        Picasso.get()
                .load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }

    @Click
    public void savuse(){
        String ufn = userfullname.getText().toString();
        String ush = userheight.getText().toString();
        String usw =userweight.getText().toString();
        String usde =userdesc.getText().toString();
        if (!ush.equals("") && !usw.equals("")) {
            Float calcbmi = (Float.valueOf(usw) / (Float.valueOf(ush) * Float.valueOf(ush)));
            if (!ufn.equals("") && !ush.equals("") && !usw.equals("") && !usde.equals("")) {
                SharedPreferences info = getSharedPreferences("data", 0);
                String welc = info.getString("UUIDS", "");
                User newUser = realm.where(User.class).equalTo("uuid", welc).findFirst();

                realm.beginTransaction();
                newUser.setRealname(ufn);
                newUser.setHeight(Float.valueOf(ush));
                newUser.setWeight(Float.valueOf(usw));
                newUser.setDescription(usde);
                newUser.setBmi(calcbmi);
                realm.commitTransaction();
                try{
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(newUser);
                    realm.commitTransaction();

                    Toast t = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
                    t.show();
                    Toast.makeText(this, "test show realm"+newUser, Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast t = Toast.makeText(this, "Error saving", Toast.LENGTH_LONG);
                    t.show();
                }
            }
            else {Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();}

        }
        else {Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();}

    }

    @Click
    public void backuse(){Intent h = new Intent(this, Login_.class);
        startActivity(h);}

    @Click
    public void deluse(){
        SharedPreferences info = getSharedPreferences("data", 0);
        String welc = info.getString("UUIDS", "");
        User newUser = realm.where(User.class).equalTo("uuid", welc).findFirst();
        if (newUser.isValid()) {
            realm.beginTransaction();
            newUser.deleteFromRealm();
            realm.commitTransaction();
            Intent h = new Intent(this, Login_.class);
            startActivity(h);
        }
    }





}
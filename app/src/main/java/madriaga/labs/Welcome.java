package madriaga.labs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
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




}
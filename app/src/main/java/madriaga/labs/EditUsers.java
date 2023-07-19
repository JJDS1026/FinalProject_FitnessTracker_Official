package madriaga.labs;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class EditUsers extends AppCompatActivity {
    /*@ViewById
    TextView uuid;*/
    @ViewById
    EditText usernameed;
    @ViewById
    EditText userpassworded;
    @ViewById
    EditText regpassconfirmed;
    @ViewById
    Button saveed;
    @ViewById
    Button canceled;
    @ViewById
    ImageView imageView2;

    Realm realm;

    public static int REQUEST_CODE_IMAGE_SCREEN = 0;

    @Click
    public void imageView2()
    {
        ImageActivity_.intent(this).startForResult(REQUEST_CODE_IMAGE_SCREEN);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users);
    }

    @AfterViews
    public void checkPermissions()
    {
        // REQUEST PERMISSIONS for Android 6+
        // THESE PERMISSIONS SHOULD MATCH THE ONES IN THE MANIFEST
        Dexter.withContext(this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                )

                .withListener(new BaseMultiplePermissionsListener()
                {
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
                            // all permissions accepted proceed
                            init();
                        }
                        else
                        {
                            // notify about permissions
                            toastRequirePermissions();
                        }
                    }
                })
                .check();
    }
    public void toastRequirePermissions()
    {
        Toast.makeText(this, "You must provide permissions for app to run", Toast.LENGTH_LONG).show();
        finish();
    }


    public void init()
    {
        realm = Realm.getDefaultInstance();
        SharedPreferences info = getSharedPreferences("data", 0);
        String welc = info.getString("UUIDS", "");

        RealmResults<User> list = realm.where(User.class).findAll();
        User id =  realm.where(User.class).equalTo("uuid", welc).findFirst();
        String nem = id.getUsernames();
        String pes = id.getPasswords();


        User newUser = realm.where(User.class).equalTo("uuid", welc).findFirst();

        if (newUser!=null)
        {
            usernameed.setText(nem);
            userpassworded.setText(pes);
            regpassconfirmed.setText(pes);
        }

        File imageDir = getExternalCacheDir();
        File imageFile = new File(imageDir, welc+".jpeg");
        if (imageFile.exists()) {
            refreshImageView(imageView2, imageFile);
        }

    }

    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode==REQUEST_CODE_IMAGE_SCREEN)
        {
            if (responseCode==ImageActivity.RESULT_CODE_IMAGE_TAKEN)
            {
                // receieve the raw JPEG data from ImageActivity
                // this can be saved to a file or save elsewhere like Realm or online
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    // save rawImage to file
                    SharedPreferences info = getSharedPreferences("data", 0);
                    String welc = info.getString("UUIDS", "");
                    File savedImage = saveFile(jpeg, welc+".jpeg");

                    // load file to the image view via picasso
                    refreshImageView(imageView2, savedImage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    private File saveFile(byte[] jpeg, String filename) throws IOException
    {
        // this is the root directory for the images
        File getImageDir = getExternalCacheDir();

        // just a sample, normally you have a diff image name each time
        File savedImage = new File(getImageDir, filename);


        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
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
    public void saveed()
    {
        String user = usernameed.getText().toString();
        String pass = userpassworded.getText().toString();
        String pass2 = regpassconfirmed.getText().toString();
        RealmResults<User> list = realm.where(User.class).findAll();
        User ename =  realm.where(User.class).equalTo("usernames", usernameed.getText().toString()).findFirst();


        if (user.equals("")) {
            Toast.makeText(this, "Name must not be blank", Toast.LENGTH_SHORT).show();
        }
        else {
            if (pass.equals("")){
                Toast.makeText(this, "Password must not be blank", Toast.LENGTH_SHORT).show();
            }
            else{
                if (pass.equals(pass2)){
                    //if (ename == null){
                        SharedPreferences info = getSharedPreferences("data", 0);
                        String welc = info.getString("UUIDS", "");
                        User newUser = realm.where(User.class).equalTo("uuid", welc).findFirst();

                        realm.beginTransaction();
                        newUser.setUsernames(user);
                        newUser.setPasswords(pass);
                        realm.commitTransaction();
                        try{
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(newUser);
                            realm.commitTransaction();
                            finish();

                        }
                        catch(Exception e)
                        {
                            Toast t = Toast.makeText(this, "Error saving", Toast.LENGTH_LONG);
                            t.show();
                        }

                    //}
                    //else {Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();}



                }
                else{
                    Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
                }

            }

        }


    }
    @Click
    public void canceled(){finish();}
}
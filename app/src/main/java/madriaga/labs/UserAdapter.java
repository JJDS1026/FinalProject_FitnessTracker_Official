package madriaga.labs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class UserAdapter extends RealmRecyclerViewAdapter<User, UserAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView una;
        TextView upa;
        Button editr;
        Button del;

        ImageView profpic;

        public ViewHolder(View item)
        {
            super(item);
            profpic = item.findViewById(R.id.imageView);
            una = item.findViewById(R.id.una);
            upa = item.findViewById(R.id.upa);
            editr = item.findViewById(R.id.editr);
            del = item.findViewById(R.id.del);
        }
    }
    Admin activity;

    public UserAdapter(Admin activity, @Nullable OrderedRealmCollection<User> data, boolean autoUpdate) {
        super(data, autoUpdate);
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = activity.getLayoutInflater().inflate(R.layout.rowslayout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        User h = getItem(position);

        holder.una.setText(h.getUsernames());
        holder.upa.setText(h.getPasswords());

        holder.del.setTag(h);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.delUser((User) view.getTag());
            }
        });

        holder.editr.setTag(h);
        holder.editr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.editrUser((User) view.getTag());
            }
        });

        File getImageDir = activity.getExternalCacheDir();  // this method is in the Activity class

        // just a sample, normally you have a diff image name each time
        File file = new File(getImageDir, h.getUuid()+".jpeg");

        if (file.exists()) {
            // this will put the image saved to the file system to the imageview
            Picasso.get()
                    .load(file)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.profpic);
        }
        else {
            // use a default picture
            holder.profpic.setImageResource(R.mipmap.ic_launcher);
        }


    }








}


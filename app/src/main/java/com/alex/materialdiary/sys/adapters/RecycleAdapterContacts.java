package com.alex.materialdiary.sys.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.ContactsFragment;
import com.alex.materialdiary.MarksFragment;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.DownloadImageTask;
import com.alex.materialdiary.sys.ImageLoader;
import com.alex.materialdiary.sys.common.CommonAPI;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriodData;

import java.util.List;

public class RecycleAdapterContacts extends RecyclerView.Adapter<RecycleAdapterContacts.ViewHolder>{

    private final ContactsFragment mf;
    private final LayoutInflater inflater;
    private ImageLoader imageLoader = null;
    List<String> Logins;
    List<String> Names;
    List<Integer> Unreaded;
    List<Boolean> isGroup;

    public RecycleAdapterContacts(ContactsFragment context, List<String> Logins, List<String> Names, List<Integer> Unreaded, List<Boolean> isGroup) {
        this.inflater = LayoutInflater.from(context.requireContext());
        imageLoader = ImageLoader.getInstance(context.requireContext());
        mf = context;
        this.Logins = Logins;
        this.Names = Names;
        this.Unreaded = Unreaded;
        this.isGroup = isGroup;
    }

    @NonNull
    @Override
    public RecycleAdapterContacts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterContacts.ViewHolder holder, int position) {
        holder.Names.setText(Names.get(position));
        holder.Unread.setText(Unreaded.get(position).toString());
        if (Unreaded.get(position) == 0) holder.Unread.setText("");
        if (isGroup.get(position)) holder.profileImage.setImageResource(R.drawable.group);
        else imageLoader.DisplayImage("https://pskovedu.ml/api/images/" + Logins.get(position), holder.profileImage);
        Log.d("rwadapter", String.valueOf(Names.get(position)));
        holder.Names.setOnClickListener(v -> mf.openMsg(Names.get(position), Logins.get(position), isGroup.get(position)));
    }

    @Override
    public int getItemCount() {
        if (Logins == null) return 0;
        return isGroup.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView profileImage;
        TextView Names;
        TextView Unread;
        ViewHolder(View view){
            super(view);
            Names = view.findViewById(R.id.contact_name);
            Unread = view.findViewById(R.id.unreadedM);
            profileImage = view.findViewById(R.id.user_icon);
        }
    }
}
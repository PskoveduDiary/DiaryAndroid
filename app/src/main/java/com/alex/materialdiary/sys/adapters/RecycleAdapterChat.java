package com.alex.materialdiary.sys.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.ChatFragment;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriodData;

import java.util.List;

public class RecycleAdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    LayoutInflater inflater;
    ChatFragment context;
    List<String> Logins;
    List<String> dates;
    List<String> text;
    List<Boolean> readed;
    public RecycleAdapterChat(ChatFragment context, List<String> Logins, List<String> dates, List<String> text, List<Boolean> readed) {
        this.inflater = LayoutInflater.from(context.requireContext());
        this.context = context;
        this.Logins = Logins;
        this.dates = dates;
        this.text = text;
        this.readed = readed;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = inflater.inflate(R.layout.me_message_item, parent, false);
                return new MeViewHolder(view);
            case 2:
                view = inflater.inflate(R.layout.other_message_item, parent, false);
                return new OtherViewHolder(view);
            default:
                view = inflater.inflate(R.layout.single_message, parent, false);
                return new OtherViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                MeViewHolder meViewHolder = (MeViewHolder)holder;
                meViewHolder.msgReaded.setVisibility(readed.get(position) ? View.VISIBLE : View.GONE);
                meViewHolder.timeText.setText(dates.get(position));
                meViewHolder.msgText.setText(text.get(position));
                break;

            case 2:
                OtherViewHolder otherViewHolder = (OtherViewHolder)holder;
                otherViewHolder.nameText.setText(Logins.get(position));
                otherViewHolder.timeText.setText(dates.get(position));
                otherViewHolder.msgText.setText(text.get(position));
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        String name = Logins.get(position);
        if (name == "Вы") return 1;
        return 2;
    }
    @Override
    public int getItemCount() {
        if (Logins == null) return 0;
        return Logins.size();
    }
    public static class MeViewHolder extends RecyclerView.ViewHolder {
        final TextView dateText;
        final ImageView msgReaded;
        final TextView msgText;
        final TextView timeText;

        MeViewHolder(View view){
            super(view);
            this.dateText = view.findViewById(R.id.dateText);
            this.msgReaded = view.findViewById(R.id.msgReaded);
            this.msgText = view.findViewById(R.id.msgText);
            this.timeText = view.findViewById(R.id.timeText);
        }
    }
    public static class OtherViewHolder extends RecyclerView.ViewHolder {
        final TextView dateText;
        final TextView msgText;
        final TextView timeText;
        final TextView nameText;

        OtherViewHolder(View view){
            super(view);
            this.dateText = view.findViewById(R.id.dateText);
            this.msgText = view.findViewById(R.id.msgText);
            this.timeText = view.findViewById(R.id.timeText);
            this.nameText = view.findViewById(R.id.nameView);
        }
    }
}

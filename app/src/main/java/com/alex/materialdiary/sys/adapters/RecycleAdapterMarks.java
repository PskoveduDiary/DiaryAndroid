package com.alex.materialdiary.sys.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.period_marks.Mark;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class RecycleAdapterMarks extends RecyclerView.Adapter<RecycleAdapterMarks.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Mark> periods;
    private final List<com.alex.materialdiary.sys.common.models.marks.Mark> diffs;

    public RecycleAdapterMarks(Context context, List<Mark> periods, List<com.alex.materialdiary.sys.common.models.marks.Mark> diffs) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
        this.diffs = diffs;
    }

    @NonNull
    @Override
    public RecycleAdapterMarks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mark mark = periods.get(position);
        MaterialCardView itemView = (MaterialCardView) holder.itemView;
        itemView.setStrokeColor(inflater.getContext().getResources().getColor(R.color.gray));
        if (diffs.contains(new com.alex.materialdiary.sys.common.models.marks.Mark(mark.getValue(), mark.getDate()))){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    itemView.setStrokeColor(Color.RED);
            }
        }
        Context c = inflater.getContext();
        Object year = Calendar.getInstance().get(Calendar.YEAR);
        String yearr = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if(mark.getDate() != null){
            String text = mark.getDate();
            try {
                String pr_year = String.valueOf(((int)year) - 1);
                text = text.replace("." + pr_year, "");
            } catch (Exception ignored) {
            }
            holder.date.setText(text.replace("." + yearr, ""));
        }
        holder.mark.setText(String.valueOf(mark.getValue()));
        //holder.itemView.setOnClickListener(v -> Toast.makeText(c, String.valueOf(mark.get), Toast.LENGTH_SHORT).show());
        if (mark.getValue() == 5) holder.mark.setTextColor(c.getResources().getColor(R.color.five));
        else if (mark.getValue() == 4) holder.mark.setTextColor(c.getResources().getColor(R.color.four));
        else if (mark.getValue() == 3) holder.mark.setTextColor(c.getResources().getColor(R.color.three));
        else if (mark.getValue() == 2) holder.mark.setTextColor(c.getResources().getColor(R.color.two));
        else if (mark.getValue() == 1) holder.mark.setTextColor(c.getResources().getColor(R.color.one));
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView mark;

        ViewHolder(View view){
            super(view);
            date = view.findViewById(R.id.date_of_mark);
            mark = view.findViewById(R.id.mark);
        }
    }
}

package com.alex.materialdiary.sys.adapters;

import android.content.Context;
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

import java.util.Calendar;
import java.util.List;

import javax.security.auth.callback.Callback;

public class RecycleAdapterMarks extends RecyclerView.Adapter<RecycleAdapterMarks.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Mark> periods;

    public RecycleAdapterMarks(Context context, List<Mark> periods) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
    }

    @NonNull
    @Override
    public RecycleAdapterMarks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterMarks.ViewHolder holder, int position) {
        Mark mark = periods.get(position);
        Context c = inflater.getContext();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        holder.date.setText(mark.getDate().replace("." + year, ""));
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

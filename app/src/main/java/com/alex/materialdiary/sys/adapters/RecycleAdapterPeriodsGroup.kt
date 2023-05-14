package com.alex.materialdiary.sys.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.period_marks.Mark;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.alex.materialdiary.sys.common.models.periods.Datum;
import com.alex.materialdiary.sys.common.models.periods.Period;
import com.alex.materialdiary.sys.common.models.periods.Periods;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class RecycleAdapterPeriodsGroup extends RecyclerView.Adapter<RecycleAdapterPeriodsGroup.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Datum> periods;

    public RecycleAdapterPeriodsGroup(Context context, List<Datum> periods) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapterPeriodsGroup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterPeriodsGroup.ViewHolder holder, int position) {
        Datum period = periods.get(position);
        holder.name.setText(period.getName());
        holder.info.setVisibility(View.GONE);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(llm);
        holder.recyclerView.setAdapter(new RecycleAdapterMarksInPeriod(context, period.getPeriods()));
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final RecyclerView recyclerView;
        final ImageView info;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.MarksLessonName);
            view.findViewById(R.id.MarksAverage).setVisibility(View.GONE);
            recyclerView = view.findViewById(R.id.marks_recycle);
            info = view.findViewById(R.id.info);
        }
    }
}

class RecycleAdapterMarksInPeriod extends RecyclerView.Adapter<RecycleAdapterMarksInPeriod.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Period> periods;

    public RecycleAdapterMarksInPeriod(Context context, List<Period> periods) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
    }

    @NonNull
    @Override
    public RecycleAdapterMarksInPeriod.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterMarksInPeriod.ViewHolder holder, int position) {
        Period mark = periods.get(position);
        Context c = inflater.getContext();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        holder.date.setText(mark.getName());
        if (mark.getMark() != null) {
            holder.mark.setText(mark.getMark().getShortName());
            //holder.itemView.setOnClickListener(v -> Toast.makeText(c, String.valueOf(mark.get), Toast.LENGTH_SHORT).show());
            if (mark.getMark().getValue() == 5)
                holder.mark.setTextColor(c.getResources().getColor(R.color.five));
            else if (mark.getMark().getValue() == 4)
                holder.mark.setTextColor(c.getResources().getColor(R.color.four));
            else if (mark.getMark().getValue() == 3)
                holder.mark.setTextColor(c.getResources().getColor(R.color.three));
            else if (mark.getMark().getValue() == 2)
                holder.mark.setTextColor(c.getResources().getColor(R.color.two));
            else if (mark.getMark().getValue() == 1)
                holder.mark.setTextColor(c.getResources().getColor(R.color.one));
        }
        else {
            holder.mark.setText(new DecimalFormat("#0.00").format(mark.getAverage()));
            holder.mark.setTextColor(c.getResources().getColor(R.color.gray));
        }
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

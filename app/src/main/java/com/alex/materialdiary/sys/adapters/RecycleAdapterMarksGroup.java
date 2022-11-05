package com.alex.materialdiary.sys.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;

import java.util.List;

public class RecycleAdapterMarksGroup extends RecyclerView.Adapter<RecycleAdapterMarksGroup.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private final List<PeriodMarksData> periods;

    public RecycleAdapterMarksGroup(Context context, List<PeriodMarksData> periods) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapterMarksGroup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterMarksGroup.ViewHolder holder, int position) {
        PeriodMarksData period = periods.get(position);
        holder.name.setText(period.getSubjectName());
        holder.average.setText(String.valueOf(period.getAverage()));
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(llm);
        holder.recyclerView.setAdapter(new RecycleAdapterMarks(context, period.getMarks()));
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView average;
        final RecyclerView recyclerView;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.MarksLessonName);
            average = view.findViewById(R.id.MarksAverage);
            recyclerView = view.findViewById(R.id.marks_recycle);
        }
    }
}

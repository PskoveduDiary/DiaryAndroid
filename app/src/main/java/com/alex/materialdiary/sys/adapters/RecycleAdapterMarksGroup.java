package com.alex.materialdiary.sys.adapters;

import static xdroid.toaster.Toaster.toast;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.marks.Mark;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.alex.materialdiary.utils.MarksTranslator;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterMarksGroup extends RecyclerView.Adapter<RecycleAdapterMarksGroup.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private final List<PeriodMarksData> periods;
    private final Boolean needShowDifs;

    public RecycleAdapterMarksGroup(Context context, List<PeriodMarksData> periods, Boolean needShowDifs) {
        this.inflater = LayoutInflater.from(context);
        this.periods = periods;
        this.context = context;
        this.needShowDifs = needShowDifs;
    }

    @NonNull
    @Override
    public RecycleAdapterMarksGroup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mark_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeriodMarksData period = periods.get(position);
        holder.name.setText(period.getSubjectName());
        holder.average.setText(String.valueOf(period.getAverage()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.info.setTooltipText("До пятерки 5: " + period.get_to_five() + "\n" +
                    "До четверки 5: " + period.get_five_to_four() + "\n" +
                    "До четверки 4: " + period.get_four_to_four());
        }
        else holder.info.setVisibility(View.GONE);
        holder.info.setOnClickListener(View::performLongClick);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        List<Mark> differences = MarksTranslator.Companion.getSubjectMarksDifferences(context, period.getSubjectName(), new MarksTranslator(periods).getItems());
        holder.recyclerView.setLayoutManager(llm);
        holder.recyclerView.setAdapter(new RecycleAdapterMarks(context, period.getMarks(), this.needShowDifs ? differences : new ArrayList<Mark>()));
        holder.recyclerView.scrollToPosition(period.getMarks().size() - 1);
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView average;
        final RecyclerView recyclerView;
        final ImageView info;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.MarksLessonName);
            average = view.findViewById(R.id.MarksAverage);
            recyclerView = view.findViewById(R.id.marks_recycle);
            info = view.findViewById(R.id.info);
        }
    }
}

package com.alex.materialdiary.sys.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.MarksFragment;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.CommonAPI;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriodData;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;

import java.util.List;

public class RecycleAdapterPeriods extends RecyclerView.Adapter<RecycleAdapterPeriods.ViewHolder>{

    private final MarksFragment mf;
    private final LayoutInflater inflater;
    private final List<AllPeriodData> periods;

    public RecycleAdapterPeriods(MarksFragment context, List<AllPeriodData> periods) {
        this.inflater = LayoutInflater.from(context.requireContext());
        mf = context;
        this.periods = periods;
    }

    @NonNull
    @Override
    public RecycleAdapterPeriods.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.period_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterPeriods.ViewHolder holder, int position) {
        AllPeriodData period = periods.get(position);
        holder.button.setText(period.getName());
        holder.button.setOnClickListener(v -> {
            CommonAPI.getInstance().periodMarks(mf, period.getDateBegin(), period.getDateEnd());
            mf.showLoader();
        });
    }

    @Override
    public int getItemCount() {
        if (periods == null) return 0;
        return periods.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button button;
        ViewHolder(View view){
            super(view);
            button = view.findViewById(R.id.Periodbutton);
        }
    }
}
package com.alex.materialdiary.sys.adapters;

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
import com.alex.materialdiary.sys.common.models.kr.kr_info;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;

import java.util.List;

public class RecycleAdapterKrInfo extends RecyclerView.Adapter<RecycleAdapterKrInfo.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private final List<kr_info> infos;

    public RecycleAdapterKrInfo(Context context, List<kr_info> infos) {
        this.inflater = LayoutInflater.from(context);
        this.infos = infos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapterKrInfo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.kr_lesson_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        kr_info info = infos.get(position);
        holder.name.setText(info.getLessonName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.info.setTooltipText("В дз/теме обнаружены ключевые слова: \n" +
                    info.getKeyword().toString());
        }
        else holder.info.setVisibility(View.GONE);
        switch (info.getType()){
            case FULL:
                holder.text.setText("Будет сложная работа!");
            case MINI:
                holder.text.setText("Будет средний по сложности тест");
            case MAYBE:
                holder.text.setText("Возможна проверочная");
        }
        holder.info.setOnClickListener(View::performLongClick);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView text;
        final TextView info;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.LessonName);
            text = view.findViewById(R.id.text);
            info = view.findViewById(R.id.info);
        }
    }
}

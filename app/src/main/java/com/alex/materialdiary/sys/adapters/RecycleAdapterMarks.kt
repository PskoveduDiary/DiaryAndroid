package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import com.google.android.material.card.MaterialCardView
import android.os.Build
import android.view.View
import android.widget.TextView
import com.alex.materialdiary.sys.net.models.period_marks.Mark
import java.lang.Exception
import java.util.*

class RecycleAdapterMarks(
    context: Context?,
    periods: List<Mark>,
    diffs: List<com.alex.materialdiary.sys.net.models.marks.Mark>
) : RecyclerView.Adapter<RecycleAdapterMarks.ViewHolder>() {
    private val inflater: LayoutInflater
    private val periods: List<Mark>
    private val diffs: List<com.alex.materialdiary.sys.net.models.marks.Mark>

    init {
        inflater = LayoutInflater.from(context)
        this.periods = periods
        this.diffs = diffs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = periods[position]
        val itemView = holder.itemView as MaterialCardView
        itemView.strokeColor = inflater.context.resources.getColor(R.color.gray)
        if (diffs.contains(
                com.alex.materialdiary.sys.net.models.marks.Mark(
                    mark.value,
                    mark.date
                )
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                itemView.strokeColor = Color.RED
            }
        }
        val c = inflater.context
        val year: Any = Calendar.getInstance()[Calendar.YEAR]
        val yearr = Calendar.getInstance()[Calendar.YEAR].toString()
        var text = mark.date
        try {
            val pr_year = (year as Int - 1).toString()
            text = text.replace(".$pr_year", "")
        } catch (_: Exception) {
        }
        holder.date.text = text.replace(".$yearr", "")
        holder.mark.text = mark.value.toString()
        if (mark.value == 5) holder.mark.setTextColor(c.resources.getColor(R.color.five)) else if (mark.value == 4) holder.mark.setTextColor(
            c.resources.getColor(R.color.four)
        ) else if (mark.value == 3) holder.mark.setTextColor(c.resources.getColor(R.color.three)) else if (mark.value == 2) holder.mark.setTextColor(
            c.resources.getColor(R.color.two)
        ) else if (mark.value == 1) holder.mark.setTextColor(c.resources.getColor(R.color.one))
    }

    override fun getItemCount(): Int {
        return periods.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView
        val mark: TextView

        init {
            date = view.findViewById(R.id.date_of_mark)
            mark = view.findViewById(R.id.mark)
        }
    }
}
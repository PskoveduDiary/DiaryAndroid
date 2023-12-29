package com.alex.materialdiary.sys.adapters

import android.content.Context
import com.alex.materialdiary.sys.net.models.periods.Datum
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.alex.materialdiary.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.sys.adapters.RecycleAdapterMarksInPeriod
import android.widget.TextView
import com.alex.materialdiary.sys.net.models.periods.Period
import java.text.DecimalFormat
import java.util.*

class RecycleAdapterPeriodsGroup(context: Context, periods: List<Datum>) :
    RecyclerView.Adapter<RecycleAdapterPeriodsGroup.ViewHolder>() {
    private val context: Context
    private val inflater: LayoutInflater
    private val periods: List<Datum>
    var viewPool = RecyclerView.RecycledViewPool();
    init {
        inflater = LayoutInflater.from(context)
        this.periods = periods
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val period = periods[position]
        holder.name.text = period.name
        holder.info.visibility = View.GONE
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        llm.initialPrefetchItemCount = 4
        holder.recyclerView.layoutManager = llm
        holder.recyclerView.setRecycledViewPool(viewPool)
        holder.recyclerView.adapter = RecycleAdapterMarksInPeriod(context, period.periods)
    }

    override fun getItemCount(): Int {
        return periods.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val recyclerView: RecyclerView
        val info: Button

        init {
            name = view.findViewById(R.id.MarksLessonName)
            view.findViewById<View>(R.id.MarksAverage).visibility = View.GONE
            recyclerView = view.findViewById(R.id.marks_recycle)
            info = view.findViewById(R.id.info)
        }
    }
}

internal class RecycleAdapterMarksInPeriod(context: Context?, periods: List<Period>?) :
    RecyclerView.Adapter<RecycleAdapterMarksInPeriod.ViewHolder>() {
    private val inflater: LayoutInflater
    private val periods: List<Period>?

    init {
        inflater = LayoutInflater.from(context)
        this.periods = periods
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, name, _, _, _, mark1, average) = periods!![position]
        val c = inflater.context
        //val year = Calendar.getInstance()[Calendar.YEAR].toString()
        holder.date.text = name
        if (mark1 != null) {
            holder.mark.text = mark1.shortName
            //holder.itemView.setOnClickListener(v -> Toast.makeText(c, String.valueOf(mark.get), Toast.LENGTH_SHORT).show());
            if (mark1.value == 5) holder.mark.setTextColor(c.resources.getColor(R.color.five)) else if (mark1.value == 4) holder.mark.setTextColor(
                c.resources.getColor(R.color.four)
            ) else if (mark1.value == 3) holder.mark.setTextColor(c.resources.getColor(R.color.three)) else if (mark1.value == 2) holder.mark.setTextColor(
                c.resources.getColor(R.color.two)
            ) else if (mark1.value == 1) holder.mark.setTextColor(c.resources.getColor(R.color.one))
        } else {
            holder.mark.text = DecimalFormat("#0.00").format(average)
            holder.mark.setTextColor(c.resources.getColor(R.color.gray))
        }
    }

    override fun getItemCount(): Int {
        return periods!!.size
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
package com.alex.materialdiary.sys.adapters

import com.alex.materialdiary.ui.fragments.MarksFragment
import com.alex.materialdiary.sys.net.models.all_periods.AllPeriodData
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.alex.materialdiary.R

class RecycleAdapterPeriods(context: MarksFragment, periods: List<AllPeriodData>?) :
    RecyclerView.Adapter<RecycleAdapterPeriods.ViewHolder>() {
    private val mf: MarksFragment
    private val inflater: LayoutInflater
    private val periods: List<AllPeriodData>?

    init {
        inflater = LayoutInflater.from(context.requireContext())
        mf = context
        this.periods = periods
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.period_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, name, dateBegin, dateEnd) = periods!![position]
        holder.button.text = name
        //holder.button.setTag(holder.button.hashCode(), period.getPeriodGuid());
        holder.button.setOnClickListener {
            mf.getMarks(dateBegin, dateEnd)
            mf.showLoader()
        }
    }

    override fun getItemCount(): Int {
        return periods?.size ?: 0
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button

        init {
            button = view.findViewById(R.id.Periodbutton)
        }
    }
}
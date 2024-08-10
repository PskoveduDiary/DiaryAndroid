package com.alex.materialdiary.sys.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.view.View
import android.widget.TextView
import com.alex.materialdiary.ui.fragments.HomeFragment
import com.alex.materialdiary.sys.net.models.marks.LastMark

class RecycleAdapterLastMarks(context: HomeFragment, items: MutableList<LastMark>) :
    RecyclerView.Adapter<RecycleAdapterLastMarks.ViewHolder>() {
    private val context: HomeFragment
    private val inflater: LayoutInflater
    val items: MutableList<LastMark>

    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.context = context
        this.items = items

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.last_marks_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.subj_name.text = item.subjName
        holder.mark.text = item.mark.toString()
        val color = when (item.mark){
            1 -> R.color.one
            2 -> R.color.two
            3 -> R.color.three
            4 -> R.color.four
            5 -> R.color.five
            else -> R.color.one
        }
        holder.mark.setTextColor(context.resources.getColor(color))
        holder.date.text = item.date
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val subj_name: TextView
        val mark: TextView
        val date: TextView

        init {
            subj_name = view.findViewById(R.id.subjName)
            mark = view.findViewById(R.id.mark)
            date = view.findViewById(R.id.date)
        }
    }
}
package com.alex.materialdiary.sys.adapters

import android.content.Context
import com.alex.materialdiary.utils.MarksTranslator.Companion.getSubjectMarksDifferences
import com.alex.materialdiary.sys.net.models.period_marks.PeriodMarksData
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.utils.MarksTranslator
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.alex.materialdiary.MarksFragment
import com.alex.materialdiary.NavGraphDirections
import java.text.DecimalFormat
import java.util.ArrayList

class RecycleAdapterMarksGroup(
    fragment: MarksFragment,
    periods: List<PeriodMarksData>,
    needShowDifs: Boolean
) : RecyclerView.Adapter<RecycleAdapterMarksGroup.ViewHolder>() {
    private val context: Context
    private val fragment: MarksFragment
    private val inflater: LayoutInflater
    private val periods: List<PeriodMarksData>
    private val needShowDifs: Boolean

    var viewPool = RecyclerView.RecycledViewPool();
    init {
        this.context = fragment.requireContext()
        this.fragment = fragment
        inflater = LayoutInflater.from(context)
        this.periods = periods.filter { it.hideInReports != true }
        this.needShowDifs = needShowDifs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val period = periods[position]
        holder.name.text = period.subjectName
        val avg = period.average
        if (avg == -100.0) holder.average.text = "Нет оценок"
        else holder.average.text = "Средний балл: " + DecimalFormat("#0.00").format(period.average)
        holder.info.setOnClickListener {
            fragment.openBottomSheet(period)
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //holder.info.tooltipText = """
            //    До пятерки 5: ${period.get_to_five()}
            //    До четверки 5: ${period.get_five_to_four()}
            //   До четверки 4: ${period.get_four_to_four()}
             //   """.trimIndent()
        } else holder.info.visibility = View.GONE
        holder.info.setOnClickListener { obj: View -> obj.performLongClick() }*/
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        llm.initialPrefetchItemCount = 4
        val differences =
            getSubjectMarksDifferences(context, period.subjectName, MarksTranslator(periods as MutableList).items)
        holder.recyclerView.layoutManager = llm
        holder.recyclerView.setRecycledViewPool(viewPool)
        holder.recyclerView.adapter = RecycleAdapterMarks(
            context,
            period.marks,
            (if (needShowDifs) differences else ArrayList())
        )
        holder.recyclerView.scrollToPosition(period.marks.size - 1)
    }

    override fun getItemCount(): Int {
        return periods.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val average: TextView
        val recyclerView: RecyclerView
        val info: Button

        init {
            name = view.findViewById(R.id.MarksLessonName)
            average = view.findViewById(R.id.MarksAverage)
            recyclerView = view.findViewById(R.id.marks_recycle)
            info = view.findViewById(R.id.info)
        }
    }
}
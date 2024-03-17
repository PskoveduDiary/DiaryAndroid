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
import com.alex.materialdiary.sys.ChooseColorSchemeBottomSheet
import com.alex.materialdiary.sys.ColorSchemes
import com.alex.materialdiary.sys.DiaryPreferences
import com.alex.materialdiary.sys.net.models.period_marks.Mark
import java.text.DecimalFormat
import java.util.ArrayList

class RecycleAdapterMarksScheme(
    context: Context,
    val parent: ChooseColorSchemeBottomSheet
) : RecyclerView.Adapter<RecycleAdapterMarksScheme.ViewHolder>() {
    private val context: Context
    private val inflater: LayoutInflater

    var viewPool = RecyclerView.RecycledViewPool();
    init {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_scheme_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = when(position+1) {
            1 -> "Классическая"
            2 -> "Градиент"
            3 -> "Стилизованная"
            else -> "Классическая"
        }
        val ExampleList = listOf(
            Mark(date="", value=1),
            Mark(date="", value=2),
            Mark(date="", value=3),
            Mark(date="", value=4),
            Mark(date="", value=5),
        )
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        llm.initialPrefetchItemCount = 5
        holder.recyclerView.layoutManager = llm
        holder.recyclerView.setRecycledViewPool(viewPool)
        holder.recyclerView.adapter = RecycleAdapterMarks(
            context,
            ExampleList,
            ArrayList(),
            position + 1
        )
        holder.itemView.setOnClickListener {
            DiaryPreferences.getInstance().setInt("color_scheme", position + 1)
            ColorSchemes.getInstance(context).currentScheme = position + 1
            parent.close()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val recyclerView: RecyclerView

        init {
            name = view.findViewById(R.id.MarksSchemeName)
            recyclerView = view.findViewById(R.id.marks_recycle)
        }
    }
}
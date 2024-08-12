package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDayData
import com.alex.materialdiary.sys.net.models.diary_day.DiaryMark
import com.alex.materialdiary.ui.fragments.DiaryFragment
import com.google.android.material.card.MaterialCardView

class RecycleAdapterLessons(context: DiaryFragment, items: MutableList<DiaryDayData>) :
    RecyclerView.Adapter<RecycleAdapterLessons.ViewHolder>() {
    private val context: DiaryFragment
    private val inflater: LayoutInflater
    val items: MutableList<DiaryDayData>
    fun cleanName(name: String): String {
//        return name
        return if (name.contains("Основы безопасности")) "ОБЖ"
        else if (name.contains("Изобразительное искусство")) "ИЗО"
        else if (name.contains("Физическая культура")) "Физкультура"
        else name
    }
    fun update() {
        lastPosition = -1
        this.notifyDataSetChanged()
    }

    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.context = context
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.lesson_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = items[position]
        holder.name.text = lesson.subjectName?.let { cleanName(it) }
        holder.info.text =
            "№${lesson.lessonNumber.toString()} (${lesson.lessonTimeBegin} - ${lesson.lessonTimeEnd})"
        var absence = ""
        lesson.absence?.forEach {
            if (absence != "") absence += ", "
            if (lesson.absence?.size!! > 1) {
                absence += it.shortName
            } else absence += it.fullName
        }
        holder.absence.text = absence
        holder.homework.text =
            if (lesson.homeworkPrevious != null) lesson.homeworkPrevious!!.homework else ""
        holder.itemView.setOnClickListener {
            context.openBottomSheet(lesson)
        }
        holder.marks.layoutManager = LinearLayoutManager(inflater.context, LinearLayoutManager.HORIZONTAL, false)
        holder.marks.adapter = RecycleAdapterLessonMarks(inflater.context, lesson.marks, "")
        setAnimation(holder.itemView, position);
    }

    override fun getItemCount(): Int {
        return items.size
    }
    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(inflater.context, android.R.anim.fade_in)
            animation.startOffset = (position * 75).toLong()
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
    fun deleteAnimation(viewToAnimate: View?, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (viewToAnimate == null) return
        val animation: Animation =
            AnimationUtils.loadAnimation(inflater.context, android.R.anim.fade_out)
        animation.startOffset = (position * 75).toLong()
        viewToAnimate.startAnimation(animation)
    }
    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        //        var Num: TextView
        var info: TextView
        var name: TextView
        var marks: RecyclerView
        var homework: TextView
        var absence: TextView
        var card: MaterialCardView

        init {
//            Num = view.findViewById(R.id.lesson_Num)
            info = view.findViewById(R.id.lesson_info)
            name = view.findViewById(R.id.lesson_name)
            marks = view.findViewById(R.id.lesson_marks)
//        Teacher = v.findViewById(R.id.lesson_Teacher)
            homework = view.findViewById(R.id.lesson_homework)
            absence = view.findViewById(R.id.lesson_absence)
            card = view.findViewById(R.id.lesson_item)
        }
    }
    class RecycleAdapterLessonMarks(
        context: Context?,
        marks: List<DiaryMark>,
        private val type: String?
    ) : RecyclerView.Adapter<RecycleAdapterLessonMarks.ViewHolder>() {
        private val inflater: LayoutInflater
        private val marks: List<DiaryMark>
        val newColor = Color.parseColor("#FF6750A4")
        init {
            inflater = LayoutInflater.from(context)
            this.marks = marks
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.lesson_mark_item, parent, false)
            return ViewHolder(view)
        }
        override fun  onBindViewHolder(holder: ViewHolder, position: Int) {
            val mark = marks[position]
            holder.card.cardElevation = 4F
            holder.card.strokeColor = Color.TRANSPARENT
            val c = inflater.context
            holder.mark.text = mark.value.toString()
//            if (mark.value in 1..5){
//                holder.mark.setTextColor(Color.WHITE)
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.view.tooltipText = type
                if (type == null) holder.view.tooltipText = "Тип не указан"
            }
            holder.view.setOnClickListener { obj: View -> obj.performLongClick() }
//            val color_scheme = ColorSchemes.getInstance(c).getCurrentScheme(scheme)
//            if (mark.value == 5) holder.card.backgroundTintList = ColorStateList.valueOf(color_scheme[4])
//            else if (mark.value == 4) holder.card.backgroundTintList = ColorStateList.valueOf(color_scheme[3])
//            else if (mark.value == 3) holder.card.backgroundTintList = ColorStateList.valueOf(color_scheme[2])
//            else if (mark.value == 2) holder.card.backgroundTintList = ColorStateList.valueOf(color_scheme[1])
//            else if (mark.value == 1) holder.card.backgroundTintList = ColorStateList.valueOf(color_scheme[0])
        }

        override fun getItemCount(): Int {
            return marks.size
        }
        class ViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(view) {
            val mark: TextView
            val card: MaterialCardView
            init {
                mark = view.findViewById(R.id.mark)
                card = view.findViewById(R.id.anchor_for_badge)
            }
        }
    }
}
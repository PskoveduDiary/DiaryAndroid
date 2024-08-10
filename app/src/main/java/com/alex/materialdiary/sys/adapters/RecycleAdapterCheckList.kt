package com.alex.materialdiary.sys.adapters

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.os.Build
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.alex.materialdiary.ui.fragments.ChecklistFragment
import com.alex.materialdiary.sys.net.models.check_list.CheckListShow
import com.alex.materialdiary.sys.net.models.check_list.Lesson

class RecycleAdapterCheckList(context: ChecklistFragment, infos: MutableList<CheckListShow>) :
    RecyclerView.Adapter<RecycleAdapterCheckList.ViewHolder>() {
    private val context: ChecklistFragment
    private val inflater: LayoutInflater
    private val infos: MutableList<CheckListShow>

    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.infos = infos
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.checklist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = infos[position].name
        holder.check.isChecked = infos[position].done
        holder.check.text = infos[position].homework
        holder.itemView.setOnClickListener {
            holder.check.performClick()
        }
        when(infos[position].done){
            true -> {
                holder.check.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
                holder.check.setTextColor(context.resources.getColor(R.color.gray))
                infos[position].done = true
                context.check(infos.map { it.toLesson() } as MutableList<Lesson>)
            }
            false -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    holder.check.paintFlags =  Paint.CURSOR_BEFORE
                }
                infos[position].done = false
                context.check(infos.map { it.toLesson() } as MutableList<Lesson>)
                holder.check.setTextColor(context.resources.getColor(R.color.icons))
            }
        }
        holder.check.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> {
                    holder.check.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
                    holder.check.setTextColor(context.resources.getColor(R.color.gray))
                    infos[position].done = true
                    context.check(infos.map { it.toLesson() } as MutableList<Lesson>)
                }
                false -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        holder.check.paintFlags =  Paint.CURSOR_BEFORE
                    }
                    infos[position].done = false
                    context.check(infos.map { it.toLesson() } as MutableList<Lesson>)
                    holder.check.setTextColor(context.resources.getColor(R.color.icons))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return infos.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val check: CheckBox

        init {
            name = view.findViewById(R.id.LessonName)
            check = view.findViewById(R.id.checkBox)
        }
    }
}
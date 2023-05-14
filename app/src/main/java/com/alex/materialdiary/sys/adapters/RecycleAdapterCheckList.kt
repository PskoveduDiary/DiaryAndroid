package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.graphics.Paint
import com.alex.materialdiary.sys.net.models.kr.kr_info
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.os.Build
import android.view.View
import android.widget.CheckBox
import com.alex.materialdiary.sys.net.models.kr.kr_info.TYPES
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import com.alex.materialdiary.sys.net.models.check_list.CheckListShow

class RecycleAdapterCheckList(context: Context, infos: List<CheckListShow>) :
    RecyclerView.Adapter<RecycleAdapterCheckList.ViewHolder>() {
    private val context: Context
    private val inflater: LayoutInflater
    private val infos: List<CheckListShow>

    init {
        inflater = LayoutInflater.from(context)
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

        holder.check.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    holder.check.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
                    //holder.check.setTextColor(resources.getColor(R.color.gray))
                }
                false -> {
                    holder.check.paintFlags =  Paint.CURSOR_BEFORE
                    //holder.check.setTextColor(getRes.getColor(R.color.icons))
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
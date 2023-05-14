package com.alex.materialdiary.sys.adapters

import android.content.Context
import com.alex.materialdiary.sys.net.models.kr.kr_info
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.os.Build
import android.view.View
import com.alex.materialdiary.sys.net.models.kr.kr_info.TYPES
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView

class RecycleAdapterKrInfo(context: Context, infos: List<kr_info>) :
    RecyclerView.Adapter<RecycleAdapterKrInfo.ViewHolder>() {
    private val context: Context
    private val inflater: LayoutInflater
    private val infos: List<kr_info>

    init {
        inflater = LayoutInflater.from(context)
        this.infos = infos
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.kr_lesson_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (lessonName, keyword, type) = infos[position]
        holder.name.text = lessonName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.info.tooltipText = """
                В дз/теме обнаружены ключевые слова: 
                $keyword
                """.trimIndent()
        } else holder.info.visibility = View.GONE
        when (type) {
            TYPES.FULL -> {
                holder.text.text = "Будет сложная работа!"
                holder.text.text = "Будет средний по сложности тест"
                holder.text.text = "Возможна проверочная"
            }
            TYPES.MINI -> {
                holder.text.text = "Будет средний по сложности тест"
                holder.text.text = "Возможна проверочная"
            }
            TYPES.MAYBE -> holder.text.text = "Возможна проверочная"
            else -> {}
        }
        holder.info.setOnClickListener { obj: View -> obj.performLongClick() }
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
    }

    override fun getItemCount(): Int {
        return infos.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val text: TextView
        val info: TextView

        init {
            name = view.findViewById(R.id.LessonName)
            text = view.findViewById(R.id.text)
            info = view.findViewById(R.id.info)
        }
    }
}
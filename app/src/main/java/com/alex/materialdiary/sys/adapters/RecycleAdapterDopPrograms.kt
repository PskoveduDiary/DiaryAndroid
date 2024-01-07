package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.graphics.Paint
import com.alex.materialdiary.sys.net.models.kr.kr_info
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.os.Build
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import com.alex.materialdiary.sys.net.models.kr.kr_info.TYPES
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.alex.materialdiary.ChecklistFragment
import com.alex.materialdiary.HomeFragment
import com.alex.materialdiary.sys.net.models.assistant_tips.AssistantTipsRequestBody
import com.alex.materialdiary.sys.net.models.assistant_tips.TipData
import com.alex.materialdiary.sys.net.models.check_list.CheckListShow
import com.alex.materialdiary.sys.net.models.check_list.Lesson
import com.alex.materialdiary.sys.net.models.dop_programs.DopProgramData
import com.alex.materialdiary.sys.net.models.marks.LastMark
import com.alex.materialdiary.sys.net.models.news.News
import com.alex.materialdiary.sys.net.models.news.NewsItem
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class RecycleAdapterDopPrograms(context: HomeFragment, items: List<DopProgramData>) :
    RecyclerView.Adapter<RecycleAdapterDopPrograms.ViewHolder>() {
    private val context: HomeFragment
    private val inflater: LayoutInflater
    val items: List<DopProgramData>
    val imgLoader = ImageLoader.getInstance()
    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.context = context
        this.items = items

        imgLoader.init(ImageLoaderConfiguration.createDefault(context.requireContext()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.dop_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.title.text = item.name
        imgLoader.displayImage("https://dop.pskovedu.ru/file/big_thumb/" + item.fileguid, holder.image);
        holder.text.text = item.description.parseAsHtml()


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val text: TextView
        val image: ImageView

        init {
            title = view.findViewById(R.id.dopTitle)
            text = view.findViewById(R.id.dopText)
            image = view.findViewById(R.id.dopImage)
        }
    }
}
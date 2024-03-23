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
import com.alex.materialdiary.ChecklistFragment
import com.alex.materialdiary.HomeFragment
import com.alex.materialdiary.sys.net.models.assistant_tips.AssistantTipsRequestBody
import com.alex.materialdiary.sys.net.models.assistant_tips.TipData
import com.alex.materialdiary.sys.net.models.check_list.CheckListShow
import com.alex.materialdiary.sys.net.models.check_list.Lesson
import com.alex.materialdiary.sys.net.models.marks.LastMark
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class RecycleAdapterAdvices(context: HomeFragment, items: MutableList<TipData>) :
    RecyclerView.Adapter<RecycleAdapterAdvices.ViewHolder>() {
    private val context: HomeFragment
    private val inflater: LayoutInflater
    val items: MutableList<TipData>
    val imgLoader = ImageLoader.getInstance()

    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.context = context
        this.items = items
        imgLoader.init(ImageLoaderConfiguration.createDefault(context.requireContext()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.advice_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.title.text = item.title
        try {
            imgLoader.displayImage("https://one.pskovedu.ru" + item.avatar_url, holder.image);
        } catch (_: java.io.FileNotFoundException) {
        }
        holder.text.text = item.text


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val text: TextView
        val image: ImageView

        init {
            title = view.findViewById(R.id.adviceTitle)
            text = view.findViewById(R.id.adviceText)
            image = view.findViewById(R.id.adviceImage)
        }
    }
}
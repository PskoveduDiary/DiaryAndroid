package com.alex.materialdiary.sys.adapters

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.alex.materialdiary.ui.fragments.HomeFragment
import com.alex.materialdiary.ui.bottom_sheets.DopBottomSheet
import com.alex.materialdiary.sys.net.models.dop_programs.DopProgramData
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

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
        val config = ImageLoaderConfiguration.Builder(context.requireContext())
            .memoryCacheSize(100000000)
            .build()
        imgLoader.init(config)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.dop_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.title.text = item.name
        val listener = object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String?, view: View?) {
                view?.visibility = View.GONE
            }

            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                view?.visibility = View.GONE
            }

            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                view?.visibility = View.VISIBLE
            }

            override fun onLoadingCancelled(imageUri: String?, view: View?) {
            }

        }
        try {
            imgLoader.displayImage(
                "https://dop.pskovedu.ru/file/big_thumb/" + item.fileguid,
                holder.image,
                listener
            )

        } catch (e: java.io.FileNotFoundException) {
            e.printStackTrace()
        }
        holder.text.text = item.description.parseAsHtml()
        holder.itemView.setOnClickListener {
            DopBottomSheet(item).show(
                context.requireActivity().supportFragmentManager,
                "DopBottomSheet"
            )
        }


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
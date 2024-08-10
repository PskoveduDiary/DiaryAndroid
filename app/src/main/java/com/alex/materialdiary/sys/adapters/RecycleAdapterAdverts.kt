package com.alex.materialdiary.sys.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alex.materialdiary.ui.fragments.HomeFragment

class RecycleAdapterAdverts(context: HomeFragment, items: MutableList<Pair<String, (() -> Unit)?>>) :
    RecyclerView.Adapter<RecycleAdapterAdverts.ViewHolder>() {
    private val context: HomeFragment
    private val inflater: LayoutInflater
    val items: MutableList<Pair<String, (() -> Unit)?>>
    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.context = context
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.advert_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (label, call) = items.get(position)
        holder.label.text = label
        if (call != null){
            holder.arrow.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                call()
            }
        }
        else {
            holder.arrow.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView
        val arrow: ImageView

        init {
            label = view.findViewById(R.id.advertLabel)
            arrow = view.findViewById(R.id.arrow)
        }
    }
}
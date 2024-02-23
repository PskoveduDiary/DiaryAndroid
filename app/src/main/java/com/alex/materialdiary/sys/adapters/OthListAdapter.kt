package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alex.materialdiary.R
import com.alex.materialdiary.containers.OtherItem

class OthListAdapter(private val context: Context,
                     private val dataSource: ArrayList<OtherItem>,
                     private val find: Fragment) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.oth_item, parent, false)
        // Get title element
        val title = rowView.findViewById(R.id.othLabel) as TextView
        // Get thumbnail element
        val thumbnail = rowView.findViewById(R.id.othIcon) as ImageView

        val item = getItem(position) as OtherItem

        title.text = item.Label
        thumbnail.setImageResource(item.Image)
        /*thumbnail.setColorFilter(context.getResources().getColor(R.color.icons))*/

        rowView.setOnClickListener{
                find.findNavController().navigate(item.Id)
        }

        return rowView
    }


}
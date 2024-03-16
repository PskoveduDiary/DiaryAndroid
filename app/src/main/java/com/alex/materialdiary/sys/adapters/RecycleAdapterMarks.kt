package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.materialdiary.R
import com.google.android.material.card.MaterialCardView
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.alex.materialdiary.sys.net.models.period_marks.Mark
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import java.lang.Exception
import java.util.*
import kotlin.coroutines.coroutineContext

class RecycleAdapterMarks(
    context: Context?,
    periods: List<Mark>,
    diffs: List<com.alex.materialdiary.sys.net.models.marks.Mark>
) : RecyclerView.Adapter<RecycleAdapterMarks.ViewHolder>() {
    private val inflater: LayoutInflater
    private val periods: List<Mark>
    private val diffs: List<com.alex.materialdiary.sys.net.models.marks.Mark>
    val useBadge: Boolean = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("useBadge", false)
    val newColor = Color.parseColor("#FF6750A4")
    init {
        inflater = LayoutInflater.from(context)
        this.periods = periods
        this.diffs = diffs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.mark_item, parent, false)
        return ViewHolder(view)
    }
    override fun  onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = periods[position]
        holder.card.cardElevation = 4F
        holder.card.strokeColor = Color.TRANSPARENT
        holder.detachBadge()
        if (diffs.contains(
                com.alex.materialdiary.sys.net.models.marks.Mark(
                    mark.value,
                    mark.date
                )
            )
        ) {
            //itemView.setBackgroundColor(inflater.context.resources.getColor(R.color.new_mark))
            //itemView.setCardBackgroundColor(inflater.context.resources.getColor(R.color.new_mark))
            //holder.card.strokeColor = inflater.context.resources.getColor(R.color.two)
            //holder.badgeDrawable.isVisible = true
            //holder.card.cardElevation = 0F
            if (useBadge) holder.attachBadge()
            else {
                holder.card.strokeColor = newColor
                holder.card.cardElevation = 0F
            }
        }
        val c = inflater.context
        val year: Any = Calendar.getInstance()[Calendar.YEAR]
        val yearr = Calendar.getInstance()[Calendar.YEAR].toString()
        var text = mark.date
        try {
            val pr_year = (year as Int - 1).toString()
            text = text.replace(".$pr_year", "")
        } catch (_: Exception) {
        }
        holder.date.text = text.replace(".$yearr", "")
        holder.mark.text = mark.value.toString()
        if (mark.value in 1..5){
            holder.mark.setTextColor(Color.WHITE)
            holder.date.setTextColor(Color.WHITE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.view.tooltipText = mark.typeName
            if (mark.typeName == null) holder.view.tooltipText = "Тип не указан"
        }
        holder.view.setOnClickListener { obj: View -> obj.performLongClick() }
        if (mark.value == 5) holder.card.backgroundTintList = ColorStateList.valueOf(c.resources.getColor(R.color.five_tint))
        else if (mark.value == 4) holder.card.backgroundTintList = ColorStateList.valueOf(c.resources.getColor(R.color.four_tint))
        else if (mark.value == 3) holder.card.backgroundTintList = ColorStateList.valueOf(c.resources.getColor(R.color.three_tint))
        else if (mark.value == 2) holder.card.backgroundTintList = ColorStateList.valueOf(c.resources.getColor(R.color.two_tint))
        else if (mark.value == 1) holder.card.backgroundTintList = ColorStateList.valueOf(c.resources.getColor(R.color.one_tint))
    }

    override fun getItemCount(): Int {
        return periods.size
    }
    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    class ViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView
        val mark: TextView
        val card: MaterialCardView
        val badgeDrawable: BadgeDrawable
        fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics)
        init {
            date = view.findViewById(R.id.date_of_mark)
            mark = view.findViewById(R.id.mark)
            card = view.findViewById(R.id.anchor_for_badge)
            badgeDrawable = BadgeDrawable.create(view.context)
            //badgeDrawable.badgeGravity = BadgeDrawable.T
            badgeDrawable.text = " "
            //badgeDrawable.isVisible = false
            //badgeDrawable.clearText()
            //badgeDrawable.horizontalOffset = view.context.toPx(45).toInt()
            card.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                badgeDrawable.updateBadgeCoordinates(card, view as FrameLayout)
            }
        }
        fun attachBadge(){
            BadgeUtils.attachBadgeDrawable(badgeDrawable, card, view as FrameLayout)
        }
        fun detachBadge(){
            BadgeUtils.detachBadgeDrawable(badgeDrawable, card)
        }
    }
}
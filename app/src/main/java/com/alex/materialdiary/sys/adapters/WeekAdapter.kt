package com.alex.materialdiary.sys.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.DiaryFragment
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.WeekItemBinding
import com.google.android.material.card.MaterialCardView
import org.joda.time.LocalDate
import org.joda.time.Months
import org.joda.time.Weeks
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun LocalDate?.toText(): String {
    if (this == null) return LocalDate.now().toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
    return this.toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
}

class WeekAdapter(val diary: DiaryFragment) : RecyclerView.Adapter<PagerVH>() {
    val selectedColor = ColorStateList.valueOf(diary.resources.getColor(R.color.gray))
    val defaultColor = ColorStateList.valueOf(Color.TRANSPARENT)
    var dateOfStart: LocalDate = LocalDate.now().withDayOfWeek(1)
    var selectedCard: MaterialCardView? = null
    var selectedDate = LocalDate.now()
    init {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = 100

    //app:cardForegroundColor="#38542980"
    public fun clearSelection() {
        selectedCard?.setStrokeColor(defaultColor)
        selectedCard?.cardElevation = 30F
    }

    public override fun getItemViewType(position: Int): Int {
        val weeks =
            Weeks.weeksBetween(selectedDate.withDayOfWeek(1), dateOfStart.plusWeeks(50-position)).weeks
        val type = when(weeks){
            0 -> 1
            else -> 0
        }
        return type
    }
    public fun selectDayOfWeek(cholder: PagerVH?, dayOfWeek: Int){
        selectedCard = cholder?.let { getCards(it).getOrNull(dayOfWeek-1) }
        if (selectedCard == null) clearSelection()
        selectedCard?.setStrokeColor(selectedColor)
        selectedCard?.cardElevation = 300F
    }
    fun getCards(holder: PagerVH): List<MaterialCardView> {
        val list = mutableListOf<MaterialCardView>()
        list += holder.binding.MondayDay
        list += holder.binding.TuesdayDay
        list += holder.binding.WednesdayDay
        list += holder.binding.ThursdayDay
        list += holder.binding.FridayDay
        list += holder.binding.SundayDay
        return list.toList()
    }
    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        getCards(holder).forEach {
            it.cardElevation = 30F
            it.setStrokeColor(defaultColor)
        }
        val weeks =
            Weeks.weeksBetween(selectedDate.withDayOfWeek(1), LocalDate.now().withDayOfWeek(1)).weeks
        if(weeks == 50-position) {
            selectDayOfWeek(holder, selectedDate.dayOfWeek)
        }
        val monday = dateOfStart.plusWeeks(position - 50)
        val tuesday = dateOfStart.plusWeeks(position - 50).plusDays(1)
        val wednesday = dateOfStart.plusWeeks(position - 50).plusDays(2)
        val thursday = dateOfStart.plusWeeks(position - 50).plusDays(3)
        val friday = dateOfStart.plusWeeks(position - 50).plusDays(4)
        val sunday = dateOfStart.plusWeeks(position - 50).plusDays(5)
        holder.binding.MondayText.text = monday.dayOfMonth.toString()
        holder.binding.MondayDay.setOnClickListener {
            diary.getDay(monday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
        holder.binding.TuesdayText.text = tuesday.dayOfMonth.toString()
        holder.binding.TuesdayDay.setOnClickListener {
            diary.getDay(tuesday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
        holder.binding.WednesdayText.text = wednesday.dayOfMonth.toString()
        holder.binding.WednesdayDay.setOnClickListener {
            diary.getDay(wednesday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
        holder.binding.ThursdayText.text = thursday.dayOfMonth.toString()
        holder.binding.ThursdayDay.setOnClickListener {
            diary.getDay(thursday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
        holder.binding.FridayText.text = friday.dayOfMonth.toString()
        holder.binding.FridayDay.setOnClickListener {
            diary.getDay(friday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
        holder.binding.SundayText.text = sunday.dayOfMonth.toString()
        holder.binding.SundayDay.setOnClickListener {
            diary.getDay(sunday)
            clearSelection()
            (it as MaterialCardView).setStrokeColor(selectedColor)
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it as MaterialCardView?
        }
    }
}

class PagerVH(val binding: WeekItemBinding) : RecyclerView.ViewHolder(binding.root) {
}
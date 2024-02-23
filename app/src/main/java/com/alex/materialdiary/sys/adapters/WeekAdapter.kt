package com.alex.materialdiary.sys.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.DiaryFragment
import com.alex.materialdiary.databinding.WeekItemBinding
import com.google.android.material.card.MaterialCardView
import org.joda.time.LocalDate
import org.joda.time.Months
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun LocalDate?.toText(): String {
    if(this == null) return LocalDate.now().toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
    return this.toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
}
class WeekAdapter(val diary: DiaryFragment) : RecyclerView.Adapter<PagerVH>() {

    var dateOfStart: LocalDate = LocalDate.now().withDayOfWeek(1)
    var selectedCard: MaterialCardView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = 600


    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        selectedCard?.cardElevation = 100F
        val monday = dateOfStart.plusWeeks(position-300)
        val tuesday = dateOfStart.plusWeeks(position-300).plusDays(1)
        val wednesday = dateOfStart.plusWeeks(position-300).plusDays(2)
        val thursday = dateOfStart.plusWeeks(position-300).plusDays(3)
        val friday = dateOfStart.plusWeeks(position-300).plusDays(4)
        val sunday = dateOfStart.plusWeeks(position-300).plusDays(5)
        holder.binding.MondayText.text = monday.dayOfMonth.toString()
        holder.binding.MondayDay.setOnClickListener {
            diary.getDay(monday.toText(), monday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
        holder.binding.TuesdayText.text = tuesday.dayOfMonth.toString()
        holder.binding.TuesdayDay.setOnClickListener {
            diary.getDay(tuesday.toText(), tuesday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
        holder.binding.WednesdayText.text = wednesday.dayOfMonth.toString()
        holder.binding.WednesdayDay.setOnClickListener {
            diary.getDay(wednesday.toText(), wednesday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
        holder.binding.ThursdayText.text = thursday.dayOfMonth.toString()
        holder.binding.ThursdayDay.setOnClickListener {
            diary.getDay(thursday.toText(), thursday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
        holder.binding.FridayText.text = friday.dayOfMonth.toString()
        holder.binding.FridayDay.setOnClickListener {
            diary.getDay(friday.toText(), friday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
        holder.binding.SundayText.text = sunday.dayOfMonth.toString()
        holder.binding.SundayDay.setOnClickListener {
            diary.getDay(sunday.toText(), sunday.toDate())
            selectedCard?.cardElevation = 30F
            (it as MaterialCardView).cardElevation = 300F
            selectedCard = it
        }
    }
}

class PagerVH(val binding: WeekItemBinding) : RecyclerView.ViewHolder(binding.root) {
}
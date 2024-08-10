package com.alex.materialdiary.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alex.materialdiary.databinding.FragmentDiaryBinding
import com.alex.materialdiary.sys.LessonBottomSheet
import com.alex.materialdiary.sys.adapters.PagerVH
import com.alex.materialdiary.sys.adapters.ProgramAdapterDiary
import com.alex.materialdiary.sys.adapters.WeekAdapter
import com.alex.materialdiary.sys.adapters.toText
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDayData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.joda.time.Weeks
import org.joda.time.format.DateTimeFormat


fun LocalDate?.toText(): String {
    if (this == null) return LocalDate.now().toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
    return this.toString(DateTimeFormat.forPattern("dd.MM.yyyy"))
}

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DiaryFragment : Fragment(){
    private var _binding: FragmentDiaryBinding? = null
    var current_date: LocalDate? = null;
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        PskoveduApi.getInstance(requireContext(), findNavController())
        if (current_date == null) current_date = LocalDate.now()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                current_date = LocalDate(year, monthOfYear+1, dayOfMonth)
                getDay(current_date)
                binding.lessons.adapter = null
                (binding.datePickerPager.adapter as WeekAdapter).clearSelection()
                val weeks =
                Weeks.weeksBetween(current_date!!.withDayOfWeek(1), LocalDate.now().withDayOfWeek(1)).weeks
                binding.datePickerPager.setCurrentItem(50-weeks, weeks in -5..5)
                Handler().postDelayed({
                    val cur = binding.datePickerPager.currentItem
                    val rv = binding.datePickerPager.get(0) as RecyclerView
                    val vh = rv.findViewHolderForAdapterPosition(cur)
                    (binding.datePickerPager.adapter as WeekAdapter).selectDayOfWeek(vh as PagerVH?, current_date!!.dayOfWeek)
                }, 1)
            }
        binding.datePickerPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                    //(binding.datePickerPager.adapter as WeekAdapter).clearSelection()
            }
        })
        binding.date.setOnClickListener {
            if (current_date == null) current_date = LocalDate.now()
            DatePickerDialog(
                this@DiaryFragment.requireContext(),
                dateSetListener,
                current_date!!.year,
                current_date!!.monthOfYear-1,
                current_date!!.dayOfMonth
            ).show()
        }
        binding.today.setOnClickListener {
            current_date = LocalDate.now()
            getDay(current_date)
            binding.lessons.adapter = null
            (binding.datePickerPager.adapter as WeekAdapter).clearSelection()
            binding.datePickerPager.currentItem = 50
            Handler().postDelayed({
                val rv = binding.datePickerPager.get(0) as RecyclerView
                val vh = rv.findViewHolderForAdapterPosition(50)
                (binding.datePickerPager.adapter as WeekAdapter).selectDayOfWeek(vh as PagerVH?, current_date!!.dayOfWeek)
            }, 20)
        }
        /*binding.checklist.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
            /*findNavController().navigate(NavGraphDirections.toCheckList().setDate(dateFormat.format(cuurent_date)))*/
        }*/
        return binding.root

    }
    fun get_nav(): NavController {
        return findNavController()
    }
    fun openBottomSheet(data: DiaryDayData){
        val modalBottomSheet = LessonBottomSheet.newInstance(data)
        modalBottomSheet.show(requireActivity().supportFragmentManager, LessonBottomSheet.TAG)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"*/
        binding.datePickerPager.adapter = WeekAdapter(this)
        binding.datePickerPager.setCurrentItem(50, false)
        getDay(current_date)
        binding.swiperefresh.setOnRefreshListener {
            getDay(current_date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDay(date: LocalDate?) {
        binding.today.paint?.isUnderlineText =
            date != LocalDate.now()
        binding.date.text = date.toText()
        (binding.datePickerPager.adapter as WeekAdapter).selectedDate = date
        binding.today.text = "Сегодня"
        binding.swiperefresh.isRefreshing = true
        val timestart = System.currentTimeMillis()
        if (current_date != date) current_date = date
        CoroutineScope(Dispatchers.IO).launch {
            val lessons =
                PskoveduApi.getInstance(requireContext(), findNavController()).getDay(date.toText())
            withContext(Dispatchers.Main) {
                if (_binding == null) return@withContext
                val timeend = System.currentTimeMillis()
                if (timeend - timestart < 800){
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (_binding == null) return@postDelayed
                        binding.swiperefresh.isRefreshing = false
                    }, 800-(timeend-timestart))
                }
                else binding.swiperefresh.isRefreshing = false
                if (lessons?.data != null) {
                    if (lessons.data.size > 0) {
                        binding.lessons.adapter =
                            ProgramAdapterDiary(
                                this@DiaryFragment,
                                lessons.data
                            )
                    }
                    else
                        binding.lessons.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.test_list_item, android.R.id.text1, mutableListOf("Нет уроков"))
                }
            }
        }

    }


}

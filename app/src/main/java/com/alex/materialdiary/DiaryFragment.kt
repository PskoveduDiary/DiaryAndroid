package com.alex.materialdiary

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.alex.materialdiary.databinding.FragmentDiaryBinding
import com.alex.materialdiary.sys.LessonBottomSheet
import com.alex.materialdiary.sys.adapters.ProgramAdapterDiary
import com.alex.materialdiary.sys.adapters.WeekAdapter
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.diary_day.DatumDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

fun Date?.toText(): String {
    if(this == null) return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(this)
}

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DiaryFragment : Fragment(){
    private var _binding: FragmentDiaryBinding? = null
    var cuurent_date: Date? = null;
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        PskoveduApi.getInstance(requireContext(), findNavController())
        if (cuurent_date == null) cuurent_date = Calendar.getInstance().time
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                val cal: Calendar = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cuurent_date = cal.time
                /*binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                        "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"*/
                getDay(cuurent_date.toText())
                binding.lessons.adapter = null
            }
        }
        binding.date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val cur = Calendar.getInstance()
                cur.time = cuurent_date!!
                DatePickerDialog(this@DiaryFragment.requireContext(),
                    dateSetListener,
                    cur.get(Calendar.YEAR),
                    cur.get(Calendar.MONTH),
                    cur.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
        binding.today.setOnClickListener {
            cuurent_date = Calendar.getInstance().time
            getDay(cuurent_date.toText())
            binding.lessons.adapter = null
        }
        /*binding.nextDay.setOnClickListener(View.OnClickListener {
            cuurent_date = Date(cuurent_date.getTime() + 86400000)
            binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                    "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
            getDay(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(cuurent_date.time))
            binding.swiperefresh.isRefreshing = true
            binding.lessons.adapter = null
        })
        binding.prevDay.setOnClickListener(View.OnClickListener {
            cuurent_date = Date(cuurent_date.getTime() - 86400000)
            binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                    "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
            getDay(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(cuurent_date.time))
            binding.swiperefresh.isRefreshing = true
            binding.lessons.adapter = null
        })
        binding.checklist.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
            /*findNavController().navigate(NavGraphDirections.toCheckList().setDate(dateFormat.format(cuurent_date)))*/
        }*/
        return binding.root

    }
    fun get_nav(): NavController {
        return findNavController()
    }
    fun openBottomSheet(data: DatumDay){
        val modalBottomSheet = LessonBottomSheet.newInstance(data)
        modalBottomSheet.show(requireActivity().supportFragmentManager, LessonBottomSheet.TAG)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"*/
        binding.datePickerPager.adapter = WeekAdapter(this)
        binding.datePickerPager.setCurrentItem(300, false)
        getDay(cuurent_date.toText())
        binding.swiperefresh.setOnRefreshListener {
            getDay(cuurent_date.toText())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDay(date: String, updateCurrent: Date? = null) {
        binding.today.paint?.isUnderlineText =
            date != SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        binding.date.text = date
        binding.today.text = "Сегодня"
        binding.swiperefresh.isRefreshing = true
        if (updateCurrent != null) cuurent_date = updateCurrent
        CoroutineScope(Dispatchers.IO).launch {
            val lessons =
                PskoveduApi.getInstance(requireContext(), findNavController()).getDay(date)
            withContext(Dispatchers.Main) {
                if (_binding == null) return@withContext
                binding.swiperefresh.isRefreshing = false
                if (lessons != null) {
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

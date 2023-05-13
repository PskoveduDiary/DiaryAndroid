package com.alex.materialdiary

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alex.materialdiary.databinding.FragmentDiaryBinding
import com.alex.materialdiary.sys.adapters.ProgramAdapterDiary
import com.alex.materialdiary.sys.common.PskoveduApi
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DiaryFragment : Fragment(){
    private var _binding: FragmentDiaryBinding? = null
    var cuurent_date = Calendar.getInstance().time;
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        PskoveduApi.getInstance(requireContext(), findNavController())
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                val cal: Calendar = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cuurent_date = cal.time
                binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                        "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
                getDay(cuurent_date.toString())
                binding.progressBar.visibility = View.VISIBLE
                binding.lessons.adapter = null
            }
        }
        binding.currentDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val cur = Calendar.getInstance()
                cur.time = cuurent_date
                DatePickerDialog(this@DiaryFragment.requireContext(),
                    dateSetListener,
                    cur.get(Calendar.YEAR),
                    cur.get(Calendar.MONTH),
                    cur.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
        binding.nextDay.setOnClickListener(View.OnClickListener {
            cuurent_date = Date(cuurent_date.getTime() + 86400000)
            binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                    "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
            getDay(cuurent_date.toString())
            binding.progressBar.visibility = View.VISIBLE
            binding.lessons.adapter = null
        })
        binding.prevDay.setOnClickListener(View.OnClickListener {
            cuurent_date = Date(cuurent_date.getTime() - 86400000)
            binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                    "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
            getDay(cuurent_date.toString())
            binding.progressBar.visibility = View.VISIBLE
            binding.lessons.adapter = null
        })

        return binding.root


    }
    fun get_nav(): NavController {
        return findNavController()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
        getDay(cuurent_date.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDay(date: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val lessons =
                PskoveduApi.getInstance(requireContext(), findNavController()).getDay(date)
            withContext(Dispatchers.Main) {
                if (_binding == null) return@withContext
                binding.progressBar.visibility = View.GONE
                if (lessons != null) {
                    if (lessons.data.size > 0) {
                        binding.lessons.adapter =
                            ProgramAdapterDiary(
                                this@DiaryFragment,
                                lessons.data
                            )
                    }
                    else
                        binding.lessons.adapter = ArrayAdapter<String>(requireContext(), R.layout.test_list_item, R.id.text1, mutableListOf("Нет уроков"))
                }
            }
        }

    }


}

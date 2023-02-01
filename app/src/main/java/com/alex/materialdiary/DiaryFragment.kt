package com.alex.materialdiary

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
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DiaryFragment : Fragment(), CommonAPI.CommonCallback {
    private lateinit var webView: WebView
    private var _binding: FragmentDiaryBinding? = null
    var cuurent_date = Calendar.getInstance().time;
    lateinit var p: SharedPreferences
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        p = requireActivity().getSharedPreferences("recs", AppCompatActivity.MODE_PRIVATE)
        CommonAPI.getInstance(requireContext(), findNavController())
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
                CommonAPI.getInstance().getDay(this@DiaryFragment, cuurent_date.toString())
                binding.progressBar.visibility = View.VISIBLE
                binding.lessons.adapter = null
            }
        }
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
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
            CommonAPI.getInstance().getDay(this, cuurent_date.toString())
            binding.progressBar.visibility = View.VISIBLE
            binding.lessons.adapter = null
        })
        binding.prevDay.setOnClickListener(View.OnClickListener {
            cuurent_date = Date(cuurent_date.getTime() - 86400000)
            binding.currentDate.text = SimpleDateFormat("EE", Locale.getDefault()).format(cuurent_date.getTime()).uppercase() +
                    "\n ${SimpleDateFormat("dd.MM", Locale.getDefault()).format(cuurent_date.getTime())}"
            CommonAPI.getInstance().getDay(this, cuurent_date.toString())
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
        CommonAPI.getInstance().getDay(this, cuurent_date.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun day(lesson: MutableList<DatumDay>?) {
        if (_binding == null) return
        binding.progressBar.visibility = View.GONE
        if (lesson != null) {
            if (!p.contains("no_first")){
                val edit = p.edit()
                edit.putBoolean("no_first", true)
                edit.apply()
                AlertDialog.Builder(requireContext())
                    .setTitle("Попробуйте новую функцию!")
                    .setMessage("Включите уведомления о контрольных!") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            // Continue with delete operation
                            findNavController().navigate(R.id.to_settings)
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.ic_baseline_error_outline_24)
                    .show()
            }
            if (lesson.size > 0) {
                binding.lessons.adapter =
                    ProgramAdapterDiary(
                        this,
                        lesson
                    )
            }
            else{
                val al = ArrayList<String>()
                al.add("Нет уроков")
                val adapter = ArrayAdapter<String>(this.requireContext(), android.R.layout.test_list_item, android.R.id.text1, al)
                binding.lessons.adapter = adapter
            }
        }
    }


}

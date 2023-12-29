package com.alex.materialdiary

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentMarksBinding
import com.alex.materialdiary.sys.MarksInfoBottomSheet
import com.alex.materialdiary.sys.adapters.RecycleAdapterMarksGroup
import com.alex.materialdiary.sys.adapters.RecycleAdapterPeriods
import com.alex.materialdiary.sys.adapters.RecycleAdapterPeriodsGroup
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.period_marks.Mark
import com.alex.materialdiary.sys.net.models.period_marks.PeriodMarks
import com.alex.materialdiary.sys.net.models.period_marks.PeriodMarksData
import com.alex.materialdiary.utils.MarksTranslator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarksFragment : Fragment(){
    private var _binding: FragmentMarksBinding? = null

    lateinit var api: PskoveduApi
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val chips: MutableList<Chip> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarksBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = PskoveduApi.getInstance(requireContext(), findNavController())
        binding.tabs.addOnTabSelectedListener((object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.marks.adapter = null
                println("selected" + tab!!.position)
                when(tab!!.position){
                    0 -> {
                        binding.periods.visibility = View.VISIBLE
                        getPeriods()
                    }
                    1 -> {
                        binding.periods.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        getItogMarks()
                    }
                    2 -> {
                        binding.periods.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        getAllMarks()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
                println("deselected" + tab!!.position)
            }
        }))
        val llm2 = LinearLayoutManager(requireContext())
        binding.marks.layoutManager = llm2
        getPeriods()
    }
    fun showLoader(){
        binding.progressBar.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        CoroutineScope(Dispatchers.IO).launch {
            PskoveduApi.getInstance(requireContext(), findNavController()).updateMarksCache()
        }
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        PskoveduApi.getInstance(requireContext(), findNavController())
    }

    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {
            PskoveduApi.getInstance(requireContext(), findNavController()).updateMarksCache()
        }
    }
    fun getPeriods() {
        CoroutineScope(Dispatchers.IO).launch {
            val periods = api.getPeriods()
            var cur_per_start: String = ""
            var cur_per_end: String = ""
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if(periods == null) return@withContext
                if (_binding == null) return@withContext

                try {
                    val cur_per = MarksTranslator.get_cur_period(periods.data)
                    val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
                    binding.progressBar.visibility = View.VISIBLE
                    cur_per_start = cur_per[0].toString(formatter)
                    cur_per_end = cur_per[1].toString(formatter)
                    getMarks(cur_per_start, cur_per_end)
                }
                catch (_: Exception){}
                binding.periods.removeAllViews()
                chips.clear()
                periods.data.forEach {
                    val chip = (View.inflate(context, R.layout.period_item, null)) as Chip
                    chip.text = it.name
                    if (it.dateBegin == cur_per_start && it.dateEnd == cur_per_end) chip.isSelected = true
                    chip.setOnClickListener {_ ->
                        getMarks(it.dateBegin, it.dateEnd)
                        showLoader()
                        chips.forEach { it.isSelected = false }
                        chip.isSelected = true
                    }
                    chips += chip
                    /*chip.setOnLongClickListener {
                        CoroutineScope(Dispatchers.IO).launch {api.forceUpdatePeriods()}
                        true
                    }*/
                    binding.periods.addView(chip)
                }
            }
        }
    }

    fun getMarks(from: String, to: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val (marks, showdiffs) = api.getPeriodMarks(from, to)
            withContext(Dispatchers.Main) {
                if (marks?.data == null) return@withContext
                if (_binding == null) return@withContext
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.marks.adapter =
                        RecycleAdapterMarksGroup(this@MarksFragment, marks.data, showdiffs)
                        binding.progressBar.visibility = View.GONE
                    },
                    250
                )
            }
        }
    }
    fun openBottomSheet(data: PeriodMarksData){
        val modalBottomSheet = MarksInfoBottomSheet.newInstance(data)
        modalBottomSheet.show(requireActivity().supportFragmentManager, MarksInfoBottomSheet.TAG)

    }
    fun getAllMarks() {
        CoroutineScope(Dispatchers.IO).launch {
            val marks = api.getAllMarks()
            if (marks?.data == null) return@launch
            var adapter = RecycleAdapterMarksGroup(this@MarksFragment, marks.data, false)
            withContext(Dispatchers.Main) {
                if (_binding == null) return@withContext
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.marks.adapter = adapter
                        binding.progressBar.visibility = View.GONE
                    },
                    250
                )
            }
        }
    }

    fun getItogMarks() {
        CoroutineScope(Dispatchers.IO).launch {
            val periods = api.getItogMarks()
            withContext(Dispatchers.Main) {
                if (periods == null) return@withContext
                if (_binding == null) return@withContext
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.marks.adapter =
                            RecycleAdapterPeriodsGroup(requireContext(), periods.data)
                        binding.progressBar.visibility = View.GONE
                    },
                    250
                )

            }
        }
    }
}

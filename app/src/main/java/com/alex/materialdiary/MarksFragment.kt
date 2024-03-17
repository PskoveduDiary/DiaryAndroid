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
import com.alex.materialdiary.sys.SortMarksBottomSheet
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarksFragment : Fragment() {
    private var _binding: FragmentMarksBinding? = null

    lateinit var api: PskoveduApi
    var sort: String = "name_asc"
    val marks_data: MutableList<PeriodMarksData> = mutableListOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val chips: MutableList<Chip> = mutableListOf()
    var marksJob: Job? = null
    var periodsJob: Job? = null
    var dontshowepmty: Boolean = false
    var showhidden: Boolean = false
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
                sort = "name_asc"
                println("selected" + tab!!.position)
                when (tab!!.position) {
                    0 -> {
                        binding.periods.visibility = View.VISIBLE
                        binding.sortClick.visibility = View.VISIBLE
                        getPeriods()
                    }

                    1 -> {
                        binding.periods.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.sortClick.visibility = View.GONE
                        getItogMarks()
                    }

                    2 -> {
                        binding.periods.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.sortClick.visibility = View.VISIBLE
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
        binding.sortClick.setOnClickListener{
            openSortBottomSheet()
        }
        binding.marks.layoutManager = llm2
        getPeriods()
    }

    fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        CoroutineScope(Dispatchers.IO).launch {
            if (activity != null)
                PskoveduApi.getInstance(requireActivity().applicationContext, findNavController()).updateMarksCache()
        }
        marksJob?.cancel()
        periodsJob?.cancel()
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
            if (activity != null)
                PskoveduApi.getInstance(requireActivity().applicationContext, findNavController()).updateMarksCache()
        }
    }

    fun getPeriods() {
        periodsJob = CoroutineScope(Dispatchers.IO).launch {
            val periods = api.getPeriods()
            var cur_per_start: String = ""
            var cur_per_end: String = ""
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (periods == null) return@withContext
                if (_binding == null) return@withContext

                try {
                    val cur_per = MarksTranslator.get_cur_period(periods.data)
                    val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
                    binding.progressBar.visibility = View.VISIBLE
                    cur_per_start = cur_per[0].toString(formatter)
                    cur_per_end = cur_per[1].toString(formatter)
                    getMarks(cur_per_start, cur_per_end)
                } catch (_: Exception) {
                }
                binding.periods.removeAllViews()
                chips.clear()
                periods.data.forEach {
                    val chip = (View.inflate(context, R.layout.period_item, null)) as Chip
                    chip.text = it.name
                    if (it.dateBegin == cur_per_start && it.dateEnd == cur_per_end) chip.isSelected =
                        true
                    chip.setOnClickListener { _ ->
                        marks_data.clear()
                        sort = "name_asc"
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
    fun updateData(showdiffs: Boolean = false){
        var marks_after_filters = marks_data.toList()
        if (dontshowepmty) marks_after_filters = marks_after_filters.filter { it.marks.isNotEmpty() }
        if (!showhidden) marks_after_filters = marks_after_filters.filter { (it.hideInReports == false) and (it.hideInSchedule == false) }
        if (_binding == null) return
        binding.marks.adapter =
            RecycleAdapterMarksGroup(this@MarksFragment, marks_after_filters.toMutableList(), showdiffs)
        binding.progressBar.visibility = View.GONE
    }
    fun getMarks(from: String, to: String) {
        marksJob = CoroutineScope(Dispatchers.IO).launch {
            val (marks, showdiffs) = api.getPeriodMarks(from, to)
            if (marks?.data == null) return@launch
            marks_data.clear()
            marks_data.addAll(marks.data)
            withContext(Dispatchers.Main) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        updateData(showdiffs)
                    },
                    250
                )
            }
        }
    }

    fun openBottomSheet(data: PeriodMarksData) {
        val modalBottomSheet = MarksInfoBottomSheet.newInstance(data)
        modalBottomSheet.show(requireActivity().supportFragmentManager, MarksInfoBottomSheet.TAG)

    }
    fun openSortBottomSheet() {
        val modalBottomSheet = SortMarksBottomSheet(this)
        modalBottomSheet.show(requireActivity().supportFragmentManager, "SortBottomSheet")

    }

    fun getAllMarks() {
        marksJob = CoroutineScope(Dispatchers.IO).launch {
            val marks = api.getAllMarks()
            if (marks?.data == null) return@launch
            marks_data.clear()
            marks_data.addAll(marks.data)
            var adapter = RecycleAdapterMarksGroup(this@MarksFragment, marks.data, false)
            withContext(Dispatchers.Main) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        updateData()
                    },
                    250
                )
            }
        }
    }

    fun getItogMarks() {
        marksJob = CoroutineScope(Dispatchers.IO).launch {
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

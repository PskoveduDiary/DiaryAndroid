package com.alex.materialdiary

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentMarksBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterMarksGroup
import com.alex.materialdiary.sys.adapters.RecycleAdapterPeriods
import com.alex.materialdiary.sys.adapters.RecycleAdapterPeriodsGroup
import com.alex.materialdiary.sys.common.PskoveduApi
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData
import com.alex.materialdiary.sys.common.models.periods.Periods
import com.alex.materialdiary.utils.MarksTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        binding.periodsButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
        binding.itogButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        binding.periodsButton.setOnClickListener {
            binding.periods.visibility = View.VISIBLE
            binding.marks.adapter = null
            it.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            binding.itogButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        }
        binding.itogButton.setOnClickListener {
            binding.periods.visibility = View.GONE
            it.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.periodsButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            getItogMarks()
            binding.marks.adapter = null
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.AllButton.setOnClickListener {
            binding.periods.visibility = View.GONE
            it.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.periodsButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            binding.itogButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            getAllMarks()
            binding.marks.adapter = null
            binding.progressBar.visibility = View.VISIBLE
        }
        val llm = LinearLayoutManager(requireContext())
        val llm2 = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.periods.layoutManager = llm
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
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if(periods == null) return@withContext
                if(periods?.data == null) return@withContext
                if (_binding == null) return@withContext

                try {
                    val cur_per = MarksTranslator.get_cur_period(periods.data)
                    val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
                    binding.progressBar.visibility = View.VISIBLE
                    getMarks(cur_per[0].toString(formatter), cur_per[1].toString(formatter))
                }
                catch (_: Exception){}
                binding.periods.adapter = RecycleAdapterPeriods(this@MarksFragment, periods.data)
            }
        }
    }

    fun getMarks(from: String, to: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val (marks, showdiffs) = api.getPeriodMarks(from, to)
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (marks?.data == null) return@withContext
                if (_binding == null) return@withContext
                binding.marks.adapter =
                    RecycleAdapterMarksGroup(requireContext(), marks.data, showdiffs)
            }
        }
    }
    fun getAllMarks() {
        CoroutineScope(Dispatchers.IO).launch {
            val marks = api.getAllMarks()
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (marks?.data == null) return@withContext
                if (_binding == null) return@withContext
                binding.marks.adapter =
                    RecycleAdapterMarksGroup(requireContext(), marks.data, false)
            }
        }
    }

    fun getItogMarks() {
        CoroutineScope(Dispatchers.IO).launch {
            val periods = api.getItogMarks()
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (periods == null) return@withContext
                if (periods?.data == null) return@withContext
                if (_binding == null) return@withContext
                binding.marks.adapter =
                    RecycleAdapterPeriodsGroup(requireContext(), periods.data)
            }
        }
    }
}

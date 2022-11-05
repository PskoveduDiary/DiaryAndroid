package com.alex.materialdiary

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentMarksBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterMarksGroup
import com.alex.materialdiary.sys.adapters.RecycleAdapterPeriods
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriodData
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarksFragment : Fragment(), CommonAPI.MarksCallback {
    private var _binding: FragmentMarksBinding? = null
    var cuurent_date = Calendar.getInstance().time;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarksBinding.inflate(inflater, container, false)
        binding.periodsButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
        binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        binding.periodsButton.setOnClickListener {
            binding.periods.visibility = View.VISIBLE
            binding.marks.adapter = null
            binding.periodsButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        }
        binding.AllButton.setOnClickListener {
            binding.periods.visibility = View.GONE
            binding.AllButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.periodsButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            CommonAPI.getInstance().allMarks(this)
        }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val llm = LinearLayoutManager(requireContext())
        val llm2 = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.periods.layoutManager = llm
        binding.marks.layoutManager = llm2
        CommonAPI.getInstance().getPeriods(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun allperiods(periods: AllPeriods?) {
        if(periods == null) return
        binding.periods.adapter = RecycleAdapterPeriods(this, periods.data)
    }

    override fun marks(marks: MutableList<PeriodMarksData>?) {
        if (marks == null) return
        binding.marks.adapter = RecycleAdapterMarksGroup(this.requireContext(), marks)
    }


}

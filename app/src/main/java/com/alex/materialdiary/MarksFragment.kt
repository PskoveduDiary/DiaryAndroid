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
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData
import com.alex.materialdiary.sys.common.models.periods.Periods
import com.alex.materialdiary.utils.MarksTranslator
import org.joda.time.format.DateTimeFormat


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarksFragment : Fragment(), CommonAPI.MarksCallback {
    private var _binding: FragmentMarksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarksBinding.inflate(inflater, container, false)
        binding.periodsButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
        binding.itogButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        binding.AllButton.setBackgroundColor(Color.parseColor("#d1bdff"))
        val api = CommonAPI.getInstance(requireContext(), findNavController())
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
            api.getPeriods(this)
            binding.marks.adapter = null
            binding.progressBar.visibility = View.VISIBLE
        }
//        binding.itogButton.setOnLongClickListener {
            //CommonAPI.getInstance().updateMarksCache()
//            false
//        }
        binding.AllButton.setOnClickListener {
            binding.periods.visibility = View.GONE
            it.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
            binding.periodsButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            binding.itogButton.setBackgroundColor(Color.parseColor("#d1bdff"))
            api.allMarks(this)
            binding.marks.adapter = null
            binding.progressBar.visibility = View.VISIBLE
        }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val api = CommonAPI.getInstance(requireContext(), findNavController())
        super.onViewCreated(view, savedInstanceState)
        val llm = LinearLayoutManager(requireContext())
        val llm2 = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.periods.layoutManager = llm
        binding.marks.layoutManager = llm2
        api.getAllPeriods(this)
        val cur_per = MarksTranslator.get_cur_period(api.cached.data)
        val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
        binding.progressBar.visibility = View.VISIBLE
        CommonAPI.getInstance().periodMarks(this, cur_per[0].toString(formatter), cur_per[1].toString(formatter))
    }
    fun showLoader(){
        binding.progressBar.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        CommonAPI.getInstance(requireContext(), findNavController()).updateMarksCache()
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        CommonAPI.getInstance(requireContext(), findNavController())
    }

    override fun onStop() {
        super.onStop()
        CommonAPI.getInstance(requireContext(), findNavController()).updateMarksCache()
    }
    override fun allperiods(periods: AllPeriods?) {
        binding.progressBar.visibility = View.GONE
        if(periods == null) return
        if(periods.data == null) return
        binding.periods.adapter = RecycleAdapterPeriods(this, periods.data)
    }

    override fun marks(marks: MutableList<PeriodMarksData>?, needShowsDifs: Boolean?) {
        binding.progressBar.visibility = View.GONE
        if (marks == null) return

       //toast(CommonAPI.getInstance().getMarksDifferencesCount(MarksTranslator(marks).items).toString())
        binding.marks.adapter = RecycleAdapterMarksGroup(this.requireContext(), marks, needShowsDifs)
    }

    override fun periods(periods: Periods?) {
        binding.progressBar.visibility = View.GONE
        if (periods == null) return
        if (periods.data == null) return
        binding.marks.adapter = RecycleAdapterPeriodsGroup(this.requireContext(), periods.data)
    }


}

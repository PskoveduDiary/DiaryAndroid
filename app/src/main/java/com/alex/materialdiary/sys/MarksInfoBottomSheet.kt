package com.alex.materialdiary.sys

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.ui.fragments.AverageFragmentArgs
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.FragmentMarksBinding
import com.alex.materialdiary.databinding.MarksInfoBottomsheetBinding
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.period_marks.PeriodMarksData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable
import java.text.DecimalFormat


class MarksInfoBottomSheet : BottomSheetDialogFragment() {
    @Suppress("DEPRECATION")
    inline fun <reified T : Parcelable> Bundle.customGetParcelable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            getParcelable(key) as? T
        }
    }
    private var _binding: MarksInfoBottomsheetBinding? = null
    var data: PeriodMarksData? = null
    var marks = mutableListOf<Int>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MarksInfoBottomsheetBinding.inflate(inflater, container, false)
        data = arguments?.customGetParcelable<PeriodMarksData>("data")
        if (data != null) {
            marks = data!!.marks.map { it -> it.value }.toMutableList()
            binding.fivetofive.text = data!!.getToFive(requireContext()).parseAsHtml()
            if (marks.average() < 4.6) {
                binding.fivetofour.text = data!!.getFiveToFour(requireContext()).parseAsHtml()
            }
            if (marks.average() < 3.6) {
                binding.fourtofour.text = data!!.getFourToFour(requireContext()).parseAsHtml()
            }
            calc()
        }
        binding.one.setOnClickListener {
            marks += 1
            calc()
        }
        binding.two.setOnClickListener {
            marks += 2
            calc()
        }
        binding.three.setOnClickListener {
            marks += 3
            calc()
        }
        binding.four.setOnClickListener {
            marks += 4
            calc()
        }
        binding.five.setOnClickListener {
            marks += 5
            calc()
        }
        binding.clearOne.setOnClickListener {
            marks.removeLastOrNull()
            calc()
        }
        binding.clearAll.setOnClickListener {
            marks.clear()
            calc()
        }
        return binding.root
    }
    fun calc(){
        if(marks.size == 0){
            binding.average.text = "n/a"
            binding.average.setTextColor(Color.GRAY)
            binding.history.text = ""
            return
        }
        binding.history.text = marks.joinToString(separator = ", ")
        val average = marks.average().toFloat()
        binding.average.text =
            DecimalFormat("#0.00").format(average.toDouble())
        if (average < 1.5f) binding.average.setTextColor(resources.getColor(R.color.one))
        else if (average == 1.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 2.5f) binding.average.setTextColor(resources.getColor(R.color.two))
        else if (average == 2.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 3.5f) binding.average.setTextColor(resources.getColor(R.color.three))
        else if (average == 3.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 4.5f) binding.average.setTextColor(resources.getColor(R.color.four))
        else if (average == 4.5f) binding.average.setTextColor(Color.GRAY)
        else binding.average.setTextColor(resources.getColor(R.color.five))

        if (data != null) {
            binding.fivetofive.text = data!!.getToFive(requireContext(), marks.toMutableList()).parseAsHtml()
            if (marks.average() < 4.6) {
                binding.fivetofour.text = data!!.getFiveToFour(requireContext(), marks.toMutableList()).parseAsHtml()
            }
            if (marks.average() < 3.6) {
                binding.fourtofour.text = data!!.getFourToFour(requireContext(), marks.toMutableList()).parseAsHtml()
            }
        }
    }
    companion object {
        const val TAG = "ModalBottomSheet"
        fun newInstance(
            data: PeriodMarksData
        ): MarksInfoBottomSheet = MarksInfoBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable("data", data)
            }
        }
    }
}
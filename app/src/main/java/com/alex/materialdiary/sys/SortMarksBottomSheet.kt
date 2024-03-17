package com.alex.materialdiary.sys

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.iterator
import androidx.navigation.fragment.findNavController
import com.alex.materialdiary.HomeFragment
import com.alex.materialdiary.MarksFragment
import com.alex.materialdiary.databinding.SortMarksBottomsheetBinding
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.diary_day.DatumDay
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip


class SortMarksBottomSheet(marks: MarksFragment) : BottomSheetDialogFragment() {
    private val marksFragment = marks
    private var _binding: SortMarksBottomsheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SortMarksBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.check(when(marksFragment.sort){
            "avg_des" -> binding.chipAverageDescending.id
            "avg_asc" -> binding.chipAverageAscending.id
            "name_des" -> binding.chipNameDescending.id
            "name_asc" -> binding.chipNameAscending.id
            "count_des" -> binding.chipCountDescending.id
            "count_asc" -> binding.chipCountAscending.id
            else -> 0
        })
        binding.ShowHidden.isChecked = marksFragment.showhidden
        binding.dontShowWithoutMarks.isChecked = marksFragment.dontshowepmty
        binding.chipAverageDescending.setOnClickListener {
            marksFragment.marks_data.sortBy { -it.average }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "avg_des"
        }
        binding.chipAverageAscending.setOnClickListener {
            marksFragment.marks_data.sortBy { it.average }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "avg_asc"
        }
        binding.chipNameDescending.setOnClickListener {
            marksFragment.marks_data.sortByDescending { it.subjectName }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "name_des"
        }
        binding.chipNameAscending.setOnClickListener {
            marksFragment.marks_data.sortBy { it.subjectName }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "name_asc"
        }
        binding.chipCountDescending.setOnClickListener {
            marksFragment.marks_data.sortByDescending { it.marks.count() }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "count_des"
        }
        binding.chipCountAscending.setOnClickListener {
            marksFragment.marks_data.sortBy { it.marks.count() }
            binding.chipGroup.check(it.id)
            marksFragment.updateData()
            marksFragment.sort = "count_asc"
        }
        binding.ShowHidden.addOnCheckedStateChangedListener { checkBox, state ->
            marksFragment.showhidden = (state == 1)
            marksFragment.updateData()
        }
        binding.dontShowWithoutMarks.addOnCheckedStateChangedListener { checkBox, state ->
            marksFragment.dontshowepmty = (state == 1)
            marksFragment.updateData()
        }
    }
}
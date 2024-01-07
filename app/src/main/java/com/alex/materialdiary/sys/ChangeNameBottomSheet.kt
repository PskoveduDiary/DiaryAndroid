package com.alex.materialdiary.sys

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.AverageFragmentArgs
import com.alex.materialdiary.HomeFragment
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.ChangeNameBottomsheetBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterFiles
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.diary_day.DatumDay
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable
import java.text.DecimalFormat


class ChangeNameBottomSheet(home: HomeFragment) : BottomSheetDialogFragment() {
    val homeFragment = home
    private var _binding: ChangeNameBottomsheetBinding? = null
    var data: DatumDay? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangeNameBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputName.setText(PskoveduApi.getInstance(requireContext(), findNavController()).getUserName())
        binding.saveName.setOnClickListener {
            DiaryPreferences(requireContext()).set("name", binding.inputName.text.toString())
            homeFragment.reloadName()
            this@ChangeNameBottomSheet.dismissNow()
        }
        binding.inputName.requestFocus()
    }
}
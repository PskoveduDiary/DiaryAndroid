package com.alex.materialdiary.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.ChooseColorSchemeBottomsheetBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterMarksScheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ChooseColorSchemeBottomSheet() : BottomSheetDialogFragment() {
    private var _binding: ChooseColorSchemeBottomsheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChooseColorSchemeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecycleAdapterMarksScheme(requireContext(), this)
    }
    fun close(){
        dismiss()
    }
}
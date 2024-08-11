package com.alex.materialdiary.ui.bottom_sheets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.DopBottomsheetBinding
import com.alex.materialdiary.sys.net.models.dop_programs.DopProgramData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration


class DopBottomSheet(dopProgramData: DopProgramData? = null) : BottomSheetDialogFragment() {
    val dopProgramData = dopProgramData
    private var _binding: DopBottomsheetBinding? = null
    val imgLoader = ImageLoader.getInstance()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DopBottomsheetBinding.inflate(inflater, container, false)
        imgLoader.init(ImageLoaderConfiguration.createDefault(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dopProgramData == null) {
            dismiss()
            return
        }
        binding.dopName.text = dopProgramData.name
        binding.dopDescription.text = dopProgramData.long_DESCRIPTION.parseAsHtml()
        imgLoader.displayImage("https://dop.pskovedu.ru/file/big_thumb/" + dopProgramData.fileguid, binding.dopImage);
        val chips = mutableListOf<String>()
        chips += dopProgramData.price
        if(dopProgramData.hours != null) chips += (dopProgramData.hours + " часов")
        if(dopProgramData.eduterm != null) chips += dopProgramData.eduterm
        if (dopProgramData.ovz != 0) chips += "С ОВЗ"
        chips += dopProgramData.address
        chips.forEach {
            val chip = (View.inflate(context, R.layout.period_item, null)) as Chip
            chip.text = it.parseAsHtml().trim()
            chip.isSelected = true
            binding.dopInfos.addView(chip)
        }
        binding.dopGo.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dop.pskovedu.ru/materials/program/" + dopProgramData.sys_GUID))
            startActivity(browserIntent)
        }
    }
}
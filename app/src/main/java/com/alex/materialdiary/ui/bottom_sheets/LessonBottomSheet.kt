package com.alex.materialdiary.ui.bottom_sheets

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.LessonBottomsheetBinding

import com.alex.materialdiary.sys.adapters.RecycleAdapterFiles
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDayData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable


class LessonBottomSheet : BottomSheetDialogFragment() {
    @Suppress("DEPRECATION")
    inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }
    private var _binding: LessonBottomsheetBinding? = null
    var data: DiaryDayData? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LessonBottomsheetBinding.inflate(inflater, container, false)
        data = arguments?.customGetSerializable<DiaryDayData>("data")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data?.subjectName == null) {
            this.dismissNow()
            return
        }
        binding.lessName.text = "${data!!.lessonNumber}. ${data!!.subjectName}"
        binding.lessTeacher.text = "${data!!.teacherName}"
        binding.lessDate.text = "${data!!.lessonDate}"
        binding.lessTime.text = "${data!!.lessonTimeBegin} - ${data!!.lessonTimeEnd}"
        if (data!!.topic != null) binding.lessTopic.text = "Тема: ${data!!.topic}"
        else binding.lessTopic.text = ""
        if (data!!.homeworkPrevious != null) binding.lessHomework.text = "${data!!.homeworkPrevious!!.homework?.replace(".ru:/", ".ru/")}"
        else binding.lessHomework.text = ""
        if (data!!.homework != null) binding.lessHomework2.text =
            data!!.homework!!.replace(".ru:/", ".ru/")
        else binding.lessHomework2.text = ""
        binding.lessMark.text = data!!.marks?.joinToString(", ") {
            it.shortName.toString()
        }
        binding.absenceText.text = data!!.absence?.joinToString(", ") {
            it.fullName.toString()
        }
        binding.lessComment.text = data!!.notes?.joinToString(", ")
        if (data!!.homeworkPrevious?.homework != null) {
            val homework = data!!.homeworkPrevious!!.homework!!.replace(".ru:/", ".ru/")
            if (homework.contains("https://one.pskovedu.ru/file/download/")){
                binding.card5.visibility = View.VISIBLE
                val links = homework.split(" ", "\n").filter {
                    it.contains("https://one.pskovedu.ru/file/download/")
                }
                val llm = LinearLayoutManager(context)
                llm.orientation = LinearLayoutManager.HORIZONTAL
                binding.filesRecycler.layoutManager = llm
                binding.filesRecycler.adapter = RecycleAdapterFiles(requireContext(), links.toMutableList())
            }
        }
    }
    companion object {
        const val TAG = "ModalBottomSheet"
        fun newInstance(
            data: DiaryDayData
        ): LessonBottomSheet = LessonBottomSheet().apply {
            arguments = Bundle().apply {
                putSerializable("data", data)
            }
        }
    }
}
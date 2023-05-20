package com.alex.materialdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentLessonBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterFiles

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LessonFragment : Fragment(){
    private lateinit var webView: WebView
    private var _binding: FragmentLessonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: LessonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.lesson.subjectName == null) findNavController().navigateUp()
        binding.lessName.text = "${args.lesson.lessonNumber}. ${args.lesson.subjectName}"
        binding.lessTeacher.text = "${args.lesson.teacherName}"
        binding.lessDate.text = "${args.lesson.lessonDate}"
        binding.lessTime.text = "${args.lesson.lessonTimeBegin} - ${args.lesson.lessonTimeEnd}"
        if (args.lesson.topic != null) binding.lessTopic.text = "Тема: ${args.lesson.topic}"
        else binding.lessTopic.text = ""
        if (args.lesson.homeworkPrevious != null) binding.lessHomework.text = "${args.lesson.homeworkPrevious!!.homework?.replace(".ru:/", ".ru/")}"
        else binding.lessHomework.text = ""
        if (args.lesson.homework != null) binding.lessHomework2.text =
            args.lesson.homework!!.replace(".ru:/", ".ru/")
        else binding.lessHomework2.text = ""
        binding.lessMark.text = args.lesson.marks?.joinToString(", ") {
            it.shortName.toString()
        }
        binding.absenceText.text = args.lesson.absence?.joinToString(", ") {
            it.fullName.toString()
        }
        binding.lessComment.text = args.lesson.notes?.joinToString(", ")
        if (args.lesson.homeworkPrevious?.homework != null) {
            val homework = args.lesson.homeworkPrevious!!.homework!!
            if (homework.contains("https://one.pskovedu.ru:/file/download/")){
                binding.card5.visibility = View.VISIBLE
                val links = homework.split(" ", "\n").filter {
                    it.contains("https://one.pskovedu.ru:/file/download/")
                }
                val llm = LinearLayoutManager(context)
                llm.orientation = LinearLayoutManager.HORIZONTAL
                binding.filesRecycler.layoutManager = llm
                binding.filesRecycler.adapter = RecycleAdapterFiles(requireContext(), links.toMutableList())
                println(links)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

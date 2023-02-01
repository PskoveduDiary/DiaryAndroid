package com.alex.materialdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentKrBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterKrInfo
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.Crypt
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import com.alex.materialdiary.sys.common.models.kr.kr_info
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class KrFragment : Fragment(), CommonAPI.CommonCallback {
    private var _binding: FragmentKrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Crypt.generateKeyFromString("aYXfLjOMB9V5az9Ce8l+7A==");
        val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
        val api = CommonAPI(requireContext())
        api.getDay(this, cuurent_date.toString())
        _binding = FragmentKrBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun check(str: String): MutableList<String> {
        val finded = mutableListOf<String>()
        keywords.kr_mini.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        keywords.kr_maybe.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        keywords.kr.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        return finded
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun day(lesson: MutableList<DatumDay>?) {
        binding.progressBar.visibility = View.GONE
        if (lesson == null) {
            binding.textView4.visibility = View.VISIBLE
            return;
        }
        val lessns = mutableListOf<kr_info>()
        for (lsn in lesson) {
            val this_lesson = lsn.subjectName?.let { kr_info(it) }
            var str = ""
            if (lsn.homeworkPrevious?.homework != null) {
                val c = lsn.homeworkPrevious!!.homework!!
                str += c
            }
            str += " "
            if (lsn.topic != null) {
                str += lsn.topic!!
            }
            keywords.kr_maybe.forEach {
                if (str.contains(it)) {
                    this_lesson?.keyword?.add(it)
                    this_lesson?.type = kr_info.TYPES.MAYBE
                }
            }
            keywords.kr_mini.forEach {
                if (str.contains(it)) {
                    this_lesson?.keyword?.add(it)
                    this_lesson?.type = kr_info.TYPES.MINI
                }
            }
            keywords.kr.forEach {
                if (str.contains(it)) {
                    this_lesson?.keyword?.add(it)
                    this_lesson?.type = kr_info.TYPES.FULL
                }
            }
            if (this_lesson != null) {
                if (this_lesson.type != kr_info.TYPES.NONE){
                    lessns.add(this_lesson)
                }
            }
        }
        val no_dubls = lessns.distinctBy { it.lessonName }
        if (no_dubls.size == 0){
            binding.textView4.visibility = View.VISIBLE
            return
        }
        binding.krs.layoutManager = LinearLayoutManager(requireContext())
        binding.krs.adapter = RecycleAdapterKrInfo(requireContext(), no_dubls)
    }
}

package com.alex.materialdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentKrBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterKrInfo
import com.alex.materialdiary.sys.common.PskoveduApi
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import com.alex.materialdiary.sys.common.models.kr.kr_info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class KrFragment : Fragment() {
    private var _binding: FragmentKrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Crypt.generateKeyFromString("aYXfLjOMB9V5az9Ce8l+7A==");
        val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
        val api = PskoveduApi.getInstance(requireContext(), findNavController())

        CoroutineScope(Dispatchers.IO).launch {
            val day = api.getDay( cuurent_date.toString())
            withContext(Dispatchers.Main) {
                setDay(day?.data)
            }
        }
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

    fun setDay(lesson: MutableList<DatumDay>?) {
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
            str = str.lowercase()
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

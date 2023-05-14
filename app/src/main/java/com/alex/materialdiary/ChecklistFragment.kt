package com.alex.materialdiary

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentChecklistBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterCheckList
import com.alex.materialdiary.sys.net.AdlemxApi
import com.alex.materialdiary.sys.net.AdlemxClient
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.PskoveduEndpoints
import com.alex.materialdiary.sys.net.models.check_list.CheckList
import com.alex.materialdiary.sys.net.models.check_list.CheckListShow
import com.alex.materialdiary.sys.net.models.check_list.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ChecklistFragment : Fragment(){
    private var _binding: FragmentChecklistBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val tomorrow: String = dateFormat.format(calendar.time)
        CoroutineScope(Dispatchers.IO).launch {
            val lessons =
                PskoveduApi.getInstance(requireContext()).getDay(tomorrow)?.data ?: return@launch
            if (lessons.size == 0) return@launch
            val checklist_lessons = mutableListOf<CheckListShow>()
            val sync_data = AdlemxClient.getEndpoints()
                .get_checklist(PskoveduApi.getInstance().guid, tomorrow).lessons
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (sync_data.size == 0) {
                    lessons.forEach {
                        if (it.homeworkPrevious?.homework?.length == 0) return@forEach
                        checklist_lessons += CheckListShow(
                            it.subjectName!!,
                            false,
                            it.homeworkPrevious?.homework
                        )
                    }
                    AdlemxClient.getEndpoints().set_checklist(
                        PskoveduApi.getInstance().guid,
                        tomorrow,
                        checklist_lessons.map { it.toLesson() })
                } else
                    for (i in 0 until sync_data.size) {
                        if (lessons[i].homeworkPrevious?.homework?.length == 0) continue
                        checklist_lessons += CheckListShow(
                            sync_data[i].name,
                            sync_data[i].done,
                            lessons[i].homeworkPrevious?.homework
                        )
                    }
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter =
                    RecycleAdapterCheckList(requireContext(), checklist_lessons)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

package com.alex.materialdiary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentChecklistBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterCheckList
import com.alex.materialdiary.sys.net.AdlemxClient
import com.alex.materialdiary.sys.net.PskoveduApi
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
    lateinit var tomorrow: String
    val args: ChecklistFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
        if (args.date != "0.0"){
            val date = dateFormat.parse(args.date!!)
            if (date != null) calendar.time = date
        }
        val showDateFormat: DateFormat = SimpleDateFormat("dd.MM")
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        tomorrow = dateFormat.format(calendar.time)
        binding.date.text = "На ${showDateFormat.format(calendar.time)}"
        CoroutineScope(Dispatchers.IO).launch {
            val lessons =
                PskoveduApi.getInstance(requireContext()).getDay(tomorrow)?.data ?: return@launch
            if (lessons.size == 0) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.errorMsg.text = "Завтра нет уроков"
                    binding.errorMsg.visibility = View.VISIBLE
                }
                return@launch
            }
            val checklist_lessons = mutableListOf<CheckListShow>()
            val sync_data: MutableList<Lesson>
            try {
                sync_data = AdlemxClient.getEndpoints()
                    .get_checklist(PskoveduApi.getInstance().guid, tomorrow).lessons
            }
            catch (e: Exception){
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.errorMsg.text = "К сожалению сейчас этот сервис не работает"
                    binding.errorMsg.visibility = View.VISIBLE
                }
                return@launch
            }
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
                    RecycleAdapterCheckList(this@ChecklistFragment, checklist_lessons)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun check(lessons: MutableList<Lesson>){
        CoroutineScope(Dispatchers.IO).launch {
            AdlemxClient.getEndpoints().set_checklist(PskoveduApi.getInstance(requireContext()).guid, tomorrow, lessons)
        }
    }
}

package com.alex.materialdiary

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentAverageBinding
import com.alex.materialdiary.databinding.FragmentHomeBinding
import com.alex.materialdiary.sys.ChangeNameBottomSheet
import com.alex.materialdiary.sys.LessonBottomSheet
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils
import com.alex.materialdiary.sys.adapters.RecycleAdapterAdvices
import com.alex.materialdiary.sys.adapters.RecycleAdapterDopPrograms
import com.alex.materialdiary.sys.adapters.RecycleAdapterLastMarks
import com.alex.materialdiary.sys.adapters.RecycleAdapterNews
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.marks.Item
import com.alex.materialdiary.sys.net.models.marks.LastMark
import com.alex.materialdiary.utils.MarksTranslator
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.util.*


private val String?.parsed: LocalTime
    get() {
        val formatter = DateTimeFormat.forPattern("HH:mm")
        return LocalTime.parse(this, formatter)
    }

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val lastMarks: MutableList<LastMark> = mutableListOf()
    private var marksJob: Job? = null
    private var timeJob: Job? = null
    private var tipsJob: Job? = null
    private var dopJob: Job? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var api: PskoveduApi
    private lateinit var skeletonAdvices: Skeleton
    private lateinit var skeletonNews: Skeleton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        api = PskoveduApi.getInstance(requireContext(), findNavController())
        val name = api.getUserName()
        binding.MainText.text = if (name != null) "Добро пожаловать, \n${name}!" else "Добро пожаловать!"

        binding.lastMarksRecycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.advicesRecycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newsRecycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dopRecycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.lastMarksRecycleView.adapter = RecycleAdapterLastMarks(this, lastMarks)

        skeletonAdvices = binding.advicesRecycleView.applySkeleton(R.layout.advice_item)
        skeletonNews = binding.newsRecycleView.applySkeleton(R.layout.news_item)

        skeletonAdvices.showSkeleton()
        skeletonNews.showSkeleton()
        binding.MainText.setOnClickListener {
            ChangeNameBottomSheet(this).show(requireActivity().supportFragmentManager, "ChangeNameBottomSheet")
        }

        checkLastMarks()
        checkAdvices()
        checkTime()
        checkNews()
        checkDop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).supportActionBar?.show()
        _binding = null
    }
    fun reloadName(){
        val name = api.getUserName()
        binding.MainText.text = if (name != null) "Добро пожаловать, \n${name}!" else "Добро пожаловать!"
    }
    fun checkLastMarks(){
        marksJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                if ((api.getCachedPeriods()?.data) == null) return@launch
                val cur_per = MarksTranslator.get_cur_period(api.getPeriods()?.data!!)
                val pattern = DateTimeFormat.forPattern("dd.MM.yyyy")
                val (marks, _) = api.getPeriodMarks(cur_per[0].toString(pattern), cur_per[1].toString(pattern))
                withContext(Dispatchers.Main) {
                    marks?.data?.let { MarksTranslator(it).items }?.let { items ->
                        items.forEach { subj ->
                            MarksTranslator.getSubjectMarksDifferences(
                                requireContext(), subj.name,
                                items
                            ).forEach{ mark ->
                                lastMarks += LastMark(mark.value, mark.date, subj.name)
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main){
                    if (lastMarks.size == 0) {
                        val utils = ReadWriteJsonFileUtils(context)
                        val readed = utils.readJsonFileData("marks.json")
                        if (readed == null) binding.marks.visibility = View.GONE
                        val listType = object : TypeToken<ArrayList<Item?>?>() {}.type
                        val t = mutableListOf<LastMark>()
                        Gson().fromJson<List<Item>>(readed, listType).forEach{lesson ->
                            lesson.marks.forEach {
                                t.add(LastMark(it.value, it.date, lesson.name))
                            }
                        }
                        t.sortByDescending { LocalDate.parse(it.date, pattern) }
                        lastMarks.addAll(t.subList(0, 5))

                    }
                    else
                        lastMarks.sortByDescending { LocalDate.parse(it.date, pattern) }
                    if (_binding != null) binding.marks.visibility = View.VISIBLE
                    if (_binding != null) binding.lastMarksRecycleView.adapter?.notifyDataSetChanged()
                }
            }  catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun checkAdvices(){
        tipsJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val tips = api.getAssistantTips()
                withContext(Dispatchers.Main) {
                    if (tips == null || tips.data.isEmpty()) {
                        if (_binding != null) binding.advices.visibility = View.GONE
                    }
                    else {
                        if (_binding != null) binding.advicesRecycleView.adapter = RecycleAdapterAdvices(this@HomeFragment,
                            tips.data.toMutableList())
                    }
                }
            }  catch (e: Exception) {
                e.printStackTrace()
                if (_binding != null) binding.advices.visibility = View.GONE
            }
        }
    }

    fun checkNews(){
        tipsJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val news = api.getEduNews()
                withContext(Dispatchers.Main) {
                    if (news?.news == null || news.news.isEmpty()) {
                        if (_binding != null) binding.news.visibility = View.GONE
                    }
                    else {
                        if (_binding != null) binding.newsRecycleView.adapter = RecycleAdapterNews(this@HomeFragment,
                            news.news.toMutableList())
                    }
                }
            }  catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun checkDop(){
        dopJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val programs = api.getDopPrograms()
                withContext(Dispatchers.Main) {
                    if (programs.isNullOrEmpty()) {
                        if (_binding != null) binding.dop.visibility = View.GONE
                    }
                    else {
                        binding.dopRecycleView.adapter = RecycleAdapterDopPrograms(this@HomeFragment,
                            programs)
                    }
                }
            }  catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @OptIn(ExperimentalStdlibApi::class)
    fun checkTime(){
        timeJob = CoroutineScope(Dispatchers.IO).launch {
            var itog = "Завтра нет уроков"
            val day = api.getDay(LocalDate.now().toString("dd.MM.yyyy"))
            val lastLesson = day?.data?.lastOrNull()
            val firstLesson = day?.data?.lastOrNull()
            if (lastLesson != null && firstLesson != null){
                if (firstLesson.lessonTimeBegin.parsed > LocalTime.now()){
                    itog = "Уроки начнутся в ${firstLesson.lessonTimeBegin}, " +
                            "сегодня ${resources.getQuantityString(R.plurals.lesson, day.data.size).format(day.data.size)}"
                }
                else if (lastLesson.lessonTimeEnd.parsed > LocalTime.now()) {
                    day.data.forEach {
                        if (it.lessonTimeBegin.parsed > LocalTime.now() &&
                            it.lessonTimeEnd.parsed > LocalTime.now()){
                            if (itog == "") {
                                val begin = Period(LocalTime.now(), it.lessonTimeBegin.parsed).minutes
                                itog = "Следующий урок - ${it.lessonNumber}. ${it.subjectName}," +
                                        " начало через ${resources.getQuantityString(R.plurals.minutes, begin).format(begin)}"
                            }
                        }
                        if (it.lessonTimeBegin.parsed < LocalTime.now() &&
                            it.lessonTimeEnd.parsed > LocalTime.now()) {
                             if (it.lessonTimeBegin.parsed < LocalTime.now()) {
                                 itog = "Сейчас идет ${it.subjectName} (${it.lessonNumber}/${day.data.size}), перемена в ${it.lessonTimeEnd}"
                             }
                        }
                    }
                }
                else {
                    val nextday = api.getDay(LocalDate.now().plusDays(1).toString("dd.MM.yyyy"))
                    val nextDayfirstLesson = nextday?.data?.first()
                    if (nextDayfirstLesson != null) {
                            itog = "Завтра ${resources.getQuantityString(R.plurals.lesson,
                                nextday.data.size).format(nextday.data.size)} с ${nextDayfirstLesson.lessonTimeBegin}"
                    }
                }
            }
            else {
                val nextday = api.getDay(LocalDate.now().plusDays(1).toString())
                val nextDayfirstLesson = nextday?.data?.firstOrNull()
                if (nextDayfirstLesson != null) {
                    itog = "Завтра ${resources.getQuantityString(R.plurals.lesson,
                        nextday.data.size).format(nextday.data.size)} с ${nextDayfirstLesson.lessonTimeBegin}"
                }
            }
            withContext(Dispatchers.Main){
                if (_binding != null) {
                    if (itog == "") binding?.statusTextCard?.visibility = View.GONE
                    binding?.statusText?.text = itog
                }
            }
        }
    }
    override fun onDestroy() {
        marksJob?.cancel()
        timeJob?.cancel()
        tipsJob?.cancel()
        dopJob?.cancel()
        super.onDestroy()
    }
}


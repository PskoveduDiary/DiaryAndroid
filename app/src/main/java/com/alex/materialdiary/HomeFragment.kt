package com.alex.materialdiary

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentHomeBinding
import com.alex.materialdiary.sys.ChangeNameBottomSheet
import com.alex.materialdiary.sys.ChooseColorSchemeBottomSheet
import com.alex.materialdiary.sys.DiaryPreferences
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils
import com.alex.materialdiary.sys.adapters.RecycleAdapterAdverts
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
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat


private val String.parsed: LocalTime
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
    val adverts: MutableList<Pair<String, (() -> Unit)?>> = mutableListOf()
    private var marksJob: Job? = null
    private var timeJob: Job? = null
    private var tipsJob: Job? = null
    private var dopJob: Job? = null
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
        binding.MainText.text =
            if (name != null && name != "") "Добро пожаловать, \n${name}!" else "Добро пожаловать!"
        lastMarks.clear()
        adverts.clear()
        binding.lastMarksRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.advicesRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newsRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dopRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.advertsRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.lastMarksRecycleView.adapter = RecycleAdapterLastMarks(this, lastMarks)
        binding.advertsRecycleView.adapter = RecycleAdapterAdverts(this, adverts)
        skeletonAdvices = binding.advicesRecycleView.applySkeleton(R.layout.advice_item)
        skeletonNews = binding.newsRecycleView.applySkeleton(R.layout.news_item)

        skeletonAdvices.showSkeleton()
        skeletonNews.showSkeleton()
        binding.MainText.setOnClickListener {
            ChangeNameBottomSheet(this).show(
                requireActivity().supportFragmentManager,
                "ChangeNameBottomSheet"
            )
        }

        checkLastMarks()
        checkAdvices()
        checkTime()
        checkNews()
        checkDop()
        checkAdverts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).supportActionBar?.show()
        _binding = null
    }

    fun reloadName() {
        val name = api.getUserName()
        binding.MainText.text =
            if (name != null) "Добро пожаловать, \n${name}!" else "Добро пожаловать!"
    }

    fun checkLastMarks() {
        marksJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                if ((api.getCachedPeriods()?.data) == null) return@launch
                val cur_per = MarksTranslator.get_cur_period(api.getPeriods()?.data!!)
                val pattern = DateTimeFormat.forPattern("dd.MM.yyyy")
                val (marks, _) = api.getPeriodMarks(
                    cur_per[0].toString(pattern),
                    cur_per[1].toString(pattern)
                )
                withContext(Dispatchers.Main) {
                    marks?.data?.let { MarksTranslator(it).items }?.let { items ->
                        items.forEach { subj ->
                            MarksTranslator.getSubjectMarksDifferences(
                                requireContext(), subj.name,
                                items
                            ).forEach { mark ->
                                lastMarks += LastMark(mark.value, mark.date, subj.name)
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    if (lastMarks.size == 0) {
                        val utils = ReadWriteJsonFileUtils(context)
                        val readed = utils.readJsonFileData("marks.json")
                        if (readed == null) binding.marks.visibility = View.GONE
                        val listType = object : TypeToken<ArrayList<Item?>?>() {}.type
                        val t = mutableListOf<LastMark>()
                        Gson().fromJson<List<Item>>(readed, listType).forEach { lesson ->
                            lesson.marks.forEach {
                                t.add(LastMark(it.value, it.date, lesson.name))
                            }
                        }
                        t.sortByDescending { LocalDate.parse(it.date, pattern) }
                        lastMarks.addAll(t.subList(0, 5))

                    } else
                        lastMarks.sortByDescending { LocalDate.parse(it.date, pattern) }
                    if (_binding != null) binding.marks.visibility = View.VISIBLE
                    if (_binding != null) binding.lastMarksRecycleView.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkAdvices() {
        tipsJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val tips = api.getAssistantTips()
                withContext(Dispatchers.Main) {
                    if (tips == null || tips.data.isEmpty()) {
                        if (_binding != null) binding.advices.visibility = View.GONE
                    } else {
                        if (_binding != null) binding.advicesRecycleView.adapter =
                            RecycleAdapterAdvices(
                                this@HomeFragment,
                                tips.data.toMutableList()
                            )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                if (_binding != null) binding.advices.visibility = View.GONE
                    }
            }
        }
    }

    fun checkNews() {
        tipsJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val news = api.getEduNews()
                withContext(Dispatchers.Main) {
                    if (news?.news == null || news.news.isEmpty()) {
                        if (_binding != null) binding.news.visibility = View.GONE
                    } else {
                        if (_binding != null) binding.newsRecycleView.adapter = RecycleAdapterNews(
                            this@HomeFragment,
                            news.news.toMutableList()
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkDop() {
        dopJob = CoroutineScope(Dispatchers.IO).launch {
            try {

                val programs = api.getDopPrograms((1..100).random(), 5)
                withContext(Dispatchers.Main) {
                    if (programs.isNullOrEmpty()) {
                        if (_binding != null) binding.dop.visibility = View.GONE
                    } else {
                        binding.dopRecycleView.adapter = RecycleAdapterDopPrograms(
                            this@HomeFragment,
                            programs
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkAdverts() {
        val launch = DiaryPreferences.getInstance().getInt("launches")
        val scheme = DiaryPreferences.getInstance().getInt("color_scheme")
        if (launch > 10) adverts += "Пожалуйста, оцените наше приложение" to ::reviewWindowFlow
        if (scheme == 0) adverts += "Выберите цветовую схему оценок" to ::chooseColorScheme
        /*adverts += "Настройте уведомление" to ::toSetupNotifications
        if (_binding!= null) binding.advertsRecycleView.adapter?.notifyDataSetChanged()*/
    }
    fun toSetupNotifications() = findNavController().navigate(R.id.to_setup_notify)
    fun chooseColorScheme(){
        ChooseColorSchemeBottomSheet().show(
            requireActivity().supportFragmentManager,
            "ChooseColorSchemeBottomSheet"
        )
    }
    fun reviewWindowFlow() {
        DiaryPreferences.getInstance().setInt("launches", -10000)
        if (context == null) return
        val uri = Uri.parse("market://details?id=" + requireContext().packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Не удается запустить Google Play", Toast.LENGTH_LONG).show()
        }
        /*val reviewManager = ReviewManagerFactory.create(requireActivity())
        val requestReviewFlow = reviewManager.requestReviewFlow()

        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                flow.addOnCompleteListener {
                    // Обрабатываем завершение сценария оценки
                    DiaryPreferences.getInstance().setInt("launches", -10000)
                }
            } else {
            }
        }*/
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun checkTime() {
        timeJob = CoroutineScope(Dispatchers.IO).launch {
            var itog = "Завтра нет уроков"
            val day = api.getDay(LocalDate.now().toString("dd.MM.yyyy"))
            val lastLesson = day?.data?.lastOrNull()
            val firstLesson = day?.data?.firstOrNull()
            if (lastLesson != null && firstLesson != null) {
                if (firstLesson.lessonTimeBegin != null && firstLesson.lessonTimeBegin!!.parsed > LocalTime.now()) {
                    itog = "Уроки начнутся в ${firstLesson.lessonTimeBegin}, " +
                            "сегодня ${
                                resources.getQuantityString(R.plurals.lesson, day.data.size)
                                    .format(day.data.size)
                            }"
                } else if (firstLesson.lessonTimeEnd != null && firstLesson.lessonTimeEnd!!.parsed > LocalTime.now()) {
                    day.data.forEach {
                        if (it.lessonTimeBegin != null &&
                            it.lessonTimeEnd != null &&
                            it.lessonTimeBegin!!.parsed > LocalTime.now() &&
                            it.lessonTimeEnd!!.parsed > LocalTime.now()
                        ) {
                            if (itog == "Завтра нет уроков") {
                                val begin =
                                    Period(LocalTime.now(), it.lessonTimeBegin!!.parsed).minutes
                                itog = "Следующий урок - ${it.lessonNumber}. ${it.subjectName}," +
                                        " начало через ${
                                            resources.getQuantityString(
                                                R.plurals.minutes,
                                                begin
                                            ).format(begin)
                                        }"
                            }
                        }
                        if (it.lessonTimeBegin != null &&
                            it.lessonTimeEnd != null &&
                            it.lessonTimeBegin!!.parsed < LocalTime.now() &&
                            it.lessonTimeEnd!!.parsed > LocalTime.now()
                        ) {
                            if (it.lessonTimeBegin!!.parsed < LocalTime.now()) {
                                itog =
                                    "Сейчас идет ${it.subjectName} (${it.lessonNumber}/${day.data.size}), перемена в ${it.lessonTimeEnd}"
                            }
                        }
                    }
                } else {
                    val nextday = api.getDay(LocalDate.now().plusDays(1).toString("dd.MM.yyyy"))
                    val nextDayfirstLesson = nextday?.data?.firstOrNull()
                    if (nextDayfirstLesson != null) {
                        itog = "Завтра ${
                            resources.getQuantityString(
                                R.plurals.lesson,
                                nextday.data.size
                            ).format(nextday.data.size)
                        } с ${nextDayfirstLesson.lessonTimeBegin}"
                    }
                }
            } else {
                val nextday = api.getDay(LocalDate.now().plusDays(1).toString())
                val nextDayfirstLesson = nextday?.data?.firstOrNull()
                if (nextDayfirstLesson != null) {
                    itog = "Завтра ${
                        resources.getQuantityString(
                            R.plurals.lesson,
                            nextday.data.size
                        ).format(nextday.data.size)
                    } с ${nextDayfirstLesson.lessonTimeBegin}"
                }
            }
            withContext(Dispatchers.Main) {
                adverts.add(0, itog to null)
                if (_binding != null) {
                    binding.advertsRecycleView.adapter?.notifyItemInserted(0)
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


package com.alex.materialdiary.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.alex.materialdiary.R
import com.alex.materialdiary.containers.OtherItem
import com.alex.materialdiary.databinding.FragmentOthBinding
import com.alex.materialdiary.sys.adapters.OthListAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OthFragment : Fragment() {
    private lateinit var listView: ListView
    private var _binding: FragmentOthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val others: ArrayList<OtherItem> = ArrayList<OtherItem>()
        others.add(OtherItem("Сервисы", R.drawable.ic_baseline_app_shortcut_24, R.id.to_services))
        others.add(OtherItem("СКУД", R.drawable.ic_baseline_library_books_24, R.id.to_eskud))
        others.add(OtherItem("Расписание занятий",
            R.drawable.ic_baseline_view_quilt_24,
            R.id.to_shedule
        ))
        others.add(OtherItem("Результаты тестирования",
            R.drawable.ic_baseline_library_add_check_24,
            R.id.to_results
        ))
        others.add(OtherItem("Контрольные", R.drawable.ic_baseline_error_outline_24, R.id.to_kr))
        others.add(OtherItem("Домашнее задание",
            R.drawable.ic_baseline_checklist_24,
            R.id.to_check_list
        ))
        others.add(OtherItem("Настройки", R.drawable.ic_baseline_settings_24, R.id.to_settings))
        val adapter = OthListAdapter(requireContext(), others, this)
        val lv = view.findViewById<ListView>(R.id.oth_list_view)
        lv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
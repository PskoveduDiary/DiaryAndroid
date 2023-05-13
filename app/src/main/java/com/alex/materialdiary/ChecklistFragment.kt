package com.alex.materialdiary

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentChecklistBinding
import com.alex.materialdiary.databinding.FragmentKrBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterKrInfo
import com.alex.materialdiary.sys.common.Crypt
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import com.alex.materialdiary.sys.common.models.kr.kr_info
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChecklistFragment : Fragment(){
    private var _binding: FragmentChecklistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.GONE
        binding.checkBox3.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    binding.checkBox3.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
                    binding.checkBox3.setTextColor(resources.getColor(R.color.gray))
                }
                false -> {
                    binding.checkBox3.paintFlags =  Paint.CURSOR_BEFORE
                    binding.checkBox3.setTextColor(resources.getColor(R.color.icons))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

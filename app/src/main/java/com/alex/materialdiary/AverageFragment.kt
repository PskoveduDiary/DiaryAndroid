package com.alex.materialdiary

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentAverageBinding
import java.text.DecimalFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AverageFragment : Fragment() {
    private var _binding: FragmentAverageBinding? = null
    var marks = mutableListOf<Int>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val args: AverageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAverageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.marks != null) {
            marks = args.marks!!.toMutableList()
            calc()
        }
        binding.one.setOnClickListener {
            marks += 1
            calc()
        }
        binding.two.setOnClickListener {
            marks += 2
            calc()
        }
        binding.three.setOnClickListener {
            marks += 3
            calc()
        }
        binding.four.setOnClickListener {
            marks += 4
            calc()
        }
        binding.five.setOnClickListener {
            marks += 5
            calc()
        }
        binding.clearOne.setOnClickListener {
            marks.removeLastOrNull()
            calc()
        }
        binding.clearAll.setOnClickListener {
            marks.clear()
            calc()
        }
    }

    fun calc(){
        if(marks.size == 0){
            binding.average.text = "n/a"
            binding.average.setTextColor(Color.GRAY)
            binding.history.text = ""
            return
        }
        binding.history.text = marks.joinToString(separator = ", ")
        var all = 0
        marks.forEach {
            all += it
        }
        val average = all.toFloat() / marks.size
        binding.average.text =
            DecimalFormat("#0.00").format(average.toDouble())
        if (average < 1.5f) binding.average.setTextColor(resources.getColor(R.color.one))
        else if (average == 1.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 2.5f) binding.average.setTextColor(resources.getColor(R.color.two))
        else if (average == 2.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 3.5f) binding.average.setTextColor(resources.getColor(R.color.three))
        else if (average == 3.5f) binding.average.setTextColor(Color.GRAY)
        else if (average < 4.5f) binding.average.setTextColor(resources.getColor(R.color.four))
        else if (average == 4.5f) binding.average.setTextColor(Color.GRAY)
        else binding.average.setTextColor(resources.getColor(R.color.five))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

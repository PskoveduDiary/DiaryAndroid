package com.alex.materialdiary

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentErrorBinding
import com.alex.materialdiary.databinding.FragmentUserInfoBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ErrorFragment : Fragment() {
    private var _binding: FragmentErrorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: ErrorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorText.text = args.errorText
        binding.toHome.setOnClickListener {
            findNavController().navigate(R.id.to_diary)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

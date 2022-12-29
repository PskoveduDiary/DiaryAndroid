package com.alex.materialdiary

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebViewFeature
import com.alex.materialdiary.databinding.FragmentAddContactBinding
import com.alex.materialdiary.databinding.FragmentChUserNewBinding
import com.alex.materialdiary.databinding.FragmentEskudBinding
import com.alex.materialdiary.sys.MyWebViewClient
import com.alex.materialdiary.sys.adapters.RecycleAdapterSharedUsers
import com.alex.materialdiary.sys.common.CommonAPI

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NewChangeUserFragment : Fragment() {
    private lateinit var webView: WebView
    private var _binding: FragmentChUserNewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChUserNewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.standardAuth.setOnClickListener {
            findNavController().navigate(R.id.to_ch_users)
        }
        binding.scanShared.setOnClickListener {
            findNavController().navigate(R.id.to_scan_shared_qr)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecycleAdapterSharedUsers(this, CommonAPI.getInstance().shared)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

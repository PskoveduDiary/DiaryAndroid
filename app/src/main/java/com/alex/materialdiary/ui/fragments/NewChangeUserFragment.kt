package com.alex.materialdiary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.FragmentChUserNewBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterSharedUsers
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.ShareUser
import com.google.gson.Gson
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import xdroid.toaster.Toaster

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
    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> {
                try {
                    val gson = Gson()
                    val shareUser = gson.fromJson(result.content.rawValue, ShareUser::class.java)
                    PskoveduApi.getInstance(requireContext()).addShared(shareUser)
                    binding.recyclerView.adapter = RecycleAdapterSharedUsers(this, PskoveduApi.getInstance(requireContext(), findNavController()).getShared())
                }
                catch (e: Exception){
                    Toaster.toast("Неверный QR-код, попробуйте другой!")
                }
            }
            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
            else -> {}
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.standardAuth.setOnClickListener {
            findNavController().navigate(R.id.to_ch_users)
        }
        binding.scanShared.setOnClickListener {
//            findNavController().navigate(R.id.to_scan_shared_qr)
            scanQrCodeLauncher.launch(null)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecycleAdapterSharedUsers(this, PskoveduApi.getInstance(requireContext(), findNavController()).getShared())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

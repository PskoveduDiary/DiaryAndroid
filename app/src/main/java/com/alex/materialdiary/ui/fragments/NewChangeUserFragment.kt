package com.alex.materialdiary.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.FragmentChUserNewBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterSharedUsers
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.ShareUser
import com.alex.materialdiary.sys.net.models.get_user.Participant
import com.alex.materialdiary.sys.net.models.get_user.Schools
import com.alex.materialdiary.ui.login.Login
import com.alex.materialdiary.ui.login.LoginActivity
import com.alex.materialdiary.ui.login.LoginResult
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xdroid.toaster.Toaster
import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NewChangeUserFragment : Fragment() {
    private lateinit var webView: WebView
    private var _binding: FragmentChUserNewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val users: MutableList<ShareUser> = mutableListOf()
    var relogin = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChUserNewBinding.inflate(inflater, container, false)
//        users.clear()
//        users.addAll(PskoveduApi.getInstance(requireContext(), findNavController()).getShared())
        return binding.root

    }

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> {
                try {
                    val gson = Gson()
                    val shareUser = gson.fromJson(result.content.rawValue, ShareUser::class.java)
                    PskoveduApi.getInstance(requireContext()).addShared(shareUser)
                    users.clear()
                    users.addAll(
                        PskoveduApi.getInstance(requireContext(), findNavController()).getShared()
                    )
                    getUserInfo()
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toaster.toast("Неверный QR-код, попробуйте другой!")
                }
            }

            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> MainActivity.showSnack("Разрешите использование камеры")
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
            else -> {}
        }
    }
    val loginLauncher = registerForActivityResult(Login()) { result ->
        when (result) {
            is LoginResult.LoginSuccess -> {
                try {
                    val schools = result.data?.schools
                    if (schools != null) {
                        users.clear()
                        users.addAll(transform(schools))
                        users.addAll(
                            PskoveduApi.getInstance(requireContext(), findNavController())
                                .getShared()
                        )
                        getUserInfo()
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Toaster.toast("Неверный QR-код, попробуйте другой!")
                }
            }

            LoginResult.LoginUserCanceled -> "User canceled"
            is LoginResult.LoginError -> {
                MainActivity.showSnack("Произошла ошибка при сканировании")
                FirebaseCrashlytics.getInstance().recordException(result.exception)
            }

            else -> {}
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserInfo()
        Handler(Looper.getMainLooper()).postDelayed(100) {
            if (_binding != null) binding.addFab.show()
        }
        binding.addSiteFab.setOnClickListener {
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
            CookieManager.getInstance().removeAllCookies(null)
            loginLauncher.launch(null)
        }
        binding.addQrFab.setOnClickListener {
//            findNavController().navigate(R.id.to_scan_shared_qr)
            scanQrCodeLauncher.launch(null)
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
        }
        binding.root.setOnTouchListener { v, event ->
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
            v.performClick()
            v?.onTouchEvent(event) ?: true
        }
        binding.addFab.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.addQrFab.show()
                    binding.addSiteFab.show()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecycleAdapterSharedUsers(
            this,
            users
        )
    }

    fun transform(schools: List<Schools>): List<ShareUser> {
        val list: MutableList<ShareUser> = ArrayList()
        for ((roles, school, _, _, participant, _, userParticipants) in schools) {
            if (roles.contains("participant")) {
                if (participant == null || school == null || school.shortName == null
                    || participant.grade == null
                ) {
                    FirebaseCrashlytics.getInstance()
                        .recordException(Exception("Null Pointer Exception when transform users"))
                    continue
                }
                list.add(
                    ShareUser(
                        name = participant.name + " " + participant.surname,
                        guid = participant.sysGuid!!,
                        school = school.shortName!!,
                        classname = participant.grade!!.name!!,
                        snils = null,
                        grade = participant.grade!!.sysGuid
                    )
                )
            } else {
                if (userParticipants == null || school == null) continue
                for (participant in userParticipants) {
                    list.add(
                        ShareUser(
                            name = participant.name + " " + participant.surname,
                            guid = participant.sysGuid!!,
                            school = school.shortName!!,
                            classname = participant.grade!!.name!!,
                            snils = null,
                            grade = participant.grade!!.sysGuid
                        )
                    )
                }
            }
        }
        return list
    }

    fun getUserInfo() {
        val userinfo =
            PskoveduApi.getInstance(requireContext(), findNavController())
                .getSavedUsers()
        println(userinfo)
        if (_binding == null) return
        users.clear()
        if (userinfo != null) {
            val transformed = transform(userinfo.schools)
            users.addAll(transformed)
        }
        users.addAll(
            PskoveduApi.getInstance(requireContext(), findNavController())
                .getShared()
        )
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

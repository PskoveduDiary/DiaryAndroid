package com.alex.materialdiary.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.alex.materialdiary.sys.net.models.get_user.Participant
import com.alex.materialdiary.sys.net.models.get_user.Schools
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChUserNewBinding.inflate(inflater, container, false)
        users.clear()
        users.addAll(PskoveduApi.getInstance(requireContext(), findNavController()).getShared())
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
                    users.addAll(PskoveduApi.getInstance(requireContext(), findNavController()).getShared())
                    getUserInfo()
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toaster.toast("Неверный QR-код, попробуйте другой!")
                }
            }

            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
            else -> {}
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserInfo()
        binding.addSiteFab.setOnClickListener {
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
            CoroutineScope(Dispatchers.IO).launch {
                PskoveduApi.getInstance(requireContext(), findNavController()).getUserInfo()
            }
        }
        binding.addQrFab.setOnClickListener {
//            findNavController().navigate(R.id.to_scan_shared_qr)
            scanQrCodeLauncher.launch(null)
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
        }
        binding.root.setOnTouchListener { v, _ ->
            binding.addQrFab.hide()
            binding.addSiteFab.hide()
            v.performClick()
            true
        }
        binding.addFab.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.addQrFab.show()
                        binding.addSiteFab.show()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecycleAdapterSharedUsers(
            this,
            users
        )
    }

    internal class MyTimerTask(val fragment: NewChangeUserFragment) : Runnable {
        override fun run() {
            if (android.webkit.CookieManager.getInstance().getCookie("one.pskovedu.ru") == null) {
                val scheduler = Executors.newSingleThreadScheduledExecutor()
                scheduler.schedule(MyTimerTask(fragment), 2, TimeUnit.SECONDS)
            } else {
                fragment.getUserInfo()
            }
        }
    }

    fun transform(schools: List<Schools>): List<ShareUser> {
        val list: MutableList<ShareUser> = ArrayList()
//        if (schools == null) return null
        println(list)
        for ((roles, school, _, _, _, participant, _, userParticipants) in schools) {
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
                        name = participant.name + "_" + participant.surname,
                        guid = participant.sysGuid!!,
                        school = school.shortName!!,
                        classname = participant.grade!!.name!!,
                        snils = null,
                        grade = participant.grade!!.sysGuid
                    )
                )
//                schoolsForUsers.add(school)
            } else {
//                list.addAll(userParticipants!!)
                if (userParticipants == null  || school == null) continue
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
//                    schoolsForUsers.add(school)
                }
            }
        }
        return list
    }

    fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val userinfo =
                PskoveduApi.getInstance(requireContext(), findNavController())
                    .getUserInfo(silent = true)
            println(userinfo)
            withContext(Dispatchers.Main) {
                if (userinfo != null) {
                    activity?.runOnUiThread(object : Runnable {
                        override fun run() {
                            if (_binding == null) return
                            val transformed = transform(userinfo.schools)
                            users.addAll(0, transformed)
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                        }
                    })
                } else {
                    val scheduler = Executors.newSingleThreadScheduledExecutor()
                    scheduler.schedule(MyTimerTask(this@NewChangeUserFragment), 1, TimeUnit.SECONDS)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

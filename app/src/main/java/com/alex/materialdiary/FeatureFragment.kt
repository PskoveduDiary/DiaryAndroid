package com.alex.materialdiary

import android.R
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentRequestFeatureBinding
import com.google.android.play.core.ktx.status
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jiang.android.pbutton.CProgressButton
import xdroid.toaster.Toaster
import xdroid.toaster.Toaster.toast


class FeatureFragment : Fragment() {

    private var _binding: FragmentRequestFeatureBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val args: FeatureFragmentArgs by navArgs()
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentRequestFeatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CProgressButton.initStatusString(
            arrayOf(
                "Загрузить",
                "Приостановить",
                "Применено",
                "Ошибка",
                "Удалить",
                "Распаковка",
                "Скачано, начинаем распаковку",
                "Ожидание"
            )
        )
        val splitInstallManager = SplitInstallManagerFactory.create(requireContext())
        val manager = requireActivity().packageManager
        binding.descriptionDelivery.text = args.description
        val progressButton = binding.btn
        progressButton.normal(0)
        if (splitInstallManager.installedModules.contains(args.featureName)) {
            progressButton.normal(2)
            val enabled = manager.getComponentEnabledSetting(ComponentName(
                requireActivity(),
                "com.alex.journals.JournalsActivity"
            ))
            if (enabled != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) try {
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "com.alex.journals.JournalsActivity"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
            catch (e: Exception) {true}
        }
        progressButton.setOnClickListener {
            if (splitInstallManager.installedModules.contains(args.featureName)) {

                val enabled = manager.getComponentEnabledSetting(ComponentName(
                    requireActivity(),
                    "com.alex.journals.JournalsActivity"
                ))
                if (enabled == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) return@setOnClickListener
            }
            val request =
                SplitInstallRequest
                    .newBuilder()
                    // You can download multiple on demand modules per
                    // request by invoking the following method for each
                    // module you want to install.
                    .addModule(args.featureName)
                    .build()
            var mySessionId = 0
            val listener = SplitInstallStateUpdatedListener { state ->
                if (state.sessionId() == mySessionId) {
                    // Read the status of the request to handle the state update.
                    when (state.status){
                        SplitInstallSessionStatus.INSTALLED -> {
                            try {
                                manager.setComponentEnabledSetting(
                                    ComponentName(
                                        requireActivity(),
                                        "com.alex.journals.JournalsActivity"
                                    ),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP
                                )
                                progressButton.normal(2, false)
                                requireActivity().startService(Intent()
                                    .setClassName("com.alex.materialdiary", "com.alex.journals.JournalsActivity"))
                            }
                            catch (e: Exception) {
                                progressButton.normal(3, false)
                                progressButton.error = "Ошибка!"
                            }
                        }
                        SplitInstallSessionStatus.INSTALLING -> {
                            progressButton.normal(5)
                        }
                        SplitInstallSessionStatus.FAILED -> {
                            progressButton.normal(3, false)
                            progressButton.error = "Ошибка " + state.errorCode()
                        }
                        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            splitInstallManager.startConfirmationDialogForResult(
                            state, requireActivity(), 1)
                        }
                        SplitInstallSessionStatus.DOWNLOADED -> {
                            progressButton.download(100)
                            progressButton.normal(6)
                        }
                        SplitInstallSessionStatus.DOWNLOADING -> {
                            progressButton.startDownLoad()
                            progressButton.download(((state.bytesDownloaded() / state.totalBytesToDownload()) * 100).toInt())
                        }
                        SplitInstallSessionStatus.PENDING -> {
                            progressButton.normal(7)
                        }
                    }
                }
            }
            splitInstallManager.registerListener(listener)
            splitInstallManager
                .startInstall(request)
                .addOnSuccessListener { sessionId -> mySessionId = sessionId }
                .addOnFailureListener { exception ->
                    progressButton.normal(3, false)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
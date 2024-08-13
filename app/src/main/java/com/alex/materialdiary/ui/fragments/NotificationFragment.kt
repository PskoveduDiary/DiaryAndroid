package com.alex.materialdiary.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentNotificationBinding
import xdroid.toaster.Toaster.toast
import android.Manifest
import com.alex.materialdiary.R

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

//    private val args: FeatureFragmentArgs by navArgs()
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val optimization = isIgnoringBatteryOptimizations(requireContext())
        val notifications = checkNotificationsPermission(requireContext())
        if (!optimization) {
            binding.buttonDisableOpt.setOnClickListener {
                val name = resources.getString(R.string.app_name)
                toast("Оптимизация батареи -> Все приложение -> $name -> Не экономить")
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }
        } else {
            binding.cardDisableOpt.visibility = View.GONE
            binding.textDisableOpt.visibility = View.GONE
        }
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    binding.cardAllowNotifications.visibility = View.GONE
                    binding.textAllowNotifications.visibility = View.GONE
                } else {
                    binding.cardAllowNotifications.visibility = View.VISIBLE
                    binding.textAllowNotifications.visibility = View.VISIBLE
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    binding.cardAllowNotifications.visibility = View.GONE
                    binding.textAllowNotifications.visibility = View.GONE
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    binding.cardAllowNotifications.visibility = View.VISIBLE
                    binding.textAllowNotifications.visibility = View.VISIBLE
                }
                else -> {
                    binding.buttonAllowNotifications.setOnClickListener {
                        requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

        }
    }

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val pwrm =
            context.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = context.applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return pwrm.isIgnoringBatteryOptimizations(name)
        }
        return true
    }

    fun checkNotificationsPermission(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
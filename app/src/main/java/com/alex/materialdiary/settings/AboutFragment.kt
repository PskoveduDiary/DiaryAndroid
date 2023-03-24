package com.alex.materialdiary.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PerformanceHintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.alex.materialdiary.BuildConfig
import com.alex.materialdiary.FeatureFragmentDirections
import com.alex.materialdiary.R
import com.alex.materialdiary.containers.Storage
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils
import com.alex.materialdiary.sys.common.CommonAPI
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.ktx.status
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import xdroid.toaster.Toaster.toast
import java.io.IOException


class AboutFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about_preferences, rootKey)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val clipboardManager = parentFragment?.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager;
        val version =
            preferenceManager.findPreference<Preference>("version") as Preference
        version.summary = BuildConfig.VERSION_NAME + "(${BuildConfig.VERSION_CODE})"
        val guid = preferenceManager.findPreference<Preference>("guid") as Preference
        guid.summary = CommonAPI.getInstance().uuid
        guid.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", guid.summary);
            toast("Скопировано!")
            clipboardManager.setPrimaryClip(clipData);
            true
        }
        val sid = preferenceManager.findPreference<Preference>("sid") as Preference
        sid.summary = CommonAPI.getInstance().sid
        sid.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", sid.summary);
            clipboardManager.setPrimaryClip(clipData);
            toast("Скопировано!")
            true
        }
        val pda = preferenceManager.findPreference<Preference>("pdakey") as Preference
        pda.summary = CommonAPI.getInstance().pdaKey
        pda.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", pda.summary);
            toast("Скопировано!")
            clipboardManager.setPrimaryClip(clipData);
            true
        }
        val marks = preferenceManager.findPreference<Preference>("marks") as Preference
        marks.setOnPreferenceClickListener {
            CommonAPI.getInstance().updateMarksCache()
            toast("Успешно, перезапустите приложение")
            true
        }
        val marks_clear = preferenceManager.findPreference<Preference>("marks_clear") as Preference
        marks_clear.setOnPreferenceClickListener {
            var str = ReadWriteJsonFileUtils(requireContext()).readJsonFileData("marks.json")
            str = str.replace(".2023", ".2020")
            ReadWriteJsonFileUtils(requireContext()).createJsonFileData("marks.json", str)
            toast("Успешно, перезапустите приложение")
            true
        }
        val teacher = preferenceManager.findPreference<Preference>("teach_feature") as Preference
        teacher.setOnPreferenceClickListener {
            val action = FeatureFragmentDirections.toFeatureInstall("teacher_features", "Загрузите дополнительный модуль для управления журналами!")
            findNavController().navigate(action)

            true
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
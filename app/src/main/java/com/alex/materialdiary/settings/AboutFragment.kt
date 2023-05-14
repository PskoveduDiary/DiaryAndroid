package com.alex.materialdiary.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alex.materialdiary.BuildConfig
import com.alex.materialdiary.FeatureFragmentDirections
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils
import com.alex.materialdiary.sys.net.PskoveduApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xdroid.toaster.Toaster.toast


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
        guid.summary = PskoveduApi.getInstance(requireContext()).guid
        guid.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", guid.summary);
            toast("Скопировано!")
            clipboardManager.setPrimaryClip(clipData);
            true
        }
        val sid = preferenceManager.findPreference<Preference>("sid") as Preference
        sid.summary = PskoveduApi.getInstance().sid
        sid.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", sid.summary);
            clipboardManager.setPrimaryClip(clipData);
            toast("Скопировано!")
            true
        }
        val pda = preferenceManager.findPreference<Preference>("pdakey") as Preference
        pda.summary = PskoveduApi.getInstance().pdaKey
        pda.setOnPreferenceClickListener {
            val clipData = ClipData.newPlainText("text", pda.summary);
            toast("Скопировано!")
            clipboardManager.setPrimaryClip(clipData);
            true
        }
        val marks = preferenceManager.findPreference<Preference>("marks") as Preference
        marks.setOnPreferenceClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                PskoveduApi.getInstance().updateMarksCache()
            }
            ReadWriteJsonFileUtils.memoryCache.clearRegion("ru")
            toast("Успешно, перезапустите приложение")
            true
        }
        val marks_clear = preferenceManager.findPreference<Preference>("marks_clear") as Preference
        marks_clear.setOnPreferenceClickListener {
            var str = ReadWriteJsonFileUtils(requireContext()).readJsonFileData("marks.json")
            if (str == null){
                toast("Ошибка, в кэше ничего нет")
                return@setOnPreferenceClickListener false
            }
            str = str.replace(".2023", ".2020")
            ReadWriteJsonFileUtils(requireContext()).createJsonFileData("marks.json", str)
            toast("Успешно, перезапустите приложение")
            ReadWriteJsonFileUtils.memoryCache.clearRegion("ru")
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
package com.alex.materialdiary.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.alex.materialdiary.R
import com.alex.materialdiary.containers.Storage
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import xdroid.toaster.Toaster.toast
import java.io.IOException


class SettingsFragment : PreferenceFragmentCompat() {
    lateinit var okHttp: okhttp3.OkHttpClient
    lateinit var mainHandler: Handler
    var logging = HttpLoggingInterceptor()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        mainHandler  = Handler(requireContext().mainLooper)
        okHttp = okhttp3.OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val result = super.onCreateView(inflater, container, savedInstanceState)
        if (result != null) {
            val lv = result.findViewById<View>(android.R.id.list)
            if (lv is ListView) {
                (lv as ListView).setOnItemLongClickListener {parent, view, pos, id ->
                    val listAdapter: ListAdapter = (parent as ListView).adapter
                    val obj: Any = listAdapter.getItem(pos)

                    println(obj.hashCode())
                    if (obj is Preference) {
                        val p = obj
                        println(p.key)
                        if (p.key.equals("news")) {
                            findNavController().navigate(R.id.to_about)
                            return@setOnItemLongClickListener true
                        }
                    } /*if*/

                    return@setOnItemLongClickListener false
                }
            }
            } else {
                //The view created is not a list view!
            }

        val api = GoogleApiAvailability.getInstance()
        val resultCode =
            api.isGooglePlayServicesAvailable(requireContext())
        val news =
            preferenceManager.findPreference<Preference>("news") as Preference
        news.setOnPreferenceClickListener {
                try {
                    val telegram =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/pskovedu_diary"))
                    startActivity(telegram)
                } catch (e: Exception) {
                    toast("Telegram не установлен")
                }
                true
            }

        val dev =
            preferenceManager.findPreference<Preference>("dev") as Preference
        dev.setOnPreferenceClickListener {
            findNavController().navigate(R.id.to_about)
            true
        }
        val about =
            preferenceManager.findPreference<Preference>("about") as Preference
        about.setOnPreferenceClickListener {
                findNavController().navigate(R.id.to_about)
                true
            }
        val kr_en =
            preferenceManager.findPreference<SwitchPreferenceCompat>("kr") as SwitchPreferenceCompat
        val marks_en =
            preferenceManager.findPreference<SwitchPreferenceCompat>("marks") as SwitchPreferenceCompat
        if ( Storage.FIREBASE_TOKEN == null){
            kr_en.isEnabled = false
            kr_en.summary = "Временно недоступно!"
            marks_en.isEnabled = false
            marks_en.summary = "Временно недоступно!"
        }
        if (resultCode != ConnectionResult.SUCCESS) {
            kr_en.isEnabled = false
            kr_en.summary = "Недоступно на вашем устройстве!"
            marks_en.isEnabled = false
            marks_en.summary = "Недоступно на вашем устройстве!"
        }
        kr_en.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
                true -> {
                    toast("Отправляем запрос на уведомления...")
                    val request = Request.Builder()
                        .url("https://pskovedu.ml/api/notify/kr?token=" + Storage.FIREBASE_TOKEN)
                        .get()
                        .build()
                    okHttp.newCall(request).enqueue(object: okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            toast("Произошла ошибка")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val runnable = Runnable {
                                if (response.code() == 400){
                                    toast("Не удалось подписаться на уведомления")
                                    kr_en.isChecked = false
                                }
                                else if (response.code() == 200){
                                    toast("Успешно!")
                                }
                                else {
                                    toast("Неизвестная ошибка!")
                                    kr_en.isChecked = true
                                }
                            }
                            mainHandler.post(runnable)
                        }
                    })
                }
                false -> {
                    toast("Отправляем запрос на уведомления...")
                    val request = Request.Builder()
                    .url("https://pskovedu.ml/api/notify/kr?token=" + Storage.FIREBASE_TOKEN)
                    .delete()
                    .build()
                    okHttp.newCall(request).enqueue(object: okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            toast("Произошла ошибка")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val runnable = Runnable {
                                if (response.code() == 400) {
                                    toast("Не удалось отписаться на уведомления")
                                    kr_en.isChecked = true
                                } else if (response.code() == 200) {
                                    toast("Успешно!")
                                } else {
                                    toast("Неизвестная ошибка!")
                                    kr_en.isChecked = true
                                }
                            }
                            mainHandler.post(runnable)
                        }
                    })
                }
            }
            true
        }
        marks_en.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
                true -> {
                    toast("Отправляем запрос на уведомления...")
                    val request = Request.Builder()
                        .url("https://pskovedu.ml/api/notify/marks?token=" + Storage.FIREBASE_TOKEN)
                        .get()
                        .build()
                    okHttp.newCall(request).enqueue(object: okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            toast("Произошла ошибка")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val runnable = Runnable {
                                if (response.code() == 400){
                                    toast("Не удалось подписаться на уведомления")
                                    marks_en.isChecked = false
                                }
                                else if (response.code() == 200){
                                    toast("Успешно!")
                                }
                                else {
                                    toast("Неизвестная ошибка!")
                                    marks_en.isChecked = true
                                }
                            }
                            mainHandler.post(runnable)
                        }
                    })
                }
                false -> {
                    toast("Отправляем запрос на уведомления...")
                    val request = Request.Builder()
                    .url("https://pskovedu.ml/api/notify/marks?token=" + Storage.FIREBASE_TOKEN)
                    .delete()
                    .build()
                    okHttp.newCall(request).enqueue(object: okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            toast("Произошла ошибка")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val runnable = Runnable {
                                if (response.code() == 400) {
                                    toast("Не удалось отписаться на уведомления")
                                    marks_en.isChecked = true
                                } else if (response.code() == 200) {
                                    toast("Успешно!")
                                } else {
                                    toast("Неизвестная ошибка!")
                                    marks_en.isChecked = true
                                }
                            }
                            mainHandler.post(runnable)
                        }
                    })
                }
            }
            true
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
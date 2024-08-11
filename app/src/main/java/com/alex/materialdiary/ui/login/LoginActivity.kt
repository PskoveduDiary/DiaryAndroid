package com.alex.materialdiary.ui.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.ActivityLoginBinding
import com.alex.materialdiary.sys.MyWebViewClient
import com.alex.materialdiary.sys.net.PskoveduApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var cookies: String
    private lateinit var webView: WebView
    private lateinit var binding: ActivityLoginBinding
//    lateinit var updateHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        webView = findViewById(R.id.loginView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            webView.settings.setAlgorithmicDarkeningAllowed(true)
        } else if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    WebSettingsCompat.setForceDark(
                        webView.settings, WebSettingsCompat.FORCE_DARK_ON
                    )
                }

                Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    WebSettingsCompat.setForceDark(
                        webView.settings, WebSettingsCompat.FORCE_DARK_OFF
                    )
                }
            }

        }
        webView.webViewClient = object : MyWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("web", "Loaded: $url")
                if (url == "https://passport.pskovedu.ru/?returnTo=https://one.pskovedu.ru") {
                    webView.loadUrl("javascript:\$(\".container\").css('background-image' , 'none')")
                    webView.loadUrl("javascript:\$(\".panel-footer\").hide()")
                    webView.loadUrl("javascript:\$(\".navbar\").hide()")
                }
                super.onPageFinished(view, url)
            }

            override fun handleUri(
                view: WebView,
                url: String
            ): Boolean {
                Log.d("web", "Goto: $url")
                val cookies: String? = CookieManager.getInstance().getCookie("one.pskovedu.ru")
                if (cookies?.contains("X1_SSO=") == true) {
                    Log.d("cookies", cookies)
                    onCookies(cookies)
                }
                return false
            }
        }
        webView.getSettings().javaScriptEnabled = true;
        webView.getSettings().domStorageEnabled = true
        webView.loadUrl("https://passport.pskovedu.ru/?returnTo=https://one.pskovedu.ru")
//        updateHandler = Handler(Looper.getMainLooper())

    }

    private fun onCookies(cookies: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userinfo =
                PskoveduApi.getInstance(this@LoginActivity)
                    .login(cookies)
            Log.d("UserInfo", userinfo.toString())
            if (userinfo != null) {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(EXTRA_RESULT_INFO, userinfo)
                    }
                )
                finish()
            }
            else {
                setResult(
                    RESULT_ERROR,
                    Intent().apply {
                        putExtra(EXTRA_RESULT_EXCEPTION, Exception("User info is null"))
                    }
                )
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        updateHandler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) webView.goBack()
        else {
            setResult(
                RESULT_CANCELED
            )
            finish()
        }
    }

    companion object {
        const val EXTRA_RESULT_INFO = "login-info"
        const val EXTRA_RESULT_VALUE = "login-value"
        const val EXTRA_RESULT_TYPE = "login-type"
        const val EXTRA_RESULT_PARCELABLE = "login-parcelable"
        const val EXTRA_RESULT_EXCEPTION = "login-exception"
        const val RESULT_MISSING_PERMISSION = RESULT_FIRST_USER + 1
        const val RESULT_ERROR = RESULT_FIRST_USER + 2
    }
}

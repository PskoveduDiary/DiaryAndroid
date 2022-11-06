package com.alex.materialdiary.ui.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.R
import com.alex.materialdiary.databinding.ActivityLoginBinding
import com.alex.materialdiary.sys.MyWebViewClient
import com.alex.materialdiary.sys.messages.MessageService

class LoginActivity : AppCompatActivity() {
    lateinit var cookies : String
    private lateinit var webView: WebView
    private lateinit var binding: ActivityLoginBinding
    lateinit var updateHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //$(".navbar").hide()
        //$(".panel-footer").hide()
        //$(".container").css('background-image' , 'none')
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        webView = findViewById(R.id.loginView)
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    WebSettingsCompat.setForceDark(webView.settings,
                        WebSettingsCompat.FORCE_DARK_ON
                    )
                }
                Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    WebSettingsCompat.setForceDark(webView.settings,
                        WebSettingsCompat.FORCE_DARK_OFF
                    )
                }
                else -> {
                    //
                }
            }
        }
        webView.webViewClient = object : MyWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url == "https://passport.pskovedu.ru/?returnTo=https://one.pskovedu.ru") {
                    webView.loadUrl("javascript:\$(\".container\").css('background-image' , 'none')")
                    webView.loadUrl("javascript:\$(\".panel-footer\").hide()")
                    webView.loadUrl("javascript:\$(\".navbar\").hide()")
                    Log.d("web", url)
                }
                if (url != null) {
                    Log.d("web", url)
                }
                super.onPageFinished(view, url)
            }

            var prev = ""
            override fun handleUri(
                view: WebView,
                url: String
            ): Boolean {
                Log.d("web", url)
                when (url){
                    "https://one.pskovedu.ru/" -> {
                        val data = Intent()
                        updateHandler.removeCallbacksAndMessages(null)
                        val gcookies: String = CookieManager.getInstance().getCookie(url)
                        CookieManager.getInstance().setCookie(url, gcookies)
                        cookies = gcookies
                        Log.d("web", cookies)
                        Log.d("web", "$prev : $url")
                        setResult(Activity.RESULT_OK, data)
                        if(prev.equals("http://one.pskovedu.ru/")) {
                            Log.d("web", "finish")
                            this@LoginActivity.finish()
                            MessageService.restartInstance()
                        }
                        val d = Runnable {
                            this@LoginActivity.finish()
                            MessageService.restartInstance()
                        }
                        updateHandler.postDelayed(d, 5000)
                        prev = url
                        return false
                    }
                    else -> {
                        prev = url
                        return false
                    }
                }
            }
        }
        webView.getSettings().javaScriptEnabled = true;
        webView.getSettings().domStorageEnabled = true
        webView.loadUrl("https://passport.pskovedu.ru/?returnTo=https://one.pskovedu.ru")
        updateHandler = Handler(Looper.getMainLooper())

    }

    override fun onDestroy() {
        super.onDestroy()
        updateHandler.removeCallbacksAndMessages(null)
    }
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
    }
}

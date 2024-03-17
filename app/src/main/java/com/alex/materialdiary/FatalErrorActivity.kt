package com.alex.materialdiary

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.alex.materialdiary.databinding.ActivityMainBinding
import com.alex.materialdiary.databinding.FragmentFatalErrorBinding
import xdroid.toaster.Toaster
import java.util.zip.Inflater

class FatalErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentFatalErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.clearData.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.clearData.setOnClickListener {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            Toaster.toast("Хранилище -> Очистить хранилище")
        }
    }
}
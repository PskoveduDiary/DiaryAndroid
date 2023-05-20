package com.alex.materialdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alex.materialdiary.databinding.ActivityMainBinding
import com.alex.materialdiary.databinding.FragmentFatalErrorBinding
import java.util.zip.Inflater

class FatalErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentFatalErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
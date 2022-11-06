package com.alex.materialdiary

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alex.materialdiary.databinding.ActivityMainBinding
import com.alex.materialdiary.sys.messages.NotificationService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File
import java.time.LocalDate
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt


open class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Сообщения"
            val descriptionText = "Сообщения от других пользователей"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("msg", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val selected = intent.getIntExtra("selected", R.id.action_1)
        if (selected == R.id.action_3) findNavController(R.id.nav_host_fragment_content_main)
            .navigate(R.id.to_marks)
        if (selected == R.id.action_4) findNavController(R.id.nav_host_fragment_content_main)
            .navigate(R.id.to_other)

        val p: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        if (!p.contains("uuid")) {
            navController.navigate(R.id.to_ch_users)
        }
        appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.DiaryFragment,
            R.id.ContactsFragment,
            R.id.MarksFragment,
            R.id.OtherFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        val intentt = Intent(this, NotificationService::class.java)
        startService(intentt)

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }*/

        bottomNav.selectedItemId = selected
        bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
            R.id.action_1 -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_diary)
                return@OnItemSelectedListener true
            }
            R.id.action_2 -> {
                //val i = Intent(this, MessageActivity::class.java)
                //startActivity(i,
                //    ActivityOptions.makeSceneTransitionAnimation(
                //        this,
                //        bottomNav,
                //        "bottomnav"
                //    ).toBundle()
                //)
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_contacts)
                return@OnItemSelectedListener true
            }
            R.id.action_3 -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_marks)
                return@OnItemSelectedListener true
            }
            R.id.action_4 -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_other)
                return@OnItemSelectedListener true
            }
            else -> {return@OnItemSelectedListener false}
        } })
        bottomNav.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.action_1 -> {
                }
                R.id.action_2 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_contacts)
                }
                R.id.action_3 -> {
                }
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val cache = Random.nextInt(0, 5)
        if (cache == 2) trimCache(baseContext)
    }
    open fun trimCache(context: Context) {
        try {
            val dir: File? = context.cacheDir
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    open fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory()) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        // The directory is now empty so delete it
        if (dir != null) {
            return dir.delete()
        }
        else{
            return false
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.subtitle = ""
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*val id = findNavController(R.id.nav_host_fragment_content_main).currentDestination?.id
        if (id == R.id.DiaryFragment)*/ menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        if (item.itemId == android.R.id.home) findNavController(R.id.nav_host_fragment_content_main).navigateUp()
        if (item.itemId == R.id.action_settings) findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_ch_users)
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    open fun to_login() {

            /*val sharedPref = this?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val s : String = ""
        sharedPref.getString("logged", s)
        Log.d("login", s)*/
        }
}

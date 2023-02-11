package com.alex.materialdiary

//import com.alex.materialdiary.sys.common.Crypt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alex.materialdiary.containers.Storage
import com.alex.materialdiary.databinding.ActivityMainBinding
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.Crypt
import com.alex.materialdiary.sys.common.cryptor.SuperCrypt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

open class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        SuperCrypt.setContext(baseContext)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        //Crypt.generateKeyFromString("aYXfLjOMB9V5az9Ce8l+7A==");
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        //val pg: PackageInfo =
            //packageManager
              //  .getPackageInfo("ru.integrics.mobileschool", PackageManager.GET_SIGNATURES)
        //Log.e("jlkjk", pg.signatures[0].toByteArray().toHex2())

        //pg.signatures[0] = new Signature("");
        //KeyHelper.get(pg, "SHA1")

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    appUpdateInfo.availableVersionCode())
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Напоминания о контрольных"
            val descriptionText = "Напомню подготовиться к контрольной"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("kr", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }/*
        KRWorkManager().start(this)
        Alarmer().start_kr(applicationContext)*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("SDJDSJJ", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Storage.FIREBASE_TOKEN = token
                // Log and toast
                Log.d("SDJDSJJ", token)
                /*Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()*/
            })
        }
        catch (e: Exception){""}
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val navigation = intent.getStringExtra("navigate")
        if (navigation != null){
            when(navigation){
                "kr" -> navController.navigate(R.id.to_kr)
            }
        }

        //if (!p.contains("uuid")) {
        //    navController.navigate(R.id.to_ch_users)
        //}
        appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.DiaryFragment,
                /*R.id.ContactsFragment,*/
                R.id.MarksFragment,
                R.id.OtherFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        //val intentt = Intent(this, NotificationService::class.java)
        //startService(intentt)
        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }*/
        if (CommonAPI.getInstance(this, navController).uuid == "") {
            binding.contentMain.bottomNavigation.visibility = View.GONE
        }
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
                    //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_contacts)
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
                else -> {
                    return@OnItemSelectedListener false
                }
            }
        })
        bottomNav.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.action_1 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_diary)
                }
                R.id.action_2 -> {
                //    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_contacts)
                }
                R.id.action_3 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_marks)
                }
                R.id.action_4 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_other)
                }
                else -> {}
            }
        }
    }

    fun checkNav() {
        if (CommonAPI.getInstance(
                this,
                findNavController(R.id.nav_host_fragment_content_main)
            ).uuid == ""
        ) {
            binding.contentMain.bottomNavigation.visibility = View.GONE
        } else binding.contentMain.bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
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
        } else {
            return false
        }
    }

    override fun onBackPressed() {
        if (CommonAPI.getInstance(
                this,
                findNavController(R.id.nav_host_fragment_content_main)
            ).uuid == ""
            && findNavController(R.id.nav_host_fragment_content_main).currentDestination?.id == R.id.NewChangeUserFragment
        )
            return;
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

        if (item.itemId == android.R.id.home) {
            if (CommonAPI.getInstance(
                    this,
                    findNavController(R.id.nav_host_fragment_content_main)
                ).uuid == ""
                && findNavController(R.id.nav_host_fragment_content_main).currentDestination?.id == R.id.NewChangeUserFragment
            )
                return false;
            findNavController(R.id.nav_host_fragment_content_main).navigateUp()
        }
        if (item.itemId == R.id.action_settings) findNavController(R.id.nav_host_fragment_content_main).navigate(
            R.id.to_new_ch_users
        )
        if (item.itemId == R.id.action_marks) findNavController(R.id.nav_host_fragment_content_main).navigate(
            R.id.to_average
        )
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

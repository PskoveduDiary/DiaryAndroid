package com.alex.materialdiary

//import com.alex.materialdiary.sys.common.Crypt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.*
import com.alex.materialdiary.containers.Storage
import com.alex.materialdiary.databinding.ActivityMainBinding
import com.alex.materialdiary.sys.DiaryPreferences
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.utils.KRWorkManager
import com.alex.materialdiary.utils.MarksTranslator
import com.alex.materialdiary.workers.KRNotifyWorker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import org.joda.time.format.DateTimeFormat
import java.io.File
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit


open class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView
    private var marksJob: Job? = null

    companion object {
        var root: View? = null
        fun showSnack(text: String) {
            root?.let {
                Snackbar.make(it, text, Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottom_navigation).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        DiaryPreferences(this)
        checkGPUpdates()
        checkNotificationsPermissionsAndRegisterChannels()
        //KRWorkManager().start(this)
        countLaunches()
        /*
        KRWorkManager().start(this)
        Alarmer().start_kr(applicationContext)*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        root = binding.root
        setSupportActionBar(binding.toolbar)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        getFirebaseToken()
        checkIntentExtras(navController)


        if (navController.currentDestination?.id == R.id.HomeFragment) {
            supportActionBar?.hide()
        }
        appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment,
                R.id.DiaryFragment,
                /*R.id.ContactsFragment,*/
                R.id.MarksFragment,
                R.id.OtherFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkBadge()

        if (PskoveduApi.getInstance(this, navController).guid == "") {
            binding.contentMain.bottomNavigation.visibility = View.GONE
        }

        bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.action_0 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_home)
                    return@OnItemSelectedListener true
                }

                R.id.action_1 -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.to_diary)
                    return@OnItemSelectedListener true
                }

                R.id.action_3 -> {
                    bottomNav.getOrCreateBadge(R.id.action_3).isVisible = false
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

    override fun onDestroy() {
        marksJob?.cancel()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (PskoveduApi.getInstance(
                this,
                findNavController(R.id.nav_host_fragment_content_main)
            ).guid == ""
            && findNavController(R.id.nav_host_fragment_content_main).currentDestination?.id == R.id.NewChangeUserFragment
        )
            return;
        super.onBackPressed()
        supportActionBar?.subtitle = ""
    }

    fun checkNav() {
        try {
            if (PskoveduApi.getInstance(
                    this,
                    findNavController(R.id.nav_host_fragment_content_main)
                ).guid == ""
            ) {
                binding.contentMain.bottomNavigation.visibility = View.GONE
            } else binding.contentMain.bottomNavigation.visibility = View.VISIBLE
        } catch (_: IllegalArgumentException) {
        }

    }

    fun checkGPUpdates() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {

                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    appUpdateInfo.availableVersionCode()
                )
                }
                catch (e: IntentSender.SendIntentException){
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    fun checkNotificationsPermissionsAndRegisterChannels() {
        /*ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED -> {

        }
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                .setAction("Settings") {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri =
                        Uri.fromParts("com.onesilisondiode.geeksforgeeks", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                .show()
        }
        else -> {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Напоминания о контрольных"
            val descriptionText = "Напоминания о подготовке к контрольной"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("kr", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Уведомления о новых оценках"
            val descriptionText =
                "Каждый день будут приходить уведомления если появились новые оценки"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("marks", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getFirebaseToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaseFCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Storage.FIREBASE_TOKEN = token
                Log.d("FirebaseFCM", token)
            })
        } catch (_: Exception) {
        }
    }

    fun checkIntentExtras(navController: NavController) {
        val navigation = intent.getStringExtra("navigate")
        if (navigation != null) {
            when (navigation) {
                "kr" -> navController.navigate(R.id.to_kr)
                "marks" -> navController.navigate(R.id.to_marks)
            }
        }
        val crash = intent.getBooleanExtra("crash", false)
        if (crash) {
            navController.navigate(R.id.to_fatal_error)
        }
    }

    fun checkBadge() {
        marksJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = PskoveduApi.getInstance(
                    baseContext,
                    findNavController(R.id.nav_host_fragment_content_main)
                )
                if ((api.getCachedPeriods()?.data) == null) return@launch
                val cur_per = MarksTranslator.get_cur_period(api.getPeriods()?.data!!)
                val pattern = DateTimeFormat.forPattern("dd.MM.yyyy")
                val (marks, _) = api.getPeriodMarks(
                    cur_per[0].toString(pattern),
                    cur_per[1].toString(pattern)
                )
                withContext(Dispatchers.Main) {
                    var diffs = 0
                    marks?.data?.let { MarksTranslator(it).items }?.let { items ->
                        items.forEach {
                            diffs += MarksTranslator.getSubjectMarksDifferences(
                                baseContext, it.name,
                                items
                            ).size
                        }
                    }
                    if (diffs > 0) {
                        val badge = bottomNav.getOrCreateBadge(R.id.action_3)
                        badge.isVisible = true
                        badge.number = diffs
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun countLaunches() {
        val launch = DiaryPreferences.getInstance().getInt("launches")
        DiaryPreferences.getInstance().setInt("launches", launch + 1)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (PskoveduApi.getInstance(
                    this,
                    findNavController(R.id.nav_host_fragment_content_main)
                ).guid == ""
                && findNavController(R.id.nav_host_fragment_content_main).currentDestination?.id == R.id.NewChangeUserFragment
            )
                return false;
            findNavController(R.id.nav_host_fragment_content_main).navigateUp()
        }
        if (item.itemId == R.id.action_settings) findNavController(R.id.nav_host_fragment_content_main).navigate(
            R.id.to_new_ch_users
        )
        if (item.itemId == R.id.action_marks) findNavController(R.id.nav_host_fragment_content_main).navigate(
            NavGraphDirections.toAverage()
        )

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}

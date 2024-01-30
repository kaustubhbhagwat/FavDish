package com.example.favdish.view.activites

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.favdish.R
import com.example.favdish.databinding.ActivityMainBinding
import com.example.favdish.model.notification.NotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mNavController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes,
                R.id.navigation_favourite_dish,
                R.id.navigation_random_dish
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        binding.navView.setupWithNavController(mNavController)
        startWork()
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() =
        PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
            .setConstraints(createConstraints())
            .build()

    private fun startWork() {

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "FavDishNotificationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                createWorkRequest()
            )
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }


    fun hideBottomNavView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
        binding.navView.visibility = View.VISIBLE

    }
}
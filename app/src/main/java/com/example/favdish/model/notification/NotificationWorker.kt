package com.example.favdish.model.notification

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
    Log.i("Notification Worker", "Do work function is called")
        return Result.success()
    }
}
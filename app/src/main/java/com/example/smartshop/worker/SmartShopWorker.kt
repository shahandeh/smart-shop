package com.example.smartshop.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartshop.R
import com.example.smartshop.constant.CHANNEL_ID
import com.example.smartshop.constant.DATA_NAME
import com.example.smartshop.constant.DESCRIPTION
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.ui.detail.DetailFragmentArgs
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SmartShopWorker(
    private val appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext,
    params) {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private lateinit var pendingIntent: PendingIntent
    private lateinit var collapseView: RemoteViews

    override suspend fun doWork(): Result {

        val inputData = inputData.getString(DATA_NAME) ?: "3"
        val date = lastCheckTime(inputData)

        val result = WorkerNetworkManager.service.getNewProductList(date)
        if (result.isSuccessful) {
            result.body()?.let {
                collapseView(it[0])
                navDeepLink(it[0].id.toString())
            }
        }

        notificationManagerInit()
        notification()

        return Result.success()
    }

    private fun collapseView(product: Product) {

        collapseView = RemoteViews(appContext.packageName, R.layout.notification_collapse)
        collapseView.setTextViewText(R.id.name_collapse, product.name)
        collapseView.setTextViewText(R.id.date_collapse, product.date_created)
        if (product.sale_price.isNotBlank())
            collapseView.setTextViewText(R.id.price_collapse, product.sale_price)
        else
            collapseView.setTextViewText(R.id.price_collapse, product.regular_price)
        collapseView.setImageViewResource(R.id.image_collapse, R.drawable.ic_shop)

    }

    private fun navDeepLink(productId: String) {
        pendingIntent = NavDeepLinkBuilder(appContext)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.detailFragment)
            .setArguments(DetailFragmentArgs(productId).toBundle())
            .createPendingIntent()
    }

    private fun getCurrentTime(): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).format(Date())

    private fun lastCheckTime(input: String): String {
        val temp = getCurrentTime()
        val splitTime = temp.split("T").component2().split(":").component1()
        return temp.replace(splitTime, (splitTime.toInt().minus(input.toInt()).toString()))
    }

    private fun notificationManagerInit() {
        notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun notification() {
        notificationChannel =
            NotificationChannel(CHANNEL_ID, DESCRIPTION, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setCustomContentView(collapseView)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(appContext.resources,
                R.mipmap.ic_launcher_round))

        notificationManager.notify(1234, builder.build())

    }
}
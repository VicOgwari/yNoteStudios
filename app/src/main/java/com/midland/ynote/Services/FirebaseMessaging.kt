package com.midland.ynote.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.midland.ynote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.midland.ynote.Activities.CloudPhotoDocs
import com.midland.ynote.Activities.DocumentLoader
import com.midland.ynote.Activities.StreamingActivity
import com.midland.ynote.MainActivity
import com.midland.ynote.Utilities.MpesaListener
import com.midland.ynote.Utilities.NotificationReceiver
import com.midland.ynote.Utilities.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseMessaging : FirebaseMessagingService(), MpesaListener {

    private lateinit var activityIntent: Intent
    companion object Notifications {
        var notifications: ArrayList<ArrayList<String>> = ArrayList()
        var notification: ArrayList<String> = ArrayList()

        fun getMessage(title: String?, body: String?): ArrayList<ArrayList<String>> {
            notification.add(title!!)
            notification.add(body!!)
            notifications.add(notification)
            return notifications
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()){
            val stkPayLoad = remoteMessage.data["payload"]
            val donation = remoteMessage.data["donation"]
            val docReply = remoteMessage.data["documentRepliesData"]
            val lecReply = remoteMessage.data["lectureRepliesData"]
            val docComment = remoteMessage.data["documentData"]
            val lecComment = remoteMessage.data["lectureData"]
            val photoDocComment = remoteMessage.data["pictorialData"]
            if (stkPayLoad != null){
                val cause: String
                lateinit var mpesaListener: MpesaListener
                mpesaListener = this

                val gson = Gson()
                val mpesaResponse: Transaction = gson.fromJson(stkPayLoad, Transaction::class.java)
                val topicID = mpesaResponse.body.stkCallback.checkoutRequestID
                if (mpesaResponse.body.stkCallback.resultCode != 0) {
                    cause = mpesaResponse.body.stkCallback.resultDesc

                }
                else {
                    val infoList: List<Transaction.Body.StkCallback.CallbackMetadata.Item> =
                        mpesaResponse.body.stkCallback.callbackMetadata.item

                    var dateOfTransaction = ""
                    var amountTransacted = ""
                    var receiptNo = ""
                    var phoneNumber = ""

                    infoList.forEach { transaction ->
                        if (transaction.name == "MpesaReceiptNumber") {
                            receiptNo = transaction.value
                        }
                        if (transaction.name == "TransactionDate") {
                            dateOfTransaction = transaction.value
                        }
                        if (transaction.name == "PhoneNumber") {
                            phoneNumber = transaction.value
                        }
                        if (transaction.name == "Amount") {
                            amountTransacted = transaction.value
                        }
                    }

                    val pendingPurchases = DocumentLoader.loadPendingPurchase(applicationContext)
                    pendingPurchases.forEach {
                        if (it.endsWith(topicID)) {
                            val pendingFinalPin = it.split("_checkoutID_")[0]
                            val bool = DocumentLoader.completePurchase(
                                applicationContext,
                                pendingFinalPin,
                                null,
                                null
                            )
                            if (bool){
                                pendingPurchases.remove(it)
                                DocumentLoader.savePendingPurchase(
                                    applicationContext,
                                    pendingPurchases
                                )
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                                    .addOnSuccessListener {
                                    }

                            }else{
                                Toast.makeText(applicationContext, "Something's not right..", Toast.LENGTH_LONG).show()
                            }
                        }
                    }


                }

                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                    .addOnSuccessListener {
                    }

                activityIntent = Intent(applicationContext, DocumentLoader::class.java)
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activityIntent.putExtra("payload", stkPayLoad)
                val pendingIntent = PendingIntent.getActivity(applicationContext, 1, activityIntent,
                    PendingIntent.FLAG_ONE_SHOT)
                val channelId = "Default"
                val builder1 = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Default channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    manager.createNotificationChannel(channel)
                }
                manager.notify(0, builder1.build())


//                    val contentIntent = PendingIntent.getActivity(
//                        this,
//                        0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    )
//
//                    val builder = NotificationCompat.Builder(applicationContext, "Firebase Messaging")
//                        .apply {
//                            setSmallIcon(R.mipmap.ic_y_note_round)
//                            setContentTitle(remoteMessage.notification!!.title)
//                            setContentText(cause)
//                            color = Color.BLUE
//                            priority = NotificationCompat.PRIORITY_HIGH
//                            setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                            setContentIntent(contentIntent)
//                            setAutoCancel(true)
//                            setOnlyAlertOnce(true)
//                        }
//                    val managerCompat = NotificationManagerCompat.from(this)
//                    managerCompat.notify(101, builder.build())

            }
            if (docReply != null){
                activityIntent = Intent(applicationContext, DocumentLoader::class.java)
                activityIntent.putExtra("notification", docReply)
            }
            if (docReply != null){
                activityIntent = Intent(applicationContext, MainActivity::class.java)
                activityIntent.putExtra("notification", donation)
            }
            if (lecReply != null){
                activityIntent = Intent(applicationContext, DocumentLoader::class.java)
                activityIntent.putExtra("notification", lecReply)
            }
            if (docComment != null){
                activityIntent = Intent(applicationContext, DocumentLoader::class.java)
                activityIntent.putExtra("notification", docComment)
            }
            if (lecComment != null){
                activityIntent = Intent(applicationContext, StreamingActivity::class.java)
                activityIntent.putExtra("notification", lecComment)
            }
            if (photoDocComment != null){
                activityIntent = Intent(this, CloudPhotoDocs::class.java)
                activityIntent.putExtra("notification", photoDocComment)
            }

            val pendingIntent = PendingIntent.getActivity(applicationContext, 1, activityIntent,
                PendingIntent.FLAG_ONE_SHOT)
            val channelId = "Default"
            val builder1 = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Default channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            }
            manager.notify(0, builder1.build())

        }

    }


    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(user.uid).update("fcmToken", s)
                .addOnSuccessListener {
                    Toast.makeText(this, "New token:\n$s", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun extractDate(date: String): String {
        return "${date.subSequence(6, 8)}${date.subSequence(4, 6)} ${
            date.subSequence(0, 4)
        } at ${date.subSequence(8, 10)}:${date.subSequence(10, 12)}:${date.subSequence(12, 14)}"
    }

    private fun getFirebaseMessage(title: String?, body: String?, theDoc: String?) {

        val activityIntent = Intent(applicationContext, DocumentLoader::class.java)
        activityIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        activityIntent.action = Intent.ACTION_MAIN
        activityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activityIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        activityIntent.putExtra("notification", theDoc)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0, activityIntent, 0
        )

        val broadcastIntent = Intent(applicationContext, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(applicationContext, 0, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(applicationContext, "Firebase Messaging")
            .setSmallIcon(R.drawable.lib_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.lib_icon, "View profile", actionIntent)



        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(101, builder.build())

    }

    override fun sendingSuccessful(
        transactionAmount: String,
        phoneNumber: String,
        transactionDate: String,
        MPesaReceiptNo: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext,
                "Transaction Successful\nM-Pesa Receipt No: $MPesaReceiptNo\nTransaction Date: $transactionDate\nTransacting Phone Number: $phoneNumber\nAmount Transacted: $transactionAmount", Toast.LENGTH_LONG).show()
        }
    }

    override fun sendingFailed(cause: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext, "Transaction Failed\nReason: $cause", Toast.LENGTH_LONG
            ).show()
        }
    }
}
package com.example.book_app.activities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.book_app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // Extract the title and body from the received FCM message
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();

        // Show a notification with the received title and body
        showNotification(title, body);
    }

    private void showNotification(String title, String message) {
        // Build the notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        // Show the notification using NotificationManagerCompat
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(101, builder.build());
    }
}

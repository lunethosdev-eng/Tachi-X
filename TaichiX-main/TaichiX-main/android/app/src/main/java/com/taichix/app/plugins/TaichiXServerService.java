package com.taichix.app.plugins;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/** Keeps TaichiX's long-running server processes foreground on Android 8+. */
public class TaichiXServerService extends Service {
    private static final String CHANNEL_ID = "taichix_servers";
    private static final int NOTIFICATION_ID = 2408;

    @Override public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Servidores TaichiX", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Mantiene activo el servidor local de TaichiX");
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, notification("Servidor TaichiX activo"));
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent != null ? intent.getStringExtra("title") : null;
        if (title != null) {
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, notification(title));
        }
        return START_STICKY;
    }

    private Notification notification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setContentTitle("TaichiX")
            .setContentText(text)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build();
    }

    @Override public void onDestroy() { super.onDestroy(); }
    @Nullable @Override public IBinder onBind(Intent intent) { return null; }
}

package com.example.diet2.data.reminders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.diet2.R;

public class WaterReminderWorker extends Worker {

    public static final String CHANNEL_ID = "water_reminders";
    public static final int NOTIFICATION_ID = 1001;

    public WaterReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        ensureChannel(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(context.getString(R.string.water_notification_title))
                .setContentText(context.getString(R.string.water_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(NOTIFICATION_ID, notification);
        }

        return Result.success();
    }

    private static void ensureChannel(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        NotificationChannel existing = nm.getNotificationChannel(CHANNEL_ID);
        if (existing != null) return;

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.water_title),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(context.getString(R.string.water_subtitle));
        nm.createNotificationChannel(channel);
    }
}

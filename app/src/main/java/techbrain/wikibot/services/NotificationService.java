package techbrain.wikibot.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.R;
import techbrain.wikibot.WebViewActivity;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.delegates.WikiUrlPreview;

import static techbrain.wikibot.ChatActivity.*;

/**
 * Created by andrea on 24/12/17.
 */

public class NotificationService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MessageElement me = ChatActivity.addRandomCuriosita(this, null, null);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(me.getPreviewText());

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_audiotrack_dark)
                .setContentTitle(WikiUrlPreview.getPreviewBaseBey(me.getMessageValue()))
                .setContentText(me.getPreviewText())
                .setStyle(inboxStyle)
                .setAutoCancel(true);

        Intent resultIntent = new Intent(this, WebViewActivity.class);
        //pass data thought intent to another activity
        resultIntent.putExtra(WebViewActivity.URL, me.getMessageValue());

        PendingIntent resultPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 7);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        // I want to restart this service again in one hour
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
            alarm.RTC_WAKEUP,
            tomorrow.getTimeInMillis(),
            PendingIntent.getService(this, 0, new Intent(this, NotificationService.class), 0)
        );
    }
}

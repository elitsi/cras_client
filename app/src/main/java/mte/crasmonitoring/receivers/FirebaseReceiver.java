package mte.crasmonitoring.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.R;
import mte.crasmonitoring.ui.activities.MonitoringActivity;
import mte.crasmonitoring.ui.activities.ShowUserListsActivity;
import mte.crasmonitoring.utils.Constants;

public class FirebaseReceiver extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        String notificationMsg = remoteMessage.getData().get("msg");
        String notificationType = remoteMessage.getData().get("type");
        Intent openIntent;

        switch (notificationType)
        {
            case "monitor request":
                openIntent = getMonitoringActivityIntent();
                String supervisorId = remoteMessage.getData().get("sup_id");
                openIntent.putExtra(Constants.MONITOR_OPEN_ACTIVITY_TYPE_KEY,Constants.MONITOR_OPEN_ACTIVITY_TYPE_ADDED_SUPERVISOR_VALUE);
                openIntent.putExtra(Constants.MONITOR_SUPERVISOR_ID_KEY,supervisorId);
                break;
            case "monitoring approved":
            case "monitoring stopped by supervise":
                EventBus.getDefault().post(new Events.RefreshRemoteUsersEvent());
                openIntent = getMainActivityIntent();
                break;
            case "monitoring stopped by supervisor":
                EventBus.getDefault().post(new Events.StopMonitorRequestEvent());
                openIntent = getMainActivityIntent();
                break;
            default:
                openIntent = getMainActivityIntent();

        }
//        else if(TextUtils.equals(notificationType,"monitoring approved") || TextUtils.equals(notificationType,"monitoring stopped by supervise"))
//        {
//            EventBus.getDefault().post(new Events.RefreshRemoteUsersEvent());
//            openIntent = getMainActivityIntent();
//        }
//        else if(TextUtils.equals(notificationType,"monitoring approved"))
//        else
//            openIntent = getMainActivityIntent();

        sendNotification(notificationMsg, openIntent);

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.eye_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private Intent getMonitoringActivityIntent()
    {
        Intent intent = new Intent(this, MonitoringActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private Intent getMainActivityIntent()
    {
        Intent intent = new Intent(this, ShowUserListsActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}

package com.tamboraagungmakmur.winwin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tamboraagungmakmur.winwin.voip.IncommingCall;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;

/**
 * Created by innan on 10/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        com.tamboraagungmakmur.winwin.Utils.SessionManager sessionManager = new com.tamboraagungmakmur.winwin.Utils.SessionManager(this);
        SessionManager sessionManager1 = new SessionManager(this);
        if (sessionManager.checkLogin() && remoteMessage.getData().get("tipe") != null && !sessionManager1.getInCall()) {

            Log.d("notif", "ok");

            if (remoteMessage.getData().get("tipe").compareTo("call") == 0) {
                Intent call = new Intent(getApplicationContext(), IncommingCall.class);

                String dariid = remoteMessage.getData().get("dariid");
                String nama = remoteMessage.getData().get("nama");
                String foto = remoteMessage.getData().get("foto");
                String waktu = remoteMessage.getData().get("waktu");
                String tipe = remoteMessage.getData().get("tipe");
                String kode = remoteMessage.getData().get("kode");


                call.putExtra("dariid", dariid);
                call.putExtra("nama", nama);
                call.putExtra("foto", foto);
                call.putExtra("waktu", waktu);
                call.putExtra("tipe", tipe);
                call.putExtra("kode", kode);
                call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call);
            } else if (remoteMessage.getData().get("tipe").compareTo("chat") == 0) {
                Intent intent = new Intent("chat");
                intent.putExtra("id_user", remoteMessage.getData().get("iduser"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                android.support.v4.app.NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Message from " + remoteMessage.getData().get("nama"))
                                .setContentText(remoteMessage.getData().get("message"));

                Intent resultIntent = new Intent(this, ChatActivity.class);
                resultIntent.putExtra("id", remoteMessage.getData().get("iduser"));
                resultIntent.putExtra("nama", remoteMessage.getData().get("nama"));
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(uri);
                int mNotificationId = 1;
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            } else if (remoteMessage.getData().get("tipe").compareTo("notif") == 0) {

                Log.d("notif", remoteMessage.getData().get("message"));

                android.support.v4.app.NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Winwin")
                                .setContentText(remoteMessage.getData().get("message"));

                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")));

//                Intent resultIntent = new Intent(this, HomeActivity.class);
//                resultIntent.putExtra("baru", "ok");
//                PendingIntent resultPendingIntent =
//                        PendingIntent.getActivity(
//                                this,
//                                0,
//                                resultIntent,
//                                PendingIntent.FLAG_ONE_SHOT
//                        );
//                mBuilder.setContentIntent(resultPendingIntent);
                Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(uri);
                int mNotificationId = 2;
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

        }

    }

}

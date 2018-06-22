package com.tamboraagungmakmur.winwin.voip.Firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tamboraagungmakmur.winwin.voip.IncommingCall;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;

/**
 * Created by Tambora on 26/08/2016.
 */
public class MessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.checkLogin() && remoteMessage.getData().get("tipe") != null) {

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

        }

    }
}

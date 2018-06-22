package com.tamboraagungmakmur.winwin.voip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.tamboraagungmakmur.winwin.voip.Utils.AndLog;
import com.tamboraagungmakmur.winwin.voip.Utils.AppConf;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.voip.WebRTC.VoiceCalling;
import com.tamboraagungmakmur.winwin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class IncommingCall extends AppCompatActivity {


    private static final String TAG = "IncommingCallLog";
    private static final int CONNECTION_REQUEST = 1;
    private static boolean commandLineRun = false;
    private static String kode, dariid, nama, foto, tipe;
    @Bind(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @Bind(R.id.txNama)
    TextView txNama;
    @Bind(R.id.btAnswer)
    ImageButton btAnswer;
    @Bind(R.id.btReject)
    ImageButton btReject;

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;


    private MediaPlayer player;

    private Vibrator v;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(AppConf.SIGNALLING_URL);
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_incoming_call);
        ButterKnife.bind(this);

        new SessionManager(getApplicationContext()).setInCall(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            dariid = bundle.getString("dariid");
            Log.d("dariid", dariid);
            nama = bundle.getString("nama");
            foto = bundle.getString("foto");
            tipe = bundle.getString("tipe");
            kode = bundle.getString("kode");

            txNama.setText(nama);
//            Glide.with(IncommingCall.this).load(AppConf.URL_SERV_IMG + "" + foto).centerCrop().into(imgAvatar);

        }

        mSocket.on(getString(R.string.response), onStatus);
        mSocket.connect();
        mSocket.emit(getString(R.string.register), new com.tamboraagungmakmur.winwin.Utils.SessionManager(IncommingCall.this).getIdadmin(), dariid);

        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();

        long[] pattern = {0, 100, 1000, 300};
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(pattern, 0);

        countDownTimer = new MyCountDownTimer(startTime, interval);

        countDownTimer.start();
        timerHasStarted = true;
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    private void connectListener() {

        boolean loopback = false;

        commandLineRun = false;
        connectToRoom(loopback, 0);

    }

    private void connectToRoom(boolean loopback, int runTimeMs) {
        // Get room name (random for loopback).


        //Log.v(TAG, kode);

        String roomUrl = AppConf.pref_room_server_url_default;

        // Video call enabled flag.
        boolean videoCallEnabled =
                Boolean.valueOf(getString(R.string.pref_videocall_default));

        // Get default codecs.
        String videoCodec = getString(R.string.pref_videocodec_default);
        String audioCodec = getString(R.string.pref_audiocodec_default);

        // Check HW codec flag.
        boolean hwCodec = Boolean.valueOf(getString(R.string.pref_hwcodec_default));

        // Check Capture to texture.
        boolean captureToTexture = Boolean.valueOf(getString(R.string.pref_capturetotexture_default));

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = Boolean.valueOf(getString(R.string.pref_noaudioprocessing_default));

        // Check Disable Audio Processing flag.
        boolean aecDump = Boolean.valueOf(getString(R.string.pref_aecdump_default));

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = Boolean.valueOf(getString(R.string.pref_opensles_default));

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        String resolution = getString(R.string.pref_resolution_default);
        String[] dimensions = resolution.split("[ x]+");
        if (dimensions.length == 2) {
            try {
                videoWidth = Integer.parseInt(dimensions[0]);
                videoHeight = Integer.parseInt(dimensions[1]);
            } catch (NumberFormatException e) {
                videoWidth = 0;
                videoHeight = 0;
                //Log.e(TAG, "Wrong video resolution setting: " + resolution);
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        String fps = getString(R.string.pref_fps_default);
        String[] fpsValues = fps.split("[ x]+");
        if (fpsValues.length == 2) {
            try {
                cameraFps = Integer.parseInt(fpsValues[0]);
            } catch (NumberFormatException e) {
                //Log.e(TAG, "Wrong camera fps setting: " + fps);
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = Boolean.valueOf(getString(R.string.pref_capturequalityslider_default));

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        String bitrateTypeDefault = getString(
                R.string.pref_startvideobitrate_default);
        String bitrateType = bitrateTypeDefault;
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = getString(R.string.pref_startvideobitratevalue_default);
            videoStartBitrate = Integer.parseInt(bitrateValue);
        }
        int audioStartBitrate = 0;
        bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
        bitrateType = bitrateTypeDefault;
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = getString(R.string.pref_startaudiobitratevalue_default);
            audioStartBitrate = Integer.parseInt(bitrateValue);
        }

        // Check statistics display option.
        boolean displayHud = Boolean.valueOf(getString(R.string.pref_displayhud_default));

        boolean tracing = Boolean.valueOf(getString(R.string.pref_tracing_default));

        // Start AppRTCDemo activity.
        //Log.d(TAG, "Connecting to room " + kode + " at URL " + roomUrl);
        if (validateUrl(roomUrl)) {
            Uri uri = Uri.parse(roomUrl);
            boolean isVideo = true;

            Intent intent;
            if (tipe.equals("call")) {
                isVideo = false;
                intent = new Intent(IncommingCall.this, VoiceCalling.class);
            } else {
                intent = new Intent(IncommingCall.this, VoiceCalling.class);
            }

            intent.setData(uri);
            intent.putExtra(VoiceCalling.EXTRA_ROOMID, kode);
            intent.putExtra(VoiceCalling.EXTRA_LOOPBACK, loopback);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_CALL, isVideo);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_WIDTH, videoWidth);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_HEIGHT, videoHeight);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_FPS, cameraFps);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED,
                    captureQualitySlider);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_BITRATE, videoStartBitrate);
            intent.putExtra(VoiceCalling.EXTRA_VIDEOCODEC, videoCodec);
            intent.putExtra(VoiceCalling.EXTRA_HWCODEC_ENABLED, hwCodec);
            intent.putExtra(VoiceCalling.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
            intent.putExtra(VoiceCalling.EXTRA_NOAUDIOPROCESSING_ENABLED,
                    noAudioProcessing);
            intent.putExtra(VoiceCalling.EXTRA_AECDUMP_ENABLED, aecDump);
            intent.putExtra(VoiceCalling.EXTRA_OPENSLES_ENABLED, useOpenSLES);
            intent.putExtra(VoiceCalling.EXTRA_AUDIO_BITRATE, audioStartBitrate);
            intent.putExtra(VoiceCalling.EXTRA_AUDIOCODEC, audioCodec);
            intent.putExtra(VoiceCalling.EXTRA_DISPLAY_HUD, displayHud);
            intent.putExtra(VoiceCalling.EXTRA_TRACING, tracing);
            intent.putExtra(VoiceCalling.EXTRA_CMDLINE, commandLineRun);
            intent.putExtra(VoiceCalling.EXTRA_RUNTIME, runTimeMs);
            intent.putExtra("iduser", dariid);
            intent.putExtra("nama", nama);
            intent.putExtra("foto", foto);
            intent.putExtra("inout", "in");
            startActivityForResult(intent, CONNECTION_REQUEST);

            finish();
        }
    }

    private boolean validateUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return true;
        }

        new AlertDialog.Builder(IncommingCall.this)
                .setTitle(getText(R.string.invalid_url_title))
                .setMessage(getString(R.string.invalid_url_text, url))
                .setCancelable(false)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
        return false;
    }

    @OnClick({R.id.btAnswer, R.id.btReject})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAnswer:
                mSocket.emit(getString(R.string.alert), dariid, getString(R.string.answer));
                connectListener();
                break;
            case R.id.btReject:
                mSocket.emit(getString(R.string.alert), dariid, getString(R.string.hungup));
                Log.d("dariid_reject", dariid);
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        new SessionManager(getApplicationContext()).setInCall(false);
        v.cancel();
        player.stop();
        countDownTimer.cancel();
        mSocket.disconnect();
        mSocket.off(getString(R.string.response), onStatus);
    }

    private Emitter.Listener onStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("user");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    if (message != null) {
                        if (message.equals(getString(R.string.hungup))) {
                            finish();
                        }
                    }

                    AndLog.ShowLog(IncommingCall.class.getSimpleName(), data.toString() + " " + message);

                }
            });
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}

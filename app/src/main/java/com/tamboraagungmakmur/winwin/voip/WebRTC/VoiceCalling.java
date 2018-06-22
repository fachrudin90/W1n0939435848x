package com.tamboraagungmakmur.winwin.voip.WebRTC;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.voip.Utils.AndLog;
import com.tamboraagungmakmur.winwin.voip.Utils.AppConf;
import com.tamboraagungmakmur.winwin.voip.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.voip.Utils.LooperExecutor;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.voip.Utils.VolleyHttp;
import com.tamboraagungmakmur.winwin.voip.WebRTC.AppRTCClient.RoomConnectionParameters;
import com.tamboraagungmakmur.winwin.voip.WebRTC.AppRTCClient.SignalingParameters;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoiceCalling extends AppCompatActivity implements AppRTCClient.SignalingEvents,
        PeerConnectionClient.PeerConnectionEvents,
        CallFragment.OnCallEvents {

    public static final String EXTRA_ROOMID =
            "com.tamboraagungmakmur.winwinadmin.ROOMID";
    public static final String EXTRA_LOOPBACK =
            "com.tamboraagungmakmur.winwinadmin.LOOPBACK";
    public static final String EXTRA_VIDEO_CALL =
            "com.tamboraagungmakmur.winwinadmin.VIDEO_CALL";
    public static final String EXTRA_VIDEO_WIDTH =
            "com.tamboraagungmakmur.winwinadmin.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT =
            "com.tamboraagungmakmur.winwinadmin.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS =
            "com.tamboraagungmakmur.winwinadmin.VIDEO_FPS";
    public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
            "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER";
    public static final String EXTRA_VIDEO_BITRATE =
            "com.tamboraagungmakmur.winwinadmin.VIDEO_BITRATE";
    public static final String EXTRA_VIDEOCODEC =
            "com.tamboraagungmakmur.winwinadmin.VIDEOCODEC";
    public static final String EXTRA_HWCODEC_ENABLED =
            "com.tamboraagungmakmur.winwinadmin.HWCODEC";
    public static final String EXTRA_CAPTURETOTEXTURE_ENABLED =
            "com.tamboraagungmakmur.winwinadmin.CAPTURETOTEXTURE";
    public static final String EXTRA_AUDIO_BITRATE =
            "com.tamboraagungmakmur.winwinadmin.AUDIO_BITRATE";
    public static final String EXTRA_AUDIOCODEC =
            "com.tamboraagungmakmur.winwinadmin.AUDIOCODEC";
    public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
            "com.tamboraagungmakmur.winwinadmin.NOAUDIOPROCESSING";
    public static final String EXTRA_AECDUMP_ENABLED =
            "com.tamboraagungmakmur.winwinadmin.AECDUMP";
    public static final String EXTRA_OPENSLES_ENABLED =
            "com.tamboraagungmakmur.winwinadmin.OPENSLES";
    public static final String EXTRA_DISPLAY_HUD =
            "com.tamboraagungmakmur.winwinadmin.DISPLAY_HUD";
    public static final String EXTRA_TRACING = "com.tamboraagungmakmur.winwinadmin.TRACING";
    public static final String EXTRA_CMDLINE =
            "com.tamboraagungmakmur.winwinadmin.CMDLINE";
    public static final String EXTRA_RUNTIME =
            "com.tamboraagungmakmur.winwinadmin.RUNTIME";
    private static final String TAG = "CallRTCClient";

    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET"
    };

    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    private PeerConnectionClient peerConnectionClient = null;
    private AppRTCClient appRtcClient;
    private SignalingParameters signalingParameters;
    private AppRTCAudioManager audioManager = null;
    private EglBase rootEglBase;
    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRender;
    private PercentFrameLayout localRenderLayout;
    private PercentFrameLayout remoteRenderLayout;
    private RendererCommon.ScalingType scalingType;
    private Toast logToast;
    private boolean commandLineRun;
    private int runTimeMs;
    private boolean activityRunning;
    private RoomConnectionParameters roomConnectionParameters;
    private PeerConnectionClient.PeerConnectionParameters peerConnectionParameters;
    private boolean iceConnected;
    private boolean isError;
    private boolean callControlFragmentVisible = true;
    private long callStartedTimeMs = 0;
    private LinearLayout lyDroupCall;
    private TextView txStatus, txNama;
    private Chronometer cmTimer;
    private CircleImageView imgAvatar;
    private ImageButton btLoud;
    private ImageButton btMute;

    private String toiduser, nama, foto;

    private MediaPlayer player;
    private String mode;

    private AudioManager audioManagers;


    // Controls
    CallFragment callFragment;
    HudFragment hudFragment;

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
        Thread.setDefaultUncaughtExceptionHandler(
                new UnhandledExceptionHandler(this));

        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_voice_calling);

//        try {
//            ProviderInstaller.installIfNeeded(this);
//        } catch (GooglePlayServicesRepairableException e) {
//            // Thrown when Google Play Services is not installed, up-to-date, or enabled
//            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
//            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), VoiceCalling.this, 0);
//        } catch (GooglePlayServicesNotAvailableException e) {
//            //  Log.e("SecurityException", "Google Play Services not available.");
//        }

        audioManagers = ((AudioManager) getSystemService(
                Context.AUDIO_SERVICE));
        audioManagers.setSpeakerphoneOn(false);

        player = MediaPlayer.create(this, R.raw.ringback_tone);


        iceConnected = false;
        signalingParameters = null;
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

        // Create UI controls.
        lyDroupCall = (LinearLayout) findViewById((R.id.lyDropCall));
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        txStatus = (TextView) findViewById(R.id.txStatus);
        txNama = (TextView) findViewById(R.id.txNama);
        cmTimer = (Chronometer) findViewById(R.id.cmTimer);
        localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        remoteRender = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        localRenderLayout = (PercentFrameLayout) findViewById(R.id.local_video_layout);
        remoteRenderLayout = (PercentFrameLayout) findViewById(R.id.remote_video_layout);
        btLoud = (ImageButton) findViewById(R.id.btLoud);
        btMute = (ImageButton) findViewById(R.id.btMute);

        btLoud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btLoud.getTag().toString().equals("off")) {
                    btLoud.setTag("on");
                    btLoud.setImageResource(R.drawable.ic_loadspekeron);
                    audioManagers.setSpeakerphoneOn(true);
                } else {
                    btLoud.setTag("off");
                    btLoud.setImageResource(R.drawable.ic_loadspekeroff);
                    audioManagers.setSpeakerphoneOn(false);

                }

            }
        });

        btMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btMute.getTag().toString().equals("off")) {
                    btMute.setTag("on");
                    btMute.setImageResource(R.drawable.ic_muteon);
                    audioManagers.setMicrophoneMute(true);
                } else {
                    btMute.setTag("off");
                    btMute.setImageResource(R.drawable.ic_muteoff);
                    audioManagers.setMicrophoneMute(false);

                }

            }
        });

        callFragment = new CallFragment();
        hudFragment = new HudFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            toiduser = bundle.getString("iduser");
            nama = bundle.getString("nama");
            foto = bundle.getString("foto");
            txNama.setText(nama);
//            Glide.with(VoiceCalling.this).load(AppConf.URL_SERV_IMG + foto).into(imgAvatar);
            mode = bundle.getString("inout");

            new SessionManager(getApplicationContext()).setInCall(true);
            mSocket.on(getString(R.string.response), onStatus);
            mSocket.connect();
            mSocket.emit(getString(R.string.register), new com.tamboraagungmakmur.winwin.Utils.SessionManager(VoiceCalling.this).getIdadmin(), toiduser);
        }

        lyDroupCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        // Show/hide call control fragment on view click.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCallControlFragmentVisibility();
            }
        };

        localRender.setOnClickListener(listener);
        remoteRender.setOnClickListener(listener);

        // Create video renderers.
        rootEglBase = EglBase.create();
        localRender.init(rootEglBase.getEglBaseContext(), null);
        remoteRender.init(rootEglBase.getEglBaseContext(), null);
        localRender.setZOrderMediaOverlay(true);
        updateVideoView();

        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                //logAndToast("Permission " + permission + " is not granted");
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }

        // Get Intent parameters.
        final Intent intent = getIntent();
        Uri roomUri = intent.getData();
        if (roomUri == null) {
            //logAndToast(getString(R.string.missing_url));
            //Log.e(TAG, "Didn't get any URL in intent!");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        String roomId = intent.getStringExtra(EXTRA_ROOMID);
        if (roomId == null || roomId.length() == 0) {
            //logAndToast(getString(R.string.missing_url));
            //Log.e(TAG, "Incorrect room ID in intent!");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        boolean loopback = intent.getBooleanExtra(EXTRA_LOOPBACK, false);
        boolean tracing = intent.getBooleanExtra(EXTRA_TRACING, false);
        peerConnectionParameters = new PeerConnectionClient.PeerConnectionParameters(
                intent.getBooleanExtra(EXTRA_VIDEO_CALL, true),
                loopback,
                tracing,
                intent.getIntExtra(EXTRA_VIDEO_WIDTH, 0),
                intent.getIntExtra(EXTRA_VIDEO_HEIGHT, 0),
                intent.getIntExtra(EXTRA_VIDEO_FPS, 0),
                intent.getIntExtra(EXTRA_VIDEO_BITRATE, 0),
                intent.getStringExtra(EXTRA_VIDEOCODEC),
                intent.getBooleanExtra(EXTRA_HWCODEC_ENABLED, true),
                intent.getBooleanExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, false),
                intent.getIntExtra(EXTRA_AUDIO_BITRATE, 0),
                intent.getStringExtra(EXTRA_AUDIOCODEC),
                intent.getBooleanExtra(EXTRA_NOAUDIOPROCESSING_ENABLED, false),
                intent.getBooleanExtra(EXTRA_AECDUMP_ENABLED, false),
                intent.getBooleanExtra(EXTRA_OPENSLES_ENABLED, false));
        commandLineRun = intent.getBooleanExtra(EXTRA_CMDLINE, false);
        runTimeMs = intent.getIntExtra(EXTRA_RUNTIME, 0);

        // Create connection client and connection parameters.
        appRtcClient = new WebSocketRTCClient(this, new LooperExecutor());
        roomConnectionParameters = new RoomConnectionParameters(
                roomUri.toString(), roomId, loopback);

        // Send intent arguments to fragments.
        callFragment.setArguments(intent.getExtras());
        hudFragment.setArguments(intent.getExtras());
        // Activate call and HUD fragments and start the call.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.call_fragment_container, callFragment);
        ft.add(R.id.hud_fragment_container, hudFragment);
        ft.commit();
        startCall();

        // For command line execution run connection for <runTimeMs> and exit.
        if (commandLineRun && runTimeMs > 0) {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    disconnect();
                }
            }, runTimeMs);
        }

        peerConnectionClient = PeerConnectionClient.getInstance();
        if (loopback) {
            PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
            options.networkIgnoreMask = 0;
            peerConnectionClient.setPeerConnectionFactoryOptions(options);
        }
        peerConnectionClient.createPeerConnectionFactory(
                VoiceCalling.this, peerConnectionParameters, VoiceCalling.this);


        if (mode.equals("in")) {
            txStatus.setText(getString(R.string.menyambungkan));
        } else {
            txStatus.setText(getString(R.string.memanggil));
            player.setLooping(true);
            player.start();
            AndLog.ShowLog(VoiceCalling.class.getSimpleName(), getString(R.string.memanggil));
            final String rooms = roomId;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mSocket.connected()) {

                        AndLog.ShowLog(VoiceCalling.class.getSimpleName(), "konek");
                        CallingToUser(rooms);

                    } else {
                        GlobalToast.ShowToast(VoiceCalling.this, getString(R.string.disconnected));
                        AndLog.ShowLog(VoiceCalling.class.getSimpleName(), "tidak konek");
                        finish();
                    }

                }
            }, 1000);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
        if (peerConnectionClient != null) {
            peerConnectionClient.stopVideoSource();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;
        if (peerConnectionClient != null) {
            peerConnectionClient.startVideoSource();
        }

        LocalBroadcastManager.getInstance(VoiceCalling.this).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(VoiceCalling.this).registerReceiver(onNotice, new IntentFilter(getString(R.string.hungustate)));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean cek = bundle.getBoolean(getString(R.string.hungustates));
                if (cek) {
                    lyDroupCall.setVisibility(View.INVISIBLE);
                } else {
                    lyDroupCall.setVisibility(View.VISIBLE);
                }

            }
        }
    };

    @Override
    protected void onDestroy() {

        if (!iceConnected) {
            mSocket.emit(getString(R.string.alert), toiduser, getString(R.string.hungup));
        }


        new SessionManager(getApplicationContext()).setInCall(false);
        LocalBroadcastManager.getInstance(VoiceCalling.this).unregisterReceiver(onNotice);

        disconnect();
        if (logToast != null) {
            logToast.cancel();
        }
        activityRunning = false;

        player.stop();

        mSocket.disconnect();
        mSocket.off(getString(R.string.response), onStatus);

        //rootEglBase.release();
        super.onDestroy();


    }

    // CallFragment.OnCallEvents interface implementation.
    @Override
    public void onCallHangUp() {
        disconnect();
    }

    @Override
    public void onCameraSwitch() {
        if (peerConnectionClient != null) {
            peerConnectionClient.switchCamera();
        }
    }

    @Override
    public void onVideoScalingSwitch(RendererCommon.ScalingType scalingType) {
        this.scalingType = scalingType;
        updateVideoView();
    }

    @Override
    public void onCaptureFormatChange(int width, int height, int framerate) {
        if (peerConnectionClient != null) {
            peerConnectionClient.changeCaptureFormat(width, height, framerate);
        }
    }

    // Helper functions.
    private void toggleCallControlFragmentVisibility() {
        if (!iceConnected || !callFragment.isAdded()) {
            return;
        }
        // Show/hide call control fragment
        callControlFragmentVisible = !callControlFragmentVisible;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (callControlFragmentVisible) {
            ft.show(callFragment);
            ft.show(hudFragment);
        } else {
            ft.hide(callFragment);
            ft.hide(hudFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void updateVideoView() {
        remoteRenderLayout.setPosition(REMOTE_X, REMOTE_Y, REMOTE_WIDTH, REMOTE_HEIGHT);
        remoteRender.setScalingType(scalingType);
        remoteRender.setMirror(false);

        if (iceConnected) {
            localRenderLayout.setPosition(
                    LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED, LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED);
            localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        } else {
            localRenderLayout.setPosition(
                    LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING, LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING);
            localRender.setScalingType(scalingType);
        }
        localRender.setMirror(true);

        localRender.requestLayout();
        remoteRender.requestLayout();
    }

    private void startCall() {
        if (appRtcClient == null) {
            //Log.e(TAG, "AppRTC client is not allocated for a call.");
            return;
        }
        callStartedTimeMs = System.currentTimeMillis();

        // Start room connection.
        //logAndToast(getString(R.string.connecting_to,
        //roomConnectionParameters.roomUrl));
        appRtcClient.connectToRoom(roomConnectionParameters);

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.
        audioManager = AppRTCAudioManager.create(this, new Runnable() {
                    // This method will be called each time the audio state (number and
                    // type of devices) has been changed.
                    @Override
                    public void run() {
                        onAudioManagerChangedState();
                    }
                }
        );
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
        //Log.d(TAG, "Initializing the audio manager...");
        audioManager.init();

        audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
    }

    // Should be called from UI thread
    private void callConnected() {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        //Log.i(TAG, "Call connected: delay=" + delta + "ms");
        if (peerConnectionClient == null || isError) {
            //Log.w(TAG, "Call is connected in closed or error state");
            return;
        }
        // Update video view.
        updateVideoView();
        // Enable statistics callback.
        peerConnectionClient.enableStatsEvents(true, STAT_CALLBACK_PERIOD);
    }

    private void onAudioManagerChangedState() {
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.

    }

    // Disconnect from remote resources, dispose of local resources, and exit.
    private void disconnect() {
        activityRunning = false;
        if (appRtcClient != null) {
            appRtcClient.disconnectFromRoom();
            appRtcClient = null;
        }
        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
        }
        if (localRender != null) {
            localRender.release();
            localRender = null;
        }
        if (remoteRender != null) {
            remoteRender.release();
            remoteRender = null;
        }
        if (audioManager != null) {
            audioManager.close();
            audioManager = null;
        }
        if (iceConnected && !isError) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void disconnectWithErrorMessage(final String errorMessage) {
        if (commandLineRun || !activityRunning) {
            //Log.e(TAG, "Critical error: " + errorMessage);
            disconnect();
        } else {
//            new AlertDialog.Builder(this)
//                    .setTitle(getText(R.string.channel_error_title))
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                            disconnect();
//                        }
//                    }).create().show();

            AndLog.ShowLog(VoiceCalling.class.getSimpleName(), errorMessage);
        }
    }

    // Log |msg| and Toast about it.
    private void logAndToast(String msg) {
        //Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        logToast.show();
    }

    private void reportError(final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError) {
                    isError = true;
                    disconnectWithErrorMessage(description);
                }
            }
        });
    }

    // -----Implementation of AppRTCClient.AppRTCSignalingEvents ---------------
    // All callbacks are invoked from websocket signaling looper thread and
    // are routed to UI thread.
    private void onConnectedToRoomInternal(final SignalingParameters params) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;

        signalingParameters = params;
        //logAndToast("Creating peer connection, delay=" + delta + "ms");
        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(),
                localRender, remoteRender, signalingParameters);

        if (signalingParameters.initiator) {
            //logAndToast("Creating OFFER...");
            // Create offer. Offer SDP will be sent to answering client in
            // PeerConnectionEvents.onLocalDescription event.
            peerConnectionClient.createOffer();
        } else {
            if (params.offerSdp != null) {
                peerConnectionClient.setRemoteDescription(params.offerSdp);
                //logAndToast("Creating ANSWER...");
                // Create answer. Answer SDP will be sent to offering client in
                // PeerConnectionEvents.onLocalDescription event.
                peerConnectionClient.createAnswer();
            }
            if (params.iceCandidates != null) {
                // Add remote ICE candidates from room.
                for (IceCandidate iceCandidate : params.iceCandidates) {
                    peerConnectionClient.addRemoteIceCandidate(iceCandidate);
                }
            }
        }
    }

    @Override
    public void onConnectedToRoom(final SignalingParameters params) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onConnectedToRoomInternal(params);
            }
        });
    }

    @Override
    public void onRemoteDescription(final SessionDescription sdp) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    //Log.e(TAG, "Received remote SDP for non-initilized peer connection.");
                    return;
                }
                //logAndToast("Received remote " + sdp.type + ", delay=" + delta + "ms");
                peerConnectionClient.setRemoteDescription(sdp);
                if (!signalingParameters.initiator) {
                    //logAndToast("Creating ANSWER...");
                    // Create answer. Answer SDP will be sent to offering client in
                    // PeerConnectionEvents.onLocalDescription event.
                    peerConnectionClient.createAnswer();
                }
            }
        });
    }

    @Override
    public void onRemoteIceCandidate(final IceCandidate candidate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    /*Log.e(TAG,
                            "Received ICE candidate for non-initilized peer connection.");*/
                    return;
                }
                peerConnectionClient.addRemoteIceCandidate(candidate);
            }
        });
    }

    @Override
    public void onChannelClose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //logAndToast("Remote end hung up; dropping PeerConnection");
                disconnect();
            }
        });
    }

    @Override
    public void onChannelError(final String description) {
        reportError(description);
    }

    // -----Implementation of PeerConnectionClient.PeerConnectionEvents.---------
    // Send local peer connection SDP and ICE candidates to remote party.
    // All callbacks are invoked from peer connection client looper thread and
    // are routed to UI thread.
    @Override
    public void onLocalDescription(final SessionDescription sdp) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (appRtcClient != null) {
                    //logAndToast("Sending " + sdp.type + ", delay=" + delta + "ms");
                    if (signalingParameters.initiator) {
                        appRtcClient.sendOfferSdp(sdp);
                    } else {
                        appRtcClient.sendAnswerSdp(sdp);
                    }
                }
            }
        });
    }

    @Override
    public void onIceCandidate(final IceCandidate candidate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (appRtcClient != null) {
                    appRtcClient.sendLocalIceCandidate(candidate);
                }
            }
        });
    }

    @Override
    public void onIceConnected() {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mSocket.disconnect();
                mSocket.off(getString(R.string.response), onStatus);

                player.stop();
                //logAndToast("ICE connected, delay=" + delta + "ms");
                iceConnected = true;
                callConnected();
                txStatus.setVisibility(View.GONE);
                cmTimer.setVisibility(View.VISIBLE);
                cmTimer.setBase(SystemClock.elapsedRealtime());
                cmTimer.start();
            }
        });
    }

    @Override
    public void onIceDisconnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //logAndToast("ICE disconnected");
                iceConnected = false;
                disconnect();
            }
        });
    }

    @Override
    public void onPeerConnectionClosed() {
    }

    @Override
    public void onPeerConnectionStatsReady(final StatsReport[] reports) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError && iceConnected) {
                    hudFragment.updateEncoderStatistics(reports);
                }
            }
        });
    }

    @Override
    public void onPeerConnectionError(final String description) {
        reportError(description);
    }

    private Emitter.Listener onStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AndLog.ShowLog(VoiceCalling.class.getSimpleName(), args.toString());
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("user");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    if (message != null) {
                        if (message.equals(getString(R.string.hungup))) {
                            finish();
                        }
                    }

                    AndLog.ShowLog(VoiceCalling.class.getSimpleName(), data.toString());
                }
            });
        }
    };

    private void CallingToUser(final String roomId) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                com.tamboraagungmakmur.winwin.Utils.SessionManager sess = new com.tamboraagungmakmur.winwin.Utils.SessionManager(VoiceCalling.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("dariid", sess.getIdadmin());
                params.put("keid", toiduser);
                params.put("tipe", "call");
                params.put("kode", roomId);
                return params;
            }
        };

        VolleyHttp.getInstance(VoiceCalling.this).addToRequestQueue(stringRequest);
    }

}

package com.tamboraagungmakmur.winwin.voip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.URLUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tamboraagungmakmur.winwin.voip.Utils.AppConf;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.voip.Utils.VolleyHttp;
import com.tamboraagungmakmur.winwin.voip.WebRTC.VoiceCalling;
import com.tamboraagungmakmur.winwin.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VoiceCallBridge extends AppCompatActivity {

    private static final String TAG = "VoiceCallBridgeLog";
    private static final int CONNECTION_REQUEST = 1;
    private static boolean commandLineRun = false;

    public static String toiduser, nama, foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call_bridge);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            toiduser = bundle.getString("iduser");
            nama = bundle.getString("nama");
            foto = bundle.getString("foto");
            connectListener();
        }

    }

    private void  connectListener() {

        boolean loopback = false;

        commandLineRun = false;
        connectToRoom(loopback, 0);

    }

    private void connectToRoom(boolean loopback, int runTimeMs) {
        // Get room name (random for loopback).
        String roomId = "";

        Random rn = new Random();

        for(int i =0; i < 9; i++)
        {
            int num = rn.nextInt(10) + 1;
            roomId += num+"";
        }

        Log.v(TAG,roomId);

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
                Log.e(TAG, "Wrong video resolution setting: " + resolution);
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
                Log.e(TAG, "Wrong camera fps setting: " + fps);
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = Boolean.valueOf(getString(R.string.pref_capturequalityslider_default));

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        String bitrateTypeDefault = getString(
                R.string.pref_startvideobitrate_default);
        String bitrateType =  bitrateTypeDefault;
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
        Log.d(TAG, "Connecting to room " + roomId + " at URL " + roomUrl);
        if (validateUrl(roomUrl)) {
            Uri uri = Uri.parse(roomUrl);
            Intent intent = new Intent(VoiceCallBridge.this, VoiceCalling.class);
            intent.setData(uri);
            intent.putExtra(VoiceCalling.EXTRA_ROOMID, roomId);
            intent.putExtra(VoiceCalling.EXTRA_LOOPBACK, loopback);
            intent.putExtra(VoiceCalling.EXTRA_VIDEO_CALL, false);
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
            intent.putExtra("iduser", toiduser);
            intent.putExtra("nama", nama);
            intent.putExtra("foto", foto);
            intent.putExtra("inout","out");
            startActivityForResult(intent, CONNECTION_REQUEST);

            finish();

            //CallingToUser(roomId);
        }
    }

    private boolean validateUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return true;
        }

        new AlertDialog.Builder(VoiceCallBridge.this)
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

                SessionManager sess = new SessionManager(VoiceCallBridge.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("dariid", sess.getIduser());
                params.put("keid", toiduser);
                params.put("tipe", "call");
                params.put("kode", roomId);
                return params;
            }
        };

        VolleyHttp.getInstance(VoiceCallBridge.this).addToRequestQueue(stringRequest);
    }
}


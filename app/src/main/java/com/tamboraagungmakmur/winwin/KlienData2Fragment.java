package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.KlienFileAdapter;
import com.tamboraagungmakmur.winwin.Model.KlienFile;
import com.tamboraagungmakmur.winwin.Model.KlienFileResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;

/**
 * Created by innan on 9/26/2017.
 */

public class KlienData2Fragment extends Fragment {

    private final static String TAG = "KLIEN_DATA2";
    private RequestQueue requestQueue;

    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private View view;
    private Context context;

    private String id;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<KlienFile> klienFileArrayList = new ArrayList<>();
    private KlienFileAdapter adapter;

    private ImageView image1;
    private TextInputEditText note;
    private Spinner spinner1;
    private Button save;
    private ProgressBar progressBar;

    public static File file;
    public static String fileName;
    public static Uri uri;
    private String placeName;

    private int pos = 1;

    private Call<TaskStoreResponse> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_klien_data2, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
//        view = inflater.inflate(R.layout.fragment_klien_data2, container, false);
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");

        initView();
        getKlien();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("klien_file"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (klienFileArrayList != null) {
                klienFileArrayList.clear();
            } else {
                klienFileArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getKlien();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        if (call != null) {
            call.cancel();
        }
        super.onStop();
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_files);
        linearLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new KlienFileAdapter(klienFileArrayList, id);
        recyclerView.setAdapter(adapter);

        if (klienFileArrayList != null) {
            klienFileArrayList.clear();
        } else {
            klienFileArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();


        image1 = (ImageView) view.findViewById(R.id.image1);
        note = (TextInputEditText) view.findViewById(R.id.note);
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        save = (Button) view.findViewById(R.id.save);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.files, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileName != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    save.setVisibility(View.INVISIBLE);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileName, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    placeName = fileName;
                    if (imageHeight > 1200 && imageWidth > 1200) {
                        File compressedImage = new Compressor.Builder(context)
                                .setMaxWidth(1200)
                                .setMaxHeight(1200)
                                .setQuality(80)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Winwin")
                                .build()
                                .compressToFile(new File(fileName));
                        placeName = compressedImage.getAbsolutePath();
                    }
                    saveFile();
                } else {
                    Toast.makeText(context, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            file = new File(uri.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileName = getPath(context, uri);
            }

            Glide.with(context)
                    .load(fileName)
                    .asBitmap()
                    .placeholder(R.drawable.placeholder)
                    .into(image1);

        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_FILES + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienFileResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienFileResponse.class);

                    for (int i=0; i<klienResponse.getData().length; i++) {
                        klienFileArrayList.add(klienResponse.getData()[i]);
                        adapter.notifyItemChanged(klienFileArrayList.size()-1);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    getActivity().finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    getActivity().finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    public static class ServiceGenerator {

        public static final String API_BASE_URL = AppConf.BASE_URL;

        static OkHttpClient.Builder okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public static <S> S createService(Class<S> serviceClass) {
            Retrofit retrofit = builder.client(okHttpClient.build()).build();
            return retrofit.create(serviceClass);
        }
    }

    public interface FileUploadService {
        @Multipart
        @POST
        Call<TaskStoreResponse> upload(@Url String url,
                            @Part MultipartBody.Part foto1,
                            @Part("type") RequestBody type1,
                            @Part("note") RequestBody note1,
                            @Part("_session") RequestBody session1);
    }

    private void saveFile() {

        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        SessionManager sessionManager = new SessionManager(context);

        RequestBody type1 = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "" + pos);
        RequestBody note1 = RequestBody.create(okhttp3.MediaType.parse("text/plain"), note.getText().toString());
        RequestBody session1 = RequestBody.create(okhttp3.MediaType.parse("text/plain"), sessionManager.getSessionId());
        RequestBody foto2 = RequestBody.create(okhttp3.MediaType.parse("image/*"), new File(placeName));

        MultipartBody.Part foto1 = MultipartBody.Part.createFormData("file", placeName, foto2);

//        call = service.upload("https://hq.ppgwinwin.com:445/klien/uploadfile/"+id,foto1, type1, note1, session1);
        call = service.upload(AppConf.URL_KLIEN_UPLOAD_FILE+"/"+id,foto1, type1, note1, session1);
        call.enqueue(new Callback<TaskStoreResponse>() {

            @Override
            public void onResponse(Call<TaskStoreResponse> call, retrofit2.Response<TaskStoreResponse> response) {
//                Log.d("share foto", response.body());
                progressBar.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

                    Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show();

                    if (klienFileArrayList != null) {
                        klienFileArrayList.clear();
                    } else {
                        klienFileArrayList = new ArrayList<>();
                    }
                    adapter.notifyDataSetChanged();
                    getKlien();

            }

            @Override
            public void onFailure(Call<TaskStoreResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                if (call.isCanceled()) {
                    Log.e(TAG, "request was cancelled");
                } else {
                    Log.e("Upload error:", t.getMessage());
                    Toast.makeText(context, "Upload Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file file_paths.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}

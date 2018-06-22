package com.tamboraagungmakmur.winwin;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

/**
 * Created by innan on 12/11/2017.
 */

public class InvestorAddActivity extends FragmentActivity {

    private final static String TAG = "INVESTOR_ADD";
    private RequestQueue requestQueue;

    private Context context;

    private String id;

    private TextInputEditText email, name, alamat, phone, norek;
    private Button save;
    private ProgressBar progressBar;

    private ImageView ktp, foto, npwp, burek;

    private static final int PICK_IMAGE = 1;
    private static final int PICK_IMAGE1 = 2;
    private static final int PICK_IMAGE2 = 3;
    private static final int PICK_IMAGE3 = 4;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public static File file;
    public static String fileName;
    public static Uri uri;
    private String placeName;

    public static File file1;
    public static String fileName1;
    public static Uri uri1;
    private String placeName1;

    public static File file2;
    public static String fileName2;
    public static Uri uri2;
    private String placeName2;

    public static File file3;
    public static String fileName3;
    public static Uri uri3;
    private String placeName3;

    private Call<TaskStoreResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_edit);
        context = InvestorAddActivity.this;

        requestQueue = Volley.newRequestQueue(context);

        id = getIntent().getStringExtra("id");

        initView();

    }

    private void initView() {
        email = (TextInputEditText) findViewById(R.id.email);
        name = (TextInputEditText) findViewById(R.id.name);
        alamat = (TextInputEditText) findViewById(R.id.alamat);
        phone = (TextInputEditText) findViewById(R.id.phone);
        norek = (TextInputEditText) findViewById(R.id.norek);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        ktp = (ImageView) findViewById(R.id.ktp);
        foto = (ImageView) findViewById(R.id.foto);
        npwp = (ImageView) findViewById(R.id.npwp);
        burek = (ImageView) findViewById(R.id.burek);

        ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE);
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE1);
            }
        });

        npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE2);
            }
        });

        burek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE3);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                edit();
                if (fileName != null) {
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
                    saveFile("1", placeName);
                }
                if (fileName1 != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileName1, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    placeName1 = fileName1;
                    if (imageHeight > 1200 && imageWidth > 1200) {
                        File compressedImage = new Compressor.Builder(context)
                                .setMaxWidth(1200)
                                .setMaxHeight(1200)
                                .setQuality(80)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Winwin")
                                .build()
                                .compressToFile(new File(fileName1));
                        placeName1 = compressedImage.getAbsolutePath();
                    }
                    saveFile("11", placeName1);
                }
                if (fileName2 != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileName2, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    placeName2 = fileName2;
                    if (imageHeight > 1200 && imageWidth > 1200) {
                        File compressedImage = new Compressor.Builder(context)
                                .setMaxWidth(1200)
                                .setMaxHeight(1200)
                                .setQuality(80)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Winwin")
                                .build()
                                .compressToFile(new File(fileName2));
                        placeName2 = compressedImage.getAbsolutePath();
                    }
                    saveFile("21", placeName2);
                }
                if (fileName3 != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileName3, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    placeName3 = fileName3;
                    if (imageHeight > 1200 && imageWidth > 1200) {
                        File compressedImage = new Compressor.Builder(context)
                                .setMaxWidth(1200)
                                .setMaxHeight(1200)
                                .setQuality(80)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Winwin")
                                .build()
                                .compressToFile(new File(fileName3));
                        placeName3 = compressedImage.getAbsolutePath();
                    }
                    saveFile("7", placeName3);
                }

            }
        });


    }

    private void pickImage(int code) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InvestorAddActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(InvestorAddActivity.this,
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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
        }
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
                    .into(ktp);

        } else if (requestCode == PICK_IMAGE1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri1 = data.getData();
            file1 = new File(uri1.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileName1 = getPath(context, uri1);
            }

            Glide.with(context)
                    .load(fileName1)
                    .asBitmap()
                    .placeholder(R.drawable.placeholder)
                    .into(foto);

        } else if (requestCode == PICK_IMAGE2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri2 = data.getData();
            file2 = new File(uri2.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileName2 = getPath(context, uri2);
            }

            Glide.with(context)
                    .load(fileName2)
                    .asBitmap()
                    .placeholder(R.drawable.placeholder)
                    .into(npwp);

        } else if (requestCode == PICK_IMAGE3 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri3 = data.getData();
            file3 = new File(uri3.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileName3 = getPath(context, uri3);
            }

            Glide.with(context)
                    .load(fileName3)
                    .asBitmap()
                    .placeholder(R.drawable.placeholder)
                    .into(burek);

        }
    }

    private void edit() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_INVESTASI_INVESTOR + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!logoutResponse.isError()) {
                        Intent intent = new Intent("invest");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        onBackPressed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
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
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("email", email.getText().toString());
                params.put("name", name.getText().toString());
                params.put("alamat", alamat.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("norek", norek.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);

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
                                       @Part("_session") RequestBody session1);
    }

    private void saveFile(final String type, final String placeName) {

        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        SessionManager sessionManager = new SessionManager(context);

        RequestBody type1 = RequestBody.create(okhttp3.MediaType.parse("text/plain"), type);
        RequestBody session1 = RequestBody.create(okhttp3.MediaType.parse("text/plain"), sessionManager.getSessionId());
        RequestBody foto2 = RequestBody.create(okhttp3.MediaType.parse("image/*"), new File(placeName));

        MultipartBody.Part foto1 = MultipartBody.Part.createFormData("file", placeName, foto2);

        call = service.upload(AppConf.URL_INVESTASI_INVESTOR_FILE + "/" +id,foto1, type1, session1);
        call.enqueue(new Callback<TaskStoreResponse>() {

            @Override
            public void onResponse(Call<TaskStoreResponse> call, retrofit2.Response<TaskStoreResponse> response) {
//                Log.d("share foto", response.body());
                progressBar.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

                Intent intent = new Intent("invest");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show();

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

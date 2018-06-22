package com.tamboraagungmakmur.winwin;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.AdapterTransaksiPembayaran;
import com.tamboraagungmakmur.winwin.Adapter.DebitBcaAdapter;
import com.tamboraagungmakmur.winwin.Model.KreditBca;
import com.tamboraagungmakmur.winwin.Model.KreditBcaResponse;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.ModelTransaksiPembayaran;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.MediaProcess;
import com.tamboraagungmakmur.winwin.Utils.OwnProgressDialog;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
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

import static com.tamboraagungmakmur.winwin.Utils.AppConf.URL_GET_API_BCA;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiPencairan extends Fragment {

    private final static String TAG = "TRANSAKSI_PENCAIRAN";
    @Bind(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    private RequestQueue requestQueue;
    private Call<TaskStoreResponse> call;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Bind(R.id.txFile)
    EditText txFile;
    @Bind(R.id.btSubmit)
    ImageButton btSubmit;
    @Bind(R.id.pbSubmit)
    ProgressBar pbSubmit;

    private View view;
    private Context context;

    private static String id;
    private String fileName = "";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<KreditBca> kreditBcaArrayList = new ArrayList<>();
    private DebitBcaAdapter adapter;

    private TextView text1;
    private Button save;
    private ProgressBar progressBar;
    private LinearLayout lin1, lin2;
    StringRequest stringRequest;
    private AdapterTransaksiPembayaran transaksiPembayaran;
    private ArrayList<ModelTransaksiPembayaran> arrayList = new ArrayList<>();
    OwnProgressDialog loading;

    public TransaksiPencairan() {
        // Required empty public constructor
    }


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
            view = inflater.inflate(R.layout.fragment_transaksi_pembayaran, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        ButterKnife.bind(this, view);

        requestQueue = Volley.newRequestQueue(context);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Transaksi Pencairan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

//        initView();
//        if (kreditBcaArrayList != null) {
//            kreditBcaArrayList.clear();
//        } else {
//            kreditBcaArrayList = new ArrayList<>();
//        }
//        adapter.notifyDataSetChanged();
//        text1.setText("");
//        getKlien();

        recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        transaksiPembayaran = new AdapterTransaksiPembayaran(arrayList, context);
        recyclerView.setAdapter(transaksiPembayaran);

        loading = new OwnProgressDialog(context);

        loading.show();
        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataBCA();
            }
        });

        getDataBCA();

        return view;
    }

    private void getDataBCA() {
        arrayList = new ArrayList<>();

        stringRequest = new StringRequest(Request.Method.GET, URL_GET_API_BCA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GG: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        ModelTransaksiPembayaran modelMenu = new ModelTransaksiPembayaran();
                        modelMenu.setTanggal(json.getString("TransactionDate"));
                        modelMenu.setKeterangan(json.getString("Trailer"));
                        modelMenu.setNominal(json.getString("TransactionAmount"));
                        modelMenu.setPengajuan(json.getString("TransactionType"));

                        if (modelMenu.getPengajuan().equals("D")){
                            modelMenu.setPengajuan("Debit");
                        }

                        if (!modelMenu.getPengajuan().equals("C")){
                            arrayList.add(modelMenu);
                        }

                    }

                    AdapterTransaksiPembayaran adapter = new AdapterTransaksiPembayaran(arrayList, context);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.dismiss();
                if (Swipe != null) {
                    Swipe.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.dismiss();
                if (Swipe != null) {
                    Swipe.setRefreshing(false);
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        if (call != null) {
            call.cancel();
        }
        super.onStop();
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DebitBcaAdapter(kreditBcaArrayList);
        recyclerView.setAdapter(adapter);

        text1 = (TextView) view.findViewById(R.id.text1);
        save = (Button) view.findViewById(R.id.save);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        lin2 = (LinearLayout) view.findViewById(R.id.lin2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                save();
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("kreditbca"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (kreditBcaArrayList != null) {
                kreditBcaArrayList.clear();
            } else {
                kreditBcaArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getKlien();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.txFile, R.id.btSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txFile:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Intent intent = new Intent();
                        intent.setType("text/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Open CSV"), MediaProcess.SELECT_FILE);

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("text/*");
                    startActivityForResult(Intent.createChooser(intent, "Open CSV"), MediaProcess.SELECT_FILE);
//                startActivityForResult(intent, MediaProcess.SELECT_FILE);
                }
                break;
            case R.id.btSubmit:
                pbSubmit.setVisibility(View.VISIBLE);
                btSubmit.setVisibility(View.INVISIBLE);
                saveFile();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == MediaProcess.SELECT_FILE && resultCode == Activity.RESULT_OK) {


//            final String filePath = data.getData().getPath();
//            final String filePath = MediaProcess.getFilePathFromUri(getActivity(), data.getData());
            //final String fileName = MediaProcess.getFilenameFromPath(filePath);

            Uri uri = data.getData();
            File file = new File(uri.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileName = getPath(context, uri);
            }

            txFile.setText(fileName);

        }

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
                                       @Part("_session") RequestBody session1);
    }

    private void saveFile() {

        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        SessionManager sessionManager = new SessionManager(context);

        RequestBody session1 = RequestBody.create(MediaType.parse("text/plain"), sessionManager.getSessionId());
        RequestBody foto2 = RequestBody.create(MediaType.parse("image/*"), new File(fileName));

        MultipartBody.Part foto1 = MultipartBody.Part.createFormData("file", fileName, foto2);

//        call = service.upload("https://hq.ppgwinwin.com:445/debitbca/upload", foto1, session1);
        call = service.upload(AppConf.URL_DEBITBCA_UPLOAD, foto1, session1);
        call.enqueue(new Callback<TaskStoreResponse>() {

            @Override
            public void onResponse(Call<TaskStoreResponse> call, retrofit2.Response<TaskStoreResponse> response) {
//                Log.d("share foto", response.body());
                pbSubmit.setVisibility(View.INVISIBLE);
                btSubmit.setVisibility(View.VISIBLE);

                Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show();

                if (kreditBcaArrayList != null) {
                    kreditBcaArrayList.clear();
                } else {
                    kreditBcaArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                text1.setText("");
                getKlien();

            }

            @Override
            public void onFailure(Call<TaskStoreResponse> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "request was cancelled");
                } else {
                    Log.e("Upload error:", t.getMessage());
                    Toast.makeText(context, "Upload Error", Toast.LENGTH_SHORT).show();
                }
                pbSubmit.setVisibility(View.INVISIBLE);
                btSubmit.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_DEBITBCA + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KreditBcaResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KreditBcaResponse.class);


                    if (klienResponse.getData() != null) {
                        lin1.setVisibility(View.GONE);
                        lin2.setVisibility(View.VISIBLE);
                        text1.setText(klienResponse.getData().getTitle());
                        id = klienResponse.getData().getId();
                        for (int i = 0; i < klienResponse.getData().getData().length; i++) {
                            klienResponse.getData().getData()[i].setId_upload(klienResponse.getData().getId());
                            kreditBcaArrayList.add(klienResponse.getData().getData()[i]);
                            adapter.notifyItemChanged(kreditBcaArrayList.size() - 1);
                        }
                    } else {
                        lin1.setVisibility(View.VISIBLE);
                        lin2.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void save() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_DEBITBCA_FINALIZE + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Toast.makeText(context, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (!klienResponse.isError()) {
                        if (kreditBcaArrayList != null) {
                            kreditBcaArrayList.clear();
                        } else {
                            kreditBcaArrayList = new ArrayList<>();
                        }
                        adapter.notifyDataSetChanged();
                        text1.setText("");
                        getKlien();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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
                final String[] selectionArgs = new String[]{
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
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
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

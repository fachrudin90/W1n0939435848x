package com.tamboraagungmakmur.winwin.Adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.tamboraagungmakmur.winwin.KlienFileActivity;
import com.tamboraagungmakmur.winwin.KlienFileDeleteActivity;
import com.tamboraagungmakmur.winwin.Model.KlienFile;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.HttpsTrustManager;
import com.tamboraagungmakmur.winwin.Utils.MediaProcess;
import com.viven.imagezoom.ImageZoomHelper;

import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by innan on 9/27/2017.
 */

public class KlienFileAdapter extends RecyclerView.Adapter<KlienFileAdapter.KlienFileViewHolder> {

    private static final String TAG = "IMAGE_ZOOM";
    private ArrayList<KlienFile> klienFileArrayList;
    private Context context;

    private String id;

    ImageZoomHelper imageZoomHelper;

    private static final String TAG1 = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public KlienFileAdapter(ArrayList<KlienFile> klienFileArrayList, String id) {

        this.klienFileArrayList = klienFileArrayList;
        this.id = id;
    }

    @Override
    public KlienFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HttpsTrustManager.allowAllSSL();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_klienfile, parent, false);
        KlienFileViewHolder holder = new KlienFileViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(final KlienFileViewHolder holder, int position) {


        final KlienFile klienFile = klienFileArrayList.get(position);

        final String img = AppConf.BASE_URL_IMG + klienFile.getFilename().replace("..", "").replace("\\", "");
        Log.d("img_klien_x", img);

        Glide.with(context)
                .load(img).asBitmap()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.image1);

//        Glide.with(context)
//                .load(img)
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>(100,100) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                        AndLog.ShowLog("img_klien_y",img);
//                        holder.image1.setImageBitmap(resource); // Possibly runOnUiThread()
//                    }
//                });

//        holder.image1.setDrawingCacheEnabled(true);
//        holder.image1.buildDrawingCache();
//        Bitmap bitmap = holder.image1.getDrawingCache();

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KlienFileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("img", img);
                intent.putExtra("type", klienFile.getType());
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        holder.text1.setText(klienFile.getType());
        holder.text2.setText(klienFile.getCreated_at());
        holder.text3.setText(klienFile.getNote());

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent(context, KlienFileDeleteActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", klienFile.getId());
//                context.startActivity(intent);
//                return true;
//            }
//        });

//        REDLYST
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, KlienFileDeleteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", klienFile.getId());
                context.startActivity(intent);
                return true;
            }
        });

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KlienFileDeleteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", klienFile.getId());
                context.startActivity(intent);
//                Toast.makeText(context, klienFile.getId(), Toast.LENGTH_SHORT).show();
            }
        });

//        try {
//            File file = Glide
//                    .with(context)
//                    .load(img)
//                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get()
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        holder.bShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DownloadImage().execute(img, klienFile.getFilename());
            }
        });


//        imageZoomHelper = new ImageZoomHelper((Activity)context);
//        ImageZoomHelper.setViewZoomable(holder.image1);


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            View decorView = getWindow().getDecorView();
//            // Hide the status bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(uiOptions);
//        }


//        holder.image1.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

//        holder.image1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ImageView view = (ImageView) v;
//                view.setScaleType(ImageView.ScaleType.MATRIX);
//                float scale;
//
//                dumpEvent(event);
//                // Handle touch events here...
//
//                switch (event.getAction() & MotionEvent.ACTION_MASK)
//                {
//                    case MotionEvent.ACTION_DOWN:   // first finger down only
//                        savedMatrix.set(matrix);
//                        start.set(event.getX(), event.getY());
//                        Log.d(TAG, "mode=DRAG"); // write to LogCat
//                        mode = DRAG;
//                        break;
//
//                    case MotionEvent.ACTION_UP: // first finger lifted
//
//                    case MotionEvent.ACTION_POINTER_UP: // second finger lifted
//
//                        mode = NONE;
//                        Log.d(TAG, "mode=NONE");
//                        break;
//
//                    case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
//
//                        oldDist = spacing(event);
//                        Log.d(TAG, "oldDist=" + oldDist);
//                        if (oldDist > 5f) {
//                            savedMatrix.set(matrix);
//                            midPoint(mid, event);
//                            mode = ZOOM;
//                            Log.d(TAG, "mode=ZOOM");
//                        }
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//
//                        if (mode == DRAG)
//                        {
//                            matrix.set(savedMatrix);
//                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
//                        }
//                        else if (mode == ZOOM)
//                        {
//                            // pinch zooming
//                            float newDist = spacing(event);
//                            Log.d(TAG, "newDist=" + newDist);
//                            if (newDist > 5f)
//                            {
//                                matrix.set(savedMatrix);
//                                scale = newDist / oldDist; // setting the scaling of the
//                                // matrix...if scale > 1 means
//                                // zoom in...if scale < 1 means
//                                // zoom out
//                                matrix.postScale(scale, scale, mid.x, mid.y);
//                            }
//                        }
//                        break;
//                }
//
//                view.setImageMatrix(matrix); // display the transformation on screen
//
//                return true; // indicate event was handled
//            }
//        });


    }


    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) (Math.sqrt(x * x + y * y));
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public int getItemCount() {
        return klienFileArrayList.size();
    }

    public class KlienFileViewHolder extends RecyclerView.ViewHolder {

        private PhotoView image1;
        private TextView text1, text2, text3;
        private Button bShare, bDelete;

        public KlienFileViewHolder(View itemView) {
            super(itemView);

            image1 = (PhotoView) itemView.findViewById(R.id.image1);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            text3 = (TextView) itemView.findViewById(R.id.text3);
            bShare = (Button) itemView.findViewById(R.id.bShare);
            bDelete = (Button) itemView.findViewById(R.id.bDelete);
        }
    }


    private class DownloadImage extends AsyncTask<String, Integer, Bitmap> {
        String TAG = getClass().getSimpleName();
        String fName = "";
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog = new ProgressDialog(context);
             progressDialog.setMessage("Loading...");
             progressDialog.setCancelable(false);
             progressDialog.show();
        }

        protected Bitmap doInBackground(String... arg0) {
            fName = arg0[1];
            Bitmap bitmap = MediaProcess.getBitmapFromURL(arg0[0]);
            return bitmap;
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result != null) {
                Bitmap icon = result;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = context.getContentResolver().openOutputStream(uri);
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(share, "Bagikan"));
            }else{
                GlobalToast.ShowToast(context, "Gagal membagikan dokumen, silahkan coba kembali");
            }
        }
    }
}

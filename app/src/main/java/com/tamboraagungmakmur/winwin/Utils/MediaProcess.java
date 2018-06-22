package com.tamboraagungmakmur.winwin.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tambora on 16/09/2016.
 */
public class MediaProcess {

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1122;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 1133;
    public static final int SELECT_PHOTO = 1100;
    public static final int SELECT_FILE = 1200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String FILE_DIRECTORY_NAME = "PolriMessenger";


    public static Bitmap scaledBitmap(String imgPath) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(imgPath, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        // Resize

        float newWidth = 0;
        float newHeight = 0;


        if (rotatedBitmap.getHeight() > rotatedBitmap.getWidth()) {

            //orientation = "portrait";

            newWidth = 640;
            newHeight = newWidth / rotatedBitmap.getWidth() * rotatedBitmap.getHeight();


        } else {

            //orientation = "lanscape";
            newHeight = 640;
            newWidth = newHeight / rotatedBitmap.getHeight() * rotatedBitmap.getWidth();


        }


        int width = rotatedBitmap.getWidth();
        int height = rotatedBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrixs = new Matrix();
        // RESIZE THE BIT MAP
        matrixs.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                rotatedBitmap, 0, 0, width, height, matrixs, false);
        rotatedBitmap.recycle();


        return resizedBitmap;

    }

    public static boolean CopyAndClose(InputStream sourceLocation, FileOutputStream targetLocation) {

        InputStream inStream = null;
        OutputStream outStream = null;

        boolean success = false;

        try {

            inStream = sourceLocation;
            outStream = targetLocation;

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            success = true;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;

    }

    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                FILE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        // Create a media file name
        String nametimeStamp = generateFileName("IMG", "jpg");

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + nametimeStamp);
        } else {
            return null;
        }

        return mediaFile;
    }


    public static String generateFileName(String front, String type) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return front + "_" + timeStamp + "_" + System.currentTimeMillis() + "." + type;
    }

    public static String getFilenameFromUri(Context context, Uri uri) {

        String fileName = null;

        String scheme = uri.getScheme();
        if (scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Video.Media.TITLE};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
        }
        return fileName;
    }

    public static String getFilenameFromUriV2(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();

            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }
        return fileName;
    }

    // Its Work Well
    public static String getFilePathFromUri(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();
        }
        return fileName;
    }

    public static String getFilenameFromPath(String path) {
        String fileName = "";
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }

        return fileName;
    }

    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static String getExtentionFromFile(String fileName) {
        String extension = null;
        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        if (extension == null) {

            extension = "UNKNOWN";
        }
        return extension + "";
    }

    public static boolean copyFile(File src, File dst) throws IOException {
        boolean result = false;
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            result = true;
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        return result;
    }

    public static File bitmapCompress(String path, int level) {

        File newFile = new File(path);
        OutputStream outStream = null;

        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
            outStream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, level, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFile;

    }

    public static File bitmapToFile(Bitmap bitmap, String path) {

        File f = new File(path);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMediaPath() {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                FILE_DIRECTORY_NAME);

        return mediaStorageDir.getAbsolutePath();
    }

    public static String getMimeTypeV2(File file) {
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        return mimetype;
    }

    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}

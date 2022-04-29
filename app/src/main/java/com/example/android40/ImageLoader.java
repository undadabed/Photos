package com.example.android40;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageLoader(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return getBitmap(params[0]);
    }

    protected Bitmap getBitmap(String path){
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return b;
        }else
            return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}

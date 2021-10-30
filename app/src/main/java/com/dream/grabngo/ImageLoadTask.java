package com.dream.grabngo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private final String url;
    @SuppressLint("StaticFieldLeak")
    private final ImageView imageView;
    @SuppressLint("StaticFieldLeak")
    private final ShimmerFrameLayout shimmerFrameLayout;

    public ImageLoadTask(String url, ImageView imageView, ShimmerFrameLayout shimmerFrameLayout) {
        this.url = url;
        this.imageView = imageView;
        this.shimmerFrameLayout = shimmerFrameLayout;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        shimmerFrameLayout.hideShimmer();
        shimmerFrameLayout.stopShimmer();
        imageView.setImageBitmap(result);
    }
}

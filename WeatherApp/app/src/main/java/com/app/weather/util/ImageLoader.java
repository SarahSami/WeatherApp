package com.app.weather.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.app.weather.api.APIUrl;

import java.io.InputStream;

public class ImageLoader {


    public ImageLoader() {

    }

    public void downloadImage(final ImageView imageView, final String iconId) {
        AsyncTask task = new AsyncTask<Void, Void, Void>() {


            Bitmap bitmap;

            @Override
            protected Void doInBackground(Void... params) {
                String url = APIUrl.ICON_URL + iconId + ".png";
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                imageView.setImageBitmap(bitmap);
            }
        };
        task.execute((Void[]) null);
    }
}

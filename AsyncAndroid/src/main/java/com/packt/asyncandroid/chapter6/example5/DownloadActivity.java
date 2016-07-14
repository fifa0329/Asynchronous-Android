package com.packt.asyncandroid.chapter6.example5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ImageView;

import com.packt.asyncandroid.LaunchActivity;
import com.packt.asyncandroid.R;
import com.packt.asyncandroid.chapter6.ConcurrentDownloadIntentService;

public class DownloadActivity extends Activity {

    public static final String URL =
            "http://www.nasa.gov/images/content/158270main_solarflare.jpg";

    private static final BitmapHandler handler = new BitmapHandler();
    private static final Messenger messenger = new Messenger(handler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch6_example5_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.attach((ImageView) findViewById(R.id.img));


        Intent intent = new Intent(this, ConcurrentDownloadIntentService.class);
        intent.putExtra(ConcurrentDownloadIntentService.DOWNLOAD_FROM_URL, URL);
        intent.putExtra(ConcurrentDownloadIntentService.REQUEST_ID, URL.hashCode());
        intent.putExtra(ConcurrentDownloadIntentService.MESSENGER, messenger);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.detach();
    }

    private static class BitmapHandler extends Handler {
        private ImageView view;

        @Override
        public void handleMessage(Message message) {
            if (message.what == ConcurrentDownloadIntentService.SUCCESSFUL) {
                if (view != null)
                    view.setImageURI((Uri) message.obj);
            } else {
                Log.w(LaunchActivity.TAG, "startDownload failed :(");
            }
        }

        public void attach(ImageView view) {
            this.view = view;
        }

        public void detach() {
            this.view = null;
        }
    }
}

package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CameraActivity extends AppCompatActivity {

    WebView cameraFeed;
    Button backButton;
    Button reloadButton;
    String IP = "";


    // Threads to handle streaming
    private HandlerThread stream_thread,flash_thread,rssi_thread;
    private Handler stream_handler,flash_handler,rssi_handler;

    private final int ID_CONNECT = 200;
    HttpURLConnection huc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Retrieve the IP
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            IP = extras.getString("IP");
        }

//        stream_thread = new HandlerThread("http");
//        stream_thread.start();
//        stream_handler = new HttpHandler(stream_thread.getLooper());
//
//        stream_handler.sendEmptyMessage(ID_CONNECT);

        cameraFeed = (WebView) findViewById(R.id.camera_monitor);
        backButton = (Button) findViewById(R.id.camera_back);
        reloadButton = (Button) findViewById(R.id.camera_reload);

        cameraFeed.setWebViewClient(new WebViewClient());
        cameraFeed.loadUrl("http://" + IP + ":81/stream");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadPressed();
            }
        });
    }

    // Might not need to disable back button navigation tbh
    @Override
    public void onBackPressed() {
//        huc.disconnect();
//        stream_handler.removeCallbacksAndMessages(null);
        cameraFeed.destroy();
        goToAccountDetails();
    }

    private void onReloadPressed(){
        cameraFeed.reload();
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
    }

//    // Following code taken from https://github.com/GCY/Android-ESP32-CAM-MJPEG-Viewer
//    private class HttpHandler extends Handler {
//        public HttpHandler(Looper looper)
//        {
//            super(looper);
//        }
//
//        @Override
//        public void handleMessage(Message msg)
//        {
//            switch (msg.what)
//            {
//                case ID_CONNECT:
//                    VideoStream();
//                default:
//                    break;
//            }
//        }
//    }
//
//    private void VideoStream() {
//        String stream_url = "http://" + IP + ":81/stream";
//
//        BufferedInputStream bis = null;
//        FileOutputStream fos = null;
//        try {
//
//            URL url = new URL(stream_url);
//
//            try {
//                huc = (HttpURLConnection) url.openConnection();
//                huc.setRequestMethod("GET");
//                huc.setConnectTimeout(1000 * 5);
//                huc.setReadTimeout(1000 * 5);
//                huc.setDoInput(true);
//                huc.connect();
//
//                if (huc.getResponseCode() == 200) {
//                    InputStream in = huc.getInputStream();
//
//                    InputStreamReader isr = new InputStreamReader(in);
//                    BufferedReader br = new BufferedReader(isr);
//
//                    String data;
//
//                    int len;
//                    byte[] buffer;
//
//                    while ((data = br.readLine()) != null) {
//                        if (data.contains("Content-Type:")) {
//                            data = br.readLine();
//
//                            len = Integer.parseInt(data.split(":")[1].trim());
//
//                            bis = new BufferedInputStream(in);
//                            buffer = new byte[len];
//
//                            int t = 0;
//                            while (t < len) {
//                                t += bis.read(buffer, t, len - t);
//                            }
//
//                            Bytes2ImageFile(buffer, getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/0A.jpg");
//
//                            final Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/0A.jpg");
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run()
//                                {
//                                    cameraFeed.setImageBitmap(bitmap);
//                                }
//                            });
//
//                        }
//
//                    }
//                }
//
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if (bis != null) {
//                    bis.close();
//                }
//                if (fos != null) {
//                    fos.close();
//                }
//
//                stream_handler.sendEmptyMessageDelayed(ID_CONNECT,3000);
//
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    private void Bytes2ImageFile(byte[] bytes, String fileName) {
//        try {
//            File file = new File(fileName);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(bytes, 0, bytes.length);
//            fos.flush();
//            fos.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
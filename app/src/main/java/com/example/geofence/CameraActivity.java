package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    // Check for network changes
    AlertDialog networkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Retrieve the IP
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            IP = extras.getString("IP");
        }

        cameraFeed = (WebView) findViewById(R.id.camera_monitor);
        backButton = (Button) findViewById(R.id.camera_back);
        reloadButton = (Button) findViewById(R.id.camera_reload);

        cameraFeed.setWebViewClient(new WebViewClient());
        cameraFeed.loadUrl("http://" + IP + ":81/stream");

        // Network Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this, R.style.AlertDialogTheme);
        View dialogView = LayoutInflater.from(CameraActivity.this).inflate(
                R.layout.dialog_information_layout_no_checkbox,
                null
        );
        builder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("No Network Found");
        ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("There was no network detected. Check your connection settings and try again.");
        ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
        ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Check Connection");

        builder.setCancelable(false);

        networkDialog = builder.create();

        dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        networkDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

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

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkReceiver);
        super.onStop();
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!Common.isConnectedToNetworkAndInternet(context)){
                networkDialog.show();
            }
            else{
                networkDialog.dismiss();
            }
        }
    };

    // Might not need to disable back button navigation
    @Override
    public void onBackPressed() {
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
}
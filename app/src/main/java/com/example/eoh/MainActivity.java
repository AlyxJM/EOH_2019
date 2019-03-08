package com.example.eoh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chibde.visualizer.CircleBarVisualizer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 2;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 4;

    // Default logging tag for messages from the main activity
    private static final String TAG = "EOH: Main";
    // Request queue for our network requests
    private static RequestQueue requestQueue;
    private int numRequests = 0;

    private MediaPlayer karaokeTrackPlayer;
    private MediaPlayer recordingPlayer;

    private CircleBarVisualizer circleBarVisualizer;
    private CircleBarVisualizer circleBarVisualizerRecord;

    private TextView lyricsTextView;

    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton recordButton;

    private MediaRecorder myAudioRecorder;
    private String outputFile;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        recordButton = findViewById(R.id.record);

        stopButton.setEnabled(false);

        askAllPermissions();

        circleBarVisualizer = findViewById(R.id.circleBarVisualizer);
        circleBarVisualizerRecord = findViewById(R.id.circleBarVisualizerRecord);

        // Set up a queue for our Volley requests
        requestQueue = Volley.newRequestQueue(this);
        lyricsTextView = findViewById(R.id.lyricTextView);
        startLyricsApiCall();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioRecorderSetUp();
                karaokeTrackSetUp();

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    isRecording = true;
                } catch (IllegalStateException ise) {
                    Toast.makeText(getApplicationContext(),
                            "Recording Failed: IllegalStateException ise",
                            Toast.LENGTH_LONG).show();

                } catch (IOException ioe) {
                    Toast.makeText(getApplicationContext(),
                            "Recording Failed: IOException ioe", Toast.LENGTH_LONG).show();
                }
                recordButton.setEnabled(false);
                playButton.setEnabled(false);
                stopButton.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started",
                        Toast.LENGTH_LONG).show();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (karaokeTrackPlayer != null) {
                        karaokeTrackPlayer.stop();
                    }

                    if (recordingPlayer != null) {
                        recordingPlayer.stop();
                    }

                    if (isRecording) {
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder = null;

                        Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Playback stopped",
                                Toast.LENGTH_LONG).show();
                    }

                    recordButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    playButton.setEnabled(true);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Stop Failed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isRecording = false;

                    karaokeTrackSetUp();

                    recordingPlayer = new MediaPlayer();
                    recordingPlayer.setDataSource(outputFile);
                    recordingPlayer.prepare();
                    recordingPlayer.start();

                    circleBarVisualizerRecord.setColor(ContextCompat.getColor(MainActivity.this,
                            R.color.colorAccent));
                    circleBarVisualizerRecord.setPlayer(recordingPlayer.getAudioSessionId());

                    playButton.setEnabled(false);
                    recordButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Playback", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Playback Failed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void askAllPermissions() {
        askSinglePermission(Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        askSinglePermission(Manifest.permission.INTERNET, MY_PERMISSIONS_REQUEST_INTERNET);
        askSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        askSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void askSinglePermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                // Show an explanation to the user about permission
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission}, requestCode);

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
                    Toast.makeText(getApplicationContext(),
                            "Record Audio Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Internet Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Write External Storage Permission Denied", Toast.LENGTH_LONG).show();
                }

                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Read External Storage Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void startLyricsApiCall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.lyrics.ovh/v1/Steve%20Aoki/Waste%20It%20On%20Me",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                if (response.get("lyrics").toString().equals("No lyrics found")
                                        && numRequests < 10) {
                                    numRequests++;
                                    startLyricsApiCall();
                                } else {
                                    lyricsTextView.setText(response.get("lyrics").toString());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Problem parsing JSON", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            }) {
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void audioRecorderSetUp() {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }

    private void karaokeTrackSetUp() {
        karaokeTrackPlayer = MediaPlayer.create(MainActivity.this, R.raw.waste_it_on_me);
        karaokeTrackPlayer.start();

        circleBarVisualizer.setColor(ContextCompat.getColor(MainActivity.this,
                R.color.colorPrimary));
        circleBarVisualizer.setPlayer(karaokeTrackPlayer.getAudioSessionId());
    }

}

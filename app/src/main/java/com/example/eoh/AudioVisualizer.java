package com.example.eoh;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.chibde.visualizer.CircleBarVisualizer;

public class AudioVisualizer extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiovisualizer);

        CircleBarVisualizer circleBarVisualizer = findViewById(R.id.circleBarVisual);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.thank_you_next);

        circleBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
    }
}

package com.yiriba.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button play_btn;
    Button pause_btn;
    Button ff_btn;
    Button replay_btn;
    TextView song_title;
    TextView time;
    ImageView image_logo;
    SeekBar seekbar;

    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        play_btn = findViewById(R.id.play_btn);
        pause_btn =findViewById(R.id.pause_btn);
        ff_btn = findViewById(R.id.forward_btn);
        replay_btn = findViewById(R.id.back_btn);
        song_title = findViewById(R.id.tv_song_title);
        time = findViewById(R.id.time);
        image_logo  = findViewById(R.id.iv);
        seekbar = findViewById(R.id.seekBar);

        mediaPlayer = mediaPlayer.create(this,
                R.raw.astronaut);
        song_title.setText(getResources()
                .getIdentifier("astronaut","raw", getPackageName()));
        seekbar.setClickable(false);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        ff_btn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Jump Forward!", Toast.LENGTH_SHORT);
                }

            }
        });

        replay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime-= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(getApplicationContext(), "Can't Go Back!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        seekbar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    // Create the Runnable

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

            seekbar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };
}
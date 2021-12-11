package com.micah.beatful.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.micah.beatful.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class BeatPlayer extends AppCompatActivity {

    Button btnPlay,btnNext,btnPrevious,btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar seekMusicBar;
    ImageView imageView;
    String songName;

    static MediaPlayer mediaPlayer;
    int i;
    ArrayList<File> mySongs;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    Thread updateSeekBar;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences_001", MODE_PRIVATE);
        if (Objects.equals(prefs.getString("textStyle", "normal"), "normal")) {
            setTheme(R.style.Theme_BeatfulPlayer);
        } else {
            setTheme(R.style.Theme_BeatfulPlayer_ButDiff);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beat_player);

        try{
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.d("BeatPlayer: ", Objects.requireNonNull(e.getLocalizedMessage()));
        }

        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnFastForward = findViewById(R.id.btnFastForward);
        btnFastBackward = findViewById(R.id.btnFastBackward);

        txtSongName = findViewById(R.id.txtSong);
        txtSongStart = findViewById(R.id.txtSongStart);
        txtSongEnd = findViewById(R.id.txtSongEnd);

        seekMusicBar = findViewById(R.id.seekBar);

        imageView = findViewById(R.id.imgView);

        if (mediaPlayer != null)
        {
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle;
        bundle = intent.getExtras();


        mySongs = (ArrayList)bundle.getParcelableArrayList("songs");
        String sName = intent.getStringExtra("songname");
        i = bundle.getInt("pos", 0);
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(i).toString());
        songName = mySongs.get(i).getName();
        txtSongName.setText(songName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();


        updateSeekBar = new Thread()
        {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekMusicBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        };

        seekMusicBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusicBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        seekMusicBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(currentTime);
                handler.postDelayed(this,delay);

            }
        },delay);


        btnPlay.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying())
            {
                btnPlay.setBackgroundResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }
            else
            {
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
                mediaPlayer.start();

                TranslateAnimation moveAnim = new TranslateAnimation(-25,25,-25,25);
                moveAnim.setInterpolator(new AccelerateInterpolator());
                moveAnim.setDuration(600);
                moveAnim.setFillEnabled(true);
                moveAnim.setFillAfter(true);
                moveAnim.setRepeatMode(Animation.REVERSE);
                moveAnim.setRepeatCount(1);
                imageView.startAnimation(moveAnim);
            }
        });


        btnNext.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            i =((i+1)%mySongs.size());
            Uri uri1 = Uri.parse(mySongs.get(i).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            songName = mySongs.get(i).getName();
            txtSongName.setText(songName);
            mediaPlayer.start();

            startAnimation(imageView,360f);
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> btnNext.performClick());

        btnPrevious.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            i = ((i-1)<0)?(mySongs.size()-1):i-1;
            Uri uri12 = Uri.parse(mySongs.get(i).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri12);
            songName = mySongs.get(i).getName();
            txtSongName.setText(songName);
            mediaPlayer.start();

            startAnimation(imageView,-360f);
        });

        btnFastForward.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+1000);
            }
        });

        btnFastBackward.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-1000);
            }
        });

    }

    public void startAnimation(View view,Float degree)
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0f,degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time = time+min+":";
        if (sec<10)
        {
            time+="0";
        }
        time+=sec;
        return time;
    }
}

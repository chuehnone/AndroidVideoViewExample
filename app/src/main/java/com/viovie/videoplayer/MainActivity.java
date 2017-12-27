package com.viovie.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ImageView mControlView;
    private SeekBar mSeekBar;
    private VideoView mVideoView;
    private int mCurrentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mControlView = findViewById(R.id.control_view);
        mVideoView = findViewById(R.id.video_view);
        mSeekBar = findViewById(R.id.seek_bar);
        mCurrentProgress = 0;

        mControlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    pauseVideo();
                } else {
                    playVideo();
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseVideo();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentProgress = seekBar.getProgress();
                mVideoView.seekTo(mCurrentProgress);
                playVideo();
            }
        });

        String videoUrl = "http://mp4.hiroia.com/12_9_16_10_12A83DF5-F194-48D3-99CC-E0521EC48E58/hiroia_video.mp4";
        mVideoView.setVideoURI(Uri.parse(videoUrl));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mSeekBar.setMax(mVideoView.getDuration());
                playVideo();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mCurrentProgress = 0;
                mVideoView.seekTo(mCurrentProgress);
                mSeekBar.setProgress(mCurrentProgress);
                pauseVideo();
            }
        });
    }

    private void playVideo() {
        mVideoView.start();
        mControlView.setImageResource(android.R.drawable.ic_media_pause);
        mHandler.postDelayed(updateSeekBar, 100);
    }

    private void pauseVideo() {
        mVideoView.pause();
        mControlView.setImageResource(android.R.drawable.ic_media_play);
        mHandler.removeCallbacks(updateSeekBar);
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            mCurrentProgress = mVideoView.getCurrentPosition();
            mSeekBar.setProgress(mCurrentProgress);
            if (mVideoView.isPlaying()) {
                mHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.seekTo(mCurrentProgress);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

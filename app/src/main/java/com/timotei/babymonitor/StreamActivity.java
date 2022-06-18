package com.timotei.babymonitor;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.timotei.babymonitor.databinding.ActivityStreamBinding;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

public class StreamActivity extends AppCompatActivity {

    private ActivityStreamBinding binding;
    private SharedPreferences sharedPreferences;
    private String url;
    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;


    // code for starting VLC stream taken from https://lindevs.com/display-rtsp-stream-from-ip-camera-using-libvlc-on-android/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding=ActivityStreamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get IP address for constructing url
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip=sharedPreferences.getString("IpAddress","none");

        url="rtsp://"+ip+":8554/unicast";
        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = binding.videoLayout;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //attach video layout to media player
        mediaPlayer.attachViews(videoLayout, null, false, false);

        Media media = new Media(libVlc, Uri.parse(url));
        media.setHWDecoderEnabled(true, false); // use GPU for rendering this view
        media.addOption(":network-caching=500"); // minimize to lower the delay

        mediaPlayer.setMedia(media);
        media.release();
        Toast.makeText(this,"Loading video stream...",Toast.LENGTH_LONG).show();

        mediaPlayer.play();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.release();
        libVlc.release();
    }

}

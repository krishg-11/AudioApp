package traf1.ganotrakrish.audioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.pm.PackageManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //https://www.techotopia.com/index.php/An_Android_Studio_Recording_and_Playback_Example_using_MediaPlayer_and_MediaRecorder
        //https://developer.android.com/guide/topics/media/mediarecorder#java
        //https://firebase.google.com/docs/storage/android/upload-files (upload from stream or upload from file)
        //https://www.tutorialspoint.com/how-to-integrate-android-speech-to-text (no audio, just speech-to-text)
        //https://www.androidhive.info/2014/07/android-speech-to-text-tutorial/ (pretty version of ^^)
        //use firebase storage for mp3 storage


        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource("AUDIO SOURCE");
            mediaPlayer.prepare();
            mediaPlayer.start();
            System.out.println("playing audio");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        System.out.println("Microphone access: " + hasMicrophone());

    }
     boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
}

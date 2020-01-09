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
    }
    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
}

package yildiz.edu.tr.onur.aydemir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Menu;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView imgOpener;
    public static String firstname, lastname;
    public static int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Onur AYDEMIR 16011039");

        imgOpener = findViewById(R.id.imageOpener);

        getSupportActionBar().hide();
        imgOpener.setImageResource(R.drawable.openerscreen);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }, 1000);


    }
}
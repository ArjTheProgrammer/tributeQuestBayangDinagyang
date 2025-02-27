package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OurTeam extends AppCompatActivity {

    private ImageView ourTeam;
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_our_team);

        ourTeam = findViewById(R.id.sign_ourteam);

        sound = Sound.getInstance(this);
    }

    public void toNext(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, OurTeam2.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toPrev(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, OurTeam3.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }
}
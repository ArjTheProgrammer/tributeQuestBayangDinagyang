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

public class OurTeam2 extends AppCompatActivity {
    private ImageView ourTeam;
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_our_team2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ourTeam = findViewById(R.id.sign_ourteam);

        sound = Sound.getInstance(this);
    }

    public void toNext(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, OurTeam3.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toPrev(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, OurTeam.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }
}
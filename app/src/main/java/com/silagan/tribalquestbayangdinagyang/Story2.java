package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Story2 extends AppCompatActivity {

    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story2);

        sound = Sound.getInstance(this);
    }

    public void toSkip(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, Mechanics.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toPrev(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, Story1.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toNext(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, Story3.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }
}
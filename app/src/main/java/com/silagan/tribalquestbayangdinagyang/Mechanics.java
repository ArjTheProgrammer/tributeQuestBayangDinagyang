package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Mechanics extends AppCompatActivity {
    ImageView ig_mechanics;
    private int[] images = {R.drawable.mech_1, R.drawable.mech_2, R.drawable.mech_3};
    private int currentIndex = 0;
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanics);

        sound = Sound.getInstance(this);
        ig_mechanics = (ImageView)  findViewById(R.id.ig_mechanics);
    }

    public void toNext(View view) {
        sound.playButtonClickSound();
        if ( currentIndex == images.length - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        ig_mechanics.setImageResource(images[currentIndex]);
    }

    public void toPrev(View view) {
        sound.playButtonClickSound();
        if (currentIndex == 0) {
            currentIndex = 2;
        } else {
            currentIndex--;
        }
        ig_mechanics.setImageResource(images[currentIndex]);
    }

    public void toStory(View view) {
        sound.playButtonClickSound();
        Intent i = new Intent(this, Story3.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toGame(View view) {
        sound.playButtonClickSound();
        sound.stopAllMusic();
        CollectGame collectGame = new CollectGame(this);
        setContentView(collectGame);
    }
}
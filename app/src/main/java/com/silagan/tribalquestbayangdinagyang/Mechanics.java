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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ig_mechanics = (ImageView)  findViewById(R.id.ig_mechanics);
    }

    public void toNext(View view) {
        if ( currentIndex == images.length - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        ig_mechanics.setImageResource(images[currentIndex]);
    }

    public void toPrev(View view) {
        if (currentIndex == 0) {
            currentIndex = 2;
        } else {
            currentIndex--;
        }
        ig_mechanics.setImageResource(images[currentIndex]);
    }

    public void toStory(View view) {
        Intent i = new Intent(this, Story3.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }
}
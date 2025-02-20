package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView title;
    private ImageView start;
    private ImageView about;
    private ImageView exit;
    private ImageView miniGame;
    private Animation floatAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.title);
        start = findViewById(R.id.start_button);
        about = findViewById(R.id.about_button);
        miniGame = findViewById(R.id.minigame_button);
        exit = findViewById(R.id.exit_button);

        miniGame.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_button));
    }

    public void toStart(View view) {
        Intent intent = new Intent(this, Story1.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void toAbout(View view) {
        Intent i = new Intent(this, About.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void toExit(View view) {
        finishAffinity();
    }

    public void toMiniGame(View view) {
        Intent i = new Intent(this, CardGame.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }
}
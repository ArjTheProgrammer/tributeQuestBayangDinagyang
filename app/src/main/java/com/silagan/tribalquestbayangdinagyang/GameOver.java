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

public class GameOver extends AppCompatActivity {

    private ImageView gameOver;

    private ImageView tryAgain;
    private ImageView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gameOver = findViewById(R.id.sign_gameover);
        tryAgain = findViewById(R.id.try_again);
        main = findViewById(R.id.toMain);

        gameOver.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swinging_anim));
    }

    public void toCollectGame(View view) {
        CollectGame collectGame = new CollectGame(this);
        setContentView(collectGame);
    }

    public void toMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
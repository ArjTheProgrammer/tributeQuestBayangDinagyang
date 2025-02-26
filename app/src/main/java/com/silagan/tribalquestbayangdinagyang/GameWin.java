package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class GameWin extends AppCompatActivity {

    TextView score;
    TextView highScore;
    ImageView playAgain;
    ImageView main;
    RelativeLayout gameWin;
    SharedPreferences sharedPreferences;

    private Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_win);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        score = findViewById(R.id.score);
        highScore = findViewById(R.id.highScore);
        gameWin = findViewById(R.id.sign_winner);
        playAgain = findViewById(R.id.play_again);
        main = findViewById(R.id.main_button);

        // Retrieve the score passed from the GameView
        int points = Objects.requireNonNull(getIntent().getExtras()).getInt("points", 0);
        gameWin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_up_anim));

        // Display the current score
        score.setText(String.valueOf(points)); // Convert int to String

        // Load high score from SharedPreferences
        sharedPreferences = getSharedPreferences("data", 0);
        int highest = sharedPreferences.getInt("highest", 0);

        // Update high score if necessary
        if (points > highest) {
            highest = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", highest);
            editor.apply();
        }

        // Display the high score
        highScore.setText(String.valueOf(highest)); // Convert int to String

        sound = Sound.getInstance(this);
        sound.playMainGameGWSounds();

    }

    public void toCollectGame(View view) {
        CollectGame collectGame = new CollectGame(this);
        setContentView(collectGame);
    }

    public void toMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
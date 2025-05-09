package com.silagan.tribalquestbayangdinagyang;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class CardGame extends AppCompatActivity implements View.OnClickListener {
    TextView tv_status, tv_timer, tv_finalscore, tv_highscore, tv_goScore;
    ImageView iv_pause, iv_playagain, iv_main, iv_goPlayagain, iv_goMain;
    private CountDownTimer timerCountdown;
    private boolean gameStarted = false;
    private boolean gameCompleted = false;
    private long timeRemaining;
    private int totalPairs;
    private ImageView resumeButton;
    private ImageView restartButton;
    private ImageView quitButton;

    private ImageView gameOver;
    private RelativeLayout pauseOverlay, gameWinOverlay, gameOverOverlay;
    private boolean isPaused = false;
    private boolean isWaitingForMismatch = false;

    private final int[] iv_CardIds = {
            R.id.iv_Card0, R.id.iv_Card1, R.id.iv_Card2, R.id.iv_Card3,
            R.id.iv_Card4, R.id.iv_Card5, R.id.iv_Card6, R.id.iv_Card7,
            R.id.iv_Card8, R.id.iv_Card9, R.id.iv_Card10, R.id.iv_Card11,
            R.id.iv_Card12, R.id.iv_Card13, R.id.iv_Card14, R.id.iv_Card15
    };

    private final int[] drawableCardIds = {
            R.drawable.card_1, R.drawable.card_2, R.drawable.card_3, R.drawable.card_4,
            R.drawable.card_5, R.drawable.card_6, R.drawable.card_7, R.drawable.card_8
    };

    private Sound sound;

    private void revealAndShuffleCards() {
        ArrayList<Integer> shuffledDrawableIds = new ArrayList<>();
        for (int drawableId : drawableCardIds) {
            shuffledDrawableIds.add(drawableId);
            shuffledDrawableIds.add(drawableId);
        }
        Collections.shuffle(shuffledDrawableIds);

        // Reveal all cards for 3 seconds
        for (int i = 0; i < iv_CardIds.length; i++) {
            ImageView imageView = findViewById(iv_CardIds[i]);
            imageView.setImageResource(shuffledDrawableIds.get(i));

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 0.7f, 1.2f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 0.7f, 1.2f, 1f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY);
            animatorSet.setDuration(300);
            animatorSet.setStartDelay(i * 80); // Staggered effect
            animatorSet.start();
        }

        new Handler().postDelayed(() -> {
            for (int i = 0; i < iv_CardIds.length; i++) {
                ImageView imageView = findViewById(iv_CardIds[i]);
                int drawableId = shuffledDrawableIds.get(i);
                CardInfo cardInfo = new CardInfo(iv_CardIds[i], drawableId);
                imageView.setImageResource(R.drawable.card_flipped);
                imageView.setTag(cardInfo);
                imageView.setOnClickListener(this);
                imageView.setEnabled(true);
            }

            startGameTimer();
        }, 1500); // Wait for 1.5 seconds before shuffling and hiding
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_game);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Initialize all the views
        tv_status = findViewById(R.id.tv_status);
        tv_timer = findViewById(R.id.tv_timer);
        iv_pause = findViewById(R.id.iv_pause);

        pauseOverlay = findViewById(R.id.pauseOverlay);
        gameWinOverlay = findViewById(R.id.gameWinOverlay);
        gameOverOverlay = findViewById(R.id.gameOverOverlay);
        tv_goScore = findViewById(R.id.tv_goScore);

        // Initialize the overlay buttons
        resumeButton = findViewById(R.id.resumeButton);
        restartButton = findViewById(R.id.restartButton);
        quitButton = findViewById(R.id.quitButton);
        iv_playagain = findViewById(R.id.iv_playagain);
        iv_main = findViewById(R.id.iv_main);
        iv_goPlayagain = findViewById(R.id.iv_goPlayagain);
        iv_goMain = findViewById(R.id.iv_goMain);
        gameOver = findViewById(R.id.sign_gameover);

        gameOver.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swinging_anim));


        // Hide overlays initially
        pauseOverlay.setVisibility(View.GONE);
        gameWinOverlay.setVisibility(View.GONE);

        sound = Sound.getInstance(this);
        sound.playMinigameMusic();

        resumeButton.setOnClickListener(v -> {
            iv_pause.setVisibility(View.VISIBLE);
            sound.playButtonClickSound();
            sound.playMinigameMusic();

            if (isPaused && gameStarted) {
                resumeGame();
                startGameTimer();
            }
        });

        restartButton.setOnClickListener(v -> {
            iv_pause.setVisibility(View.VISIBLE);
            sound.playButtonClickSound();
            // Remove the isPaused check to allow restart at any time
            resetGame();
            revealAndShuffleCards();
            sound.resumeAll();
            sound.stopAllMusic();
            sound.playMinigameMusic();
            if (isPaused) {
                resumeGame();
            }
        });

        quitButton.setOnClickListener(v -> {
            iv_pause.setVisibility(View.VISIBLE);
            sound.playButtonClickSound();
            sound.stopAllMusic();
            sound.playMainAppMusic();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
        });

        iv_pause.setOnClickListener(v -> {
            sound.playButtonClickSound();
            sound.pauseAll();
            iv_pause.setVisibility(View.INVISIBLE);

            if (isPaused) {
                resumeGame();
            } else {
                pauseGame();
            }
        });

        iv_playagain.setOnClickListener(view -> {
            sound.playButtonClickSound();
            sound.stopAllMusic();
            sound.playMinigameMusic();
            gameWinOverlay.setVisibility(View.GONE);
            resetGame();
            revealAndShuffleCards();
            enableAllCardClicks();
        });

        iv_main.setOnClickListener(view -> {
            sound.playButtonClickSound();
            sound.stopAllMusic();
            sound.playMainAppMusic();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
            startActivity(intent);
            overridePendingTransition(0,0);
        });

        iv_goPlayagain.setOnClickListener(view -> {
            sound.playButtonClickSound();
            sound.stopAllMusic();
            sound.playMinigameMusic();
            gameOverOverlay.setVisibility(View.GONE);
            resetGame();
            revealAndShuffleCards();
        });

        iv_goMain.setOnClickListener(view -> {
            sound.playButtonClickSound();
            sound.stopAllMusic();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

        // Auto-start the game immediately
        new Handler().postDelayed(() -> {
            resetGame();
            revealAndShuffleCards();
        }, 500); // Short delay to ensure everything is loaded
    }

    private void resetGame() {
        if (timerCountdown != null) {
            timerCountdown.cancel();
        }

        // Re-enable all game controls
        iv_pause.setEnabled(true);

        tv_timer.setText("25 SECS");
        totalPairs = iv_CardIds.length / 2;
        matchCount = 0;
        gameCompleted = false;
        gameStarted = true;
        isWaitingForMismatch = false;
        cardInfo1 = null;
        isPaused = false; // Reset pause state
        timeRemaining = 26000; // Reset time remaining

        tv_status.setText("0");

        // Hide all overlays
        pauseOverlay.setVisibility(View.GONE);
        gameWinOverlay.setVisibility(View.GONE);
        gameOverOverlay.setVisibility(View.GONE);

        for (int id : iv_CardIds) {
            ImageView card = findViewById(id);
            if (card != null) {
                card.setEnabled(true);
            }
        }
    }

    private void startGameTimer() {
        if (timerCountdown != null) {
            timerCountdown.cancel();
        }
        timerCountdown = new CountDownTimer(timeRemaining > 0 ? timeRemaining : 26000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                tv_timer.setText("" + millisUntilFinished / 1000 + " SECS");
            }

            @Override
            public void onFinish() {
                if (!gameCompleted) {
                    disableGame();
                    gameCompleted = true; // Mark the game as completed
                    iv_pause.setEnabled(false); // Disable pause button when game is over
                    gameStarted = false;
                    gameOver(); // Show game over when time runs out
                }
            }
        }.start();
    }

    private void resumeGame() {
        if (isPaused && timeRemaining > 0) {

            pauseOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            pauseOverlay.setVisibility(View.GONE);
                            startGameTimer();
                            isPaused = false;
                        }
                    });
        }
    }

    private void pauseGame() {
        if (timerCountdown != null) {
            timerCountdown.cancel();
        }
        isPaused = true;

        pauseOverlay.setAlpha(0f);
        pauseOverlay.setVisibility(View.VISIBLE);
        pauseOverlay.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);
    }

    private void disableGame() {
        for (int id : iv_CardIds) {
            findViewById(id).setOnClickListener(null);
        }
    }

    private void gameCompletion() {
        if (matchCount == totalPairs) {
            gameCompleted = true;
            if (timerCountdown != null) {
                timerCountdown.cancel();
            }
            gameWin(); // Show game win when all cards are matched
        }
    }

    private void gameWin() {
        // Stop the timer if it's still running
        if (timerCountdown != null) {
            timerCountdown.cancel();
        }

        // Show the game win overlay with animation
        gameWinOverlay.setVisibility(View.VISIBLE);
        gameWinOverlay.setAlpha(0f);
        gameWinOverlay.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);

        Sound.getInstance(this).playMiniGameGWSounds();
    }

    private void gameOver() {
        if (timerCountdown != null) {
            timerCountdown.cancel();
        }

        // Display match count instead of score
        tv_goScore.setText(String.valueOf(matchCount));

        // Check if no matches were made to apply the slide-up animation
        if (matchCount == 0) {
            // Load the slide-up animation
            Animation slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);

            // Add animation listener to detect when animation ends
            slideUpAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // No action needed on start
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Clear the animation after it completes
                    gameOverOverlay.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // No action needed on repeat
                }
            });

            // Make overlay visible
            gameOverOverlay.setVisibility(View.VISIBLE);

            // Start the animation
            gameOverOverlay.startAnimation(slideUpAnim);
        } else {
            // Use the existing fade-in animation for non-zero matches
            gameOverOverlay.setVisibility(View.VISIBLE);
            gameOverOverlay.setAlpha(0f);
            gameOverOverlay.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(null);
        }

        Sound.getInstance(this).playMiniGameGOSounds();
    }

    CardInfo cardInfo1 = null;
    int matchCount = 0;

    @Override
    public void onClick(View v) {
        // If we're waiting for animations or game is paused, ignore clicks
        if (isWaitingForMismatch || isPaused) return;

        ImageView imageView = (ImageView) v;
        CardInfo cardInfo = (CardInfo) imageView.getTag();

        // Prevent clicking already matched or clicked cards
        if (cardInfo.isMatched() || cardInfo.isFlipped()) {
            return;
        }

        // Count currently clicked cards before processing new click
        int clickedCards = 0;
        for (int id : iv_CardIds) {
            ImageView card = findViewById(id);
            CardInfo info = (CardInfo) card.getTag();
            if (info.isFlipped() && !info.isMatched()) {
                clickedCards++;
            }
        }

        // Strictly enforce two-card limit
        if (clickedCards >= 2) {
            return;
        }

        // Mark card as flipped and show it
        cardInfo.setFlipped(true);
        flipCard(imageView, cardInfo.getDrawableId(), null);

        // First card click
        if (cardInfo1 == null) {
            cardInfo1 = cardInfo;
        }
        // Second card click - process match/mismatch
        else {
            // After second card is flipped, disable all cards
            isWaitingForMismatch = true;
            disableAllCardClicks();

            // Check for match AFTER both cards are fully flipped
            new Handler().postDelayed(() -> {
                if (cardInfo.getDrawableId() == cardInfo1.getDrawableId()) {
                    // Match found
                    matchCount++;
                    cardInfo1.setMatched(true);
                    cardInfo.setMatched(true);
                    tv_status.setText("" + matchCount);
                    matchAnimation(imageView, findViewById(cardInfo1.getImageViewId()));

                    // Reset for next pair
                    new Handler().postDelayed(() -> {
                        cardInfo1 = null;
                        isWaitingForMismatch = false;
                        enableAllCardClicks();
                    }, 500);

                    // Check if game is complete
                    gameCompletion();
                } else {
                    // No match - animate and then flip cards back
                    mismatchAnimation(imageView, findViewById(cardInfo1.getImageViewId()));

                    new Handler().postDelayed(() -> {
                        flipCard(imageView, R.drawable.card_flipped, null);
                        flipCard(findViewById(cardInfo1.getImageViewId()), R.drawable.card_flipped, null);
                        cardInfo.setFlipped(false);
                        cardInfo1.setFlipped(false);
                        cardInfo1 = null;
                        isWaitingForMismatch = false;
                        enableAllCardClicks();
                    }, 700);
                }
            }, 250); // Short delay to ensure second card is fully visible
        }
    }

    // Add these new methods to your MainActivity class:
    private void disableAllCardClicks() {
        for (int id : iv_CardIds) {
            ImageView card = findViewById(id);
            if (card != null) {
                card.setEnabled(false);
            }
        }
    }

    private void enableAllCardClicks() {
        for (int id : iv_CardIds) {
            ImageView card = findViewById(id);
            CardInfo info = (CardInfo) card.getTag();
            if (card != null && (info == null || !info.isMatched())) {
                card.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (gameWinOverlay.getVisibility() == View.VISIBLE) {
            // If game win screen is visible, hide it and reset the game
            gameWinOverlay.setVisibility(View.GONE);
            resetGame();
        } else if (isPaused) {
            resumeGame();
        } else if (gameStarted && !gameCompleted) {
            pauseGame();
        } else if (gameStarted) {
            resetGame();
        } else {
            super.onBackPressed();
        }
    }

    private void flipCard(final ImageView card, final int newImageResId, final Runnable onEnd) {
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1f);

        flipOut.setDuration(200);
        flipIn.setDuration(200);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                card.setImageResource(newImageResId);
                flipIn.start();
            }
        });

        flipIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onEnd != null) {
                    onEnd.run();
                }
            }
        });

        flipOut.start();
    }

    private void matchAnimation(View card1, View card2) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(card1, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(card1, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(card2, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(card2, "scaleY", 1f, 1.2f, 1f);

        animatorSet.playTogether(scaleX, scaleY, scaleX2, scaleY2);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void mismatchAnimation(View card1, View card2) {
        ObjectAnimator shake1 = ObjectAnimator.ofFloat(card1, "translationX", 0, 10, -10, 10, -10, 0);
        ObjectAnimator shake2 = ObjectAnimator.ofFloat(card2, "translationX", 0, 10, -10, 10, -10, 0);

        shake1.setDuration(300);
        shake2.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(shake1, shake2);
        animatorSet.start();
    }
}
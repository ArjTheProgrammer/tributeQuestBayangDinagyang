package com.silagan.tribalquestbayangdinagyang;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class CollectGame extends View {

    Bitmap background, ground, person;
    private Bitmap timeScoreBackground;
    Rect rectBackground, rectGround, rectTimer;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint timerPaint = new Paint();
    float TEXT_SIZE = 60;
    int points = 0;
    static int dWidth, dHeight;
    Random random;
    float personX, personY;
    float oldX;
    float oldPersonX;
    ArrayList<Bomb> bombs;
    ArrayList<Item> items;
    ArrayList<Item> preallocatedItems;
    ArrayList<Explosion> explosions;
    ArrayList<Collect> collects;
    long startTime;
    int gameDuration = 60; // Game lasts 60 seconds
    boolean isGameOver;

    int scoreBgWidth = 464;
    int scoreBgHeight = 225;
    private Bitmap pauseButton, continueButton, restartButton, exitButton, blurredBackground;
    private boolean isPaused = false;
    private Paint blurPaint;
    private Rect pauseButtonRect;
    private Rect continueButtonRect, restartButtonRect, exitButtonRect;
    private long pauseStartTime = 0;
    private long totalPausedTime = 0;

    Intent toGameOver;
    Intent toGameWin;

    boolean bombCollected;

    public CollectGame(Context context) {
        super(context);
        this.context = context;
        isGameOver = false;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_game);
        ground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.game_sand), 720, 247, true);
        Bitmap origPerson = BitmapFactory.decodeResource(getResources(), R.drawable.character);
        person = Bitmap.createScaledBitmap(origPerson, (int)(origPerson.getWidth() / 2), origPerson.getHeight() / 2, true);

        timeScoreBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.game_time_score), scoreBgWidth, scoreBgHeight, true);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        rectTimer = new Rect(20, 50 + scoreBgHeight + 10, 20 + scoreBgWidth, 20 + scoreBgHeight + 10 + scoreBgHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    invalidate();
                }
            }
        };

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.sigher));

        timerPaint.setColor(Color.WHITE);
        timerPaint.setTextSize(TEXT_SIZE + 5);
        timerPaint.setTextAlign(Paint.Align.LEFT);
        timerPaint.setTypeface(ResourcesCompat.getFont(context, R.font.sigher));

        // Load button images
        pauseButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_pause);
        continueButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_pause_resume);
        restartButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_pause_restart);
        exitButton = BitmapFactory.decodeResource(getResources(), R.drawable.button_pause_quit);

        // Scale buttons to desired size
        pauseButton = Bitmap.createScaledBitmap(pauseButton, 170, 170, true);
        continueButton = Bitmap.createScaledBitmap(continueButton, 487, 170, true);
        restartButton = Bitmap.createScaledBitmap(restartButton, 487, 170, true);
        exitButton = Bitmap.createScaledBitmap(exitButton, 487, 170, true);

        // Initialize button positions
        pauseButtonRect = new Rect(dWidth - pauseButton.getWidth() - 50, 140, dWidth - 50, 140 + pauseButton.getWidth());

        // Center menu buttons
        int centerX = dWidth / 2 - continueButton.getWidth() / 2;
        int centerY = dHeight / 2 - continueButton.getHeight() - 100;
        continueButtonRect = new Rect(centerX, centerY, centerX + continueButton.getWidth(), centerY + continueButton.getHeight());
        restartButtonRect = new Rect(centerX, centerY + restartButton.getHeight() + 40, centerX + restartButton.getWidth(), centerY + 2 * restartButton.getHeight() + 40);
        exitButtonRect = new Rect(centerX, centerY + 2 * exitButton.getHeight() + 80, centerX + exitButton.getWidth(), centerY + 3 * exitButton.getHeight() + 80);

        // Initialize blur paint
        blurPaint = new Paint();
        blurPaint.setColor(Color.argb(180, 0, 0, 0));

        random = new Random();
        personX = (float) dWidth / 2 - (float) person.getWidth() / 2;
        personY = dHeight - ground.getHeight() - person.getHeight() + 100;
        bombs = new ArrayList<>();
        items = new ArrayList<>();
        preallocatedItems = new ArrayList<>();
        explosions = new ArrayList<>();
        collects = new ArrayList<>();

        preallocatedItems.add(new Item(context, R.drawable.fabric, 1)); //0
        preallocatedItems.add(new Item(context, R.drawable.fabric, 1)); //1
        preallocatedItems.add(new Item(context, R.drawable.fabric, 1)); //2
        preallocatedItems.add(new Item(context, R.drawable.fabric, 1)); //3
        preallocatedItems.add(new Item(context, R.drawable.pearl_necklace, 3)); //4
        preallocatedItems.add(new Item(context, R.drawable.pearl_necklace, 3)); //5
        preallocatedItems.add(new Item(context, R.drawable.pearl_necklace, 3)); //6
        preallocatedItems.add(new Item(context, R.drawable.trash1, -2));//7
        preallocatedItems.add(new Item(context, R.drawable.trash2, -2));//8
        preallocatedItems.add(new Item(context, R.drawable.trash3, -2));//9
        preallocatedItems.add(new Item(context, R.drawable.trash2, -2));//10
        preallocatedItems.add(new Item(context, R.drawable.golden_salakot, 5)); //11
        preallocatedItems.add(new Item(context, R.drawable.golden_salakot, 5)); //12
        preallocatedItems.add(new Item(context, R.drawable.golden_necklace, 10));//13

        bombCollected = false;

        toGameOver = new Intent(context, GameOver.class);
        toGameWin = new Intent(context, GameWin.class);

        startTime = SystemClock.elapsedRealtime();
    }

    // Add method to create blurred background
    private void createBlurredBackground(Canvas canvas) {
        if (blurredBackground == null) {
            blurredBackground = Bitmap.createBitmap(dWidth, dHeight, Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(blurredBackground);

            // Draw current game state to temp canvas
            tempCanvas.drawBitmap(background, null, rectBackground, null);
            tempCanvas.drawBitmap(ground, null, rectGround, null);
            tempCanvas.drawBitmap(person, personX, personY, null);

            // Draw items and bombs
            for (Item item : items) {
                item.draw(tempCanvas);
            }
            for (Bomb bomb : bombs) {
                tempCanvas.drawBitmap(bomb.getBomb(), bomb.bombX, bomb.bombY, null);
            }
        }
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!isPaused) {
            // Calculate elapsed time accounting for paused time
            long elapsedTime = (SystemClock.elapsedRealtime() - startTime - totalPausedTime) / 1000;
            int remainingTime = Math.max(0, gameDuration - (int) elapsedTime);

            // Normal game drawing
            canvas.drawBitmap(background, null, rectBackground, null);
            canvas.drawBitmap(ground, null, rectGround, null);
            canvas.drawBitmap(person, personX, personY, null);

            //Increase items over time
            if (elapsedTime == 1) {
                if (items.isEmpty()) {
                    items.add(preallocatedItems.get(0)); // Feather
                }
            }

            if (elapsedTime == 2) {
                if (items.size() < 2) {
                    items.add(preallocatedItems.get(1)); // Feather
                }
            }

            if (elapsedTime == 3) {
                if (items.size() < 3) {
                    items.add(preallocatedItems.get(4)); // Necklace
                }
            }

            if (elapsedTime == 4) {
                if (items.size() < 4) {
                    items.add(preallocatedItems.get(7)); // Bottle
                }
            }

            if (elapsedTime == 10) {
                if (items.size() < 5) {
                    items.add(preallocatedItems.get(2)); // Feather
                }
            }

            if (elapsedTime == 11) {
                if (items.size() < 6) {
                    items.add(preallocatedItems.get(3)); // Feather
                }
            }

            if (elapsedTime == 12) {
                if (items.size() < 7) {
                    items.add(preallocatedItems.get(5)); // Necklace
                }
            }

            if (elapsedTime == 13) {
                if (items.size() < 8) {
                    items.add(preallocatedItems.get(6)); // Necklace
                }
            }

            if (elapsedTime == 14) {
                if (items.size() < 9) {
                    items.add(preallocatedItems.get(8)); // Bottle
                }
            }

            if (elapsedTime == 20) {
                if (items.size() < 10) {
                    items.add(preallocatedItems.get(11)); // Sword
                }
            }

            if (elapsedTime == 21) {
                if (items.size() < 11) {
                    items.add(preallocatedItems.get(9)); // Bottle
                }
            }

            if (elapsedTime == 22) {
                if (items.size() < 12) {
                    items.add(preallocatedItems.get(10)); // Bottle
                }
            }

            if (elapsedTime == 30) {
                if (items.size() < 13) {
                    items.add(preallocatedItems.get(12)); // Sword
                }
            }

            if (elapsedTime == 40) {
                if (items.size() < 14) {
                    items.add(preallocatedItems.get(13)); // Diamond
                }
            }

            // Increase bombs over time
            if (elapsedTime == 20) {
                if (bombs.isEmpty()) {
                    bombs.add(new Bomb(context)); // Add the first bomb at 20 seconds
                }
            } else if (elapsedTime > 20 && (elapsedTime - 20) % 10 == 0) {
                if (bombs.size() < (elapsedTime - 20) / 10 + 1) {
                    bombs.add(new Bomb(context)); // Add additional bombs every 10-second interval
                }
            }


            // Draw and update items
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                item.updateRotation();
                item.draw(canvas);
                item.itemY += item.itemVelocity;

                // Check collision with person
                if (item.itemX + item.getItemWidth() >= personX + 100
                        && item.itemX <= personX + person.getWidth() - 100
                        && item.itemY + item.getItemHeight() - 320 >= personY
                        && item.itemY <= personY + person.getHeight() - 320) {
                    points += item.value;

                    Collect collect = new Collect(context);
                    collect.collectX = items.get(i).itemX;
                    collect.collectY = items.get(i).itemY;
                    collects.add(collect);

                    // Only reset position if there's more than 5 seconds remaining
                    if (remainingTime > 4) {
                        item.resetPosition();
                    } else {
                        items.remove(i);
                        i--; // Adjust index after removal
                    }
                }

                // Reset position if item falls off ground
                if (item.itemY + item.getItemHeight() >= dHeight - ground.getHeight()) {
                    Explosion explosion = new Explosion(context);
                    explosion.explosionX = items.get(i).itemX;
                    explosion.explosionY = items.get(i).itemY;
                    explosions.add(explosion);
                    // Only reset position if there's more than 5 seconds remaining
                    if (remainingTime > 4) {
                        item.resetPosition();
                    } else {
                        items.remove(i);
                        i--; // Adjust index after removal
                    }
                }
            }

            // Draw and update bomb
            for (int i = 0; i < bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                canvas.drawBitmap(bomb.getBomb(), bomb.bombX, bomb.bombY, null);
                bomb.bombY += bomb.bombVelocity;

                // Collision detection with person
                if (bomb.bombX + bomb.getBombWidth() >= personX + 100
                        && bomb.bombX <= personX + person.getWidth() - 100
                        && bomb.bombY + bomb.getBombHeight() - 290 >= personY
                        && bomb.bombY <= personY + person.getHeight() - 290) {
                    Explosion explosion = new Explosion(context);
                    explosion.explosionX = bombs.get(i).bombX;
                    explosion.explosionY = bombs.get(i).bombY;
                    explosions.add(explosion);
                    bomb.resetPosition();
                    bombCollected = true;
                }

                // Reset bomb position if it falls off ground
                if (bomb.bombY + bomb.getBombHeight() >= dHeight - ground.getHeight()) {
                    Explosion explosion = new Explosion(context);
                    explosion.explosionX = bombs.get(i).bombX;
                    explosion.explosionY = bombs.get(i).bombY;
                    explosions.add(explosion);
                    // Only reset position if there's more than 5 seconds remaining
                    if (remainingTime > 4) {
                        bomb.resetPosition();
                    } else {
                        bombs.remove(i);
                        i--; // Adjust index after removal
                    }
                }
            }

            // End game after duration
            if (elapsedTime >= gameDuration) {
                endGame(toGameWin);
            }

            for (int i = 0; i < explosions.size(); i++) {
                canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                        explosions.get(i).explosionY, null);
                explosions.get(i).explosionFrame++;
                if (explosions.get(i).explosionFrame > 3) {
                    explosions.remove(i);

                    if (bombCollected){
                        endGame(toGameOver);
                    }
                }
            }

            for (int i = 0; i < collects.size(); i++) {
                canvas.drawBitmap(collects.get(i).getCollect(collects.get(i).collectFrame), collects.get(i).collectX,
                        collects.get(i).collectY, null);
                collects.get(i).collectFrame++;
                if (collects.get(i).collectFrame > 7) {
                    collects.remove(i);
                }
            }

            // Update score
            canvas.drawBitmap(timeScoreBackground, 30, 120, null);
            canvas.drawText("" + points, 300, TEXT_SIZE + 190, textPaint);
            canvas.drawText("" + remainingTime + "s", 80, TEXT_SIZE + 190, timerPaint);

            // Draw pause button
            canvas.drawBitmap(pauseButton, null, pauseButtonRect, null);
        } else {
            // Draw blurred background
            if (blurredBackground != null) {
                canvas.drawBitmap(blurredBackground, 0, 0, null);
            }
            canvas.drawRect(0, 0, dWidth, dHeight, blurPaint);

            // Draw menu buttons
            canvas.drawBitmap(continueButton, null, continueButtonRect, null);
            canvas.drawBitmap(restartButton, null, restartButtonRect, null);
            canvas.drawBitmap(exitButton, null, exitButtonRect, null);
        }

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    private void endGame(Intent i) {
        isGameOver = true;
        Intent intent = i;
        intent.putExtra("points", points);
        if (context instanceof Activity) { // Ensure context is an Activity
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0); // Now it's safe to call
            activity.finish();
        } else {
            context.startActivity(intent); // Fallback, but no animation
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            if (!isPaused) {
                // Check if pause button was clicked
                if (pauseButtonRect.contains((int)touchX, (int)touchY)) {
                    isPaused = true;
                    pauseStartTime = SystemClock.elapsedRealtime();
                    createBlurredBackground(new Canvas());
                    return true;
                }
            } else {
                // Check menu button clicks
                if (continueButtonRect.contains((int) touchX, (int) touchY)) {
                    isPaused = false;
                    totalPausedTime += (SystemClock.elapsedRealtime() - pauseStartTime);
                    if (blurredBackground != null) {
                        blurredBackground.recycle();
                        blurredBackground = null;
                    }
                    return true;
                }
                if (restartButtonRect.contains((int) touchX, (int) touchY)) {
                    restartGame();
                    return true;
                }
                if (exitButtonRect.contains((int) touchX, (int) touchY)) {
                    ((Activity) context).finish();
                    return true;
                }
                return true;
            }
        }

        // Handle normal game touch events when not paused
        if (!isPaused) {
            return handleGameTouchEvent(event);
        }

        return true;
    }

    // Move the original touch handling to a separate method
    private boolean handleGameTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (touchY >= personY) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldPersonX = personX;
            }

            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newPersonX = oldPersonX - shift;
                if (newPersonX <= 0) {
                    personX = 0;
                } else if (newPersonX >= dWidth - person.getWidth()) {
                    personX = dWidth - person.getWidth();
                } else {
                    personX = newPersonX;
                }
            }
        }
        return true;
    }

    // Add method to restart the game
    private void restartGame() {
        CollectGame gameView = new CollectGame(context);
        ((Activity) context).setContentView(gameView);
    }
}
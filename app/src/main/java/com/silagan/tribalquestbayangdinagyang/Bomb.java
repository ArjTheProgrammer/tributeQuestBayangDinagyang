package com.silagan.tribalquestbayangdinagyang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Bomb {

    Bitmap bomb;
    int bombX, bombY, bombVelocity;
    Random random;

    public Bomb(Context context) {
        bomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);

        random = new Random();
        bombVelocity = 20;
        resetPosition();
    }

    public Bitmap getBomb() {
        return bomb;
    }

    public int getBombWidth() {
        return bomb.getWidth();
    }

    public int getBombHeight() {
        return bomb.getHeight();
    }

    public void resetPosition() {
        bombX = random.nextInt(CollectGame.dWidth - getBombWidth());
        bombY = -200;
    }
}

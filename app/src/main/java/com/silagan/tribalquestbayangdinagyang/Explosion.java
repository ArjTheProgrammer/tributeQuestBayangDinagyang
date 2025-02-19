package com.silagan.tribalquestbayangdinagyang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    private Bitmap[] explosion = new Bitmap[4];
    int explosionFrame = 0;
    int explosionX, explosionY;

    public Explosion(Context context) {
        // Load normal sized explosions
        explosion[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode0);
        explosion[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode1);
        explosion[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode2);
        explosion[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode3);
    }

    public Bitmap getExplosion(int explosionFrame) {
        // Return large explosion if bomb collision, otherwise normal explosion
        return explosion[explosionFrame];
    }
}

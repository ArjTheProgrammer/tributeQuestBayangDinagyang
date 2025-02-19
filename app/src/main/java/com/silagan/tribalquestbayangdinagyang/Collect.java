package com.silagan.tribalquestbayangdinagyang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Collect {

    private Bitmap[] collect = new Bitmap[8];

    int collectFrame = 0;
    int collectX, collectY;

    public Collect(Context context) {
        // Load normal sized explosions
        collect[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected0);
        collect[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected0);
        collect[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected1);
        collect[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected1);
        collect[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected2);
        collect[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected2);
        collect[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected3);
        collect[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.collected3);
    }

    public Bitmap getCollect(int collectFrame) {
        return collect[collectFrame];
    }
}


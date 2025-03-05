package com.silagan.tribalquestbayangdinagyang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.Random;

public class Item {

    private Bitmap originalItem;
    private Matrix matrix;
    int itemX, itemY, itemVelocity, value;
    Random random;
    private float rotationAngle;
    private float rotationSpeed;

    public Item(Context context, int drawableId, int value) {
        Bitmap origItem = BitmapFactory.decodeResource(context.getResources(), drawableId);
        originalItem = Bitmap.createScaledBitmap(origItem, (int)(origItem.getWidth() / 1.6), (int)(origItem.getHeight() / 1.6), true);
        this.value = value;
        random = new Random();
        itemVelocity = 20;
        rotationSpeed = 1;
        rotationAngle = random.nextFloat() * 360;
        matrix = new Matrix();
        resetPosition();
    }

    public void draw(android.graphics.Canvas canvas) {
        matrix.reset();
        matrix.postRotate(rotationAngle, originalItem.getWidth()/2f, originalItem.getHeight()/2f);
        matrix.postTranslate(itemX, itemY);
        canvas.drawBitmap(originalItem, matrix, null);
    }

    public int getItemWidth() {
        return originalItem.getWidth();
    }

    public int getItemHeight() {
        return originalItem.getHeight();
    }

    public void resetPosition() {
        itemX = random.nextInt(CollectGame.dWidth - getItemWidth());
        itemY = -200;
        rotationAngle = random.nextFloat() * 360;
    }

    public void updateRotation() {
        rotationAngle += rotationSpeed;
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        } else if (rotationAngle <= 0) {
            rotationAngle += 360;
        }
    }
}

package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageIndicator extends Indicator<ImageIndicator> {
    private Bitmap bitmapIndicator;
    private RectF bitmapRect;
    private int height;
    private int width;

    public ImageIndicator(Context context, int resource) {
        this(context, BitmapFactory.decodeResource(context.getResources(), resource));
    }

    public ImageIndicator(Context context, int resource, int width2, int height2) {
        this(context, BitmapFactory.decodeResource(context.getResources(), resource), width2, height2);
    }

    public ImageIndicator(Context context, Bitmap bitmapIndicator2) {
        this(context, bitmapIndicator2, bitmapIndicator2.getWidth(), bitmapIndicator2.getHeight());
    }

    public ImageIndicator(Context context, Bitmap bitmapIndicator2, int width2, int height2) {
        super(context);
        this.bitmapRect = new RectF();
        this.bitmapIndicator = bitmapIndicator2;
        this.width = width2;
        this.height = height2;
        if (width2 <= 0) {
            throw new IllegalArgumentException("width must be bigger than 0");
        } else if (height2 <= 0) {
            throw new IllegalArgumentException("height must be bigger than 0");
        }
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return 0.0f;
    }

    public void draw(Canvas canvas, float degree) {
        canvas.save();
        canvas.rotate(90.0f + degree, getCenterX(), getCenterY());
        this.bitmapRect.set(getCenterX() - (((float) this.width) / 2.0f), getCenterY() - (((float) this.height) / 2.0f), getCenterX() + (((float) this.width) / 2.0f), getCenterY() + (((float) this.height) / 2.0f));
        canvas.drawBitmap(this.bitmapIndicator, (Rect) null, this.bitmapRect, this.indicatorPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void updateIndicator() {
    }

    /* access modifiers changed from: protected */
    public void setWithEffects(boolean withEffects) {
    }
}

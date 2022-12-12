package com.github.anastr.speedviewlib.components.note;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageNote extends Note<ImageNote> {
    private int height;
    private Bitmap image;
    private RectF imageRect;
    private Paint notePaint;
    private int width;

    public ImageNote(Context context, int resource) {
        this(context, BitmapFactory.decodeResource(context.getResources(), resource));
    }

    public ImageNote(Context context, int resource, int width2, int height2) {
        this(context, BitmapFactory.decodeResource(context.getResources(), resource), width2, height2);
    }

    public ImageNote(Context context, Bitmap image2) {
        this(context, image2, image2.getWidth(), image2.getHeight());
    }

    public ImageNote(Context context, Bitmap image2, int width2, int height2) {
        super(context);
        this.imageRect = new RectF();
        this.notePaint = new Paint(1);
        if (image2 == null) {
            throw new IllegalArgumentException("image cannot be null.");
        }
        this.image = image2;
        this.width = width2;
        this.height = height2;
        if (width2 <= 0) {
            throw new IllegalArgumentException("width must be bigger than 0");
        } else if (height2 <= 0) {
            throw new IllegalArgumentException("height must be bigger than 0");
        }
    }

    public void build(int viewWidth) {
        noticeContainsSizeChange(this.width, this.height);
    }

    /* access modifiers changed from: protected */
    public void drawContains(Canvas canvas, float leftX, float topY) {
        this.imageRect.set(leftX, topY, ((float) this.width) + leftX, ((float) this.height) + topY);
        canvas.drawBitmap(this.image, (Rect) null, this.imageRect, this.notePaint);
    }
}

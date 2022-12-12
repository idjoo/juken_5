package com.github.anastr.speedviewlib.components.note;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.github.anastr.speedviewlib.components.note.Note;

public abstract class Note<N extends Note> {
    public static final int INFINITE = -1;
    private Align align = Align.Top;
    private Bitmap backgroundBitmap;
    private Paint backgroundPaint = new Paint(1);
    private int containsH = 0;
    private int containsW = 0;
    private float cornersRound = 5.0f;
    private float density;
    private int noteH = 0;
    private int noteW = 0;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    private Paint paint = new Paint(1);
    private Position position = Position.CenterIndicator;
    private float triangleHeight;

    public enum Align {
        Left,
        Top,
        Right,
        Bottom
    }

    public enum Position {
        TopIndicator,
        CenterIndicator,
        BottomIndicator,
        TopSpeedometer,
        CenterSpeedometer,
        QuarterSpeedometer
    }

    public abstract void build(int i);

    /* access modifiers changed from: protected */
    public abstract void drawContains(Canvas canvas, float f, float f2);

    protected Note(Context context) {
        this.density = context.getResources().getDisplayMetrics().density;
        init();
    }

    private void init() {
        this.triangleHeight = dpTOpx(12.0f);
        this.backgroundPaint.setColor(-2697257);
        setPadding(dpTOpx(7.0f), dpTOpx(7.0f), dpTOpx(7.0f), dpTOpx(7.0f));
    }

    public float dpTOpx(float dp) {
        return this.density * dp;
    }

    /* access modifiers changed from: protected */
    public void noticeContainsSizeChange(int containsW2, int containsH2) {
        this.containsW = containsW2;
        this.containsH = containsH2;
        if (this.align == Align.Top || this.align == Align.Bottom) {
            this.noteW = (int) (((float) containsW2) + this.paddingLeft + this.paddingRight);
            this.noteH = (int) (((float) containsH2) + this.paddingTop + this.paddingBottom + this.triangleHeight);
        } else {
            this.noteW = (int) (((float) containsW2) + this.paddingLeft + this.paddingRight + this.triangleHeight);
            this.noteH = (int) (((float) containsH2) + this.paddingTop + this.paddingBottom);
        }
        updateBackgroundBitmap();
    }

    private void updateBackgroundBitmap() {
        this.backgroundBitmap = Bitmap.createBitmap(this.noteW, this.noteH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(this.backgroundBitmap);
        if (this.align == Align.Left) {
            bitmapLeft(c);
        } else if (this.align == Align.Top) {
            bitmapTop(c);
        } else if (this.align == Align.Right) {
            bitmapRight(c);
        } else if (this.align == Align.Bottom) {
            bitmapBottom(c);
        }
    }

    private void bitmapLeft(Canvas c) {
        RectF rectF = new RectF(0.0f, 0.0f, ((float) this.noteW) - this.triangleHeight, (float) this.noteH);
        Path p = new Path();
        p.moveTo((float) this.noteW, ((float) this.noteH) / 2.0f);
        p.lineTo(rectF.right - 1.0f, (((float) this.noteH) / 2.0f) - dpTOpx(9.0f));
        p.lineTo(rectF.right - 1.0f, (((float) this.noteH) / 2.0f) + dpTOpx(9.0f));
        c.drawPath(p, this.backgroundPaint);
        c.drawRoundRect(rectF, this.cornersRound, this.cornersRound, this.backgroundPaint);
    }

    private void bitmapTop(Canvas c) {
        RectF rectF = new RectF(0.0f, 0.0f, (float) this.noteW, ((float) this.noteH) - this.triangleHeight);
        Path p = new Path();
        p.moveTo(((float) this.noteW) / 2.0f, (float) this.noteH);
        p.lineTo((((float) this.noteW) / 2.0f) - dpTOpx(9.0f), rectF.bottom - 1.0f);
        p.lineTo((((float) this.noteW) / 2.0f) + dpTOpx(9.0f), rectF.bottom - 1.0f);
        c.drawPath(p, this.backgroundPaint);
        c.drawRoundRect(rectF, this.cornersRound, this.cornersRound, this.backgroundPaint);
    }

    private void bitmapRight(Canvas c) {
        RectF rectF = new RectF(this.triangleHeight + 0.0f, 0.0f, (float) this.noteW, (float) this.noteH);
        Path p = new Path();
        p.moveTo(0.0f, ((float) this.noteH) / 2.0f);
        p.lineTo(rectF.left + 1.0f, (((float) this.noteH) / 2.0f) - dpTOpx(9.0f));
        p.lineTo(rectF.left + 1.0f, (((float) this.noteH) / 2.0f) + dpTOpx(9.0f));
        c.drawPath(p, this.backgroundPaint);
        c.drawRoundRect(rectF, this.cornersRound, this.cornersRound, this.backgroundPaint);
    }

    private void bitmapBottom(Canvas c) {
        RectF rectF = new RectF(0.0f, this.triangleHeight + 0.0f, (float) this.noteW, (float) this.noteH);
        Path p = new Path();
        p.moveTo(((float) this.noteW) / 2.0f, 0.0f);
        p.lineTo((((float) this.noteW) / 2.0f) - dpTOpx(9.0f), rectF.top + 1.0f);
        p.lineTo((((float) this.noteW) / 2.0f) + dpTOpx(9.0f), rectF.top + 1.0f);
        c.drawPath(p, this.backgroundPaint);
        c.drawRoundRect(rectF, this.cornersRound, this.cornersRound, this.backgroundPaint);
    }

    public void draw(Canvas canvas, float posX, float posY) {
        switch (this.align) {
            case Left:
                canvas.drawBitmap(this.backgroundBitmap, posX - ((float) this.noteW), posY - (((float) this.noteH) / 2.0f), this.paint);
                drawContains(canvas, (posX - ((float) this.noteW)) + this.paddingLeft, (posY - (((float) this.noteH) / 2.0f)) + this.paddingTop);
                return;
            case Top:
                canvas.drawBitmap(this.backgroundBitmap, posX - (((float) this.noteW) / 2.0f), posY - ((float) this.noteH), this.paint);
                drawContains(canvas, posX - (((float) this.containsW) / 2.0f), (posY - ((float) this.noteH)) + this.paddingTop);
                return;
            case Right:
                canvas.drawBitmap(this.backgroundBitmap, posX, posY - (((float) this.noteH) / 2.0f), this.paint);
                drawContains(canvas, this.triangleHeight + posX + this.paddingLeft, (posY - (((float) this.noteH) / 2.0f)) + this.paddingTop);
                return;
            case Bottom:
                canvas.drawBitmap(this.backgroundBitmap, posX - (((float) this.noteW) / 2.0f), posY, this.paint);
                drawContains(canvas, posX - (((float) this.containsW) / 2.0f), this.triangleHeight + posY + this.paddingTop);
                return;
            default:
                return;
        }
    }

    public int getBackgroundColor() {
        return this.backgroundPaint.getColor();
    }

    public N setBackgroundColor(int backgroundColor) {
        this.backgroundPaint.setColor(backgroundColor);
        return this;
    }

    public float getCornersRound() {
        return this.cornersRound;
    }

    public N setCornersRound(float cornersRound2) {
        if (cornersRound2 < 0.0f) {
            throw new IllegalArgumentException("cornersRound cannot be negative");
        }
        this.cornersRound = cornersRound2;
        return this;
    }

    public Align getAlign() {
        return this.align;
    }

    public N setAlign(Align align2) {
        this.align = align2;
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public N setPosition(Position position2) {
        this.position = position2;
        return this;
    }

    public N setPadding(float left, float top, float right, float bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        noticeContainsSizeChange(this.containsW, this.containsH);
        return this;
    }
}

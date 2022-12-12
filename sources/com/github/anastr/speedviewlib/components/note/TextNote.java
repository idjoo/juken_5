package com.github.anastr.speedviewlib.components.note;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class TextNote extends Note<TextNote> {
    private TextPaint notePaint = new TextPaint(1);
    private CharSequence noteText;
    private StaticLayout textLayout;
    private float textSize = this.notePaint.getTextSize();

    public TextNote(Context context, CharSequence noteText2) {
        super(context);
        if (noteText2 == null) {
            throw new IllegalArgumentException("noteText cannot be null.");
        }
        this.noteText = noteText2;
        this.notePaint.setTextAlign(Paint.Align.LEFT);
    }

    public void build(int viewWidth) {
        this.textLayout = new StaticLayout(this.noteText, this.notePaint, viewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int w = 0;
        for (int i = 0; i < this.textLayout.getLineCount(); i++) {
            w = (int) Math.max((float) w, this.textLayout.getLineWidth(i));
        }
        noticeContainsSizeChange(w, this.textLayout.getHeight());
    }

    /* access modifiers changed from: protected */
    public void drawContains(Canvas canvas, float leftX, float topY) {
        canvas.save();
        canvas.translate(leftX, topY);
        this.textLayout.draw(canvas);
        canvas.restore();
    }

    public float getTextSize() {
        return this.textSize;
    }

    public TextNote setTextSize(float textSize2) {
        this.textSize = textSize2;
        this.notePaint.setTextSize(textSize2);
        return this;
    }

    public TextNote setTextTypeFace(Typeface typeface) {
        this.notePaint.setTypeface(typeface);
        return this;
    }

    public int getTextColor() {
        return this.notePaint.getColor();
    }

    public TextNote setTextColor(int textColor) {
        this.notePaint.setColor(textColor);
        return this;
    }
}

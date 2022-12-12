package com.androidplot.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.util.FontUtils;

public class TextLabelWidget extends Widget {
    private boolean autoPackEnabled;
    private Paint labelPaint;
    private TextOrientation orientation;
    private String text;

    public TextLabelWidget(LayoutManager layoutManager, Size size) {
        this(layoutManager, size, TextOrientation.HORIZONTAL);
    }

    public TextLabelWidget(LayoutManager layoutManager, String title, Size size, TextOrientation orientation2) {
        this(layoutManager, size, orientation2);
        setText(title);
    }

    public TextLabelWidget(LayoutManager layoutManager, Size size, TextOrientation orientation2) {
        super(layoutManager, new Size(0.0f, SizeMode.ABSOLUTE, 0.0f, SizeMode.ABSOLUTE));
        this.autoPackEnabled = true;
        this.labelPaint = new Paint();
        this.labelPaint.setColor(-1);
        this.labelPaint.setAntiAlias(true);
        this.labelPaint.setTextAlign(Paint.Align.CENTER);
        setClippingEnabled(false);
        setSize(size);
        this.orientation = orientation2;
    }

    /* access modifiers changed from: protected */
    public void onMetricsChanged(Size olds, Size news) {
        if (this.autoPackEnabled) {
            pack();
        }
    }

    public void onPostInit() {
        if (this.autoPackEnabled) {
            pack();
        }
    }

    public void pack() {
        Rect size = FontUtils.getStringDimensions(this.text, getLabelPaint());
        if (size != null) {
            switch (this.orientation) {
                case HORIZONTAL:
                    setSize(new Size((float) size.height(), SizeMode.ABSOLUTE, (float) (size.width() + 2), SizeMode.ABSOLUTE));
                    break;
                case VERTICAL_ASCENDING:
                case VERTICAL_DESCENDING:
                    setSize(new Size((float) size.width(), SizeMode.ABSOLUTE, (float) (size.height() + 2), SizeMode.ABSOLUTE));
                    break;
            }
            refreshLayout();
        }
    }

    public void doOnDraw(Canvas canvas, RectF widgetRect) {
        if (this.text != null && this.text.length() != 0) {
            float vOffset = this.labelPaint.getFontMetrics().descent;
            PointF start = getAnchorCoordinates(widgetRect, Anchor.CENTER);
            try {
                canvas.save();
                canvas.translate(start.x, start.y);
                switch (this.orientation) {
                    case HORIZONTAL:
                        break;
                    case VERTICAL_ASCENDING:
                        canvas.rotate(-90.0f);
                        break;
                    case VERTICAL_DESCENDING:
                        canvas.rotate(90.0f);
                        break;
                    default:
                        throw new UnsupportedOperationException("Orientation " + this.orientation + " not yet implemented for TextLabelWidget.");
                }
                canvas.drawText(this.text, 0.0f, vOffset, this.labelPaint);
            } finally {
                canvas.restore();
            }
        }
    }

    public Paint getLabelPaint() {
        return this.labelPaint;
    }

    public void setLabelPaint(Paint labelPaint2) {
        this.labelPaint = labelPaint2;
        if (this.autoPackEnabled) {
            pack();
        }
    }

    public TextOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(TextOrientation orientation2) {
        this.orientation = orientation2;
        if (this.autoPackEnabled) {
            pack();
        }
    }

    public boolean isAutoPackEnabled() {
        return this.autoPackEnabled;
    }

    public void setAutoPackEnabled(boolean autoPackEnabled2) {
        this.autoPackEnabled = autoPackEnabled2;
        if (autoPackEnabled2) {
            pack();
        }
    }

    public void setText(String text2) {
        this.text = text2;
        if (this.autoPackEnabled) {
            pack();
        }
    }

    public String getText() {
        return this.text;
    }
}

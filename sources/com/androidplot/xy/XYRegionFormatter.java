package com.androidplot.xy;

import android.content.Context;
import android.graphics.Paint;
import com.halfhp.fig.Fig;
import com.halfhp.fig.FigException;

public class XYRegionFormatter {
    private Paint paint = new Paint();

    public XYRegionFormatter(Context ctx, int xmlCfgId) {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        if (getClass().equals(XYRegionFormatter.class)) {
            try {
                Fig.configure(ctx, (Object) this, xmlCfgId);
            } catch (FigException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public XYRegionFormatter(int color) {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setColor(color);
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public Paint getPaint() {
        return this.paint;
    }
}

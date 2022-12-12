package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.Canvas;

public class NoIndicator extends Indicator<NoIndicator> {
    public NoIndicator(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return 0.0f;
    }

    public void draw(Canvas canvas, float degree) {
    }

    /* access modifiers changed from: protected */
    public void updateIndicator() {
    }

    /* access modifiers changed from: protected */
    public void setWithEffects(boolean withEffects) {
    }
}

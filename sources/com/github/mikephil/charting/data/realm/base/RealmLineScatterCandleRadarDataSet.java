package com.github.mikephil.charting.data.realm.base;

import android.graphics.DashPathEffect;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmLineScatterCandleRadarDataSet<T extends RealmObject, S extends Entry> extends RealmBarLineScatterCandleBubbleDataSet<T, S> implements ILineScatterCandleRadarDataSet<S> {
    protected boolean mDrawHorizontalHighlightIndicator = true;
    protected boolean mDrawVerticalHighlightIndicator = true;
    protected DashPathEffect mHighlightDashPathEffect = null;
    protected float mHighlightLineWidth = 0.5f;

    public RealmLineScatterCandleRadarDataSet(RealmResults<T> results, String yValuesField) {
        super(results, yValuesField);
    }

    public RealmLineScatterCandleRadarDataSet(RealmResults<T> results, String yValuesField, String xIndexField) {
        super(results, yValuesField, xIndexField);
    }

    public void setDrawHorizontalHighlightIndicator(boolean enabled) {
        this.mDrawHorizontalHighlightIndicator = enabled;
    }

    public void setDrawVerticalHighlightIndicator(boolean enabled) {
        this.mDrawVerticalHighlightIndicator = enabled;
    }

    public void setDrawHighlightIndicators(boolean enabled) {
        setDrawVerticalHighlightIndicator(enabled);
        setDrawHorizontalHighlightIndicator(enabled);
    }

    public boolean isVerticalHighlightIndicatorEnabled() {
        return this.mDrawVerticalHighlightIndicator;
    }

    public boolean isHorizontalHighlightIndicatorEnabled() {
        return this.mDrawHorizontalHighlightIndicator;
    }

    public void setHighlightLineWidth(float width) {
        this.mHighlightLineWidth = Utils.convertDpToPixel(width);
    }

    public float getHighlightLineWidth() {
        return this.mHighlightLineWidth;
    }

    public void enableDashedHighlightLine(float lineLength, float spaceLength, float phase) {
        this.mHighlightDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public void disableDashedHighlightLine() {
        this.mHighlightDashPathEffect = null;
    }

    public boolean isDashedHighlightLineEnabled() {
        return this.mHighlightDashPathEffect != null;
    }

    public DashPathEffect getDashPathEffectHighlight() {
        return this.mHighlightDashPathEffect;
    }
}

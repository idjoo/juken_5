package com.androidplot.pie;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.androidplot.Plot;
import com.androidplot.R;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.util.AttrUtils;
import com.androidplot.util.PixelUtils;

public class PieChart extends Plot<Segment, SegmentFormatter, PieRenderer, SegmentBundle, SegmentRegistry> {
    private static final int DEFAULT_LEGEND_WIDGET_H_DP = 30;
    private static final int DEFAULT_LEGEND_WIDGET_ICON_SIZE_DP = 18;
    private static final int DEFAULT_LEGEND_WIDGET_X_OFFSET_DP = 40;
    private static final int DEFAULT_LEGEND_WIDGET_Y_OFFSET_DP = 0;
    private static final int DEFAULT_PADDING_DP = 5;
    private static final int DEFAULT_PIE_WIDGET_H_DP = 18;
    private static final int DEFAULT_PIE_WIDGET_W_DP = 10;
    private static final int DEFAULT_PIE_WIDGET_X_OFFSET_DP = 0;
    private static final int DEFAULT_PIE_WIDGET_Y_OFFSET_DP = 0;
    private PieLegendWidget legend;
    private PieWidget pie;

    /* access modifiers changed from: protected */
    public SegmentRegistry getRegistryInstance() {
        return new SegmentRegistry();
    }

    public PieChart(Context context, String title) {
        super(context, title);
    }

    public PieChart(Context context, String title, Plot.RenderMode mode) {
        super(context, title, mode);
    }

    public PieChart(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    /* access modifiers changed from: protected */
    public void onPreInit() {
        this.pie = new PieWidget(getLayoutManager(), this, new Size(PixelUtils.dpToPix(18.0f), SizeMode.FILL, PixelUtils.dpToPix(10.0f), SizeMode.FILL));
        this.pie.position(PixelUtils.dpToPix(0.0f), HorizontalPositioning.ABSOLUTE_FROM_CENTER, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_CENTER, Anchor.CENTER);
        this.legend = new PieLegendWidget(getLayoutManager(), this, new Size(PixelUtils.dpToPix(30.0f), SizeMode.ABSOLUTE, 0.5f, SizeMode.RELATIVE), new DynamicTableModel(0, 1), new Size(PixelUtils.dpToPix(18.0f), SizeMode.ABSOLUTE, PixelUtils.dpToPix(18.0f), SizeMode.ABSOLUTE));
        this.legend.position(PixelUtils.dpToPix(40.0f), HorizontalPositioning.ABSOLUTE_FROM_RIGHT, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_BOTTOM, Anchor.RIGHT_BOTTOM);
        this.legend.setVisible(false);
        float padding = PixelUtils.dpToPix(5.0f);
        this.pie.setPadding(padding, padding, padding, padding);
    }

    /* access modifiers changed from: protected */
    public void processAttrs(TypedArray attrs) {
        AttrUtils.configureLinePaint(attrs, getBorderPaint(), R.styleable.pie_PieChart_pieBorderColor, R.styleable.pie_PieChart_pieBorderThickness);
    }

    public void setPie(PieWidget pie2) {
        this.pie = pie2;
    }

    public PieWidget getPie() {
        return this.pie;
    }

    public void addSegment(Segment segment, SegmentFormatter formatter) {
        addSeries(segment, formatter);
    }

    public void removeSegment(Segment segment) {
        removeSeries(segment);
    }

    public PieLegendWidget getLegend() {
        return this.legend;
    }

    public void setLegend(PieLegendWidget legend2) {
        this.legend = legend2;
    }
}

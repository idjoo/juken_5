package com.androidplot.xy;

import com.androidplot.util.SeriesUtils;
import com.androidplot.xy.OrderedXYSeries;
import java.util.ArrayList;
import java.util.List;

public class SampledXYSeries implements FastXYSeries, OrderedXYSeries {
    private XYSeries activeSeries;
    private Sampler algorithm;
    /* access modifiers changed from: private */
    public RectRegion bounds;
    /* access modifiers changed from: private */
    public Exception lastResamplingException;
    private float ratio;
    /* access modifiers changed from: private */
    public XYSeries rawData;
    private int threshold;
    private final OrderedXYSeries.XOrder xOrder;
    private List<EditableXYSeries> zoomLevels;

    public SampledXYSeries(XYSeries rawData2, OrderedXYSeries.XOrder xOrder2, float ratio2, int threshold2) {
        this.algorithm = new LTTBSampler();
        this.rawData = rawData2;
        this.xOrder = xOrder2;
        setRatio(ratio2);
        setThreshold(threshold2);
        resample();
    }

    public SampledXYSeries(XYSeries rawData2, float ratio2, int threshold2) {
        this(rawData2, SeriesUtils.getXYOrder(rawData2), ratio2, threshold2);
    }

    public void resample() {
        this.bounds = null;
        this.zoomLevels = new ArrayList();
        int t = (int) Math.ceil((double) (((float) this.rawData.size()) / getRatio()));
        List<Thread> threads = new ArrayList<>(this.zoomLevels.size());
        while (t > this.threshold) {
            final EditableXYSeries thisSeries = new FixedSizeEditableXYSeries(getTitle(), t);
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        RectRegion b = SampledXYSeries.this.getAlgorithm().run(SampledXYSeries.this.rawData, thisSeries);
                        if (SampledXYSeries.this.bounds == null) {
                            RectRegion unused = SampledXYSeries.this.bounds = b;
                        }
                    } catch (Exception ex) {
                        Exception unused2 = SampledXYSeries.this.lastResamplingException = ex;
                    }
                }
            }, "Androidplot XY Series Sampler");
            getZoomLevels().add(thisSeries);
            threads.add(thread);
            thread.start();
            t = (int) Math.ceil((double) (((float) t) / getRatio()));
        }
        for (Thread thread2 : threads) {
            try {
                thread2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (this.lastResamplingException != null) {
            throw new RuntimeException("Exception encountered during resampling", this.lastResamplingException);
        }
    }

    /* access modifiers changed from: protected */
    public List<EditableXYSeries> getZoomLevels() {
        return this.zoomLevels;
    }

    public void setZoomFactor(double factor) {
        if (factor <= 1.0d) {
            this.activeSeries = this.rawData;
            return;
        }
        int i = getZoomIndex(factor, (double) getRatio());
        if (i < this.zoomLevels.size()) {
            this.activeSeries = this.zoomLevels.get(i);
        } else {
            this.activeSeries = this.zoomLevels.get(this.zoomLevels.size() - 1);
        }
    }

    protected static int getZoomIndex(double zoomFactor, double ratio2) {
        int index = (int) Math.round(Math.log(zoomFactor) / Math.log(ratio2));
        if (index > 0) {
            return index - 1;
        }
        return 0;
    }

    public double getMaxZoomFactor() {
        return Math.pow((double) getRatio(), (double) this.zoomLevels.size());
    }

    public Sampler getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(Sampler algorithm2) {
        this.algorithm = algorithm2;
        resample();
    }

    public String getTitle() {
        return this.rawData.getTitle();
    }

    public int size() {
        return this.activeSeries.size();
    }

    public Number getX(int index) {
        return this.activeSeries.getX(index);
    }

    public Number getY(int index) {
        return this.activeSeries.getY(index);
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold2) {
        if (threshold2 >= this.rawData.size()) {
            throw new IllegalArgumentException("Threshold must be < original series size.");
        }
        this.threshold = threshold2;
    }

    public RectRegion getBounds() {
        return this.bounds;
    }

    public void setBounds(RectRegion bounds2) {
        this.bounds = bounds2;
    }

    public RectRegion minMax() {
        return this.bounds;
    }

    public OrderedXYSeries.XOrder getXOrder() {
        return this.xOrder;
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio2) {
        if (ratio2 <= 1.0f) {
            throw new IllegalArgumentException("Ratio must be greater than 1");
        }
        this.ratio = ratio2;
    }
}

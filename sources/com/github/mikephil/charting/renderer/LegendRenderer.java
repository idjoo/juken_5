package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class LegendRenderer extends Renderer {
    protected Legend mLegend;
    protected Paint mLegendFormPaint;
    protected Paint mLegendLabelPaint = new Paint(1);

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);
        this.mLegend = legend;
        this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Paint.Align.LEFT);
        this.mLegendFormPaint = new Paint(1);
        this.mLegendFormPaint.setStyle(Paint.Style.FILL);
        this.mLegendFormPaint.setStrokeWidth(3.0f);
    }

    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }

    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v38, resolved type: com.github.mikephil.charting.data.ChartData<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v44, resolved type: com.github.mikephil.charting.data.ChartData<?>} */
    /* JADX WARNING: type inference failed for: r7v0, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    /* JADX WARNING: type inference failed for: r19v24, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void computeLegend(com.github.mikephil.charting.data.ChartData<?> r23) {
        /*
            r22 = this;
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            boolean r19 = r19.isLegendCustom()
            if (r19 != 0) goto L_0x01c0
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r9 = 0
        L_0x0017:
            int r19 = r23.getDataSetCount()
            r0 = r19
            if (r9 >= r0) goto L_0x0167
            r0 = r23
            com.github.mikephil.charting.interfaces.datasets.IDataSet r7 = r0.getDataSetByIndex(r9)
            java.util.List r4 = r7.getColors()
            int r8 = r7.getEntryCount()
            boolean r0 = r7 instanceof com.github.mikephil.charting.interfaces.datasets.IBarDataSet
            r19 = r0
            if (r19 == 0) goto L_0x008d
            r19 = r7
            com.github.mikephil.charting.interfaces.datasets.IBarDataSet r19 = (com.github.mikephil.charting.interfaces.datasets.IBarDataSet) r19
            boolean r19 = r19.isStacked()
            if (r19 == 0) goto L_0x008d
            r3 = r7
            com.github.mikephil.charting.interfaces.datasets.IBarDataSet r3 = (com.github.mikephil.charting.interfaces.datasets.IBarDataSet) r3
            java.lang.String[] r16 = r3.getStackLabels()
            r11 = 0
        L_0x0045:
            int r19 = r4.size()
            r0 = r19
            if (r11 >= r0) goto L_0x006f
            int r19 = r3.getStackSize()
            r0 = r19
            if (r11 >= r0) goto L_0x006f
            r0 = r16
            int r0 = r0.length
            r19 = r0
            int r19 = r11 % r19
            r19 = r16[r19]
            r0 = r19
            r13.add(r0)
            java.lang.Object r19 = r4.get(r11)
            r0 = r19
            r6.add(r0)
            int r11 = r11 + 1
            goto L_0x0045
        L_0x006f:
            java.lang.String r19 = r3.getLabel()
            if (r19 == 0) goto L_0x008a
            r19 = 1122868(0x112234, float:1.573473E-39)
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)
            r0 = r19
            r6.add(r0)
            java.lang.String r19 = r3.getLabel()
            r0 = r19
            r13.add(r0)
        L_0x008a:
            int r9 = r9 + 1
            goto L_0x0017
        L_0x008d:
            boolean r0 = r7 instanceof com.github.mikephil.charting.interfaces.datasets.IPieDataSet
            r19 = r0
            if (r19 == 0) goto L_0x00e0
            java.util.List r18 = r23.getXVals()
            r15 = r7
            com.github.mikephil.charting.interfaces.datasets.IPieDataSet r15 = (com.github.mikephil.charting.interfaces.datasets.IPieDataSet) r15
            r11 = 0
        L_0x009b:
            int r19 = r4.size()
            r0 = r19
            if (r11 >= r0) goto L_0x00c4
            if (r11 >= r8) goto L_0x00c4
            int r19 = r18.size()
            r0 = r19
            if (r11 >= r0) goto L_0x00c4
            r0 = r18
            java.lang.Object r19 = r0.get(r11)
            r0 = r19
            r13.add(r0)
            java.lang.Object r19 = r4.get(r11)
            r0 = r19
            r6.add(r0)
            int r11 = r11 + 1
            goto L_0x009b
        L_0x00c4:
            java.lang.String r19 = r15.getLabel()
            if (r19 == 0) goto L_0x008a
            r19 = 1122868(0x112234, float:1.573473E-39)
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)
            r0 = r19
            r6.add(r0)
            java.lang.String r19 = r15.getLabel()
            r0 = r19
            r13.add(r0)
            goto L_0x008a
        L_0x00e0:
            boolean r0 = r7 instanceof com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
            r19 = r0
            if (r19 == 0) goto L_0x012b
            r19 = r7
            com.github.mikephil.charting.interfaces.datasets.ICandleDataSet r19 = (com.github.mikephil.charting.interfaces.datasets.ICandleDataSet) r19
            int r19 = r19.getDecreasingColor()
            r20 = 1122867(0x112233, float:1.573472E-39)
            r0 = r19
            r1 = r20
            if (r0 == r1) goto L_0x012b
            r19 = r7
            com.github.mikephil.charting.interfaces.datasets.ICandleDataSet r19 = (com.github.mikephil.charting.interfaces.datasets.ICandleDataSet) r19
            int r19 = r19.getDecreasingColor()
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)
            r0 = r19
            r6.add(r0)
            r19 = r7
            com.github.mikephil.charting.interfaces.datasets.ICandleDataSet r19 = (com.github.mikephil.charting.interfaces.datasets.ICandleDataSet) r19
            int r19 = r19.getIncreasingColor()
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)
            r0 = r19
            r6.add(r0)
            r19 = 0
            r0 = r19
            r13.add(r0)
            java.lang.String r19 = r7.getLabel()
            r0 = r19
            r13.add(r0)
            goto L_0x008a
        L_0x012b:
            r11 = 0
        L_0x012c:
            int r19 = r4.size()
            r0 = r19
            if (r11 >= r0) goto L_0x008a
            if (r11 >= r8) goto L_0x008a
            int r19 = r4.size()
            int r19 = r19 + -1
            r0 = r19
            if (r11 >= r0) goto L_0x0159
            int r19 = r8 + -1
            r0 = r19
            if (r11 >= r0) goto L_0x0159
            r19 = 0
            r0 = r19
            r13.add(r0)
        L_0x014d:
            java.lang.Object r19 = r4.get(r11)
            r0 = r19
            r6.add(r0)
            int r11 = r11 + 1
            goto L_0x012c
        L_0x0159:
            r0 = r23
            com.github.mikephil.charting.interfaces.datasets.IDataSet r19 = r0.getDataSetByIndex(r9)
            java.lang.String r12 = r19.getLabel()
            r13.add(r12)
            goto L_0x014d
        L_0x0167:
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            int[] r19 = r19.getExtraColors()
            if (r19 == 0) goto L_0x01aa
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            java.lang.String[] r19 = r19.getExtraLabels()
            if (r19 == 0) goto L_0x01aa
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            int[] r2 = r19.getExtraColors()
            int r14 = r2.length
            r10 = 0
        L_0x018b:
            if (r10 >= r14) goto L_0x019b
            r5 = r2[r10]
            java.lang.Integer r19 = java.lang.Integer.valueOf(r5)
            r0 = r19
            r6.add(r0)
            int r10 = r10 + 1
            goto L_0x018b
        L_0x019b:
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            java.lang.String[] r19 = r19.getExtraLabels()
            r0 = r19
            java.util.Collections.addAll(r13, r0)
        L_0x01aa:
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            r0 = r19
            r0.setComputedColors(r6)
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            r0 = r19
            r0.setComputedLabels(r13)
        L_0x01c0:
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            android.graphics.Typeface r17 = r19.getTypeface()
            if (r17 == 0) goto L_0x01d9
            r0 = r22
            android.graphics.Paint r0 = r0.mLegendLabelPaint
            r19 = r0
            r0 = r19
            r1 = r17
            r0.setTypeface(r1)
        L_0x01d9:
            r0 = r22
            android.graphics.Paint r0 = r0.mLegendLabelPaint
            r19 = r0
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r20 = r0
            float r20 = r20.getTextSize()
            r19.setTextSize(r20)
            r0 = r22
            android.graphics.Paint r0 = r0.mLegendLabelPaint
            r19 = r0
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r20 = r0
            int r20 = r20.getTextColor()
            r19.setColor(r20)
            r0 = r22
            com.github.mikephil.charting.components.Legend r0 = r0.mLegend
            r19 = r0
            r0 = r22
            android.graphics.Paint r0 = r0.mLegendLabelPaint
            r20 = r0
            r0 = r22
            com.github.mikephil.charting.utils.ViewPortHandler r0 = r0.mViewPortHandler
            r21 = r0
            r19.calculateDimensions(r20, r21)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.LegendRenderer.computeLegend(com.github.mikephil.charting.data.ChartData):void");
    }

    public void renderLegend(Canvas c) {
        float posX;
        float posY;
        float originPosX;
        float posY2;
        float f;
        float f2;
        if (this.mLegend.isEnabled()) {
            Typeface tf = this.mLegend.getTypeface();
            if (tf != null) {
                this.mLegendLabelPaint.setTypeface(tf);
            }
            this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
            this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
            float labelLineHeight = Utils.getLineHeight(this.mLegendLabelPaint);
            float labelLineSpacing = Utils.getLineSpacing(this.mLegendLabelPaint) + this.mLegend.getYEntrySpace();
            float formYOffset = labelLineHeight - (((float) Utils.calcTextHeight(this.mLegendLabelPaint, "ABC")) / 2.0f);
            String[] labels = this.mLegend.getLabels();
            int[] colors = this.mLegend.getColors();
            float formToTextSpace = this.mLegend.getFormToTextSpace();
            float xEntrySpace = this.mLegend.getXEntrySpace();
            Legend.LegendDirection direction = this.mLegend.getDirection();
            float formSize = this.mLegend.getFormSize();
            float stackSpace = this.mLegend.getStackSpace();
            float yoffset = this.mLegend.getYOffset();
            float xoffset = this.mLegend.getXOffset();
            Legend.LegendPosition legendPosition = this.mLegend.getPosition();
            switch (legendPosition) {
                case BELOW_CHART_LEFT:
                case BELOW_CHART_RIGHT:
                case BELOW_CHART_CENTER:
                case ABOVE_CHART_LEFT:
                case ABOVE_CHART_RIGHT:
                case ABOVE_CHART_CENTER:
                    float contentWidth = this.mViewPortHandler.contentWidth();
                    if (legendPosition == Legend.LegendPosition.BELOW_CHART_LEFT || legendPosition == Legend.LegendPosition.ABOVE_CHART_LEFT) {
                        originPosX = this.mViewPortHandler.contentLeft() + xoffset;
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            originPosX += this.mLegend.mNeededWidth;
                        }
                    } else if (legendPosition == Legend.LegendPosition.BELOW_CHART_RIGHT || legendPosition == Legend.LegendPosition.ABOVE_CHART_RIGHT) {
                        originPosX = this.mViewPortHandler.contentRight() - xoffset;
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            originPosX -= this.mLegend.mNeededWidth;
                        }
                    } else {
                        originPosX = (this.mViewPortHandler.contentLeft() + (contentWidth / 2.0f)) - (this.mLegend.mNeededWidth / 2.0f);
                    }
                    FSize[] calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
                    FSize[] calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
                    Boolean[] calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
                    float posX2 = originPosX;
                    if (legendPosition == Legend.LegendPosition.ABOVE_CHART_LEFT || legendPosition == Legend.LegendPosition.ABOVE_CHART_RIGHT || legendPosition == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                        posY2 = 0.0f;
                    } else {
                        posY2 = (this.mViewPortHandler.getChartHeight() - yoffset) - this.mLegend.mNeededHeight;
                    }
                    int lineIndex = 0;
                    int count = labels.length;
                    for (int i = 0; i < count; i++) {
                        if (i < calculatedLabelBreakPoints.length && calculatedLabelBreakPoints[i].booleanValue()) {
                            posX2 = originPosX;
                            posY2 += labelLineHeight + labelLineSpacing;
                        }
                        if (posX2 == originPosX && legendPosition == Legend.LegendPosition.BELOW_CHART_CENTER && lineIndex < calculatedLineSizes.length) {
                            posX2 += (direction == Legend.LegendDirection.RIGHT_TO_LEFT ? calculatedLineSizes[lineIndex].width : -calculatedLineSizes[lineIndex].width) / 2.0f;
                            lineIndex++;
                        }
                        boolean drawingForm = colors[i] != 1122868;
                        boolean isStacked = labels[i] == null;
                        if (drawingForm) {
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX2 -= formSize;
                            }
                            drawForm(c, posX2, posY2 + formYOffset, i, this.mLegend);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX2 += formSize;
                            }
                        }
                        if (!isStacked) {
                            if (drawingForm) {
                                if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                    f2 = -formToTextSpace;
                                } else {
                                    f2 = formToTextSpace;
                                }
                                posX2 += f2;
                            }
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX2 -= calculatedLabelSizes[i].width;
                            }
                            drawLabel(c, posX2, posY2 + labelLineHeight, labels[i]);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX2 += calculatedLabelSizes[i].width;
                            }
                            f = direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -xEntrySpace : xEntrySpace;
                        } else {
                            f = direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -stackSpace : stackSpace;
                        }
                        posX2 += f;
                    }
                    return;
                case PIECHART_CENTER:
                case RIGHT_OF_CHART:
                case RIGHT_OF_CHART_CENTER:
                case RIGHT_OF_CHART_INSIDE:
                case LEFT_OF_CHART:
                case LEFT_OF_CHART_CENTER:
                case LEFT_OF_CHART_INSIDE:
                    float stack = 0.0f;
                    boolean wasStacked = false;
                    if (legendPosition == Legend.LegendPosition.PIECHART_CENTER) {
                        posX = (this.mViewPortHandler.getChartWidth() / 2.0f) + (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? (-this.mLegend.mTextWidthMax) / 2.0f : this.mLegend.mTextWidthMax / 2.0f);
                        posY = ((this.mViewPortHandler.getChartHeight() / 2.0f) - (this.mLegend.mNeededHeight / 2.0f)) + this.mLegend.getYOffset();
                    } else {
                        if (legendPosition == Legend.LegendPosition.RIGHT_OF_CHART || legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_CENTER || legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_INSIDE) {
                            posX = this.mViewPortHandler.getChartWidth() - xoffset;
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX -= this.mLegend.mTextWidthMax;
                            }
                        } else {
                            posX = xoffset;
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX += this.mLegend.mTextWidthMax;
                            }
                        }
                        if (legendPosition == Legend.LegendPosition.RIGHT_OF_CHART || legendPosition == Legend.LegendPosition.LEFT_OF_CHART) {
                            posY = this.mViewPortHandler.contentTop() + yoffset;
                        } else if (legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_CENTER || legendPosition == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                            posY = (this.mViewPortHandler.getChartHeight() / 2.0f) - (this.mLegend.mNeededHeight / 2.0f);
                        } else {
                            posY = this.mViewPortHandler.contentTop() + yoffset;
                        }
                    }
                    for (int i2 = 0; i2 < labels.length; i2++) {
                        Boolean drawingForm2 = Boolean.valueOf(colors[i2] != 1122868);
                        float x = posX;
                        if (drawingForm2.booleanValue()) {
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                x += stack;
                            } else {
                                x -= formSize - stack;
                            }
                            drawForm(c, x, posY + formYOffset, i2, this.mLegend);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                x += formSize;
                            }
                        }
                        if (labels[i2] != null) {
                            if (drawingForm2.booleanValue() && !wasStacked) {
                                x += direction == Legend.LegendDirection.LEFT_TO_RIGHT ? formToTextSpace : -formToTextSpace;
                            } else if (wasStacked) {
                                x = posX;
                            }
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                x -= (float) Utils.calcTextWidth(this.mLegendLabelPaint, labels[i2]);
                            }
                            if (!wasStacked) {
                                drawLabel(c, x, posY + labelLineHeight, labels[i2]);
                            } else {
                                posY += labelLineHeight + labelLineSpacing;
                                drawLabel(c, x, posY + labelLineHeight, labels[i2]);
                            }
                            posY += labelLineHeight + labelLineSpacing;
                            stack = 0.0f;
                        } else {
                            stack += formSize + stackSpace;
                            wasStacked = true;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawForm(Canvas c, float x, float y, int index, Legend legend) {
        if (legend.getColors()[index] != 1122868) {
            this.mLegendFormPaint.setColor(legend.getColors()[index]);
            float formsize = legend.getFormSize();
            float half = formsize / 2.0f;
            switch (legend.getForm()) {
                case CIRCLE:
                    c.drawCircle(x + half, y, half, this.mLegendFormPaint);
                    return;
                case SQUARE:
                    c.drawRect(x, y - half, x + formsize, y + half, this.mLegendFormPaint);
                    return;
                case LINE:
                    c.drawLine(x, y, x + formsize, y, this.mLegendFormPaint);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawLabel(Canvas c, float x, float y, String label) {
        c.drawText(label, x, y, this.mLegendLabelPaint);
    }
}

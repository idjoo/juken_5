package com.github.mikephil.charting.components;

import android.graphics.Paint;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.List;

public class Legend extends ComponentBase {
    private Boolean[] mCalculatedLabelBreakPoints;
    private FSize[] mCalculatedLabelSizes;
    private FSize[] mCalculatedLineSizes;
    private int[] mColors;
    private LegendDirection mDirection;
    private int[] mExtraColors;
    private String[] mExtraLabels;
    private float mFormSize;
    private float mFormToTextSpace;
    private boolean mIsLegendCustom;
    private String[] mLabels;
    private float mMaxSizePercent;
    public float mNeededHeight;
    public float mNeededWidth;
    private LegendPosition mPosition;
    private LegendForm mShape;
    private float mStackSpace;
    public float mTextHeightMax;
    public float mTextWidthMax;
    private boolean mWordWrapEnabled;
    private float mXEntrySpace;
    private float mYEntrySpace;

    public enum LegendDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public enum LegendForm {
        SQUARE,
        CIRCLE,
        LINE
    }

    public enum LegendPosition {
        RIGHT_OF_CHART,
        RIGHT_OF_CHART_CENTER,
        RIGHT_OF_CHART_INSIDE,
        LEFT_OF_CHART,
        LEFT_OF_CHART_CENTER,
        LEFT_OF_CHART_INSIDE,
        BELOW_CHART_LEFT,
        BELOW_CHART_RIGHT,
        BELOW_CHART_CENTER,
        ABOVE_CHART_LEFT,
        ABOVE_CHART_RIGHT,
        ABOVE_CHART_CENTER,
        PIECHART_CENTER
    }

    public Legend() {
        this.mIsLegendCustom = false;
        this.mPosition = LegendPosition.BELOW_CHART_LEFT;
        this.mDirection = LegendDirection.LEFT_TO_RIGHT;
        this.mShape = LegendForm.SQUARE;
        this.mFormSize = 8.0f;
        this.mXEntrySpace = 6.0f;
        this.mYEntrySpace = 0.0f;
        this.mFormToTextSpace = 5.0f;
        this.mStackSpace = 3.0f;
        this.mMaxSizePercent = 0.95f;
        this.mNeededWidth = 0.0f;
        this.mNeededHeight = 0.0f;
        this.mTextHeightMax = 0.0f;
        this.mTextWidthMax = 0.0f;
        this.mWordWrapEnabled = false;
        this.mCalculatedLabelSizes = new FSize[0];
        this.mCalculatedLabelBreakPoints = new Boolean[0];
        this.mCalculatedLineSizes = new FSize[0];
        this.mFormSize = Utils.convertDpToPixel(8.0f);
        this.mXEntrySpace = Utils.convertDpToPixel(6.0f);
        this.mYEntrySpace = Utils.convertDpToPixel(0.0f);
        this.mFormToTextSpace = Utils.convertDpToPixel(5.0f);
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mStackSpace = Utils.convertDpToPixel(3.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(4.0f);
    }

    public Legend(int[] colors, String[] labels) {
        this();
        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        } else if (colors.length != labels.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        } else {
            this.mColors = colors;
            this.mLabels = labels;
        }
    }

    public Legend(List<Integer> colors, List<String> labels) {
        this();
        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        } else if (colors.size() != labels.size()) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        } else {
            this.mColors = Utils.convertIntegers(colors);
            this.mLabels = Utils.convertStrings(labels);
        }
    }

    public void setComputedColors(List<Integer> colors) {
        this.mColors = Utils.convertIntegers(colors);
    }

    public void setComputedLabels(List<String> labels) {
        this.mLabels = Utils.convertStrings(labels);
    }

    public float getMaximumEntryWidth(Paint p) {
        float max = 0.0f;
        for (int i = 0; i < this.mLabels.length; i++) {
            if (this.mLabels[i] != null) {
                float length = (float) Utils.calcTextWidth(p, this.mLabels[i]);
                if (length > max) {
                    max = length;
                }
            }
        }
        return this.mFormSize + max + this.mFormToTextSpace;
    }

    public float getMaximumEntryHeight(Paint p) {
        float max = 0.0f;
        for (int i = 0; i < this.mLabels.length; i++) {
            if (this.mLabels[i] != null) {
                float length = (float) Utils.calcTextHeight(p, this.mLabels[i]);
                if (length > max) {
                    max = length;
                }
            }
        }
        return max;
    }

    public int[] getColors() {
        return this.mColors;
    }

    public String[] getLabels() {
        return this.mLabels;
    }

    public String getLabel(int index) {
        return this.mLabels[index];
    }

    public int[] getExtraColors() {
        return this.mExtraColors;
    }

    public String[] getExtraLabels() {
        return this.mExtraLabels;
    }

    public void setExtra(List<Integer> colors, List<String> labels) {
        this.mExtraColors = Utils.convertIntegers(colors);
        this.mExtraLabels = Utils.convertStrings(labels);
    }

    public void setExtra(int[] colors, String[] labels) {
        this.mExtraColors = colors;
        this.mExtraLabels = labels;
    }

    public void setCustom(int[] colors, String[] labels) {
        if (colors.length != labels.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mLabels = labels;
        this.mColors = colors;
        this.mIsLegendCustom = true;
    }

    public void setCustom(List<Integer> colors, List<String> labels) {
        if (colors.size() != labels.size()) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mColors = Utils.convertIntegers(colors);
        this.mLabels = Utils.convertStrings(labels);
        this.mIsLegendCustom = true;
    }

    public void resetCustom() {
        this.mIsLegendCustom = false;
    }

    public boolean isLegendCustom() {
        return this.mIsLegendCustom;
    }

    public LegendPosition getPosition() {
        return this.mPosition;
    }

    public void setPosition(LegendPosition pos) {
        this.mPosition = pos;
    }

    public LegendDirection getDirection() {
        return this.mDirection;
    }

    public void setDirection(LegendDirection pos) {
        this.mDirection = pos;
    }

    public LegendForm getForm() {
        return this.mShape;
    }

    public void setForm(LegendForm shape) {
        this.mShape = shape;
    }

    public void setFormSize(float size) {
        this.mFormSize = Utils.convertDpToPixel(size);
    }

    public float getFormSize() {
        return this.mFormSize;
    }

    public float getXEntrySpace() {
        return this.mXEntrySpace;
    }

    public void setXEntrySpace(float space) {
        this.mXEntrySpace = Utils.convertDpToPixel(space);
    }

    public float getYEntrySpace() {
        return this.mYEntrySpace;
    }

    public void setYEntrySpace(float space) {
        this.mYEntrySpace = Utils.convertDpToPixel(space);
    }

    public float getFormToTextSpace() {
        return this.mFormToTextSpace;
    }

    public void setFormToTextSpace(float space) {
        this.mFormToTextSpace = Utils.convertDpToPixel(space);
    }

    public float getStackSpace() {
        return this.mStackSpace;
    }

    public void setStackSpace(float space) {
        this.mStackSpace = space;
    }

    public float getFullWidth(Paint labelpaint) {
        float width = 0.0f;
        for (int i = 0; i < this.mLabels.length; i++) {
            if (this.mLabels[i] != null) {
                if (this.mColors[i] != 1122868) {
                    width += this.mFormSize + this.mFormToTextSpace;
                }
                width += (float) Utils.calcTextWidth(labelpaint, this.mLabels[i]);
                if (i < this.mLabels.length - 1) {
                    width += this.mXEntrySpace;
                }
            } else {
                width += this.mFormSize;
                if (i < this.mLabels.length - 1) {
                    width += this.mStackSpace;
                }
            }
        }
        return width;
    }

    public float getFullHeight(Paint labelpaint) {
        float height = 0.0f;
        for (int i = 0; i < this.mLabels.length; i++) {
            if (this.mLabels[i] != null) {
                height += (float) Utils.calcTextHeight(labelpaint, this.mLabels[i]);
                if (i < this.mLabels.length - 1) {
                    height += this.mYEntrySpace;
                }
            }
        }
        return height;
    }

    public void setWordWrapEnabled(boolean enabled) {
        this.mWordWrapEnabled = enabled;
    }

    public boolean isWordWrapEnabled() {
        return this.mWordWrapEnabled;
    }

    public float getMaxSizePercent() {
        return this.mMaxSizePercent;
    }

    public void setMaxSizePercent(float maxSize) {
        this.mMaxSizePercent = maxSize;
    }

    public FSize[] getCalculatedLabelSizes() {
        return this.mCalculatedLabelSizes;
    }

    public Boolean[] getCalculatedLabelBreakPoints() {
        return this.mCalculatedLabelBreakPoints;
    }

    public FSize[] getCalculatedLineSizes() {
        return this.mCalculatedLineSizes;
    }

    public void calculateDimensions(Paint labelpaint, ViewPortHandler viewPortHandler) {
        float requiredWidth;
        float requiredSpacing;
        int i;
        if (this.mPosition == LegendPosition.RIGHT_OF_CHART || this.mPosition == LegendPosition.RIGHT_OF_CHART_CENTER || this.mPosition == LegendPosition.LEFT_OF_CHART || this.mPosition == LegendPosition.LEFT_OF_CHART_CENTER || this.mPosition == LegendPosition.PIECHART_CENTER) {
            this.mNeededWidth = getMaximumEntryWidth(labelpaint);
            this.mNeededHeight = getFullHeight(labelpaint);
            this.mTextWidthMax = this.mNeededWidth;
            this.mTextHeightMax = getMaximumEntryHeight(labelpaint);
        } else if (this.mPosition == LegendPosition.BELOW_CHART_LEFT || this.mPosition == LegendPosition.BELOW_CHART_RIGHT || this.mPosition == LegendPosition.BELOW_CHART_CENTER || this.mPosition == LegendPosition.ABOVE_CHART_LEFT || this.mPosition == LegendPosition.ABOVE_CHART_RIGHT || this.mPosition == LegendPosition.ABOVE_CHART_CENTER) {
            int labelCount = this.mLabels.length;
            float labelLineHeight = Utils.getLineHeight(labelpaint);
            float labelLineSpacing = Utils.getLineSpacing(labelpaint) + this.mYEntrySpace;
            float contentWidth = viewPortHandler.contentWidth();
            ArrayList<FSize> calculatedLabelSizes = new ArrayList<>(labelCount);
            ArrayList<Boolean> calculatedLabelBreakPoints = new ArrayList<>(labelCount);
            ArrayList<FSize> calculatedLineSizes = new ArrayList<>();
            float maxLineWidth = 0.0f;
            float currentLineWidth = 0.0f;
            float requiredWidth2 = 0.0f;
            int stackedStartIndex = -1;
            for (int i2 = 0; i2 < labelCount; i2++) {
                boolean drawingForm = this.mColors[i2] != 1122868;
                calculatedLabelBreakPoints.add(false);
                if (stackedStartIndex == -1) {
                    requiredWidth = 0.0f;
                } else {
                    requiredWidth = requiredWidth2 + this.mStackSpace;
                }
                if (this.mLabels[i2] != null) {
                    calculatedLabelSizes.add(Utils.calcTextSize(labelpaint, this.mLabels[i2]));
                    requiredWidth2 = requiredWidth + (drawingForm ? this.mFormToTextSpace + this.mFormSize : 0.0f) + calculatedLabelSizes.get(i2).width;
                } else {
                    calculatedLabelSizes.add(new FSize(0.0f, 0.0f));
                    requiredWidth2 = requiredWidth + (drawingForm ? this.mFormSize : 0.0f);
                    if (stackedStartIndex == -1) {
                        stackedStartIndex = i2;
                    }
                }
                if (this.mLabels[i2] != null || i2 == labelCount - 1) {
                    if (currentLineWidth == 0.0f) {
                        requiredSpacing = 0.0f;
                    } else {
                        requiredSpacing = this.mXEntrySpace;
                    }
                    if (!this.mWordWrapEnabled || currentLineWidth == 0.0f || contentWidth - currentLineWidth >= requiredSpacing + requiredWidth2) {
                        currentLineWidth += requiredSpacing + requiredWidth2;
                    } else {
                        calculatedLineSizes.add(new FSize(currentLineWidth, labelLineHeight));
                        maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                        if (stackedStartIndex > -1) {
                            i = stackedStartIndex;
                        } else {
                            i = i2;
                        }
                        calculatedLabelBreakPoints.set(i, 1);
                        currentLineWidth = requiredWidth2;
                    }
                    if (i2 == labelCount - 1) {
                        calculatedLineSizes.add(new FSize(currentLineWidth, labelLineHeight));
                        maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                    }
                }
                if (this.mLabels[i2] != null) {
                    stackedStartIndex = -1;
                }
            }
            this.mCalculatedLabelSizes = (FSize[]) calculatedLabelSizes.toArray(new FSize[calculatedLabelSizes.size()]);
            this.mCalculatedLabelBreakPoints = (Boolean[]) calculatedLabelBreakPoints.toArray(new Boolean[calculatedLabelBreakPoints.size()]);
            this.mCalculatedLineSizes = (FSize[]) calculatedLineSizes.toArray(new FSize[calculatedLineSizes.size()]);
            this.mTextWidthMax = getMaximumEntryWidth(labelpaint);
            this.mTextHeightMax = getMaximumEntryHeight(labelpaint);
            this.mNeededWidth = maxLineWidth;
            this.mNeededHeight = (((float) (this.mCalculatedLineSizes.length == 0 ? 0 : this.mCalculatedLineSizes.length - 1)) * labelLineSpacing) + (labelLineHeight * ((float) this.mCalculatedLineSizes.length));
        } else {
            this.mNeededWidth = getFullWidth(labelpaint);
            this.mNeededHeight = getMaximumEntryHeight(labelpaint);
            this.mTextWidthMax = getMaximumEntryWidth(labelpaint);
            this.mTextHeightMax = this.mNeededHeight;
        }
    }
}

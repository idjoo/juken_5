package juken.android.com.juken_5;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class GaugeView extends View {
    public static final float BOTTOM = 1.0f;
    public static final float CENTER = 0.5f;
    public static final float INNER_RIM_BORDER_WIDTH = 0.005f;
    public static final float INNER_RIM_WIDTH = 0.06f;
    public static final float LEFT = 0.0f;
    public static final float NEEDLE_HEIGHT = 0.28f;
    public static final float NEEDLE_WIDTH = 0.02f;
    public static final float OUTER_BORDER_WIDTH = 0.04f;
    public static final float OUTER_RIM_WIDTH = 0.05f;
    public static final int[] OUTER_SHADOW_COLORS = {Color.argb(40, 255, 254, 187), Color.argb(20, 255, 247, 219), Color.argb(5, 255, 255, 255)};
    public static final float[] OUTER_SHADOW_POS = {0.9f, 0.95f, 0.99f};
    public static final float OUTER_SHADOW_WIDTH = 0.03f;
    public static final int[] RANGE_COLORS = {Color.rgb(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), Color.rgb(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), Color.rgb(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), Color.rgb(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)};
    public static final float[] RANGE_VALUES = {16.0f, 25.0f, 40.0f, 100.0f};
    public static final float RIGHT = 1.0f;
    public static final int SCALE_DIVISIONS = 15;
    public static final float SCALE_END_VALUE = 16.0f;
    public static final float SCALE_POSITION = 0.025f;
    public static final float SCALE_START_ANGLE = 30.0f;
    public static final float SCALE_START_VALUE = 0.0f;
    public static final int SCALE_SUBDIVISIONS = 5;
    public static final boolean SHOW_INNER_RIM = true;
    public static final boolean SHOW_NEEDLE = true;
    public static final boolean SHOW_OUTER_BORDER = true;
    public static final boolean SHOW_OUTER_RIM = true;
    public static final boolean SHOW_OUTER_SHADOW = true;
    public static final boolean SHOW_RANGES = true;
    public static final boolean SHOW_SCALE = false;
    public static final boolean SHOW_TEXT = false;
    public static final int SIZE = 300;
    public static final int TEXT_SHADOW_COLOR = Color.argb(100, 0, 0, 0);
    public static final int TEXT_UNIT_COLOR = -1;
    public static final float TEXT_UNIT_SIZE = 0.1f;
    public static final int TEXT_VALUE_COLOR = -1;
    public static final float TEXT_VALUE_SIZE = 0.3f;
    public static final float TOP = 0.0f;
    private Bitmap mBackground;
    private Paint mBackgroundPaint;
    private float mCurrentValue;
    private float mDivisionValue;
    private int mDivisions;
    private Paint mFaceBorderPaint;
    private Paint mFacePaint;
    private RectF mFaceRect;
    private Paint mFaceShadowPaint;
    private Paint mInnerRimBorderDarkPaint;
    private Paint mInnerRimBorderLightPaint;
    private RectF mInnerRimBorderRect;
    private float mInnerRimBorderWidth;
    private Paint mInnerRimPaint;
    private RectF mInnerRimRect;
    private float mInnerRimWidth;
    private float mNeedleAcceleration;
    private float mNeedleHeight;
    private boolean mNeedleInitialized;
    private long mNeedleLastMoved;
    private Paint mNeedleLeftPaint;
    private Path mNeedleLeftPath;
    private Paint mNeedleRightPaint;
    private Path mNeedleRightPath;
    private Paint mNeedleScrewBorderPaint;
    private Paint mNeedleScrewPaint;
    private float mNeedleVelocity;
    private float mNeedleWidth;
    private Paint mOuterBorderPaint;
    private RectF mOuterBorderRect;
    private float mOuterBorderWidth;
    private Paint mOuterRimPaint;
    private RectF mOuterRimRect;
    private float mOuterRimWidth;
    private Paint mOuterShadowPaint;
    private RectF mOuterShadowRect;
    private float mOuterShadowWidth;
    private int[] mRangeColors;
    private Paint[] mRangePaints;
    private float[] mRangeValues;
    private float mScaleEndAngle;
    private float mScaleEndValue;
    private float mScalePosition;
    private RectF mScaleRect;
    private float mScaleRotation;
    private float mScaleStartAngle;
    private float mScaleStartValue;
    private boolean mShowInnerRim;
    private boolean mShowNeedle;
    private boolean mShowOuterBorder;
    private boolean mShowOuterRim;
    private boolean mShowOuterShadow;
    private boolean mShowRanges;
    private boolean mShowScale;
    private boolean mShowText;
    private float mSubdivisionAngle;
    private float mSubdivisionValue;
    private int mSubdivisions;
    private float mTargetValue;
    private int mTextShadowColor;
    private String mTextUnit;
    private int mTextUnitColor;
    private Paint mTextUnitPaint;
    private float mTextUnitSize;
    private String mTextValue;
    private int mTextValueColor;
    private Paint mTextValuePaint;
    private float mTextValueSize;

    public GaugeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mNeedleLastMoved = -1;
        readAttrs(context, attrs, defStyle);
        init();
    }

    public GaugeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    private void readAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GaugeView, defStyle, 0);
        this.mShowOuterShadow = a.getBoolean(0, true);
        this.mShowOuterBorder = a.getBoolean(1, true);
        this.mShowOuterRim = a.getBoolean(2, true);
        this.mShowInnerRim = a.getBoolean(3, true);
        this.mShowNeedle = a.getBoolean(4, true);
        this.mShowScale = a.getBoolean(5, false);
        this.mShowRanges = a.getBoolean(6, true);
        this.mShowText = a.getBoolean(7, false);
        this.mOuterShadowWidth = this.mShowOuterShadow ? a.getFloat(8, 0.03f) : 0.0f;
        this.mOuterBorderWidth = this.mShowOuterBorder ? a.getFloat(9, 0.04f) : 0.0f;
        this.mOuterRimWidth = this.mShowOuterRim ? a.getFloat(10, 0.05f) : 0.0f;
        this.mInnerRimWidth = this.mShowInnerRim ? a.getFloat(11, 0.06f) : 0.0f;
        this.mInnerRimBorderWidth = this.mShowInnerRim ? a.getFloat(12, 0.005f) : 0.0f;
        this.mNeedleWidth = a.getFloat(13, 0.02f);
        this.mNeedleHeight = a.getFloat(14, 0.28f);
        this.mScalePosition = (this.mShowScale || this.mShowRanges) ? a.getFloat(15, 0.025f) : 0.0f;
        this.mScaleStartValue = a.getFloat(16, 0.0f);
        this.mScaleEndValue = a.getFloat(17, 16.0f);
        this.mScaleStartAngle = a.getFloat(18, 30.0f);
        this.mScaleEndAngle = a.getFloat(19, 360.0f - this.mScaleStartAngle);
        this.mDivisions = a.getInteger(20, 15);
        this.mSubdivisions = a.getInteger(21, 5);
        if (this.mShowRanges) {
            this.mTextShadowColor = a.getColor(30, TEXT_SHADOW_COLOR);
            readRanges(a.getTextArray(22), a.getTextArray(23));
        }
        if (this.mShowText) {
            int textValueId = a.getResourceId(24, 0);
            String textValue = a.getString(24);
            if (textValueId > 0) {
                textValue = context.getString(textValueId);
            } else if (textValue == null) {
                textValue = "";
            }
            this.mTextValue = textValue;
            int textUnitId = a.getResourceId(27, 0);
            String textUnit = a.getString(27);
            if (textUnitId > 0) {
                textUnit = context.getString(textUnitId);
            } else if (textUnit == null) {
                textUnit = "";
            }
            this.mTextUnit = textUnit;
            this.mTextValueColor = a.getColor(25, -1);
            this.mTextUnitColor = a.getColor(28, -1);
            this.mTextShadowColor = a.getColor(30, TEXT_SHADOW_COLOR);
            this.mTextValueSize = a.getFloat(26, 0.3f);
            this.mTextUnitSize = a.getFloat(29, 0.1f);
        }
        a.recycle();
    }

    private void readRanges(CharSequence[] rangeValues, CharSequence[] rangeColors) {
        int rangeValuesLength;
        int rangeColorsLength;
        if (rangeValues == null) {
            rangeValuesLength = RANGE_VALUES.length;
        } else {
            rangeValuesLength = rangeValues.length;
        }
        if (rangeColors == null) {
            rangeColorsLength = RANGE_COLORS.length;
        } else {
            rangeColorsLength = rangeColors.length;
        }
        if (rangeValuesLength != rangeColorsLength) {
            throw new IllegalArgumentException("The ranges and colors arrays must have the same length.");
        }
        int length = rangeValuesLength;
        if (rangeValues != null) {
            this.mRangeValues = new float[length];
            for (int i = 0; i < length; i++) {
                this.mRangeValues[i] = Float.parseFloat(rangeValues[i].toString());
            }
        } else {
            this.mRangeValues = RANGE_VALUES;
        }
        if (rangeColors != null) {
            this.mRangeColors = new int[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.mRangeColors[i2] = Color.parseColor(rangeColors[i2].toString());
            }
            return;
        }
        this.mRangeColors = RANGE_COLORS;
    }

    private void init() {
        initDrawingRects();
        initDrawingTools();
        if (this.mShowRanges) {
            initScale();
        }
    }

    public void initDrawingRects() {
        this.mOuterShadowRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.mOuterBorderRect = new RectF(this.mOuterShadowRect.left + this.mOuterShadowWidth, this.mOuterShadowRect.top + this.mOuterShadowWidth, this.mOuterShadowRect.right - this.mOuterShadowWidth, this.mOuterShadowRect.bottom - this.mOuterShadowWidth);
        this.mOuterRimRect = new RectF(this.mOuterBorderRect.left + this.mOuterBorderWidth, this.mOuterBorderRect.top + this.mOuterBorderWidth, this.mOuterBorderRect.right - this.mOuterBorderWidth, this.mOuterBorderRect.bottom - this.mOuterBorderWidth);
        this.mInnerRimRect = new RectF(this.mOuterRimRect.left + this.mOuterRimWidth, this.mOuterRimRect.top + this.mOuterRimWidth, this.mOuterRimRect.right - this.mOuterRimWidth, this.mOuterRimRect.bottom - this.mOuterRimWidth);
        this.mInnerRimBorderRect = new RectF(this.mInnerRimRect.left + this.mInnerRimBorderWidth, this.mInnerRimRect.top + this.mInnerRimBorderWidth, this.mInnerRimRect.right - this.mInnerRimBorderWidth, this.mInnerRimRect.bottom - this.mInnerRimBorderWidth);
        this.mFaceRect = new RectF(this.mInnerRimRect.left + this.mInnerRimWidth, this.mInnerRimRect.top + this.mInnerRimWidth, this.mInnerRimRect.right - this.mInnerRimWidth, this.mInnerRimRect.bottom - this.mInnerRimWidth);
        this.mScaleRect = new RectF(this.mFaceRect.left + this.mScalePosition, this.mFaceRect.top + this.mScalePosition, this.mFaceRect.right - this.mScalePosition, this.mFaceRect.bottom - this.mScalePosition);
    }

    private void initDrawingTools() {
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setFilterBitmap(true);
        if (this.mShowOuterShadow) {
            this.mOuterShadowPaint = getDefaultOuterShadowPaint();
        }
        if (this.mShowOuterBorder) {
            this.mOuterBorderPaint = getDefaultOuterBorderPaint();
        }
        if (this.mShowOuterRim) {
            this.mOuterRimPaint = getDefaultOuterRimPaint();
        }
        if (this.mShowInnerRim) {
            this.mInnerRimPaint = getDefaultInnerRimPaint();
            this.mInnerRimBorderLightPaint = getDefaultInnerRimBorderLightPaint();
            this.mInnerRimBorderDarkPaint = getDefaultInnerRimBorderDarkPaint();
        }
        if (this.mShowRanges) {
            setDefaultScaleRangePaints();
        }
        if (this.mShowNeedle) {
            setDefaultNeedlePaths();
            this.mNeedleLeftPaint = getDefaultNeedleLeftPaint();
            this.mNeedleRightPaint = getDefaultNeedleRightPaint();
            this.mNeedleScrewPaint = getDefaultNeedleScrewPaint();
            this.mNeedleScrewBorderPaint = getDefaultNeedleScrewBorderPaint();
        }
        if (this.mShowText) {
            this.mTextValuePaint = getDefaultTextValuePaint();
            this.mTextUnitPaint = getDefaultTextUnitPaint();
        }
        this.mFacePaint = getDefaultFacePaint();
        this.mFaceBorderPaint = getDefaultFaceBorderPaint();
        this.mFaceShadowPaint = getDefaultFaceShadowPaint();
    }

    public Paint getDefaultOuterShadowPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mOuterShadowRect.width() / 2.0f, OUTER_SHADOW_COLORS, OUTER_SHADOW_POS, Shader.TileMode.MIRROR));
        return paint;
    }

    private Paint getDefaultOuterBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(245, 0, 0, 0));
        return paint;
    }

    public Paint getDefaultOuterRimPaint() {
        LinearGradient verticalGradient = new LinearGradient(this.mOuterRimRect.left, this.mOuterRimRect.top, this.mOuterRimRect.left, this.mOuterRimRect.bottom, Color.rgb(255, 255, 255), Color.rgb(84, 90, 100), Shader.TileMode.REPEAT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light_alu);
        BitmapShader aluminiumTile = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Matrix matrix = new Matrix();
        matrix.setScale(1.0f / ((float) bitmap.getWidth()), 1.0f / ((float) bitmap.getHeight()));
        aluminiumTile.setLocalMatrix(matrix);
        Paint paint = new Paint(1);
        paint.setShader(new ComposeShader(verticalGradient, aluminiumTile, PorterDuff.Mode.MULTIPLY));
        paint.setFilterBitmap(true);
        return paint;
    }

    private Paint getDefaultInnerRimPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new LinearGradient(this.mInnerRimRect.left, this.mInnerRimRect.top, this.mInnerRimRect.left, this.mInnerRimRect.bottom, new int[]{Color.argb(255, 68, 73, 80), Color.argb(255, 91, 97, 105), Color.argb(255, 178, 180, 183), Color.argb(255, 188, 188, 190), Color.argb(255, 84, 90, 100), Color.argb(255, 137, 137, 137)}, new float[]{0.0f, 0.1f, 0.2f, 0.4f, 0.8f, 1.0f}, Shader.TileMode.CLAMP));
        return paint;
    }

    private Paint getDefaultInnerRimBorderLightPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 255, 255, 255));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    private Paint getDefaultInnerRimBorderDarkPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 81, 84, 89));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public Paint getDefaultFacePaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mFaceRect.width() / 2.0f, new int[]{Color.rgb(50, 132, 206), Color.rgb(36, 89, 162), Color.rgb(27, 59, 131)}, new float[]{0.5f, 0.96f, 0.99f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public Paint getDefaultFaceBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 81, 84, 89));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public Paint getDefaultFaceShadowPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mFaceRect.width() / 2.0f, new int[]{Color.argb(60, 40, 96, 170), Color.argb(80, 15, 34, 98), Color.argb(120, 0, 0, 0), Color.argb(140, 0, 0, 0)}, new float[]{0.6f, 0.85f, 0.96f, 0.99f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public void setDefaultNeedlePaths() {
        this.mNeedleLeftPath = new Path();
        this.mNeedleLeftPath.moveTo(0.5f, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f - this.mNeedleWidth, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f, 0.5f - this.mNeedleHeight);
        this.mNeedleLeftPath.lineTo(0.5f, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f - this.mNeedleWidth, 0.5f);
        this.mNeedleRightPath = new Path();
        this.mNeedleRightPath.moveTo(0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(this.mNeedleWidth + 0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(0.5f, 0.5f - this.mNeedleHeight);
        this.mNeedleRightPath.lineTo(0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(this.mNeedleWidth + 0.5f, 0.5f);
    }

    public Paint getDefaultNeedleLeftPaint() {
        Paint paint = new Paint(1);
        paint.setColor(Color.rgb(176, 10, 19));
        return paint;
    }

    public Paint getDefaultNeedleRightPaint() {
        Paint paint = new Paint(1);
        paint.setColor(Color.rgb(252, 18, 30));
        paint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
        return paint;
    }

    public Paint getDefaultNeedleScrewPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, 0.07f, new int[]{Color.rgb(171, 171, 171), -1}, new float[]{0.05f, 0.9f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public Paint getDefaultNeedleScrewBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 81, 84, 89));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public void setDefaultScaleRangePaints() {
        int length = this.mRangeValues.length;
        this.mRangePaints = new Paint[length];
        for (int i = 0; i < length; i++) {
            this.mRangePaints[i] = new Paint(65);
            this.mRangePaints[i].setColor(this.mRangeColors[i]);
            this.mRangePaints[i].setStyle(Paint.Style.STROKE);
            this.mRangePaints[i].setStrokeWidth(0.005f);
            this.mRangePaints[i].setTextSize(0.05f);
            this.mRangePaints[i].setTypeface(Typeface.SANS_SERIF);
            this.mRangePaints[i].setTextAlign(Paint.Align.CENTER);
            this.mRangePaints[i].setShadowLayer(0.005f, 0.002f, 0.002f, this.mTextShadowColor);
        }
    }

    public Paint getDefaultTextValuePaint() {
        Paint paint = new Paint(65);
        paint.setColor(this.mTextValueColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(this.mTextValueSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, this.mTextShadowColor);
        return paint;
    }

    public Paint getDefaultTextUnitPaint() {
        Paint paint = new Paint(65);
        paint.setColor(this.mTextUnitColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(this.mTextUnitSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, this.mTextShadowColor);
        return paint;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        super.onRestoreInstanceState(bundle.getParcelable("superState"));
        this.mNeedleInitialized = bundle.getBoolean("needleInitialized");
        this.mNeedleVelocity = bundle.getFloat("needleVelocity");
        this.mNeedleAcceleration = bundle.getFloat("needleAcceleration");
        this.mNeedleLastMoved = bundle.getLong("needleLastMoved");
        this.mCurrentValue = bundle.getFloat("currentValue");
        this.mTargetValue = bundle.getFloat("targetValue");
    }

    private void initScale() {
        this.mScaleRotation = (this.mScaleStartAngle + 180.0f) % 360.0f;
        this.mDivisionValue = (this.mScaleEndValue - this.mScaleStartValue) / ((float) this.mDivisions);
        this.mSubdivisionValue = this.mDivisionValue / ((float) this.mSubdivisions);
        this.mSubdivisionAngle = (this.mScaleEndAngle - this.mScaleStartAngle) / ((float) (this.mDivisions * this.mSubdivisions));
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("needleInitialized", this.mNeedleInitialized);
        state.putFloat("needleVelocity", this.mNeedleVelocity);
        state.putFloat("needleAcceleration", this.mNeedleAcceleration);
        state.putLong("needleLastMoved", this.mNeedleLastMoved);
        state.putFloat("currentValue", this.mCurrentValue);
        state.putFloat("targetValue", this.mTargetValue);
        return state;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(chooseDimension(widthMode, View.MeasureSpec.getSize(widthMeasureSpec)), chooseDimension(heightMode, View.MeasureSpec.getSize(heightMeasureSpec)));
    }

    private int chooseDimension(int mode, int size) {
        switch (mode) {
            case Integer.MIN_VALUE:
            case 1073741824:
                return size;
            default:
                return getDefaultDimension();
        }
    }

    private int getDefaultDimension() {
        return SIZE;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        drawGauge();
    }

    private void drawGauge() {
        float f = 0.0f;
        if (this.mBackground != null) {
            this.mBackground.recycle();
        }
        this.mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mBackground);
        float scale = (float) Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        float width = scale == ((float) getHeight()) ? ((((float) getWidth()) - scale) / 2.0f) / scale : 0.0f;
        if (scale == ((float) getWidth())) {
            f = ((((float) getHeight()) - scale) / 2.0f) / scale;
        }
        canvas.translate(width, f);
        drawRim(canvas);
        drawFace(canvas);
        if (this.mShowRanges) {
            drawScale(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f = 0.0f;
        drawBackground(canvas);
        float scale = (float) Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        float width = scale == ((float) getHeight()) ? ((((float) getWidth()) - scale) / 2.0f) / scale : 0.0f;
        if (scale == ((float) getWidth())) {
            f = ((((float) getHeight()) - scale) / 2.0f) / scale;
        }
        canvas.translate(width, f);
        if (this.mShowNeedle) {
            drawNeedle(canvas);
        }
        if (this.mShowText) {
            drawText(canvas);
        }
        computeCurrentValue();
    }

    private void drawBackground(Canvas canvas) {
        if (this.mBackground != null) {
            canvas.drawBitmap(this.mBackground, 0.0f, 0.0f, this.mBackgroundPaint);
        }
    }

    private void drawRim(Canvas canvas) {
        if (this.mShowOuterShadow) {
            canvas.drawOval(this.mOuterShadowRect, this.mOuterShadowPaint);
        }
        if (this.mShowOuterBorder) {
            canvas.drawOval(this.mOuterBorderRect, this.mOuterBorderPaint);
        }
        if (this.mShowOuterRim) {
            canvas.drawOval(this.mOuterRimRect, this.mOuterRimPaint);
        }
        if (this.mShowInnerRim) {
            canvas.drawOval(this.mInnerRimRect, this.mInnerRimPaint);
            canvas.drawOval(this.mInnerRimRect, this.mInnerRimBorderLightPaint);
            canvas.drawOval(this.mInnerRimBorderRect, this.mInnerRimBorderDarkPaint);
        }
    }

    private void drawFace(Canvas canvas) {
        canvas.drawOval(this.mFaceRect, this.mFacePaint);
        canvas.drawOval(this.mFaceRect, this.mFaceBorderPaint);
        canvas.drawOval(this.mFaceRect, this.mFaceShadowPaint);
    }

    private void drawText(Canvas canvas) {
        String textValue = !TextUtils.isEmpty(this.mTextValue) ? this.mTextValue : valueString(this.mCurrentValue);
        float textValueWidth = this.mTextValuePaint.measureText(textValue);
        canvas.drawText(textValue, 0.5f - ((!TextUtils.isEmpty(this.mTextUnit) ? this.mTextUnitPaint.measureText(this.mTextUnit) : 0.0f) / 2.0f), 0.6f, this.mTextValuePaint);
        if (!TextUtils.isEmpty(this.mTextUnit)) {
            canvas.drawText(this.mTextUnit, (textValueWidth / 2.0f) + 0.5f + 0.03f, 0.5f, this.mTextUnitPaint);
        }
    }

    private void drawScale(Canvas canvas) {
        canvas.save(1);
        canvas.rotate(this.mScaleRotation, 0.5f, 0.5f);
        int totalTicks = (this.mDivisions * this.mSubdivisions) + 1;
        for (int i = 0; i < totalTicks; i++) {
            float y1 = this.mScaleRect.top;
            float y2 = y1 + 0.015f;
            float y3 = y1 + 0.045f;
            float value = getValueForTick(i);
            Paint paint = getRangePaint(this.mScaleStartValue + value);
            float mod = value % this.mDivisionValue;
            if (((double) Math.abs(mod - 0.0f)) < 0.001d || ((double) Math.abs(mod - this.mDivisionValue)) < 0.001d) {
                canvas.drawLine(0.5f, y1, 0.5f, y3, paint);
                drawTextOnCanvasWithMagnifier(canvas, valueString(value), 0.5f, 0.045f + y3, paint);
            } else {
                canvas.drawLine(0.5f, y1, 0.5f, y2, paint);
            }
            canvas.rotate(this.mSubdivisionAngle, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    public static void drawTextOnCanvasWithMagnifier(Canvas canvas, String text, float x, float y, Paint paint) {
        if (Build.VERSION.SDK_INT <= 15) {
            canvas.drawText(text, x, y, paint);
            return;
        }
        float originalStrokeWidth = paint.getStrokeWidth();
        float originalTextSize = paint.getTextSize();
        canvas.save();
        canvas.scale(0.001f, 0.001f);
        paint.setTextSize(originalTextSize * 1000.0f);
        paint.setStrokeWidth(originalStrokeWidth * 1000.0f);
        canvas.drawText(text, x * 1000.0f, y * 1000.0f, paint);
        canvas.restore();
        paint.setTextSize(originalTextSize);
        paint.setStrokeWidth(originalStrokeWidth);
    }

    private String valueString(float value) {
        return String.format("%d", new Object[]{Integer.valueOf((int) value)});
    }

    private float getValueForTick(int tick) {
        return ((float) tick) * (this.mDivisionValue / ((float) this.mSubdivisions));
    }

    private Paint getRangePaint(float value) {
        int length = this.mRangeValues.length;
        for (int i = 0; i < length - 1; i++) {
            if (value < this.mRangeValues[i]) {
                return this.mRangePaints[i];
            }
        }
        if (value <= this.mRangeValues[length - 1]) {
            return this.mRangePaints[length - 1];
        }
        throw new IllegalArgumentException("Value " + value + " out of range!");
    }

    private void drawNeedle(Canvas canvas) {
        if (this.mNeedleInitialized) {
            float angle = getAngleForValue(this.mCurrentValue);
            canvas.save(1);
            canvas.rotate(angle, 0.5f, 0.5f);
            setNeedleShadowPosition(angle);
            canvas.drawPath(this.mNeedleLeftPath, this.mNeedleLeftPaint);
            canvas.drawPath(this.mNeedleRightPath, this.mNeedleRightPaint);
            canvas.restore();
            canvas.drawCircle(0.5f, 0.5f, 0.04f, this.mNeedleScrewPaint);
            canvas.drawCircle(0.5f, 0.5f, 0.04f, this.mNeedleScrewBorderPaint);
        }
    }

    private void setNeedleShadowPosition(float angle) {
        if (angle <= 180.0f || angle >= 360.0f) {
            this.mNeedleLeftPaint.setShadowLayer(0.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
            this.mNeedleRightPaint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
            return;
        }
        this.mNeedleRightPaint.setShadowLayer(0.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        this.mNeedleLeftPaint.setShadowLayer(0.01f, -0.005f, 0.005f, Color.argb(127, 0, 0, 0));
    }

    private float getAngleForValue(float value) {
        return (this.mScaleRotation + (((value - this.mScaleStartValue) / this.mSubdivisionValue) * this.mSubdivisionAngle)) % 360.0f;
    }

    private void computeCurrentValue() {
        if (Math.abs(this.mCurrentValue - this.mTargetValue) > 0.01f) {
            if (-1 != this.mNeedleLastMoved) {
                float time = ((float) (System.currentTimeMillis() - this.mNeedleLastMoved)) / 1000.0f;
                float direction = Math.signum(this.mNeedleVelocity);
                if (Math.abs(this.mNeedleVelocity) < 90.0f) {
                    this.mNeedleAcceleration = (this.mTargetValue - this.mCurrentValue) * 5.0f;
                } else {
                    this.mNeedleAcceleration = 0.0f;
                }
                this.mNeedleAcceleration = (this.mTargetValue - this.mCurrentValue) * 5.0f;
                this.mCurrentValue += this.mNeedleVelocity * time;
                this.mNeedleVelocity += this.mNeedleAcceleration * time;
                if ((this.mTargetValue - this.mCurrentValue) * direction < 0.01f * direction) {
                    this.mCurrentValue = this.mTargetValue;
                    this.mNeedleVelocity = 0.0f;
                    this.mNeedleAcceleration = 0.0f;
                    this.mNeedleLastMoved = -1;
                } else {
                    this.mNeedleLastMoved = System.currentTimeMillis();
                }
                invalidate();
                return;
            }
            this.mNeedleLastMoved = System.currentTimeMillis();
            computeCurrentValue();
        }
    }

    public void setTargetValue(float value) {
        if (!this.mShowScale && !this.mShowRanges) {
            this.mTargetValue = value;
        } else if (value < this.mScaleStartValue) {
            this.mTargetValue = this.mScaleStartValue;
        } else if (value > this.mScaleEndValue) {
            this.mTargetValue = this.mScaleEndValue;
        } else {
            this.mTargetValue = value;
        }
        this.mNeedleInitialized = true;
        invalidate();
    }
}

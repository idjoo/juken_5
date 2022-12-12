package juken.android.com.juken_5.data_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.VirtualDyno.virtual_dyno;

public class data_logger_utama extends Activity {
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    private static final int SERIES_SIZE = 2000;
    static final int TWO_FINGERS_DRAG = 2;
    Button BVDyno;
    XYGraphWidget Widget;
    /* access modifiers changed from: private */
    public XYPlot afrPlot;
    String[] arr_data;
    /* access modifiers changed from: private */
    public XYPlot batPlot;
    /* access modifiers changed from: private */
    public XYPlot bmPlot;
    float distBetweenFingers;
    /* access modifiers changed from: private */
    public XYPlot eotPlot;
    PointF firstFinger;
    /* access modifiers changed from: private */
    public XYPlot fuelPlot;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    int hitung = 0;
    /* access modifiers changed from: private */
    public XYPlot igPlot;
    /* access modifiers changed from: private */
    public XYPlot itPlot;
    int jumlah_data_logger;
    Boolean klik_pertama = true;
    float leftBoundary;
    /* access modifiers changed from: private */
    public XYPlot mapPlot;
    int max = 50;
    private PointF maxXY;
    int max_afr = 8;
    int max_bat = 0;
    int max_bm = 0;
    int max_eot = 0;
    int max_fuel = -100;
    int max_ig = 0;
    int max_it = 0;
    int max_map = 0;
    int max_rpm = 0;
    private PointF minXY;
    int min_afr = 20;
    int min_bat = 16;
    int min_bm = 20;
    int min_eot = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    int min_fuel = 100;
    int min_ig = 90;
    int min_it = 720;
    int min_map = 100;
    int min_rpm = 16000;
    int mode = 0;
    String newString;
    int posisi = -1;
    int posisi_kedua = 0;
    int posisi_pertama = 0;
    Random r = new Random();
    float rightBoundary;
    /* access modifiers changed from: private */
    public XYPlot rpmPlot;
    ScrollView scroll1;
    private Pair<Integer, XYSeries> selection;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (data_logger_utama.this.hitung < data_logger_utama.this.jumlah_data_logger) {
                    data_logger_utama.this.populateSeries1(data_logger_utama.this.hitung);
                    data_logger_utama.this.ReDrawAllPlot();
                    data_logger_utama.this.hitung++;
                } else {
                    data_logger_utama.this.handler.removeCallbacks(data_logger_utama.this.sendData);
                }
                data_logger_utama.this.handler.postDelayed(this, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private SimpleXYSeries series1 = null;
    private SimpleXYSeries series10 = null;
    /* access modifiers changed from: private */
    public SimpleXYSeries series2 = null;
    /* access modifiers changed from: private */
    public SimpleXYSeries series3 = null;
    private SimpleXYSeries series4 = null;
    private SimpleXYSeries series5 = null;
    private SimpleXYSeries series6 = null;
    private SimpleXYSeries series7 = null;
    private SimpleXYSeries series8 = null;
    private SimpleXYSeries series9 = null;
    boolean stopThread = false;
    TextView textAFR;
    TextView textBAT;
    TextView textBM;
    TextView textEOT;
    TextView textF;
    TextView textIG;
    TextView textIT;
    TextView textMAP;
    TextView textRPM;
    TextView textTPS;
    Boolean touch;
    /* access modifiers changed from: private */
    public XYPlot tpsPlot;
    float zoomRatio = 2.0f;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_logger_utama);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                this.newString = "";
            } else {
                this.newString = extras.getString("Posisi");
            }
        } else {
            this.newString = (String) savedInstanceState.getSerializable("Posisi");
        }
        GetList();
        this.textRPM = (TextView) findViewById(R.id.textRPM);
        this.textTPS = (TextView) findViewById(R.id.textTPS);
        this.textF = (TextView) findViewById(R.id.textF);
        this.textBM = (TextView) findViewById(R.id.textBM);
        this.textIG = (TextView) findViewById(R.id.textIG);
        this.textIT = (TextView) findViewById(R.id.textIT);
        this.textAFR = (TextView) findViewById(R.id.textAFR);
        this.textMAP = (TextView) findViewById(R.id.textMAP);
        this.textEOT = (TextView) findViewById(R.id.textEOT);
        this.textBAT = (TextView) findViewById(R.id.textBAT);
        this.scroll1 = (ScrollView) findViewById(R.id.scroll1);
        final Button Bevent = (Button) findViewById(R.id.ButtonClick);
        Bevent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Bevent.getText().equals("Click")) {
                    Bevent.setText("P/Z");
                    data_logger_utama.this.BVDyno.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    return;
                }
                Bevent.setText("Click");
            }
        });
        this.BVDyno = (Button) findViewById(R.id.ButtonVDyno);
        this.BVDyno.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.BVDyno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (data_logger_utama.this.BVDyno.getTextColors().getDefaultColor() == -16777216) {
                    data_logger_utama.this.BVDyno.setTextColor(SupportMenu.CATEGORY_MASK);
                    Bevent.setText("Click");
                    data_logger_utama.this.klik_pertama = true;
                    return;
                }
                data_logger_utama.this.BVDyno.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            }
        });
        this.tpsPlot = (XYPlot) findViewById(R.id.tpsPlot);
        this.tpsPlot.setRangeLabel("");
        this.tpsPlot.setDomainLabel("");
        this.tpsPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0d);
        this.tpsPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.tpsPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0d);
        this.tpsPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        this.tpsPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.tpsPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.tpsPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.tpsPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.tpsPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.tpsPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.tpsPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.rpmPlot = (XYPlot) findViewById(R.id.rpmPlot);
        this.rpmPlot.setRangeLabel("");
        this.rpmPlot.setDomainLabel("");
        this.rpmPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0d);
        this.rpmPlot.setRangeBoundaries(Integer.valueOf(this.min_rpm), Integer.valueOf(this.max_rpm), BoundaryMode.FIXED);
        this.rpmPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 500.0d);
        this.rpmPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.rpmPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.rpmPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.rpmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.rpmPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.rpmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.rpmPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.Widget = this.rpmPlot.getGraph();
        this.touch = false;
        this.rpmPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                } else if (event.getAction() == 0) {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    if (data_logger_utama.this.posisi != -1) {
                        data_logger_utama.this.posisi_pertama = data_logger_utama.this.posisi;
                        data_logger_utama.this.posisi = -1;
                    }
                } else if (event.getAction() == 2) {
                    if (data_logger_utama.this.touch.booleanValue()) {
                        data_logger_utama.this.onPlotClicked3(new PointF(event.getX(), event.getY()));
                    }
                } else if (event.getAction() == 1) {
                    if (data_logger_utama.this.posisi != -1) {
                        data_logger_utama.this.posisi_kedua = data_logger_utama.this.posisi;
                        data_logger_utama.this.posisi = -1;
                    }
                    if (!(!data_logger_utama.this.touch.booleanValue() || data_logger_utama.this.posisi_pertama == 0 || data_logger_utama.this.posisi_kedua == 0)) {
                        data_logger_utama.this.touch = false;
                        data_logger_utama.this.widgetClear();
                        data_logger_utama.this.plotRefresh();
                        if (data_logger_utama.this.posisi_kedua < data_logger_utama.this.posisi_pertama) {
                            data_logger_utama.this.posisi_pertama += data_logger_utama.this.posisi_kedua;
                            data_logger_utama.this.posisi_kedua = data_logger_utama.this.posisi_pertama - data_logger_utama.this.posisi_kedua;
                            data_logger_utama.this.posisi_pertama -= data_logger_utama.this.posisi_kedua;
                        }
                        MappingHandle.list_VDyno_afr.clear();
                        MappingHandle.list_VDyno_rpm.clear();
                        for (int i = data_logger_utama.this.posisi_pertama; i <= data_logger_utama.this.posisi_kedua; i++) {
                            MappingHandle.list_VDyno_rpm.add(data_logger_utama.this.series2.getY(i).toString());
                            MappingHandle.list_VDyno_afr.add(data_logger_utama.this.series3.getY(i).toString());
                        }
                        data_logger_utama data_logger_utama = data_logger_utama.this;
                        data_logger_utama.this.posisi_kedua = 0;
                        data_logger_utama.posisi_pertama = 0;
                        data_logger_utama.this.startActivityForResult(new Intent(data_logger_utama.this.getApplicationContext(), virtual_dyno.class), 0);
                    }
                }
                return true;
            }
        });
        this.afrPlot = (XYPlot) findViewById(R.id.afrPlot);
        this.afrPlot.setRangeLabel("");
        this.afrPlot.setDomainLabel("");
        this.afrPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0d);
        this.afrPlot.setRangeBoundaries(Integer.valueOf(this.min_afr), Integer.valueOf(this.max_afr), BoundaryMode.FIXED);
        this.afrPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.afrPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.afrPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.afrPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.afrPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.afrPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.afrPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.mapPlot = (XYPlot) findViewById(R.id.mapPlot);
        this.mapPlot.setRangeLabel("");
        this.mapPlot.setDomainLabel("");
        this.mapPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.mapPlot.setRangeBoundaries(Integer.valueOf(this.min_map), Integer.valueOf(this.max_map), BoundaryMode.FIXED);
        this.mapPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.mapPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, (double) ((this.max_map - this.min_map) / 9));
        this.mapPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.mapPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.mapPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mapPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.mapPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.mapPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.fuelPlot = (XYPlot) findViewById(R.id.fuelPlot);
        this.fuelPlot.setRangeLabel("");
        this.fuelPlot.setDomainLabel("");
        this.fuelPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.fuelPlot.setRangeBoundaries(Integer.valueOf(this.min_fuel), Integer.valueOf(this.max_fuel), BoundaryMode.FIXED);
        this.fuelPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.fuelPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.fuelPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, (double) ((this.max_fuel - this.min_fuel) / 9));
        this.fuelPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.fuelPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.fuelPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.fuelPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.fuelPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.bmPlot = (XYPlot) findViewById(R.id.bmPlot);
        this.bmPlot.setRangeLabel("");
        this.bmPlot.setDomainLabel("");
        this.bmPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.bmPlot.setRangeBoundaries(Integer.valueOf(this.min_bm), Integer.valueOf(this.max_bm), BoundaryMode.FIXED);
        this.bmPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.bmPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.bmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.bmPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.bmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(".##"));
        this.bmPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.bmPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.igPlot = (XYPlot) findViewById(R.id.igPlot);
        this.igPlot.setRangeLabel("");
        this.igPlot.setDomainLabel("");
        this.igPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.igPlot.setRangeBoundaries(Integer.valueOf(this.min_ig), Integer.valueOf(this.max_ig), BoundaryMode.FIXED);
        this.igPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.igPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.igPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.igPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.igPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.igPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.itPlot = (XYPlot) findViewById(R.id.itPlot);
        this.itPlot.setRangeLabel("");
        this.itPlot.setDomainLabel("");
        this.itPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.itPlot.setRangeBoundaries(Integer.valueOf(this.min_it), Integer.valueOf(this.max_it), BoundaryMode.FIXED);
        this.itPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.itPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, (double) ((this.max_it - this.min_it) / 9));
        this.itPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.itPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.itPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.itPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.itPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.itPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.eotPlot = (XYPlot) findViewById(R.id.eotPlot);
        this.eotPlot.setRangeLabel("");
        this.eotPlot.setDomainLabel("");
        this.eotPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.eotPlot.setRangeBoundaries(Integer.valueOf(this.min_eot), Integer.valueOf(this.max_eot), BoundaryMode.FIXED);
        this.eotPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.eotPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.eotPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.eotPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.eotPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.eotPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.batPlot = (XYPlot) findViewById(R.id.batPlot);
        this.batPlot.setRangeLabel("");
        this.batPlot.setDomainLabel("");
        this.batPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.batPlot.setRangeBoundaries(Integer.valueOf(this.min_bat), Integer.valueOf(this.max_bat), BoundaryMode.FIXED);
        this.batPlot.getGraph().getDomainGridLinePaint().setColor(0);
        this.batPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.batPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(0);
        this.batPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.batPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.batPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (!Bevent.getText().equals("Click")) {
                    data_logger_utama.this.TouchEvent(arg0, event);
                    return true;
                } else if (event.getAction() != 0) {
                    return true;
                } else {
                    data_logger_utama.this.onPlotClicked2(new PointF(event.getX(), event.getY()));
                    return true;
                }
            }
        });
        this.series1 = new SimpleXYSeries("TPS");
        this.series1.useImplicitXVals();
        LineAndPointFormatter line1 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 139, 7, 196)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series2 = new SimpleXYSeries("RPM");
        this.series2.useImplicitXVals();
        LineAndPointFormatter line2 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 255, 0, 0)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series3 = new SimpleXYSeries("AFR");
        this.series3.useImplicitXVals();
        LineAndPointFormatter line3 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 242, 163, 4)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series4 = new SimpleXYSeries("MAP");
        this.series4.useImplicitXVals();
        LineAndPointFormatter line4 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 11, 237, 204)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series5 = new SimpleXYSeries("F");
        this.series5.useImplicitXVals();
        LineAndPointFormatter line5 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 129, 21, 153)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series6 = new SimpleXYSeries("BM");
        this.series6.useImplicitXVals();
        LineAndPointFormatter line6 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 0, 255, 0)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series7 = new SimpleXYSeries("IG");
        this.series7.useImplicitXVals();
        LineAndPointFormatter line7 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 247, 153, 2)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series8 = new SimpleXYSeries("IT");
        this.series8.useImplicitXVals();
        LineAndPointFormatter line8 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 129, 21, 153)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series9 = new SimpleXYSeries("EOT");
        this.series9.useImplicitXVals();
        LineAndPointFormatter line9 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 5, 17, 241)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.series10 = new SimpleXYSeries("BAT");
        this.series10.useImplicitXVals();
        LineAndPointFormatter line10 = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 0, 255, 0)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.tpsPlot.addSeries(this.series1, line1);
        this.rpmPlot.addSeries(this.series2, line2);
        this.afrPlot.addSeries(this.series3, line3);
        this.mapPlot.addSeries(this.series4, line4);
        this.fuelPlot.addSeries(this.series5, line5);
        this.bmPlot.addSeries(this.series6, line6);
        this.igPlot.addSeries(this.series7, line7);
        this.itPlot.addSeries(this.series8, line8);
        this.eotPlot.addSeries(this.series9, line9);
        this.batPlot.addSeries(this.series10, line10);
        this.hitung = 0;
        this.handler.post(this.sendData);
        this.tpsPlot.calculateMinMaxVals();
        this.minXY = new PointF(0.0f, 0.0f);
        this.maxXY = new PointF((float) (this.jumlah_data_logger / 10), 100.0f);
        this.leftBoundary = 0.0f;
        this.rightBoundary = ((float) this.jumlah_data_logger) / 10.0f;
    }

    private void onPlotClicked(PointF point) {
        XYGraphWidget widget = this.rpmPlot.getGraph();
        if (this.rpmPlot.getGraph().getGridRect().contains(point.x, point.y)) {
            Number x = this.rpmPlot.getXVal(point);
            Number y = this.rpmPlot.getYVal(point);
            this.selection = null;
            double xDistance = 0.0d;
            double yDistance = 0.0d;
            for (int i = 0; i < this.series2.size(); i++) {
                Number thisX = this.series2.getX(i);
                Number thisY = this.series2.getY(i);
                if (!(thisX == null || thisY == null)) {
                    double thisXDistance = LineRegion.measure(x, thisX).doubleValue();
                    double thisYDistance = LineRegion.measure(y, thisY).doubleValue();
                    if (this.selection == null) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance < xDistance) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance == xDistance && thisYDistance < yDistance && thisY.doubleValue() >= y.doubleValue()) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    }
                }
            }
        } else {
            this.selection = null;
        }
        if (this.selection == null) {
            Toast.makeText(getApplicationContext(), "No Text", 0).show();
        } else {
            Toast.makeText(getApplicationContext(), "Selected: " + ((XYSeries) this.selection.second).getTitle() + " Value: " + ((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()), 0).show();
            this.textRPM.setText("RPM : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
            widget.setRangeCursorPosition(Float.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).floatValue()));
            widget.setDomainCursorPosition(Float.valueOf(point.x));
        }
        this.rpmPlot.redraw();
    }

    /* access modifiers changed from: private */
    public void onPlotClicked2(PointF point) {
        processClick(point, this.tpsPlot, this.series1, 1);
        processClick(point, this.rpmPlot, this.series2, 2);
        processClick(point, this.afrPlot, this.series3, 3);
        processClick(point, this.mapPlot, this.series4, 4);
        processClick(point, this.fuelPlot, this.series5, 5);
        processClick(point, this.bmPlot, this.series6, 6);
        processClick(point, this.igPlot, this.series7, 7);
        processClick(point, this.itPlot, this.series8, 8);
        processClick(point, this.eotPlot, this.series9, 9);
        processClick(point, this.batPlot, this.series10, 10);
    }

    /* access modifiers changed from: private */
    public void onPlotClicked3(PointF point) {
        this.mapPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.mapPlot.redraw();
        this.fuelPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.fuelPlot.redraw();
        this.bmPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.bmPlot.redraw();
        this.igPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.igPlot.redraw();
        this.itPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.itPlot.redraw();
        this.eotPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.eotPlot.redraw();
        this.batPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.batPlot.redraw();
        processClick(point, this.tpsPlot, this.series1, 1);
        processClick(point, this.rpmPlot, this.series2, 2);
        processClick(point, this.afrPlot, this.series3, 3);
    }

    private void processClick(PointF point, XYPlot plot, SimpleXYSeries series, int pos) {
        XYGraphWidget widget = plot.getGraph();
        if (plot.getGraph().getGridRect().contains(point.x, point.y)) {
            Number x = plot.getXVal(point);
            Number y = plot.getYVal(point);
            this.selection = null;
            double xDistance = 0.0d;
            double yDistance = 0.0d;
            for (int i = 0; i < series.size(); i++) {
                Number thisX = series.getX(i);
                Number thisY = series.getY(i);
                if (!(thisX == null || thisY == null)) {
                    double thisXDistance = LineRegion.measure(x, thisX).doubleValue();
                    double thisYDistance = LineRegion.measure(y, thisY).doubleValue();
                    if (this.selection == null) {
                        this.selection = new Pair<>(Integer.valueOf(i), series);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance < xDistance) {
                        this.selection = new Pair<>(Integer.valueOf(i), series);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance == xDistance && thisYDistance < yDistance && thisY.doubleValue() >= y.doubleValue()) {
                        this.selection = new Pair<>(Integer.valueOf(i), series);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    }
                }
            }
        } else {
            this.selection = null;
        }
        if (this.selection != null) {
            switch (pos) {
                case 1:
                    this.textTPS.setText("RPM : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                    if (this.BVDyno.getTextColors().getDefaultColor() == -65536) {
                        this.touch = 1;
                        this.posisi = ((Integer) ((XYSeries) this.selection.second).getX(((Integer) this.selection.first).intValue())).intValue();
                        break;
                    }
                    break;
                case 2:
                    this.textRPM.setText("TPS : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                    break;
                case 3:
                    this.textAFR.setText("AFR : " + String.format("%.1f", new Object[]{Double.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue())}));
                    break;
                case 4:
                    this.textMAP.setText("MAP : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                    break;
                case 5:
                    this.textF.setText("F : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                    break;
                case 6:
                    this.textBM.setText("BM : " + String.format("%.2f", new Object[]{Double.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue())}));
                    break;
                case 7:
                    this.textIG.setText("IG : " + String.format("%.1f", new Object[]{Double.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue())}));
                    break;
                case 8:
                    this.textIT.setText("IT : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                    break;
                case 9:
                    this.textEOT.setText("EOT : " + String.format("%.1f", new Object[]{Double.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue())}));
                    break;
                case 10:
                    this.textBAT.setText("BAT : " + String.format("%.2f", new Object[]{Double.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue())}));
                    break;
            }
            widget.setDomainCursorPosition(Float.valueOf(point.x));
        }
        plot.redraw();
    }

    private void onPlotClicked1(PointF point) {
        if (this.rpmPlot.getGraph().getGridRect().contains(point.x, point.y)) {
            Number x = this.rpmPlot.getXVal(point);
            Number y = this.rpmPlot.getYVal(point);
            this.selection = null;
            double xDistance = 0.0d;
            double yDistance = 0.0d;
            for (int i = 0; i < this.series2.size(); i++) {
                Number thisX = this.series2.getX(i);
                Number thisY = this.series2.getY(i);
                if (!(thisX == null || thisY == null)) {
                    double thisXDistance = LineRegion.measure(x, thisX).doubleValue();
                    double thisYDistance = LineRegion.measure(y, thisY).doubleValue();
                    if (this.selection == null) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance < xDistance) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    } else if (thisXDistance == xDistance && thisYDistance < yDistance && thisY.doubleValue() >= y.doubleValue()) {
                        this.selection = new Pair<>(Integer.valueOf(i), this.series2);
                        xDistance = thisXDistance;
                        yDistance = thisYDistance;
                    }
                }
            }
        } else {
            this.selection = null;
        }
        if (this.selection == null) {
            Toast.makeText(getApplicationContext(), "No Text", 0).show();
        } else {
            Context applicationContext = getApplicationContext();
            Context context = applicationContext;
            Toast.makeText(context, "Selected: " + ((XYSeries) this.selection.second).getTitle() + " Value: " + ((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()), 0).show();
        }
        this.rpmPlot.redraw();
    }

    /* access modifiers changed from: private */
    public void widgetClear() {
        this.tpsPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.rpmPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.afrPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.mapPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.fuelPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.bmPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.igPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.itPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.eotPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
        this.batPlot.getGraph().setDomainCursorPosition(Float.valueOf(-1.0f));
    }

    /* access modifiers changed from: private */
    public void plotRefresh() {
        this.tpsPlot.redraw();
        this.rpmPlot.redraw();
        this.afrPlot.redraw();
        this.mapPlot.redraw();
        this.fuelPlot.redraw();
        this.bmPlot.redraw();
        this.igPlot.redraw();
        this.itPlot.redraw();
        this.eotPlot.redraw();
        this.batPlot.redraw();
    }

    /* access modifiers changed from: private */
    public void TouchEvent(View arg0, MotionEvent event) {
        widgetClear();
        switch (event.getAction() & 255) {
            case 0:
                this.firstFinger = new PointF(event.getX(), event.getY());
                this.mode = 1;
                this.stopThread = true;
                return;
            case 1:
            case 6:
                this.mode = 0;
                return;
            case 2:
                if (this.mode == 1) {
                    PointF oldFirstFinger = this.firstFinger;
                    this.firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - this.firstFinger.x);
                    ReDraw();
                    return;
                } else if (this.mode == 2) {
                    float oldDist = this.distBetweenFingers;
                    this.distBetweenFingers = spacing(event);
                    zoom(oldDist / this.distBetweenFingers);
                    ReDraw();
                    return;
                } else {
                    return;
                }
            case 5:
                this.distBetweenFingers = spacing(event);
                if (this.distBetweenFingers > 5.0f) {
                    this.mode = 2;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void ReDraw() {
        this.tpsPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.rpmPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.afrPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.mapPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.fuelPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.bmPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.igPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.itPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.eotPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.batPlot.setDomainBoundaries(Float.valueOf(this.minXY.x), Float.valueOf(this.maxXY.x), BoundaryMode.FIXED);
        this.tpsPlot.redraw();
        this.rpmPlot.redraw();
        this.afrPlot.redraw();
        this.mapPlot.redraw();
        this.fuelPlot.redraw();
        this.bmPlot.redraw();
        this.igPlot.redraw();
        this.itPlot.redraw();
        this.eotPlot.redraw();
        this.batPlot.redraw();
    }

    /* access modifiers changed from: private */
    public void populateSeries(int jumlahLogger) {
        for (int i = 0; i < jumlahLogger; i++) {
            int nilai_tps = Integer.valueOf(this.arr_data[i * 10]).intValue();
            double angka_tps = 0.0d;
            if (nilai_tps == 0) {
                angka_tps = 0.0d;
            } else if (nilai_tps == 1) {
                angka_tps = 2.0d;
            } else if (nilai_tps == 20) {
                angka_tps = 100.0d;
            } else if (nilai_tps > 1 && nilai_tps < 20) {
                angka_tps = (double) ((nilai_tps * 5) - 5);
            }
            double adc_map = Double.valueOf(this.arr_data[(i * 10) + 5]).doubleValue();
            double angka_rpm = (Double.valueOf(this.arr_data[(i * 10) + 1]).doubleValue() * 250.0d) + 1000.0d;
            double doubleValue = Double.valueOf(this.arr_data[(i * 10) + 6]).doubleValue();
            double doubleValue2 = Double.valueOf(this.arr_data[(i * 10) + 7]).doubleValue();
            double doubleValue3 = Double.valueOf(this.arr_data[(i * 10) + 9]).doubleValue();
            double doubleValue4 = Double.valueOf(this.arr_data[(i * 10) + 8]).doubleValue();
            double doubleValue5 = Double.valueOf(this.arr_data[(i * 10) + 4]).doubleValue();
            double angka_afr = Double.valueOf(this.arr_data[(i * 10) + 2]).doubleValue();
            double doubleValue6 = Double.valueOf(this.arr_data[(i * 10) + 3]).doubleValue();
            double round = (double) ((int) Math.round(mapping_map((5.0d * adc_map) / 1023.0d)));
            this.series1.addLast(Integer.valueOf(i), Double.valueOf(angka_tps));
            this.series2.addLast(Integer.valueOf(i), Double.valueOf(angka_rpm));
            this.series3.addLast(Integer.valueOf(i), Double.valueOf(angka_afr));
        }
    }

    private void populateSeriesTest(int i) {
        this.series1.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(100)));
        this.series2.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(16000)));
        this.series3.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(18)));
        this.series4.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(100)));
        this.series5.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(100)));
        this.series6.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(20)));
        this.series7.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(70)));
        this.series8.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(700)));
        this.series9.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(150)));
        this.series10.addLast(Integer.valueOf(i), Integer.valueOf(this.r.nextInt(15)));
    }

    /* access modifiers changed from: private */
    public void populateSeries1(int i) {
        int nilai_tps = Integer.valueOf(this.arr_data[i * 10]).intValue();
        double angka_tps = 0.0d;
        if (nilai_tps == 0) {
            angka_tps = 0.0d;
        } else if (nilai_tps == 1) {
            angka_tps = 2.0d;
        } else if (nilai_tps == 20) {
            angka_tps = 100.0d;
        } else if (nilai_tps > 1 && nilai_tps < 20) {
            angka_tps = (double) ((nilai_tps * 5) - 5);
        }
        double adc_map = Double.valueOf(this.arr_data[(i * 10) + 5]).doubleValue();
        double angka_rpm = (Double.valueOf(this.arr_data[(i * 10) + 1]).doubleValue() * 250.0d) + 1000.0d;
        double angka_f = Double.valueOf(this.arr_data[(i * 10) + 6]).doubleValue();
        double angka_bm = Double.valueOf(this.arr_data[(i * 10) + 7]).doubleValue();
        double angka_ig = Double.valueOf(this.arr_data[(i * 10) + 9]).doubleValue();
        double angka_it = Double.valueOf(this.arr_data[(i * 10) + 8]).doubleValue();
        double angka_bat = Double.valueOf(this.arr_data[(i * 10) + 4]).doubleValue();
        double angka_afr = Double.valueOf(this.arr_data[(i * 10) + 2]).doubleValue();
        double angka_eot = Double.valueOf(this.arr_data[(i * 10) + 3]).doubleValue();
        double angka_map = (double) ((int) Math.round(mapping_map((5.0d * adc_map) / 1023.0d)));
        this.series1.addLast(Integer.valueOf(i), Double.valueOf(angka_tps));
        this.series2.addLast(Integer.valueOf(i), Double.valueOf(angka_rpm));
        this.series3.addLast(Integer.valueOf(i), Double.valueOf(angka_afr));
        this.series4.addLast(Integer.valueOf(i), Double.valueOf(angka_map));
        this.series5.addLast(Integer.valueOf(i), Double.valueOf(angka_f));
        this.series6.addLast(Integer.valueOf(i), Double.valueOf(angka_bm));
        this.series7.addLast(Integer.valueOf(i), Double.valueOf(angka_ig));
        this.series8.addLast(Integer.valueOf(i), Double.valueOf(angka_it));
        this.series9.addLast(Integer.valueOf(i), Double.valueOf(angka_eot));
        this.series10.addLast(Integer.valueOf(i), Double.valueOf(angka_bat));
    }

    /* access modifiers changed from: private */
    public void ReDrawAllPlot() {
        this.tpsPlot.redraw();
        this.rpmPlot.redraw();
        this.afrPlot.redraw();
        this.mapPlot.redraw();
        this.fuelPlot.redraw();
        this.bmPlot.redraw();
        this.igPlot.redraw();
        this.itPlot.redraw();
        this.eotPlot.redraw();
        this.batPlot.redraw();
    }

    private double mapping_map(double volt) {
        if (volt < 0.3d) {
            return (100.0d * volt) / 3.0d;
        }
        if (volt >= 0.3d && volt < 0.6d) {
            return ((100.0d * (volt - 0.3d)) / 3.0d) + 10.0d;
        }
        if (volt >= 0.6d && volt < 1.1d) {
            return ((100.0d * (volt - 0.6d)) / 5.0d) + 20.0d;
        }
        if (volt >= 1.1d && volt < 1.7d) {
            return ((100.0d * (volt - 1.1d)) / 6.0d) + 30.0d;
        }
        if (volt >= 1.7d && volt < 2.2d) {
            return ((100.0d * (volt - 1.7d)) / 5.0d) + 40.0d;
        }
        if (volt >= 2.2d && volt < 2.7d) {
            return ((100.0d * (volt - 2.2d)) / 5.0d) + 50.0d;
        }
        if (volt >= 2.7d && volt < 3.3d) {
            return ((100.0d * (volt - 2.7d)) / 6.0d) + 60.0d;
        }
        if (volt >= 3.3d && volt < 3.8d) {
            return ((100.0d * (volt - 3.3d)) / 5.0d) + 70.0d;
        }
        if (volt >= 3.8d && volt < 4.4d) {
            return ((100.0d * (volt - 3.8d)) / 6.0d) + 80.0d;
        }
        if (volt >= 4.4d && volt < 4.9d) {
            return ((100.0d * (volt - 4.4d)) / 5.0d) + 90.0d;
        }
        if (volt >= 4.9d) {
            return 100.0d;
        }
        return 0.0d;
    }

    private void zoom(float scale) {
        float domainSpan = this.maxXY.x - this.minXY.x;
        float oldMax = this.maxXY.x;
        float oldMin = this.minXY.x;
        float domainMidPoint = this.maxXY.x - (domainSpan / 2.0f);
        float offset = (domainSpan * scale) / 2.0f;
        this.minXY.x = domainMidPoint - offset;
        this.maxXY.x = domainMidPoint + offset;
        if (this.maxXY.x - this.minXY.x < 5.0f) {
            this.minXY.x = oldMin;
            this.maxXY.x = oldMax;
        }
        if (this.minXY.x < this.leftBoundary) {
            this.minXY.x = this.leftBoundary;
            this.maxXY.x = this.leftBoundary + (this.zoomRatio * domainSpan);
            if (this.maxXY.x > this.series1.getX(this.series1.size() - 1).floatValue()) {
                this.maxXY.x = this.rightBoundary;
            }
        }
        if (this.maxXY.x > this.series1.getX(this.series1.size() - 1).floatValue()) {
            this.maxXY.x = this.rightBoundary;
            this.minXY.x = this.rightBoundary - (this.zoomRatio * domainSpan);
            if (this.minXY.x < this.leftBoundary) {
                this.minXY.x = this.leftBoundary;
            }
        }
    }

    private void scroll(float pan) {
        float domainSpan = this.maxXY.x - this.minXY.x;
        float offset = pan * (domainSpan / ((float) this.tpsPlot.getWidth()));
        this.minXY.x += offset;
        this.maxXY.x += offset;
        if (this.minXY.x < this.leftBoundary) {
            this.minXY.x = this.leftBoundary;
            this.maxXY.x = this.leftBoundary + domainSpan;
        }
        if (this.maxXY.x > this.series1.getX(this.series1.size() - 1).floatValue()) {
            this.maxXY.x = this.rightBoundary;
            this.minXY.x = this.rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public void GetList() {
        int posisi_awal = 0;
        int posisi_akhir = 0;
        if (!this.newString.equals("")) {
            String[] mentah = MappingHandle.data_logger_posisi100.get(Integer.valueOf(this.newString).intValue()).split(";");
            posisi_awal = Integer.valueOf(mentah[0]).intValue();
            posisi_akhir = Integer.valueOf(mentah[1]).intValue();
        }
        ArrayList<String> calledList = new ArrayList<>();
        for (int i = posisi_awal; i <= posisi_akhir; i += 10) {
            calledList.add(MappingHandle.data_data_logger.get(i).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 1).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 2).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 3).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 4).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 5).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 6).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 7).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 8).toString().replace(",", "."));
            calledList.add(MappingHandle.data_data_logger.get(i + 9).toString().replace(",", "."));
            double adc_map = Double.valueOf(MappingHandle.data_data_logger.get(i + 5)).doubleValue();
            double angka_rpm = (Double.valueOf(MappingHandle.data_data_logger.get(i + 1)).doubleValue() * 250.0d) + 1000.0d;
            double angka_f = Double.valueOf(MappingHandle.data_data_logger.get(i + 6)).doubleValue();
            double angka_bm = Double.valueOf(MappingHandle.data_data_logger.get(i + 7)).doubleValue();
            double angka_ig = Double.valueOf(MappingHandle.data_data_logger.get(i + 9)).doubleValue();
            double angka_it = Double.valueOf(MappingHandle.data_data_logger.get(i + 8)).doubleValue();
            double angka_bat = Double.valueOf(MappingHandle.data_data_logger.get(i + 4)).doubleValue();
            double angka_afr = Double.valueOf(MappingHandle.data_data_logger.get(i + 2)).doubleValue();
            double angka_eot = Double.valueOf(MappingHandle.data_data_logger.get(i + 3)).doubleValue();
            double angka_map = (double) ((int) Math.round(mapping_map((5.0d * adc_map) / 1023.0d)));
            if (angka_rpm > ((double) this.max_rpm)) {
                this.max_rpm = ((int) angka_rpm) + 500;
                if (this.max_rpm > 16500) {
                    this.max_rpm = 16500;
                }
            }
            if (angka_rpm < ((double) this.min_rpm)) {
                this.min_rpm = ((int) angka_rpm) - 500;
                if (this.min_rpm < 500) {
                    this.min_rpm = 500;
                }
            }
            if (angka_f > ((double) this.max_fuel)) {
                this.max_fuel = ((int) angka_f) + 10;
                if (this.max_fuel > 110) {
                    this.max_fuel = 110;
                }
            }
            if (angka_f < ((double) this.min_fuel)) {
                this.min_fuel = ((int) angka_f) - 10;
                if (this.min_fuel < -110) {
                    this.min_fuel = -110;
                }
            }
            if (angka_bm > ((double) this.max_bm)) {
                this.max_bm = ((int) angka_bm) + 1;
                if (this.max_bm > 21) {
                    this.max_bm = 21;
                }
            }
            if (angka_bm < ((double) this.min_bm)) {
                this.min_bm = ((int) angka_bm) - 1;
                if (this.min_bm < -1) {
                    this.min_bm = -1;
                }
            }
            if (angka_it > ((double) this.max_it)) {
                this.max_it = ((int) angka_it) + 20;
                if (this.max_it > 750) {
                    this.max_it = 750;
                }
            }
            if (angka_it < ((double) this.min_it)) {
                this.min_it = ((int) angka_it) - 20;
                if (this.min_it < -20) {
                    this.min_it = -20;
                }
            }
            if (angka_ig > ((double) this.max_ig)) {
                this.max_ig = ((int) angka_ig) + 5;
                if (this.max_ig > 95) {
                    this.max_ig = 95;
                }
            }
            if (angka_ig < ((double) this.min_ig)) {
                this.min_ig = ((int) angka_ig) - 5;
                if (this.min_ig < -5) {
                    this.min_ig = -5;
                }
            }
            if (angka_afr > ((double) this.max_afr)) {
                this.max_afr = ((int) angka_afr) + 1;
                if (this.max_afr > 20) {
                    this.max_afr = 20;
                }
            }
            if (angka_afr < ((double) this.min_afr)) {
                this.min_afr = ((int) angka_afr) - 1;
                if (this.min_afr < 10) {
                    this.min_afr = 10;
                }
            }
            if (angka_map > ((double) this.max_map)) {
                this.max_map = ((int) angka_map) + 5;
                if (this.max_map > 105) {
                    this.max_map = 105;
                }
            }
            if (angka_map < ((double) this.min_map)) {
                this.min_map = ((int) angka_map) - 5;
                if (this.min_map < -5) {
                    this.min_map = -5;
                }
            }
            if (angka_eot > ((double) this.max_eot)) {
                this.max_eot = ((int) angka_eot) + 10;
                if (this.max_eot > 160) {
                    this.max_eot = 160;
                }
            }
            if (angka_eot < ((double) this.min_eot)) {
                this.min_eot = ((int) angka_eot) - 10;
                if (this.min_eot < -10) {
                    this.min_eot = -10;
                }
            }
            if (angka_bat > ((double) this.max_bat)) {
                this.max_bat = ((int) angka_bat) + 1;
                if (this.max_bat > 20) {
                    this.max_bat = 20;
                }
            }
            if (angka_bat < ((double) this.min_bat)) {
                this.min_bat = ((int) angka_bat) - 1;
                if (this.min_bat < 8) {
                    this.min_bat = 8;
                }
            }
        }
        this.arr_data = new String[calledList.size()];
        for (int k = 0; k < calledList.size(); k++) {
            this.arr_data[k] = calledList.get(k).toString();
        }
        this.jumlah_data_logger = calledList.size();
    }

    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }

    private class SeriesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        int jumlahLogger;
        private SimpleXYSeries series1;
        private SimpleXYSeries series10;
        private SimpleXYSeries series2;
        private SimpleXYSeries series3;
        private SimpleXYSeries series4;
        private SimpleXYSeries series5;
        private SimpleXYSeries series6;
        private SimpleXYSeries series7;
        private SimpleXYSeries series8;
        private SimpleXYSeries series9;

        public SeriesAsyncTask(Context context2, SimpleXYSeries series12, SimpleXYSeries series22, SimpleXYSeries series32, SimpleXYSeries series42, SimpleXYSeries series52, SimpleXYSeries series62, SimpleXYSeries series72, SimpleXYSeries series82, SimpleXYSeries series92, SimpleXYSeries series102, int jumlahLogger2) {
            this.context = context2;
            this.series1 = series12;
            this.series2 = series22;
            this.series3 = series32;
            this.series4 = series42;
            this.series5 = series52;
            this.series6 = series62;
            this.series7 = series72;
            this.series8 = series82;
            this.series9 = series92;
            this.series10 = series102;
            this.jumlahLogger = jumlahLogger2;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            data_logger_utama.this.populateSeries(this.jumlahLogger);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            data_logger_utama.this.tpsPlot.redraw();
            data_logger_utama.this.rpmPlot.redraw();
            data_logger_utama.this.afrPlot.redraw();
            data_logger_utama.this.mapPlot.redraw();
            data_logger_utama.this.fuelPlot.redraw();
            data_logger_utama.this.bmPlot.redraw();
            data_logger_utama.this.igPlot.redraw();
            data_logger_utama.this.itPlot.redraw();
            data_logger_utama.this.eotPlot.redraw();
            data_logger_utama.this.batPlot.redraw();
        }
    }
}

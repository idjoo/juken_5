package juken.android.com.juken_5.VirtualDyno;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Iterator;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.VirtualDyno.Fuel.StaticClass;
import juken.android.com.juken_5.data_logger.LineRegion;
import juken.android.com.juken_5.range_int_min;

public class virtual_dyno extends Activity {
    TextView XAfrRpm;
    TextView XRpmTime;
    TextView YAfrRpm;
    TextView YRpmTime;
    /* access modifiers changed from: private */
    public XYPlot afrRpmPlot;
    /* access modifiers changed from: private */
    public SimpleXYSeries afrRpmPlotSeries = null;
    int jumlah = 0;
    int maksimum = 0;
    int max_rpm = 0;
    int min_rpm = 16000;
    int pengingat = 1;
    /* access modifiers changed from: private */
    public XYPlot rpmTimePlot;
    /* access modifiers changed from: private */
    public SimpleXYSeries rpmTimePlotSeries = null;
    private Pair<Integer, XYSeries> selection;
    int status_flag = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_dyno);
        for (int i = 1; i <= 61; i++) {
            ((TextView) findViewById(i)).setText(MappingHandle.list_base_map.get(i + 1120));
        }
        for (int i2 = 1; i2 <= 61; i2++) {
            ((EditText) findViewById(i2 + 61)).setText(MappingHandle.list_fuel.get(i2 + 1120));
        }
        MappingHandle.list_VDyno_rpmAvg.clear();
        MappingHandle.list_VDyno_time.clear();
        for (int i3 = 0; i3 < MappingHandle.list_VDyno_rpm.size(); i3++) {
            MappingHandle.list_VDyno_time.add(String.valueOf(((double) i3) * 0.02d));
        }
        this.XAfrRpm = (TextView) findViewById(R.id.XAfrRpm);
        this.YAfrRpm = (TextView) findViewById(R.id.YAfrRpm);
        this.XRpmTime = (TextView) findViewById(R.id.XRpmTime);
        this.YRpmTime = (TextView) findViewById(R.id.YRpmTime);
        ((ImageButton) findViewById(R.id.save_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < 61; i++) {
                    MappingHandle.list_fuel.remove(i + 1220);
                    MappingHandle.list_fuel.add(i + 1220, ((EditText) virtual_dyno.this.findViewById(i + 62)).getText().toString());
                }
                Toast.makeText(virtual_dyno.this, "data saved", 0).show();
            }
        });
        ((ImageButton) findViewById(R.id.set_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.set_value();
            }
        });
        ((ImageButton) findViewById(R.id.add_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.add_value();
            }
        });
        ((ImageButton) findViewById(R.id.copy_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.Copy();
            }
        });
        ((ImageButton) findViewById(R.id.paste_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.Paste();
            }
        });
        ((ImageButton) findViewById(R.id.select_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.select();
            }
        });
        ((ImageButton) findViewById(R.id.plus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.plus_minus("plus");
            }
        });
        ((ImageButton) findViewById(R.id.minus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                virtual_dyno.this.plus_minus("minus");
            }
        });
        FilterRpmAvg1(6);
        Acceleration();
        this.rpmTimePlot = (XYPlot) findViewById(R.id.rpmTimePlot);
        this.rpmTimePlot.setRangeLabel("RPM");
        this.rpmTimePlot.setDomainLabel("Time (s)");
        this.rpmTimePlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.rpmTimePlot.getGraph().getDomainGridLinePaint().setColor(-1);
        this.rpmTimePlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.rpmTimePlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(-1);
        this.rpmTimePlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.rpmTimePlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.rpmTimePlot.getLayoutManager().remove(this.rpmTimePlot.getLegend());
        this.rpmTimePlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.rpmTimePlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() != 0) {
                    return true;
                }
                virtual_dyno.this.processClick(new PointF(event.getX(), event.getY()), virtual_dyno.this.rpmTimePlot, virtual_dyno.this.rpmTimePlotSeries, 2);
                return true;
            }
        });
        this.afrRpmPlot = (XYPlot) findViewById(R.id.AfrRpmPlot);
        this.afrRpmPlot.setRangeLabel("AFR");
        this.afrRpmPlot.setDomainLabel("RPM");
        this.afrRpmPlot.setBorderStyle(Plot.BorderStyle.NONE, (Float) null, (Float) null);
        this.afrRpmPlot.setRangeBoundaries(8, 18, BoundaryMode.FIXED);
        this.afrRpmPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1.0d);
        this.afrRpmPlot.getGraph().getDomainGridLinePaint().setColor(-1);
        this.afrRpmPlot.getGraph().getRangeGridLinePaint().setColor(Color.argb(100, 255, 255, 255));
        this.afrRpmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(-1);
        this.afrRpmPlot.getGraph().getGridBackgroundPaint().setColor(ViewCompat.MEASURED_STATE_MASK);
        this.afrRpmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat(""));
        this.afrRpmPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat(""));
        this.afrRpmPlot.getLayoutManager().remove(this.afrRpmPlot.getLegend());
        this.afrRpmPlot.setPlotMargins(5.0f, 0.0f, 0.0f, 0.0f);
        this.afrRpmPlot.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() == 0) {
                    virtual_dyno.this.processClick(new PointF(event.getX(), event.getY()), virtual_dyno.this.afrRpmPlot, virtual_dyno.this.afrRpmPlotSeries, 1);
                }
                return true;
            }
        });
        this.rpmTimePlotSeries = new SimpleXYSeries("");
        LineAndPointFormatter rpmTimePlotLine = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 11, 237, 204)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.afrRpmPlotSeries = new SimpleXYSeries("");
        LineAndPointFormatter afrRpmPlotLine = new LineAndPointFormatter(Integer.valueOf(Color.argb(255, 11, 237, 204)), (Integer) null, (Integer) null, (PointLabelFormatter) null);
        this.rpmTimePlot.addSeries(this.rpmTimePlotSeries, rpmTimePlotLine);
        this.afrRpmPlot.addSeries(this.afrRpmPlotSeries, afrRpmPlotLine);
        for (int i4 = 0; i4 < MappingHandle.list_VDyno_rpm.size(); i4++) {
            this.rpmTimePlotSeries.addLast(Double.valueOf(MappingHandle.list_VDyno_time.get(i4)), Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(i4)));
            this.afrRpmPlotSeries.addLast(Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(i4)), Double.valueOf(MappingHandle.list_VDyno_afr.get(i4)));
            double angka_rpm = Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(i4)).doubleValue();
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
        }
        this.rpmTimePlot.setRangeBoundaries(Integer.valueOf(this.min_rpm), Integer.valueOf(this.max_rpm), BoundaryMode.FIXED);
        this.rpmTimePlot.setRangeStep(StepMode.INCREMENT_BY_VAL, (double) ((this.max_rpm - this.min_rpm) / 9));
        this.afrRpmPlot.setDomainBoundaries(Integer.valueOf(this.min_rpm), Integer.valueOf(this.max_rpm), BoundaryMode.FIXED);
        this.afrRpmPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, (double) ((this.max_rpm - this.min_rpm) / 9));
        this.rpmTimePlot.redraw();
        this.afrRpmPlot.redraw();
    }

    /* access modifiers changed from: private */
    public void processClick(PointF point, XYPlot plot, SimpleXYSeries series, int pos) {
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
            if (pos == 1) {
                this.XAfrRpm.setText("X : " + String.valueOf(((XYSeries) this.selection.second).getX(((Integer) this.selection.first).intValue()).intValue()));
                this.YAfrRpm.setText("Y : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).doubleValue()));
                int val = ((XYSeries) this.selection.second).getX(((Integer) this.selection.first).intValue()).intValue();
                ((EditText) findViewById((((val - modInt(val, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 3) + 61)).requestFocus();
            } else {
                this.XRpmTime.setText("X : " + String.valueOf(((XYSeries) this.selection.second).getX(((Integer) this.selection.first).intValue()).doubleValue()));
                this.YRpmTime.setText("Y : " + String.valueOf(((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue()));
                int val2 = ((XYSeries) this.selection.second).getY(((Integer) this.selection.first).intValue()).intValue();
                ((EditText) findViewById((((val2 - modInt(val2, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 3) + 61)).requestFocus();
            }
            widget.setDomainCursorPosition(Float.valueOf(point.x));
        }
        plot.redraw();
    }

    private void Acceleration() {
        double awal = Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(0)).doubleValue() - mod(Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(0)).doubleValue(), 250.0d);
        double maxX = Double.valueOf(MappingHandle.list_VDyno_time.get(0)).doubleValue();
        double minX = Double.valueOf(MappingHandle.list_VDyno_time.get(0)).doubleValue();
        double minY = Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(0)).doubleValue();
        double maxY = Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(0)).doubleValue();
        for (int i = 0; i < MappingHandle.list_VDyno_rpmAvg.size(); i++) {
            double dataY = Double.valueOf(MappingHandle.list_VDyno_rpmAvg.get(i)).doubleValue();
            double dataX = Double.valueOf(MappingHandle.list_VDyno_time.get(i)).doubleValue();
            if (dataY >= awal && dataY < 250.0d + awal) {
                if (dataY > maxY) {
                    maxY = dataY;
                } else if (dataY < minY) {
                    minY = dataY;
                }
                if (dataX > maxX) {
                    maxX = dataX;
                } else if (dataX < minX) {
                    minX = dataX;
                }
                if (i == MappingHandle.list_VDyno_rpmAvg.size() - 1) {
                    Double gradient = Double.valueOf((maxY - minY) / (1000.0d * (maxX - minX)));
                    int posisiData = ((int) ((awal / 250.0d) - 4.0d)) + 122;
                    if (posisiData > 182) {
                        posisiData = 182;
                    } else if (posisiData < 123) {
                        posisiData = 123;
                    }
                    String data_masuk = String.format("%.5f", new Object[]{gradient});
                    if (gradient.isNaN()) {
                        data_masuk = "-";
                    }
                    ((TextView) findViewById(posisiData)).setText(data_masuk);
                }
            } else if (i != 0) {
                double gradient2 = (maxY - minY) / (1000.0d * (maxX - minX));
                minX = dataX;
                maxX = dataX;
                minY = dataY;
                maxY = dataY;
                int posisiData2 = ((int) ((awal / 250.0d) - 4.0d)) + 123;
                if (posisiData2 > 182) {
                    posisiData2 = 182;
                } else if (posisiData2 < 123) {
                    posisiData2 = 123;
                }
                ((TextView) findViewById(posisiData2)).setText(String.format("%.5f", new Object[]{Double.valueOf(gradient2)}));
                awal += 250.0d;
            }
        }
    }

    private double mod(double x, double y) {
        double result = x % y;
        if (result < 0.0d) {
            return result + y;
        }
        return result;
    }

    private int modInt(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }

    private void FilterRpmAvg1(int value) {
        if (value != 0) {
            double[] arrray = new double[MappingHandle.list_VDyno_rpm.size()];
            for (int i = 0; i < MappingHandle.list_VDyno_rpm.size(); i++) {
                arrray[i] = Double.valueOf(MappingHandle.list_VDyno_rpm.get(i)).doubleValue();
            }
            int indexMax = 0;
            double maxRpmLevel = 0.0d;
            for (int i2 = 0; i2 < MappingHandle.list_VDyno_rpm.size() - 1; i2++) {
                if (Double.valueOf(MappingHandle.list_VDyno_rpm.get(i2)).doubleValue() >= maxRpmLevel) {
                    maxRpmLevel = Double.valueOf(MappingHandle.list_VDyno_rpm.get(i2)).doubleValue();
                    indexMax = i2;
                }
            }
            ArrayList<String> rowsToRemove = new ArrayList<>();
            for (int i3 = indexMax; i3 < MappingHandle.list_VDyno_rpm.size(); i3++) {
                rowsToRemove.add(MappingHandle.list_VDyno_rpm.get(i3));
            }
            ArrayList<Double> s = new ArrayList<>();
            ArrayList<Double> r = new ArrayList<>();
            ArrayList<Double> smoothDataPointsLeft = new ArrayList<>();
            ArrayList<Double> smoothDataPointsRight = new ArrayList<>();
            int length = arrray.length;
            for (int i4 = 0; i4 < length; i4++) {
                Double x = Double.valueOf(arrray[i4]);
                if (s.size() >= value) {
                    s.remove(0);
                }
                s.add(x);
                smoothDataPointsLeft.add(Double.valueOf(calculateAverage(s)));
            }
            for (int i5 = arrray.length - 1; i5 >= 0; i5--) {
                if (r.size() >= value) {
                    r.remove(0);
                }
                r.add(Double.valueOf(arrray[i5]));
                smoothDataPointsRight.add(Double.valueOf(calculateAverage(r)));
            }
            ArrayList<Double> smoothDataPointsRightRev = new ArrayList<>();
            for (int i6 = smoothDataPointsRight.size() - 1; i6 >= 0; i6--) {
                smoothDataPointsRightRev.add(smoothDataPointsRight.get(i6));
            }
            for (int i7 = 0; i7 < MappingHandle.list_VDyno_rpm.size(); i7++) {
                MappingHandle.list_VDyno_rpmAvg.add(String.valueOf((smoothDataPointsRightRev.get(i7).doubleValue() + smoothDataPointsLeft.get(i7).doubleValue()) / 2.0d));
            }
        }
    }

    private double calculateAverage(ArrayList<Double> marks) {
        double sum = 0.0d;
        if (marks.isEmpty()) {
            return 0.0d;
        }
        Iterator<Double> it = marks.iterator();
        while (it.hasNext()) {
            sum += it.next().doubleValue();
        }
        return sum / ((double) marks.size());
    }

    /* access modifiers changed from: private */
    public void add_value() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(this);
        inputNilaiAdd.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilaiAdd.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                virtual_dyno.this.AddValue(inputNilaiAdd.getEditableText().toString());
            }
        });
        alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.create().show();
    }

    /* access modifiers changed from: private */
    public void set_value() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert_set = new AlertDialog.Builder(this);
        alert_set.setTitle((CharSequence) "Set Value");
        alert_set.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilai = new EditText(this);
        inputNilai.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilai.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                virtual_dyno.this.SetValue(inputNilai.getEditableText().toString());
            }
        });
        alert_set.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog create = alert_set.create();
        alert_set.create().show();
    }

    public void AddValue(String Value) {
        double nilai;
        for (int i = 62; i <= 122; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 100.0d) {
                    nilai2 = 100.0d;
                } else if (nilai2 < -100.0d) {
                    nilai2 = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai2)));
            }
        }
    }

    public void SetValue(String Value) {
        for (int i = 62; i <= 122; i++) {
            EditText v = (EditText) findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value);
            }
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan) {
        double nilai;
        for (int i = 62; i <= 122; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 1.0d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 1.0d;
                }
                if (nilai > 100.0d) {
                    nilai = 100.0d;
                } else if (nilai < -100.0d) {
                    nilai = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai)));
            }
        }
    }

    /* access modifiers changed from: private */
    public void select() {
        StaticClass.selectionStatus = false;
        StaticClass.position.clear();
        for (int i = 62; i <= 122; i++) {
            StaticClass.position.add(Integer.valueOf(i));
            EditText x = (EditText) findViewById(i);
            x.setBackgroundColor(-16711681);
            x.setTag("Warna CYAN");
        }
    }

    public void Copy() {
        this.status_flag = 1;
        if (StaticClass.letak.size() != 0) {
            StaticClass.data.clear();
            StaticClass.letak.clear();
        }
        for (int i = 62; i <= 122; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                StaticClass.data.add(str);
                StaticClass.letak.add(Integer.valueOf(i));
                this.jumlah++;
            }
        }
        this.maksimum = this.jumlah;
        this.jumlah = 0;
        Toast.makeText(getApplicationContext(), "copied", 0).show();
    }

    public void Paste() {
        if (this.status_flag == 1) {
            this.status_flag = 1;
            int nilai_acuan = StaticClass.letak.get(0).intValue();
            for (int i = 62; i <= 122; i++) {
                EditText v = (EditText) findViewById(i);
                if (v.getTag() != null && v.getTag().equals("Warna BLUE")) {
                    for (int k = 0; k < this.maksimum; k++) {
                        String data_ditulis = StaticClass.data.get(k);
                        int letakan = i + (StaticClass.letak.get(k).intValue() - nilai_acuan);
                        this.pengingat = letakan;
                        if (letakan <= 122) {
                            ((EditText) findViewById(letakan)).setText(data_ditulis);
                        }
                    }
                    StaticClass.position.add(Integer.valueOf(i));
                }
            }
        }
    }
}

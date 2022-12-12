package juken.android.com.juken_5.emap;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import juken.android.com.juken_5.MappingHandle;

public class emaP extends RelativeLayout {
    EditText EditTextB;
    public final String TAG = "TableMainLayout.java";
    Context context;
    int[] headerCellsWidth = new int[this.headers.length];
    String[] headers = {"                    ", "   1000   ", "   1250   ", "   1500   ", "   1750   ", "   2000   ", "   2250   ", "   2500   ", "   2750   ", "   3000   ", "   3250   ", "   3500   ", "   3750   ", "   4000   ", "   4250   ", "   4500   ", "   4750   ", "   5000   ", "   5250   ", "   5500   ", "   5750   ", "   6000   ", "   6250   ", "   6500   ", "   6750   ", "   7000   ", "   7250   ", "   7500   ", "   7750   ", "   8000   ", "   8250   ", "   8500   ", "   8750   ", "   9000   ", "   9250   ", "   9500   ", "   9750   ", "   10000   ", "   10250   ", "   10500   ", "   10750   ", "   11000   ", "   11250   ", "   11500   ", "   11750   ", "   12000   ", "   12250   ", "   12500   ", "   12750   ", "   13000   ", "   13250   ", "   13500   ", "   13750   ", "   14000   ", "   14250   ", "   14500   ", "   14750   ", "   15000   ", "   15250   ", "   15500   ", "   15750   ", "   16000   "};
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    List<Nilai> listNilaiAwal;
    List<SampleObject> sampleObjects = sampleObjects();
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    public emaP(Context context2) {
        super(context2);
        this.context = context2;
        init();
    }

    public emaP(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
        init();
    }

    public emaP(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        this.context = context2;
        init();
    }

    @RequiresApi(api = 21)
    public emaP(Context context2, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context2, attrs, defStyleAttr, defStyleRes);
        this.context = context2;
        init();
    }

    /* access modifiers changed from: package-private */
    public List<SampleObject> sampleObjects() {
        ArrayList arrayList = new ArrayList();
        int nilai_tps = 0;
        for (int x = 0; x < 21; x++) {
            if (x == 1) {
                nilai_tps = 2;
            } else if (x > 1 && x < 20) {
                nilai_tps = (x * 5) - 5;
            } else if (x == 20) {
                nilai_tps = 100;
            }
            arrayList.add(new SampleObject("" + (String.valueOf(nilai_tps) + "%"), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }
        return arrayList;
    }

    public void init() {
        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        this.horizontalScrollViewB.addView(this.tableB);
        this.scrollViewC.addView(this.tableC);
        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);
        addComponentToMainLayout();
        setBackgroundColor(-16776961);
        addTableRowToTableA();
        addTableRowToTableB();
        resizeHeaderHeight();
        getTableRowHeaderCellWidth();
        generateTableC_AndTable_B();
        resizeBodyTableRowHeight();
        getNilai();
    }

    private void initComponents() {
        this.tableA = new TableLayout(getContext());
        this.tableB = new TableLayout(getContext());
        this.tableC = new TableLayout(getContext());
        this.tableD = new TableLayout(getContext());
        this.scrollViewC = new MyScrollView(getContext());
        this.scrollViewD = new MyScrollView(getContext());
        this.scrollViewD.setOnTouchListener(new OnTouch());
        this.horizontalScrollViewB = new MyHorizontalScrollView(getContext());
        this.horizontalScrollViewD = new MyHorizontalScrollView(getContext());
        this.horizontalScrollViewD.setOnTouchListener(new OnTouch());
        this.tableA.setBackgroundColor(-16776961);
        this.horizontalScrollViewB.setBackgroundColor(-16776961);
    }

    private void setComponentsId() {
        this.tableA.setId(10000);
        this.horizontalScrollViewB.setId(20000);
        this.scrollViewC.setId(30000);
        this.scrollViewD.setId(40000);
    }

    private void getNilai() {
        cek_nilai();
        if (MappingHandle.list_fuel.size() < 2) {
            for (int i = 1; i <= 1281; i++) {
                ((EditText) findViewById(i)).setText("50");
            }
            return;
        }
        for (int i2 = 1; i2 <= 1281; i2++) {
            EditText v = (EditText) findViewById(i2);
            try {
                v.setText(String.valueOf(MappingHandle.list_fuel.get(i2 - 1)));
            } catch (IndexOutOfBoundsException e) {
                v.setText("0");
            }
        }
    }

    private void cek_nilai() {
        if (MappingHandle.list_fuel.size() < 2) {
            for (int i = 0; i < 1281; i++) {
                if (i / 61 == 0) {
                    MappingHandle.list_fuel.add("68");
                } else {
                    MappingHandle.list_fuel.add("5");
                }
            }
        }
        if (MappingHandle.list_history.size() < 2) {
            for (int i2 = 0; i2 < 1281; i2++) {
                MappingHandle.list_history.add("255");
            }
        }
    }

    private void setScrollViewAndHorizontalScrollViewTag() {
        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");
        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    private void addComponentToMainLayout() {
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(-2, -2);
        componentB_Params.addRule(1, this.tableA.getId());
        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(-2, -2);
        componentC_Params.addRule(3, this.tableA.getId());
        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(-2, -2);
        componentD_Params.addRule(1, this.scrollViewC.getId());
        componentD_Params.addRule(3, this.horizontalScrollViewB.getId());
        addView(this.tableA);
        addView(this.horizontalScrollViewB, componentB_Params);
        addView(this.scrollViewC, componentC_Params);
        addView(this.scrollViewD, componentD_Params);
    }

    private void addTableRowToTableA() {
        this.tableA.addView(componentATableRow());
    }

    private void addTableRowToTableB() {
        this.tableB.addView(componentBTableRow());
    }

    /* access modifiers changed from: package-private */
    public TableRow componentATableRow() {
        TableRow componentATableRow = new TableRow(getContext());
        componentATableRow.addView(headerTextView(this.headers[0]));
        return componentATableRow;
    }

    /* access modifiers changed from: package-private */
    public TableRow componentBTableRow() {
        TableRow componentBTableRow = new TableRow(getContext());
        int headerFieldCount = this.headers.length;
        TableRow.LayoutParams params = new TableRow.LayoutParams(-2, -1);
        params.setMargins(2, 0, 0, 0);
        for (int x = 0; x < headerFieldCount - 1; x++) {
            TextView textView = headerTextView(this.headers[x + 1]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }
        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("TableMainLayout.java", this.headerCellsWidth[x] + "");
        }
        for (int i = 1; i <= this.sampleObjects.size(); i++) {
            SampleObject sampleObject = this.sampleObjects.get(i - 1);
            TableRow tableRowForTableC = tableRowForTableC(sampleObject);
            TableRow taleRowForTableD = taleRowForTableD(sampleObject, i);
            tableRowForTableC.setBackgroundColor(0);
            taleRowForTableD.setBackgroundColor(0);
            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
        }
    }

    /* access modifiers changed from: package-private */
    public TableRow tableRowForTableC(SampleObject sampleObject) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], -1);
        params.setMargins(0, 2, 0, 0);
        TableRow tableRowForTableC = new TableRow(getContext());
        tableRowForTableC.addView(bodyTextView(sampleObject.header1), params);
        return tableRowForTableC;
    }

    /* access modifiers changed from: package-private */
    public TableRow taleRowForTableD(SampleObject sampleObject, int rowCount) {
        TableRow taleRowForTableD = new TableRow(getContext());
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        int akhir = rowCount * loopCount;
        int temp = 1;
        int xxx = 0;
        for (int x = ((rowCount * loopCount) + 1) - loopCount; x <= akhir; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[temp], -1);
            temp++;
            params.setMargins(2, 2, 0, 0);
            this.EditTextB = bodyTextView1("0");
            System.out.println(x);
            position p = new position();
            p.setId(Integer.valueOf(x));
            p.setX(Integer.valueOf(xxx));
            p.setY(Integer.valueOf(rowCount - 1));
            StaticClass.listPosition.add(p);
            xxx++;
            this.EditTextB.setId(x);
            this.EditTextB.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    StaticClass.selectionStatus = true;
                    v.setBackgroundColor(-16776961);
                    v.setTag("Warna BLUE");
                    StaticClass.position.clear();
                    StaticClass.position.add(Integer.valueOf(v.getId()));
                    return true;
                }
            });
            this.EditTextB.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case 0:
                            emaP.this.EditTextB.setFocusable(true);
                            if (StaticClass.selectionStatus) {
                                v.setBackgroundColor(-16711681);
                                if (StaticClass.position.size() != 2) {
                                    StaticClass.position.add(Integer.valueOf(v.getId()));
                                    if (StaticClass.position.size() == 2) {
                                        int x1 = 0;
                                        int x2 = 0;
                                        int y1 = 0;
                                        int y2 = 0;
                                        for (int i = 0; i < 1281; i++) {
                                            if (StaticClass.listPosition.get(i).getId().intValue() == StaticClass.position.get(0).intValue()) {
                                                x1 = StaticClass.listPosition.get(i).getX().intValue();
                                                y1 = StaticClass.listPosition.get(i).getY().intValue();
                                            } else if (StaticClass.listPosition.get(i).getId().intValue() == StaticClass.position.get(1).intValue()) {
                                                x2 = StaticClass.listPosition.get(i).getX().intValue();
                                                y2 = StaticClass.listPosition.get(i).getY().intValue();
                                            }
                                        }
                                        if (x2 < x1) {
                                            int temp = x2;
                                            x2 = x1;
                                            x1 = temp;
                                        }
                                        if (y2 < y1) {
                                            int temp2 = y1;
                                            y1 = y2;
                                            y2 = temp2;
                                        }
                                        for (int i2 = 0; i2 < 1281; i2++) {
                                            if (StaticClass.listPosition.get(i2).getX().intValue() < x1 || StaticClass.listPosition.get(i2).getX().intValue() > x2 || StaticClass.listPosition.get(i2).getY().intValue() < y1 || StaticClass.listPosition.get(i2).getY().intValue() > y2) {
                                                View x = emaP.this.findViewById(StaticClass.listPosition.get(i2).getId().intValue());
                                                x.setBackgroundColor(-1);
                                                x.setTag("Warna WHITE");
                                            } else {
                                                View x3 = emaP.this.findViewById(StaticClass.listPosition.get(i2).getId().intValue());
                                                x3.setBackgroundColor(-16711681);
                                                x3.setTag("Warna CYAN");
                                            }
                                        }
                                    }
                                } else {
                                    StaticClass.selectionStatus = false;
                                    for (int i3 = 1; i3 <= 1281; i3++) {
                                        View views = emaP.this.findViewById(i3);
                                        views.setTag("Warna PUTIH");
                                        views.setBackgroundColor(-1);
                                    }
                                    StaticClass.position.clear();
                                    StaticClass.position.add(Integer.valueOf(v.getId()));
                                }
                                return false;
                            }
                            for (int i4 = 1; i4 <= 1281; i4++) {
                                View views2 = emaP.this.findViewById(i4);
                                views2.setTag("Warna PUTIH");
                                views2.setBackgroundColor(-1);
                            }
                            return false;
                        case 1:
                            return false;
                        case 2:
                            return false;
                        default:
                            return false;
                    }
                }
            });
            taleRowForTableD.addView(this.EditTextB, params);
        }
        return taleRowForTableD;
    }

    /* access modifiers changed from: package-private */
    public TextView bodyTextView(String label) {
        TextView bodyTextView = new TextView(getContext());
        bodyTextView.setBackgroundColor(-1);
        bodyTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        bodyTextView.setText(label);
        bodyTextView.setGravity(17);
        bodyTextView.setPadding(5, 5, 5, 5);
        bodyTextView.setTextSize(12.0f);
        return bodyTextView;
    }

    /* access modifiers changed from: package-private */
    public EditText bodyTextView1(String label) {
        EditText bodyTextView1 = new EditText(getContext());
        bodyTextView1.setBackgroundColor(-1);
        bodyTextView1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        bodyTextView1.setText(label);
        bodyTextView1.setGravity(17);
        bodyTextView1.setPadding(5, 5, 5, 5);
        bodyTextView1.setEnabled(false);
        bodyTextView1.setTextSize(12.0f);
        return bodyTextView1;
    }

    /* access modifiers changed from: package-private */
    public TextView headerTextView(String label) {
        TextView headerTextView = new TextView(getContext());
        headerTextView.setBackgroundColor(-1);
        headerTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        headerTextView.setText(label);
        headerTextView.setGravity(17);
        headerTextView.setPadding(5, 5, 5, 5);
        headerTextView.setTextSize(12.0f);
        return headerTextView;
    }

    /* access modifiers changed from: package-private */
    public void resizeHeaderHeight() {
        TableRow tableRow;
        int finalHeight;
        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);
        int rowAHeight = viewHeight(productNameHeaderTableRow);
        int rowBHeight = viewHeight(productInfoTableRow);
        if (rowAHeight < rowBHeight) {
            tableRow = productNameHeaderTableRow;
        } else {
            tableRow = productInfoTableRow;
        }
        if (rowAHeight > rowBHeight) {
            finalHeight = rowAHeight;
        } else {
            finalHeight = rowBHeight;
        }
        matchLayoutHeight(tableRow, finalHeight);
    }

    /* access modifiers changed from: package-private */
    public void getTableRowHeaderCellWidth() {
        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        for (int x = 0; x < tableAChildCount + tableBChildCount; x++) {
            if (x == 0) {
                this.headerCellsWidth[x] = viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void resizeBodyTableRowHeight() {
        TableRow tableRow;
        int finalHeight;
        int tableC_ChildCount = this.tableC.getChildCount();
        for (int x = 0; x < tableC_ChildCount; x++) {
            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);
            int rowAHeight = viewHeight(productNameHeaderTableRow);
            int rowBHeight = viewHeight(productInfoTableRow);
            if (rowAHeight < rowBHeight) {
                tableRow = productNameHeaderTableRow;
            } else {
                tableRow = productInfoTableRow;
            }
            if (rowAHeight > rowBHeight) {
                finalHeight = rowAHeight;
            } else {
                finalHeight = rowBHeight;
            }
            matchLayoutHeight(tableRow, finalHeight);
        }
    }

    private void matchLayoutHeight(TableRow tableRow, int height) {
        int tableRowChildCount = tableRow.getChildCount();
        if (tableRow.getChildCount() == 1) {
            TableRow.LayoutParams params = (TableRow.LayoutParams) tableRow.getChildAt(0).getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);
            return;
        }
        for (int x = 0; x < tableRowChildCount; x++) {
            TableRow.LayoutParams params2 = (TableRow.LayoutParams) tableRow.getChildAt(x).getLayoutParams();
            if (!isTheHeighestLayout(tableRow, x)) {
                params2.height = height - (params2.bottomMargin + params2.topMargin);
                return;
            }
        }
    }

    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {
        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;
        for (int x = 0; x < tableRowChildCount; x++) {
            int height = viewHeight(tableRow.getChildAt(x));
            if (viewHeight <= height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }
        return heighestViewPosition == layoutPosition;
    }

    private int viewHeight(View view) {
        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

    private int viewWidth(View view) {
        view.measure(0, 0);
        return view.getMeasuredWidth();
    }

    class MyHorizontalScrollView extends HorizontalScrollView {
        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (((String) getTag()).equalsIgnoreCase("horizontal scroll view b")) {
                emaP.this.horizontalScrollViewD.scrollTo(l, 0);
            } else {
                emaP.this.horizontalScrollViewB.scrollTo(l, 0);
            }
        }
    }

    class MyScrollView extends ScrollView {
        public MyScrollView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (((String) getTag()).equalsIgnoreCase("scroll view c")) {
                emaP.this.scrollViewD.scrollTo(0, t);
            } else {
                emaP.this.scrollViewC.scrollTo(0, t);
            }
        }
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        YScrollDetector() {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }

    private class OnTouch implements View.OnTouchListener {
        private OnTouch() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
}

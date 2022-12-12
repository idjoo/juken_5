package juken.android.com.juken_5.VirtualDyno.AccelerationTime;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
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
import juken.android.com.juken_5.range_int_min;

public class LayoutAcceleration extends RelativeLayout {
    public final String TAG = "TableMainLayout.java";
    TextView TextViewB;
    Context context;
    int[] headerCellsWidth = new int[this.headers.length];
    String[] headers = {"                    ", " 1000 -> 1250 ", " 1250 -> 1500 ", " 1500 -> 1750 ", " 1750 -> 2000 ", " 2000 -> 2250 ", " 2250 -> 2500 ", " 2500 -> 2750 ", " 2750 -> 3000 ", " 3000 -> 3250 ", " 3250 -> 3500 ", " 3500 -> 3750 ", " 3750 -> 4000 ", " 4000 -> 4250 ", " 4250 -> 4500 ", " 4500 -> 4750 ", " 4750 -> 5000 ", " 5000 -> 5250 ", " 5250 -> 5500 ", " 5500 -> 5750 ", " 5750 -> 6000 ", " 6000 -> 6250 ", " 6250 -> 6500 ", " 6500 -> 6750 ", " 6750 -> 7000 ", " 7000 -> 7250 ", " 7250 -> 7500 ", " 7500 -> 7750 ", " 7750 -> 8000 ", " 8000 -> 8250 ", " 8250 -> 8500 ", " 8500 -> 8750 ", " 8750 -> 9000 ", " 9000 -> 9250 ", " 9250 -> 9500 ", " 9500 -> 9750 ", " 9750 -> 10000 ", " 10000 -> 10250 ", " 10250 -> 10500 ", " 10500 -> 10750 ", " 10750 -> 11000 ", " 11000 -> 11250 ", " 11250 -> 11500 ", " 11500 -> 11750 ", " 11750 -> 12000 ", " 12000 -> 12250 ", " 12250 -> 12500 ", " 12500 -> 12750 ", " 12750 -> 13000 ", " 13000 -> 13250 ", " 13250 -> 13500 ", " 13500 -> 13750 ", " 13750 -> 14000 ", " 14000 -> 14250 ", " 14250 -> 14500 ", " 14500 -> 14750 ", " 14750 -> 15000 ", " 15000 -> 15250 ", " 15250 -> 15500 ", " 15500 -> 15750 ", " 15750 -> 16000 "};
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    int id_text_view = 1680;
    List<SampleObject> sampleObjects = sampleObjects();
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    public LayoutAcceleration(Context context2) {
        super(context2);
        this.context = context2;
        init();
    }

    public LayoutAcceleration(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
        init();
    }

    public LayoutAcceleration(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        this.context = context2;
        init();
    }

    @RequiresApi(api = 21)
    public LayoutAcceleration(Context context2, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context2, attrs, defStyleAttr, defStyleRes);
        this.context = context2;
        init();
    }

    /* access modifiers changed from: package-private */
    public List<SampleObject> sampleObjects() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SampleObject("   Av   ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        TextView textView = bodyTextView(sampleObject.header1);
        textView.setId(this.id_text_view);
        this.id_text_view++;
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;
    }

    /* access modifiers changed from: package-private */
    public TableRow taleRowForTableD(SampleObject sampleObject, int rowCount) {
        TableRow taleRowForTableD = new TableRow(getContext());
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        int akhir = rowCount * loopCount;
        int temp = 1;
        for (int x = ((rowCount * loopCount) + 1) - loopCount; x <= akhir; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[temp], -1);
            temp++;
            params.setMargins(2, 2, 0, 0);
            this.TextViewB = bodyTextView("-");
            this.TextViewB.setId(x + 122);
            taleRowForTableD.addView(this.TextViewB, params);
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
        bodyTextView1.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        bodyTextView1.setFilters(new InputFilter[]{new range_int_min(-100, 100)});
        bodyTextView1.setImeOptions(268435456);
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
                LayoutAcceleration.this.horizontalScrollViewD.scrollTo(l, 0);
            } else {
                LayoutAcceleration.this.horizontalScrollViewB.scrollTo(l, 0);
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
                LayoutAcceleration.this.scrollViewD.scrollTo(0, t);
            } else {
                LayoutAcceleration.this.scrollViewC.scrollTo(0, t);
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

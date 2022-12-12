package juken.android.com.juken_5.folder_analisis;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TableMainLayoutAna extends RelativeLayout {
    CheckBox CheckBoxB;
    EditText EditTextB;
    public final String TAG = "TableMainLayout.java";
    TextView TextViewB;
    Context context;
    int[] headerCellsWidth = new int[this.headers.length];
    String[] headers = {"         TPS          ", "          RPM           ", "       IT       ", "     BM Tot     ", "            ", "    Suggestion IT   ", "                     Suggestion Flow Rate                     "};
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    int idC = 1000;
    List<Nilai> listNilaiAwal;
    int rpm = 1000;
    List<SampleObject> sampleObjects = sampleObjects();
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    public TableMainLayoutAna(Context context2) {
        super(context2);
        this.context = context2;
        init();
    }

    public TableMainLayoutAna(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
        init();
    }

    public TableMainLayoutAna(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        this.context = context2;
        init();
    }

    @RequiresApi(api = 21)
    public TableMainLayoutAna(Context context2, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context2, attrs, defStyleAttr, defStyleRes);
        this.context = context2;
        init();
    }

    /* access modifiers changed from: package-private */
    public List<SampleObject> sampleObjects() {
        List<SampleObject> sampleObjects2 = new ArrayList<>();
        for (int x = 1; x <= 61; x++) {
            sampleObjects2.add(new SampleObject("100%", "", "", "", "", "", ""));
        }
        return sampleObjects2;
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
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView tps = (TextView) TableMainLayoutAna.this.findViewById(view.getId());
                if (((ColorDrawable) tps.getBackground()).getColor() != -256) {
                    int id = TableMainLayoutAna.this.mod(view.getId(), 1000) * 6;
                    TextView rpm = (TextView) TableMainLayoutAna.this.findViewById(id + 1);
                    EditText it = (EditText) TableMainLayoutAna.this.findViewById(id + 2);
                    TextView bm = (TextView) TableMainLayoutAna.this.findViewById(id + 3);
                    if (AnalisisPassData.AnaArray.size() < 8) {
                        AnalisisPassData.AnaArray.add(new AnalisisParam(tps.getText().toString(), rpm.getText().toString(), it.getText().toString(), bm.getText().toString()));
                    } else {
                        AnalisisPassData.AnaArray.remove(7);
                        AnalisisPassData.AnaArray.add(new AnalisisParam(tps.getText().toString(), rpm.getText().toString(), it.getText().toString(), bm.getText().toString()));
                    }
                    Collections.sort(AnalisisPassData.AnaArray, AnalisisParam.RpmComparator);
                    for (int i = 1000; i < 1061; i++) {
                        ((TextView) TableMainLayoutAna.this.findViewById(i)).setBackgroundColor(-1);
                    }
                    Iterator<AnalisisParam> it2 = AnalisisPassData.AnaArray.iterator();
                    while (it2.hasNext()) {
                        ((TextView) TableMainLayoutAna.this.findViewById(((Integer.valueOf(it2.next().getRpm()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4) + 1000)).setBackgroundColor(InputDeviceCompat.SOURCE_ANY);
                    }
                }
            }
        });
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;
    }

    /* access modifiers changed from: package-private */
    public TableRow taleRowForTableD(SampleObject sampleObject, int rowCount) {
        TableRow taleRowForTableD = new TableRow(getContext());
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        int akhir = rowCount * loopCount;
        int temp = 1;
        int xxx = 0;
        int posisi = 0;
        for (int x = ((rowCount * loopCount) + 1) - loopCount; x <= akhir; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[temp], -1);
            temp++;
            params.setMargins(2, 2, 0, 0);
            position p = new position();
            switch (posisi) {
                case 0:
                    posisi = 1;
                    this.TextViewB = headerTextView(String.valueOf(this.rpm));
                    this.rpm += ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                    if (this.rpm > 16000) {
                        this.rpm = 1000;
                    }
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.TextViewB.setId(x);
                    taleRowForTableD.addView(this.TextViewB, params);
                    break;
                case 1:
                    posisi = 2;
                    this.EditTextB = bodyTextView1("0");
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.EditTextB.setId(x);
                    this.EditTextB.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            StaticClass.selectionStatus = true;
                            v.setBackgroundColor(-16711681);
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
                                    TableMainLayoutAna.this.EditTextB.setFocusable(true);
                                    if (StaticClass.selectionStatus) {
                                        if (StaticClass.position.size() != 2) {
                                            StaticClass.position.add(Integer.valueOf(v.getId()));
                                            if (StaticClass.position.size() == 2) {
                                                int x1 = 0;
                                                int x2 = 0;
                                                int y1 = 0;
                                                int y2 = 0;
                                                for (int i = 0; i < 366; i++) {
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
                                                for (int i2 = 0; i2 < 366; i2++) {
                                                    if (StaticClass.listPosition.get(i2).getX().intValue() < x1 || StaticClass.listPosition.get(i2).getX().intValue() > x2 || StaticClass.listPosition.get(i2).getY().intValue() < y1 || StaticClass.listPosition.get(i2).getY().intValue() > y2) {
                                                        TableMainLayoutAna.this.findViewById(StaticClass.listPosition.get(i2).getId().intValue()).setTag("Warna WHITE");
                                                    } else {
                                                        View x = TableMainLayoutAna.this.findViewById(StaticClass.listPosition.get(i2).getId().intValue());
                                                        x.setBackgroundColor(-16711681);
                                                        x.setTag("Warna CYAN");
                                                    }
                                                }
                                            }
                                        } else {
                                            StaticClass.selectionStatus = false;
                                            for (int i3 = 2; i3 < 366; i3 += 6) {
                                                View views = TableMainLayoutAna.this.findViewById(i3);
                                                views.setTag("Warna PUTIH");
                                                views.setBackgroundColor(-1);
                                            }
                                            StaticClass.position.clear();
                                            StaticClass.position.add(Integer.valueOf(v.getId()));
                                        }
                                        return false;
                                    }
                                    for (int i4 = 2; i4 < 366; i4 += 6) {
                                        View views2 = TableMainLayoutAna.this.findViewById(i4);
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
                    break;
                case 2:
                    posisi = 3;
                    this.TextViewB = headerTextView("20.00");
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.TextViewB.setId(x);
                    taleRowForTableD.addView(this.TextViewB, params);
                    break;
                case 3:
                    posisi = 4;
                    this.CheckBoxB = bodyCheckBox();
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.CheckBoxB.setId(x);
                    this.CheckBoxB.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (((CheckBox) TableMainLayoutAna.this.findViewById(view.getId())).isChecked()) {
                                try {
                                    TextView edit = (TextView) TableMainLayoutAna.this.findViewById(view.getId() + 1);
                                    if (!edit.getText().toString().equals("-")) {
                                        EditText ganti = (EditText) TableMainLayoutAna.this.findViewById(view.getId() - 2);
                                        ganti.setText(edit.getText().toString());
                                        ganti.setTextColor(SupportMenu.CATEGORY_MASK);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(TableMainLayoutAna.this.context, e.toString(), 1).show();
                                }
                            }
                        }
                    });
                    taleRowForTableD.addView(this.CheckBoxB, params);
                    break;
                case 4:
                    posisi = 5;
                    this.TextViewB = headerTextView1("-");
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.TextViewB.setId(x);
                    taleRowForTableD.addView(this.TextViewB, params);
                    break;
                case 5:
                    posisi = 0;
                    this.TextViewB = headerTextView1("-");
                    p.setId(Integer.valueOf(x));
                    p.setX(Integer.valueOf(xxx));
                    p.setY(Integer.valueOf(rowCount - 1));
                    StaticClass.listPosition.add(p);
                    xxx++;
                    this.TextViewB.setId(x);
                    taleRowForTableD.addView(this.TextViewB, params);
                    break;
            }
        }
        return taleRowForTableD;
    }

    /* access modifiers changed from: private */
    public int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
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
        bodyTextView.setId(this.idC);
        this.idC++;
        return bodyTextView;
    }

    /* access modifiers changed from: package-private */
    public CheckBox bodyCheckBox() {
        CheckBox bodyCheckBox = new CheckBox(getContext());
        bodyCheckBox.setChecked(false);
        bodyCheckBox.setBackgroundColor(-1);
        bodyCheckBox.setButtonTintList(new ColorStateList(new int[][]{new int[]{16842912}, new int[0]}, new int[]{-16776961, ViewCompat.MEASURED_STATE_MASK}));
        return bodyCheckBox;
    }

    /* access modifiers changed from: package-private */
    public EditText bodyTextView1(String label) {
        EditText bodyTextView1 = new EditText(getContext());
        bodyTextView1.setBackgroundColor(-1);
        bodyTextView1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        bodyTextView1.setText(label);
        bodyTextView1.setGravity(17);
        bodyTextView1.setPadding(5, 5, 5, 5);
        bodyTextView1.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        bodyTextView1.setTextSize(12.0f);
        bodyTextView1.setImeOptions(268435456);
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
    public TextView headerTextView1(String label) {
        TextView headerTextView = new TextView(getContext());
        headerTextView.setBackgroundColor(-1);
        headerTextView.setTextColor(SupportMenu.CATEGORY_MASK);
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
                TableMainLayoutAna.this.horizontalScrollViewD.scrollTo(l, 0);
            } else {
                TableMainLayoutAna.this.horizontalScrollViewB.scrollTo(l, 0);
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
                TableMainLayoutAna.this.scrollViewD.scrollTo(0, t);
            } else {
                TableMainLayoutAna.this.scrollViewC.scrollTo(0, t);
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

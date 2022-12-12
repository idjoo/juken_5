package juken.android.com.juken_5.folder_analisis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import juken.android.com.juken_5.R;

public class AnalisisImageFragment extends Fragment {
    ArrayList array_bm = new ArrayList();
    ArrayList array_it = new ArrayList();
    ArrayList array_rpm = new ArrayList();
    ImageView image;
    String nilai_tps = "";
    View view;

    public static AnalisisImageFragment newInstance(String param1, String param2) {
        AnalisisImageFragment fragment = new AnalisisImageFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_analisis_image, container, false);
        this.image = (ImageView) this.view.findViewById(R.id.image_analisis);
        return this.view;
    }

    public void onResume() {
        super.onResume();
        this.array_rpm.clear();
        this.array_it.clear();
        this.array_bm.clear();
        Iterator<AnalisisParam> it = AnalisisPassData.AnaArray.iterator();
        while (it.hasNext()) {
            String[] pecah = it.next().toString().split(";");
            this.nilai_tps = pecah[0];
            this.array_rpm.add(pecah[1]);
            this.array_it.add(pecah[2]);
            this.array_bm.add(pecah[3]);
        }
        draw_arrow();
    }

    private void draw_arrow() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.analisis_background, options);
            Bitmap.Config config = bm.getConfig();
            Bitmap newImage = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), config);
            Canvas c = new Canvas(newImage);
            c.drawBitmap(bm, 0.0f, 0.0f, (Paint) null);
            Float ex_open = Float.valueOf(((-123.0f * Float.valueOf(AnalisisPassData.open_exhaust).floatValue()) + 27810.0f) / 90.0f);
            Float ex_close = Float.valueOf(((123.0f * Float.valueOf(AnalisisPassData.close_exhaust).floatValue()) + 49950.0f) / 90.0f);
            Float in_open = Float.valueOf(((-123.0f * Float.valueOf(AnalisisPassData.open_intake).floatValue()) + 49950.0f) / 90.0f);
            Float in_close = Float.valueOf(((123.0f * Float.valueOf(AnalisisPassData.close_intake).floatValue()) + 72090.0f) / 90.0f);
            Paint paintL = new Paint();
            paintL.setStrokeWidth(3.0f);
            paintL.setColor(Color.argb(255, 255, 153, 0));
            paintL.setDither(false);
            paintL.setStyle(Paint.Style.STROKE);
            paintL.setStrokeJoin(Paint.Join.ROUND);
            paintL.setStrokeCap(Paint.Cap.ROUND);
            Paint paintL1 = new Paint();
            paintL1.setStrokeWidth(3.0f);
            paintL1.setColor(Color.argb(255, 82, 255, 82));
            paintL1.setDither(false);
            paintL1.setStyle(Paint.Style.STROKE);
            paintL1.setStrokeJoin(Paint.Join.ROUND);
            paintL1.setStrokeCap(Paint.Cap.ROUND);
            c.drawLine(ex_close.floatValue(), 35.0f, ex_close.floatValue(), 390.0f, paintL);
            c.drawLine(ex_open.floatValue(), 35.0f, ex_open.floatValue(), 390.0f, paintL);
            c.drawLine(in_close.floatValue(), 35.0f, in_close.floatValue(), 390.0f, paintL1);
            c.drawLine(in_open.floatValue(), 35.0f, in_open.floatValue(), 390.0f, paintL1);
            Paint paintT = new Paint();
            paintT.setStrokeWidth(4.0f);
            paintT.setColor(Color.argb(255, 255, 153, 0));
            paintT.setStyle(Paint.Style.FILL_AND_STROKE);
            paintT.setAntiAlias(true);
            Paint paintT1 = new Paint();
            paintT1.setStrokeWidth(4.0f);
            paintT1.setColor(Color.argb(255, 82, 255, 82));
            paintT1.setStyle(Paint.Style.FILL_AND_STROKE);
            paintT1.setAntiAlias(true);
            Paint paintText = new Paint();
            paintText.setColor(ViewCompat.MEASURED_STATE_MASK);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTextSize(18.0f);
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            Path path1 = new Path();
            path1.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(309.0f, 425.0f);
            path.lineTo(ex_open.floatValue() + 5.0f, 425.0f);
            path.lineTo(ex_open.floatValue(), 433.0f);
            path.lineTo(ex_open.floatValue() + 5.0f, 441.0f);
            path.lineTo(309.0f, 441.0f);
            path.close();
            c.drawPath(path, paintT);
            path.moveTo(555.0f, 425.0f);
            path.lineTo(ex_close.floatValue() - 5.0f, 425.0f);
            path.lineTo(ex_close.floatValue(), 433.0f);
            path.lineTo(ex_close.floatValue() - 5.0f, 441.0f);
            path.lineTo(555.0f, 441.0f);
            path.close();
            c.drawPath(path, paintT);
            path1.moveTo(555.0f, 425.0f);
            path1.lineTo(in_open.floatValue() + 5.0f, 425.0f);
            path1.lineTo(in_open.floatValue(), 433.0f);
            path1.lineTo(in_open.floatValue() + 5.0f, 441.0f);
            path1.lineTo(555.0f, 441.0f);
            path1.close();
            c.drawPath(path1, paintT1);
            path1.moveTo(801.0f, 425.0f);
            path1.lineTo(in_close.floatValue() - 5.0f, 425.0f);
            path1.lineTo(in_close.floatValue(), 433.0f);
            path1.lineTo(in_close.floatValue() - 5.0f, 441.0f);
            path1.lineTo(801.0f, 441.0f);
            path1.close();
            c.drawPath(path1, paintT1);
            c.drawText(AnalisisPassData.open_exhaust, 290.0f, 440.0f, paintText);
            c.drawText(AnalisisPassData.close_exhaust, 563.0f, 440.0f, paintText);
            c.drawText(AnalisisPassData.open_intake, 530.0f, 440.0f, paintText);
            c.drawText(AnalisisPassData.close_intake, 805.0f, 440.0f, paintText);
            Paint paintInj = new Paint();
            paintInj.setStrokeWidth(4.0f);
            paintInj.setColor(-7829368);
            paintInj.setStyle(Paint.Style.FILL_AND_STROKE);
            paintInj.setAntiAlias(true);
            Paint paintText1 = new Paint();
            paintText1.setColor(ViewCompat.MEASURED_STATE_MASK);
            paintText1.setStyle(Paint.Style.FILL);
            paintText1.setTextSize(10.0f);
            c.drawText("TPS : " + this.nilai_tps, 3.0f, 440.0f, paintText);
            int posisi = 50;
            for (int i = 0; i < this.array_it.size(); i++) {
                Float inj_start = Float.valueOf(((984.0f * Float.valueOf(this.array_it.get(i).toString()).floatValue()) + 45360.0f) / 720.0f);
                Float nilai_bm = Float.valueOf(this.array_bm.get(i).toString());
                Float panjang = Float.valueOf(nilai_bm.floatValue() / Float.valueOf(500.0f / (Float.valueOf(this.array_rpm.get(i).toString()).floatValue() * 3.0f)).floatValue());
                Float panjang1 = Float.valueOf((((984.0f * (Float.valueOf(this.array_it.get(i).toString()).floatValue() + panjang.floatValue())) + 45360.0f) / 720.0f) - inj_start.floatValue());
                Float panjang2 = Float.valueOf(((984.0f * (Float.valueOf(this.array_it.get(i).toString()).floatValue() + panjang.floatValue())) + 45360.0f) / 720.0f);
                if (panjang2.floatValue() > 1047.0f) {
                    Float valueOf = Float.valueOf(1047.0f - inj_start.floatValue());
                    Float panjang_sisa = Float.valueOf(panjang2.floatValue() - 1047.0f);
                    Float titik_akhir = Float.valueOf(panjang_sisa.floatValue() + 63.0f);
                    Path path_lebih = new Path();
                    path_lebih.setFillType(Path.FillType.EVEN_ODD);
                    Path path_lebih1 = new Path();
                    path_lebih1.setFillType(Path.FillType.EVEN_ODD);
                    Path path2 = path_lebih;
                    path2.moveTo(inj_start.floatValue(), (float) posisi);
                    path_lebih.lineTo(1047.0f, (float) posisi);
                    path_lebih.lineTo(1047.0f, (float) (posisi + 16));
                    path_lebih.lineTo(inj_start.floatValue(), (float) (posisi + 16));
                    path_lebih.close();
                    c.drawPath(path_lebih, paintInj);
                    path_lebih1.moveTo(63.0f, (float) posisi);
                    Path path3 = path_lebih1;
                    path3.lineTo(titik_akhir.floatValue() - 10.0f, (float) posisi);
                    path_lebih1.lineTo(titik_akhir.floatValue(), (float) (posisi + 8));
                    path_lebih1.lineTo(titik_akhir.floatValue() - 10.0f, (float) (posisi + 16));
                    path_lebih1.lineTo(63.0f, (float) (posisi + 16));
                    path_lebih1.close();
                    c.drawPath(path_lebih1, paintInj);
                    if (panjang_sisa.floatValue() < 56.0f) {
                        c.drawText(nilai_bm.toString() + "mS", (inj_start.floatValue() + 1047.0f) / 2.0f, (float) (posisi + 14), paintText);
                    } else {
                        c.drawText(nilai_bm.toString() + "mS", 72.0f, (float) (posisi + 14), paintText);
                    }
                    c.drawText(this.array_it.get(i).toString(), inj_start.floatValue() - 15.0f, (float) (posisi - 2), paintText);
                    c.drawText(String.valueOf(panjang.floatValue() + Float.valueOf(this.array_it.get(i).toString()).floatValue()), titik_akhir.floatValue() - 15.0f, (float) (posisi - 2), paintText);
                    c.drawText(this.array_rpm.get(i).toString(), 3.0f, (float) (posisi + 14), paintText);
                } else {
                    Path path_cukup = new Path();
                    path_cukup.setFillType(Path.FillType.EVEN_ODD);
                    Path path4 = path_cukup;
                    path4.moveTo(inj_start.floatValue(), (float) posisi);
                    Path path5 = path_cukup;
                    path5.lineTo(panjang2.floatValue() - 10.0f, (float) posisi);
                    path_cukup.lineTo(panjang2.floatValue(), (float) (posisi + 8));
                    path_cukup.lineTo(panjang2.floatValue() - 10.0f, (float) (posisi + 16));
                    path_cukup.lineTo(inj_start.floatValue(), (float) (posisi + 16));
                    path_cukup.close();
                    c.drawPath(path_cukup, paintInj);
                    if (panjang1.floatValue() < 40.0f) {
                        c.drawText(nilai_bm.toString() + "mS", panjang2.floatValue(), (float) (posisi + 14), paintText);
                    } else {
                        c.drawText(nilai_bm.toString() + "mS", ((inj_start.floatValue() + panjang2.floatValue()) / 2.0f) - 20.0f, (float) (posisi + 14), paintText);
                    }
                    c.drawText(this.array_it.get(i).toString(), inj_start.floatValue() - 15.0f, (float) (posisi - 2), paintText);
                    c.drawText(String.valueOf(panjang.floatValue() + Float.valueOf(this.array_it.get(i).toString()).floatValue()), panjang2.floatValue(), (float) (posisi - 2), paintText);
                    c.drawText(this.array_rpm.get(i).toString(), 3.0f, (float) (posisi + 14), paintText);
                }
                posisi += 45;
            }
            this.image.setImageBitmap(newImage);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), 1).show();
        }
    }
}

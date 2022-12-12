package juken.android.com.juken_5.folder_analisis;

import java.util.Comparator;

public class AnalisisParam {
    public static Comparator<AnalisisParam> RpmComparator = new Comparator<AnalisisParam>() {
        public int compare(AnalisisParam s1, AnalisisParam s2) {
            return Integer.valueOf(s2.getRpm()).intValue() - Integer.valueOf(s1.getRpm()).intValue();
        }
    };
    private String bm;
    private String it;
    private String rpm;
    private String tps;

    public AnalisisParam(String tps2, String rpm2, String it2, String bm2) {
        this.tps = tps2;
        this.rpm = rpm2;
        this.it = it2;
        this.bm = bm2;
    }

    public String getTps() {
        return this.tps;
    }

    public void setTps(String tps2) {
        this.tps = tps2;
    }

    public String getRpm() {
        return this.rpm;
    }

    public void setRpm(String rpm2) {
        this.rpm = rpm2;
    }

    public String getIt() {
        return this.it;
    }

    public void setIt(String it2) {
        this.it = it2;
    }

    public String getBm() {
        return this.bm;
    }

    public void setBm(String bm2) {
        this.bm = bm2;
    }

    public String toString() {
        return this.tps + ";" + this.rpm + ";" + this.it + ";" + this.bm;
    }
}

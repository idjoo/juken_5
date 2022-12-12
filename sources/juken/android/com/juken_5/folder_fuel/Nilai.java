package juken.android.com.juken_5.folder_fuel;

public class Nilai {
    private Double nilai;
    private int posisi;
    private int status;

    public Nilai() {
    }

    public Nilai(int posisi2, Double nilai2, int status2) {
        this.posisi = posisi2;
        this.nilai = nilai2;
        this.status = status2;
    }

    public int getPosisi() {
        return this.posisi;
    }

    public void setPosisi(int posisi2) {
        this.posisi = posisi2;
    }

    public double getStatus() {
        return (double) this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public double getNilai() {
        return this.nilai.doubleValue();
    }

    public void setNilai(double nilai2) {
        this.nilai = Double.valueOf(nilai2);
    }
}

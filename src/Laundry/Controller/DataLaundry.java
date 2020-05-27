package Laundry.Controller;

public class DataLaundry {
    private String idCucian, jenisLaundry, durasiLaundry;
    private int hargaLaundry;

    public String getIdCucian(){return idCucian;}
    public String getJenisLaundry(){return jenisLaundry;}
    public int getHargaLaundry(){return hargaLaundry;}
    public String getDurasiLaundry() { return durasiLaundry; }

    public void setIdCucian(String vidCucian){idCucian = vidCucian;}
    public void setJenisLaundry(String vjenisLaundry) { jenisLaundry = vjenisLaundry; }
    public void setHargaLaundry(int vhargaLaundry) { hargaLaundry = vhargaLaundry; }
    public void setDurasiLaundry(String vdurasiLaundry) { durasiLaundry = vdurasiLaundry; }

}

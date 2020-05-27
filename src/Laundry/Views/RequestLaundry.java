package Laundry.Views;

import Laundry.Controller.DataLaundry;
import Laundry.Controller.Koneksi;
import Laundry.Controller.UserSession;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestLaundry extends JFrame {
    JFrame window = new JFrame("Request Laundry");
    // JLabel
    JLabel lBerat       = new JLabel("Berat");
    JLabel lkg          = new JLabel("Kg");
    JLabel lJenis       = new JLabel("Jenis Cucian");
    JLabel lDurasi      = new JLabel("Durasi");
    JLabel lHarga       = new JLabel("Harga PerKg");
    JLabel lTotal       = new JLabel("Total");
    // JTextFields
    JTextField fBerat   = new JTextField();
    JTextField fDurasi  = new JTextField();
    JTextField fHarga   = new JTextField();
    JTextField fTotal   = new JTextField();
    // JComboBox
    JComboBox<String> cUser     = new JComboBox<>();
    JComboBox<String> cJenis    = new JComboBox<>();
    DataLaundry jenis;
    List<DataLaundry>jeniss=new ArrayList<>();
    //Button
    JButton bLanjut     = new JButton("Lanjut");
    JButton bKembali    = new JButton("Kembali");

    Koneksi koneksi     = new Koneksi();
    ResultSet resultSet;
    Statement statement;
    NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp now = new Timestamp(System.currentTimeMillis());
    Boolean pesan = false;

    static String idCucian, durasi;
    static int harga;
    static float berat, total;

    public RequestLaundry(){
        initComponents();
        loadJenisCucian();
        initListeners();
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));

        window.add(lBerat);
            lBerat.setBounds(20, 20, 150, 20);
            lBerat.setForeground(new Color(255, 255, 255));
            window.add(lkg);
            lkg.setBounds(315, 20, 150, 20);
            lkg.setForeground(new Color(255, 255, 255));
            window.add(fBerat);
                fBerat.setBounds(130, 20, 170, 25);

        window.add(lJenis);
            lJenis.setBounds(20, 63, 150, 20);
            lJenis.setForeground(new Color(255, 255, 255));
            window.add(cJenis);
                cJenis.setBounds(130, 60, 170, 25);

        window.add(lDurasi);
            lDurasi.setBounds(20, 103, 150, 20);
            lDurasi.setForeground(new Color(255, 255, 255));
            window.add(fDurasi);
                fDurasi.setBounds(130, 100, 170, 25);
                fDurasi.setEditable(false);

        window.add(lHarga);
            lHarga.setBounds(20, 143, 150, 20);
            lHarga.setForeground(new Color(255, 255, 255));
            window.add(fHarga);
                fHarga.setBounds(130, 140, 170, 25);
                fHarga.setEditable(false);

        window.add(lTotal);
            lTotal.setBounds(20, 183, 150, 20);
            lTotal.setForeground(new Color(255, 255, 255));
            window.add(fTotal);
                fTotal.setBounds(130, 180, 170, 25);
                fTotal.setEditable(false);

        window.add(bLanjut);
            bLanjut.setBounds(130,220,170,25);
        window.add(bKembali);
            bKembali.setBounds(20,220,90,25);

        window.setLayout(null);
        window.setSize(365, 300);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    private List<DataLaundry> getAllJenis(){
        try {
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM cucian";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                jenis = new DataLaundry();
                jenis.setIdCucian(resultSet.getString("id_cucian"));
                jenis.setJenisLaundry(resultSet.getString("jenis_cucian"));
                jenis.setHargaLaundry(resultSet.getInt("harga_cucian"));
                jenis.setDurasiLaundry(resultSet.getString("durasi_cucian"));
                jeniss.add(jenis);
            }
        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
        return jeniss;
    }

    private void loadJenisCucian(){
        cJenis.removeAllItems();
        List<DataLaundry> jeniss = getAllJenis();
        for(DataLaundry data:jeniss){
            cJenis.addItem(data.getJenisLaundry().toString());
        }
    }

    public void initListeners(){
        cJenis.addActionListener(e -> {
            try {
                idCucian = jeniss.get(cJenis.getSelectedIndex()).getIdCucian();
                durasi   = jeniss.get(cJenis.getSelectedIndex()).getDurasiLaundry();
                fDurasi.setText(durasi);
                harga    = jeniss.get(cJenis.getSelectedIndex()).getHargaLaundry();
                fHarga.setText("Rp."+ nf.format(harga));
                berat    = Float.parseFloat(fBerat.getText());
                total    = berat*harga;
                fTotal.setText("Rp."+ nf.format(total));
            }catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null, "Berat Harus Diisi!");
                fDurasi.setText("");
                fHarga.setText("");
                fTotal.setText("");
            }
        });

        bLanjut.addActionListener(e -> {
            insertLaundry();
            if (pesan = true) {
                window.setVisible(false);
                JOptionPane.showMessageDialog(null, "Berhasil Memesan!");
                window.setVisible(false);
                new MenuUtama();
            } else {
                JOptionPane.showMessageDialog(null, "gagal Memesan!");
            }
        });

        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new MenuUtama();
        });
    }

    private void insertLaundry(){
        DataLaundry jenis = new DataLaundry();
        String kode;
        try{
                statement = koneksi.getConnection().createStatement();
                String sqlMax = "SELECT max(id_pemesanan) as max_kode FROM pemesanan";
                resultSet = statement.executeQuery(sqlMax);
                if (resultSet.next()) {
                    String kode_pmsn = resultSet.getString("max_kode");
                    if(kode_pmsn == null ){
                        kode = "PMSN-001";
                    }else {
                        String kode_pmsn_bersih = kode_pmsn.substring(5, 8);
                        int no_urut = Integer.parseInt(kode_pmsn_bersih);
                        no_urut += 1;

                        String pmsn = "PMSN-";
                        kode = pmsn + String.format("%03d", no_urut);
                    }
                    statement.executeUpdate("INSERT INTO pemesanan VALUES('" + kode + "','" + UserSession.getId_user() + "','" + idCucian + "','"+ berat +"','" + total + "','"+ time.format(now) +"','"+time.format(now)+"','SEDANG DIPROSES')");
                    pesan = true;
                }

            resultSet.close();

        }catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(rootPane, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(rootPane, "Driver tidak ditemukan !!");
        }catch (NumberFormatException e){
            System.err.println("error"+e);
        }
    }

}

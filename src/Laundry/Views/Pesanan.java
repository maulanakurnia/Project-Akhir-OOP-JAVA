package Laundry.Views;

import Laundry.Controller.DataLaundry;
import Laundry.Controller.Koneksi;
import Laundry.Controller.UserSession;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pesanan extends JFrame{
    JFrame window 	= new JFrame();
    // Component to connect and access Database
    Koneksi koneksi = new Koneksi();
    ResultSet resultSet;
    Statement statement;

    // Set Component Table datas is data and kolom is Coloumn
    String[][] datas = new String[500][7];
    String[] kolom = new String[]{"User", "Kode", "Jenis Cuci", "tanggal serah", "berat", "total", "status"};

    // Component Table
    JTable tTable;
    JScrollPane pane;
    TableModel model;
    TableRowSorter sorter;

    // Label
    JLabel lId          = new JLabel("Kode");
    JLabel lJenis       = new JLabel("Jenis");
    JLabel lBerat       = new JLabel("Berat");
    JLabel lStatus      = new JLabel("Status");
    JLabel lCari        = new JLabel("Cari Data");
    // Text Field
    JTextField fIdPesan = new JTextField();
    JTextField fId      = new JTextField();
    JTextField fBerat   = new JTextField();
    JTextField fCari    = new JTextField();
    // Button
    JButton bUpdate 	= new JButton("Ubah");
    JButton bHapus   	= new JButton("Hapus");
    JButton bKembali 	= new JButton("Kembali");
    JButton bLihat      = new JButton("Lihat Progres");
    //ComboBox
    JComboBox<Object> cJenis   = new JComboBox<>();
    JComboBox<Object> cStatus  = new JComboBox<>();
    DataLaundry jenis;
    List<DataLaundry> jeniss= new ArrayList<>();

    // Component Time
    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp now = new Timestamp(System.currentTimeMillis());

    // Constructor
    public Pesanan(){
        // Set Permission
        if(UserSession.getId_user() == null){
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }
        else {
            UserSession.setId_user("1");
            loadData();
            loadJenisCucian();
            initComponents();
            initListeners();
        }
    }

    // Set Component Swing
    private void initComponents(){
        // Set Table Component
        model = new DefaultTableModel(datas,kolom);
        sorter= new TableRowSorter<>(model);
        tTable= new JTable(model);
        tTable.setRowSorter(sorter);
        pane = new JScrollPane(tTable);
        tTable.setBackground(new Color(247, 252, 255));
        TableColumnModel columnModel = tTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(30);
        columnModel.getColumn(4).setPreferredWidth(10);

        // add Component to panel and Set Button
        window.add(lId);
        window.add(fIdPesan);
        window.add(lJenis);
        window.add(lBerat);
        window.add(lStatus);
        window.add(cStatus);
        window.add(bUpdate);
            bUpdate.setForeground(new Color(255, 255, 255));
            bUpdate.setBackground(new Color(168, 168, 50));
        window.add(bKembali);
            bKembali.setForeground(new Color(255, 255, 255));
            bKembali.setBackground(new Color(82, 77, 64));
        window.add(bLihat);
            bLihat.setForeground(new Color(255, 255, 255));
            bLihat.setBackground(new Color(58, 133, 86));
        window.add(pane);
            pane.setBackground(new Color(247, 252, 255));

        // Set Table Component
        window.getContentPane().setBackground(new Color(28, 27, 27));
        window.setLayout(null);
        window.setSize(690, 590);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        // Set Component ADMIN
        if(UserSession.getId_user().equals("1")) {
            window.setTitle("Kelola Pesanan");
            lId.setBounds(20, 20, 150, 20);
            lId.setForeground(new Color(255, 255, 255));
            fIdPesan.setBounds(130, 20, 170, 25);
            fIdPesan.setEditable(false);

            lJenis.setBounds(20, 63, 150, 20);
            lJenis.setForeground(new Color(255, 255, 255));
            window.add(cJenis);
            cJenis.setBounds(130, 60, 170, 25);

            lBerat.setBounds(340, 23, 150, 20);
            lBerat.setForeground(new Color(255, 255, 255));
            window.add(fBerat);
            fBerat.setBounds(450, 20, 200, 25);

            lStatus.setBounds(340, 63, 150, 20);
            lStatus.setForeground(new Color(255, 255, 255));
            cStatus.setBounds(450, 60, 200, 25);
            cStatus.addItem("MENUNGGU DIKONFIRMASI");
            cStatus.addItem("SEDANG DIPROSES");
            cStatus.addItem("SEDANG DIPROSES (DICUCI)");
            cStatus.addItem("CUCIAN TELAH SELESAI");
            cStatus.addItem("TELAH DIAMBIL");

            window.add(lCari);
            lCari.setBounds(20,100,150,20);
            lCari.setForeground(new Color(255, 255, 255));
                window.add(fCari);
                fCari.setBounds(130,100,520,25);

            bUpdate.setBounds(185, 500, 140, 30);
            bHapus.setBounds(345, 500, 140, 30);
            bKembali.setBounds(510, 500, 140, 30);
            bLihat.setBounds(20,500,140,30);
            pane.setBounds(20, 140, 630, 340);

            window.add(bHapus);
            bHapus.setForeground(new Color(255, 255, 255));
            bHapus.setBackground(new Color(102, 55, 51));
        }else{ // Set Component USER
            window.setTitle("Daftar Pesanan");
            pane.setBounds(20, 20, 630, 340);
            bLihat.setBounds(20,400,190,30);
            bKembali.setBounds(220, 400, 140, 30);
        }
    }

    // Set EventHandling
    private void initListeners(){
        // Set Event Table, if mouse clicked show data on JTextField and JCombobox
        tTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int baris = tTable.rowAtPoint(e.getPoint());
                    String id = tTable.getValueAt(baris, 1).toString();
                    fIdPesan.setText(id);
                    String jenis = tTable.getValueAt(baris, 2).toString();
                    cStatus.setSelectedItem(jenis);
                    String berat = tTable.getValueAt(baris, 4).toString();
                    fBerat.setText(berat);
                    String status = tTable.getValueAt(baris, 6).toString();
                    cStatus.setSelectedItem(status);
                } catch (Exception ea) {
                    JOptionPane.showMessageDialog(null, "Mohon Maaf Data " + ea.getMessage());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        // Update Data
        bUpdate.addActionListener(e -> {
        String sql, sql2, check;
            try {
                statement = koneksi.getConnection().createStatement();
                String idCucian = jeniss.get(cJenis.getSelectedIndex()).getIdCucian();
                System.out.println(idCucian);
                check = "SELECT * FROM pemesanan WHERE id_pemesanan='"+fIdPesan.getText()+"' AND berat_laundry='"+fBerat.getText()+"' AND id_cucian='"+idCucian+"'";
                resultSet = statement.executeQuery(check);
                if(!resultSet.next()) {
                    float harga    = jeniss.get(cJenis.getSelectedIndex()).getHargaLaundry();
                    float berat    = Float.parseFloat(fBerat.getText());
                    float total    = berat*harga;
                    sql = "UPDATE pemesanan set id_cucian='" + idCucian + "',berat_laundry='" + fBerat.getText() + "',total='"+total+"',status='" + cStatus.getSelectedItem() + "' WHERE id_pemesanan='" + fIdPesan.getText() + "'";
                }else {
                    sql = "UPDATE pemesanan set id_cucian='" + idCucian + "',berat_laundry='" + fBerat.getText() + "',status='" + cStatus.getSelectedItem() + "' WHERE id_pemesanan='" + fIdPesan.getText() + "'";
                }
                System.out.println("ID: "+fIdPesan.getText()+"  STATUS: "+ cStatus.getSelectedItem());
                sql2 = "INSERT INTO riwayat_pesanan VALUES(default,'" + fIdPesan.getText() + "','"+ time.format(now) +"','" + cStatus.getSelectedItem() + "')";
                int disimpan = statement.executeUpdate(sql);
                int tambahriwayat = statement.executeUpdate(sql2);
                if (disimpan == 1 && tambahriwayat == 1) {
                    JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Informasi", JOptionPane.WARNING_MESSAGE);
                    statement.close();
                    window.setVisible(false);
                    new Pesanan();
                }

            } catch (SQLException sqlError) {
                JOptionPane.showMessageDialog(null, "Gagal Update Data! error : " + sqlError);
            } catch (ClassNotFoundException classError) {
                JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
            }
        });

        // Show Progress Pesananan
        bLihat.addActionListener(e -> {
            new tabelProgres(fIdPesan.getText());
        });
        // Delete Data
        bHapus.addActionListener(ae -> {
            int result = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data dengan Kode "+fIdPesan.getText()+"?", "INFO", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    if(!Objects.equals(cStatus.getSelectedItem(), "TELAH DIAMBIL")){
                        JOptionPane.showMessageDialog(null, "Tidak bisa menghapus pesanan yang belum selesai!");
                    }else {
                        statement = koneksi.getConnection().createStatement();
                        String sql = "DELETE FROM pemesanan WHERE id_pemesanan='" + fIdPesan.getText() + "'";
                        statement.execute(sql);
                        JOptionPane.showMessageDialog(null, "Berhasil Hapus Data!", "Informasi", JOptionPane.WARNING_MESSAGE);
                        statement.close();
                        window.setVisible(false);
                        new Pesanan();
                    }
                } catch (HeadlessException | SQLException | ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });

        // Back
        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new MenuUtama();
        });

        // Search Data with DocumentListener
        fCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            public void search(String str) {
                if (str.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(str));
                }
            }
        });
    }

    // Load Data on SQL and then import to JTable
    private void loadData(){
        String sql;
        try {
            statement = koneksi.getConnection().createStatement();
            if (UserSession.getId_user().equals("1")) {
                sql = "SELECT * FROM pemesanan a INNER JOIN cucian b ON a.id_cucian=b.id_cucian INNER JOIN user c ON a.id_user=c.id_user";

            } else {
                sql = "SELECT * FROM pemesanan a INNER JOIN cucian b ON a.id_cucian=b.id_cucian INNER JOIN user c ON a.id_user=c.id_user WHERE a.id_user='" + UserSession.getId_user() + "'";
            }
            resultSet = statement.executeQuery(sql);

            int row = 0;
            while (resultSet.next()){
                datas[row][0] = resultSet.getString("username");
                datas[row][1] = resultSet.getString("id_pemesanan");
                datas[row][2] = resultSet.getString("jenis_cucian");
                datas[row][3] = resultSet.getString("tanggal_request");
                datas[row][4] = resultSet.getString("berat_laundry");
                datas[row][5] = resultSet.getString("total");
                datas[row][6] = resultSet.getString("status");
                row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }

    // Get All Jenis with Arraylist (Save on Arraylist)
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
    // Load Jenis Cucian and then import to JCombobox
    private void loadJenisCucian(){
        cJenis.removeAllItems();
        List<DataLaundry> jeniss = getAllJenis();
        for(DataLaundry data:jeniss){
            cJenis.addItem(data.getJenisLaundry());
        }
    }
}

package Laundry.Views;

import Laundry.Controller.Koneksi;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class tabelProgres extends JFrame{
    String id;
    JFrame window 	        = new JFrame("Progres Pesanan");
    String[][] datas        = new String[100][4];
    String[] kolom          = {"#","ID Pemesanan", "tanggal","status"};
    JTable tTable           = new JTable(datas,kolom);
    JScrollPane scrollPane  = new JScrollPane(tTable);
    ResultSet resultSet;
    Statement statement;
    public tabelProgres(String vId){
        id = vId;
        initComponents();
        loadData();
    }
    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));
        TableColumnModel columnModel = tTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(5);
        columnModel.getColumn(1).setPreferredWidth(40);
        columnModel.getColumn(2).setPreferredWidth(60);
        columnModel.getColumn(3).setPreferredWidth(60);
        window.add(scrollPane,BorderLayout.CENTER);
        tTable.setEnabled(false);
        tTable.setFont(new Font("Arial", Font.BOLD,14));
        tTable.setRowHeight(30);
        scrollPane.setBounds(70, 70, 400, 400);

        window.setSize(510, 420);
        window.setVisible(true);
    }

    private void loadData(){
        Koneksi koneksi = new Koneksi();
        try{
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM riwayat_pesanan WHERE id_pemesanan = '"+ id +"'";
            resultSet = statement.executeQuery(sql);
            int row = 0;
            while (resultSet.next()){
                datas[row][0] = String.valueOf(row+1);
                datas[row][1] = resultSet.getString("id_pemesanan");
                datas[row][2] = resultSet.getString("tanggal_riwayat");
                datas[row][3] = resultSet.getString("status");
                row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }
}

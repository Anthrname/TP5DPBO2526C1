import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel tinggiValueLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JSlider tinggiSlider;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;

    private Database database;
    private int selectedIndex = -1;
    private String selectedId = null;

    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setTitle("Data Orang");
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
    }

    public PeopleMenu() {
        database = new Database();
        peopleTable.setModel(setTable());

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        String[] kategoriData = {"???", "Mahasiswa", "Dosen", "Staf", "Alumni"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        tinggiSlider.setMinimum(140);
        tinggiSlider.setMaximum(200);
        tinggiSlider.setValue(170);
        tinggiValueLabel.setText("170 cm");
        tinggiSlider.addChangeListener(e ->
                tinggiValueLabel.setText(tinggiSlider.getValue() + " cm"));

        deleteButton.setVisible(false);

        addUpdateButton.addActionListener(e -> {
            if (selectedIndex == -1) insertData();
            else updateData();
        });

        deleteButton.addActionListener(e -> {
            int pilihan = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin menghapus data ini?", "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (pilihan == JOptionPane.YES_OPTION) deleteData();
        });

        cancelButton.addActionListener(e -> clearForm());

        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex < 0) return;

                // kolom 0 = No, jadi data dimulai dari kolom 1
                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curTinggi = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                selectedId = curId;
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                tinggiSlider.setValue(Integer.parseInt(curTinggi));

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Tinggi (cm)"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            ResultSet rs = database.selectQuery("SELECT * FROM person ORDER BY id");
            int i = 1;
            while (rs.next()) {
                tmp.addRow(new Object[]{i++, rs.getString("id"), rs.getString("nama"),
                        rs.getString("tahun_lahir"), rs.getString("kategori"),
                        rs.getString("tinggi")});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tmp;
    }

    private void showError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean validasi(String id, String nama, String tahun, String kategori) {
        if (id.isEmpty() || nama.isEmpty() || tahun.isEmpty() || kategori.equals("???")) {
            showError("Semua field harus diisi!");
            return false;
        }
        return true;
    }

    private boolean isIdExists(String id) {
        try {
            ResultSet rs = database.selectQuery("SELECT 1 FROM person WHERE id = ?", id);
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertData() {
        try {
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunText = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            if (!validasi(id, nama, tahunText, kategori)) return;

            int tahun = Integer.parseInt(tahunText);
            int tinggi = tinggiSlider.getValue();

            if (isIdExists(id)) {
                showError("ID '" + id + "' sudah digunakan!");
                return;
            }

            database.insertUpdateDeleteQuery(
                    "INSERT INTO person (id, nama, tahun_lahir, kategori, tinggi) VALUES (?, ?, ?, ?, ?)",
                    id, nama, tahun, kategori, tinggi);

            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            showError("Tahun lahir harus berupa angka!");
        }
    }

    public void updateData() {
        try {
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunText = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            if (!validasi(id, nama, tahunText, kategori)) return;

            int tahun = Integer.parseInt(tahunText);
            int tinggi = tinggiSlider.getValue();

            if (!id.equals(selectedId) && isIdExists(id)) {
                showError("ID '" + id + "' sudah digunakan!");
                return;
            }

            database.insertUpdateDeleteQuery(
                    "UPDATE person SET id = ?, nama = ?, tahun_lahir = ?, kategori = ?, tinggi = ? WHERE id = ?",
                    id, nama, tahun, kategori, tinggi, selectedId);

            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            showError("Tahun lahir harus berupa angka!");
        }
    }

    public void deleteData() {
        database.insertUpdateDeleteQuery("DELETE FROM person WHERE id = ?", selectedId);
        peopleTable.setModel(setTable());
        clearForm();

        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        tinggiSlider.setValue(170);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        peopleTable.clearSelection();
        selectedIndex = -1;
        selectedId = null;
    }
}

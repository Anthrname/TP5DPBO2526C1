# TP5 DPBO - PeopleData (Java Swing GUI & Database MySQL)

Aplikasi CRUD data orang berbasis Java Swing (IntelliJ IDEA, Swing UI Designer) yang terintegrasi dengan database MySQL (`db_people`).

## 📌 JANJI
> Saya **Najib Nurohman** dengan NIM **2509653** mengerjakan **Tugas Praktikum 5** dalam mata kuliah **Desain Pemrograman Berorientasi Objek (DPBO)** untuk keberkahan-Nya maka saya tidak akan melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain & Fungsi Program

| Berkas | Fungsi |
|---|---|
| `PeopleData/Person.java` | Class model: `id`, `nama`, `tahunLahir`, `kategori`, `tinggi` beserta getter/setter |
| `PeopleData/Database.java` | Class koneksi dan eksekusi query basis data MySQL (SELECT, INSERT, UPDATE, DELETE) |
| `PeopleData/PeopleMenu.java` | Logika GUI, event listener, validasi input, dan operasi CRUD ke database |
| `PeopleData/PeopleMenu.form` | Tata letak form GUI (Swing UI Designer) |
| `db_people.sql` | Skrip pembuatan database, struktur tabel `person`, dan data awal |
| `lib/mysql-connector-j-26.7.0.jar` | JDBC Driver untuk koneksi Java ke MySQL |

**Atribut:** ID, Nama, Tahun Lahir, Kategori (JComboBox), dan **Tinggi Badan (JSlider, 140-200 cm)** sebagai atribut komponen non-JTextField.

**Komponen form:** `idField`, `namaField`, `tahunLahirField`, `kategoriComboBox`, `tinggiSlider`, `tinggiValueLabel`, `addUpdateButton`, `cancelButton`, `deleteButton`, `peopleTable`.

## Alur Program

1. **Inisialisasi & Read:** Saat aplikasi dijalankan, objek `Database` menginisialisasi koneksi JDBC ke MySQL (`db_people`). Method `setTable()` menjalankan query `SELECT * FROM person ORDER BY id` dan menampilkan hasilnya ke `peopleTable`.
2. **Create:** Pengguna mengisi formulir lalu menekan tombol **Add**. Program memvalidasi seluruh field tidak boleh kosong, tahun lahir berupa angka, dan ID belum pernah terdaftar sebelumnya. Data baru disimpan ke basis data menggunakan query `INSERT`, tabel diperbarui, dan form dibersihkan.
3. **Pilih Baris (Selection):** Mengklik salah satu baris tabel akan memuat data baris tersebut ke form input, tombol aksi berganti teks menjadi **Update**, dan tombol **Delete** ditampilkan.
4. **Update:** Pengguna memodifikasi data pada form lalu menekan **Update**. Program memastikan data valid dan ID tidak bertabrakan dengan ID lain, kemudian mengeksekusi query `UPDATE` ke database dan memuat ulang data tabel.
5. **Delete:** Menekan tombol **Delete** memunculkan dialog konfirmasi. Jika dikonfirmasi **Yes**, data dihapus dari database menggunakan query `DELETE` berdasarkan ID terpilih.
6. **Cancel:** Mengosongkan form input, membatalkan seleksi tabel, menyembunyikan tombol Delete, dan mengembalikan tombol aksi ke mode **Add**.

## Konfigurasi Basis Data

1. Buat database dan tabel menggunakan file `db_people.sql` pada MySQL/phpMyAdmin:
   ```sql
   source db_people.sql;
   ```
2. Konfigurasi default koneksi JDBC di `PeopleData/Database.java`:
   - Host: `127.0.0.1:3306`
   - Database: `db_people`
   - User: `root`
   - Password: `""` (kosong)

## Dokumentasi Menjalankan Program

![Demo Program](Dokumentasi/demo.gif)

- **Video Dokumentasi:** [Screen Recording 2026-10-11 012953.mp4](Dokumentasi/Screen%20Recording%202026-10-11%20012953.mp4)

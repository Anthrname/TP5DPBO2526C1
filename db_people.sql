-- Database: db_people
CREATE DATABASE IF NOT EXISTS `db_people`;
USE `db_people`;

-- Table structure for table `person`
CREATE TABLE IF NOT EXISTS `person` (
  `id` varchar(50) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `tahun_lahir` int(11) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  `tinggi` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `person`
INSERT INTO `person` (`id`, `nama`, `tahun_lahir`, `kategori`, `tinggi`) VALUES
('P001', 'Budi Santoso', 2003, 'Mahasiswa', 172),
('P002', 'Siti Aminah', 2002, 'Mahasiswa', 160),
('P003', 'Rian Pratama', 1985, 'Dosen', 175),
('P004', 'Dewi Lestari', 1990, 'Staf', 163),
('P005', 'Ahmad Fauzi', 1998, 'Alumni', 170),
('P006', 'Nadia Putri', 1994, 'Staf', 158)
ON DUPLICATE KEY UPDATE `nama`=VALUES(`nama`);

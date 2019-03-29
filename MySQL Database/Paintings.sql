-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 29, 2019 at 02:03 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Paintings`
--

-- --------------------------------------------------------

--
-- Table structure for table `paintings`
--

CREATE TABLE `paintings` (
  `painting_id` int(50) NOT NULL,
  `painting_name` varchar(200) NOT NULL,
  `painting_price` int(200) NOT NULL,
  `painting_url` varchar(200) NOT NULL,
  `painting_description` varchar(200) NOT NULL,
  `painting_artist` int(42) NOT NULL,
  `Painting_category` int(3) NOT NULL,
  `Painting_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Verified` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paintings`
--

INSERT INTO `paintings` (`painting_id`, `painting_name`, `painting_price`, `painting_url`, `painting_description`, `painting_artist`, `Painting_category`, `Painting_date`, `Verified`) VALUES
(2, 'Aqua Victory', 319, 'https://cdn.shopify.com/s/files/1/2126/2505/products/hh-3_1296x.jpg?v=1549788472', 'Description\r\nMasterful play of dark tones and predominant blue turns this minimal abstract into a real treat for the eyes. Dynamic and vivid blue and white lines are making it stand out and add artist', 0, 0, '2019-03-20 13:50:11', 0),
(3, 'Aqua Victory', 319, 'https://cdn.shopify.com/s/files/1/2126/2505/products/hh-3_1296x.jpg?v=1549788472', 'Description\r\nMasterful play of dark tones and predominant blue turns this minimal abstract into a real treat for the eyes. Dynamic and vivid blue and white lines are making it stand out and add artist', 0, 0, '2019-03-20 13:50:11', 0);

-- --------------------------------------------------------

--
-- Table structure for table `purchase`
--

CREATE TABLE `purchase` (
  `purchase_id` int(50) NOT NULL,
  `paintings_id` int(50) NOT NULL,
  `status` varchar(100) NOT NULL,
  `user` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchase`
--

INSERT INTO `purchase` (`purchase_id`, `paintings_id`, `status`, `user`) VALUES
(1, 0, 'Shipped', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(50) NOT NULL,
  `username` varchar(250) NOT NULL,
  `name` varchar(42) NOT NULL,
  `Email` varchar(42) NOT NULL,
  `Address` varchar(150) NOT NULL,
  `password` varchar(100) NOT NULL,
  `usertype` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `name`, `Email`, `Address`, `password`, `usertype`) VALUES
(1, 'admin', 'admin', 'admin@admin.com', '22st, Baushar ', 'D033E22AE348AEB5660FC2140AEC35850C4DA997', 0),
(4, 'admin', 'admin', 'dr.mm94@gmail.com', '22st', 'd033e22ae348aeb5660fc2140aec35850c4da997', 0),
(8, 'Challo', 'Challo', 'Challo@gmail.com', 'Challo town', 'b5b2f856314e7ef04003beb5a3ea03cfecf712cf', 2),
(9, 'Mohammed', 'MohammedAlharthy', 'test@test.com', '22st', '6e67cd66855bbdf6c4f4501a8252cb25963007ff', 0),
(10, 'Mohammed1', 'MohammedAlharthy', 'test1@test.com', '22st', '6e67cd66855bbdf6c4f4501a8252cb25963007ff', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `paintings`
--
ALTER TABLE `paintings`
  ADD PRIMARY KEY (`painting_id`);

--
-- Indexes for table `purchase`
--
ALTER TABLE `purchase`
  ADD PRIMARY KEY (`purchase_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `paintings`
--
ALTER TABLE `paintings`
  MODIFY `painting_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `purchase`
--
ALTER TABLE `purchase`
  MODIFY `purchase_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

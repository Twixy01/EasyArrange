-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2026. Ápr 23. 19:02
-- Kiszolgáló verziója: 10.4.32-MariaDB
-- PHP verzió: 8.2.12

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `easyarrange`
--
CREATE DATABASE IF NOT EXISTS `easyarrange` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci;
USE `easyarrange`;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `booking`
--

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
  `booking_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `service_id` bigint(20) NOT NULL,
  `status` enum('BOOKED','CANCELLED','COMPLETED','NO_SHOW') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `booking`
--

INSERT INTO `booking` (`booking_id`, `staff_id`, `customer_id`, `start_datetime`, `end_datetime`, `service_id`, `status`) VALUES
(13, 1, 5, '2026-04-02 09:00:00', '2026-04-02 09:20:00', 10, 'COMPLETED'),
(14, 5, 6, '2026-04-02 10:00:00', '2026-04-02 10:25:00', 2, 'COMPLETED'),
(15, 2, 7, '2026-04-02 13:30:00', '2026-04-02 14:00:00', 5, 'COMPLETED'),
(16, 4, 9, '2026-04-02 14:30:00', '2026-04-02 15:15:00', 9, 'BOOKED'),
(17, 3, 5, '2026-04-03 09:00:00', '2026-04-03 10:00:00', 13, 'BOOKED'),
(18, 1, 6, '2026-04-03 10:30:00', '2026-04-03 12:00:00', 3, 'COMPLETED'),
(19, 2, 7, '2026-04-03 14:00:00', '2026-04-03 14:40:00', 1, 'COMPLETED'),
(20, 5, 9, '2026-04-03 15:00:00', '2026-04-03 15:20:00', 10, 'CANCELLED'),
(21, 4, 5, '2026-04-04 09:00:00', '2026-04-04 09:25:00', 2, 'BOOKED'),
(22, 2, 6, '2026-04-04 11:30:00', '2026-04-04 12:15:00', 9, 'COMPLETED'),
(23, 5, 7, '2026-04-04 12:30:00', '2026-04-04 13:00:00', 5, 'COMPLETED'),
(24, 1, 9, '2026-04-05 10:00:00', '2026-04-05 11:30:00', 3, 'BOOKED'),
(25, 3, 5, '2026-04-05 12:00:00', '2026-04-05 13:00:00', 14, 'BOOKED'),
(36, 3, 3, '2026-04-07 09:00:00', '2026-04-07 10:00:00', 14, 'COMPLETED'),
(37, 3, 3, '2026-04-07 10:00:00', '2026-04-07 11:30:00', 3, 'COMPLETED'),
(38, 2, 3, '2026-04-08 09:00:00', '2026-04-08 09:40:00', 1, 'CANCELLED'),
(55, 5, 3, '2026-04-21 09:00:00', '2026-04-21 09:25:00', 2, 'BOOKED'),
(56, 3, 3, '2026-04-26 12:30:00', '2026-04-26 14:00:00', 3, 'BOOKED');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `calendar_block`
--

DROP TABLE IF EXISTS `calendar_block`;
CREATE TABLE `calendar_block` (
  `calendar_block_id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `staff_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `calendar_block`
--

INSERT INTO `calendar_block` (`calendar_block_id`, `title`, `start_datetime`, `end_datetime`, `staff_id`) VALUES
(19, 'Time off', '2026-04-20 00:00:00', '2026-04-21 00:00:00', 4),
(22, 'Time off', '2026-04-21 00:00:00', '2026-04-24 00:00:00', 4);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `role`
--

INSERT INTO `role` (`role_id`, `name`) VALUES
(1, 'ADMIN'),
(2, 'CUSTOMER'),
(3, 'STAFF');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `service_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `service`
--

INSERT INTO `service` (`service_id`, `name`, `price`, `duration`, `description`, `image`) VALUES
(1, 'Haircut', 6500, 40, 'Precision haircut tailored to your style.', 'https://images.unsplash.com/photo-1622286342621-4bd786c2447c?auto=format&fit=crop&w=1200&q=80'),
(2, 'Beard trimming', 4500, 25, 'Sharp beard shaping and edge cleanup.', 'https://images.unsplash.com/photo-1517832606299-7ae9b720a186?auto=format&fit=crop&w=1200&q=80'),
(3, 'Hair dyeing', 12000, 90, 'Modern coloring with expert consultation.', 'https://images.unsplash.com/photo-1617391654484-2894196c2cc9?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(4, 'Hair washing', 2000, 10, 'Relaxing wash with premium salon products.', 'https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=1200&q=80'),
(5, 'Kid Haircut', 5000, 30, 'Gentle, kid-friendly haircut tailored to your child’s style in a calm and comfortable environment.', 'https://images.unsplash.com/photo-1704072650662-76df3af134a7?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(7, 'Beard Shaping & Line-up', 5000, 30, 'Defined beard contours with sharp, clean lines.', 'https://images.unsplash.com/photo-1747352690432-7c90bf17ede1?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(8, 'Full Beard Grooming', 6500, 40, 'Complete beard care with trim, shaping, and nourishing finish.', 'https://images.unsplash.com/photo-1654097803253-d481b6751f29?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(9, 'Fade / Skin Fade', 7000, 45, 'Clean fade blended with precision and modern styling.', 'https://images.unsplash.com/photo-1621605815971-fbc98d665033?auto=format&fit=crop&w=1200&q=80'),
(10, 'Hair Styling', 4000, 20, 'Professional styling and finishing for a polished look.', 'https://images.unsplash.com/photo-1582095133179-bfd08e2fc6b3?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(11, 'Hair Bleaching', 14000, 100, 'Professional lightening process for bold color results.', 'https://images.unsplash.com/photo-1587225438173-701d7edc94f9?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(12, 'Manicure', 6000, 45, 'Clean nail shaping and hand care with an elegant finish.', 'https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=1200&q=80'),
(13, 'Pedicure', 7500, 60, 'Foot and nail care treatment for comfort and appearance.', 'https://images.unsplash.com/photo-1519415510236-718bdfcd89c8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
(14, 'Facial Treatment', 9000, 60, 'Refreshing facial care for cleaner, healthier-looking skin.', 'https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `shift`
--

DROP TABLE IF EXISTS `shift`;
CREATE TABLE `shift` (
  `shift_id` bigint(20) NOT NULL,
  `day` enum('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
  `start_shift` time NOT NULL,
  `end_shift` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `shift`
--

INSERT INTO `shift` (`shift_id`, `day`, `start_shift`, `end_shift`) VALUES
(16, 'MONDAY', '07:00:00', '10:00:00'),
(20, 'MONDAY', '07:00:00', '12:00:00'),
(22, 'MONDAY', '08:00:00', '10:00:00'),
(1, 'MONDAY', '09:00:00', '18:00:00'),
(19, 'MONDAY', '10:00:00', '11:00:00'),
(2, 'TUESDAY', '09:00:00', '18:00:00'),
(18, 'TUESDAY', '10:00:00', '18:00:00'),
(21, 'TUESDAY', '10:00:00', '23:00:00'),
(3, 'WEDNESDAY', '09:00:00', '18:00:00'),
(4, 'THURSDAY', '09:00:00', '18:00:00'),
(5, 'FRIDAY', '09:00:00', '18:00:00'),
(6, 'SATURDAY', '09:00:00', '14:00:00'),
(23, 'SATURDAY', '09:00:00', '18:00:00'),
(7, 'SUNDAY', '10:00:00', '14:00:00');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `staff_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `bio` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `staff`
--

INSERT INTO `staff` (`staff_id`, `user_id`, `title`, `bio`) VALUES
(1, 4, 'Color Specialist', 'Balayage, highlights, hair coloring, washing, and polished styling.'),
(2, 8, 'Senior Hair Stylist', 'Precision cuts, kid haircuts, fades, styling, washing, and modern color work.'),
(3, 10, 'Nail Technician', 'Manicure, pedicure, nail care, and refreshing facial treatments.'),
(4, 3, 'Master Barber', 'Haircuts, fades, beard trimming, beard shaping, and premium grooming.'),
(5, 6, 'Junior Stylist', 'Fresh cuts, kid haircuts, beard trims, washing, and stylish finishing.');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `staff_service`
--

DROP TABLE IF EXISTS `staff_service`;
CREATE TABLE `staff_service` (
  `staff_id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `staff_service`
--

INSERT INTO `staff_service` (`staff_id`, `service_id`) VALUES
(1, 1),
(1, 3),
(1, 4),
(1, 10),
(1, 11),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 9),
(2, 10),
(3, 3),
(3, 12),
(3, 13),
(3, 14),
(4, 1),
(4, 2),
(4, 4),
(4, 7),
(4, 8),
(4, 9),
(5, 1),
(5, 2),
(5, 4),
(5, 5),
(5, 10);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `staff_shift`
--

DROP TABLE IF EXISTS `staff_shift`;
CREATE TABLE `staff_shift` (
  `staff_id` bigint(20) NOT NULL,
  `shift_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `staff_shift`
--

INSERT INTO `staff_shift` (`staff_id`, `shift_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 7),
(4, 3),
(4, 4),
(4, 5),
(4, 7),
(4, 19),
(4, 21),
(4, 23),
(5, 1),
(5, 2),
(5, 3),
(5, 4),
(5, 5),
(5, 6),
(5, 7);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone_number` varchar(12) NOT NULL,
  `profile_picture` text DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `user`
--

INSERT INTO `user` (`user_id`, `name`, `email`, `phone_number`, `profile_picture`, `password`, `role_id`) VALUES
(3, 'Peter', 'peter@gmail.com', '+36301231234', 'https://images.unsplash.com/photo-1654110455429-cf322b40a906?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3Dhttps://images.unsplash.com/photo-1654110455429-cf322b40a906?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', '$2a$10$9mwbhK7NknzqZa.JqvTehOvIWC9IdOFGaVcog6t95f0ZUaMQ.aJsS', 1),
(4, 'Milan', 'milan@gmail.com', '+36301234321', 'https://plus.unsplash.com/premium_photo-1689977968861-9c91dbb16049?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', '$2a$10$hDowIUWT9wH7Ijdr78/nnuv/Bc/arzyDJvATxdFfq.IDPXojUNS/i', 3),
(5, 'Elek', 'elek@gmail.com', '+36301231243', NULL, '$2a$10$muUAaM9HS6Aq.VNJBnrJaeruHzW24hgYlBm2c1SuR20j/71RnfNfe', 2),
(6, 'Anna', 'anna@gmail.com', '+36301232134', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', '$2a$10$np7X0HLTmdELFoOVA/.Df.pliYehAotzPcW5tKmyprPKQPBvkVp5i', 3),
(7, 'Bence', 'bence@gmail.com', '+36301232143', NULL, '$2a$10$rZggb8AKCS3F36najikyp.2Dw5CAIqy6dzMAkkeE.VZ0DESkTwV76', 2),
(8, 'Kata', 'kata@gmail.com', '+36201231234', 'https://plus.unsplash.com/premium_photo-1688572454849-4348982edf7d?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', '$2a$10$6iem3vobGKAV544xacyY0u1yjYMJroz0oJYyB7UrUAa2GMTKTzcm2', 3),
(9, 'David', 'david@gmail.com', '+36203211234', NULL, '$2a$10$gKDCShf2gjGAgqbzBRowyOVs2LxesOhYA/htp1uQ/zMZuawo/8U4a', 2),
(10, 'Sofia', 'sofia@gmail.com', '+36301112222', 'https://plus.unsplash.com/premium_photo-1689551670902-19b441a6afde?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', '$2a$10$OFCvSwISm1wpKNarsMku1eKM4t/9nDEZbhMMFGw5/liQ0LdYNpN6W', 3),
(15, 'Dalma12', 'dalma@gmail.com', '+36302221234', 'https://m.media-amazon.com/images/M/MV5BMjM4MzE0MGItY2U4OS00MTU5LTgwNWUtYzMxZjMzMTQ5Yjg1XkEyXkFqcGc@._V1_.jpg', '$2a$10$ExlJpH3oxzZGNrmlEB7jN.YNaslYQQQMx.G9ubVglPb7vIHl5gU66', 2);

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD UNIQUE KEY `uq_booking_staff_start_end` (`staff_id`,`start_datetime`,`end_datetime`),
  ADD KEY `fk_booking_customer` (`customer_id`),
  ADD KEY `fk_booking_service` (`service_id`);

--
-- A tábla indexei `calendar_block`
--
ALTER TABLE `calendar_block`
  ADD PRIMARY KEY (`calendar_block_id`),
  ADD UNIQUE KEY `staff_start_end` (`staff_id`,`start_datetime`,`end_datetime`);

--
-- A tábla indexei `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- A tábla indexei `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`service_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- A tábla indexei `shift`
--
ALTER TABLE `shift`
  ADD PRIMARY KEY (`shift_id`),
  ADD UNIQUE KEY `uk_shift_unique` (`day`,`start_shift`,`end_shift`);

--
-- A tábla indexei `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staff_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- A tábla indexei `staff_service`
--
ALTER TABLE `staff_service`
  ADD PRIMARY KEY (`staff_id`,`service_id`),
  ADD KEY `fk_staff_service_service` (`service_id`);

--
-- A tábla indexei `staff_shift`
--
ALTER TABLE `staff_shift`
  ADD PRIMARY KEY (`staff_id`,`shift_id`),
  ADD KEY `fk_staff_shift_shift` (`shift_id`);

--
-- A tábla indexei `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_user_role` (`role_id`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT a táblához `calendar_block`
--
ALTER TABLE `calendar_block`
  MODIFY `calendar_block_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT a táblához `role`
--
ALTER TABLE `role`
  MODIFY `role_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT a táblához `service`
--
ALTER TABLE `service`
  MODIFY `service_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT a táblához `shift`
--
ALTER TABLE `shift`
  MODIFY `shift_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT a táblához `staff`
--
ALTER TABLE `staff`
  MODIFY `staff_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT a táblához `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

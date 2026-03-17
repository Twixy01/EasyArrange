-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2026. Már 17. 12:06
-- Kiszolgáló verziója: 10.4.32-MariaDB
-- PHP verzió: 8.2.12

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
  `status` enum('BOOKED','CANCELLED','COMPLETED','NO-SHOW') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `booking`
--

INSERT INTO `booking` (`booking_id`, `staff_id`, `customer_id`, `start_datetime`, `end_datetime`, `service_id`, `status`) VALUES
(1, 1, 5, '2026-03-18 09:00:00', '2026-03-18 09:40:00', 1, 'BOOKED'),
(2, 2, 6, '2026-03-19 11:00:00', '2026-03-19 11:25:00', 2, 'COMPLETED'),
(3, 3, 7, '2026-03-20 15:00:00', '2026-03-20 16:30:00', 3, 'BOOKED'),
(4, 4, 9, '2026-03-21 10:00:00', '2026-03-21 10:10:00', 4, 'CANCELLED'),
(5, 5, 5, '2026-03-22 12:00:00', '2026-03-22 12:30:00', 5, 'BOOKED');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `calendar_block`
--

DROP TABLE IF EXISTS `calendar_block`;
CREATE TABLE `calendar_block` (
  `calendar_block_id` bigint(20) NOT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `staff_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `calendar_block`
--

INSERT INTO `calendar_block` (`calendar_block_id`, `start_datetime`, `end_datetime`, `staff_id`) VALUES
(1, '2026-03-18 12:00:00', '2026-03-18 13:00:00', 1),
(2, '2026-03-19 10:00:00', '2026-03-19 11:00:00', 2),
(3, '2026-03-20 14:00:00', '2026-03-20 15:00:00', 3),
(4, '2026-03-21 09:00:00', '2026-03-21 10:00:00', 4),
(5, '2026-03-22 11:00:00', '2026-03-22 12:00:00', 5);

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
  `duration` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `service`
--

INSERT INTO `service` (`service_id`, `name`, `price`, `duration`) VALUES
(1, 'Hajvágás', 6500, 40),
(2, 'Szakáll vágás', 4500, 25),
(3, 'Hajfestés', 12000, 90),
(4, 'Hajmosás', 2000, 10),
(5, 'Gyerek hajvágás', 5000, 30);

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
(1, 'MONDAY', '08:00:00', '16:00:00'),
(2, 'TUESDAY', '08:00:00', '16:00:00'),
(3, 'WEDNESDAY', '10:00:00', '18:00:00'),
(4, 'THURSDAY', '10:00:00', '18:00:00'),
(5, 'FRIDAY', '08:00:00', '14:00:00');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `staff_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `staff`
--

INSERT INTO `staff` (`staff_id`, `user_id`) VALUES
(4, 3),
(1, 4),
(5, 6),
(2, 8),
(3, 10);

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
(2, 2),
(3, 3),
(4, 4),
(5, 5);

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
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `user`
--

INSERT INTO `user` (`user_id`, `name`, `email`, `profile_picture`, `password`, `role_id`) VALUES
(3, 'Peter', 'test@gmail.com', 'profile', '$2a$10$6iem3vobGKAV544xacyY0u1yjYMJroz0oJYyB7UrUAa2GMTKTzcm2', 1),
(4, 'Milan', 'alma@gmail.com', 'picture_link', '$2a$10$hDowIUWT9wH7Ijdr78/nnuv/Bc/arzyDJvATxdFfq.IDPXojUNS/i', 3),
(5, 'Nincsneve', 'noname@email.com', 'profile', '$2a$10$muUAaM9HS6Aq.VNJBnrJaeruHzW24hgYlBm2c1SuR20j/71RnfNfe', 2),
(6, 'Anna', 'anna@gmail.com', 'profile', '$2a$10$np7X0HLTmdELFoOVA/.Df.pliYehAotzPcW5tKmyprPKQPBvkVp5i', 2),
(7, 'Bence', 'bence@gmail.com', 'profile', '$2a$10$rZggb8AKCS3F36najikyp.2Dw5CAIqy6dzMAkkeE.VZ0DESkTwV76', 2),
(8, 'Kata', 'kata@gmail.com', 'profile', '$2a$10$6iem3vobGKAV544xacyY0u1yjYMJroz0oJYyB7UrUAa2GMTKTzcm2', 3),
(9, 'David', 'david@gmail.com', 'profile', '$2a$10$gKDCShf2gjGAgqbzBRowyOVs2LxesOhYA/htp1uQ/zMZuawo/8U4a', 2),
(10, 'Sofia', 'sofia@gmail.com', 'profile', '$2a$10$OFCvSwISm1wpKNarsMku1eKM4t/9nDEZbhMMFGw5/liQ0LdYNpN6W', 3);

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
  MODIFY `booking_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `calendar_block`
--
ALTER TABLE `calendar_block`
  MODIFY `calendar_block_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `role`
--
ALTER TABLE `role`
  MODIFY `role_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT a táblához `service`
--
ALTER TABLE `service`
  MODIFY `service_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `shift`
--
ALTER TABLE `shift`
  MODIFY `shift_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `staff`
--
ALTER TABLE `staff`
  MODIFY `staff_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `fk_booking_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`),
  ADD CONSTRAINT `fk_booking_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

--
-- Megkötések a táblához `calendar_block`
--
ALTER TABLE `calendar_block`
  ADD CONSTRAINT `fk_calendar_block_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE;

--
-- Megkötések a táblához `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `fk_staff_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Megkötések a táblához `staff_service`
--
ALTER TABLE `staff_service`
  ADD CONSTRAINT `fk_staff_service_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_staff_service_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Megkötések a táblához `staff_shift`
--
ALTER TABLE `staff_shift`
  ADD CONSTRAINT `fk_staff_shift_shift` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`shift_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_staff_shift_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Megkötések a táblához `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Entities.*;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        final String DB_CONN = "jdbc:mariadb://localhost:3306/easyarrange";
        final String DB_USER = "root";
        final String DB_PASS = "";
        try (Connection conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PASS)) {
            /*BookingDao<Booking> dao = new BookingDaoJdbc(conn);
            dao.findBookingsByStaffBetween(4,LocalDateTime.of(2026,2,3,13,0),
                    LocalDateTime.of(2026,2,26,13,0)).forEach(e->{
                System.out.println(e.toString());
            });*/
            CalendarBlockDao dao = new CalendarBlockDaoJdbc(conn);
            CalendarBlock block = dao.findCalendarBlockById(1);
            block.setStartDatetime(LocalDateTime.of(2026, 2, 3, 0, 0, 0));
            dao.update(block);

        } catch (SQLException e) {
            throw new RuntimeException("DB hiba történt", e);
        }
    }
}
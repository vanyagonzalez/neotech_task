package com.neotech.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class Utils {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:tcp://localhost:9092/~/neotech";
    private static String DB_USER = "sa";
    private static String DB_PASS = "";

    public static void executePrepareStatement(Consumer<Connection> consumer) {
        Connection conn = null;
        try {
            while (true) {
                try {
                    System.out.println("Try to connect to db...");
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    consumer.accept(conn);
                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        System.out.println(String.format("Something wrong happened with connection to db. %s", e1.getMessage()));
                    }
                }
            }

        } finally {
            try {
                if(conn!=null) conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}

package com.neotech.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.neotech.task.Utils.executePrepareStatement;

public class PrintService {

    public void print() {

        executePrepareStatement((conn) -> {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT ID, SAVED_TIME FROM SAVED_TIMES ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()) {
                    int id  = rs.getInt("ID");
                    Timestamp savedTime = rs.getTimestamp("SAVED_TIME");
                    System.out.println(String.format("Id: %s, Time: %s", id, savedTime));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if(rs!=null) rs.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                try{
                    if(ps!=null) ps.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

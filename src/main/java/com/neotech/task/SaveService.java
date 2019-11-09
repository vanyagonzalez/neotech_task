package com.neotech.task;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.neotech.task.Utils.executePrepareStatement;

public class SaveService {
    private static ConcurrentLinkedQueue<Date> datesQueue = new ConcurrentLinkedQueue<>();

    public void save() {
        new Thread(() -> {
            while(true) {
                try {
                    Date date = new Date();
                    boolean offer = datesQueue.offer(date);
                    System.out.println(String.format("Date %s %s added to the queue", date, offer ? "is" : "isn't"));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while(true) {
                try {
                    while (true) {
                        Date date = datesQueue.peek();
                        if (date == null) {
                            break;
                        }
                        executePrepareStatement((conn) -> {
                                    PreparedStatement ps = null;
                                    try {
                                        String sql = "INSERT INTO SAVED_TIMES(SAVED_TIME) VALUES (?)";
                                        try {
                                            ps = conn.prepareStatement(sql);
                                            ps.setTimestamp(1, new Timestamp(date.getTime()));
                                            ps.executeUpdate();
                                        } catch (SQLException e) {
                                            System.out.println(String.format("Couldn't insert date ... Cause: %s", e.getMessage()));
                                        }
                                    } finally {
                                        try{
                                            if(ps!=null) ps.close();
                                        } catch(SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                        datesQueue.poll();
                        System.out.println(String.format("Date %s is inserted to db", date));
                    }

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

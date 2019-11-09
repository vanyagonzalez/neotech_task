package com.neotech.task;

public class NeotechTaskApp {

    public static void main(String[] args) {
        if (args.length > 0 && "-p".equals(args[0])) {
            new PrintService().print();
        } else {
            new SaveService().save();
        }
    }






}

package com.example.exp;

public class DailyTotal {
    private final String date;
    private final double total;

    public DailyTotal(String date, double total) {
        this.date = date;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }
}

package com.example.exp;

public class Expense {
    private String name;
    private double amount;
    private String date;
    private byte[] image;

    public Expense(String name, double amount, String date, byte[] image) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.image = image;
    }

    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public byte[] getImage() { return image; }
}

package com.example.tradingjournal.DTO;

import java.util.Date;

public class OrderDTO {

    private int orderID;
    private String action;
    private Date dateTime;
    private int quantity;
    private double price;
    private double fee;

    public OrderDTO(int orderID, String action, Date dateTime, int quantity, double price, double fee) {
        this.orderID = orderID;
        this.action = action;
        this.dateTime = dateTime;
        this.quantity = quantity;
        this.price = price;
        this.fee = fee;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderId) {
        this.orderID = orderId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}

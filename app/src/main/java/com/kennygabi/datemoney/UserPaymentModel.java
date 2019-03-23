package com.kennygabi.datemoney;

import java.util.Date;

public class UserPaymentModel {

    private String name;
    private String amount;
    private String timestamp;
    //private Date date;

    public UserPaymentModel() {
    }

    public UserPaymentModel(String name, String amount, String timestamp) {
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
        //this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getAmount(){
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    //public Date getDate() { return date; }
}

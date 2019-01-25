package com.kennygabi.datemoney;

public class UserPaymentModel {

    private String name;
    private String amount;
    private String timestamp;

    public UserPaymentModel() {
    }

    public UserPaymentModel(String name, String amount, String timestamp) {
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
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
}

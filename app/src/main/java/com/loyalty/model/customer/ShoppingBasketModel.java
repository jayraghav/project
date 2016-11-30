package com.loyalty.model.customer;

/**
 * Created by anjalipandey on 27/8/16.
 */
public class ShoppingBasketModel {

    public String productname;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String amount;
}

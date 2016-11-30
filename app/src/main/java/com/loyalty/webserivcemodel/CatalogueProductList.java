package com.loyalty.webserivcemodel;

import java.io.Serializable;

/**
 * Created by jayendrapratapsingh on 23/9/16.
 */
public class CatalogueProductList implements Serializable{

    public String productId;
    public String itemCount;
    public String date;
    public String price;
    public String expDate;
    public String productName;
    public String productImage;
    public int count=0;
    public int total=0;
    public boolean check;
    public String description;
}

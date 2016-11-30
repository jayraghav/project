package com.loyalty.webserivcemodel;

/**
 * Created by jayendrapratapsingh on 10/9/16.
 */
public class CatalogueProductDetails implements Cloneable{

    public String points;
    public String dealId;
    public String creationDate;
    public boolean status;
    public boolean isHot;
    public String dealDesc;
    public String productId;
    public String dealImage;
    public String expDate;
    public String itemCount;
    public String date;
    public String price;
    public String productName;
    public boolean isLoyalty;
    public String productImage;
    public String offer="no";
    public String qty;
    public int count=0;

    public boolean isAnyChanges;
    //    public String isLoyalty;
    //    public String isHot;
    //    public String status;


    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

}
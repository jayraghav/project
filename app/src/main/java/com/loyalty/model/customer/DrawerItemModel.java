package com.loyalty.model.customer;

/**
 * Created by Shivangi on 25-08-2016.
 */
public class DrawerItemModel {
    public DrawerItemModel(String drawerItem) {
        this.item = drawerItem;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    String item;


}

package com.loyalty.webserivcemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayendrapratapsingh on 7/10/16.
 */
public class ShoppingBasketUpdateRequest {

    public String userId;
    public String storeId;
    public String businessId;
    public String total;
    public List<OrderItems> orderItems=new ArrayList<>();
    public List<Deal> dealsList=new ArrayList<>();
  //  public List<ProductList> productList=new ArrayList<>();

}

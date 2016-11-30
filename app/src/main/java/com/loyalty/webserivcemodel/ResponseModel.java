package com.loyalty.webserivcemodel;

import com.loyalty.model.customer.Questionnaire;

import java.util.List;

/**
 * Created by jayendrapratapsingh on 5/9/16.
 */
public class ResponseModel {

    public String responseMessage;
    public String responseCode;
    public UserInfo userInfo;
    public List<GetBusineesDetailsResponse> businessList;
    public BusinessInfo businessInfo;
    public CheckInBeaconsModel beaconsInfo;
    public List<HotDeals> hotDeals;
    public List<LoyaltyHistory> loyaltyHistory;
    public boolean distance;
    public boolean popularity;
    public boolean productService;
    public List<AllHotDeals> hotDealList;
    public List<CatalogueProductList> productList;
    public List<CatalogueDetailsList> dealsList;
    public  List<LoyaltyHotDealList>loyaltyHotDealList;
    public List<FilterModel>businessTypeList;
    public String userCheckedInId;
    public String orderId;
    public String token;
    public List<LoyaltyHistory> loyaltyList;
    public UserHistoryDetails historyDetails;
    public List<ItemListReceipt> itemList;
    public List<PendingOrdersList> PendingOrdersList;
    public PendingOrdersDetails pendingOrdersDetails;
    public List<YourLoyaltyDetails> yourLoyaltyDetails;
    public List<Questionnaire> questionnaire;
    public String referenceId;
    public String customerToken;
    public String cardId;
    public String totalRecords;
    public boolean businessType;
    public String businessSubCategory;
    public boolean isQuestionnaireExist;
}






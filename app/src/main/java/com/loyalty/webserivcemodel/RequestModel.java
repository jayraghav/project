package com.loyalty.webserivcemodel;

import java.util.List;

/**
 * Created by jayendrapratapsingh on 5/9/16.
 */
public class RequestModel {

   public  String referById;
    public String email;
    public String phone;
    public String password;
    public String firstName;
    public String lastName;
    public String countryCode;
    public String userName;
    public String profileImage;
    public String socialId;
    public String deviceKey;
    public String deviceToken;
    public String deviceType;
    public String socialLoginType;
    public String areaCode;
    public String userId;
    public String newPassword;
    public String dealId;
    public String storeId;
    public String userCheckedInId;
    private String otp;
    public String businessId;
    public String questionnaireId;
    public String total;
    public List<OrderItems> orderItems;
    public List<Deal> dealsList;
    public String orderId;
    public List<ProductList> productList;
    public List<UseranswerModel> answers;
    public String paymentAmount;
    public String nonce;
    public String cardId;


    public String customerToken;
    public String tableNo;


    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }
    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public List<UseranswerModel> getanswers() {
        return answers;
    }

    public void setAnswers(List<UseranswerModel> answers) {
        this.answers = answers;
    }

    public List<com.loyalty.webserivcemodel.Cart> getCart() {
        return Cart;
    }

    public void setCart(List<com.loyalty.webserivcemodel.Cart> cart) {
        Cart = cart;
    }

    private List<Cart>Cart;


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSocialLoginType() {
        return socialLoginType;
    }

    public void setSocialLoginType(String socialLoginType) {
        this.socialLoginType = socialLoginType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserCheckedInId() {
        return userCheckedInId;
    }

    public void setUserCheckedInId(String userCheckedInId) {
        this.userCheckedInId = userCheckedInId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTableNumber() {
        return tableNo;
    }

    public void setTableNumber(String tableNo) {
        this.tableNo = tableNo;
    }







}

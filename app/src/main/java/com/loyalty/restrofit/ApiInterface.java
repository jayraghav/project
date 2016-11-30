package com.loyalty.restrofit;


import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Interceptor;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.OkHttpClient;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Request;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Response;
import com.loyalty.model.customer.ShoppingBasketModel;
import com.loyalty.webserivcemodel.ChangePasswordModel;
import com.loyalty.webserivcemodel.EditProfileResponseModel;
import com.loyalty.webserivcemodel.FilterModel;
import com.loyalty.webserivcemodel.FilterRequestModel;
import com.loyalty.webserivcemodel.ForgetPasswordResponse;
import com.loyalty.webserivcemodel.GetBusineesDetailsResponse;
import com.loyalty.webserivcemodel.JsonResponseCardDetail;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseBusinessTypeModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.ShoppingBasketResponse;
import com.loyalty.webserivcemodel.ShoppingBasketUpdateRequest;
import com.loyalty.webserivcemodel.UserProfileData;
import com.loyalty.webserivcemodel.UserProfileModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface
{
    @POST("userSignup")
    Call<ResponseModel> userSignup(@Header("Authorization") String token, @Body RequestModel loginRequestModel);
    @POST("userLogin")
    Call<UserProfileData> userLogin(@Header("Authorization") String token,@Body RequestModel loginRequestModel);
    @GET("userProfile?")
    Call<UserProfileModel> userInfo(@Header("Authorization") String token,@Query("userId") int groupId);
    @POST("editProfile")
    Call<EditProfileResponseModel> editProfile(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userLogout")
    Call<ResponseModel> LogoutUser(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userForgotPassword")
    Call<ForgetPasswordResponse> forgetPasswordApi(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userChangePassword")
    Call<ChangePasswordModel> getChangePassoword(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userCheckIn")
    Call<ResponseModel> checkIn(@Header("Authorization") String token,@Body RequestModel requestModel);
    @GET("getAllBusiness?")
    Call<ResponseModel> bussinessList(@Header("Authorization") String token,@Query("latitude") String lat, @Query("longitude") String longi,
                                      @Query("businessTypeList") String list,
                                      @Query("distance") String distance,@Query("popularity") String popularity);
    @GET("getBusinessDetails?")
    Call<ResponseModel> businessDetails(@Header("Authorization") String token,@Query("storeId")
                                        String Id,@Query("userId")String userid);
    @GET("getToken?")
    Call<ResponseModel> genrateBrainTreeToken(@Header("Authorization") String token,@Query("userId")
                                        String Id,@Query("orderId")String userid);
    @GET("getUserHistory?")
    Call<ResponseModel> historyList(@Header("Authorization") String token,@Query("userId")int userid,@Query("pageNo") String pageNo ,
                                    @Query("limit") String limit);
    @GET("getStoreCatalogue?")
    Call<ResponseModel> checkoutDetails(@Header("Authorization") String token,@Query("storeId")int groupid,@Query("storeId") int storeid);
    @GET("userFilters?")
    Call<ResponseModel> userFilterApi(@Header("Authorization") String token,@Query("userId")String groupid);
    @POST("userEditFilters")
    Call<ResponseModel> editFilterApi(@Header("Authorization") String token,@Body FilterRequestModel requestModel);
    @GET("getUserHistoryDetails?")
    Call<ResponseModel> userCheckoutDetails(@Header("Authorization") String token,@Query("orderId")String groupid);
    @GET("getUserHistoryDetails?")
    Call<ResponseModel> getOrderDetail(@Header("Authorization") String token,@Query("orderId")String orderId,@Query("businessId")String businessId);
    @GET("getAllHotDeals?")
    Call<ResponseModel> getAllHotDeal(@Header("Authorization") String token,@Query("userId")String id);
    @GET("searchByItem?")
    Call<ResponseBusinessTypeModel> searchApi(@Header("Authorization") String token, @Query("searchString") String search, @Query("latitude") String lat, @Query("longitude") String longi,
                                              @Query("businessTypeList") String list,
                                              @Query("distance") String distance, @Query("popularity") String popularit);
    @POST("userSocialLogin")
    Call<ResponseModel> socialSignup(@Header("Authorization") String token,@Body RequestModel requestModel);
    @GET("getStoreCatalogue?")
    Call<ResponseModel> catalogueOfStore(@Header("Authorization") String token,@Query("storeId")String id,@Query("businessId") String businessId);
    @POST("addDealsToCart")
    Call<ResponseModel> addItemToCart(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("addItemToCart")
    Call<ResponseModel> addProductTOCart(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userCheckOut")
    Call<ResponseModel> checkout(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("placeOrder")
    Call<ResponseModel> placeOrder(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userVerify")
    Call<ResponseModel> verifyOtp(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("resendOtp")
    Call<ResponseModel> resendOtp(@Header("Authorization") String token,@Body RequestModel requestModel);
    @GET("getItemFromCart?")
    Call<ResponseModel> basketList(@Header("Authorization") String token,@Query("userId")String userid, @Query("storeId")String id, @Query("businessId") String businessId);
    @GET("getLoyaltyHotDeals?")
    Call<ResponseModel> getLoayltyHotDeals(@Header("Authorization") String token,@Query("userId")String userid);
    @POST("editCart")
    Call<ResponseModel> editCartApi(@Header("Authorization") String token,@Body ShoppingBasketUpdateRequest requestModel);
    @GET("getPendingOrders?")
    Call<ResponseModel> getPendingOredersApi(@Header("Authorization") String token,@Query("userId")String userId,@Query("pageNo") String pageNo ,
                                             @Query("limit") String limit);
    @GET("getPendingOrdersDetails?")
    Call<ResponseModel> getPendingOredersDetailsApi(@Header("Authorization") String token,@Query("orderId")String orderId);
    @GET("getYourLoyalty?")
    Call<ResponseModel> getYourLoyaltyAPi(@Header("Authorization") String token,@Query("userId")String userId);
    @GET("getQuestionnaire?")
    Call<ResponseModel> getQuestionnaireApi(@Header("Authorization") String token,@Query("userId")String userId,@Query("storeId") String storeid ,@Query("businessId")String businessId);
    @POST("customerCompleteOrder")
    Call<ResponseModel> getCustomerCompleteOrderApi(@Header("Authorization") String token,@Body RequestModel requestModel);
    @POST("userAnswer")
    Call<ResponseModel> getUserAnswerAPi(@Header("Authorization") String token,@Body RequestModel requestModel);

    @POST("cancelOrder")
    Call<ResponseModel> getCancelOrderApi(@Header("Authorization") String token,@Body RequestModel requestModel);

    @DELETE("deleteCard?")
    Call<ResponseModel> getDeleteCard(@Header("Authorization") String token,@Query("cardId")String cardId);


    @GET("getUserCardList?")
    Call<JsonResponseCardDetail> getCardDetail(@Header("Authorization") String token, @Query("userId")String userId);


}

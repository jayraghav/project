package com.loyalty.restrofit;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Interceptor;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Request;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    /*http://172.16.40.1:8080/loyalty/*/
    //:http://54.83.7.62:8080/loyalty/userAnswer
   public static final String BASE_URL = "http://54.83.7.62:8080/loyalty/customer/";
 // public static final String BASE_URL = "http://172.16.40.1:8080/loyalty/customer/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }



   /* public static ApiInterface getApiServiceHeader()
    {

        final String basic ="Basic bW9iaWxvaXR0ZTpNb2JpbG9pdHRlMQ==";

        httpClient.addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }
        );


        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(ApiInterface.class);
    }*/








}










